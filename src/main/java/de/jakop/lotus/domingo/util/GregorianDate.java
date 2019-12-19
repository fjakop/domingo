/*
 * This file is part of Domingo
 * an Open Source Java-API to Lotus Notes/Domino
 * originally hosted at http://domingo.sourceforge.net, now available
 * at https://github.com/fjakop/domingo
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
 * A date-only Gregorian calendar.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class GregorianDate extends GregorianCalendar {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1L;

    /** Default time zone: Greenwich meantime. */
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /**
     * Default Constructor.
     */
    public GregorianDate() {
        super(GMT);
        computeTime();
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Date</code>.
     *
     * @param date the date for the new calendar
     */
    public GregorianDate(final Date date) {
        super(GMT);
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
    public GregorianDate(final Calendar calendar) {
        super(GMT);
        if (calendar != null) {
            setTime(calendar.getTime());
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
    public GregorianDate(final int year, final int month, final int day) {
        super(GMT);
        set(Calendar.YEAR, year);
        set(Calendar.MONTH, month);
        set(Calendar.DATE, day);
        computeTime();
        computeFields();
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
        if (isTimeField(field)) {
            return;
        }
        super.set(field, value);
    }

    /**
     * Overwrite to prevent setting time fields.
     *
     * @param field the time field.
     * @param amount the amount of date or time to be added to the field.
     *
     * @see java.util.GregorianCalendar#add(int, int)
     */
    public void add(final int field, final int amount) {
        if (isTimeField(field)) {
            return;
        }
        super.add(field, amount);
    }

    /**
     * Checks if a given field number indicating a time field.
     * @param field field index
     * @return <code>true</code> if the field number indicating a time field, else <code>false</code>
     */
    private boolean isTimeField(final int field) {
        if (field == Calendar.HOUR || field == Calendar.HOUR_OF_DAY || field == Calendar.AM_PM) {
            return true;
        }
        if (field == Calendar.MINUTE || field == Calendar.SECOND) {
            return true;
        }
        if (field == Calendar.MILLISECOND) {
            return true;
        }
        return false;
    }

    /**
     * Overwrite prevents setting a time zone to keep the time unchanged and unavailable.
     *
     * @param zone the new time zone (ignored)
     *
     * @see java.util.Calendar#setTimeZone(java.util.TimeZone)
     */
    public void setTimeZone(final TimeZone zone) {
    }

    /**
     * Overwrites {@link Calendar#setTimeInMillis(long)}, to disable all time fields.
     *
     * @param millis the new time in UTC milliseconds from the epoch.
     * @see java.util.Calendar#setTimeInMillis(long)
     */
    public void setTimeInMillis(final long millis) {
        super.setTimeInMillis(millis);
        computeTime();
    }

    /**
     * Overwrites GregorianCalendar.computeFields(), to disable all time fields.
     *
     * @see java.util.Calendar#computeTime()
     */
    protected void computeFields() {
        super.computeFields();
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
     * Clears all time fields.
     */
    private void clearFields() {
        clear(Calendar.HOUR_OF_DAY);
        clear(Calendar.HOUR);
        clear(Calendar.MINUTE);
        clear(Calendar.SECOND);
        clear(Calendar.AM_PM);
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
     * {@inheritDoc}
     *
     * @see java.util.Calendar#toString()
     */
    public String toString() {
        return DateUtil.getDateString(this);
    }
}
