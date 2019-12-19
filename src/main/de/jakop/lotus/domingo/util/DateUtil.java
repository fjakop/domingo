/*
 * This file is part of Domingo
 * an Open Source Java-API to Lotus Notes/Domino
 * hosted at http://domingo.sourceforge.net
 *
 * Copyright (c) 2003-2007 Beck et al. projects GmbH Munich, Germany (http://www.bea.de)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.jakop.lotus.domingo.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Static utility methods for date operations and conversions.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede </a>
 */
public final class DateUtil {

    /**
     * Date format used for parsing.
     * The format it compatible with the format of class <tt>java.text.SimpleDateFormat</tt>.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Time format used for parsing.
     * The format it compatible with the format of class <tt>java.text.SimpleDateFormat</tt>.
     */
    public static final String TIME_FORMAT = "hh:mm:ss";

    /**
     * Date/time format used for parsing.
     * The format it compatible with the format of class <tt>java.text.SimpleDateFormat</tt>.
     */
    public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    /** Start index of year component within date format. */
    private static final int YEAR_START = DATE_TIME_FORMAT.indexOf('y');

    /** End index of year component within date format. */
    private static final int YEAR_END = DATE_TIME_FORMAT.lastIndexOf('y');

    /** Start index of year component within date format. */
    private static final int MONTH_START = DATE_TIME_FORMAT.indexOf('M');

    /** End index of year component within date format. */
    private static final int MONTH_END = DATE_TIME_FORMAT.lastIndexOf('M');

    /** Start index of year component within date format. */
    private static final int DAY_START = DATE_TIME_FORMAT.indexOf('d');

    /** End index of year component within date format. */
    private static final int DAY_END = DATE_TIME_FORMAT.lastIndexOf('d');

    /** Start index of year component within date format. */
    private static final int HOUR_START = DATE_TIME_FORMAT.indexOf('h');

    /** End index of year component within date format. */
    private static final int HOUR_END = DATE_TIME_FORMAT.lastIndexOf('h');

    /** Start index of year component within date format. */
    private static final int MINUTE_START = DATE_TIME_FORMAT.indexOf('m');

    /** End index of year component within date format. */
    private static final int MINUTE_END = DATE_TIME_FORMAT.lastIndexOf('m');

    /** Start index of year component within date format. */
    private static final int SECOND_START = DATE_TIME_FORMAT.indexOf('s');

    /** End index of year component within date format. */
    private static final int SECOND_END = DATE_TIME_FORMAT.lastIndexOf('s');

    /**
     * private constructor to prevent instantiation.
     */
    private DateUtil() {
    }

    /**
     * Parses a date given as a string.
     * The format of the resulting string is {@link DateUtil#DATE_TIME_FORMAT}.
     *
     * @param date a date formatted as a string
     * @param adjustToLocalTimeZone if result should be adjusted to local time zone or not
     * @return the date as a <tt>java.util.Calendar</tt> or <tt>null</tt> if argument string is null or empty
     * @throws IndexOutOfBoundsException if the String is too short
     * @throws NumberFormatException if a date/time component is out of range or cannot be parsed
     */
    public static Calendar parseDate(final String date, final boolean adjustToLocalTimeZone)
    throws IndexOutOfBoundsException, NumberFormatException {
        if (date == null || "".equals(date)) {
            return null;
        }
        final int year = Integer.parseInt(date.substring(YEAR_START, YEAR_END + 1));
        final int month = Integer.parseInt(date.substring(MONTH_START, MONTH_END + 1));
        final int day = Integer.parseInt(date.substring(DAY_START, DAY_END + 1));
        final int hour;
        final int minute;
        final int second;
        if (date.length() > HOUR_START) {
            hour = Integer.parseInt(date.substring(HOUR_START, HOUR_END + 1));
            minute = Integer.parseInt(date.substring(MINUTE_START, MINUTE_END + 1));
            second = Integer.parseInt(date.substring(SECOND_START, SECOND_END + 1));
        } else {
            hour = 0;
            minute = 0;
            second = 0;
        }
        final Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        if (adjustToLocalTimeZone) {
            calendar.add(Calendar.MILLISECOND, Calendar.getInstance().getTimeZone().getRawOffset());
        }
        return calendar;
    }

    /**
     * Formats the date and time time of a Calendar instance to a String.
     * The format of the resulting string is the short date/time format of the default locale.
     *
     * @param date the date as a <tt>java.util.Calendar</tt>
     * @param time the time as a <tt>java.util.Calendar</tt>
     * @return date as a formatted string
     */
    public static String getDateTimeString(final Calendar date, final Calendar time) {
        return getDateTimeString(date, time, Locale.getDefault());
    }

    /**
     * Formats the date and time time of a Calendar instance to a String.
     * The format of the resulting string is the short date/time format of the given locale.
     *
     * @param date the date as a <tt>java.util.Calendar</tt>
     * @param time the time as a <tt>java.util.Calendar</tt>
     * @param locale locale
     * @return date as a formatted string
     */
    public static String getDateTimeString(final Calendar date, final Calendar time, final Locale locale) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.LONG, locale);
        return dateFormat.format(date.getTime()) + " " + timeFormat.format(time.getTime());
    }

    /**
     * Formats the date and time time of a Calendar instance to a String.
     * The format of the resulting string is the short date/time format of the default locale.
     *
     * @param calendar the date as a <tt>java.util.Calendar</tt>
     * @return date as a formatted string
     */
    public static String getDateTimeString(final Calendar calendar) {
        return getDateTimeString(calendar, Locale.getDefault());
    }

    /**
     * Formats the date and time time of a Calendar instance to a String.
     * The format of the resulting string is the short date/time format of the given locale.
     *
     * @param calendar the date as a <tt>java.util.Calendar</tt>
     * @param locale locale
     * @return date as a formatted string
     */
    public static String getDateTimeString(final Calendar calendar, final Locale locale) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale);
        return format.format(calendar.getTime());
    }

    /**
     * Formats the date of a Calendar instance to a String.
     * The format of the resulting string is the short date/time format of the defaultlocale.
     *
     * @param calendar the date as a <tt>java.util.Calendar</tt>
     * @return date as a formatted string
     */
    public static String getDateString(final Calendar calendar) {
        return getDateString(calendar, Locale.getDefault());
    }

    /**
     * Formats the date of a Calendar instance to a String.
     * The format of the resulting string is the short date/time format of the given locale.
     *
     * @param calendar the date as a <tt>java.util.Calendar</tt>
     * @param locale locale
     * @return date as a formatted string
     */
    public static String getDateString(final Calendar calendar, final Locale locale) {
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        return format.format(calendar.getTime());
    }

    /**
     * Formats the time of a Calendar instance to a String.
     * The format of the resulting string is the short time format of the default locale.
     *
     * @param calendar the date as a <tt>java.util.Calendar</tt>
     * @return date as a formatted string
     */
    public static String getTimeString(final Calendar calendar) {
        return getTimeString(calendar, Locale.getDefault());
    }

    /**
     * Formats the time of a Calendar instance to a String.
     * The format of the resulting string is the short time format of the given locale.
     *
     * @param calendar the date as a <tt>java.util.Calendar</tt>
     * @param locale locale
     * @return date as a formatted string
     */
    public static String getTimeString(final Calendar calendar, final Locale locale) {
        DateFormat format = DateFormat.getTimeInstance(DateFormat.LONG, locale);
        return format.format(calendar.getTime());
    }
}
