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

import java.util.MissingResourceException;
import java.util.TimeZone;

import de.jakop.lotus.domingo.i18n.ResourceManager;
import de.jakop.lotus.domingo.i18n.Resources;

/**
 * Static utility methods for time zone operations and conversions.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede </a>
 */
public final class Timezones {

    /** Maps time zone strings to Lotus Notes format. */
    private static final Resources TIME_ZONES = ResourceManager.getClassResources(Timezones.class).withFailOnError(true);

    /** Default time zone. */
    public static final String DEFAULT = getLotusTimeZoneString(TimeZone.getDefault().getID());

    // TODO complete file TimezonesResources.properties

    /**
     * Private default constructor to prevent instantiation.
     */
    private Timezones() {
    }

    /**
     * Converts a Java time zone to a Lotus timezone string.
     * if the given java time zone string is null, empry or unknown,
     * the default lotus time zone string is returned (<tt>""</tt>).
     *
     * @param timeZone the time zone
     * @return Lotus time zone string
     * @throws NullPointerException if the given time zone string is null
     * @throws IllegalArgumentException if the length of the given time zone string is zero
     */
    public static String getLotusTimeZoneString(final TimeZone timeZone) throws NullPointerException, IllegalArgumentException {
        return getLotusTimeZoneString(timeZone.getID());
    }

    /**
     * Converts a Java time zone string to a Lotus timezone string.
     * if the given java time zone string is null, empry or unknown,
     * the default lotus time zone string is returned (<tt>""</tt>).
     *
     * @param javaTimeZoneString the java time zone string
     * @return Lotus time zone string
     * @throws NullPointerException if the given time zone string is null
     * @throws IllegalArgumentException if the given time zone string is empty or unknown
     */
    public static String getLotusTimeZoneString(final String javaTimeZoneString) throws NullPointerException,
            IllegalArgumentException {
        if (javaTimeZoneString == null) {
            throw new NullPointerException("timezone must not be null");
        }
        if (javaTimeZoneString.length() == 0) {
            throw new IllegalArgumentException("timezone must not be empty");
        }
        try {
            return TIME_ZONES.getString(javaTimeZoneString);
        } catch (MissingResourceException e) {
            throw new IllegalArgumentException("unknown timezone: " + javaTimeZoneString);
        }
    }
}
