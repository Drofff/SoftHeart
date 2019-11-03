package com.softHeart.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Connection {

    private static java.sql.Connection connection;
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String CONNECTION_URI = "jdbc:mysql://localhost:3306/soft_heart?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection() {}

    public static java.sql.Connection getConnection() throws SQLException, ClassNotFoundException {
        if(isConnectionClosed()) {
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection(CONNECTION_URI, USERNAME, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if(!isConnectionClosed()) {
            connection.close();
        }
    }

    private static boolean isConnectionClosed() throws SQLException {
        return Objects.isNull(connection) || connection.isClosed();
    }

}
