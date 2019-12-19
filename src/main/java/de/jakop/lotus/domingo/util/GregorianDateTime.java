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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A Gregorian calendar with date and time, but without milliseconds.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class GregorianDateTime extends GregorianCalendar {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public GregorianDateTime() {
        this(TimeZone.getDefault());
    }

    /**
     * Constructor.
     *
     * @param zone the given time zone.
     */
    public GregorianDateTime(final TimeZone zone) {
        super(zone);
        computeTime();
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Date</code>.
     *
     * @param date the date for the new calendar
     */
    public GregorianDateTime(final Date date) {
        this(date, TimeZone.getDefault());
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Date</code>.
     *
     * @param date the date for the new calendar
     * @param zone the given time zone.
     */
    public GregorianDateTime(final Date date, final TimeZone zone) {
        super(zone);
        if (date != null) {
            setTime(date);
        }
        computeTime();
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Calendar</code>.
     *
     * @param calendar the original calendar for the new calendar
     */
    public GregorianDateTime(final Calendar calendar) {
        if (calendar != null) {
            setTimeZone(calendar.getTimeZone());
            setTime(calendar.getTime());
            set(HOUR_OF_DAY, calendar.get(HOUR_OF_DAY));
            set(MINUTE, calendar.get(MINUTE));
        }
        computeTime();
    }

    /**
     * Creates a new Gregorian date from given year, month and date.
     *
     * <p>The first month of the year is <code>JANUARY</code> which is 0; the
     * last month is <code>DEDCEMBER</code> which is 11.</p>
     *
     * @param year the year of the new calendar
     * @param month the month of the new calendar
     * @param day the day of the new calendar
     */
    public GregorianDateTime(final int year, final int month, final int day) {
        super(year, month, day);
    }

    /**
     * Creates a new Gregorian date from given year, month and date.
     *
     * <p>The first month of the year is <code>JANUARY</code> which is 0; the
     * last month is <code>DEDCEMBER</code> which is 11.</p>
     *
     * @param year the year of the new calendar
     * @param month the month of the new calendar
     * @param day the day of the new calendar
     * @param hour the hour of the new calendar
     * @param minute the minute of the new calendar
     * @param second the second of the new calendar
     */
    public GregorianDateTime(final int year, final int month, final int day, final int hour, final int minute, final int second) {
        this(year, month, day, hour, minute, second, TimeZone.getDefault());
    }

    /**
     * Creates a new Gregorian date from given year, month and date.
     *
     * <p>The first month of the year is <code>JANUARY</code> which is 0; the
     * last month is <code>DEDCEMBER</code> which is 11.</p>
     *
     * @param year the year of the new calendar
     * @param month the month of the new calendar
     * @param day the day of the new calendar
     * @param hour the hour of the new calendar
     * @param minute the minute of the new calendar
     * @param second the second of the new calendar
     * @param zone the given time zone.
     */
    public GregorianDateTime(final int year, final int month, final int day, final int hour, final int minute, final int second, final TimeZone zone) {
        super(zone);
        set(Calendar.YEAR, year);
        set(Calendar.MONTH, month);
        set(Calendar.DATE, day);
        set(Calendar.HOUR_OF_DAY, hour);
        set(Calendar.MINUTE, minute);
        set(Calendar.SECOND, second);
        computeTime();
    }

    /**
     * Overwrite prevents setting time fields.
     *
     * @param field the given calendar field.
     * @param value the value to be set for the given calendar field.
     *
     * @see java.util.Calendar#set(int, int)
     */
    public void set(final int field, final int value) {

        if (field == Calendar.MILLISECOND) {
            return;
        }
        super.set(field, value);
        computeTime();
    }

    /**
     * Overwrite prevents setting a time zone to keep the time unchanged and unavailable.
     *
     * @param zone the new time zone (ignored)
     *
     * @see java.util.Calendar#setTimeZone(java.util.TimeZone)
     */
    public void setTimeZone(final TimeZone zone) {
        super.setTimeZone(zone);
        computeTime();
    }

    /**
     * Overwrites GregorianCalendar.computeTime(), to disable all time fields.
     *
     * @see java.util.Calendar#computeTime()
     */
    protected void computeTime() {
        clearFields();
        super.computeTime();
    }

    /**
     * Clears the millis.
     */
    private void clearFields() {
        clear(Calendar.MILLISECOND);
    }

    /**
     * Returns the month of the calendar.
     *
     * @return the month
     */
    public int getMonth() {
        return get(Calendar.MONTH);
    }

    /**
     * Returns the day of the calendar.
     *
     * @return the day
     */
    public int getDay() {
        return get(Calendar.DATE);
    }

    /**
     * Returns the year of the calendar.
     *
     * @return the year
     */
    public int getYear() {
        return get(Calendar.YEAR);
    }

    /**
     * Returns the hour of the calendar.
     *
     * @return the hour
     */
    public int getHour() {
        return get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Returns the minute of the calendar.
     *
     * @return the minute
     */
    public int getMinute() {
        return get(Calendar.MINUTE);
    }

    /**
     * Returns the second of the calendar.
     *
     * @return the second
     */
    public int getSecond() {
        return get(Calendar.SECOND);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Calendar#toString()
     */
    public String toString() {
        return DateUtil.getDateTimeString(this);
    }
}
