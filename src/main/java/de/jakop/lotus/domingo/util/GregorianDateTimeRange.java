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

import de.jakop.lotus.domingo.DDateRange;


/**
 * A date/time range.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class GregorianDateTimeRange implements DDateRange {

    private GregorianDateTime fStart;

    private GregorianDateTime fEnd;

    /**
     * Constructor.
     */
    public GregorianDateTimeRange() {
    }

    /**
     * Constructor.
     *
     * @param start start date
     * @param end end date
     */
    public GregorianDateTimeRange(final Calendar start, final Calendar end) {
        setFrom(start);
        setTo(end);
    }

    /**
     * Returns the end date of the date range.
     *
     * @return start date
     */
    public Calendar getFrom() {
        return fStart;
    }

    /**
     * Returns the end date of the date range.
     *
     * @return end date
     */
    public Calendar getTo() {
        return fEnd;
    }

    /**
     * Sets the end date of the date range.
     *
     * @param end end date
     */
    public void setTo(final Calendar end) {
        if (end instanceof GregorianDateTime) {
            fEnd = (GregorianDateTime) end;
        } else {
            fEnd = new GregorianDateTime(end);
        }
    }

    /**
     * Sets the end date of the date range.
     *
     * @param start start date
     */
    public void setFrom(final Calendar start) {
        if (start instanceof GregorianDateTime) {
            fStart = (GregorianDateTime) start;
        } else {
            fStart = new GregorianDateTime(start);
        }
    }

    /**
     * {@inheritDoc}
     * @see java.util.Calendar#toString()
     */
    public String toString() {
        return DateUtil.getDateTimeString(getFrom()) + " - " + DateUtil.getDateTimeString(getTo());
    }
}
