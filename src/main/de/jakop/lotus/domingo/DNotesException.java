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

package de.jakop.lotus.domingo;

import java.io.PrintStream;
import java.io.PrintWriter;

import de.jakop.lotus.domingo.exception.CascadingThrowable;
import de.jakop.lotus.domingo.exception.ExceptionUtil;

/**
 * Exception thrown from the domingo API.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public class DNotesException extends Exception implements CascadingThrowable {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257570589891048500L;

    /** detail error of the exception. */
    private final Throwable fCause;

    /**
     * Construct a new <code>DNotesException</code> instance.
     *
     * @param message The detail message for this exception.
     */
    public DNotesException(final String message) {
        super(message);
        fCause = null;
    }

    /**
     * Construct a new <code>DNotesException</code> instance.
     *
     * @param cause the cause of this throwable or <code>null</code> if the
     *          cause is nonexistent or unknown.
     */
    public DNotesException(final Throwable cause) {
        super();
        fCause = cause;
    }

    /**
     * Construct a new <code>DNotesException</code> instance.
     *
     * @param message The detail message for this exception.
     * @param cause the cause of this throwable or <code>null</code> if the
     *          cause is nonexistent or unknown.
     */
    public DNotesException(final String message, final Throwable cause) {
        super(message);
        fCause = cause;
    }

    /**
     * Returns the detail error that caused the exception.
     *
     * @return the cause of this throwable or <code>null</code> if the
     *          cause is nonexistent or unknown.
     * @see java.lang.Throwable#getCause()
     */
    public final Throwable getCause() {
        return fCause;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Throwable#getMessage()
     */
    public final String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        }
        if (fCause != null) {
            return fCause.toString();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Throwable#printStackTrace()
     */
    public final void printStackTrace() {
        ExceptionUtil.printStackTrace(this);
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public final void printStackTrace(final PrintStream out) {
        ExceptionUtil.printStackTrace(this, out);
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public final void printStackTrace(final PrintWriter out) {
        ExceptionUtil.printStackTrace(this, out);
    }

    /**
     * {@inheritDoc}
     * @see CascadingThrowable#printPartialStackTrace(java.io.PrintWriter)
     */
    public final void printPartialStackTrace(final PrintWriter out) {
        super.printStackTrace(out);
    }
}
