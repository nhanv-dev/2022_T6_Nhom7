package com.controller;

import com.model.Commodity;
import com.model.SourcePattern;
import com.model.FileLog;
import com.service.ICommodityService;
import com.service.IConfigurationService;
import com.service.IFileLogService;
import com.service.implement.CommodityService;
import com.service.implement.ConfigurationService;
import com.service.implement.FileLogService;
import com.util.FormatterUtil;
import com.util.LoggerUtil;

import java.io.File;
import java.util.*;

public class Processor {

    public void run(int configId, int authorId) {
        try {
            LoggerUtil.getInstance(Processor.class).info("Run processor");
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
            FileLog fileLog = new FileLog(configId, authorId, path, FormatterUtil.createdFormatDate(path));
            if (extracted) fileLog.setStatus(FileLog.EXTRACT_STATUS);
            else fileLog.setStatus(FileLog.ERROR_STATUS);
            long logId = fileLogService.insert(fileLog);
            LoggerUtil.getInstance(Processor.class).info("Insert file log with id = " + logId + ", status = " + fileLog.getStatus());
            if (logId == -1) throw new Exception("Insert into table file_log unsuccessfully");
            if (!extracted) return;
            // Load to staging
            commodityService.truncateStaging();
            commodityService.loadToStaging(path);
            LoggerUtil.getInstance(Processor.class).info("Load to Staging successfully");
            // Transform staging
            Trasnformer trasnformer = new Trasnformer();
            commodityService.transformStaging();
            LoggerUtil.getInstance(Processor.class).info("Transform staging >>> Success = " + true);
            // Load to Data warehouse
            commodityService.loadToDataWarehouse();
            LoggerUtil.getInstance(Processor.class).info("Load to Data warehouse successfully");
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.getInstance(Processor.class).error(e);
        }
    }
    public void backup(int configId, int authorId, String path) {
        try {
            LoggerUtil.getInstance(Processor.class).info("Run backup");
            IConfigurationService configurationService = new ConfigurationService();
            IFileLogService fileLogService = new FileLogService();
            ICommodityService commodityService = new CommodityService();
            // Find configuration by id
            SourcePattern sourcePattern = configurationService.findOne(configId);
            if (sourcePattern == null) throw new Exception("Configuration not found");
            // Insert file log
            FileLog fileLog = new FileLog(configId, authorId, path, FormatterUtil.createdFormatDate(path));
            fileLog.setStatus(FileLog.EXTRACT_STATUS);
            long logId = fileLogService.insert(fileLog);
            LoggerUtil.getInstance(Processor.class).info("Insert new file log with id = " + logId + ", status = " + fileLog.getStatus());
            if (logId == -1) throw new Exception("Insert into table file_log unsuccessfully");
            // Load to staging
            commodityService.truncateStaging();
            commodityService.loadToStaging(path);
            fileLogService.updateStatus(fileLog.getId(), FileLog.TRANSFORM_STATUS);
            LoggerUtil.getInstance(Processor.class).info("Load file to staging successfully");
            // Transform staging
            Trasnformer trasnformer = new Trasnformer();
            commodityService.transformStaging();
            fileLogService.updateStatus(fileLog.getId(), FileLog.LOAD_STATUS);
            LoggerUtil.getInstance(Processor.class).info("Transform staging successfully");
            // Load to Data warehouse
            commodityService.loadToDataWarehouse();
            fileLogService.updateStatus(fileLog.getId(), FileLog.DONE_STATUS);
            LoggerUtil.getInstance(Processor.class).info("Load staging to data warehouse successfully");
        } catch (Exception e) {
            LoggerUtil.getInstance(Processor.class).error(e);
        }
    }
    public static void loadAllFile(String dirPath) {
        LoggerUtil.getInstance(Processor.class).info("Run load all file");
        File directory = new File(dirPath);
        Map<String, File> map = new TreeMap<>();
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            String date = FormatterUtil.getDateFromFormatName(file.getName());
            map.put(date + "-" + file.getName(), file);
        }
        for (Map.Entry<String, File> entry : map.entrySet()) {
            new Processor().backup(1, 1, entry.getValue().getPath());
        }
    }
    public static void main(String[] args) throws InterruptedException {

    }
}
