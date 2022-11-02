package com.controller;


import com.dao.ICommodityDao;
import com.dao.implement.CommodityDao;
import com.model.Configuration;
import com.model.FileLog;
import com.service.ICommodityService;
import com.service.IConfigurationService;
import com.service.IFileLogService;
import com.service.implement.CommodityService;
import com.service.implement.ConfigurationService;
import com.service.implement.FileLogService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void start(int configId) {
        IConfigurationService configurationService = new ConfigurationService();
        IFileLogService fileLogService = new FileLogService();
        ICommodityService commodityService = new CommodityService();

        // Find configuration by id
        Configuration configuration = configurationService.findOne(configId);
        // Generate path location
        String path = configuration.generatePath();
        System.out.println("Start >>> Path = " + path);
        SourceProvider provider = new SourceProvider();
        // Extract / Raw data
        boolean extracted = provider.extract(configuration, path);
        System.out.println("Extract >>> Success = " + extracted);
//        if (!extracted) {
//            fileLogService.insert(configId, path, FileLog.ERROR_STATUS, 1);
//            return;
//        }
//        // Insert file log when extract success
//        long logId = fileLogService.insert(configId, path, FileLog.EXTRACT_STATUS, 1);
//        System.out.println("Insert file log >>> ID = " + logId);
//        // Load to staging
//        commodityService.truncateStaging();
//        System.out.println("Truncate staging >>> Success = " + true);
//        commodityService.loadToStaging(path);
//        System.out.println("Load to staging >>> Success = " + true);
//        // Transform staging
//        Trasnformer trasnformer = new Trasnformer();
//
//        // Load to Data warehouse


    }

    public static void main(String[] args) {
        Main.start(2);
        System.out.println("Done...");

    }
}
