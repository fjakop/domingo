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
import java.util.TimeZone;

import junit.framework.TestCase;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class GregorianTest extends TestCase {

    private static final String SAMPLE_DATE_STRING = "2007-07-31 23:59:58";

    /**
     * @param name the name of the test
     */
    public GregorianTest(String name) {
        super(name);
    }

    /**
     * test Gregorian operations.
     */
    public void testGregorian() {

        // check that new calendars conform to the policies
        GregorianDate date1 = new GregorianDate(2004, Calendar.JULY, 31);
        GregorianTime time1 = new GregorianTime(17, 30, 15);
        assertDate(date1);
        assertTime(time1);

        // check that modified calendars conform to the policies
        date1.set(Calendar.YEAR, 2005);
        time1.set(Calendar.HOUR, 19);
        assertDate(date1);
        assertTime(time1);

        // check that invalid modifications don't change the calendars
        GregorianDate date2 = new GregorianDate(date1);
        GregorianTime time2 = new GregorianTime(time1);
        time2.set(Calendar.YEAR, 1999);
        time2.set(Calendar.MONTH, 0);
        time2.set(Calendar.DATE, 1);
        date2.set(Calendar.HOUR, 19);
        date2.set(Calendar.MINUTE, 19);
        date2.set(Calendar.SECOND, 19);
        assertEquals(date1, date2);
        assertEquals(time1, time2);

        // TODO check that setTimeInMillis() doesn't perform invalid modifications
    }

    private void assertTime(Calendar time) {
        // first do something with the date to enforce internal handling
        time.get(Calendar.YEAR);
        time.get(Calendar.HOUR);
        time.get(Calendar.MILLISECOND);
        // now check the fields
        assertFalse("Time has a date field set", time.isSet(Calendar.YEAR));
        assertFalse("Time has a date field set", time.isSet(Calendar.MONTH));
        assertFalse("Time has a date field set", time.isSet(Calendar.DATE));
    }

    private void assertDate(Calendar date) {
        // first do something with the date to enforce internal handling
        date.get(Calendar.YEAR);
        date.get(Calendar.HOUR);
        date.get(Calendar.MILLISECOND);
        // now check the fields
        assertFalse("Date has a time field set", date.isSet(Calendar.HOUR_OF_DAY));
        assertFalse("Date has a time field set", date.isSet(Calendar.MINUTE));
        assertFalse("Date has a time field set", date.isSet(Calendar.SECOND));
    }

    /**
     * test integrity of class GregorianTime against method setTimeInMillis().
     */
    public void testGregorianTimeSetTimeInMillis() {
        GregorianTime time1 = new GregorianTime();
        GregorianTime time2 = new GregorianTime();
        long timeInMillis = time1.getTimeInMillis();
        // adding one millisecond should not change the time
        time1.setTimeInMillis(timeInMillis + 1);
        assertEquals(time1, time2);
        assertEquals(time1.getTimeInMillis(), time2.getTimeInMillis());
    }

    /**
     * test integrity of class GregorianTime against method setTime().
     */
    public void testGregorianTimeSetTime() {
        Date date = new Date();
        GregorianTime time1 = new GregorianTime(date);
        GregorianTime time2 = new GregorianTime(date);
        // adding one millisecond should not change the time
        time1.setTime(new Date(date.getTime() + 1));
        assertEquals(time1, time2);
        assertEquals(time1.getTimeInMillis(), time2.getTimeInMillis());
    }

    /**
     * test integrity of class GregorianTime against method setTimeZone().
     */
    public void testGregorianTimeSetTimeZone() {
        Date date = new Date();
        GregorianTime time1 = new GregorianTime(date);
        GregorianTime time2 = new GregorianTime(date);
        // changing the time zone must not change the time ot the zone
        time1.setTimeZone(TimeZone.getTimeZone("GMT+5"));
        assertEquals(time1, time2);
        assertEquals(time2, time1);
        assertEquals(time1.getTimeInMillis(),  time2.getTimeInMillis());
    }

    /**
     * test integrity of class GregorianDate against method setTimeInMillis().
     */
    public void testGregorianDateSetTimeInMillis() {
        GregorianDate date1 = new GregorianDate();
        GregorianDate date2 = new GregorianDate();
        long timeInMillis = date1.getTimeInMillis();
        // adding one millisecond should not change the time
        date1.setTimeInMillis(timeInMillis + 1);
        assertEquals(date1, date2);
        assertEquals(date1.getTimeInMillis(), date2.getTimeInMillis());
    }

    /**
     * test integrity of class GregorianDate against method setTime().
     */
    public void testGregorianDateSetTime() {
        Date date = new Date();
        GregorianDate date1 = new GregorianDate(date);
        GregorianDate date2 = new GregorianDate(date);
        // adding one millisecond should not change the time
        date1.setTime(new Date(date.getTime() + 1));
        assertEquals(date1, date2);
        assertEquals(date1.getTimeInMillis(), date2.getTimeInMillis());
    }

    /**
     * test integrity of class GregorianDate against method setTimeZone().
     */
    public void testGregorianDateSetTimeZone() {
        Date date = new Date();
        GregorianDate date1 = new GregorianDate(date);
        GregorianDate date2 = new GregorianDate(date);
        // changing the time zone must not change the date
        date1.setTimeZone(TimeZone.getTimeZone("GMT+5"));
        assertEquals(date1, date2);
        assertEquals(date1.getTimeInMillis(), date2.getTimeInMillis());
    }

    /**
     * Test creating instances.
     */
    public void testCreateGregorian() {
        Calendar calendar;
        
        calendar = new GregorianDate(2005, Calendar.JANUARY, 31);

        assertEquals(2005, calendar.get(Calendar.YEAR));
        assertEquals(01, calendar.get(Calendar.MONTH) + 1);
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));

        calendar = new GregorianTime(15, 37, 48);
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(37, calendar.get(Calendar.MINUTE));
        assertEquals(48, calendar.get(Calendar.SECOND));

        calendar = new GregorianDateTime(2005, Calendar.JANUARY, 31, 15, 37, 48);
        assertEquals(2005, calendar.get(Calendar.YEAR));
        assertEquals(01, calendar.get(Calendar.MONTH) + 1);
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(37, calendar.get(Calendar.MINUTE));
        assertEquals(48, calendar.get(Calendar.SECOND));
    }

    /**
     * Tests equality of calendars after cloning.
     */
    public void testCloneGregorianDate() {
        Calendar c1 = new GregorianDate(2007, Calendar.JANUARY, 1);
        Calendar c2 = (Calendar) c1.clone();
        assertDate(c1);
        assertDate(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of calendars after copying.
     */
    public void testCopyGregorianDate() {
        Calendar c1 = new GregorianDate(2007, Calendar.JANUARY, 1);
        assertDate(c1);
        Calendar c2 = new GregorianDate(c1);
        assertDate(c1);
        assertDate(c1);
        assertDate(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of calendars creates with different constuctors.
     */
    public void testFromCalendarGregorianDate() {
        Calendar base = Calendar.getInstance();
        Calendar c1 = new GregorianDate(base.get(Calendar.YEAR), base.get(Calendar.MONTH), base.get(Calendar.DAY_OF_MONTH));
        Calendar c2 = new GregorianDate(base);
        assertDate(c1);
        assertDate(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of calendars created from a string.
     */
    public void testFromStringGregorianDate() {
        Calendar base = new GregorianDate(DateUtil.parseDate(SAMPLE_DATE_STRING, false));
        Calendar c1 = new GregorianDate(base);
        Calendar c2 = new GregorianDate(base.get(Calendar.YEAR), base.get(Calendar.MONTH), base.get(Calendar.DAY_OF_MONTH));
        assertDate(c1);
        assertDate(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of calendars before and after serialization.
     */
    public void testFromSerializedGregorianDate() {
        Calendar base = new GregorianDate(DateUtil.parseDate(SAMPLE_DATE_STRING, false));
        // TODO testFromSerialized
        Calendar c1 = new GregorianDate(base);
        Calendar c2 = (Calendar) DeepCopy.copy(c1);
        assertDate(c1);
        assertDate(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of calendars after cloning.
     */
    public void testCloneGregorianTime() {
        Calendar c1 = new GregorianTime(11, 12, 13);
        Calendar c2 = (Calendar) c1.clone();
        assertTime(c1);
        assertTime(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of calendars after copying.
     */
    public void testCopyGregorianTime() {
        Calendar c1 = new GregorianTime(11, 12, 13);
        Calendar c2 = new GregorianTime(c1);
        assertTime(c1);
        assertTime(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of calendars creates with different constuctors.
     */
    public void testFromCalendarGregorianTime() {
        Calendar base = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Calendar c1 = new GregorianTime(base.get(Calendar.HOUR_OF_DAY), base.get(Calendar.MINUTE), base.get(Calendar.SECOND));
        Calendar c2 = new GregorianTime(base);
        assertTime(c1);
        assertTime(c2);
        assertEquals(c1.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.HOUR_OF_DAY));
        assertEquals(c1.get(Calendar.MINUTE), c2.get(Calendar.MINUTE));
        assertEquals(c1.get(Calendar.SECOND), c2.get(Calendar.SECOND));
    }

    /**
     * Tests equality of calendars created from a string.
     */
    public void testFromStringGregorianTime() {
        Calendar base = new GregorianTime(DateUtil.parseDate(SAMPLE_DATE_STRING, false));
        Calendar c1 = new GregorianTime(base);
        Calendar c2 = new GregorianTime(base.get(Calendar.HOUR_OF_DAY), base.get(Calendar.MINUTE), base.get(Calendar.SECOND));
        assertTime(c1);
        assertTime(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of calendars before and after serialization.
     */
    public void testFromSerializedGregorianTime() {
        Calendar base = new GregorianTime(DateUtil.parseDate(SAMPLE_DATE_STRING, false));
        // TODO testFromSerialized
        Calendar c1 = new GregorianTime(base);
        Calendar c2 = (Calendar) DeepCopy.copy(c1);
        assertTime(c1);
        assertTime(c2);
        assertEquals(c1, c2);
    }

    /**
     * Tests equality of grgorian timezones.
     */
    public void testEqualsGregorianTimezone() {
        GregorianTimeZone tz1 = new GregorianTimeZone();
        GregorianTimeZone tz2 = new GregorianTimeZone();
        assertEquals(tz1, tz2);
        assertFalse(tz1 == tz2);
        
        tz1 = new GregorianTimeZone(TimeZone.getTimeZone("GMT"));
        tz2 = new GregorianTimeZone(TimeZone.getTimeZone("GMT"));
        assertEquals(tz1, tz2);
        assertFalse(tz1 == tz2);

        tz1.setTimeZone(TimeZone.getTimeZone("GMT"));
        tz2.setTimeZone(TimeZone.getTimeZone("PST"));
        assertFalse(tz1.equals(tz2));

        
    }
}