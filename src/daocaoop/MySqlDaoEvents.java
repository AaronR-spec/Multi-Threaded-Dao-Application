package daocaoop;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aaron Reihill / D00222467
 */
public class MySqlDaoEvents extends MySqlDao implements EventDaoInterface
{

    /**
     * writes the map of event objects to the database
     *
     * @param events
     * @throws DaoException
     */
    @Override
    public void writeToDatabase(Map<String, ArrayList<Event>> events) throws DaoException
    {
        Connection con = null;
        String query;
        try
        {
            ArrayList<Event> list;
            con = this.getConnection();
            Statement sqlStatement = con.createStatement();
            for (String s : events.keySet())
            {
                list = events.get(s);
                for (Event e : list)
                {
                    query = "INSERT INTO Events(BoothId,Reg,Img,timestamp)"
                            + "\nVALUES ('" + e.getBoothId()+ "', '"+ e.getReg() + "', '" + e.getImgId() + "',Timestamp('" + e.getTimestamp() + "'));\n";
                    sqlStatement.execute(query);
                }
            }
        }
        catch (SQLException e)
        {
            if (e instanceof MySQLIntegrityConstraintViolationException)
            {
            }
            else
            {
                throw new DaoException("getAllVehicleReg() " + e.getMessage());
            }
        }

        finally
        {
            try
            {

                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getAllVehicleReg() " + e.getMessage());
            }
        }

    }
    /**
     * writes the map of event objects to the database
     *
     * @param event
     * @throws DaoException
     */
    @Override
    public void writeEventToDatabase(Event event) throws DaoException
    {
        Connection con = null;
        String query;
        try
        {
            con = this.getConnection();
            Statement sqlStatement = con.createStatement();
                    query = "INSERT INTO Events(BoothId,Reg,Img,timestamp)"
                            + "\nVALUES ('" + event.getBoothId()+ "', '"+ event.getReg() + "', '" + event.getImgId() + "',Timestamp('" + event.getTimestamp() + "'));\n";
                    sqlStatement.execute(query);
        }
        catch (SQLException e)
        {
            if (e instanceof MySQLIntegrityConstraintViolationException)
            {
            }
            else
            {
                throw new DaoException("getAllVehicleReg() " + e.getMessage());
            }
        }

        finally
        {
            try
            {

                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getAllVehicleReg() " + e.getMessage());
            }
        }

    }
    /**
     * Gets all currently stored events in the database
     *
     * @return all the event objects in database as a list
     * @throws DaoException
     */
    @Override
    public Map<String, List<Event>> getAllEvents() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2;
        ResultSet rs = null;
        ResultSet rs2;
        Map<String, List<Event>> events = new HashMap();

        try
        {
            con = this.getConnection();

            String query = "SELECT Reg FROM Events";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next())
            {
                String registration = rs.getString("Reg");
                String query2 = "SELECT * FROM Events WHERE '" + registration + "'";
                ps2 = con.prepareStatement(query2);
                rs2 = ps2.executeQuery();
                events.put(registration, new ArrayList<>());
                while (rs2.next())
                {
                    String boothId = rs2.getString("BoothId");
                    String reg = rs2.getString("Reg");
                    String img = rs2.getString("Img");
                    String time = rs2.getString("timestamp");
                    events.get(registration).add(new Event(boothId, reg, img, Timestamp.valueOf(time)));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getAllEvents() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getAllEvents() " + e.getMessage());
            }
        }
        return events;
    }

    /**
     * gets all events by reg
     * @param s
     * @return list of found results
     * @throws DaoException
     */
    @Override
    public List<Event> getAllEventsByReg(String s) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Event> events = new ArrayList<>();

        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM Events WHERE Reg = '" + s + "'";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next())
            {
                String boothId = rs.getString("BoothId");
                String reg = rs.getString("Reg");
                String img = rs.getString("Img");
                String time = rs.getString("timestamp");
                events.add(new Event(boothId,reg, img, Timestamp.valueOf(time)));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getAllEvents() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getAllEvents() " + e.getMessage());
            }
        }
        return events;
    }

    /**
     * gets all reg in database
     * @return list of all found results
     * @throws DaoException
     */
    @Override
    public List<String> getAllReg() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> registered = new ArrayList<>();

        try
        {
            con = this.getConnection();

            String query = "SELECT DISTINCT Reg FROM Events ";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next())
            {
                String reg = rs.getString("Reg");
                registered.add(reg);
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getAllEvents() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getAllEvents() " + e.getMessage());
            }
        }
        Collections.sort(registered);
        return registered;
    }

    /**
     * gets all events since set date
     * @param s
     * @return list of found results
     * @throws DaoException
     */
    @Override
    public List<Event> getAllEventsSinceDate(String s) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Event> events = new ArrayList<>();
        Timestamp t = Timestamp.valueOf(s);
        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM Events WHERE timestamp >= '" + t + "'";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next())
            {
                String boothId = rs.getString("BoothId");
                String reg = rs.getString("Reg");
                String img = rs.getString("Img");
                String time = rs.getString("timestamp");
                events.add(new Event(boothId,reg, img, Timestamp.valueOf(time)));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getAllEvents() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getAllEvents() " + e.getMessage());
            }
        }
        return events;
    }

    /**
     * gets all events between set dates
     * @param start
     * @param end
     * @return list of all results found 
     * @throws DaoException
     */
    @Override
    public List<Event> getAllEventsStartAndEndDate(String start, String end) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Event> events = new ArrayList<>();
        Timestamp timeStart = Timestamp.valueOf(start);
        Timestamp timeEnd = Timestamp.valueOf(end);
        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM Events WHERE timestamp >= '" + timeStart + "' And timestamp <='" + timeEnd + "'";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next())
            {
                String boothId = rs.getString("BoothId");
                String reg = rs.getString("Reg");
                String img = rs.getString("Img");
                String time = rs.getString("timestamp");
                events.add(new Event(boothId,reg, img, Timestamp.valueOf(time)));
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getAllEvents() " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            }
            catch (SQLException e)
            {
                throw new DaoException("getAllEvents() " + e.getMessage());
            }
        }
        return events;
    }
}
