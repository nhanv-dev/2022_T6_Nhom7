package com.controller;

import com.model.SourcePattern;
import com.model.FileLog;
import com.service.ICommodityService;
import com.service.IConfigurationService;
import com.service.IFileLogService;
import com.service.implement.CommodityService;
import com.service.implement.ConfigurationService;
import com.service.implement.FileLogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public void run(int configId, int authorId) {
        try {
            logger.info("Run...");
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
            FileLog fileLog = new FileLog(configId, authorId, path, sourcePattern.getCreatedDate());
            if (extracted) fileLog.setStatus(FileLog.EXTRACT_STATUS);
            else fileLog.setStatus(FileLog.ERROR_STATUS);
            long logId = fileLogService.insert(fileLog);
            logger.info("Insert file log with id = " + logId + ", status = " + fileLog.getStatus());
            if (!extracted) throw new Exception("Extract source unsuccessfully");
            if (logId == -1) throw new Exception("Insert into table file_log unsuccessfully");
            // Load to staging
            commodityService.truncateStaging();
            commodityService.loadToStaging(path);
            logger.info("Load to Staging successfully");
            // Transform staging
            Trasnformer trasnformer = new Trasnformer();
            commodityService.transformStaging();
            logger.info("Transform staging >>> Success = " + true);
            // Load to Data warehouse
            commodityService.loadToDataWarehouse();
            logger.info("Load to Data warehouse successfully");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static void main(String[] args) {
        new Main().run(1, 1);
    }
}
