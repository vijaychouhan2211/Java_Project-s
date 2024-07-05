package BankingApplication;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Users {
    private Connection connection;
    private Scanner scanner;

    public Users(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void createUser() {
        Random random = new Random();
        int max = 999;
        String userid = String.valueOf(random.nextInt(max + 1  ));
        System.out.println("Enter Your Name: ");
        String username = scanner.next();
        System.out.println("Create Password: ");
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
            } else {
                System.out.println("Failed to Added User!! \nTry again");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void userLogin() throws SQLException {
        System.out.println("Enter UserID: ");
        String userid = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();
        try {
            String query = "SELECT * from  users WHERE userid = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userid);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("User Login Successful");
                BankApp.userMenu(Integer.parseInt(userid), connection, scanner);
            } else {
                System.out.println("Invalid UserID or Password \nTry again");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}