package com.controller;

import com.model.Configuration;
import com.service.ICommodityService;
import com.service.IFileLogService;
import com.service.implement.CommodityService;
import com.service.implement.FileLogService;
import com.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

public class Transformer {
    private static final Logger logger = LoggerUtil.getInstance(Transformer.class);
    private static final ICommodityService commodityService = new CommodityService();

    public static boolean transform() {
        try {
            commodityService.transformStaging();
            logger.info("Transform staging successfully");
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
