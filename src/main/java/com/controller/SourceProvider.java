package com.controller;

import com.model.Commodity;
import com.model.SourcePattern;
import com.util.LoggerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Date;
import java.util.Map;

public class SourceProvider {
    public boolean extract(SourcePattern configuration, String path) throws Exception {
        if (configuration.getProperty("method") == null)
            throw new Exception("Configuration does not found extract method");
        if (configuration.getProperty("method").equalsIgnoreCase("jsoup"))
            return this.extractBySelector(configuration, path);
        return false;
    }

    private boolean extractBySelector(SourcePattern configuration, String path) throws IOException {
        File file = new File(path);
        if (file.exists()) file.delete();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        Document doc = Jsoup.connect(configuration.getSource()).get();
        Elements containers = doc.select(configuration.getProperty("selector_container"));
        for (Element container : containers) {
            int size = container.select(configuration.getProperty("selector_row")).size();
            for (int i = 0; i < size; i++) {
                try {
                    Element row = container.select(configuration.getProperty("selector_row")).get(i);
                    Commodity commodity = new Commodity();
                    commodity.setCreatedDate(new Date(System.currentTimeMillis()));
                    for (Map.Entry<String, String> selector : configuration.getColumns().entrySet()) {
                        Element element = row.select(selector.getValue()).first();
                        if (element == null) element = container.select(selector.getValue()).first();
                        if (element != null) {
                            String normalizeText = element.text().replaceAll("%|,", "");
                            commodity.setValue(selector.getKey().split("_")[2], normalizeText);
                        }
                    }
                    writer.println(commodity.print());
                    writer.flush();
                } catch (Exception e) {
                    LoggerUtil.getInstance(SourceProvider.class).error(e);
                }
            }
        }
        return true;
    }
}
