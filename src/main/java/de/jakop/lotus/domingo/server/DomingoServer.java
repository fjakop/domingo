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
import java.util.HashMap;
import java.util.Map;

import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DSession;

/**
 * Domingo server.
 * TODO.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DomingoServer extends BaseCommand implements Command {

    private final Map commands = new HashMap();

    /**
     * Constructor.
     */
    public DomingoServer() {
        super();
        commands.put("error", new ErrorCommand());
        commands.put("readdatabase", new ReadDatabaseCommand());
        commands.put("readdocument", new ReadDocumentCommand());
        commands.put("savedocument", new SaveDocumentCommand());
        commands.put("createdatabase", new CreateDatabaseCommand());
        commands.put("createdatabasefromtemplate", new CreateDatabaseFromTemplateCommand());
    }

    /**
     * Executes a domingo request with the specified parameters.
     * Depending on the <code>cmd</code> parameter, the request is delegated to the
     * corresponsing implementation of the {@link Command} interface.
     *
     * @see Command#execute(DSession, java.util.Map, java.io.PrintWriter)
     *
     * @param session domingo session for execution
     * @param parameters request parameters
     * @param printWriter response stream for output
     * @throws DNotesException if the command cannot be executed
     * @throws IOException if the response could not be created or completed
     * @throws UnsupportedOperationException if the specified command doesn't exist
     */
    public void execute(final DSession session, final Map parameters, final PrintWriter printWriter)
            throws UnsupportedOperationException, DNotesException, IOException {
        final String commandString = getParameterString(parameters, "cmd");
        final Command command = (Command) commands.get(commandString.toLowerCase());
        Exception exception = null;
        if (command == null) {
            throw new UnsupportedOperationException("Cannot execute command " + commandString);
        }
        try {
            command.execute(session, parameters, printWriter);
        } catch (UnsupportedOperationException e) {
            parameters.put("error_id", "7001");
            exception = e;
        } catch (DNotesException e) {
            parameters.put("error_id", "7002");
            exception = e;
        } catch (IOException e) {
            parameters.put("error_id", "7003");
            exception = e;
        }
        if (exception != null) {
            parameters.put("exception", exception);
            Command errorCommand = (Command) commands.get("error");
            errorCommand.execute(session, parameters, printWriter);
        }
    }
}
