package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatients() {
        System.out.println("Enter Patient ID: ");
        int id = scanner.nextInt();
        System.out.println("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.println("Enter Patient Gender: ");
        String gender = scanner.nextLine();
        try {
            String query = "INSERT INTO patient (id, name, age, gender) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, gender);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Patient Added Successfully");
            } else {
                System.out.println("Failed to Added Patient!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = "select * from patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patient Details");
            System.out.println("+------------+--------------------+-------------+---------------+");
            System.out.println("| Patient ID | Name               | Age         | Gender        |");
            System.out.println("+------------+--------------------+-------------+---------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-20s|%-13s|%-16s|\n",id,name,age,gender);
                System.out.println("+------------+--------------------+-------------+---------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

      public boolean getPatientById(int id) {
          String query = "select * from patients where id = ?";
          try {
              PreparedStatement preparedStatement = connection.prepareStatement(query);
              preparedStatement.setInt(1, id);
              ResultSet resultSet = preparedStatement.executeQuery();
              if (resultSet.next()) {
                  return true;
              } else {
                  return false;
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return false;
      }
}