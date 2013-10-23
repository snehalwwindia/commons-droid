
package com.commonsdroid.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

/**
 * @author Sandeep S. Saini
 */
public class DateTimeUtil {

    private static DateTime dateTime;

    /**
     * used to get the system's current date and time
     * 
     * @return returns string containing date and time in "dd MMM yyyy HH:MM:SS"
     *         format
     */
    public static String getCurrentDateTime() {
        dateTime = new DateTime();
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        return dateFormat.format(dateTime.toDate());
    }

    /**
     * used to get the system's current date
     * 
     * @return returns string containing date in "dd MMM yyyy" format
     */
    public static String getCurrentDate() {
        dateTime = new DateTime();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        return dateFormat.format(dateTime.toDate());
    }

    /**
     * used to get the system's current time
     * 
     * @return returns string containing time in "HH:MM:SS" format
     */
    public static String getCurrentTime() {
        dateTime = new DateTime();
        DateFormat dateFormat = SimpleDateFormat.getTimeInstance();
        return dateFormat.format(dateTime.toDate());
    }

    /**
     * used to get the system's current date
     * 
     * @param format string specifying the required format of date
     * @return returns string containing date in the specified format
     */
    public String getCurrentDate(String format) {
        dateTime = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(dateTime.toDate());
    }

    /**
     * used to convert the specified date to the specified format
     * 
     * @param date date to be formatted
     * @param format format string specifying the required format of date
     * @return returns string containing date in the specified format
     */
    public static String getFormatedDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * used to convert the specified date string to Date instance
     * 
     * @param dateString date string
     * @param currentFormat format string specifying the current format of date
     * @return returns Date instance in "day MMM dd HH:MM:SS Timzone yyyy"
     *         format
     */
    public static Date convertStringToDate(String dateString, String currentFormat)
            throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(currentFormat, Locale.getDefault());
        return dateFormat.parse(dateString);
    }

    /**
     * used to get the current time-stamp
     * 
     * @return returns time-stamp in milliseconds
     */
    public static long getCurrentTimeStamp() {
        dateTime = new DateTime();
        return dateTime.getMillis();
    }

    public static long nsToUnixTime(double nsTime) {
        return 0;
    }

    public static long unixToNs(long timeInMilliSeconds) {
        return 0;
    }
}
