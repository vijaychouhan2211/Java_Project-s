package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Withdraw {
    private Connection connection;
    private Scanner scanner;

    public Withdraw(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void withdraw(int userID) {
        System.out.println("Withdraw Amount: ");
        double amount = scanner.nextDouble();

        double currentBalance = getCurrentBalance(userID);
        if (currentBalance < 0)
            System.out.println("user Not Found.");

        if (amount > currentBalance) {
            System.out.println("Insufficient Balance." + currentBalance);
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
        recordTransaction(userID, amount, "Debit");
        CheckBalance checkBalance = new CheckBalance(connection);
        checkBalance.check(userID);
    }

    private void recordTransaction(int userID, double amount, String type) {
        double balance = getCurrentBalance(userID);
        String transactionQuery = "INSERT INTO transactions(transactionid, userid, amount, tdate, ttime, ttype, rebalance) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery);
            preparedStatement.setInt(1, new Random().nextInt(999999));
            preparedStatement.setInt(2, userID);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
            preparedStatement.setString(6, type);
            preparedStatement.setDouble(7, balance);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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