package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private final Connection connection;
    private final Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatients() {
        System.out.print("Enter Patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        String query = "INSERT INTO patients (id, name, age, gender) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, gender);
            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(affectedRows > 0 ? "Patient Added Successfully" : "Failed to Add Patient!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = "SELECT * FROM patients ORDER BY id ASC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Patient Details");
            System.out.println("+------------+--------------------+-----+--------+");
            System.out.println("| Patient ID | Name               | Age | Gender |");
            System.out.println("+------------+--------------------+-----+ -------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-20s|%-5s|%-8s|\n", id, name, age, gender);
            }
            System.out.println("+------------+--------------------+-----+--------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}