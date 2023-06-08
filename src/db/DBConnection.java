package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author : Gihan Madhusankha
 * 2022-05-30 10:36 AM
 **/

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Shop", "root", "1234");
    }

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        return dbConnection == null ? dbConnection = new DBConnection() : dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }


}
