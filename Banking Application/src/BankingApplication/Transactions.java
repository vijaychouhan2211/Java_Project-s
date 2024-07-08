package BankingApplication;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Transactions {
    private Connection connection;
    private Scanner scanner;

    public Transactions(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void checkBalance(int userID) {
        String query = "SELECT balance FROM users WHERE userid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Your balance is: " + balance);
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposit(int userID, double amount) {
        System.out.println("Enter the Amount to Deposit: ");
        amount = scanner.nextDouble();
        String updateQuery = "UPDATE users SET balance = balance + ? WHERE userid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, userID);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Deposit successful. ");
            } else {
                System.out.println("Deposit failed. User not found.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        recordTransaction(userID, amount, "Credit");
        checkBalance(userID);
    }

    public void withdraw(int userID, double amount) {
        System.out.println("Enter the Amount to Withdraw: ");
        amount = scanner.nextDouble();
        double currentBalance = getCurrentBalance(userID);
        if (currentBalance < 0) {
            System.out.println("User not found.");
            return;
        }
        if (amount > currentBalance) {
            System.out.println("Insufficient Balance. Current balance: " + currentBalance);
            return;
        }
        currentBalance = currentBalance - amount;
        String updateQuery = "UPDATE users SET balance = ? WHERE userid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDouble(1, currentBalance);
            preparedStatement.setInt(2, userID);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Withdrawal successful..");
            } else {
                System.out.println("Withdrawal failed. User not found.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        recordTransaction(userID, amount, "Debit");
        checkBalance(userID);
    }

    private double getCurrentBalance(int userID) {
        String query = "SELECT balance FROM users WHERE userid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void recordTransaction(int userID, double amount, String type) {
        String userQuery = "SELECT * FROM users WHERE userid = ?";
        double balance = getCurrentBalance(userID);
        String transactionQuery = "INSERT INTO transactions (transactionid, userid, amount, tdate, ttime, ttype, rebalance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery);
            PreparedStatement userBalance = connection.prepareStatement(userQuery);
            userBalance.setInt(1, userID);
            ResultSet resultSetBalance = userBalance.executeQuery();
            if (resultSetBalance.next()) {
                if (type.equals("Credit")) {
                    preparedStatement.setDouble(7,balance);
                } else if (type.equals("Debit")) {
                    preparedStatement.setDouble(7,balance);
                }
            }
            preparedStatement.setInt(1, new Random().nextInt(99999));
            preparedStatement.setInt(2, userID);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            preparedStatement.setString(5, new SimpleDateFormat("HH:mm:ss").format(new Date()));
            preparedStatement.setString(6, type);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showTransactionsHistory(int userID) {
        String transactionQuery = "SELECT amount, ttype, tdate, ttime, rebalance FROM transactions WHERE userid = ? ORDER BY ttime DESC, tdate  ";
        try {
            PreparedStatement transactionStatement = connection.prepareStatement(transactionQuery);
            transactionStatement.setInt(1, userID);
            try {
                    ResultSet transactionResultSet = transactionStatement.executeQuery();
                    System.out.println("|--------------------+-----------------+-----------+----------+-----------------|");
                    System.out.println("|Transaction Amount  |Transaction Type |Date       |Time      |Remaining Balance|");
                    System.out.println("|--------------------+-----------------+-----------+----------+-----------------|");

                    while (transactionResultSet.next()) {
                        double amount = transactionResultSet.getDouble("amount");
                        String ttype = transactionResultSet.getString("ttype");
                        String tdate = transactionResultSet.getString("tdate");
                        String ttime = transactionResultSet.getString("ttime");
                        double rebalance = transactionResultSet.getDouble("rebalance");

                        System.out.printf("|%-20s|%-17s|%-11s|%-10s|%-17s|\n", amount, ttype, tdate, ttime, rebalance);
                    }
                    System.out.println("|--------------------+-----------------+-----------+----------+-----------------|");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}