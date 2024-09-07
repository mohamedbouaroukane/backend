package com.dac.dac.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class CustomDate {

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
    public static String dateTostring(Date date) {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    public static String getRelativeTime(Date date) {
        Instant givenInstant = date.toInstant();
        Instant now = Instant.now();

        Duration duration = Duration.between(givenInstant, now);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            if(days > 7 ){
                return dateTostring(date);
            }
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (minutes > 0) {
            return minutes + " min" + (minutes > 1 ? "s" : "") + " ago";
        } else {
            return "just now";
        }
    }

}
