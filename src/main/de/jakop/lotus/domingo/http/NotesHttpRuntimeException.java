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

import de.jakop.lotus.domingo.DNotesRuntimeException;

/**
 * Class from which all runtime exceptions in this package inherit.
 * Allows recording of nested exceptions.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class NotesHttpRuntimeException extends DNotesRuntimeException {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3256727294654428981L;

    /**
     * Construct a new <code>NotesHttpRuntimeException</code> instance.
     *
     * @param message The detail message for this exception.
     */
    public NotesHttpRuntimeException(final String message) {
        super(message);
    }

    /**
     * Construct a new <code>NotesHttpRuntimeException</code> instance.
     *
     * @param throwable the root cause of the exception
     */
    public NotesHttpRuntimeException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Construct a new <code>NotesHttpRuntimeException</code> instance.
     *
     * @param message The detail message for this exception.
     * @param throwable the root cause of the exception
     */
    public NotesHttpRuntimeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
