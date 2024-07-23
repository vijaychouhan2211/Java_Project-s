package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Deposit {
    private final Connection connection;
    private final Scanner scanner;

    public Deposit(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void deposit(int userID) {
        System.out.println("Deposit Amount: ");
        double amount = scanner.nextDouble();
        String updateQuery = "UPDATE users SET balance = balance + ? WHERE userid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, userID);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0)
                System.out.println("Deposit Successfully..");
            else
                System.out.println("Deposit Failed..\nUser not found.");
            } catch (SQLException e) {
            e.printStackTrace();
        }
        recordTransaction(userID, amount, "Credit");
        CheckBalance checkBalance = new CheckBalance((connection));
        checkBalance.check(userID);
    }

    private void recordTransaction( int userID, double amount, String type) {
        double balance = getCurrentBalance(userID);
        String transactionQuery = "INSERT INTO transactions(transactionid, userid, amount, tdate, ttime, ttype, rebalance) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery);
            preparedStatement.setInt(1, new java.util.Random().nextInt(999999));
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

