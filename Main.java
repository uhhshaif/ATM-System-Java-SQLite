public class Main {
    public static void main(String[] args) {
        DatabaseManager.testConnection();
        DatabaseManager.createTables();

        BankSystem system = new BankSystem();
        system.start();
    }
}