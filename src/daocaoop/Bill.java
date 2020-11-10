
package daocaoop;

/**
 *
 * @author D00222467/Aaron Reihill
 */
public class Bill
{
    double cost; 
    String vehicleReg,type;
    int id;

    /**
     * bill costructor 
     * @param cost
     * @param vehicleReg
     * @param type
     * @param id
     */
    public Bill(double cost, String vehicleReg,String type, int id)
    {
        this.cost = cost;
        this.vehicleReg = vehicleReg;
        this.type = type;
        this.id = id;
    }

    /**
     * 
     * @return type
     */
    public String getType()
    {
        return type;
    }

    /**
     *
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     *
     * @return cost
     */
    public double getCost()
    {
        return cost;
    }

    /**
     *
     * @param cost
     */
    public void setCost(int cost)
    {
        this.cost = cost;
    }

    /**
     *
     * @return reg
     */
    public String getVehicleReg()
    {
        return vehicleReg;
    }

    /**
     *
     * @param vehicleReg
     */
    public void setVehicleReg(String vehicleReg)
    {
        this.vehicleReg = vehicleReg;
    }

    /**
     *
     * @param cost
     */
    public void setCost(double cost)
    {
        this.cost = cost;
    }

    /**
     *
     * @param type
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     *
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }
    
    /**
     * displays the bills values
     */
    public void display()
    {
    System.out.print(String.format("%-15s%-15s%-15s\u20ac%-15s\n",this.getId(), this.getVehicleReg(),this.getType(),this.getCost()));
    
    }
    @Override
    public String toString()
    {
        return "Bill{" + "cost=" + cost + ", vehicleReg=" + vehicleReg + '}';
    }
    
}
