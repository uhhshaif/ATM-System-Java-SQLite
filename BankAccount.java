import java.util.ArrayList;

public class BankAccount {
    private String accountHolderName;
    private String accountNumber;
    private int pin;
    private double balance;
    private double currentLoan;
    private ArrayList<String> history;

    public BankAccount(String accountHolderName, String accountNumber, int pin) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = 0.0;
        this.currentLoan = 0.0;
        this.history = new ArrayList<>();

        history.add("Account created for " + accountHolderName
                + " | Balance: RM " + String.format("%.2f", balance));
    }

    public BankAccount(String accountHolderName, String accountNumber, int pin,
                       double balance, double currentLoan, ArrayList<String> history) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.currentLoan = currentLoan;
        this.history = history;

        if (this.history == null) {
            this.history = new ArrayList<>();
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

        currentLoan = loanAmount;
        balance += loanAmount;

        history.add("Loan taken: RM " + String.format("%.2f", loanAmount)
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

        history.add("Loan repaid: RM " + String.format("%.2f", repayAmount)
                + " | Remaining Loan: RM " + String.format("%.2f", currentLoan)
                + " | Balance: RM " + String.format("%.2f", balance));
        return true;
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