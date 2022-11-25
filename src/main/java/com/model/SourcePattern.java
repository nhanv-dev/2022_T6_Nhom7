package com.model;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SourcePattern {
    private long id;
    private String source;
    private Map<String, String> properties;
    private Date createdDate;

    public SourcePattern() {
        this.properties = new TreeMap<>();
        this.createdDate = new Date(System.currentTimeMillis());
    }

    public String generateName() {
        try {
            String name = this.getProperty("name");
            if (name == null) throw new Exception("Configuration name attribute not found");
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            return name + "-" + dateFormat.format(createdDate) + ".csv";
        } catch (Exception e) {
            return null;
        }
    }

    public String generateLocalPath() {
        try {
            String name = this.getProperty("name");
            if (name == null) throw new Exception("Configuration name attribute not found");
            String directory = this.getProperty("directory");
            if (directory == null) throw new Exception("Configuration directory attribute not found");
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            return directory + "/" + name + "-" + dateFormat.format(createdDate) + ".csv";
        } catch (Exception e) {
            return null;
        }
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

    public void put(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        if (properties.containsKey(key))
            return properties.get(key);
        return null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
