package com.dac.dac.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDate {

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
    public static String dateTostring(Date date) {

        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
    }
}
