import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/airlinedb";
        String user = "root";
        String password = "root"; // change as per your MySQL

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Flight ID: ");
        int flightId = sc.nextInt();

        sc.nextLine(); // consume newline
        System.out.print("Enter Passenger Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Seats Required: ");
        int seatsRequested = sc.nextInt();

        try {
            Connection con = DriverManager.getConnection(url, user, password);

            // Start transaction
            con.setAutoCommit(false);

            // Check available seats
            String checkQuery = "SELECT available_seats, price_per_seat FROM flights WHERE flight_id = ?";
            PreparedStatement psCheck = con.prepareStatement(checkQuery);
            psCheck.setInt(1, flightId);

            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                int availableSeats = rs.getInt("available_seats");
                double price = rs.getDouble("price_per_seat");

                if (availableSeats >= seatsRequested) {
                    int newSeats = availableSeats - seatsRequested;
                    double totalAmount = seatsRequested * price;

                    // Update seats
                    String updateQuery = "UPDATE flights SET available_seats = ? WHERE flight_id = ?";
                    PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                    psUpdate.setInt(1, newSeats);
                    psUpdate.setInt(2, flightId);
                    psUpdate.executeUpdate();

                    // Insert booking
                    String insertQuery = "INSERT INTO bookings(passenger_name, flight_id, seats_booked, total_amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement psInsert = con.prepareStatement(insertQuery);
                    psInsert.setString(1, name);
                    psInsert.setInt(2, flightId);
                    psInsert.setInt(3, seatsRequested);
                    psInsert.setDouble(4, totalAmount);
                    psInsert.executeUpdate();

                    // Commit
                    con.commit();
                    System.out.println("Booking Successful!");
                } else {
                    // Not enough seats
                    con.rollback();
                    System.out.println("Booking Failed: Not enough seats available");
                }
            } else {
                con.rollback();
                System.out.println("Flight not found!");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}