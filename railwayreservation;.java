package railwayreservation;

import java.sql.*;
import java.util.Scanner;

class Ticket {
    private static Connection connection = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void connectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/railway";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int generateTicket() {
        int nextTicket = 1000; // Starting ticket number
        return nextTicket++;
    }

    public static void displayTicket() {
        System.out.println("Ticket Number : " + generateTicket());
        System.out.println("----------------------------------------");
        System.out.println("Ticket Details");
        System.out.println("----------------------------------------");
        String selectQuery = "SELECT * FROM reservations";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String passengerName = resultSet.getString("passenger_name");
                String trainName = resultSet.getString("train_name");
                String departure = resultSet.getString("departure");
                String arrival = resultSet.getString("arrival");
                // Retrieve and display ticket details
                System.out.println("Passenger Name: " + passengerName + "\nTrain Name: " + trainName +
                        "\nDeparture: " + departure + "\nArrival: " + arrival);
                System.out.println("----------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving ticket details: " + e.getMessage());
        }
    }

    public static void bookTicket() {
        connectToDatabase();
        try {
            System.out.println("Enter Passenger Name:");
            String passengerName = scanner.next();
            System.out.println("Enter Train Name:");
            String trainName = scanner.next();
            System.out.println("Enter Departure Station:");
            String departure = scanner.next();
            System.out.println("Enter Arrival Station:");
            String arrival = scanner.next();

            String insertQuery = "INSERT INTO reservations (passenger_name, train_name, departure, arrival) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, passengerName);
            preparedStatement.setString(2, trainName);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, arrival);
            preparedStatement.executeUpdate();

            System.out.println("Ticket booked successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class Train {
    private static Connection connection = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void connectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/railway";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayTrainSchedule() {
        connectToDatabase();
        try {
            String selectQuery = "SELECT * FROM train_schedule";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Print column headers
            System.out.println("Train Schedule");
            System.out.println("----------------------------------------");
            // Iterate over the ResultSet and print each row's details
            while (resultSet.next()) {
                String trainName = resultSet.getString("train_name");
                String departure = resultSet.getString("departure");
                String arrival = resultSet.getString("arrival");
                // Display train schedule details
                System.out.println("Train Name: " + trainName + "\nDeparture: " + departure + "\nArrival: " + arrival);
                System.out.println("----------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
private static void loginUser() {
        System.out.println("Enter username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();
        System.out.println("Enter Role:");
        String role = scanner.next();

        try {
            String query = "SELECT * FROM user WHERE username=? AND password=? AND role=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login successful!");
                if (role.equals("user")) {
                    processBooking(new User(username, password));
                } else {
                    Admin admin = new Admin();
                    admin.connectToDatabase();
                    admin.createTables();
                    while (true) {
                        System.out.println("Press 1 to add SEAT, 2 to Delete SEAT, 3 to Update SEAT, 4 to Search Seats, 0 to exit:");
                        int ch = scanner.nextInt();
                        if (ch == 1) {
                            admin.addSeat();
                        } else if (ch == 2) {
                            admin.deleteSeat();
                        } else if (ch == 3) {
                            admin.updateSeat();
                        } else if (ch == 4) {
                            searchSeats();
                        } else {
                            System.out.println("Exiting...");
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Invalid username or password or role! Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


class User {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to Railway Reservation System!");
            System.out.println("Press 1 to Book Ticket");
            System.out.println("Press 2 to View Train Schedule");
            System.out.println("Press 3 to View Ticket");
            System.out.println("Press 0 to Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Ticket.bookTicket();
                    break;
                case 2:
                    Train.displayTrainSchedule();
                    break;
                case 3:
                    Ticket.displayTicket();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}


