package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowTransactionsHistory {
    private Connection connection;

    public ShowTransactionsHistory(Connection connection) {
        this.connection = connection;
    }

    public void show(int userID) {
        String transactionQuery = "SELECT amount, ttype, tdate, ttime, rebalance FROM transactions WHERE userid = ? ORDER BY ttime DESC, tdate";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(transactionQuery);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("|--------------------+------------------+-----------+----------+-------------------|");
            System.out.println("| Transaction Amount | Transaction Type | Date      | Time     | Remaining Balance |");
            System.out.println("|--------------------+------------------+-----------+----------+-------------------|");

            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                String ttype = resultSet.getString("ttype");
                String tdate = resultSet.getString("tdate");
                String ttime = resultSet.getString("ttime");
                double rebalance = resultSet.getDouble("rebalance");

                System.out.printf("| %-19s | %-15s | %-9s | %-8s | %-16s |\n", amount, ttype, tdate, ttime, rebalance);
            }

            System.out.println("|--------------------+------------------+-----------+----------+-------------------|");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
