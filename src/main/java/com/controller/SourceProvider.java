package com.controller;

import com.model.Commodity;
import com.model.SourcePattern;
import com.service.IFileLogService;
import com.service.implement.FileLogService;
import com.util.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Date;
import java.util.Map;

public class SourceProvider {
    private static final Logger logger = LoggerUtil.getInstance(SourceProvider.class);

    public static boolean extract(SourcePattern sourcePattern, String path) throws Exception {
        logger.info("Start running extract " + sourcePattern.getSource());
        if (sourcePattern.getProperty("method") == null)
            throw new Exception("Configuration does not found extract method");
        if (sourcePattern.getProperty("method").equalsIgnoreCase("jsoup"))
            return extractBySelector(sourcePattern, path);
        return false;
    }

    private static boolean extractBySelector(SourcePattern sourcePattern, String path) {
        try {
            File file = new File(path);
            if (file.exists()) file.delete();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            Document doc = Jsoup.connect(sourcePattern.getSource()).get();
            Elements containers = doc.select(sourcePattern.getProperty("selector_container"));
            for (Element container : containers) {
                int size = container.select(sourcePattern.getProperty("selector_row")).size();
                for (int i = 0; i < size; i++) {
                    try {
                        Element row = container.select(sourcePattern.getProperty("selector_row")).get(i);
                        Commodity commodity = new Commodity();
                        commodity.setCreatedDate(new Date(System.currentTimeMillis()));
                        for (Map.Entry<String, String> selector : sourcePattern.getColumns().entrySet()) {
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
                        logger.error(e);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
    }
}
