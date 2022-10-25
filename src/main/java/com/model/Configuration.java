package com.model;


import java.util.*;

public class Configuration {
    private String source;
    private Map<String, String> properties;

    public Configuration() {
        this.properties = new HashMap<>();
    }

    public Map<String, String> getColumns() {
        Map<String, String> selectors = new TreeMap<>();
        properties.forEach((key, value) -> {
            if (key.startsWith("selector_col")) selectors.put(key, value);
        });
        return selectors;
    }

    public void addProperty(String key, String value) throws Exception {
        if (properties.containsKey(key)) throw new Exception("Key already contains in properties");
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
}
