package com.util;

import com.controller.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {
    private static Logger logger;

    public static Logger getInstance(Object c) {
        logger = LogManager.getLogger(c);
        Class<Main> mainClass = Main.class;
        return logger;
    }
}
