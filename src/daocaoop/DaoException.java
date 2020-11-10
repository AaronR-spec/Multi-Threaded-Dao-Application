package daocaoop;

import java.sql.SQLException;

/**
 *
 * @author Aaron Reihill / D00222467
 */
public class DaoException extends SQLException
{

    /**
     * empty constructor
     */
    public DaoException()
    {
    }

    /**
     * Custom checked exception to be thrown whenever a dao function is used
     *
     * @param aMessage
     */
    public DaoException(String aMessage)
    {
        super(aMessage);
    }
}
