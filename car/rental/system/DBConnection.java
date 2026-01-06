package car.rental.system;

import java.sql.*;

public class DBConnection {

    static final String URL = "jdbc:mysql://localhost:3306/CarRental?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASSWORD = "ot10forever10@";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load the MySQL driver (important!)
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connection successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
