package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserLogin {
    private final Connection connection;
    private final Scanner scanner;

    public UserLogin(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void login() {
        System.out.println("Enter UserId: ");
        String userid = scanner.next();
        System.out.println("Enter Password: ");
        String password = scanner.next();
        try {
            String query = "SELECT * FROM users WHERE userid = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userid);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("User Login Successfully");
                userMenu(Integer.parseInt(userid));
            } else
                System.out.println("Invalid UserID or Password\nTry again....");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void userMenu(int userID) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transaction History");
            System.out.println("5. Logout");

            System.out.println("Choose an Option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    CheckBalance checkBalance = new CheckBalance(connection);
                    checkBalance.check(userID);
                    break;
                case 2:
                    Deposit deposit = new Deposit(connection, scanner);
                    deposit.deposit(userID);
                    break;
                case 3:
                    Withdraw withdraw = new Withdraw(connection, scanner);
                    withdraw.withdraw(userID);
                    break;
                case 4:
                    ShowTransactionsHistory transactionsHistory = new ShowTransactionsHistory(connection);
                    transactionsHistory.show(userID);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Option");
            }
        }
    }
}