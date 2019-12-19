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

package de.jakop.lotus.domingo.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import lotus.domino.NotesException;


/**
 * Wrapper for Notes Exceptions.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoException extends Exception {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257003241924735281L;

    private final NotesException fCause;

    /**
     * Construct a new <code>DominoException</code> instance.
     *
     * @param theNotesException the root cause of the exception
     */
    public DominoException(final NotesException theNotesException) {
        super("[" + theNotesException.id + "] " + theNotesException.text);
        fCause = theNotesException;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() {
        fCause.printStackTrace();
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(final PrintStream s) {
        fCause.printStackTrace(s);
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(final PrintWriter s) {
        fCause.printStackTrace(s);
    }
}
