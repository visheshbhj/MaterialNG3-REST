package com.rest.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

    public static String getRandomString(int length) {
        StringBuilder buildString = new StringBuilder();
        String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characterSet.length());
            buildString.append(characterSet.charAt(index));
        }
        return buildString.toString();
    }

    public static String getCurrentStandardFormattedDateTime(){
        LocalDateTime now =  LocalDateTime.now();
        return standardFormattedDateTime(now);
    }

    public static String standardFormattedDateTime(LocalDateTime datetime){
        return formattedDateTime(datetime,"dd-MM-yyyy HH:mm:ss");
    }

    public static String formattedDateTime(LocalDateTime datetime, String dateformat){
        DateTimeFormatter format = DateTimeFormatter.ofPattern(dateformat);
        return datetime.format(format);
    }

    public static String standardFormattedDateTime(String datetime){
        return formattedDateTime(datetime,"dd-MM-yyyy HH:mm:ss");
    }

    public static String formattedDateTime(String datetime, String dateformat){
        DateTimeFormatter format = DateTimeFormatter.ofPattern(dateformat);
        LocalDateTime converter = LocalDateTime.parse(datetime);
        return converter.format(format);
    }

    public static void main(String[] args) {
        System.out.println(CommonUtils.getCurrentStandardFormattedDateTime());
        System.out.println(CommonUtils.standardFormattedDateTime(LocalDateTime.now().toString()));
    }
}
