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

import java.util.Calendar;

/**
 * A date range, can be a range of date-only calendars or a range of time-only calendars.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public interface DDateRange {

    /**
     * Returns the start date/time of the range.
     * @return start date/time of date range
     */
    Calendar getFrom();

    /**
     * Sets the start date/time of the range.
     * @param from start date/time of date range
     */
    void setFrom(final Calendar from);

    /**
     * Returns the end date/time of the range.
     * @return end date/time of date range
     */
    Calendar getTo();

    /**
     * Sets the end date/time of the range.
     * @param to end date/time of date range
     */
    void setTo(final Calendar to);
}
