package com.util;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ErrorLogger {
    private static final Logger logger = Logger.getLogger(String.valueOf(ErrorLogger.class));

    public static void log(LogRecord record) {
        logger.log(record);
    }
}
