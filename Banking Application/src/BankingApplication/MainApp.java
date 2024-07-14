package BankingApplication;

import java.sql.Connection;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DBConnection.getConnection();

            while (true) {
                System.out.println("1. Create New User");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Choose an Option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        CreateUser createUser = new CreateUser(connection, scanner);
                        createUser.create();
                        break;
                    case 2:
                        UserLogin userLogin = new UserLogin(connection, scanner);
                        userLogin.login();
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Invalid Option. Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}