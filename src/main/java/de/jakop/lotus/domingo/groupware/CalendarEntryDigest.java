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

package de.jakop.lotus.domingo.groupware;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.jakop.lotus.domingo.groupware.CalendarEntry.Type;
import de.jakop.lotus.domingo.map.BaseDigest;
import de.jakop.lotus.domingo.util.DateUtil;

/**
 * Represents a digest of a Notes mail document.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 * @author Thanx to <a href=mailto:d-rizzle at users.sourceforge.net>Dave Rowe</a> for some patches
 */
public final class CalendarEntryDigest extends BaseDigest {

    private Type fEntryType;

    private Calendar fStartDateTime;

    private Calendar fEndDateTime;

    private String fSubject;

    private String fLocation;

    /** Meeting Chairs (List of Strings). */
    private List fChairs = new ArrayList();


    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return fSubject;
    }

    /**
     * @param text The subject to set.
     */
    public void setSubject(final String text) {
        this.fSubject = text;
    }

    /**
     * @return the fChair
     */
    public List getChairs() {
        return fChairs;
    }

    /**
     * @param chairs the fChair to set
     */
    public void setChairs(final List chairs) {
        fChairs = chairs;
    }

    /**
     * @return the fEndDateTime
     */
    public Calendar getEndDateTime() {
        return fEndDateTime;
    }

    /**
     * @param endDateTime the fEndDateTime to set
     */
    public void setEndDateTime(final Calendar endDateTime) {
        fEndDateTime = endDateTime;
    }

    /**
     * @return the fLocation
     */
    public String getLocation() {
        return fLocation;
    }

    /**
     * @param location the fLocation to set
     */
    public void setLocation(final String location) {
        fLocation = location;
    }

    /**
     * @return the fStartDateTime
     */
    public Calendar getStartDateTime() {
        return fStartDateTime;
    }

    /**
     * @param startDateTime the fStartDateTime to set
     */
    public void setStartDateTime(final Calendar startDateTime) {
        fStartDateTime = startDateTime;
    }

    /**
     * @return type of entry
     */
    public Type getType() {
        return fEntryType;
    }

    /**
     * @param type type of entry
     */
    public void setType(final Type type) {
        fEntryType = type;
    }

    /**
     * @return  a string representation of the object.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return fChairs + " " + fSubject + " " + fLocation + " Start: " + DateUtil.getDateTimeString(fStartDateTime);
    }
}
