package com.model;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Configuration {
    private String source;
    private Map<String, String> properties;

    public Configuration() {
        this.properties = new TreeMap<>();
    }

    public String modifiedPath() {
        String directory = this.getProperty("directory");
        String name = this.getProperty("name");
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        return directory + "/" + name + "-" + dateFormat.format(date) + ".csv";
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("source = " + source);
        for (Map.Entry<String, String> entry : properties.entrySet())
            output.append("\n").append(entry.getKey()).append("=").append(entry.getValue());

        return output.toString();
    }

    public Map<String, String> getColumns() {
        Map<String, String> selectors = new TreeMap<>();
        properties.forEach((key, value) -> {
            if (key.startsWith("selector_col")) selectors.put(key, value);
        });
        return selectors;
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        if (properties.containsKey(key))
            return properties.get(key);
        return null;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
