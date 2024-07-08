package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String username = "HospitalManagementSystem";
    private static final String password = "123456";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patients");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointments");
                System.out.println("5. View Appointments");
                System.out.println("6. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatients();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointments(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        viewAppointments(patient, doctor, connection, scanner);
                        System.out.println();
                    case 6:
                        return;
                    default:
                        System.out.println("Enter a valid choice.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointments(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Appointment Id: ");
        int appointmentId = scanner.nextInt();
        System.out.println("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter Appointment Date (DD-MM-YYYY): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointments (id, patient_id, doctor_id, appointment_date) VALUES (?,?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery)) {
                    preparedStatement.setInt(1, appointmentId);
                    preparedStatement.setInt(2, patientId);
                    preparedStatement.setInt(3, doctorId);
                    preparedStatement.setString(4, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked Successfully");
                    } else {
                        System.out.println("Appointment Booking Failed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor Not Available on this date!");
            }
        } else {
            System.out.println("Either Patient or Doctor are Not Present");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void viewAppointments(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        String query = "SELECT * FROM appointments";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Appointment Details");
            System.out.println("+----------------+------------+------------+-------------------+");
            System.out.println("| Appointment ID | Patient ID | Doctor ID  | Appointment Date  |");
            System.out.println("+----------------+------------+------------+-------------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                String appointmentDate = resultSet.getString("appointment_date");
                System.out.printf("|%-16s|%-12s|%-12s|%-18s|\n", id, patientId, doctorId, appointmentDate);
            }
            System.out.println("+----------------+------------+------------+-------------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}