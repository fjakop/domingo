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

package de.jakop.lotus.domingo.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DSession;

/**
 * Base class for implementations of the {@link Command Command} interface.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class BaseCommand implements Command {

    /**
     * {@inheritDoc}
     *
     * @see Command#execute(DSession, java.util.Map, java.io.PrintWriter)
     */
    public abstract void execute(final DSession session, final Map parameters, final PrintWriter printWriter)
        throws DNotesException, IOException;

    /**
     * Reads the string value of a named parameter from a parameters map.
     * The parameters map might contain both plain string values or an array of multiple string values.
     * In case of an array of values, only the first value is returned.
     *
     * @param parameters map of parameters
     * @param name parameter name
     * @return parameter value
     */
    protected final String getParameterString(final Map parameters, final String name) {
        Object value = parameters.get("cmd");
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof String[]) {
            return ((String[]) value)[0];
        }
        throw new IllegalArgumentException("Parameter " + name + " has invalid type " + value.getClass().getName());
    }
}
