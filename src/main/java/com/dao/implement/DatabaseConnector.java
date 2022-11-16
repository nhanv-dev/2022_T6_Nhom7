package com.dao.implement;

import com.model.Configuration;

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
            username = Configuration.getProperty("database.username");
            password = Configuration.getProperty("database.password");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String database) {
        try {
            return DriverManager.getConnection(URL + database, "root", "admin");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
