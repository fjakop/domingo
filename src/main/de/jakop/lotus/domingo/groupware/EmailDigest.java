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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.jakop.lotus.domingo.map.BaseDigest;
import de.jakop.lotus.domingo.util.GregorianDate;
import de.jakop.lotus.domingo.util.GregorianTime;

/**
 * Represents a digest of a Notes mail document.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class EmailDigest extends BaseDigest {

    private String who;

    private Calendar date;

    private Calendar time;

    private String subject;

    /**
     * @return Returns the date.
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * @param calendar The date to set.
     */
    public void setDate(final Calendar calendar) {
        this.date = calendar instanceof GregorianDate ? calendar : new GregorianDate(calendar);
    }

    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param text The subject to set.
     */
    public void setSubject(final String text) {
        this.subject = text;
    }

    /**
     * @return Returns the time.
     */
    public Calendar getTime() {
        return time;
    }

    /**
     * @param calendar The time to set.
     */
    public void setTime(final Calendar calendar) {
        this.time = calendar instanceof GregorianTime ? calendar : new GregorianTime(calendar);
    }

    /**
     * @return Returns the who.
     */
    public String getWho() {
        return who;
    }

    /**
     * @param person The who to set.
     */
    public void setWho(final String person) {
        this.who = person;
    }

    /**
     * @return  a string representation of the object.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z");
        return "" + sdf.format(this.getDate().getTime()) + " " + getWho() + "     " + getSubject();
    }
}
