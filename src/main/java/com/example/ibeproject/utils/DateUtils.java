package com.example.ibeproject.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String incrementDate(String dateTimeString, int months) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter).plusMonths(months);
        return localDateTime.format(formatter);
    }
}
