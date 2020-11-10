package daocaoop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Aaron Reihill / D00222467
 */
public class MySqlDao
{

    /**
     * Gets the connection from a database
     *
     * @return a connection object
     * @throws DaoException
     */
    public Connection getConnection() throws DaoException
    {

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/java_ca";
        String username = "root";
        String password = "";
        Connection con = null;

        try
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        }
        
        catch (ClassNotFoundException ex1)
        {
            System.err.println("Failed to find driver class " + ex1.getMessage());
            System.exit(1);
        }
        catch (SQLException ex2)
        {
            System.err.println("Connection failed " + ex2.getMessage());
            System.exit(2);
        }
  
        return con;
    }

    /**
     * frees the connection from a database
     *
     * @param con
     * @throws DaoException
     */
    public void freeConnection(Connection con) throws DaoException
    {
        try
        {
            if (con != null)
            {
                con.close();
                con = null;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Failed to free connection: " + e.getMessage());
            System.exit(1);
        }
    }
}
