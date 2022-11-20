package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateFormatter {
    public static Date formatCreatedDate(String path) throws ParseException {
        String time = path.split("-")[1].replace(".csv", "");
        return new SimpleDateFormat("yyyyMMdd").parse(time);
    }

    public static String generateDateFromFormatName(String name, String delimiter) {
        String path = name.split("-")[1].replace(".csv", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(path, formatter);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy" + delimiter + "MM" + delimiter + "dd");
        return formatter1.format(date);
    }

    public static String generateRemoteFilePath(String name) {
        String directory = generateDateFromFormatName(name, "-");
        return "/" + directory +"/"+ name;
    }
}
