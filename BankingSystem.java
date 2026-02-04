import java.sql.*;
import java.util.Scanner;

public class BankingSystem {

    static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    static final String USER = "root";
    static final String PASSWORD = "Aditya123";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            con.setAutoCommit(false);

            while (true) {
                System.out.println("\n--- BANKING SYSTEM ---");
                System.out.println("1. Add New Customer");
                System.out.println("2. Update Customer");
                System.out.println("3. Delete Customer");
                System.out.println("4. Transfer Money");
                System.out.println("5. View Accounts");
                System.out.println("6. Exit");
                System.out.print("Choose option: ");

                int choice = sc.nextInt();

                switch (choice) {

                    case 1: // Insert
                        System.out.print("Account No: ");
                        int acc = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Balance: ");
                        double bal = sc.nextDouble();

                        PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO accounts VALUES (?, ?, ?)");
                        insert.setInt(1, acc);
                        insert.setString(2, name);
                        insert.setDouble(3, bal);
                        insert.executeUpdate();

                        con.commit();
                        System.out.println("Customer Added Successfully");
                        break;

                    case 2: // Update
                        System.out.print("Account No: ");
                        acc = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Name: ");
                        name = sc.nextLine();
                        System.out.print("New Balance: ");
                        bal = sc.nextDouble();

                        PreparedStatement update = con.prepareStatement(
                                "UPDATE accounts SET name=?, bal=? WHERE acc=?");
                        update.setString(1, name);
                        update.setDouble(2, bal);
                        update.setInt(3, acc);

                        if (update.executeUpdate() == 0) {
                            con.rollback();
                            System.out.println("Account Not Found");
                        } else {
                            con.commit();
                            System.out.println("Account Updated");
                        }
                        break;

                    case 3: // Delete
                        System.out.print("Account No: ");
                        acc = sc.nextInt();

                        PreparedStatement delete = con.prepareStatement(
                                "DELETE FROM accounts WHERE acc=?");
                        delete.setInt(1, acc);

                        if (delete.executeUpdate() == 0) {
                            con.rollback();
                            System.out.println("Account Not Found");
                        } else {
                            con.commit();
                            System.out.println("Account Deleted");
                        }
                        break;

                    case 4: // Transfer
                        System.out.print("From Account: ");
                        int fromAcc = sc.nextInt();
                        System.out.print("To Account: ");
                        int toAcc = sc.nextInt();
                        System.out.print("Amount: ");
                        double amount = sc.nextDouble();

                        PreparedStatement check = con.prepareStatement(
                                "SELECT bal FROM accounts WHERE acc=?");

                        check.setInt(1, fromAcc);
                        ResultSet rs = check.executeQuery();

                        if (!rs.next() || rs.getDouble("bal") <= 0 || rs.getDouble("bal") < amount) {
                            con.rollback();
                            System.out.println("Insufficient Balance or Invalid Account");
                            break;
                        }

                        check.setInt(1, toAcc);
                        rs = check.executeQuery();

                        if (!rs.next()) {
                            con.rollback();
                            System.out.println("Receiver Account Not Found");
                            break;
                        }

                        PreparedStatement withdraw = con.prepareStatement(
                                "UPDATE accounts SET bal = bal - ? WHERE acc=?");
                        withdraw.setDouble(1, amount);
                        withdraw.setInt(2, fromAcc);
                        withdraw.executeUpdate();

                        PreparedStatement deposit = con.prepareStatement(
                                "UPDATE accounts SET bal = bal + ? WHERE acc=?");
                        deposit.setDouble(1, amount);
                        deposit.setInt(2, toAcc);
                        deposit.executeUpdate();

                        con.commit();
                        System.out.println("Transaction Successful");
                        break;

                    case 5: // View
                        Statement st = con.createStatement();
                        ResultSet view = st.executeQuery("SELECT * FROM accounts");

                        System.out.println("\nACC\tNAME\tBALANCE");
                        while (view.next()) {
                            System.out.println(
                                    view.getInt(1) + "\t" +
                                    view.getString(2) + "\t" +
                                    view.getDouble(3));
                        }
                        break;

                    case 6:
                        con.close();
                        System.out.println("Thank You!");
                        System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
