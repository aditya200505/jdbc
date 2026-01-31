import java.sql.*;
import java.util.Scanner;

public class hotelreservation{

    
    static String URL = "jdbc:mysql://localhost:3306/?user=root";
    static String USER = "root";
    static String PASS = "Aditya123";

    static Connection con;

    static void connectDB() {
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Database Connected Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    static void allocateRoom(String name, String contact, int room) {
        try {
            String sql = "INSERT INTO reservation (guest_name, contact_no, room_no, status) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, contact);
            ps.setInt(3, room);
            ps.setString(4, "CHECKED-IN");

            ps.executeUpdate();
            System.out.println("Room Allocated Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void retrieveReservations() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM reservation");

            System.out.println("\nID | Name | Room | Check-in Time | Status");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("reservation_id") + " | " +
                    rs.getString("guest_name") + " | " +
                    rs.getInt("room_no") + " | " +
                    rs.getTimestamp("check_in_time") + " | " +
                    rs.getString("status")
                );
            }
        } 
        catch (Exception e) {
           
            e.printStackTrace();
        }
    }


    static void checkOut(int id) {
        try {
            String sql = "UPDATE reservation SET status='CHECKED-OUT' WHERE reservation_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Checked Out Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        connectDB();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- HOTEL RESERVATION SYSTEM ---");
            System.out.println("1. Allocate Room");
            System.out.println("2. View Reservations");
            System.out.println("3. Check Out");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Guest Name: ");
                    String name = sc.next();
                    System.out.print("Contact No: ");
                    String contact = sc.next();
                    System.out.print("Room No: ");
                    int room = sc.nextInt();
                    allocateRoom(name, contact, room);
                    break;

                case 2:
                    retrieveReservations();
                    break;

                case 3:
                    System.out.print("Reservation ID: ");
                    int id = sc.nextInt();
                    checkOut(id);
                    break;

                case 4:
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice");
            }
            sc.close();
        }
        
    }
}