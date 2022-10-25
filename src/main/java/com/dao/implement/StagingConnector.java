package com.dao.implement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StagingConnector {
    private static Connection connection = null;
    private static final String DATABASE = "staging";
    private static final String URL = "jdbc:mysql:// localhost:3306/" + DATABASE;
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection Successfully: " + connection.getCatalog());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Failed!!!");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
