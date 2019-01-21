package lk.ijse.dep.fx.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if(connection==null){
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "root");
        }
        return connection;
    }
}
