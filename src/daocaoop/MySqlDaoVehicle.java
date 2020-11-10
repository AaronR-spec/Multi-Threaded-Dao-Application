package daocaoop;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Aaron Reihill / D00222467
 */
public class MySqlDaoVehicle extends MySqlDao implements VehicleDaoInterface
{

    /**
     * Gets all the vehicles registration currently stored in the database
     *
     * @return list of String containing all the vehicle registration
     * @throws DaoException
     */
    @Override
    public HashSet<String> getAllVehicleReg() throws DaoException 
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashSet<String> vehicle = new HashSet<>();

        try
        {
            con = this.getConnection();

            String query = "SELECT Reg FROM Vehicles";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next())
            {
                String reg = rs.getString("Reg");
                vehicle.add(reg);
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("getAllVehicleReg() " + e.getMessage());
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
                throw new DaoException("getAllVehicleReg() " + e.getMessage());
            }
        }
        return vehicle;
    }

    /**
     * Writes a hash set of Strings to the database, used to store vehicle
     * registration in the database
     *
     * @param table
     * @throws DaoException
     */
    @Override
    public void writeToDatabase(HashSet<String> table) throws DaoException
    {
        Connection con = null;
        String query;
        try
        {
            con = this.getConnection();
            Statement sqlStatement = con.createStatement();
            for (String s : table)
            {

                query = "INSERT INTO Vehicles(Reg)"
                        + "\nVALUES ('" + s + "');\n";
                sqlStatement.execute(query);

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
     * writes vehicle reg to database 
     * @param vehicleReg
     * @throws DaoException
     */
    @Override
    public void writeToVehicleTable(String vehicleReg) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query,reg,type;
        try
        {
            con = this.getConnection();
            Statement sqlStatement = con.createStatement();
            con = this.getConnection();
            
            
            query = "SELECT * FROM Vehicles WHERE Reg = '" +vehicleReg+"'";
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();
            while (rs.next())
            {
                reg = rs.getString("Reg");
                type = rs.getString("type");
                 query = "INSERT INTO vehicle_table(vehicle_registration, vehicle_type)"
                        + "\nVALUES ('" + reg + "', '"+type + "');\n";
                sqlStatement.execute(query);
            
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
}
