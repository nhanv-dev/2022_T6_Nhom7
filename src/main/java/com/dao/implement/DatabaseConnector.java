package com.dao.implement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static final String STAGING = "staging";
    public static final String DATA_WAREHOUSE = "data_warehouse";
    public static final String CONTROLLER = "controller";
    private static final String URL = "jdbc:mysql:// localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(String database) {
        try {
            Connection connection = DriverManager.getConnection(URL + database, USER, PASSWORD);
            System.out.println("Connection Successfully: " + connection.getCatalog());
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Failed!!!");
            return null;
        }
    }
}
