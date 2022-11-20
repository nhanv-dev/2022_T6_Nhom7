package com.controller;

import com.model.Configuration;
import com.service.ICommodityService;
import com.service.IFileLogService;
import com.service.implement.CommodityService;
import com.service.implement.FileLogService;
import com.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

public class Loader {
    private static final Logger logger = LoggerUtil.getInstance(Loader.class);
    private static final ICommodityService commodityService = new CommodityService();

    public static boolean loadToStaging(String path) {
        try {
            commodityService.truncateStaging();
            commodityService.loadToStaging(path);
            logger.info("Load to staging successfully");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean loadToDataWarehouse() {
        try {
            commodityService.loadToDataWarehouse();
            logger.info("Load to data warehouse successfully");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
