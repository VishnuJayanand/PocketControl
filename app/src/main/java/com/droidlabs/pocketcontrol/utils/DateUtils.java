package com.droidlabs.pocketcontrol.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Util method to format dates.
 */
public final class DateUtils {

    private static final String DEFAULT_FORMAT = "dd-MM-yyyy";

    /**
     * Private constructor to prevent extending utility classes.
     */
    private DateUtils() { }

    /**
     * Returns instance of SimpleDateFormat with default params.
     * @return SimpleDateFormat with default params.
     */
    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat(DEFAULT_FORMAT, Locale.US);
    }

    /**
     * Returns instance of SimpleDateFormat with custom format.
     * @param format string format for date.
     * @return SimpleDateFormat with custom format.
     */
    public static SimpleDateFormat getDateFormatter(final String format) {
        return new SimpleDateFormat(format, Locale.US);
    }

    /**
     * Formats date in milliseconds using default dateFormatter.
     * @param milliseconds date in milliseconds.
     * @return formatted date.
     */
    public static String formatDate(final long milliseconds) {
        SimpleDateFormat simpleDateFormat = getDateFormatter();
        return simpleDateFormat.format(milliseconds);
    }

    /**
     * Formats date in milliseconds using the dateFormatter with custom format.
     * @param milliseconds date in milliseconds.
     * @param format string format for date.
     * @return formatted date.
     */
    public static String formatDate(final long milliseconds, final String format) {
        SimpleDateFormat simpleDateFormat = getDateFormatter(format);
        return simpleDateFormat.format(milliseconds);
    }

    /**
     * Returns midnight of the current day.
     * @return Calendar instance set to midnight of the current day.
     */
    public static Calendar getStartOfCurrentDay() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date;
    }

    /**
     * Returns 23:59:59:999 of the current day.
     * @return Calendar instance set to 23:59:59:999 of the current day.
     */
    public static Calendar getEndOfCurrentDay() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 999);

        return date;
    }

    /**
     * Returns midnight of a date.
     * @param milliseconds date in milliseconds.
     * @return Calendar instance set to midnight of a certain day.
     */
    public static Calendar getStartOfDay(final long milliseconds) {
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(milliseconds);

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date;
    }

    /**
     * Returns midnight of a date.
     * @param milliseconds date in milliseconds.
     * @return Calendar instance set to midnight of a certain day.
     */
    public static long getStartOfDayInMS(final long milliseconds) {
        Calendar date = getStartOfDay(milliseconds);
        return date.getTimeInMillis();
    }

    /**
     * Returns 23:59:59:999 of a date.
     * @param milliseconds date in milliseconds.
     * @return Calendar instance set to 23:59:59:999 of a certain day.
     */
    public static Calendar getEndOfDay(final long milliseconds) {
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(milliseconds);

        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 999);

        return date;
    }

    /**
     * Returns 23:59:59:999 of a date.
     * @param milliseconds date in milliseconds.
     * @return Calendar instance set to 23:59:59:999 of a certain day.
     */
    public static long getEndOfDayInMS(final long milliseconds) {
        Calendar date = getEndOfDay(milliseconds);
        return date.getTimeInMillis();
    }

}
