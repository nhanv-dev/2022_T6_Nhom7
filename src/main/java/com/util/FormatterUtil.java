package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FormatterUtil {
    public static Date createdFormatDate(String path) throws ParseException {
        String time = path.split("-")[1].replace(".csv", "");
        return new SimpleDateFormat("yyyyMMddhhmmss").parse(time);
    }
    public static String getDateFromFormatName(String name) {
        String path = name.split("-")[1].replace(".csv", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        LocalDate date = LocalDate.parse(path, formatter);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter1.format(date);
    }

    public static String formatDirectoryPathFTP(String fileName) {
        String path = fileName.split("-")[1].replace(".csv", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        LocalDate date = LocalDate.parse(path, formatter);
        return "/" + date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
    }
}
