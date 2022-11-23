package com.controller;

import com.model.Configuration;
import com.model.SourcePattern;
import com.model.FileLog;
import com.service.IConfigurationService;
import com.service.IFileLogService;
import com.service.implement.ConfigurationService;
import com.service.implement.FileLogService;
import com.util.DateFormatter;
import com.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;


public class Processor {
    private final Logger logger = LoggerUtil.getInstance(Processor.class);

    public void run(int configId, int authorId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        FTPConnector ftpConnector = new FTPConnector();
        long id = -1;
        try {
            // Find configuration by id
            SourcePattern sourcePattern = configurationService.findOne(configId);
            String name = sourcePattern.generateName();
            String localPath = sourcePattern.generateLocationPath();
            String remotePath = DateFormatter.generateRemoteFilePath(name);
            FileLog fileLog = new FileLog(configId, authorId, localPath, DateFormatter.formatCreatedDate(localPath), Configuration.getProperty("database.error_status"));
            id = fileLogService.insert(fileLog);
            ftpConnector.connect();

            // Extract data. If the server already has an extract file today, it will download the file.
            if (!ftpConnector.containFile(remotePath)) {
                if (!SourceProvider.extract(sourcePattern, localPath))
                    throw new Exception("Source " + sourcePattern.getSource() + " extract failed");
                ftpConnector.uploadFile(localPath, DateFormatter.generateDateFromFormatName(name, "-"), remotePath);
            } else {
                ftpConnector.downloadFile(remotePath, localPath);
            }
            fileLogService.updateStatus(id, Configuration.getProperty("database.extract_status"));

            // Load to staging
            if (!Loader.loadToStaging(localPath))
                throw new Exception("Source " + sourcePattern.getSource() + " load to staging failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.transform_status"));

            // Transform staging
            if (!Transformer.transform())
                throw new Exception("Source " + sourcePattern.getSource() + " transform failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.load_status"));

            // Load to Data warehouse
            if (!Loader.loadToDataWarehouse())
                throw new Exception("Source " + sourcePattern.getSource() + " load to data warehouse failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.done_status"));
        } catch (Exception e) {
            if (id != -1) fileLogService.updateStatus(id, Configuration.getProperty("database.transform_status"));
            e.printStackTrace();
            logger.error(e);
        }
    }

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
            // Load to staging
            if (!Loader.loadToStaging(localPath))
                throw new Exception("Source " + sourcePattern.getSource() + " load to staging failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.transform_status"));
            // Transform staging
            if (!Transformer.transform())
                throw new Exception("Source " + sourcePattern.getSource() + " transform failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.load_status"));
            // Load to Data warehouse
            if (!Loader.loadToDataWarehouse())
                throw new Exception("Source " + sourcePattern.getSource() + " load to data warehouse failed");
            fileLogService.updateStatus(id, Configuration.getProperty("database.done_status"));
        } catch (Exception e) {
            if (id != -1) fileLogService.updateStatus(id, Configuration.getProperty("database.transform_status"));
            e.printStackTrace();
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
            System.out.println(entry.getValue().getPath());
            new Processor().loadFileBackup(configId, authorId, entry.getValue());
        }
    }

    public static void main(String[] args) {
//        new Processor().loadAllFileBackup(1, "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\data");
        new Processor().run(1, 1);
        new Processor().run(2, 1);
    }
}
