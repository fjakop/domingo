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
package de.jakop.lotus.domingo.proxy;

import lotus.domino.Log;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DLog;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DSession;

/**
 * A notes Log.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class LogProxy extends BaseProxy implements DLog {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3762817086526863668L;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param log the Notes log object
     * @param monitor the monitor
     */
    private LogProxy(final NotesProxyFactory theFactory, final DBase parent,
                     final Log log, final DNotesMonitor monitor) {
        super(theFactory, parent, log, monitor);
        getFactory().preprocessMethod();
    }

    /**
     * Creates an notes log.
     *
     * @param theFactory the controlling factory
     * @param session the Notes Session
     * @param theLog the Notes Log
     * @param monitor the monitor
     * @return a log object
     */
    static DLog getInstance(final NotesProxyFactory theFactory, final DSession session,
                            final Log theLog, final DNotesMonitor monitor) {
        if (theLog == null) {
            return null;
        }
        LogProxy logProxy = (LogProxy) theFactory.getBaseCache().get(theLog);
        if (logProxy == null) {
            logProxy = new LogProxy(theFactory, session, theLog, monitor);
            theFactory.getBaseCache().put(theLog, logProxy);
        }
        return logProxy;
    }

    /**
     * Returns the notes log object.
     *
     * @return the notes log object
     */
    private Log getLog() {
        return (Log) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see DLog#logAction(java.lang.String)
     */
    public void logAction(final String text) {
        getFactory().preprocessMethod();
        try {
            getLog().logAction(text);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot log action", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DLog#logError(int, java.lang.String)
     */
    public void logError(final int code, final String text) {
        getFactory().preprocessMethod();
        try {
            getLog().logError(code, text);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot log error", e);
        }
    }

    /**
     * @see java.lang.Object#toString()
     * @return  a string representation of the object.
     */
    public String toString() {
        return super.toStringIntern(this);
    }

    /**
     * {@inheritDoc}
     * @see DLog#openNotesLog(java.lang.String, java.lang.String)
     */
    public void openNotesLog(final String server, final String database) {
        getFactory().preprocessMethod();
        try {
            getLog().openNotesLog(server, database);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot open notes log " + server + "!!" + database, e);
        }
    }
}
