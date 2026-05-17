package com.clinic.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static String url = "jdbc:mysql://localhost:3306/clinic_system";
    private static String username = "root";
    private static String password = "";

    private DatabaseConnection() {
    }

    public static void setCredentials(String dbUrl, String dbUsername, String dbPassword) {
        url = dbUrl;
        username = dbUsername;
        password = dbPassword;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
