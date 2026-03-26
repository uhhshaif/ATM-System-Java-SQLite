# ATM System (Java + SQLite)

A command-line ATM banking system built using Java with SQLite database integration for persistent data storage.

## Features
- Create new account
- Login with PIN authentication
- Deposit money
- Withdraw money
- Loan system (take & repay)
- Transaction history tracking
- Persistent data storage using SQLite

## Technologies Used
- Java
- SQLite (JDBC)

## How to Run

### Option 1: Using VS Code (Recommended)
1. Open the project in VS Code
2. Make sure the `lib` folder contains the SQLite JDBC file
3. Run `Main.java`

### Option 2: Using Terminal

Compile:
javac -cp ".;lib/sqlite-jdbc-3.51.3.0.jar" *.java

Run:
java -cp ".;lib/sqlite-jdbc-3.51.3.0.jar" Main

## Project Structure
- ATM.java → User interface
- BankSystem.java → Core system logic
- BankAccount.java → Account model
- DatabaseManager.java → Database handling
- Main.java → Entry point