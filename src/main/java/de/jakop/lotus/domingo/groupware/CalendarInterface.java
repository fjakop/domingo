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

package de.jakop.lotus.domingo.groupware;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Interface to the calendar functionality of a Notes mail database.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface CalendarInterface {

    /**
     * Saves a new calendar entry.
     *
     * @param entry the calendar entry to save
     */
    void save(final CalendarEntry entry);

    /**
     * Returns calendar objects within the specified time frame.
     *
     * @param from start date
     * @param to end date
     * @return list of calendar entries
     */
    List getObjects(Calendar from, Calendar to);

    /**
     * Returns an iterator over all entries in the Calendar.
     *
     * @return iterator over all entries in the Calendar.
     */
    Iterator getCalendar();

    /**
     * Returns an iterator over all entries in the Calendar.
     *
     * <p>Depending on how the Calendar is sorted (ascending or descending by
     * date), choose where to start reading entries.</p>
     *
     * @param reverseOrder <code>true</code> if iterator should iterate in
     *            reverse order
     * @return iterator over all entries in the Calendar.
     */
    Iterator getCalendar(final boolean reverseOrder);

    /**
     * Given a CalendarEntryDigest, retrieve the corresponding CalendarEntry.
     * @param ced the calendar entry digest
     * @return CalendarEntry
     */
    Object getCalendarEntry(final CalendarEntryDigest ced);

    /**
     * Returns a calendar entry for a given unid.
     * @see CalendarEntry#getAppointmentUnid()
     *
     * @param unid the Notes document unid
     * @return the calendar entry
     * @throws GroupwareException if the calendar entry cannot be
     * found or mapped.
     */
     Object getCalendarEntry(final String unid) throws GroupwareException;

    /**
     * Deletes an existing calendar entry.
     *
     * @param entry an entry to delete
     */
    void remove(final CalendarEntry entry);

    /**
     * Deletes an existing calendar entry.
     *
     * @param digest a calendar entry digest to delete
     */
    void remove(final CalendarEntryDigest digest);


}
