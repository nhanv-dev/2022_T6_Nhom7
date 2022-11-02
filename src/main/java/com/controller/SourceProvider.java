package com.controller;

import com.model.Commodity;
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
            Elements containers = doc.select(configuration.getProperty("selector_container"));
            for (Element container : containers) {
                int size = container.select(configuration.getProperty("selector_row")).size();
                for (int i = 0; i < size; i++) {
                    StringBuilder output = new StringBuilder();
                    Element row = container.select(configuration.getProperty("selector_row")).get(i);
                    Commodity commodity = new Commodity();
                    commodity.setCreatedDate(new Date(System.currentTimeMillis()));
                    for (Map.Entry<String, String> selector : configuration.getColumns().entrySet()) {
                        Element element = row.select(selector.getValue()).first();
                        if (element == null) element = container.select(selector.getValue()).first();
                        if (element == null) {
                            output.append(",");
                        } else {
                            String normalizeText = element.text().replaceAll("%|,", "");
                            commodity.setValue(selector.getKey().split("_")[2], normalizeText);
                        }
                    }
                    writer.println(commodity.toString());
//                    writer.println(output);
                    writer.flush();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
