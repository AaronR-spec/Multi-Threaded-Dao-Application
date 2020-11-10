package daocaoop;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aaron Reihill / D00222467
 */
public class MySqlDaoCustomers extends MySqlDao implements CustomerDaoInterface
{

    /**
     *
     * @return
     * @throws DaoException
     */
    @Override
    public Map<Integer, Customer> getAllCustomersBills() throws DaoException 
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Customer c;
        Bill b;
        Map<Integer, Customer> customers = new HashMap();
        List<String> registarionTable = new ArrayList<>();
        Map<String, Integer> vehicleTypes = new HashMap();
        String type, name, address,reg, query;
        int cost;
        Map<Integer, List<Integer>> customersBills = new HashMap();
        try
        {
            con = this.getConnection();
            //gets all vehicle reg from events processing
            query = "SELECT vehicle_registration FROM vehicle_table";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next())
            {
                registarionTable.add(rs.getString("vehicle_registration"));
            }
            
            //gets all the vehicles id
            for (String s : registarionTable)
            {
                
                //add to customer hashmap
                query = "SELECT vehicle_id FROM vehicles WHERE Reg = '"+ s+ "'";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next())
                {
                    query = "SELECT * FROM customer_vehicle WHERE vehicle_id = " +rs.getInt("vehicle_id")+"" ;
                    ps = con.prepareStatement(query);
                    rs2 = ps.executeQuery();
                    while (rs2.next())
                    {

                        // Gets the customer with bills and stores them in a map with a list of there bills
                        int customerId = rs2.getInt("customer_id");
                        int vehicleId = rs2.getInt("vehicle_id");
                        if (!customersBills.containsKey(customerId))
                        {
                            ArrayList<Integer> list = new ArrayList<>();
                            //list.add(customerId);
                            customersBills.put(customerId, list);
                            //customersBills.get(customerId).add(vehicleId);
                        }

                        customersBills.get(customerId).add(vehicleId);
                    }
                }
            }

            // gets the list of all the vehicle types and costs HASHMAP
            query = "SELECT * FROM vehicle_type_cost";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                type = rs.getString("vehicle_type");
                cost = rs.getInt("cost");
                vehicleTypes.put(type, cost);
            }
            // Adds customer detials and creates customer object from it 
            for (int i : customersBills.keySet())
            {
                query = "SELECT DISTINCT * FROM customer_table WHERE customer_id = " + i + "";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next())
                {
                    name = rs.getString("customer_name");
                    address = rs.getString("customer_address");
                    c = new Customer(name, address, i);
                    customers.put(i, c);
                }
                //gets the vehicle info (checks hashmap for cost and creates bill object and adds it to the customer)   
                for (int vehicleId : customersBills.get(i))
                {
                    query = "SELECT * FROM vehicles WHERE vehicle_id = " + vehicleId + "";
                    ps = con.prepareStatement(query);
                    rs = ps.executeQuery();
                    while (rs.next())
                    {
                        type = rs.getString("type");

                        cost = vehicleTypes.get(type);
                        reg = rs.getString("Reg");
                        b = new Bill(cost, reg,type,vehicleId);
                        customers.get(i).addBill(b);
                    }
                }

            }

        }
        catch (SQLException ex)
        {
            throw new DaoException("getAllEvents() " + ex.getLocalizedMessage());
        }

        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (rs2 != null)
                {
                    rs2.close();
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
        return customers;
    }

    /**
     *
     * @param reg
     * @param status
     * @return
     * @throws DaoException
     */
    @Override
    public boolean processEvent(String reg, String status) throws DaoException 
    {
        Boolean processed = false;
        Connection con = null;
        String query;
        int validUpdate;
        try
        {
            con = this.getConnection();
            Statement sqlStatement = con.createStatement();
                    query = "UPDATE events SET status = '"+status+"' WHERE Reg = '"+ reg+"'";
                   validUpdate =  sqlStatement.executeUpdate(query);
                    if(validUpdate != 0)
                    {
                        processed = true;
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
                throw new DaoException("processEvent() " + e.getMessage());
            }
        }
        return processed;
    }
}
