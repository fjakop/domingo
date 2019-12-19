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

package de.jakop.lotus.domingo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import de.jakop.lotus.domingo.util.GregorianDate;
import de.jakop.lotus.domingo.util.GregorianDateRange;
import de.jakop.lotus.domingo.util.GregorianDateTime;
import de.jakop.lotus.domingo.util.GregorianDateTimeRange;
import de.jakop.lotus.domingo.util.GregorianTime;
import de.jakop.lotus.domingo.util.GregorianTimeRange;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DateTimeTest extends BaseProxyTest {

    private static final String ITEM_NAME = "test";

    /**
     * @param name the name of the test
     */
    public DateTimeTest(String name) {
        super(name);
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeWinter() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JANUARY, TimeZone.getDefault());
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeSummer() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JULY, TimeZone.getDefault());
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeBerlinWinter() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JANUARY, TimeZone.getTimeZone("Europe/Berlin"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeBerlinSummer() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JULY, TimeZone.getTimeZone("Europe/Berlin"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeLosAngelesWinter() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JANUARY, TimeZone.getTimeZone("America/Los_Angeles"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeLosAngelesSummer() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JULY, TimeZone.getTimeZone("America/Los_Angeles"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeMoscowWinter() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JANUARY, TimeZone.getTimeZone("Europe/Moscow"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeMoscowSummer() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JULY, TimeZone.getTimeZone("Europe/Moscow"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeKatmanduWinter() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JANUARY, TimeZone.getTimeZone("Asia/Katmandu"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeKatmanduSummer() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JULY, TimeZone.getTimeZone("Asia/Katmandu"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeFijiWinter() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JANUARY, TimeZone.getTimeZone("Pacific/Fiji"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeFijiSummer() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JULY, TimeZone.getTimeZone("Pacific/Fiji"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeKuwaitWinter() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JANUARY, TimeZone.getTimeZone("Asia/Kuwait"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateTimeKuwaitSummer() {
        testReplaceItemValueDateGregorianDateTime(Calendar.JULY, TimeZone.getTimeZone("Asia/Kuwait"));
    }

    /**
     * Tests replacing and reading date/time values.
     * 
     * @param month month
     * @param zone time zone
     */
    public void testReplaceItemValueDateGregorianDateTime(final int month, final TimeZone zone) {
        System.out.println("-> testReplaceItemValueDateGregorianDateTime: Month: " + month + ", Zone: " + zone.getID());
        DBaseDocument doc = createDBaseDocument();
        doc.replaceItemValue("Form", "testReplaceItemValueDateGregorianDateTime");
        Calendar value = new GregorianDateTime(2004, month, 17, 15, 59, 58, zone);
        doc.replaceItemValue(ITEM_NAME, value);
        doc.save();
        Calendar result = doc.getItemValueDate(ITEM_NAME);
        result.setTimeZone(zone);
        assertValidCalendar(value, result);
        result = ((DItem) doc.getFirstItem(ITEM_NAME)).getValueDateTime();
        result.setTimeZone(zone);
        assertValidCalendar(value, result);
    }

    // #################################################

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateWinter() {
        testReplaceItemValueDateGregorianDate(Calendar.JANUARY);
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianDateSummer() {
        testReplaceItemValueDateGregorianDate(Calendar.JULY);
    }

    /**
     * Tests replacing and reading date/time values.
     * 
     * @param month month
     */
    public void testReplaceItemValueDateGregorianDate(final int month) {
        System.out.println("-> testReplaceItemValueDateGregorianDate: Month: " + month);
        DBaseDocument doc = createDBaseDocument();
        doc.replaceItemValue("Form", "testReplaceItemValueDateGregorianDate");
        Calendar value = new GregorianDate(2005, month, 17);
        doc.replaceItemValue(ITEM_NAME, value);
        doc.save();
        Calendar result = doc.getItemValueDate(ITEM_NAME);
        System.out.println(value.getTime() + " zone: " + value.getTimeZone().getID());
        System.out.println(result.getTime() + " zone: " + result.getTimeZone().getID());
        int valueZoneOffset = value.getTimeZone().getOffset(value.getTimeInMillis()) / 60000;
        int resultZoneOffset = result.getTimeZone().getOffset(value.getTimeInMillis()) / 60000;
        assertTrue("wrong type", result instanceof GregorianDate);
        assertEquals("wrong zone", valueZoneOffset, resultZoneOffset);
        assertEquals("wrong time", value.getTime(), result.getTime());
        result = ((DItem) doc.getFirstItem(ITEM_NAME)).getValueDateTime();
        System.out.println(result.getTime() + " zone: " + result.getTimeZone().getID());
        resultZoneOffset = result.getTimeZone().getOffset(value.getTimeInMillis()) / 60000;
        assertTrue("wrong type", result instanceof GregorianDate);
        assertEquals("wrong zone", valueZoneOffset, resultZoneOffset);
        assertEquals("wrong time", value.getTime(), result.getTime());
    }

    // ##################################################

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianTimeWinter() {
        testReplaceItemValueDateGregorianTime(Calendar.JANUARY);
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateGregorianTimeSummer() {
        testReplaceItemValueDateGregorianTime(Calendar.JULY);
    }

    /**
     * Tests replacing and reading date/time values.
     * 
     * @param month month
     */
    public void testReplaceItemValueDateGregorianTime(final int month) {
        System.out.println("-> testReplaceItemValueDateGregorianTimeWinter: Month: " + month);
        DBaseDocument doc = createDBaseDocument();
        doc.replaceItemValue("Form", "testReplaceItemValueDateGregorianTime");
        Calendar value = new GregorianTime(15, 59, 58);
        doc.replaceItemValue(ITEM_NAME, value);
        doc.save();
        Calendar result = doc.getItemValueDate(ITEM_NAME);
        System.out.println(value.getTime() + " zone: " + value.getTimeZone().getID());
        System.out.println(result.getTime() + " zone: " + result.getTimeZone().getID());
        assertTrue("wrong type", result instanceof GregorianTime);
        assertEquals("wrong time", value.getTime(), result.getTime());
        result = ((DItem) doc.getFirstItem(ITEM_NAME)).getValueDateTime();
        System.out.println(result.getTime() + " zone: " + result.getTimeZone().getID());
        assertTrue("wrong type", result instanceof GregorianTime);
        assertEquals("wrong time", value.getTime(), result.getTime());
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeWinter() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getDefault());
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeSummer() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getDefault());
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeBerlinWinter() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Europe/Berlin"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeBerlinSummer() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Europe/Berlin"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeLosAngelesWinter() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("America/Los_Angeles"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeLosAngelesSummer() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("America/Los_Angeles"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeMoscowWinter() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Europe/Moscow"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeMoscowSummer() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Europe/Moscow"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeKatmanduWinter() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Asia/Katmandu"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeKatmanduSummer() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Asia/Katmandu"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeFijiWinter() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Pacific/Fiji"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeFijiSummer() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Pacific/Fiji"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeKuwaitWinter() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Asia/Kuwait"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueListGregorianDateTimeKuwaitSummer() {
        testReplaceItemValueListGregorianDateTime(TimeZone.getTimeZone("Asia/Kuwait"));
    }

    /**
     * Tests replacing and reading date/time values.
     * 
     * @param zone time zone
     */
    public void testReplaceItemValueListGregorianDateTime(final TimeZone zone) {
        System.out.println("-> testReplaceItemValueListGregorianDateTime");
        DBaseDocument doc = createDBaseDocument();
        doc.replaceItemValue("Form", "testReplaceItemValueListGregorianDateTime");
        List list = new ArrayList();
        Calendar value1 = new GregorianDateTime(2004, Calendar.JANUARY, 17, 15, 59, 58, zone);
        Calendar value2 = new GregorianDateTime(2004, Calendar.JULY, 17, 15, 59, 58, zone);
        list.add(value1);
        list.add(value2);
        doc.replaceItemValue(ITEM_NAME, list);
        doc.save();
        List result = doc.getItemValue(ITEM_NAME);
        assertEqualsDateTimeList(list, result, zone);
        result = ((DItem) doc.getFirstItem(ITEM_NAME)).getValues();
        assertEqualsDateTimeList(list, result, zone);
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeWinter() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getDefault());
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeSummer() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getDefault());
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeBerlinWinter() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Europe/Berlin"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeBerlinSummer() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Europe/Berlin"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeLosAngelesWinter() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("America/Los_Angeles"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeLosAngelesSummer() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("America/Los_Angeles"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeMoscowWinter() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Europe/Moscow"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeMoscowSummer() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Europe/Moscow"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeKatmanduWinter() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Asia/Katmandu"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeKatmanduSummer() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Asia/Katmandu"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeFijiWinter() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Pacific/Fiji"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeFijiSummer() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Pacific/Fiji"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeKuwaitWinter() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Asia/Kuwait"));
    }

    /** Tests replacing and reading date/time values. */
    public void testReplaceItemValueDateRangeGregorianDateTimeKuwaitSummer() {
        testReplaceItemValueDateRangeGregorianDateTime(TimeZone.getTimeZone("Asia/Kuwait"));
    }

    /**
     * Tests getValueDateRange.
     * 
     * @param zone time zone
     */
    public void testReplaceItemValueDateRangeGregorianDateTime(final TimeZone zone) {
        final Calendar value1 = new GregorianDateTime(2004, Calendar.JANUARY, 17, 15, 59, 58, zone);
        final Calendar value2 = new GregorianDateTime(2004, Calendar.JULY, 17, 15, 59, 58, zone);
        GregorianDateTimeRange range = new GregorianDateTimeRange(value1, value2);
        testReplaceItemValueDateRange(range, zone);
    }

    /**
     * Tests getValueDateTime.
     */
    public void testReplaceItemValueDateRangeGregorianDate() {
        final Calendar value1 = new GregorianDate(2004, Calendar.JANUARY, 17);
        final Calendar value2 = new GregorianDate(2004, Calendar.JULY, 17);
        GregorianDateRange range = new GregorianDateRange(value1, value2);
        testReplaceItemValueDateRange(range, value1.getTimeZone());
    }

    /**
     * Tests getValueDateTime.
     */
    public void testReplaceItemValueDateRangeGregorianTime() {
        final Calendar value1 = new GregorianTime(15, 59, 58);
        final Calendar value2 = new GregorianTime(16, 59, 58);
        GregorianTimeRange range = new GregorianTimeRange(value1, value2);
        testReplaceItemValueDateRange(range, value1.getTimeZone());
    }

    /**
     * Tests getValueDateTime.
     *
     * @param value1 start date/time
     * @param value2 end date/time
     * @param zone time zone of test case
     */
    public void testReplaceItemValueDateRange(final Calendar value1, final Calendar value2, final TimeZone zone) {
        testReplaceItemValueDateRange(new GregorianDateRange(value1, value2), zone);
    }

    private void testReplaceItemValueDateRange(final DDateRange value, final TimeZone zone) {
        System.out.println("-> testReplaceItemValueDateRange: Zone: " + zone.getID());
        final DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Form", "testReplaceItemValueDateRange");
        doc.replaceItemValue(ITEM_NAME, value);
        doc.save();
        DDateRange result = doc.getItemValueDateRange(ITEM_NAME);
        assertEqualsDateRange(value, result, zone);
        final DItem item = (DItem) doc.getFirstItem(ITEM_NAME);
        result = item.getValueDateRange();
        assertEqualsDateRange(value, result, zone);
    }

    /**
     * Asserts equality of two lists of calendar instances.
     * 
     * @param expected expected value
     * @param result the value to test
     * @param zone time zone of test case
     */
    private void assertEqualsDateTimeList(final List expected, final List result, final TimeZone zone) {
        assertEquals("result length is wrong", expected.size(), result.size());
        Calendar value1 = (Calendar) expected.get(0);
        Calendar value2 = (Calendar) expected.get(1);
        Calendar result1 = (Calendar) result.get(0);
        Calendar result2 = (Calendar) result.get(1);
        result1.setTimeZone(zone);
        result2.setTimeZone(zone);
        assertValidCalendar(value1, result1);
        assertValidCalendar(value2, result2);
    }

    /**
     * Validates two calendar instances being equal.
     * 
     * @param expected the expected value
     * @param result the value to test
     */
    private void assertValidCalendar(Calendar expected, Calendar result) {
        System.out.println(expected.getTime() + " zone: " + expected.getTimeZone().getID());
        System.out.println(result.getTime() + " zone: " + result.getTimeZone().getID());
        int valueZoneOffset = expected.getTimeZone().getOffset(expected.getTimeInMillis()) / 60000;
        int resultZoneOffset = result.getTimeZone().getOffset(expected.getTimeInMillis()) / 60000;
        assertEquals(0, ((result.getTimeInMillis() - expected.getTimeInMillis()) / 60000));
        assertEquals("wrong zone", valueZoneOffset, resultZoneOffset);
        assertEquals("wrong time", expected.getTime(), result.getTime());
    }

    /**
     * Validates two date ranges being equal.
     * 
     * @param expected the expected value
     * @param result the value to test
     * @param zone expected time zone
     */
    private void assertEqualsDateRange(final DDateRange value, DDateRange result, final TimeZone zone) {
        final Calendar value1 = value.getFrom();
        final Calendar value2 = value.getTo();
        final Calendar result1 = result.getFrom();
        final Calendar result2 = result.getTo();
        result1.setTimeZone(zone);
        result2.setTimeZone(zone);
        assertValidCalendar(value1, result1);
        assertValidCalendar(value2, result2);
    }

    /**
     * @return DBaseDocument that is used for tests in this class, but is of a
     *         type of all extending classes.
     */
    protected DBaseDocument createDBaseDocument() {
        return getDatabase().createDocument();
    }

    /**
     * {@inheritDoc}
     * 
     * @see BaseProxyTest#setUpTest()
     */
    protected void setUpTest() {
    }
}
