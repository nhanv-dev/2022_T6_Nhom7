package com.dao.implement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class DatabaseConnector {
    public static final String STAGING = "staging";
    public static final String DATA_WAREHOUSE = "data_warehouse";
    public static final String CONTROLLER = "controller";
    private static final String URL = "jdbc:mysql:// localhost:3306/";
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            BufferedReader br = new BufferedReader(new FileReader(".config"));
            String line = "";
            int count = 0;
            while((line = br.readLine()) != null && count < 1) {
                StringTokenizer stk = new StringTokenizer(line, " ");

                while(stk.hasMoreTokens()){
                    USER = stk.nextToken().trim();
                    PASSWORD = stk.nextToken().trim();
                }
                count++;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(String database) {
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
