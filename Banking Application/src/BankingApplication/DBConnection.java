package BankingApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bankingapplication";
    private static final String USERNAME = "root"; // Your MySQL username
    private static final String PASSWORD = "@#$vijay22112005"; // Your MySQL password

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load MySQL JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}