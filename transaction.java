import java.sql.*;
public class transaction {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/college";
        String user = "root";
        String password = "Aditya123";
        String withdraw = "update accounts set bal = bal - ? where acc = ?;";
        String deposit = "update accounts set bal = bal + ? where acc = ?;";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Established");

            connection.setAutoCommit(false);

            try{
            PreparedStatement withdrawStatement = connection.prepareStatement(withdraw);
            PreparedStatement depositStatement = connection.prepareStatement(deposit);

            withdrawStatement.setDouble(1, 1000);
            withdrawStatement.setInt(2, 1);
            depositStatement.setDouble(1, 1000);
            depositStatement.setInt(2, 2);

            withdrawStatement.executeUpdate();
            depositStatement.executeUpdate();

            connection.commit();
            System.out.println("Transaction Successfull");
        }catch(Exception e){
            
            connection.rollback();
            System.out.println("Transaction Failed");
        }
        }catch(Exception e){
            e.printStackTrace();
            
        }
            
    }
    
}