package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class CreateUser {
    private Connection connection;
    private Scanner scanner;

    public CreateUser(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void create() {
        Random random = new Random();
        int max = 999;
        String userid = String.valueOf(random.nextInt(max+1));
        System.out.println("Enter Your Name: ");
        String username = scanner.next();
        System.out.println("Create Password (4-Digits): ");
        String password = scanner.next();
        try {
            String query = "INSERT INTO users(userid, username, password) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userid);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User Added Successfully");
                System.out.println("Your User Id = " + userid);
            } else
                System.out.println("Failed to Add User!! \nTry again");

        } catch(SQLException e) {
            e.printStackTrace();
        }

    }
}
