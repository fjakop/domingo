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

package de.jakop.lotus.domingo.http;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Enumeration of all possible time zones in Lotus Notes.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoTimeZone {

    /**
     * value for field <tt>tz_dst</tt> for 'dailight saving time not
     * observed'.
     * TODO is this used somewhere?
     */
    public static final String DST_NOT_OBSERVED = "0";

    /** value for field <tt>tz_dst</tt> for 'dailight saving time observed'. */
    public static final String DST_OBSERVED = "1";

    /** List of possible time zones in Lotus Notes. */
    private static final List TIME_ZONES_LIST = new ArrayList();

    /** Map of possible time zones in Lotus Notes. */
    private static final Map TIME_ZONES_MAP = new HashMap();

    /** name of the time zone. */
    private final String fName;

    /** value of the time zone as for field <tt>tz_zone</tt>. */
    private final String fValue;

    /** whether to observe dailight saving time or not. */
    private final boolean fDst;

    /**
     * Constructor.
     *
     * @param name name of the time zone
     * @param value value of the time zone
     * @param dst observe daylight saving time
     */
    private DominoTimeZone(final String name, final String value, final boolean dst) {
        fName = name;
        fValue = value;
        fDst = dst;
    }

    /**
     * Returns the time zone value for a given name.
     *
     * @param name exact name of a time zone
     * @return the time zone value
     */
    public static DominoTimeZone get(final String name) {
        return (DominoTimeZone) TIME_ZONES_MAP.get(name);
    }

    /**
     * Returns the time zone value for a given search string.
     *
     * <p>The search string is simply a partial string of a time zone name.</p>
     *
     * @param searchString search string of a time zone
     * @return the time zone value or <code>null</code> if not found
     */
    public static DominoTimeZone searchTimeZone(final String searchString) {
        if (searchString == null) {
            return null;
        }
        Iterator iterator = TIME_ZONES_LIST.iterator();
        while (iterator.hasNext()) {
            DominoTimeZone timeZone = (DominoTimeZone) iterator.next();
            if (timeZone.getName().indexOf(searchString) >= 0) {
                return timeZone;
            }
        }
        return null;
    }

    /**
     * Returns whether daylight saving time should be observed.
     *
     * @return <code>true</code> if daylight saving time should be observed,
     *         else <code>false</code>
     */
    public boolean isDst() {
        return fDst;
    }

    /**
     * The name of the time zone.
     *
     * @return time zone name
     */
    public String getName() {
        return fName;
    }

    /**
     * The value of the time zone.
     *
     * @return the time zone value
     */
    public String getValue() {
        return fValue;
    }

    /**
     * The value of the time zone.
     *
     * @return time zone value
     */
    public String getTimeZones() {
        return fValue;
    }

    /**
     * Collection of all time zone values.
     *
     * @return collection of time zone values
     */
    public Collection getTimeZoneValues() {
        return TIME_ZONES_MAP.values();
    }

    static {
        addTimeZone("(GMT-12:00) International Date Line West", "Dateline:12:0", false);
        addTimeZone("(GMT-11:00) Midway Island, Samoa", "Samoa:11:0", false);
        addTimeZone("(GMT-10:00) Hawaii", "Hawaiian:10:0", false);
        addTimeZone("(GMT-09:00) Alaska", "Alaskan:9:4|1|1|10|-1|1", true);
        addTimeZone("(GMT-08:00) Pacific Time (US & Canada); Tijuana", "Pacific:8:4|1|1|10|-1|1", true);
        addTimeZone("(GMT-07:00) Mountain Time (US & Canada)", "Mountain:7:4|1|1|10|-1|1", true);
        addTimeZone("(GMT-07:00) Chihuahua, La Paz, Mazatlan", "Mexico%202:7:5|1|1|9|-1|1", true);
        addTimeZone("(GMT-07:00) Arizona", "US%20Mountain:7:0", false);
        addTimeZone("(GMT-06:00) Central Time (US & Canada)", "Central:6:4|1|1|10|-1|1", true);
        addTimeZone("(GMT-06:00) Guadalajara, Mexico City, Monterrey", "Mexico:6:4|1|1|9|-1|1", true);
        addTimeZone("(GMT-06:00) Central America, Saskatchewan", "Central%20America/Canada%20Central:6:0", false);
        addTimeZone("(GMT-05:00) Eastern Time (US & Canada)", "Eastern:5:4|1|1|10|-1|1", true);
        addTimeZone("(GMT-05:00) Indiana(East), Bogota, Lima, Quito", "US%20Eastern/SA%20Pacific:5:0", false);
        addTimeZone("(GMT-04:00) Atlantic Time (Canada)", "Atlantic:4:4|1|1|10|-1|1", true);
        addTimeZone("(GMT-04:00) Santiago", "Pacific%20SA:4:10|2|7|3|2|7", true);
        addTimeZone("(GMT-04:00) Caracas, La Paz", "SA%20Western:4:0", false);
        addTimeZone("(GMT-03:30) Newfoundland", "Newfoundland:3003:4|1|1|10|-1|1", true);
        addTimeZone("(GMT-03:00) Brasilia", "E.%20South%20America:3:10|3|1|2|2|1", true);
        addTimeZone("(GMT-03:00) Greenland", "Greenland:3:4|1|1|10|-1|1", true);
        addTimeZone("(GMT-03:00) Buenos Aires, Georgetown", "SA%20Eastern:3:0", false);
        addTimeZone("(GMT-02:00) Mid-Atlantic", "Mid-Atlnatic:2:3|-1|1|9|-1|1", true);
        addTimeZone("(GMT-01:00) Azores", "Azores:1:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT-01:00) Cape Verde Is.", "Cape%20Verde:1:0", false);
        addTimeZone("(GMT) Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London", "GMT:0:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT) Casablanca, Monrovia", "Greenwich:0:0", false);
        addTimeZone("(GMT+01:00) Central European", "Western/Central%20Europe:-1:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+01:00) West Central Africa", "W.%20Central%20Africa:-1:0", false);
        addTimeZone("(GMT+02:00) Eastern European", "Eastern%20Europe:-2:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+02:00) Cairo", "Egypt:-2:5|1|6|9|-1|4", true);
        addTimeZone("(GMT+02:00) Jerusalem, Harare, Pretoria", "Israel/South%20Africa:-2:0", false);
        addTimeZone("(GMT+03:00) Moscow, St. Petersburg, Volgograd", "Russian:-3:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+03:00) Baghdad", "Arabic:-3:4|1|1|10|1|1", true);
        addTimeZone("(GMT+03:00) Kuwait, Riyadh, Nairobi", "Arab/E.%20Africa:-3:0", false);
        addTimeZone("(GMT+03:30) Tehran", "Iran:-3003:3|1|1|9|4|3", true);
        addTimeZone("(GMT+04:00) Baku, Tbilisi, Yerevan", "Caucasus:-4:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+04:00) Abu Dhabi, Muscat", "Arabian:-4:0", false);
        addTimeZone("(GMT+04:30) Kabul", "Afghanistan:-3004:0", false);
        addTimeZone("(GMT+05:00) Ekaterinburg", "Ekaterinburg:-5:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+05:00) Islamabad, Karachi, Tashkent", "West%20Asia:-5:0", false);
        addTimeZone("(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi", "India:-3005:0", false);
        addTimeZone("(GMT+05:45) Kathmandu", "Nepal:-4505:0", false);
        addTimeZone("(GMT+06:00) Almaty, Novosibirsk", "N.%20Central%20Asia:-6:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+06:00) Astana, Dhaka, Sri Jayawardenepura", "Central%20Asia/Sri%20Lanka:-6:0", false);
        addTimeZone("(GMT+06:30) Rangoon", "Myanmar:-3006:0", false);
        addTimeZone("(GMT+07:00) Bangkok, Hanoi, Jakarta", "SE%20Asia:-7:0", false);
        addTimeZone("(GMT+07:00) Krasnoyarsk", "North%20Asia:-7:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+08:00) Beijing, Taipei, Singapore, Perth", "China/Singapore/Taiwan/W.%20Australia:-8:0", false);
        addTimeZone("(GMT+08:00) Irkutsk, Ulaan Bataar", "North%20Asia%20East:-8:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+09:00) Seoul, Tokyo", "Japan/Korea:-9:0", false);
        addTimeZone("(GMT+09:00) Yakutsk", "Yakutsk:-9:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+09:30) Adelaide", "Cen.%20Australia:-3009:10|-1|1|3|-1|1", true);
        addTimeZone("(GMT+09:30) Darwin", "AUS%20Central:-3009:0", false);
        addTimeZone("(GMT+10:00) Canberra, Melbourne, Sydney", "AUS%20Eastern:-10:10|-1|1|3|-1|1", true);
        addTimeZone("(GMT+10:00) Vladivostok", "Vladivostok:-10:3|-1|1|10|-1|1", true);
        addTimeZone("(GMT+10:00) Hobart", "Tasmania:-10:10|1|1|3|-1|1", true);
        addTimeZone("(GMT+10:00) Brisbane, Guam, Port Moresby", "E.%20Australia/West%20Pacific:-10:0", false);
        addTimeZone("(GMT+11:00) Magadan, Solomon Is., New Caledonia", "Central%20Pacific:-11:0", false);
        addTimeZone("(GMT+12:00) Auckland, Wellington", "New%20Zealand:-12:10|1|1|3|3|1", true);
        addTimeZone("(GMT+12:00) Fiji, Kamchatka, Marchall Is.", "Fiji:-12:0", false);
        addTimeZone("(GMT+13:00) Nuku\'alofa", "Tonga:-13:0", false);
    }

    /**
     * Adds a time zone to the list of time zones possible in Lotus Notes.
     *
     * @param name name of the time zone
     * @param value value of the time zone
     * @param dst observe daylight saving time
     */
    private static void addTimeZone(final String name, final String value, final boolean dst) {
        DominoTimeZone dominoTimeZone = new DominoTimeZone(name, value, dst);
        TIME_ZONES_LIST.add(dominoTimeZone);
        TIME_ZONES_MAP.put(name, dominoTimeZone);
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DominoTimeZone)) {
            return false;
        }
        final DominoTimeZone dtz = (DominoTimeZone) obj;
        return fName.equals(dtz.fName) && fValue.equals(dtz.fValue) && fDst == dtz.fDst;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (fName + fValue + fDst).hashCode();
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[name=" + fName + ", value=" + fValue + ", dst=" + fDst + "]";
    }

    /**
     * Serialization helper used to resolve the enumeration instances.
     */
    private Object readResolve() throws ObjectStreamException {
        return TIME_ZONES_MAP.get(this.fName);
    }
}
