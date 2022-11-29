package com.controller;

import com.model.Configuration;
import com.model.FileLog;
import com.model.SourcePattern;
import com.service.ICommodityService;
import com.service.IConfigurationService;
import com.service.IFileLogService;
import com.service.ISendMailError;
import com.service.implement.CommodityService;
import com.service.implement.ConfigurationService;
import com.service.implement.FileLogService;
import com.service.implement.SendErrorService;
import com.util.DateFormatter;
import com.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class Backup {
    private final Logger logger = LoggerUtil.getInstance(Backup.class);

    public void loadFileBackup(int configId, int authorId, File file) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        FTPConnector ftpConnector = new FTPConnector();
        long id = -1;
        try {
            ftpConnector.connect();
            // Find configuration by id
            SourcePattern sourcePattern = configurationService.findOne(configId);
            String name = file.getName();
            String localPath = file.getPath();
            String remotePath = DateFormatter.generateRemoteFilePath(name);
            if (!ftpConnector.containFile(remotePath))
                ftpConnector.uploadFile(localPath, DateFormatter.generateDateFromFormatName(name, "-"), remotePath);
            FileLog fileLog = new FileLog(configId, authorId, localPath, DateFormatter.formatCreatedDate(localPath), Configuration.getProperty("database.extract_status"));
            id = fileLogService.insert(fileLog);
            Loader loader = new Loader();
            // Load to staging
            if (!loader.loadToStaging(localPath))
                throw new Exception("Source " + sourcePattern.getSource() + " load to staging failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.transform_status"));
            // Transform staging
            if (!loader.transform())
                throw new Exception("Source " + sourcePattern.getSource() + " transform failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.load_status"));
            // Load to Data warehouse
            if (!loader.loadToDataWarehouse())
                throw new Exception("Source " + sourcePattern.getSource() + " load to data warehouse failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.done_status"));
        } catch (Exception e) {
            if (id != -1) fileLogService.updateStatus(id, Configuration.getProperty("database.transform_status"));
            logger.error(e);
        }
    }

    public void loadAllFileBackup(int authorId, String dirPath) {
        logger.info("Run load all file");
        File directory = new File(dirPath);
        Map<String, File> map = new TreeMap<>();
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            map.put(DateFormatter.generateDateFromFormatName(file.getName(), "") + "-" + file.getName(), file);
        }
        for (Map.Entry<String, File> entry : map.entrySet()) {
            int configId = 1;
            if (entry.getValue().getName().startsWith("business")) configId = 2;
            new Backup().loadFileBackup(configId, authorId, entry.getValue());
        }
    }

    public static void main(String[] args) {
        new Backup().loadAllFileBackup(1, "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\data");
    }

    static class Loader {
        private final Logger logger = LoggerUtil.getInstance(Loader.class);
        private final ICommodityService commodityService = new CommodityService();

        public boolean loadToStaging(String path) {
            try {
                commodityService.truncateStaging();
                commodityService.loadToStaging(path);
                logger.info("Load to staging successfully");
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public boolean loadToDataWarehouse() {
            try {
                commodityService.loadToDataWarehouse();
                logger.info("Load to data warehouse successfully");
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public boolean transform() {
            try {
                commodityService.transformStaging();
                logger.info("Transform staging successfully");
                return true;
            } catch (Exception e) {
                return false;
            }
        }

    }
}
