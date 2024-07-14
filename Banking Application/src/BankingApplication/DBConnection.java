package BankingApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String username = "BankingApplication";
    private static final String password = "123456";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(url, username, password);
    }
}