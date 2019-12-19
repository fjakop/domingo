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

/**
 * Enumeration of notes errors.
 *
 * @see DNotesException
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class DNotesError {

    /** error: general error. */
    public static final DNotesError GENERAL_ERROR = new DNotesError(0);

    /** error: database not found. */
    public static final DNotesError DATABASE_NOT_FOUND = new DNotesError(1);

    /** error: no access to database. */
    public static final DNotesError DATABASE_NO_ACCESS = new DNotesError(2);

    /** internal error code. */
    private final int id;

    /**
     * Private Constructor.
     *
     * @param theId the error code
     */
    public DNotesError(final int theId) {
        this.id = theId;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @param   object the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the object
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof DNotesError)) {
            return false;
        }
        return this.id == ((DNotesError) object).id;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     * @return the id of the error
     */
    public int hashCode() {
        return id;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return the id of the error
     */
    public String toString() {
        return "" + id;
    }

    /**
     * Returns a message for a given error.
     *
     * @param e an error
     * @return a message for the error
     */
    public static String getMessage(final DNotesError e) {
        if (e.equals(DATABASE_NOT_FOUND)) {
            return "database not found";
        } else if (e.equals(DATABASE_NOT_FOUND)) {
            return "no access to database";
        } else {
            return "";
        }
    }
}
