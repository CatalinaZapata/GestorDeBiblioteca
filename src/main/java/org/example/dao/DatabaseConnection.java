package org.example.dao;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static DatabaseConnection instance;
    private java.sql.Connection connection;

    private DatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public java.sql.Connection getConnection(){ return connection;}
}