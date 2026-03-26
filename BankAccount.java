import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BankAccount {
    private String accountHolderName;
    private String accountNumber;
    private int pin;
    private double balance;

    // Loan fields
    private double currentLoan;
    private double interestRate;
    private String loanDueDate;

    private ArrayList<String> history;

    // For new account
    public BankAccount(String accountHolderName, String accountNumber, int pin) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = 0.0;

        this.currentLoan = 0.0;
        this.interestRate = 0.0;
        this.loanDueDate = "";

        this.history = new ArrayList<>();
        history.add("Account created for " + accountHolderName
                + " | Balance: RM " + String.format("%.2f", balance));
    }

    // For loading from database
    public BankAccount(String accountHolderName, String accountNumber, int pin,
                       double balance, double currentLoan, double interestRate,
                       String loanDueDate, ArrayList<String> history) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.currentLoan = currentLoan;
        this.interestRate = interestRate;
        this.loanDueDate = loanDueDate;
        this.history = history;

        if (this.history == null) {
            this.history = new ArrayList<>();
        }
        if (this.loanDueDate == null) {
            this.loanDueDate = "";
        }
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public double getCurrentLoan() {
        return currentLoan;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getLoanDueDate() {
        return loanDueDate;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public boolean checkPin(int enteredPin) {
        return this.pin == enteredPin;
    }

    public boolean changePin(int newPin) {
        if (newPin < 1000 || newPin > 9999) {
            return false;
        }

        this.pin = newPin;
        history.add("PIN changed successfully");
        return true;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }

        balance += amount;
        history.add("Deposited RM " + String.format("%.2f", amount)
                + " | Balance: RM " + String.format("%.2f", balance));
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }

        balance -= amount;
        history.add("Withdrew RM " + String.format("%.2f", amount)
                + " | Balance: RM " + String.format("%.2f", balance));
        return true;
    }

    public boolean takeLoan(double loanAmount) {
        if (currentLoan > 0) {
            return false;
        }

        if (loanAmount <= 0 || loanAmount > 10000) {
            return false;
        }

        interestRate = 0.05; // 5%
        double totalRepayment = loanAmount + (loanAmount * interestRate);

        currentLoan = totalRepayment;
        balance += loanAmount;

        LocalDate dueDate = LocalDate.now().plusDays(30);
        loanDueDate = dueDate.toString();

        history.add("Loan taken: RM " + String.format("%.2f", loanAmount)
                + " | Interest: 5%"
                + " | Total to repay: RM " + String.format("%.2f", currentLoan)
                + " | Due date: " + loanDueDate
                + " | Balance: RM " + String.format("%.2f", balance));
        return true;
    }

    public boolean repayLoan(double repayAmount) {
        if (currentLoan == 0) {
            return false;
        }

        if (repayAmount <= 0 || repayAmount > balance || repayAmount > currentLoan) {
            return false;
        }

        balance -= repayAmount;
        currentLoan -= repayAmount;

        if (currentLoan < 0.01) {
            currentLoan = 0.0;
        }

        history.add("Loan repaid: RM " + String.format("%.2f", repayAmount)
                + " | Remaining Loan: RM " + String.format("%.2f", currentLoan)
                + " | Balance: RM " + String.format("%.2f", balance));

        if (currentLoan == 0.0) {
            interestRate = 0.0;
            loanDueDate = "";
            history.add("Loan fully settled");
        }

        return true;
    }

    public String getLoanReminder() {
        if (currentLoan <= 0 || loanDueDate == null || loanDueDate.isEmpty()) {
            return "";
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = LocalDate.parse(loanDueDate);
        long daysLeft = ChronoUnit.DAYS.between(today, dueDate);

        if (daysLeft < 0) {
            return "WARNING: Your loan payment is overdue! Due date was " + loanDueDate;
        } else if (daysLeft <= 3) {
            return "REMINDER: Your loan payment of RM "
                    + String.format("%.2f", currentLoan)
                    + " is due in " + daysLeft + " day(s) on " + loanDueDate;
        } else if (daysLeft <= 7) {
            return "NOTICE: Your loan payment of RM "
                    + String.format("%.2f", currentLoan)
                    + " is due soon on " + loanDueDate;
        }

        return "";
    }

    public void printHistory() {
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("---- TRANSACTION HISTORY ----");
        for (String transaction : history) {
            System.out.println(transaction);
        }
        System.out.println("-----------------------------");
    }

    public void printAccountDetails() {
        System.out.println("---- ACCOUNT DETAILS ----");
        System.out.println("Account Holder : " + accountHolderName);
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Balance        : RM " + String.format("%.2f", balance));
        System.out.println("Current Loan   : RM " + String.format("%.2f", currentLoan));

        if (currentLoan > 0) {
            System.out.println("Interest Rate  : " + String.format("%.0f", interestRate * 100) + "%");
            System.out.println("Loan Due Date  : " + loanDueDate);
        }

        System.out.println("--------------------------");
    }

    public String historyToString() {
        if (history == null || history.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            sb.append(history.get(i));
            if (i != history.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static ArrayList<String> historyFromString(String historyData) {
        ArrayList<String> historyList = new ArrayList<>();

        if (historyData == null || historyData.trim().isEmpty()) {
            return historyList;
        }

        String[] lines = historyData.split("\n");
        for (String line : lines) {
            historyList.add(line);
        }

        return historyList;
    }
}