package daocaoop;

import java.util.Map;

/**
 *
 * @author Aaron Reihill / D00222467
 */
public interface CustomerDaoInterface
{

    /**
     * gets all the customers with bills from the server, in a map with there id as a key
     * @return map of all customers 
     * @throws DaoException
     */
    public Map<Integer, Customer> getAllCustomersBills() throws DaoException;

    /**
     * takes in the events reg and what status to change it too
     * @param reg
     * @param status
     * @return true if processed and false if not
     * @throws DaoException
     */
    public boolean processEvent(String reg, String status) throws DaoException;
}
