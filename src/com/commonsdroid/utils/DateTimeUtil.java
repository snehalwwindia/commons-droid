
package com.commonsdroid.utils;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sandeep S. Saini
 */
public class DateTimeUtil {

    private static DateTime dateTime;

    public static String getCurrentDateTime() {
        dateTime = new DateTime();
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        return dateFormat.format(dateTime.toDate());
    }

    public static String getCurrentDate() {
        dateTime = new DateTime();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        return dateFormat.format(dateTime.toDate());
    }

    public static String getCurrentTime() {
        dateTime = new DateTime();
        DateFormat dateFormat = SimpleDateFormat.getTimeInstance();
        return dateFormat.format(dateTime.toDate());
    }

    public String getCurrentDate(String format) {
        return new String();
    }

    public static String getFormatedDate(String date, String format) {
        return new String();
    }

    public static long nsToUnixTime(double nsTime) {
        return 0;
    }

    public static long unixToNs(long timeInMilliSeconds) {
        return 0;
    }
}
