import java.util.ArrayList;
import java.util.Scanner;

public class BankSystem {
    private ArrayList<BankAccount> accounts;
    private Scanner sc;

    public BankSystem() {
        accounts = DatabaseManager.loadAccounts();
        sc = new Scanner(System.in);
    }

    public void start() {
        int choice;

        do {
            showMainMenu();

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number from 1 to 3.");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    registerAccount();
                    break;
                case 2:
                    loginAccount();
                    break;
                case 3:
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose between 1 and 3.");
            }

        } while (choice != 3);

        System.out.println("Thank you for using the banking system.");
    }

    private void showMainMenu() {
        System.out.println();
        System.out.println("========= BANK SYSTEM =========");
        System.out.println("1) Create New Account");
        System.out.println("2) Login to Existing Account");
        System.out.println("3) Exit");
        System.out.print("Enter your choice: ");
    }

    private void registerAccount() {
        System.out.print("Enter account holder name: ");
        String accountHolderName = sc.nextLine();

        if (accountHolderName.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        if (accountHolderName.matches("\\d+")) {
            System.out.println("Name cannot be numbers only.");
            return;
        }

        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine();

        if (!accountNumber.matches("\\d+") || accountNumber.length() < 6 || accountNumber.length() > 12) {
            System.out.println("Invalid account number. It must contain only digits and be 6 to 12 digits long.");
            return;
        }

        if (findAccount(accountNumber) != null) {
            System.out.println("Account number already exists.");
            return;
        }

        System.out.print("Enter 4-digit PIN: ");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a 4-digit PIN.");
            sc.next();
        }

        int pin = sc.nextInt();
        sc.nextLine();

        if (pin < 1000 || pin > 9999) {
            System.out.println("Invalid PIN. PIN must be exactly 4 digits.");
            return;
        }

        BankAccount newAccount = new BankAccount(accountHolderName, accountNumber, pin);

        accounts.add(newAccount);
        DatabaseManager.insertAccount(newAccount);

        System.out.println("Account created successfully.");
        System.out.println("Welcome, " + accountHolderName + "!");
        System.out.println("Your new account balance is RM 0.00.");
    }

    private void loginAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine();

        BankAccount account = findAccount(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        int attempts = 0;

        while (attempts < 3) {
            System.out.print("Enter PIN: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a 4-digit PIN.");
                sc.next();
            }

            int enteredPin = sc.nextInt();
            sc.nextLine();

           if (account.checkPin(enteredPin)) {
        System.out.println("Login successful.");

     String reminder = account.getLoanReminder();
        if (!reminder.isEmpty()) {
        System.out.println(reminder);
        }

       ATM machine = new ATM(account, sc, accounts);
        machine.startApp();
        return;
            } else {
                attempts++;
                System.out.println("Wrong PIN. Attempts remaining: " + (3 - attempts));
            }
        }

        System.out.println("Too many failed attempts. Returning to main menu.");
    }

    private BankAccount findAccount(String accountNumber) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber().equals(accountNumber)) {
                return accounts.get(i);
            }
        }
        return null;
    }
}