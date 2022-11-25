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

    public void run(int configId, int authorId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        SourcePattern sourcePattern = null;
        long logId = -1;
        try {
            // Find configuration by logId
            sourcePattern = configurationService.findOne(configId);
            String localPath = sourcePattern.generateLocalPath();
            if (!exist(configId, DateFormatter.formatCreatedDate(localPath))) {
                FileLog fileLog = new FileLog(configId, authorId, localPath, DateFormatter.formatCreatedDate(localPath), Configuration.getProperty("database.error_status"));
                logId = fileLogService.insert(fileLog);
                extract(configId, logId);
                loadToStaging(configId, logId);
                transform(configId, logId);
                loadToDataWarehouse(configId, logId);
            } else {
                logger.info("File log exist in database " + sourcePattern.getSource() + " - " + sourcePattern.getCreatedDate());
            }
        } catch (Exception exception) {
            handleError(logId, sourcePattern, exception, "Process run failed");
        }
    }

    public boolean exist(long configId, Date date) {
        IFileLogService fileLogService = new FileLogService();
        return fileLogService.findOne(configId, date) != null;
    }

    public void extract(int configId, long logId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        FTPConnector ftpConnector = new FTPConnector();
        SourcePattern sourcePattern = null;
        try {
            sourcePattern = configurationService.findOne(configId);
            String name = sourcePattern.generateName();
            String localPath = sourcePattern.generateLocalPath();
            String remotePath = DateFormatter.generateRemoteFilePath(name);
            ftpConnector.connect();
            // Extract data. If the server already has an extract file today, it will download the file.
            if (!ftpConnector.containFile(remotePath)) {
                if (!SourceProvider.extract(sourcePattern, localPath))
                    throw new Exception("Source " + sourcePattern.getSource() + " extract failed");
                ftpConnector.uploadFile(localPath, DateFormatter.generateDateFromFormatName(name, "-"), remotePath);
            } else {
                ftpConnector.downloadFile(remotePath, localPath);
            }
            fileLogService.updateStatus(logId, Configuration.getProperty("database.extract_status"));
            logger.info("Extract source " + sourcePattern.getSource() + " successfully");
        } catch (Exception exception) {
            handleError(logId, sourcePattern, exception, "Extract source  failed");
        }
    }

    public void loadToStaging(int configId, long logId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        ICommodityService commodityService = new CommodityService();
        SourcePattern sourcePattern = null;
        try {
            sourcePattern = configurationService.findOne(configId);
            String localPath = sourcePattern.generateLocalPath();
            commodityService.truncateStaging();
            commodityService.loadToStaging(localPath);
            fileLogService.updateStatus(logId, Configuration.getProperty("database.transform_status"));
            logger.info("Load to staging source " + sourcePattern.getSource() + " successfully");
        } catch (Exception exception) {
            handleError(logId, sourcePattern, exception, "Load to staging failed");
        }
    }

    public void transform(int configId, long logId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        ICommodityService commodityService = new CommodityService();
        SourcePattern sourcePattern = null;
        try {
            sourcePattern = configurationService.findOne(configId);
            commodityService.transformStaging();
            fileLogService.updateStatus(logId, Configuration.getProperty("database.load_status"));
            logger.info("Transform source " + sourcePattern.getSource() + " successfully");
        } catch (Exception exception) {
            handleError(logId, sourcePattern, exception, "Transform source failed");
        }
    }

    public void loadToDataWarehouse(int configId, long logId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        ICommodityService commodityService = new CommodityService();
        SourcePattern sourcePattern = null;
        try {
            sourcePattern = configurationService.findOne(configId);
            commodityService.loadToDataWarehouse();
            fileLogService.updateStatus(logId, Configuration.getProperty("database.done_status"));
            logger.info("Load to data warehouse source " + sourcePattern.getSource() + " successfully");
        } catch (Exception exception) {
            handleError(logId, sourcePattern, exception, "Load to data warehouse failed");
        }
    }

    private void handleError(long logId, SourcePattern sourcePattern, Exception exception, String message) {
        IAuthorService authorService = new AuthorService();
        IFileLogService fileLogService = new FileLogService();
        List<String> emails = authorService.listEmailAuthor();
        fileLogService.updateStatus(logId, Configuration.getProperty("database.error_status"));
        if (exception != null) logger.error(exception);
        if (sourcePattern != null) logger.error(message);
        sendMailError.sendError(message, "", emails.toArray(new String[0]));
    }


    public static void main(String[] args) {
        new Processor().run(1, 1);
        new Processor().run(2, 1);
    }
}
