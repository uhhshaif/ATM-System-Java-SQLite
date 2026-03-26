import java.util.Scanner;

public class ATM {
    private BankAccount account;
    private Scanner sc;

    public ATM(BankAccount account, Scanner sc) {
        this.account = account;
        this.sc = sc;
    }

    public void startApp() {
        int choice;

        do {
            showMenu();

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number from 1 to 9.");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine();

            handleChoice(choice);

        } while (choice != 9);

        System.out.println("You have been logged out successfully.");
    }

    private void showMenu() {
        System.out.println();
        System.out.println("----------- ATM MENU -----------");
        System.out.println("Welcome, " + account.getAccountHolderName());
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("1) Check Balance");
        System.out.println("2) Deposit Money");
        System.out.println("3) Withdraw Money");
        System.out.println("4) Change PIN");
        System.out.println("5) View Transaction History");
        System.out.println("6) Take Loan");
        System.out.println("7) Repay Loan");
        System.out.println("8) View Account Details");
        System.out.println("9) Logout");
        System.out.print("Enter your choice: ");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                handleCheckBalance();
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
                handleHistory();
                break;
            case 6:
                handleTakeLoan();
                break;
            case 7:
                handleRepayLoan();
                break;
            case 8:
                handleAccountDetails();
                break;
            case 9:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please choose between 1 and 9.");
        }
    }

    private void handleCheckBalance() {
        System.out.println("Your current balance is: RM " + String.format("%.2f", account.getBalance()));
    }

    private void handleDeposit() {
        System.out.print("Enter amount to deposit: RM ");

        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid amount.");
            sc.next();
        }

        double depositAmount = sc.nextDouble();
        sc.nextLine();

        if (depositAmount <= 0) {
            System.out.println("Deposit amount must be more than RM 0.00.");
            return;
        }

        if (account.deposit(depositAmount)) {
            DatabaseManager.updateAccount(account);
            System.out.println("Deposit successful.");
            System.out.println("Updated balance: RM " + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Deposit failed.");
        }
    }

    private void handleWithdraw() {
        System.out.print("Enter amount to withdraw: RM ");

        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid amount.");
            sc.next();
        }

        double withdrawAmount = sc.nextDouble();
        sc.nextLine();

        if (withdrawAmount <= 0) {
            System.out.println("Withdrawal amount must be more than RM 0.00.");
            return;
        }

        if (withdrawAmount > account.getBalance()) {
            System.out.println("Insufficient balance.");
            return;
        }

        if (account.withdraw(withdrawAmount)) {
            DatabaseManager.updateAccount(account);
            System.out.println("Withdrawal successful.");
            System.out.println("Updated balance: RM " + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Withdrawal failed.");
        }
    }

    private void handleChangePin() {
        System.out.print("Enter new 4-digit PIN: ");

        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a 4-digit PIN.");
            sc.next();
        }

        int newPin = sc.nextInt();
        sc.nextLine();

        if (account.changePin(newPin)) {
            DatabaseManager.updateAccount(account);
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Invalid PIN. PIN must be exactly 4 digits.");
        }
    }

    private void handleHistory() {
        account.printHistory();
    }

    private void handleTakeLoan() {
        System.out.print("Enter loan amount (Maximum RM 10000.00): RM ");

        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid amount.");
            sc.next();
        }

        double loanTake = sc.nextDouble();
        sc.nextLine();

        if (loanTake <= 0) {
            System.out.println("Loan amount must be more than RM 0.00.");
            return;
        }

        if (loanTake > 10000) {
            System.out.println("Loan amount cannot exceed RM 10000.00.");
            return;
        }

        if (account.getCurrentLoan() > 0) {
            System.out.println("You already have an active loan.");
            return;
        }

        if (account.takeLoan(loanTake)) {
            DatabaseManager.updateAccount(account);
            System.out.println("Loan approved successfully.");
            System.out.println("Current loan: RM " + String.format("%.2f", account.getCurrentLoan()));
            System.out.println("Updated balance: RM " + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Loan request failed.");
        }
    }

    private void handleRepayLoan() {
        if (account.getCurrentLoan() == 0) {
            System.out.println("You do not have any active loan to repay.");
            return;
        }

        System.out.println("Current loan: RM " + String.format("%.2f", account.getCurrentLoan()));
        System.out.print("Enter amount to repay: RM ");

        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid amount.");
            sc.next();
        }

        double loanRepay = sc.nextDouble();
        sc.nextLine();

        if (loanRepay <= 0) {
            System.out.println("Repayment amount must be more than RM 0.00.");
            return;
        }

        if (loanRepay > account.getBalance()) {
            System.out.println("Insufficient balance to repay this amount.");
            return;
        }

        if (loanRepay > account.getCurrentLoan()) {
            System.out.println("Repayment amount cannot exceed current loan.");
            return;
        }

        if (account.repayLoan(loanRepay)) {
            DatabaseManager.updateAccount(account);
            System.out.println("Loan repayment successful.");
            System.out.println("Remaining loan: RM " + String.format("%.2f", account.getCurrentLoan()));
            System.out.println("Updated balance: RM " + String.format("%.2f", account.getBalance()));

            if (account.getCurrentLoan() == 0) {
                System.out.println("Congratulations. Your loan has been fully repaid.");
            }
        } else {
            System.out.println("Loan repayment failed.");
        }
    }

    private void handleAccountDetails() {
        account.printAccountDetails();
    }
}