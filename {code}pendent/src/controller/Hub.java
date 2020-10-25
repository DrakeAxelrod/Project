package controller;

import exceptions.InvalidInputException;
import items.*;
import items.properties.*;
import people.*;
import people.features.*;
import people.features.membership.*;
import tools.Input;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Hub {

    private final Customer customer = new Customer();
    private final RentalTransaction transaction = new RentalTransaction();
    private final ArrayList<Employee> employees = new ArrayList<>();
    private final ArrayList<Customer> customerList = new ArrayList<>();
    private final List<Inventory> inventory = new ArrayList<>();
    private final HashMap<String, Membership> membershipRequests = new HashMap<>();
    private final ArrayList<RentalTransaction> rentalHistory = new ArrayList<>();
    private final Input input = Input.getInstance();

    public Hub() {
    }

    private ArrayList<RentalTransaction> getRentalHistory() {
        return rentalHistory;
    }

    protected void readFile(){
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader(new File("{code}pendent/src/db.txt")));
            while((line = br.readLine()) != null) {
                String[] token = line.split(";");
                switch (token[0].toLowerCase()) {
                    case "employee" -> employees.add(new Employee(token[1], token[2], Integer.parseInt(token[3]), token[4], Double.parseDouble(token[5])));
                    case "customer" -> customerList.add(new Customer(token[1], token[2]));
                    case "game" -> inventory.add(new Game(token[1], token[2], Double.parseDouble(token[3]), Integer.parseInt(token[4]), Boolean.parseBoolean(token[5]), LocalDate.of(Integer.parseInt(token[6]), Integer.parseInt(token[7]), Integer.parseInt(token[8]))));
                    case "album" -> inventory.add(new Album(token[1], token[2], Integer.parseInt(token[3]), Double.parseDouble(token[4]), Boolean.parseBoolean(token[5]), LocalDate.of(Integer.parseInt(token[6]), Integer.parseInt(token[7]), Integer.parseInt(token[8]))));
                    default -> System.out.println("broken dont get here...");
                }
            }
            br.close();
        } catch (InvalidInputException | IOException e) {
            e.printStackTrace();
        }
    }

    private void exportTransaction(RentalTransaction transaction){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter((new File("{code}pendent/src/transactions.txt")), true));
            String newTransaction = transaction.getCustomerId()+";"+transaction.getItemId()+";"+transaction.getTitle()+";"+transaction.getRentExpense();
            bw.write(newTransaction + Input.EOL);
            bw.close();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    protected void itemsByProfit() {
        rentalHistory.sort(Comparator.comparingDouble(RentalTransaction::getRentExpense));
        Collections.reverse(rentalHistory);
        rentalHistory.forEach(System.out::println);
    }

    protected void bestCustomer() {
        customerList.sort(Comparator.comparingDouble(Customer::getSpentMoney));
        Collections.reverse(customerList);
        Customer bestCustomer = customerList.get(0);
        if (bestCustomer.getSpentMoney() > 0){
            System.out.println("Name: " + bestCustomer.getName() + Input.EOL + "Total rental expense: " + Math.round(bestCustomer.getSpentMoney()*100)/100);
        }
    }

    protected void rentalFrequency() {
        HashMap<String, Integer> rentalFrequency = new HashMap<>();
        for (Inventory item : inventory) {
            int rentalTimes = 0;
            for (RentalTransaction rental : rentalHistory) {
                if (item.getId().equals(rental.getItemId())) {
                    rentalTimes = +1;
                }
            }
            if (rentalTimes != 0) {
                rentalFrequency.put(item.getTitle(), rentalTimes);
            }
        }
        for (String key : rentalFrequency.keySet()) {
            System.out.println("Title: " + key + Input.EOL + "Times rented: " + rentalFrequency.get(key) + Input.EOL);
        }
    }

    private RentalTransaction askRating(String customerId, Inventory rentedItem, double userBill) {
        RentalTransaction rentTransaction = null;
        Rating customerRating;
        String ratingQuestion = input.getInput("We hope you enjoyed " + rentedItem.getTitle() + ". Would you like to rate it? Y/N ");
        if (ratingQuestion.equalsIgnoreCase("n")) {
            rentTransaction = new RentalTransaction(customerId, rentedItem.getId(), rentedItem.getTitle(), userBill);
        } else if (ratingQuestion.equalsIgnoreCase("y")) {
            customerRating = customerRating(rentedItem);
            rentTransaction = new RentalTransaction(customerId, rentedItem.getId(), rentedItem.getTitle(), userBill, customerRating);
            rentedItem.getRatingSet().add(customerRating);
        }
            return rentTransaction;
    }

    private Rating customerRating(Inventory rentedItem) {
        String feedback = null;
        int rating = 0;
        rating = input.getInt("How would you rate it on a scale of 0-5? ");
        if (rating > 5) {
            rating = 5;
        } else if (rating < 0) {
            rating = 0;
        }
        String feedbackQuestion = input.getInput("Would you like to leave a review? Y/N: ");
        if (feedbackQuestion.equalsIgnoreCase("y")) {
            feedback = input.getInput("How did you experience " + rentedItem.getTitle() + "?" + Input.EOL + "Do you have any advice for other players? or did you kind of just suck at it...");
            System.out.println("Thank you for your feedback!");
            return new Rating(rating, feedback);
        } else {
            System.out.println("Thank you for your feedback!");
            return new Rating(rating);
        }
    }

    private Inventory retrieveItem(String rentId) {
        Inventory searchedItem;
        Iterator<Inventory> searching = inventory.iterator();
        while(searching.hasNext()){
            Inventory current = searching.next();
            if(current.getId().equals(rentId)){
                searchedItem = current;
                return searchedItem;
            }
        }
        System.out.println("That item doesn't exist on our database.");
        return null;
    }

    private Customer retrieveCustomer(String name) {
        Customer user = null;
        Iterator<Customer> searching = customerList.iterator();
        while(searching.hasNext() && user == null){
            Customer currentCustomer = searching.next();
            if(currentCustomer.getName().equalsIgnoreCase(name)){
                user = currentCustomer;
                return user;
            }
        }
        System.out.println("That customer doesn't exist on our database.");
        return null;
    }

    protected void viewTransactions() {
        for (RentalTransaction rental : getRentalHistory()) {
            System.out.println(rental);
        }
    }

    protected void totalProfit() {
        double profit = transaction.getRentalIncome();
        System.out.println("The total profit is " + profit + " kr" + Input.EOL);
    }

    protected void addCustomer() {
        try {
            String name = input.getInput("Enter the Customers Name: ");
            customerList.add(new Customer("", name));
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }
    }

    protected void removeCustomer() {
        viewCustomer();
        String removeId = input.getInput("Enter the ID of the customer you want to remove. " + Input.EOL + "ID: ");
        this.customerList.removeIf(customer -> customer.getId().equals(removeId));
    }

    protected void viewCustomer() {
        for (Customer customer : customerList) {
            System.out.println(customer.toString());
        }
    }

    protected void membershipRequestList() {
        for (String key : membershipRequests.keySet()) {
            String choice = input.getInput((key + " has requested a membership upgrade" + Input.EOL + "Approve (Y/N): "));
            if (choice.equalsIgnoreCase("y")) {
                Customer customer = retrieveCustomer(key);
                String membership = customer.getMembershipType();
                switch (membership) {
                    case "Regular" -> {
                        customer.setMembership(new Silver());
                        System.out.println(key + " has been upgraded to Silver Membership" + Input.EOL);
                    }
                    case "Silver" -> {
                        customer.setMembership(new Gold());
                        System.out.println(key + " has been upgraded to Gold Membership" + Input.EOL);
                    }
                    case "Gold" -> {
                        customer.setMembership(new Platinum());
                        System.out.println(key + " has been upgraded to Platinum Membership" + Input.EOL);
                    }
                    default -> System.out.print("Membership is not valid for upgrade");
                }
            } else {
                        customer.getInbox().add(new Message("Recent Membership Request",
                                "Your membership request at this time has been reviewed and unfortunately at this time has been denied." + Input.EOL,
                                "Management",
                                "DART"));
            }
        }
        membershipRequests.clear();
    }

    protected void requestMembership(){
        String name = input.getInput("Customer Name: ");
        Customer user = retrieveCustomer(name);
            if (user != null){
                if (user.getMembershipType().equals("Regular")){
                    membershipRequests.put(user.getName(), user.getMembership());
                    System.out.print("Request for Silver Membership has been submitted for review."+ Input.EOL);
                }
            }
    }

    protected void upgradeMembership(){
        String name = input.getInput("Customer Name: ");
        Customer user = retrieveCustomer(name);
        if (user != null){
            if (user.getMembershipType().equals("Regular")) {
                System.out.println("This customer does not seem to have a membership try requesting one.");
            } else if (user.getMembershipType().equalsIgnoreCase("Platinum")) {
                System.out.println("Platinum Members cannot upgrade further.");
            } else {
                membershipRequests.put(user.getName(), customer.getMembership());
                System.out.println("Your application for a membership upgrade has been submitted for review." + Input.EOL + "You will receive a message in your inbox as soon as the review is done.");
            }
        }
    }

    protected void addEmployee() {
        boolean isRunning = false;
        do {
            try {
                String name = input.getInput("Name: ");
                int birthYear = input.getInt("Birth year: ");
                String address = input.getInput("Address: ");
                double monthlySalary = input.getDouble("Monthly salary: ");
                double grossSalary = monthlySalary * Employee.MONTHS;
                employees.add(new Employee("", name, birthYear, address, grossSalary));
                isRunning = true;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
                System.out.println(">> Press \u001B[31m'Enter'\u001B[0m to try again: ");
                Input.input.nextLine();

            }
        } while (!isRunning);
    }

    protected void removeEmployee() {
        String removeID = input.getInput("Enter the ID of the employee you want to remove." + Input.EOL + "Employee ID: ");
        this.employees.removeIf(employee -> employee.getId().equals(removeID));
        System.out.println("Employee has been deleted of the face of the earth!" + Input.EOL);
    }

    protected void viewEmployee() {
        for (Employee employee : employees) {
            System.out.println(employee.toString());
        }
    }

    protected void addAlbum() {
        String userInput = "";
        do {
            try {
                String addTitle = input.getInput( "Title: ");
                String addArtist = input.getInput("Artist: ");
                int addYear = input.getInt("Year: ");
                double addDailyRent = input.getDouble("Daily Rent amount: ");
                this.inventory.add(new Album(addTitle, addArtist, addYear, addDailyRent));
            } catch (Exception exception){
                System.out.println(Input.EOL + exception.getMessage());
                userInput = input.getInput("Would you like to try again? Y/N: ");
            }
        } while (userInput.equalsIgnoreCase("y"));
    }

    protected void removeAlbum() {
        String removeID = input.getInput("Remove." + Input.EOL + "Album ID: ");
        Inventory toRemove = retrieveItem(removeID);
        if (toRemove != null){
            inventory.removeIf(album -> album.getId().equals(removeID));
            System.out.println("Album has been sent to the moon and is no longer retrievable!" + Input.EOL);
        }
    }


    protected void rentAlbum() {
        String maxRentals = "You have reached your current limit on rentals. It's great that you enjoy our products so much!";
        String user = input.getInput("Please enter your name: ");
        Customer customer = retrieveCustomer(user);
        if (user != null) {
                if (customer.canRent()) {
                    viewAlbums();
                    String rentId = input.getInput("Hi " + customer.getName() + ". Which album would you like to rent?" + Input.EOL + "ID: ");
                    Inventory albumToRent = retrieveItem(rentId);
                    if (albumToRent != null){
                        transaction.rentItem(albumToRent);
                        customer.incrementRentals();
                    }
                } else {
                    System.out.println(maxRentals);
                }
            }
    }

    protected void returnAlbum() {
        String name = input.getInput("Type your name to begin a lengthy and tedious return process: ");
        Customer customer = retrieveCustomer(name);
            if (customer != null) {
                Inventory rentedItem = retrieveItem(input.getInput("Which album are you returning? ID: "));
                if (rentedItem != null){
                    try {
                       processReturn(rentedItem, customer);
                    } catch (Exception exception){
                        System.out.println(exception.getMessage());
                    }
                }
            }
    }

    protected void viewAlbums() {
        inventory.sort(Comparator.comparingInt(Inventory::getYear));
        Collections.reverse(inventory);
        for (Inventory album : inventory){
            if (album instanceof Album){
                System.out.println(album.toString());
            }
        }
    }

    protected void searchAlbums() {
        int year = input.getInt("Album Search" + Input.EOL + "Release Year: ");
        boolean yearExists = false;
        for (Inventory album : inventory) {
            if (album instanceof Album){
                if (album.getYear() == year) {
                    yearExists = true;
                    System.out.println(album.toString());
                }
            }
        }
        if (!yearExists){
            System.out.println("There is no album available for this year.");
        }
    }

    protected void viewAlbumsByRating() {
        inventory.sort(Comparator.comparingDouble(Inventory::getRating));
        Collections.reverse(inventory);
        for (Inventory album : inventory){
            if (album instanceof Album){
                System.out.println(album.toString());
            }
        }
    }

    protected void addGame() {
        String userInput = "";
        do {
            try {
                String newGameTitle = input.getInput("Title:  ");
                String newGameGenre = input.getInput("Genre:  ");
                int newGameYear = input.getInt("Year:  ");
                double newGameRentCost = input.getDouble("Daily Rent Fee:  ");
                inventory.add(new Game(newGameTitle, newGameGenre, newGameRentCost, newGameYear ));
                System.out.println("Game Added Successfully " +Input.EOL + inventory.get(inventory.size()-1).toString());
            } catch (Exception exception){
               System.out.println( exception.getMessage());
                userInput = input.getInput("Would you like to try again? Y/N"+Input.EOL);
            }
        } while (userInput.equalsIgnoreCase("y"));
    }

    protected void removeGame() {
        viewGames();
        String gameId = input.getInput("Which game should be removed? Enter the Game ID: ");
        Inventory gameToRemove = retrieveItem(gameId);
        if (gameToRemove != null) {
            System.out.println("Are you sure you want to remove this game from the directory?" + Input.EOL + gameToRemove.toString() + Input.EOL + "(Y/N)");
            String doubleCheck = Input.input.nextLine();
            if (doubleCheck.equalsIgnoreCase("y")) {
                inventory.removeIf(game -> game.getId().equals(gameId));
                System.out.println("Game fed to a bunch of alpacas - theres no retrieving it anymore...");
            } else {
                System.out.println("Okay, no problem. ");
            }
        }
    }

    protected void viewGames() {
        inventory.sort(Comparator.comparingInt(Inventory::getYear));
        Collections.reverse(inventory);
        for (Inventory game : inventory) {
            if (game instanceof Game){
                System.out.println(game.toString());
            }
        }
    }

    protected void rentGame() {
        String maxRentals = "You have reached your current limit on rentals. That's awesome that you enjoy our products so much!";
        String name = input.getInput(Input.EOL + "Customer Name: ");
        Customer user = retrieveCustomer(name);
        if (user != null) {
            if (user.canRent()) {
                viewGames();
                String rentId = input.getInput("Hi " + user.getName() + ". Which game would you like to rent?" + Input.EOL + "ID: ");
                Inventory gameToRent = retrieveItem(rentId);
                if (gameToRent != null) {
                    transaction.rentItem(gameToRent);
                    user.incrementRentals();
                }
            } else {
                System.out.println(maxRentals);
            }
        }
    }

    protected void returnGame() {
        String name = input.getInput("Hiya! What is your name?  ");
        String returned;
        Customer customer = retrieveCustomer(name);
        if (customer != null) {
            viewGames();
            do {
                returned = "n";
                String rentId = input.getInput(Input.EOL + "Enter the ID of the game would you like to return: ");
                Inventory gameToReturn = retrieveItem(rentId);
                if (gameToReturn != null) {
                    if (gameToReturn.isRentStatus()) {
                        try {
                            processReturn(gameToReturn, customer);
                        } catch (Exception exception){
                            System.out.println(exception.getMessage());
                        }
                    } else {
                       returned = input.getInput("This game hasn't been rented. " + Input.EOL + "Try again? Y/N: ");
                    }
                }
            } while (!returned.equalsIgnoreCase("n")) ;
        }
    }

    private void processReturn(Inventory itemToReturn, Customer customer) throws Exception {
        double userBill = transaction.returnItem(customer, itemToReturn);
        if (userBill != 0) {
            RentalTransaction newTransaction = askRating(customer.getId(), itemToReturn, userBill);
            getRentalHistory().add(newTransaction);
            customer.applyCredits();
            customer.setSpentMoney(userBill);
            exportTransaction(newTransaction);
        }
    }

    protected void searchGames() {
        String google = input.getInput("Game Search" + Input.EOL + "Genre: ");
        input.input.nextLine();
        for (Inventory game : inventory) {
            if (game instanceof Game){
                if (((Game) game).getGenre().equalsIgnoreCase(google)) {
                    System.out.println(game.toString());
                }
            }
        }
    }

    protected void viewGamesByRating() {
        inventory.sort(Comparator.comparingDouble(Inventory::getRating));
        Collections.reverse(inventory);
        for (Inventory game : inventory) {
            if (game instanceof Game){
                System.out.println(game.toString());
            }
        }
    }

    protected void sendMessage() {
        viewCustomer();
        String recipientId = input.getInput(Input.EOL + "Customer ID of the person you want to contact: ");
        for (Customer customer : customerList) {
            if (customer.getId().equalsIgnoreCase(recipientId)) {
                String senderID = input.getInput("Your customer ID: ");
                String senderName = input.getInput("Your Name: ");
                String subject = input.getInput("Type your Title: ");
                String body = input.getInput("Type your message: ");
                Message newMessage = new Message(subject, body, senderID, senderName);
                System.out.println("Your message has been delivered to our own personal flying T-rex for prompt delivery (we breed them specifically for speed and agility).");

                customer.getInbox().add(newMessage);
            }
        }
    }

    protected void viewMessages() {
        String name = input.getInput("Enter your name to access your inbox: ");
        input.input.nextLine();
        for (Customer reader : customerList) {
            if (reader.getName().equalsIgnoreCase(name) && reader.getInbox().size() != 0) {
                Collections.reverse(reader.getInbox());
                System.out.println(Input.EOL + ">> List of messages in order received <<" + Input.EOL);
                customer.checkMessages(reader);
                Collections.reverse(reader.getInbox());
            } else if (reader.getName().equalsIgnoreCase(name) && reader.getInbox().size() == 0) {
                System.out.println(Input.EOL + "No messages to view.");
            }
        }
    }

    protected void removeMessages() {
        viewMessages();
        String removeMessage = input.getInput("Enter the message ID you want to delete: ");
        for (Customer customer : customerList) {
            customer.getInbox().removeIf(message -> message.getMessageId().equalsIgnoreCase(removeMessage));
        }
        System.out.println("The message has been deleted.");
    }

    protected void team() {
        System.out.println("-- Team {Code}pendant --" + Input.EOL+"Silent Saboteur - Axel"+Input.EOL+"Hacker SEM 2020 - Navya"+Input.EOL+"The Dead One - Drake"+Input.EOL+"The British One - Vernita");
    }
}
