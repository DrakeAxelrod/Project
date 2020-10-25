package tools;

public class Menus {

    private String title;
    private String[] options;
    private final Input input = Input.getInstance();
    public static final String PROMPT = "\u001B[34m---------------------\u001B[0m" + System.lineSeparator() + "Choose your option: ";
    public static final String DIVIDER = "\u001B[34m---------------------\u001B[0m";

    public Menus(String title, String[] options) {
        this.title = title;
        this.options = options;
    }

    public Menus() {
    }

    public void mainMenu() {
        String[] options = {Input.ANSI_RESET + " 1) Manager Menu", " 2) Employee Menu", " 3) Customer Menu", " 4) Exit System"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "------Main-Menu------" + Input.ANSI_RESET, options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print(PROMPT);
    }

    public void managerMenu() {
        String[] options = {Input.ANSI_RESET + " 1) Register Employee", " 2) Remove Employee", " 3) View Employees",
                " 4) View Rental History ", " 5) View Items by Profit ",
                " 6) View Rent Frequency ", " 7) Best Customer ", " 8) Main Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "-----Manager-Menu-----", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void employeeMenu() {
        String[] options = {Input.ANSI_RESET + " 1) Game Options", " 2) Album Options", " 3) Customer Options", " 4) Total Rent Profit", " 5) Main Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "----Employee-Menu----", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void empGameOptions() {
        String[] options = {Input.ANSI_RESET + " 1) Register Game", " 2) Remove Game", " 3) View Games", " 4) Employee Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "-----Game-Options----", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void empAlbumOptions() {
        String[] options = {Input.ANSI_RESET + " 1) Register Album", " 2) Remove Album", " 3) View Albums", " 4) Employee Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "----Album-Options----", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void empCustomerOptions() {
        String[] options = {Input.ANSI_RESET + " 1) View Membership requests", " 2) Register Customer", " 3) Remove Customer", " 4) View Customers", " 5) Employee Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "---Customer-Options---", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void customerMenu() {
        String[] options = {Input.ANSI_RESET + " 1) Game Options", " 2) Album Options", " 3) Membership Options", " 4) Inbox", " 5) Main Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "----Customer-Menu----", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void cusGameOptions() {
        String[] options = {Input.ANSI_RESET + " 1) Rent Game", " 2) Return Game", " 3) Search Game by Genre", " 4) View Games by Year", " 5) View Games by Rating", " 6) Customer Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "---------Game--------", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void cusAlbumOptions() {
        String[] options = {Input.ANSI_RESET + " 1) Rent Album", " 2) Return Album", " 3) Search Album by Year", " 4) View Albums by Year", " 5) View Albums by Rating", " 6) Customer Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "--------Album--------", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void cusMembershipOptions() {
        String[] options = {Input.ANSI_RESET + " 1) Add Membership", " 2) Upgrade Membership", " 3) Customer Menu"};
        Menus menu = new Menus(Input.EOL + Input.ANSI_BLUE + "-----Membership------", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void inboxMenu() {
        String[] options = {Input.ANSI_RESET + " 1) View messages", " 2) Send Message", " 3) Delete Message", " 4) Customer Menu"};
        Menus menu = new Menus(Input.ANSI_BLUE + "--------Inbox--------", options);
        System.out.println(menu.title);
        for (String option : options) {
            System.out.println(option);
        }
    }
    public void secret(){
    String[] options = {Input.ANSI_WHITE + " 1) {code}pendant ", " 2) Main Menu"+Input.ANSI_RESET};
    String title = Input.EOL + Input.ANSI_WHITE + "---- Secret Menu ----"+Input.ANSI_RESET;
        System.out.println(title);
        for (String option : options) {
            System.out.println(option);
        }
    }
}
