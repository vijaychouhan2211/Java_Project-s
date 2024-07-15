package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckBalance {
    private Connection connection;

    public CheckBalance(Connection connection) {
        this.connection = connection;
    }

    public void check(int userID) {
        String query = "SELECT balance FROM users WHERE userid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Your Balance : " + balance);
        } else
            System.out.println("User not Found...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}