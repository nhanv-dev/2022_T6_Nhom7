package com.controller;

import com.model.Configuration;
import com.model.SourcePattern;
import com.model.FileLog;
import com.service.*;
import com.service.implement.*;
import com.util.DateFormatter;
import com.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class Processor {
    private final Logger logger = LoggerUtil.getInstance(Processor.class);
    private final ISendMailError sendMailError = new SendErrorService();
    private String localPath = "";
    String remotePath  = "" ;

    public void run(int configId, int authorId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        SourcePattern sourcePattern = null;
        FileLog fileLog = null;
        // Find configuration by logId
        sourcePattern = configurationService.findOne(configId);
        localPath = sourcePattern.generateLocalPath();
        Date date = DateFormatter.formatCreatedDate(localPath);
        fileLog = fileLogService.findOne(configId, date);
        if (fileLog == null || !fileLog.getStatus().equalsIgnoreCase(Configuration.getProperty("database.done_status"))) {
            extract(configId, authorId, date);
            loadToStaging(configId, date);
            transform(configId, date);
            loadToDataWarehouse(configId, date);
        } else {
            logger.info("Data already exists in data warehouse");
        }
    }

    public void extract(long configId, long authorId, Date date) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        FTPConnector ftpConnector = new FTPConnector();
        SourcePattern sourcePattern = null;
        FileLog fileLog = null;

        try {
            fileLog = fileLogService.findOne(configId, date);
            if (fileLog != null && fileLog.getStatus().equalsIgnoreCase(Configuration.getProperty("database.extract_status")))
                return;
            sourcePattern = configurationService.findOne(configId);
            String name = sourcePattern.generateName();
            localPath = sourcePattern.generateLocalPath();
            String remotePath = DateFormatter.generateRemoteFilePath(name);

            if (fileLog == null) {
                fileLog = new FileLog(configId, authorId, localPath, DateFormatter.formatCreatedDate(localPath), Configuration.getProperty("database.error_status"));
                fileLog.setId(fileLogService.insert(fileLog));
            }
            ftpConnector.connect();
            // Extract data. If the server already has an extract file today, it will download the file.
            if (!ftpConnector.containFile(remotePath)) {
                if (!SourceProvider.extract(sourcePattern, localPath))
                    throw new Exception("Source " + sourcePattern.getSource() + " extract failed");
                ftpConnector.uploadFile(localPath, DateFormatter.generateDateFromFormatName(name, "-"), remotePath);
            } else {
                ftpConnector.downloadFile(remotePath, localPath);
            }
            fileLogService.updateStatus(fileLog.getId(), Configuration.getProperty("database.extract_status"));
            logger.info("Extract source " + sourcePattern.getSource() + " successfully");
        } catch (Exception exception) {
            if (fileLog != null)
                fileLogService.updateStatus(fileLog.getId(), Configuration.getProperty("database.error_status"));
            handleError(sourcePattern, localPath, exception, "Extract source failed");
        }
    }

    public void loadToStaging(int configId, Date date) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        ICommodityService commodityService = new CommodityService();
        SourcePattern sourcePattern = null;
        FileLog fileLog = null;
        try {
            fileLog = fileLogService.findOne(configId, date);
            if (fileLog != null && fileLog.getStatus().equalsIgnoreCase(Configuration.getProperty("database.extract_status"))) {
                 localPath = fileLog.getPath();
                sourcePattern = configurationService.findOne(configId);
                commodityService.truncateStaging();
                commodityService.loadToStaging(localPath);
                fileLogService.updateStatus(fileLog.getId(), Configuration.getProperty("database.transform_status"));
                logger.info("Load to staging source " + sourcePattern.getSource() + " successfully");
            }
        } catch (Exception exception) {
            if (fileLog != null)
                fileLogService.updateStatus(fileLog.getId(), Configuration.getProperty("database.error_status"));
            handleError(sourcePattern, localPath, exception, "Load to staging failed");
        }
    }

    public void transform(int configId, Date date) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        ICommodityService commodityService = new CommodityService();
        SourcePattern sourcePattern = null;
        FileLog fileLog = null;
        try {
            fileLog = fileLogService.findOne(configId, date);
            if (fileLog != null && fileLog.getStatus().equalsIgnoreCase(Configuration.getProperty("database.transform_status"))) {
                sourcePattern = configurationService.findOne(configId);
                commodityService.transformStaging();
                fileLogService.updateStatus(fileLog.getId(), Configuration.getProperty("database.load_status"));
                logger.info("Transform source " + sourcePattern.getSource() + " successfully");
            }
        } catch (Exception exception) {
            if (fileLog != null)
                fileLogService.updateStatus(fileLog.getId(), Configuration.getProperty("database.error_status"));
            handleError(sourcePattern, localPath, exception, "Transform source failed");
        }
    }

    public void loadToDataWarehouse(int configId, Date date) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        ICommodityService commodityService = new CommodityService();
        SourcePattern sourcePattern = null;
        FileLog fileLog = null;
        try {
            fileLog = fileLogService.findOne(configId, date);
            if (fileLog != null && fileLog.getStatus().equalsIgnoreCase(Configuration.getProperty("database.load_status"))) {
                fileLog = fileLogService.findOne(configId, date);
                sourcePattern = configurationService.findOne(configId);
                commodityService.loadToDataWarehouse();
                fileLogService.updateStatus(fileLog.getId(), Configuration.getProperty("database.done_status"));
                logger.info("Load into data warehouse source " + sourcePattern.getSource() + " successfully");
            }
        } catch (Exception exception) {
            if (fileLog != null)
                fileLogService.updateStatus(fileLog.getId(), Configuration.getProperty("database.error_status"));
            handleError(sourcePattern, localPath, exception, "Load into data warehouse failed");
        }
    }

    private void handleError(SourcePattern sourcePattern, String filePath, Exception exception, String message) {
        IAuthorService authorService = new AuthorService();
        List<String> emails = authorService.listEmailAuthor();
        if (exception != null) logger.error(exception);
        if (sourcePattern != null) logger.error(message);
        sendMailError.sendError(message, filePath, emails.toArray(new String[0]));
    }


    public static void main(String[] args) {
        new Processor().run(1, 1);
        new Processor().run(2, 1);
    }
}
