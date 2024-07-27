package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowTransactionsHistory {
    private final Connection connection;

    public ShowTransactionsHistory(Connection connection) {
        this.connection = connection;
    }

    public void show(int userID) {
        String transactionQuery = "SELECT amount, ttype, tdatetime, rebalance FROM transactions WHERE userid = ? ORDER BY tdatetime DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("|--------------------+------------------+------------------+-------------------|");
            System.out.println("| Transaction Amount | Transaction Type | Date & Time      | Remaining Balance |");
            System.out.println("|--------------------+------------------+------------------+-------------------|");

            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                String ttype = resultSet.getString("ttype");
                String tdatetime = resultSet.getString("tdatetime");
                double rebalance = resultSet.getDouble("rebalance");

                System.out.printf("| %-19s | %-15s | %-19s | %-14s |\n", amount, ttype, tdatetime, rebalance);
            }

            System.out.println("|--------------------+------------------+------------------+-------------------|");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
