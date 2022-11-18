package com.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Configuration {
    private static final Logger logger = LogManager.getLogger(Configuration.class);
    private static Map<String, String> properties;

    static {
        try {
            properties = new TreeMap<>();
            Properties prop = new Properties();
            InputStream application = Configuration.class.getClassLoader().getResourceAsStream("application.properties");
            InputStream database = Configuration.class.getClassLoader().getResourceAsStream("database.properties");
            prop.load(application);
            prop.load(database);
            for (Map.Entry<Object, Object> entry : prop.entrySet())
                properties.put((String) entry.getKey(), (String) entry.getValue());
        } catch (IOException e) {
            logger.info(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, String> entry : properties.entrySet())
            output.append("\n").append(entry.getKey()).append("=").append(entry.getValue());
        return output.toString();
    }

    public static void put(String key, String value) {
        properties.put(key, value);
    }

    public static String getProperty(String key) {
        if (properties.containsKey(key))
            return properties.get(key);
        return null;
    }
}
