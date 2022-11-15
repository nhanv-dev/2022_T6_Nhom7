package com.controller;

import com.model.SourcePattern;
import com.model.FileLog;
import com.service.ICommodityService;
import com.service.IConfigurationService;
import com.service.IFileLogService;
import com.service.implement.CommodityService;
import com.service.implement.ConfigurationService;
import com.service.implement.FileLogService;
import com.util.LoggerUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static Date createdDate(String path) throws ParseException {
        String time = path.split("-")[1].replace(".csv", "");
        Date date = new SimpleDateFormat("yyyyMMddhhmmss").parse(time);
        return date;
    }

    public void run(int configId, int authorId) {
        try {
            LoggerUtil.getInstance(Main.class).info("Run...");
            IConfigurationService configurationService = new ConfigurationService();
            IFileLogService fileLogService = new FileLogService();
            ICommodityService commodityService = new CommodityService();
            // Find configuration by id
            SourcePattern sourcePattern = configurationService.findOne(configId);
            if (sourcePattern == null) throw new Exception("Configuration not found");
            // Generate path location
            String path = sourcePattern.generatePath();
            SourceProvider provider = new SourceProvider();
            // Extract / Raw data
            boolean extracted = provider.extract(sourcePattern, path);
            // Insert file log
            FileLog fileLog = new FileLog(configId, authorId, path, Main.createdDate(path));
            if (extracted) fileLog.setStatus(FileLog.EXTRACT_STATUS);
            else fileLog.setStatus(FileLog.ERROR_STATUS);
            long logId = fileLogService.insert(fileLog);
            LoggerUtil.getInstance(Main.class).info("Insert file log with id = " + logId + ", status = " + fileLog.getStatus());
            if (logId == -1) throw new Exception("Insert into table file_log unsuccessfully");
            if (!extracted) return;
            // Load to staging
            commodityService.truncateStaging();
            commodityService.loadToStaging(path);
            LoggerUtil.getInstance(Main.class).info("Load to Staging successfully");
            // Transform staging
            Trasnformer trasnformer = new Trasnformer();
            commodityService.transformStaging();
            LoggerUtil.getInstance(Main.class).info("Transform staging >>> Success = " + true);
            // Load to Data warehouse
            commodityService.loadToDataWarehouse();
            LoggerUtil.getInstance(Main.class).info("Load to Data warehouse successfully");
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.getInstance(Main.class).error(e);
        }
    }

    public void backup(int configId, int authorId, String path) {
        try {
            LoggerUtil.getInstance(Main.class).info("Run...");
            IConfigurationService configurationService = new ConfigurationService();
            IFileLogService fileLogService = new FileLogService();
            ICommodityService commodityService = new CommodityService();
            // Find configuration by id
            SourcePattern sourcePattern = configurationService.findOne(configId);
            if (sourcePattern == null) throw new Exception("Configuration not found");
            // Insert file log
            FileLog fileLog = new FileLog(configId, authorId, path, Main.createdDate(path));
            if (true) fileLog.setStatus(FileLog.EXTRACT_STATUS);
            else fileLog.setStatus(FileLog.ERROR_STATUS);
            long logId = fileLogService.insert(fileLog);
            LoggerUtil.getInstance(Main.class).info("Insert file log with id = " + logId + ", status = " + fileLog.getStatus());
            if (logId == -1) throw new Exception("Insert into table file_log unsuccessfully");
            if (!true) return;
            // Load to staging
            commodityService.truncateStaging();
            commodityService.loadToStaging(path);
            LoggerUtil.getInstance(Main.class).info("Load to Staging successfully");
            // Transform staging
            Trasnformer trasnformer = new Trasnformer();
            commodityService.transformStaging();
            LoggerUtil.getInstance(Main.class).info("Transform staging >>> Success = " + true);
            // Load to Data warehouse
            commodityService.loadToDataWarehouse();
            LoggerUtil.getInstance(Main.class).info("Load to Data warehouse successfully");
        } catch (Exception e) {
            LoggerUtil.getInstance(Main.class).error(e);
        }
    }

    public static void main(String[] args) {
        String path = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\data\\tradingeconomics-20220917045734.csv";
        new Main().run(1, 1);
        new Main().run(2, 1);

    }
}
