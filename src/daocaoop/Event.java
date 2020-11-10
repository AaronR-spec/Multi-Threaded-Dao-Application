package daocaoop;

import java.sql.Timestamp;

/**
 *
 * @author Aaron Reihill / D00222467
 */
public class Event
{
    private String reg, imgId,boothId;
    private Timestamp timestamp;

    /**
     * constructor
     *
     * @param boothId
     * @param reg
     * @param imgId
     * @param timestamp
     */
    public Event(String boothId,String reg, String imgId, Timestamp timestamp)
    {
        this.boothId = boothId;
        this.reg = reg;
        this.imgId = imgId;
        this.timestamp = timestamp;
    }

    /**
     *
     * @return boothId 
     */
    public String getBoothId()
    {
        return boothId;
    }
    /**
     *
     * @return objects registration
     */
    public String getReg()
    {
        return reg;
    }

    /**
     *
     * @return objects image id
     */
    public String getImgId()
    {
        return imgId;
    }

    /**
     *
     * @return objects timestamp
     */
    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    /**
     *
     * @param setBoothId
     */
    public void setBoothId(String setBoothId)
    {
        this.reg = setBoothId;
    }
    /**
     * sets the objects registration
     *
     * @param reg
     */
    public void setReg(String reg)
    {
        this.reg = reg;
    }

    /**
     * sets the image id
     *
     * @param imgId
     */
    public void setImgId(String imgId)
    {
        this.imgId = imgId;
    }

    /**
     * sets the objects timestamp
     *
     * @param timestamp
     */
    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "Event{" + "boothId=" +  this.getBoothId() + ","+"reg=" +  this.getReg() + ", imgId=" +  this.getImgId() + ", timestamp=" +  this.getTimestamp() + '}';
    }

}
