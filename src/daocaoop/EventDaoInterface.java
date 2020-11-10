package daocaoop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aaron Reihill / D00222467
 */
public interface EventDaoInterface
{

    /**
     * gets all curretn events in the database
     * @return a list filled with event objects
     * @throws DaoException
     */
    public Map<String, List<Event>> getAllEvents() throws DaoException;

    /**
     * writes to a database with map of event objects
     *
     * @param events
     * @throws DaoException
     */
    public void writeToDatabase(Map<String, ArrayList<Event>> events) throws DaoException;

    /**
     * writes event to database
     * @param event
     * @throws DaoException
     */
    public void writeEventToDatabase(Event event) throws DaoException;

    /**
     * gets all events with reg 
     * @param s
     * @return list of all found results
     * @throws DaoException
     */
    public List<Event> getAllEventsByReg(String s) throws DaoException;

    /**
     *
     * @return list of reg currently in database
     * @throws DaoException
     */
    public List<String> getAllReg() throws DaoException;

    /**
     * gets all events from database since date
     * @param s
     * @return list of all found results
     * @throws DaoException
     */
    public List<Event> getAllEventsSinceDate(String s) throws DaoException;

    /**
     * gets all events between date
     * @param start
     * @param end
     * @return list of found results
     * @throws DaoException
     */
    public List<Event> getAllEventsStartAndEndDate(String start, String end) throws DaoException;
}
