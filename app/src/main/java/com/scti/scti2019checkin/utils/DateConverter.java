package com.scti.scti2019checkin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static String getShortFormatedDate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        String resultDate = "";

        try {
            Date date = simpleDateFormat.parse(dateString);
            Calendar calendar = simpleDateFormat.getCalendar();
            calendar.setTime(date);
            resultDate = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "/"
                    + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + " "
                    + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                    + String.format("%02d", calendar.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultDate;
    }

    public static String getShortFormatedTime(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        String resultDate = "";

        try {
            Date date = simpleDateFormat.parse(dateString);
            Calendar calendar = simpleDateFormat.getCalendar();
            calendar.setTime(date);
            resultDate = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                    + String.format("%02d", calendar.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultDate;
    }

    public static String getFormatedNow() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.YEAR) + "-"
                + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-"
                + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + " "
                + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":"
                + String.format("%02d", calendar.get(Calendar.SECOND));
    }

}
