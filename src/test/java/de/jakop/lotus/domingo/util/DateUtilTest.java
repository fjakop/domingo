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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DateUtilTest extends TestCase {

    /** A gregorian test date. */
    private final Calendar gregorianDate = new GregorianDate(2007, Calendar.DECEMBER, 31);

    /** A gregorian test time. */
    private final Calendar gregorianTime = new GregorianTime(23, 59, 58);

    /** A gregorian test date/time. */
    private final Calendar gregorianDateTime = new GregorianDateTime(2007, Calendar.DECEMBER, 31, 23, 59, 58);

    /** String format of {@link #gregorianDate}. */
    private  static final String SAMPLE_DATE_STRING = "2007-12-31";

    /** String format of {@link #gregorianTime}. */
    public static final String SAMPLE_TIME_STRING = "23:59:58";
    
    /** String format of {@link #gregorianDateTime}. */
    public static final String SAMPLE_DATE_TIME_STRING = SAMPLE_DATE_STRING + " " + SAMPLE_TIME_STRING;

    /**
     * @param name the name of the test
     */
    public DateUtilTest(String name) {
        super(name);
    }

    /**
     * test time zone conversions.
     */
    public void testParse() {
        String dateString1 = SAMPLE_DATE_TIME_STRING;
        Calendar calendar = DateUtil.parseDate(dateString1, false);
        String dateString2 = DateUtil.getDateTimeString(calendar);
        System.out.println(dateString1 + " -> " + dateString2);
        assertEquals(dateString1, dateString2);
    }

    /**
     * Test time formatting.
     */
    public void testGetTimeString() {
        assertEquals("", DateUtil.getTimeString(gregorianDate));
        assertEquals(SAMPLE_TIME_STRING, DateUtil.getTimeString(gregorianTime));
        assertEquals(SAMPLE_TIME_STRING, DateUtil.getTimeString(gregorianDateTime));
        assertEquals("", DateUtil.getTimeString(null));
    }

    /**
     * Test date formatting.
     */
    public void testGetDateString() {
        assertEquals(SAMPLE_DATE_STRING, DateUtil.getDateString(gregorianDate));
        assertEquals("", DateUtil.getDateString(gregorianTime));
        assertEquals(SAMPLE_DATE_STRING, DateUtil.getDateString(gregorianDateTime));
        assertEquals("", DateUtil.getDateString(null));
    }

    /**
     * Test date/time formatting.
     */
    public void testGetDateTimeString() {
        assertEquals(SAMPLE_DATE_STRING, DateUtil.getDateTimeString(gregorianDate));
        assertEquals(SAMPLE_TIME_STRING, DateUtil.getDateTimeString(gregorianTime));
        assertEquals(SAMPLE_DATE_TIME_STRING, DateUtil.getDateTimeString(gregorianDateTime));
        assertEquals("", DateUtil.getDateTimeString(null));
    }
}
