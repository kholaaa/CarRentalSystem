package car.rental.system;

import java.sql.*;

public class DBConnection {

    // Change your DB URL, user, password as needed
    static final String URL = "root@127.0.0.1:3306";
    static final String USER = "root";
    static final String PASSWORD = "ot10forever10@";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}