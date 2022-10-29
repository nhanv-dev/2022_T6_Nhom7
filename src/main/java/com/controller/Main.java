package com.controller;


import com.dao.ICommodityDao;
import com.dao.implement.CommodityDao;
import com.model.Configuration;
import com.model.FileLog;
import com.service.IConfigurationService;
import com.service.IFileLogService;
import com.service.implement.ConfigurationService;
import com.service.implement.FileLogService;

public class Main {
    public static void start(int configId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        ICommodityDao commodityDao = new CommodityDao();

        // Find configuration by id
        Configuration configuration = configurationService.findOne(configId);
        String path = configuration.modifiedPath();
        System.out.println("Start >>> Path=" + path);
        SourceProvider provider = new SourceProvider();
        // Extract / Crawl data from source
        boolean extracted = provider.extract(configuration, path);
        if (!extracted) {
            fileLogService.insert(configId, path, FileLog.ERROR_STATUS, 1);
//            FileLog fileLog = new FileLog(1, configId, 1, path, FileLog.EXTRACT_SUCCESS);
            return;
        }
        // Insert file log when extract success
        int logId = fileLogService.insert(configId, path, FileLog.EXTRACT_STATUS, 1);

        // Load to staging
        commodityDao.loadDataInFile(path);


        Trasnformer trasnformer = new Trasnformer();


    }

    public static void main(String[] args) throws Exception {
        Main.start(1);
        System.out.println("Done...");
    }
}
