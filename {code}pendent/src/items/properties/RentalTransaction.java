package items.properties;

import items.Inventory;
import people.Customer;
import tools.Input;

import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

public class RentalTransaction {
    private String customerId;
    private String itemId;
    private String title;
    private double rentExpense;
    private Rating rating;
    private static double rentalIncome = 0;
    private Input input = Input.getInstance();

    // Default Constructor
    public RentalTransaction() {
    }

    public RentalTransaction(String customerId, String itemId, String title, double rentExpense) {
        this.customerId = customerId;
        this.itemId = itemId;
        this.title = title;
        this.rentExpense = rentExpense;
    }

    public RentalTransaction(String customerId, String itemId, String title, double rentExpense, Rating rating) {
        this.customerId = customerId;
        this.itemId = itemId;
        this.title = title;
        this.rentExpense = rentExpense;
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getItemId() {
        return itemId;
    }

    public double getRentExpense() {
        return Math.round(rentExpense * 100.0) / 100.0;
    }

    public double getRentalIncome() {
        return Math.round(rentalIncome * 100)/100;
    }

    public double setRentalIncome(double transaction) {
        return rentalIncome + transaction;
    }

    public void rentItem(Inventory itemToRent) {
        if (!itemToRent.isRentStatus()) {
            itemToRent.setRentStatus(true);
            itemToRent.setRentedDate(LocalDate.now());
            System.out.println("Game has been rented. Enjoy!");
        } else {
            System.out.println("Sorry, that game is being rented at the moment " + input.EOL);
        }
    }

    public double returnItem(Customer customer, Inventory rentedItem) {
        double userBill = 0;
        long daysRented = DAYS.between(rentedItem.getRentedDate(), LocalDate.now());
        if (daysRented > 0) {
            rentedItem.setRentStatus(false);
            if (customer.getCredits() == 5) {
                userBill = 0;
            } else userBill = customer.memberDiscount(daysRented * rentedItem.getDailyRent());
            setRentalIncome(userBill);
            System.out.println(input.EOL + "You rented " + rentedItem.getTitle() + " for " + daysRented + " days. " + input.EOL + "Your total is " + Math.round(userBill * 100.0) / 100.0 + " kr" + input.EOL);
            System.out.println("The Game has now been returned.");
            return userBill;
        } else {
            System.out.println("Invalid operation. Upon returning an item, the number of days rented must be positive." + input.EOL);
        }
        return 0;
    }

    public String toString() {
        return "Customer ID: " + getCustomerId() + input.EOL + "Rental Item: " + getItemId() + input.EOL + "Transaction Cost: " + getRentExpense() + input.EOL + "Rating: " + getRating() + Input.EOL;
    }
}
