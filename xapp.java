import java.sql.*;
import java.util.Scanner;

public class xapp {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/college";
        String user = "root";
        String password = "Aditya123";
        String query = "INSERT INTO student(id, name, marks) VALUES (?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");

            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Established");

            PreparedStatement ps = con.prepareStatement(query);
            Scanner sc = new Scanner(System.in);

            char choice;
            do {
                System.out.print("Enter ID: ");
                int id = sc.nextInt();
                sc.nextLine(); 

                System.out.print("Enter Name: ");
                String name = sc.nextLine().toUpperCase(); 

                System.out.print("Enter Marks: ");
                int marks = sc.nextInt();

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setInt(3, marks);

                ps.executeUpdate();
                System.out.println("Data Inserted Successfully ✅");

                System.out.print("Do you want to insert more data? (y/n): ");
                choice = sc.next().charAt(0);

            } while (choice == 'y' || choice == 'Y');

            ps.close();
            con.close();
            sc.close();

            System.out.println("Program Ended");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
