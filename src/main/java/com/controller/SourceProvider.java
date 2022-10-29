package com.controller;

import com.model.Configuration;
import com.util.PathGenerator;
import com.util.SlugGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SourceProvider {

    public boolean extract(Configuration configuration, String path) {
        if (configuration.getProperty("method").equalsIgnoreCase("jsoup"))
            return this.extractBySelector(configuration, path);
        return false;
    }

    private boolean extractBySelector(Configuration configuration, String path) {
        try {
            File file = new File(path);
            if (file.exists()) file.delete();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            Document doc = Jsoup.connect(configuration.getSource()).get();
//            Element container= doc.select(configuration.getProperty("selector_container")).first();
            Elements rows = doc.select(configuration.getProperty("selector_row"));
            for (Element row : rows) {
                StringBuilder output = new StringBuilder();
                String naturalKey = configuration.getProperty("natural_key");
                boolean hasCreatedDate = configuration.getProperty("created_date") != null;
                for (Map.Entry<String, String> selector : configuration.getColumns().entrySet()) {
                    Element element = row.select(selector.getValue()).first();
                    if (element != null) {
                        if (selector.getKey().equalsIgnoreCase(naturalKey))
                            output.append(SlugGenerator.toSlug(element.text())).append(",");
                        output.append(element.text().replaceAll("%|,", "")).append(",");
                    }
                }
                if (hasCreatedDate) output.append(this.getCreatedDate());
                writer.println(output);
                writer.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getCreatedDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return dateFormat.format(date);
    }

}
