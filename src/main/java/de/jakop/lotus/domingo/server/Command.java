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
 * TODO.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @param session domingo session for execution
     * @param parameters request parameters
     * @param printWriter writer for the response
     * @throws DNotesException if the command cannot be executed
     * @throws IOException if the response could not be created or completed
     */
    void execute(DSession session, Map parameters, PrintWriter printWriter) throws DNotesException, IOException;
}
