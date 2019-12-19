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

package de.jakop.lotus.domingo.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.exception.ExceptionUtil;

/**
 * Writes an error to the response writer.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ErrorCommand implements Command {

    /**
     * {@inheritDoc}
     *
     * @see Command#execute(DSession, java.util.Map, java.io.PrintWriter)
     */
    public void execute(final DSession session, final Map parameters, final PrintWriter printWriter)
        throws DNotesException, IOException {
        Exception e = (Exception) parameters.get("exception");
        String id = (String) parameters.get("error_id");
        printError(printWriter, "", id, e);
    }

    /**
     * Prints an error to the output stream.
     *
     * @param message the error message
     * @param e optional exception causing the error
     */
    private void printError(final PrintWriter agentOutput, final String message, final String id, final Throwable t) {
        agentOutput.println("<?xml version=\"1.0\" ?>");
        agentOutput.println("<error>");
        agentOutput.println("  <id>" + id + "</id");
        agentOutput.println("  <message>" + message + "</message");
        if (t != null) {
            agentOutput.print("  <exception>" + ExceptionUtil.getStackTrace(t) + "</eception>");
        }
        agentOutput.print("</error>");
    }
}
