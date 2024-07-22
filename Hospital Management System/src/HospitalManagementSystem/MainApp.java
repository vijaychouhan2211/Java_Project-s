package HospitalManagementSystem;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class MainApp {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String USERNAME = "HospitalManagementSystem";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while (true) {
                displayMenu();
                int choice = getUserChoice(scanner);

                switch (choice) {
                    case 1:
                        patient.addPatients();
                        break;
                    case 2:
                        patient.viewPatients();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        break;
                    case 4:
                        bookAppointments(patient, doctor, connection, scanner);
                        break;
                    case 5:
                        viewAppointments(connection);
                        break;
                    case 6:
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("1. Add Patients");
        System.out.println("2. View Patients");
        System.out.println("3. View Doctors");
        System.out.println("4. Book Appointments");
        System.out.println("5. View Appointments");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void bookAppointments(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.print("Appointment Id: ");
        int appointmentId = scanner.nextInt();
        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter Appointment Date (DD-MM-YYYY): ");
        String appointmentDateStr = scanner.next();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            java.util.Date parsedDate = dateFormat.parse(appointmentDateStr);
            java.sql.Date appointmentDate = new java.sql.Date(parsedDate.getTime());

            if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
                if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                    String appointmentQuery = "INSERT INTO appointments (id, patient_id, doctor_id, \"DATE\") VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery)) {
                        preparedStatement.setInt(1, appointmentId);
                        preparedStatement.setInt(2, patientId);
                        preparedStatement.setInt(3, doctorId);
                        preparedStatement.setDate(4, appointmentDate);
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println(rowsAffected > 0 ? "Appointment Booked Successfully" : "Appointment Booking Failed");
                    }
                } else {
                    System.out.println("Doctor Not Available on this date!");
                }
            } else {
                System.out.println("Either Patient or Doctor are Not Present");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkDoctorAvailability(int doctorId, java.sql.Date date, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND \"DATE\" = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setDate(2, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) == 0; // Check if count is zero
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void viewAppointments(Connection connection) {
        String query = "SELECT * FROM appointments";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Appointment Details");
            System.out.println("+----------------+------------+------------+------------------+");
            System.out.println("| Appointment ID | Patient ID | Doctor ID  | Appointment Date |");
            System.out.println("+----------------+------------+------------+------------------+");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                Date appointmentDate = resultSet.getDate("date");
                String formattedDate = dateFormat.format(appointmentDate);
                System.out.printf("|%-16s|%-12s|%-12s|%-18s|\n", id, patientId, doctorId, formattedDate);
            }
            System.out.println("+----------------+------------+------------+------------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}