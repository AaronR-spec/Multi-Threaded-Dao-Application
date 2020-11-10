package daocaoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 *@author D00222467/Aaron Reihill
 */
public class Server
{

    private HashSet<String> table = new HashSet<>();
    private final MySqlDaoEvents eventDao = new MySqlDaoEvents();
    private final MySqlDaoVehicle vehicleDao = new MySqlDaoVehicle();
    private final List<Event> invalid = new ArrayList<>();

    /**
     * main of server
     * @param args
     */
    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    /**
     * starts the server making it open and listening for client request/ ports
     */
    public void start()
    {

        MySqlDaoVehicle v = new MySqlDaoVehicle();
        try
        {
            ServerSocket ss = new ServerSocket(50000);  // set up ServerSocket to listen for connections on port 8080

            System.out.println("Server: Server started. Listening for connections on port 50000...");

            int clientNumber = 0;  // a number for clients that the server allocates as clients connect
            table = v.getAllVehicleReg();
            while (true)    // loop continuously to accept new client connections
            {
                Socket socket = ss.accept();    // listen (and wait) for a connection, accept the connection, 
                // and open a new socket to communicate with the client
                clientNumber++;

                System.out.println("Server: Client " + clientNumber + " has connected.");

                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                Thread t = new Thread(new ClientHandler(socket, clientNumber)); // create a new ClientHandler for the client,
                t.start();                                                  // and run it in its own thread

                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
        }
        catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        catch (DaoException e)
        {
            System.out.println("Server: DaoException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }

    /**
     * each client gets a clienthandler which handles clients requests and anything involving them
     */
    public class ClientHandler implements Runnable   // each ClientHandler communicates with one Client
    {

        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket socket;
        int clientNumber;

        /**
         * takes in socket number and client number 
         * @param clientSocket
         * @param clientNumber
         */
        public ClientHandler(Socket clientSocket, int clientNumber)
        {
            try
            {
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);
                OutputStream os = clientSocket.getOutputStream();
                this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer

                this.clientNumber = clientNumber;  // ID number that we are assigning to this client

                this.socket = clientSocket;  // store socket ref for closing 

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            String message, request;
            try
            {
                while ((message = socketReader.readLine()) != null)
                {
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + message);
                    JsonReader reader = Json.createReader(new StringReader(message));

                    JsonObject object = reader.readObject();

                    request = object.getJsonString("PacketType").getString().trim();
                    if (request.equalsIgnoreCase("Heartbeat"))
                    {
                        socketWriter.println(buildResponse("Heartbeat response"));
                    }
                    else if (request.equalsIgnoreCase("GetRegisteredVehicles"))
                    {
                        socketWriter.println(buildJsonArray(new ArrayList<>(table)));
                    }
                    else if (request.equalsIgnoreCase("RegisterValidTollEvent"))
                    {
                        Event e = buildEvent(object);
                        vehicleDao.writeToVehicleTable(e.getReg());
                        eventDao.writeEventToDatabase(e);
                        socketWriter.println(buildResponse("RegisteredValidTollEvent"));
                    }
                    else if (request.equalsIgnoreCase("RegisterInvalidTollEvent"))
                    {
                        Event e = buildEvent(object);
                        invalid.add(e);
                        socketWriter.println(buildResponse("RegisteredInvalidTollEvent"));
                    }
                    else if (request.equalsIgnoreCase("Close"))
                    {
                        System.out.println("Closing the client");
                        displayInvalid();
                        break;
                    }
                    else
                    {
                        socketWriter.println("I'm sorry I don't understand :(");
                    }

                }
                
                socket.close();
            }
            catch (IOException | InputMismatchException ex)
            {
                System.out.println("Error " + ex.getLocalizedMessage());
            }
            catch (DaoException ex)
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }

        /**
         * builds event from Json object
         * @param object
         * @return
         */
        public Event buildEvent(JsonObject object)
        {
            String boothId = object.getJsonString("ID").getString().trim();
            String reg = object.getJsonString("Reg").getString().trim();
            String img = object.getJsonString("Img").getString().trim();
            String time = object.getJsonString("Time").getString().trim();
            return new Event(boothId, reg, img, java.sql.Timestamp.valueOf(time));
        }

        /**
         * builds a Json response to be sent to the client
         * @param response
         * @return
         */
        public String buildResponse(String response)
        {
            String json = Json.createObjectBuilder()
                    .add("PacketType", response)
                    .build()
                    .toString();
            return json;
        }

        private void displayInvalid()
        {
            System.out.println("\nInvalid Toll Events");
            System.out.print(String.format("\n%-15s%-15s%-15s%-15s", "Tool Booth Id", "Registration", "Image", "Timestamp"));
            System.out.println("\n-------------------------------------------------------");
            for (Event e : invalid)
            {
                System.out.println(String.format("%-15s%-15s%-15s%-15s", e.getBoothId(), e.getReg(), e.getImgId(), e.getTimestamp()));
            }
        }

        /**
         * builds the vehicle json string to be sent to the client on request
         * @param list
         * @return Json string of vehicles
         */
        public String buildJsonArray(List<String> list)
        {
            JsonBuilderFactory factory = Json.createBuilderFactory(null);
            JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

            for (String s : list)
            {
                arrayBuilder.add(s);
            }
            JsonArray jsonArray = arrayBuilder.build();

            JsonObject jsonRootObject
                    = Json.createObjectBuilder()
                            .add("Vehicles", jsonArray)
                            .build();
            return jsonRootObject.toString();
        }
    }

}
