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

import java.io.PrintWriter;

/**
 * An interface to be implemented by {@link java.lang.Throwable} extensions
 * which would like to be able to cascade root exceptions inside themselves.
 */
public interface CascadingThrowable {

    /**
     * Returns the reference to the exception or error that caused the exception.
     *
     * @return throwable that caused the original exception
     */
    Throwable getCause();

    /**
     * Prints the stack trace for this exception only--root cause not
     * included--using the provided writer. Used by
     * {@link ExceptionUtil} to write
     * individual stack traces to a buffer. The implementation of this method
     * should call <code>super.printStackTrace(out);</code> in most cases.
     *
     * @param out The writer to use.
     */
    void printPartialStackTrace(PrintWriter out);
}
