/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daocaoop;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author D00222467/Aaron Reihill
 */
public class Customer
{
    private String name,address;
    private int id;
    private List<Bill> bills = new ArrayList<>();

    /**
     * Customer constructor
     * @param name
     * @param address
     * @param id
     */
    public Customer(String name, String address, int id)
    {
        this.name = name;
        this.address = address;
        this.id = id;
    }

    /**
     * 
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address)
    {
        this.address = address;
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
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     *
     * @return list of all bills owned by customer
     */
    public List<Bill> getBills()
    {
        return bills;
    }

    /**
     *
     * @param bills
     */
    public void setBills(List<Bill> bills)
    {
        this.bills = bills;
    }
    
    /**
     *
     * @return gets sum of total bill costs 
     */
    public double getTotalBill()
    {
        double total = 0;
        for(Bill b: this.bills)
        {
            total += b.getCost();
        }
        return total;
    }

    /**
     * adds bill to customers list of bills
     * @param b
     * @return true if added. false if not added
     */
    public boolean addBill(Bill b)
    {
        return this.bills.add(b);
    }

    /**
     * takes in string reg and removes bill with said reg 
     * @param reg
     * @return true if removed, false if not removed
     */
    public boolean removeBill(String reg)
    {   
        for(int i = 0; i < this.bills.size(); i++)
        {
            if(this.bills.get(i).getVehicleReg().equalsIgnoreCase(reg))
            {
               this.bills.remove(i);
            }
        }
        return false;
    }

    /**
     * takes in bill object and removes it from customers list of bills
     * @param b
     * @return true if removed, false if not removed
     */
    public boolean removeBill(Bill b)
    {   
           return this.getBills().remove(b);
    }

    /**
     * displays the customers details and all bills they have
     */
    public void display()
    {
        System.out.println("\nCustomer: "+ this.name );
        System.out.println("Address: " + this.address);
        System.out.println("--------------------------------------------------");
         System.out.print(String.format("%-15s%-15s%-15s%-15s","Id", "Registation","Type","Cost"));
         System.out.println("");
        for (Bill b : this.bills)
        {
            b.display();
        }
        System.out.println("--------------------------------------------------");
        System.out.println("\nTotal Cost: \u20ac" +this.getTotalBill()  );
        System.out.println("------------------");
        System.out.println("");
    }

    @Override
    public String toString()
    {
        return "Customer{" + "name=" + name + ", address=" + address + ", id=" + id + ", bills=" + bills + '}';
    }
    
}
