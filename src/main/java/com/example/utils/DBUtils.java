package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DBUtils {
    public static final Dotenv dotenv = Dotenv.configure().load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USERNAME");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");
    private static final String DRIVER_CLASS = dotenv.get("DRIVER_CLASS");

    // Method for creating database connection
    public static Connection getConnection() throws SQLException {
        Connection connection;
        try {
            Class.forName(DRIVER_CLASS);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new SQLException("Cannot connect to db", e);
        }
        return connection;
    }
}
