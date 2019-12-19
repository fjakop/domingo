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

import java.util.TimeZone;

import junit.framework.TestCase;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class TimezonesTest extends TestCase {

    /**
     * @param name the name of the test
     */
    public TimezonesTest(String name) {
        super(name);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneEuropeBerlinString() {
        String javaTimeZone = "Europe/Berlin";
        String lotusTimeZone = Timezones.getLotusTimeZoneString(javaTimeZone);
        System.out.println("Lotus time zone for '" + javaTimeZone + "' = '" + lotusTimeZone + "'");
        assertEquals("Z=-1$DO=1$DL=3 -1 1 10 -1 1$ZX=70$ZN=W. Europe", lotusTimeZone);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneUSPacificString() {
        String javaTimeZone = "US/Pacific";
        String lotusTimeZone = Timezones.getLotusTimeZoneString(javaTimeZone);
        System.out.println("Lotus time zone for '" + javaTimeZone + "' = '" + lotusTimeZone + "'");
        assertEquals("Z=8$DO=1$DL=3 2 1 11 1 1$ZX=61$ZN=Pacific", lotusTimeZone);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneEuropeBerlinTimeZone() {
        TimeZone javaTimeZone = TimeZone.getTimeZone("Europe/Berlin");
        String lotusTimeZone = Timezones.getLotusTimeZoneString(javaTimeZone);
        System.out.println("Lotus time zone for '" + javaTimeZone + "' = '" + lotusTimeZone + "'");
        assertEquals("Z=-1$DO=1$DL=3 -1 1 10 -1 1$ZX=70$ZN=W. Europe", lotusTimeZone);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneUSPacificTimeZone() {
        TimeZone javaTimeZone = TimeZone.getTimeZone("US/Pacific");
        String lotusTimeZone = Timezones.getLotusTimeZoneString(javaTimeZone);
        System.out.println("Lotus time zone for '" + javaTimeZone + "' = '" + lotusTimeZone + "'");
        assertEquals("Z=8$DO=1$DL=3 2 1 11 1 1$ZX=61$ZN=Pacific", lotusTimeZone);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneUnknown() {
        String javaTimeZone = "Unknown";
        try {
            Timezones.getLotusTimeZoneString(javaTimeZone);
        } catch (NullPointerException e) {
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            return; // ok, exception thrown as excpected
        }
        fail("IllegalArgumentException expected");
    }

    /**
     * test time zone null string.
     */
    public void testTimezoneNullString() {
        String javaTimeZone = null;
        try {
            Timezones.getLotusTimeZoneString((String) javaTimeZone);
        } catch (NullPointerException e) {
            return; // ok, exception thrown as excpected
        } catch (IllegalArgumentException e) {
            fail("NullPointerException expected");
        }
        fail("NullPointerException expected");
    }

    /**
     * test time zone empty string.
     */
    public void testTimezoneEmptyString() {
        String javaTimeZone = "";
        try {
            Timezones.getLotusTimeZoneString((String) javaTimeZone);
        } catch (NullPointerException e) {
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            return; // ok, exception thrown as excpected
        }
        fail("IllegalArgumentException expected");
    }

    /**
     * test time zone null timezone.
     */
    public void testTimezoneNullTimezone() {
        TimeZone javaTimeZone = null;
        try {
            Timezones.getLotusTimeZoneString((TimeZone) javaTimeZone);
        } catch (NullPointerException e) {
            return; // ok, exception thrown as excpected
        } catch (IllegalArgumentException e) {
            fail("NullPointerException expected");
        }
        fail("NullPointerException expected");
    }
}
