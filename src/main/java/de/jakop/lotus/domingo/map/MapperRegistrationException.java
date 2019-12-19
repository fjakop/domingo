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

package de.jakop.lotus.domingo.map;

import de.jakop.lotus.domingo.DNotesException;

/**
 * Signals errors during mapping from or to domingo.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public class MapperRegistrationException extends DNotesException {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257572801899870265L;

    /**
     * Constructor.
     *
     * @param message The detail message for this exception.
     */
    public MapperRegistrationException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param throwable the root cause of the exception
     */
    public MapperRegistrationException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructor.
     *
     * @param message The detail message for this exception.
     * @param throwable the root cause of the exception
     */
    public MapperRegistrationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
