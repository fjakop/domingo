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

package de.jakop.lotus.domingo.proxy;

import java.util.TimeZone;

import junit.framework.TestCase;

/**
 * Tests for class {@link InternationalProxy}.
 * 
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class InternationalProxyTest extends TestCase {

    /**
     * Test computing a Notes time zone value.
     */
    public void testGetNotesTimeZoneValue() {
        long date = System.currentTimeMillis();
        assertEquals(0, InternationalProxy.getNotesTimeZoneValue(TimeZone.getTimeZone("GMT"), date));
        assertEquals(5, InternationalProxy.getNotesTimeZoneValue(TimeZone.getTimeZone("America/New_York"), date));
        assertEquals(3003, InternationalProxy.getNotesTimeZoneValue(TimeZone.getTimeZone("Canada/Newfoundland"), date));
        assertEquals(-4505, InternationalProxy.getNotesTimeZoneValue(TimeZone.getTimeZone("Asia/Katmandu"), date));
        assertEquals(-3009, InternationalProxy.getNotesTimeZoneValue(TimeZone.getTimeZone("Australia/Adelaide"), date));
        assertEquals(-1, InternationalProxy.getNotesTimeZoneValue(TimeZone.getTimeZone("Europe/Berlin"), date));
        assertEquals(-3, InternationalProxy.getNotesTimeZoneValue(TimeZone.getTimeZone("Europe/Moscow"), date));
    }

    /**
     * Test computing a Java offset.
     */
    public void testGetOffset() {
        assertEquals(0, 0);
        assertEquals(+500 * 60 * 60 * 10, InternationalProxy.getRawOffset(5));
        assertEquals(+350 * 60 * 60 * 10, InternationalProxy.getRawOffset(3003));
        assertEquals(-575 * 60 * 60 * 10, InternationalProxy.getRawOffset(-4505));
        assertEquals(-950 * 60 * 60 * 10, InternationalProxy.getRawOffset(-3009));
        assertEquals(-100 * 60 * 60 * 10, InternationalProxy.getRawOffset(-1));
        assertEquals(-300 * 60 * 60 * 10, InternationalProxy.getRawOffset(-3));
    }
}
