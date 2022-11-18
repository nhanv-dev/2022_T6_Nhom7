package com.dao.implement;

import com.model.Configuration;
import com.util.LoggerUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;

public class DatabaseConnector {
    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            username = Configuration.getProperty("database.username");
            password = Configuration.getProperty("database.password");
            url = Configuration.getProperty("database.driver");
        } catch (ClassNotFoundException e) {
            LoggerUtil.getInstance(DatabaseConnector.class).error(e);
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String database) {
        try {

            return DriverManager.getConnection(url + database, username, password);
        } catch (SQLException e) {
            LoggerUtil.getInstance(DatabaseConnector.class).error(e);
            e.printStackTrace();
            return null;
        }
    }
}
