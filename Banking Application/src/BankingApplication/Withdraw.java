package BankingApplication;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;

public class Withdraw {
    private final Connection connection;
    private final Scanner scanner;

    public Withdraw(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void withdraw(int userID) {
        System.out.println("Withdraw Amount: ");
        double amount = scanner.nextDouble();

        double currentBalance = getCurrentBalance(userID);
        if (currentBalance < 0) {
            System.out.println("User Not Found.");
            return;
        }

        if (amount > currentBalance) {
            System.out.println("Insufficient Balance: " + currentBalance);
            return;
        }

        currentBalance -= amount;
        String updateQuery = "UPDATE users SET balance = ? WHERE userid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDouble(1, currentBalance);
            preparedStatement.setInt(2, userID);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0)
                System.out.println("Withdraw Successful..");
            else
                System.out.println("Withdraw failed");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Call recordTransaction with three parameters
        recordTransaction(userID, amount, "Debit");
        CheckBalance checkBalance = new CheckBalance(connection);
        checkBalance.check(userID);
    }

    private void recordTransaction(int userID, double amount, String type) {
        double balance = getCurrentBalance(userID);
        String transactionQuery = "INSERT INTO transactions(transactionid, userid, amount, tdatetime, ttype, rebalance) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery);
            preparedStatement.setInt(1, new Random().nextInt(999999)); // Generate a random transaction ID
            preparedStatement.setInt(2, userID);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // Current date and time
            preparedStatement.setString(5, type);
            preparedStatement.setDouble(6, balance);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Print the full stack trace for debugging
        }
    }

    private double getCurrentBalance(int userID) {
        String query = "SELECT balance FROM users WHERE userid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getDouble("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}