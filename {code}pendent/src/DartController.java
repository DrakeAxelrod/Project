import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DartController {

    //opening child classes
    private static Employee employee = new Employee();
    private static Customer customer = new Customer();
    private static Game game = new Game();
    private static Rental rental = new Rental();
    private static Helper helper = new Helper();
    private static Menus menu = new Menus();
    private static Album album = new Album();


    // "kind of" Storage
    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<Game> games = new ArrayList<>();
        /*(Arrays.asList(new Game( "Sonic: The Hedgehog", "Explore", 23, false),
            new Game( "Crash Bandicoot", "Racing", 24, false),
            new Game( "The Legend of Zelda", "Explore", 51, true),
            new Game( "Prince of Persia", "Impossible", 33, false),
            new Game( "Super Mario", "Classic", 32, false),
            new Game( "Street Fighter", "Fighting", 54, false),
            new Game( "Tekken", "Fighting", 29, false)
    ));
    */

    private ArrayList<Customer> customerList = new ArrayList<>();
    /*(Arrays.asList(new Customer(1,"Vernita"),
            new Customer(2,"Navya"), new Customer(3,"Drake"),
            new Customer(4,"Altan"),  new Customer(5,"Axel")
    ));
*/
    public ArrayList<Employee> getEmployees(){return employees;}
    public ArrayList<Customer> getCustomers() {
        return customerList;
    }

    public ArrayList<Game> getGames(){
        return games;
    }
    public Customer getCustomer(){
        return customer;
    }

    public void setCustomer(){
        this.customer = customer;
    }
    //Game
    public void rentGame(){
        rental.rentGame();
    }
    //Customer

    public void addOneCustomer(){
       this.customerList.add(customer.addCustomer());
       System.out.println(customerList.toString());
   }

    public void removeCustomer(){
       int removeId = helper.getInt("ID of customer to remove: ");
       this.customerList.removeIf(customer -> customer.getCustomerId() == removeId);
       viewCustomer();
   }
     public void viewCustomer(){
        for (Customer customer : customerList) {
            System.out.println(customer.toString());
        }
    }

    public void addMembership(){
        this.customer.addMembership();
    }


    // Talking to employee
    //--------------------------------------------------------------------------//
    public void addEmployee(){
        this.employees.add(employee.addEmployee());
        menu.managerMenu();
    }
    public void removeEmployee() {
        String check = helper.getInput("ID: ");
        removeEmployee();
        employees.remove(employee);
    }
    public void viewEmployee(){
        for (Employee employee : employees){
            System.out.println(employee.toString());
        }
    }
    //--------------------------------------------------------------------------//

    public void viewAlbums() {
        for (Album album : albums) {
            System.out.println(album.toString());
        }
    }

    public void addAlbum(){
        this.albums.add(album.addAlbum());
        menu.employeeMenu();
    }

    // Default constructor
    public void run() {
        menu.mainMenu();
        }


}