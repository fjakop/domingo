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
 * A time-only Gregorian calendar.
 * Milliseconds are always zero,
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class GregorianTime extends GregorianCalendar {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1L;

    /** Default time zone: Greenwich meantime. */
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /**
     * Default Constructor.
     */
    public GregorianTime() {
        super(GMT);
        computeTime();
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Date</code>.
     *
     * @param date the date for the new calendar
     */
    public GregorianTime(final Date date) {
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
    public GregorianTime(final Calendar calendar) {
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
     * @param hour the hour of the new calendar
     * @param minute the minute of the new calendar
     * @param second the second of the new calendar
     */
    public GregorianTime(final int hour, final int minute, final int second) {
        super(GMT);
        set(Calendar.HOUR_OF_DAY, hour);
        set(Calendar.MINUTE, minute);
        set(Calendar.SECOND, second);
        computeTime();
    }

    /**
     * Overwrite prevents setting date fields.
     *
     * @param field the given calendar field.
     * @param value the value to be set for the given calendar field.
     *
     * @see java.util.Calendar#set(int, int)
     */
    public void set(final int field, final int value) {
        if (isDateField(field)) {
            return;
        }
        super.set(field, value);
    }

    /**
     * Overwrite to prevent setting date fields.
     *
     * @param field the time field.
     * @param amount the amount of date or time to be added to the field.
     *
     * @see java.util.GregorianCalendar#add(int, int)
     */
    public void add(final int field, final int amount) {
        if (isDateField(field)) {
            return;
        }
        super.add(field, amount);
        computeTime();
    }

    /**
     * Checks if a given field number indicating a time field.
     * @param field field index
     * @return <code>true</code> if the field number indicating a time field, else <code>false</code>
     */
    private boolean isDateField(final int field) {
        if (field == Calendar.ERA || field == Calendar.YEAR) {
            return true;
        }
        if (field == Calendar.MONTH || field == Calendar.DATE) {
            return true;
        }
        if (field == Calendar.WEEK_OF_MONTH || field == Calendar.WEEK_OF_YEAR) {
            return true;
        }
        if (field == Calendar.MILLISECOND) {
            return true;
        }
        return false;
    }

    /**
     * Overwrite prevents setting a time zone to keep the time unchanged and the date unavailable.
     *
     * @param zone the new time zone (ignored)
     *
     * @see java.util.Calendar#setTimeZone(java.util.TimeZone)
     */
    public void setTimeZone(final TimeZone zone) {
    }

    /**
     * Overwrites {@link Calendar#setTimeInMillis(long)}, to disable all date fields.
     *
     * @param millis the new time in UTC milliseconds from the epoch.
     * @see java.util.Calendar#setTimeInMillis(long)
     */
    public void setTimeInMillis(final long millis) {
        super.setTimeInMillis(millis);
        computeTime();
    }

    /**
     * Overwrites GregorianCalendar.computeFields(), to disable all date fields.
     *
     * @see java.util.Calendar#computeTime()
     */
    protected void computeFields() {
        super.computeFields();
        computeTime();
    }

    /**
     * Overwrites GregorianCalendar.computeTime(), to disable all date fields.
     *
     * @see java.util.Calendar#computeTime()
     */
    protected void computeTime() {
        clearFields();
        super.computeTime();
    }

    /**
     * Clears all date fields and the millis.
     */
    private void clearFields() {
        clear(Calendar.ERA);
        clear(Calendar.YEAR);
        clear(Calendar.MONTH);
        clear(Calendar.WEEK_OF_YEAR);
        clear(Calendar.WEEK_OF_MONTH);
        clear(Calendar.DAY_OF_MONTH);
        clear(Calendar.DAY_OF_YEAR);
        clear(Calendar.DAY_OF_WEEK);
        clear(Calendar.DAY_OF_WEEK_IN_MONTH);
        clear(Calendar.MILLISECOND);
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
        return DateUtil.getTimeString(this);
    }
}
