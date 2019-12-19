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

import java.io.ObjectStreamException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A time-only Gregorian calendar.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class GregorianTimeZone extends GregorianCalendar {

    private static final long serialVersionUID = 1L;

    private static final int MILLIS_PER_HOUR = 3600000;

    /**
     * Default Constructor.
     */
    public GregorianTimeZone() {
        super();
        computeFields();
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Date</code>.
     *
     * @param date the date for the new calendar
     */
    public GregorianTimeZone(final Date date) {
        super();
        if (date != null) {
            setTime(date);
        }
        computeFields();
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Calendar</code>.
     *
     * @param calendar the original calendar for the new calendar
     */
    public GregorianTimeZone(final Calendar calendar) {
        super();
        if (calendar != null) {
            set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
            set(Calendar.SECOND, calendar.get(Calendar.SECOND));
            set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));
            computeFields();
        }
    }

    /**
     * Creates a new Gregorian time zone from a given time zone.
     *
     * @param timezone the time zone
     */
    public GregorianTimeZone(final TimeZone timezone) {
        super();
        setTimeZone(timezone);
        computeFields();
    }

    /**
     * Overwrite prevents setting date and time fields.
     *
     * @param field the given calendar field.
     * @param value the value to be set for the given calendar field.
     *
     * @see java.util.Calendar#set(int, int)
     */
    public void set(final int field, final int value) {
    }

    /**
     * Formats a date as a string in a locale-independent format.
     *
     * @return string representation of date in reverse format with leading
     *         zeros: <code>YYYY-MM-DD</code>.
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        // FIXME getDSTSavings() should be compatible with JDK 1.3
        return getTimeZone().getID() + "/" + (getTimeZone().getDSTSavings() / MILLIS_PER_HOUR) + "h";
    }

    /**
     * Overwrites GregorianCalendar.computeFields(), to disable all date and
     * time fields.
     *
     * @see java.util.Calendar#computeFields()
     */
    protected void computeFields() {
        super.computeFields();
        clear(Calendar.ERA);
        clear(Calendar.YEAR);
        clear(Calendar.MONTH);
        clear(Calendar.WEEK_OF_YEAR);
        clear(Calendar.WEEK_OF_MONTH);
        clear(Calendar.DAY_OF_MONTH);
        clear(Calendar.DAY_OF_YEAR);
        clear(Calendar.DAY_OF_WEEK);
        clear(Calendar.DAY_OF_WEEK_IN_MONTH);
        clear(Calendar.HOUR_OF_DAY);
        clear(Calendar.HOUR);
        clear(Calendar.MINUTE);
        clear(Calendar.SECOND);
        clear(Calendar.AM_PM);
        clear(Calendar.MILLISECOND);
    }

    /**
     * Serialization helper. Usually used to resolve singletons or other static
     * instances. Here used to compute all fields, especially the array
     * <code>stamp</code> which is not serialized in <code>JDK 1.3.1</code>.
     */
    private Object readResolve() throws ObjectStreamException {
        computeFields();
        return this;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param object the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the object
     *         argument; <code>false</code> otherwise.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof java.util.Calendar)) {
            return false;
        } else {
            TimeZone tz1 = ((java.util.Calendar) object).getTimeZone();
            TimeZone tz2 = getTimeZone();
            // todo write unit tests for the equals method
            return tz1.getID().equals(tz2.getID());
        }
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return get(Calendar.HOUR) << (2 * 2 * 3) | get(Calendar.MINUTE) << (2 * 3) | get(Calendar.SECOND);
    }
}
