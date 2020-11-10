package daocaoop;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author D00222467/Aaron Reihill
 */
public class BillingClient
{

    private static final MySqlDaoCustomers CUSTOMERDAO = new MySqlDaoCustomers();
    private static final List<Customer> CUSTOMERLIST = new ArrayList<>();

    /**
     * starts the billing client
     * @param args
     */
    public static void main(String[] args)
    {
        menu();
    }

    /**
     * the billing systems menu, user interface
     */
    public static void menu()
    {
        int option;
        Boolean exit = false;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Java CA6 D00222467-Aaron Reihill");
        while (!exit)
        {

            System.out.println("\n(0) Exit");
            System.out.println("(1) Get Customers Bills");
            System.out.println("(2) Display Customers Bills");
            System.out.println("(3) Process Customer Bills");
            System.out.println("(4) Reset Customer Bills");
            try
            {
                System.out.print("Select: ");
                option = keyboard.nextInt();
                switch (option)
                {
                    case 0:
                        System.out.println("Goodbye...");
                        exit = true;
                        break;
                    case 1:
                        getCustomerBills();
                        break;
                    case 2:
                        displayBills();
                        break;
                    case 3:
                        processesBill();
                        break;
                    case 4:
                        resetBillsStatus();
                        break;
                    default:
                        System.err.println("Not An Option");
                }
            }
            catch (InputMismatchException e)
            {
                System.err.println("Not An Option");
                keyboard.next();
            }
        }

    }

    private static void displayBills()
    {
        if (CUSTOMERLIST.size() > 0)
        {
            if (CUSTOMERLIST.size() > 0)
            {
                for (Customer c : CUSTOMERLIST)
                {
                    c.display();
                }
            }
            else
            {
                System.out.println("There Are No Bills To Display");
            }
        }
        else
        {
            System.out.println("There Are No Bills To Display");

        }
    }

    private static void getCustomerBills()
    {
        Map<Integer, Customer> customersTable;
        try
        {
            customersTable = CUSTOMERDAO.getAllCustomersBills();
            for (int i : customersTable.keySet())
            {
                CUSTOMERLIST.add(customersTable.get(i));

            }
            System.out.println("Bills Loaded...");
        }

        catch (DaoException ex)
        {
            Logger.getLogger(BillingClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (CUSTOMERLIST.size() <= 0)
        {
            System.out.println("No Bills Found");
        }
    }

    private static void processesBill()
    {
        if (CUSTOMERLIST.size() > 0)
        {
            List<Bill> processedBills = new ArrayList<>();
            List<Bill> customersBills;
            List<Customer> emptyCustomers = new ArrayList<>();
            for (Customer c : CUSTOMERLIST)
            {
                customersBills = c.getBills();
                for (Bill b : customersBills)
                {
                    try
                    {
                        if (CUSTOMERDAO.processEvent(b.getVehicleReg(), "Processed"))
                        {
                            processedBills.add(b);
                        }
                    }
                    catch (DaoException ex)
                    {
                        Logger.getLogger(BillingClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                for (Bill b : processedBills)
                {
                    customersBills.remove(b);
                }
                if (customersBills.size() <= 0)
                {
                    emptyCustomers.add(c);
                }
            }
            for (Customer c : emptyCustomers)
            {
                CUSTOMERLIST.remove(c);
            }
            System.out.println("Bills Processed");
        }
        else
        {
            System.out.println("There Are No Bills To Process, Please Load Them In Before Trying To Process");
        }

    }

    private static void resetBillsStatus()
    {
        if (CUSTOMERLIST.size() > 0)
        {
            List<Bill> customersBills;
            for (Customer c : CUSTOMERLIST)
            {
                customersBills = c.getBills();
                for (Bill b : customersBills)
                {
                    try
                    {
                        CUSTOMERDAO.processEvent(b.getVehicleReg(), "pending");

                    }
                    catch (DaoException ex)
                    {
                        Logger.getLogger(BillingClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            System.out.println("Bills Status Reset To 'Pending'");
        }
        else
        {
            System.out.println("There Are No Bills To Reset, Please Load Them In Before Trying To Reset");
        }
    }
}
