package com.dao.implement;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;

public class DatabaseConnector {
    public static String STAGING = "staging";
    public static String DATA_WAREHOUSE = "data_warehouse";
    public static String CONTROLLER = "controller";
    private static String URL = "jdbc:mysql://localhost:3306/";
    private static String username;
    private static String password;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream(".config");
            prop.load(fis);
            username = prop.getProperty("database.username");
            password = prop.getProperty("database.password");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(String database) {
        try {
            Connection connection = DriverManager.getConnection(URL + database, username, password);
            System.out.println("Connection Successfully: " + connection.getCatalog());
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Failed!!!");
            return null;
        }
    }
}
