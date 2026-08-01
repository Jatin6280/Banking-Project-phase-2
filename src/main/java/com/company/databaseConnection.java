package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {

    // Store these safely (more on this below)
    private static final String URL = "jdbc:mysql://localhost:3306/banking_app";
    private static final String USER = "root";
    private static final String PASSWORD = "Jatin6280492168";



    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Connection failed! Check your password or if MySQL is running.");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Connected successfully to the banking_app database!");
        }
    }
}