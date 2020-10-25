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
    private static double rentalIncome;
    private final Input input = Input.getInstance();

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

    public double setRentalIncome(double total) {
        return total;
    }

    public void rentItem(Inventory itemToRent) {
        if (!itemToRent.isRentStatus()) {
            itemToRent.setRentStatus(true);
            itemToRent.setRentedDate(LocalDate.now());
            System.out.println(itemToRent.getTitle() + " has been rented. Enjoy!");
        } else {
            System.out.println("Sorry, " + itemToRent.getTitle() + " is being rented at the moment " + input.EOL);
        }
    }

    public double returnItem(Customer customer, Inventory rentedItem) throws Exception{
        double userBill = 0;
        long daysRented = DAYS.between(rentedItem.getRentedDate(), LocalDate.now());
        if (rentedItem.isRentStatus()) {
            if (daysRented > 0) {
                rentedItem.setRentStatus(false);
                if (customer.getCredits() == 5) {
                    userBill = 0;
                    customer.setCredit(0);
                } else userBill = customer.memberDiscount(daysRented * rentedItem.getDailyRent());
                rentalIncome += userBill;
                System.out.println(input.EOL + "You rented " + rentedItem.getTitle() + " for " + daysRented + " days. " + input.EOL + "Your total is " + Math.round(userBill * 100.0) / 100.0 + " kr" + input.EOL);
                System.out.println(rentedItem.getTitle() + " has now been returned.");
                return userBill;
            } else {
                throw new Exception("Invalid operation. Upon returning an item, the number of days rented must be positive." + input.EOL);
            }
        } else {
            System.out.println("That item is not out on rent!");
        }
        return userBill;
    }

    public String toString() {
        return "Customer ID: " + getCustomerId() + input.EOL + "Rental Item: " + getItemId() + input.EOL + "Transaction Cost: " + getRentExpense() + input.EOL + "Rating: " + getRating() + Input.EOL;
    }
}
