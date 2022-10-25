package com.controller;

import com.model.Configuration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class SourceProvider {
    public boolean extract(Configuration configuration) {
        if (configuration.getProperty("method").equalsIgnoreCase("jsoup")) return this.extractBySelector(configuration);
        return false;
    }

    private boolean extractBySelector(Configuration configuration) {
        try {
            String path = modifiedPath(configuration.getProperty("directory"), configuration.getProperty("name"));
            File file = new File(path);
            if (file.exists()) file.delete();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            Document doc = Jsoup.connect(configuration.getSource()).get();
            Elements rows = doc.select(configuration.getProperty("selector_row"));
            for (Element row : rows) {
                StringBuilder output = new StringBuilder();
                for (Map.Entry<String, String> selector : configuration.getColumns().entrySet()) {
                    output.append(Objects.requireNonNull(row.select(selector.getValue()).first()).text().replaceAll("%", "")).append(",");
                }
                writer.println(output);
                writer.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String modifiedPath(String directory, String name) {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        return directory + "/" + name + "-" + dateFormat.format(date) + ".csv";
    }
}
