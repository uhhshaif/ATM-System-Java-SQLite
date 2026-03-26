import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    private BankAccount account;
    private Scanner sc;
    private ArrayList<BankAccount> accounts;

    public ATM(BankAccount account, Scanner sc, ArrayList<BankAccount> accounts) {
        this.account = account;
        this.sc = sc;
        this.accounts = accounts;
    }

    public void startApp() {
        int choice;

        do {
            showMenu();

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number (1-10): ");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine();

            System.out.println();
            handleChoice(choice);

            if (choice != 10) {
                pause();
            }

        } while (choice != 10);

        printMessage("Logged out successfully.");
    }

    private void showMenu() {
        printHeader("ATM SYSTEM");

        System.out.println("User      : " + account.getAccountHolderName());
        System.out.println("Account   : " + account.getAccountNumber());
        printLine();

        String reminder = account.getLoanReminder();
        if (!reminder.isEmpty()) {
            System.out.println("! " + reminder);
            printLine();
        }

        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Change PIN");
        System.out.println("5. Transaction History");
        System.out.println("6. Take Loan");
        System.out.println("7. Repay Loan");
        System.out.println("8. Account Details");
        System.out.println("9. Transfer Money");
        System.out.println("10. Logout");

        printLine();
        System.out.print("Enter choice: ");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                printMessage("Current Balance: RM " + String.format("%.2f", account.getBalance()));
                break;

            case 2:
                handleDeposit();
                break;

            case 3:
                handleWithdraw();
                break;

            case 4:
                handleChangePin();
                break;

            case 5:
                printHeader("TRANSACTION HISTORY");
                account.printHistory();
                break;

            case 6:
                handleTakeLoan();
                break;

            case 7:
                handleRepayLoan();
                break;

            case 8:
                printHeader("ACCOUNT DETAILS");
                account.printAccountDetails();
                break;

            case 9:
                handleTransfer();
                break;

            case 10:
                break;

            default:
                printMessage("Invalid choice.");
        }
    }

    private void handleDeposit() {
        System.out.print("Enter amount to deposit: RM ");

        while (!sc.hasNextDouble()) {
            System.out.print("Invalid amount. Enter again: ");
            sc.next();
        }

        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0) {
            printMessage("Amount must be greater than 0.");
            return;
        }

        if (account.deposit(amount)) {
            DatabaseManager.updateAccount(account);
            printMessage("Deposit successful!");
            System.out.println("Balance: RM " + String.format("%.2f", account.getBalance()));
        } else {
            printMessage("Deposit failed.");
        }
    }

    private void handleWithdraw() {
        System.out.print("Enter amount to withdraw: RM ");

        while (!sc.hasNextDouble()) {
            System.out.print("Invalid amount. Enter again: ");
            sc.next();
        }

        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0) {
            printMessage("Amount must be greater than 0.");
            return;
        }

        if (amount > account.getBalance()) {
            printMessage("Insufficient balance.");
            return;
        }

        if (account.withdraw(amount)) {
            DatabaseManager.updateAccount(account);
            printMessage("Withdrawal successful!");
            System.out.println("Balance: RM " + String.format("%.2f", account.getBalance()));
        } else {
            printMessage("Withdrawal failed.");
        }
    }

    private void handleChangePin() {
        System.out.print("Enter new 4-digit PIN: ");

        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Enter again: ");
            sc.next();
        }

        int newPin = sc.nextInt();
        sc.nextLine();

        if (account.changePin(newPin)) {
            DatabaseManager.updateAccount(account);
            printMessage("PIN changed successfully.");
        } else {
            printMessage("Invalid PIN. Must be 4 digits.");
        }
    }

    private void handleTakeLoan() {
        System.out.print("Enter loan amount (max RM 10000): RM ");

        while (!sc.hasNextDouble()) {
            System.out.print("Invalid amount. Enter again: ");
            sc.next();
        }

        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0 || amount > 10000) {
            printMessage("Invalid loan amount.");
            return;
        }

        if (account.getCurrentLoan() > 0) {
            printMessage("You already have an active loan.");
            return;
        }

        if (account.takeLoan(amount)) {
            DatabaseManager.updateAccount(account);

            printMessage("Loan approved!");
            System.out.println("Amount received : RM " + String.format("%.2f", amount));
            System.out.println("Interest rate   : " + String.format("%.0f", account.getInterestRate() * 100) + "%");
            System.out.println("Total to repay  : RM " + String.format("%.2f", account.getCurrentLoan()));
            System.out.println("Due date        : " + account.getLoanDueDate());
            System.out.println("New balance     : RM " + String.format("%.2f", account.getBalance()));
        } else {
            printMessage("Loan request failed.");
        }
    }

    private void handleRepayLoan() {
        if (account.getCurrentLoan() == 0) {
            printMessage("No active loan.");
            return;
        }

        System.out.println("Current loan: RM " + String.format("%.2f", account.getCurrentLoan()));
        System.out.println("Due date    : " + account.getLoanDueDate());
        System.out.print("Enter repayment amount: RM ");

        while (!sc.hasNextDouble()) {
            System.out.print("Invalid amount. Enter again: ");
            sc.next();
        }

        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0) {
            printMessage("Amount must be greater than 0.");
            return;
        }

        if (amount > account.getBalance()) {
            printMessage("Insufficient balance.");
            return;
        }

        if (amount > account.getCurrentLoan()) {
            printMessage("Amount cannot exceed remaining loan.");
            return;
        }

        if (account.repayLoan(amount)) {
            DatabaseManager.updateAccount(account);

            printMessage("Repayment successful!");
            System.out.println("Remaining loan: RM " + String.format("%.2f", account.getCurrentLoan()));
            System.out.println("Balance       : RM " + String.format("%.2f", account.getBalance()));
        } else {
            printMessage("Repayment failed.");
        }
    }

    private void handleTransfer() {
        System.out.print("Enter receiver account number: ");
        String receiverAccNum = sc.nextLine();

        if (receiverAccNum.equals(account.getAccountNumber())) {
            printMessage("Cannot transfer to your own account.");
            return;
        }

        BankAccount receiver = null;

        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equals(receiverAccNum)) {
                receiver = acc;
                break;
            }
        }

        if (receiver == null) {
            printMessage("Receiver account not found.");
            return;
        }

        System.out.print("Enter amount to transfer: RM ");

        while (!sc.hasNextDouble()) {
            System.out.print("Invalid amount. Enter again: ");
            sc.next();
        }

        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0) {
            printMessage("Amount must be greater than 0.");
            return;
        }

        if (amount > account.getBalance()) {
            printMessage("Insufficient balance.");
            return;
        }

        // transfer
        account.withdraw(amount);
        receiver.deposit(amount);

        // FIXED: history BEFORE saving
        account.getHistory().add("Transferred RM " + String.format("%.2f", amount)
                + " to " + receiver.getAccountNumber());

        receiver.getHistory().add("Received RM " + String.format("%.2f", amount)
                + " from " + account.getAccountNumber());

        // save BOTH
        DatabaseManager.updateAccount(account);
        DatabaseManager.updateAccount(receiver);

        printMessage("Transfer successful!");
        System.out.println("Sent RM " + String.format("%.2f", amount)
                + " to " + receiver.getAccountHolderName());
        System.out.println("Balance: RM " + String.format("%.2f", account.getBalance()));
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("        " + title);
        System.out.println("========================================");
    }

    private void printLine() {
        System.out.println("----------------------------------------");
    }

    private void printMessage(String message) {
        System.out.println(">> " + message);
    }

    private void pause() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        sc.nextLine();
    }
}