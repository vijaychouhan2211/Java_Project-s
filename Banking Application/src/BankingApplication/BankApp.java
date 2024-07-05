package BankingApplication;

import java.sql.*;
import java.util.Scanner;

public class BankApp {
    private static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String username = "BankingApplication";
    private static final String password = "123456";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Users users = new Users(connection, scanner);

            while (true) {
                System.out.println("1. Create New User");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Choose an Option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        users.createUser();
                        break;
                    case 2:
                        users.userLogin();
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Invalid Option. Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void userMenu(int userID, Connection connection, Scanner scanner) throws SQLException {
        Transactions transactions = new Transactions(connection, scanner);

        while (true) {
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transaction History");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    transactions.checkBalance(userID);
                    break;
                case 2:
                    transactions.deposit(userID, 0);
                    break;
                case 3:
                    transactions.withdraw(userID, 0);
                    break;
                case 4:
                    transactions.showTransactionsHistory(userID);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}