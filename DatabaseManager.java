import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:atm.db";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }

    public static void testConnection() {
        try (Connection conn = connect()) {
            if (conn != null) {
                System.out.println("Database connected successfully!");
            }
        } catch (Exception e) {
            System.out.println("Database connection failed!");
            System.out.println(e.getMessage());
        }
    }

    public static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_number TEXT PRIMARY KEY, "
                + "account_holder_name TEXT NOT NULL, "
                + "pin INTEGER NOT NULL, "
                + "balance REAL NOT NULL, "
                + "current_loan REAL NOT NULL, "
                + "interest_rate REAL NOT NULL DEFAULT 0, "
                + "loan_due_date TEXT DEFAULT '', "
                + "history TEXT"
                + ")";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tables created successfully!");

        } catch (Exception e) {
            System.out.println("Error creating table!");
            System.out.println(e.getMessage());
        }
    }

    public static void insertAccount(BankAccount account) {
        String sql = "INSERT INTO accounts "
                + "(account_number, account_holder_name, pin, balance, current_loan, interest_rate, loan_due_date, history) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getAccountHolderName());
            pstmt.setInt(3, account.getPin());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setDouble(5, account.getCurrentLoan());
            pstmt.setDouble(6, account.getInterestRate());
            pstmt.setString(7, account.getLoanDueDate());
            pstmt.setString(8, account.historyToString());

            pstmt.executeUpdate();
            System.out.println("DEBUG: Account inserted into database!");

        } catch (Exception e) {
            System.out.println("Error inserting account!");
            System.out.println(e.getMessage());
        }
    }

    public static void updateAccount(BankAccount account) {
        String sql = "UPDATE accounts SET "
                + "account_holder_name = ?, "
                + "pin = ?, "
                + "balance = ?, "
                + "current_loan = ?, "
                + "interest_rate = ?, "
                + "loan_due_date = ?, "
                + "history = ? "
                + "WHERE account_number = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getAccountHolderName());
            pstmt.setInt(2, account.getPin());
            pstmt.setDouble(3, account.getBalance());
            pstmt.setDouble(4, account.getCurrentLoan());
            pstmt.setDouble(5, account.getInterestRate());
            pstmt.setString(6, account.getLoanDueDate());
            pstmt.setString(7, account.historyToString());
            pstmt.setString(8, account.getAccountNumber());

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error updating account!");
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<BankAccount> loadAccounts() {
        ArrayList<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String accountNumber = rs.getString("account_number");
                String accountHolderName = rs.getString("account_holder_name");
                int pin = rs.getInt("pin");
                double balance = rs.getDouble("balance");
                double currentLoan = rs.getDouble("current_loan");
                double interestRate = rs.getDouble("interest_rate");
                String loanDueDate = rs.getString("loan_due_date");
                String historyData = rs.getString("history");

                ArrayList<String> history = BankAccount.historyFromString(historyData);

                BankAccount account = new BankAccount(
                        accountHolderName,
                        accountNumber,
                        pin,
                        balance,
                        currentLoan,
                        interestRate,
                        loanDueDate,
                        history
                );

                accounts.add(account);
            }

        } catch (Exception e) {
            System.out.println("Error loading accounts!");
            System.out.println(e.getMessage());
        }

        return accounts;
    }
}