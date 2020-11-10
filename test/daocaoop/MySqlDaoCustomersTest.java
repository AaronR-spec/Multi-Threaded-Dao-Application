package daocaoop;

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author D00222467/Aaron Reihill
 */
public class MySqlDaoCustomersTest
{
    
    public MySqlDaoCustomersTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }
    //valid registarion
    @Test
    public void testProcessEventValidRegistration() throws DaoException
    {
        System.out.println("testProcessEventValidRegistration");
        CustomerDaoInterface dao = new MySqlDaoCustomers();
        boolean expResult = true;
        boolean result;
        result = dao.processEvent("201CN3456","pending");
        assertEquals(expResult, result);
    }
    //Invalid registarion
    @Test
    public void testProcessEventInvalidRegistration() throws DaoException
    {
        System.out.println("testProcessEventInvalidRegistration");
        CustomerDaoInterface dao = new MySqlDaoCustomers();
        boolean expResult = false;
        boolean result;
        result = dao.processEvent("Apple","pending");
        assertEquals(expResult, result);
    }
    //Get Customer Bills (all i can test is if it returns anything, as dao's are not seperated inside method)
    @Test
    public void testGetAllCustomersBills() throws DaoException
    {
        System.out.println("testGetAllCustomersBills");
        CustomerDaoInterface dao = new MySqlDaoCustomers();
        boolean expResult = true;
        Map<Integer, Customer> result;
        result = dao.getAllCustomersBills();
        assertEquals(expResult, result.size() > 0);
    }
}
