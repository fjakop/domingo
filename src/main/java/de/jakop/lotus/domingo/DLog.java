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

/**
 * Enables you to record actions and errors that take place during
 * a program's execution. You can record actions and errors in.
 * <p><ul>
 * <li>A Domino database</li>
 * <li>A mail memo</li>
 * <li>A file (for programs that run locally)</li>
 * <li>An agent log (for agents)</li>
 * </ul></p>
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public interface DLog extends DBase {

    /**
     * Opens a notes database as the log target.
     *
     * @param server notes server name
     * @param database notes database filename
     */
    void openNotesLog(String server, String database);

    /**
     * Records an error in a log.
     *
     * @param code a number indicating which error occurred.
     * @param text a description of the error that occurred, as you want it to appear in the log.
     */
    void logError(int code, String text);

    /**
     * Records an action in a log.
     *
     * @param text a description of the action, as you want it to appear in the log.
     */
    void logAction(String text);
}
