package daocaoop;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author D00222467/Aaron Reihill
 */
public class Client
{

    private static final String SERVER_NOT_RESPONDING = "Server Is Not Responding";

    private static Socket socket;
    private static boolean close = false;
    private static final HashSet<String> VEHICLETABLE = new HashSet<>();
    private static final HashSet<String> BILLINGTABLE = new HashSet<>();
    private static final List<Event> EVENTS = new ArrayList<>();
    private static Map<Integer, Customer> customers = new HashMap<>();
    private static boolean serverAlive = true;

    /**
     * starts the client 
     * @param args
     */
    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        boolean exit = false;
        int option;
        start();
        System.out.println("Java CA6 D00222467-AaronReihill");
        while (!exit)
        {

            System.out.println("\n(0) Exit");
            System.out.println("(1) Hearbeat Request");
            System.out.println("(2) Get Registered Vehicles");
            System.out.println("(3) Display Registered Vehicles");
            System.out.println("(4) Load Toll Events");
            System.out.println("(5) Process Toll Events");
            System.out.println("(6) Close");
            try
            {
                System.out.print("Select: ");
                option = keyboard.nextInt();
                switch (option)
                {
                    case 0:
                        System.out.println("Goodbye...");
                        close();
                        exit = true;
                        break;
                    case 1:
                        heartBeatRequest();
                        break;
                    case 2:
                        getVehicleTable();
                        break;
                    case 3:
                        displayVehicleTable();
                        break;
                    case 4:
                        readEvents("Toll-Events.csv");
                        break;
                    case 5:
                        processEvents();
                        break;
                    case 6:
                        close();
                        break;
                    default:
                        System.err.println("Not An Option");
                }
            }
            catch (InputMismatchException e)
            {
                System.err.println("Not An Option");
                keyboard.next();
            }
        }

    }

    private static void heartBeatRequest()
    {
        if (buildRequest("Heartbeat").equals(""))
        {
            System.out.println(SERVER_NOT_RESPONDING);
        }
    }

    private static void processEvents()
    {
        if (EVENTS.size() > 0 || VEHICLETABLE.size() > 0)
        {
            Map<String, ArrayList<Event>> validEvents = new HashMap<>();
            
            for (Event e : EVENTS)
            {
                if (VEHICLETABLE.contains(e.getReg()))
                {
                    if (validEvents.containsKey(e.getReg().trim()))
                    {
                        validEvents.get(e.getReg()).add(e);
                    }
                    else
                    {
                        ArrayList<Event> list = new ArrayList<>();
                        list.add(e);
                        validEvents.put(e.getReg().trim(), list);
                    }
                }
                else
                {
                    if (eventRequest("RegisterInvalidTollEvent", e))
                    {
                        // removes event from local memory
                        EVENTS.remove(e);
                    }
                }
            }
            for (String s : validEvents.keySet())
            {
                for (Event e : validEvents.get(s))
                {
                    //return true if saved
                    if (eventRequest("RegisterValidTollEvent", e))
                    {
                        // removes event from local memory
                        EVENTS.remove(e);
                    }
                }
            }
        }
        if (EVENTS.size() <= 0)
        {
            System.out.println("There Are No Events To Be Processed");
        }
        else if (VEHICLETABLE.size() <= 0)
        {
            System.out.println("There Are No Registered Vehicles, Please Load Vehicles Before Processing Events");
        }

    }

    /**
     * displays all currently stored vehicles in a table
     */
    public static void displayVehicleTable()
    {
        if (VEHICLETABLE.size() > 0)
        {
            System.out.println("");
            int count = 0;
            int lineSplit = 6;
            String line = "-------------------------------------------------------------------------------------";
            System.out.println("Vehicle Table");
            System.out.println(line);
            for (String s : VEHICLETABLE)
            {
                System.out.print(String.format("%-15s", s, ","));
                count++;
                if (count >= lineSplit)
                {
                    System.out.println("");
                    count = 0;
                }
            }
            System.out.println(line);
        }
        else
        {
            System.out.println("There Are No Registered Vehicles");
        }
    }


    private static void readEvents(String fileName)
    {
        int count = 0;
        Scanner sc = new Scanner("");
        try
        {
            sc = new Scanner(new File(fileName));
            sc.useDelimiter("[;\n\r]+");

            String reg, img, time, boothId;
            while (sc.hasNext())
            {
                boothId = sc.next().trim();
                reg = sc.next().trim();
                img = sc.next().trim();
                time = sc.next().trim();

                Timestamp t = java.sql.Timestamp.from(Instant.parse(time));
                EVENTS.add(new Event(boothId, reg, img, t));
                count++;
            }
            System.out.println(count + " Events Loaded");
        }
        catch (IOException e)
        {
            System.out.println("File Not Found. " + e.getMessage());
        }
        catch (InputMismatchException e)
        {
            System.out.println("Format Is Wrong" + e.getMessage());
        }

        finally
        {
            sc.close();
        }
    }

    /**
     * starts the client on a set port
     */
    public static void start()
    {
        Scanner in = new Scanner(System.in);
        String request;
        String response;
        try
        {
            socket = new Socket("localhost", 50000);

            System.out.println("Client: Port# of this client : " + socket.getLocalPort());
            System.out.println("Client: Port# of Server :" + socket.getPort());
            System.out.println("Client: This Client is running and has connected to the server");

            //buildRequest("Heartbeat");
        }
        catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
    }

    // used to request from the server this is the actual method for the server / client
    private static String request(String command)
    {
        if (serverAlive)
        {
            String response = "";
            OutputStream os;
            try
            {
                os = socket.getOutputStream();
                PrintWriter socketWriter = new PrintWriter(os, true);// true=> auto flush buffers
                socketWriter.println(command);
                Scanner socketReader = new Scanner(socket.getInputStream());
                System.out.println("Client Request: " + command);
                if (!close)
                {
                    response = socketReader.nextLine();
                    System.out.println("Servers response: " + response);
                }
                else
                {
                    socketWriter.close();
                    socketReader.close();
                    socket.close();
                    os.close();
                }
                
            }
            catch (IOException ex)
            {
                System.out.println("Client Exception: " + ex.getLocalizedMessage());
            }
            catch (NullPointerException e)
            {
                serverAlive = false;
                System.out.println(SERVER_NOT_RESPONDING);
                return "";
            }
            catch (NoSuchElementException e)
            {
                System.out.println("MYSQL Database Is Not Responding");
            }
            serverAlive = true;
            return response;
        }
        else
        {

            return "";
        }

    }

    private static void getVehicleTable()
    {
        String json = buildRequest("GetRegisteredVehicles");
        if (!json.equals(""))
        {
            JsonReader reader = Json.createReader(new StringReader(json));

            JsonObject object = reader.readObject();
            JsonArray arry = object.getJsonArray("Vehicles");
            for (int i = 0; i < object.getJsonArray("Vehicles").size(); i++)
            {
                VEHICLETABLE.add(arry.getString(i));
            }
        }
        else
        {
            System.out.println(SERVER_NOT_RESPONDING);
        }
    }

    // builds the request

    /**
     * builds a Json to then is sent to the request method to be sent to the server
     * @param request
     * @return server response
     */
    public static String buildRequest(String request)
    {
        String json = Json.createObjectBuilder()
                .add("PacketType", request)
                .build()
                .toString();
        return request(json);
    }

    /**
     * takes in request and Event object and builds a json string, then send it to request which sends it to the server
     * @param request
     * @param e
     * @return server response
     */
    public static boolean eventRequest(String request, Event e)
    {
        String json = Json.createObjectBuilder()
                .add("PacketType", request)
                .add("ID", e.getBoothId())
                .add("Reg", e.getReg())
                .add("Img", e.getImgId())
                .add("Time", e.getTimestamp().toString())
                .build()
                .toString();

        // servers response
        Boolean processed = false;
        String response = request(json);
        if (request.equalsIgnoreCase("RegisterValidTollEvent"))
        {
            if (response.trim().equalsIgnoreCase("RegisteredValidTollEvent"))
            {
                processed = true;
            }
        }
        else
        {
            if (response.trim().equalsIgnoreCase("RegisteredInvalidTollEvent"))
            {
                processed = true;
            }
        }
        return processed;
    }

    /**
     * close method to close the socket port so that the server stops listening for the client
     */
    public static void close()
    {
        close = true;
        String json = Json.createObjectBuilder()
                .add("PacketType", "Close")
                .build()
                .toString();
        request(json);
    }
}
