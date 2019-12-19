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

package de.jakop.lotus.domingo.notes;

import java.io.PrintStream;
import java.io.PrintWriter;

import de.jakop.lotus.domingo.exception.ExceptionUtil;
import de.jakop.lotus.domingo.monitor.AbstractDefaultMonitor;
import de.jakop.lotus.domingo.monitor.AbstractMonitor;
import lotus.domino.AgentBase;
import lotus.domino.Session;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.i18n.ResourceManager;
import de.jakop.lotus.domingo.i18n.Resources;


/**
 * Domingo agents must extend DAgentBase and use run() as the entry point for their
 * functional code. Use {link #getDSession() getDSession()} to get a DSession
 * object. For output to browsers as well as Notes clients (the Java debug
 * console), create a PrintWriter object with
 * {@link #getAgentOutput() getAgentOutput()}.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class DAgentBase extends AgentBase {

    /** Reference to the Notes Session. */
    private DSession session;

    /** Reference to a Domingo Monitor. */
    private NotesAgentMonitor monitor;

    /** Whether access to the Notes session is allowed or not. */
    private boolean isSessionProtected = false;

    /** Internationalized resources. */
    private static final Resources RESOURCES = ResourceManager.getPackageResources(DAgentBase.class);

    /**
     * Constructor.
     */
    public DAgentBase() {
        super();
    }

    /**
     * main method to be implemented by concrete agents.
     */
    public abstract void main();

    /**
     * @see lotus.domino.AgentBase#NotesMain()
     */
    public final void NotesMain() {
        DNotesFactory factory = null;
        try {
            monitor = new NotesAgentMonitor(AbstractMonitor.WARN);
            factory = DNotesFactory.newInstance("de.bea.domingo.proxy.NotesProxyFactory", monitor);
            session = factory.getSession(super.getSession());
        } catch (Exception e) {
            getMonitor().fatalError(RESOURCES.getString("agent.init.failed"), e);
        }
        try {
            isSessionProtected = true;
            main();
        } catch (DNotesRuntimeException  e) {
            getMonitor().fatalError(RESOURCES.getString("agent.run.failed"), e);
        } finally {
            isSessionProtected = false;
        }
        try {
            if (factory != null) {
                factory.disposeInstance(false);
            }
        } catch (Exception e) {
            getMonitor().fatalError(RESOURCES.getString("agent.dispose.failed"), e);
        }
    }

    /**
     * Returns the Domingo session of the agent.
     *
     * @return Domingo session
     */
    protected final DSession getDSession() {
        return session;
    }

    /**
     * This method should not be called in Domingo agents. and always returns a runtime
     * exception if a Domingo agent tries to get the internal Notes session.
     *
     * @return the underlying Notes session.
     * @deprecated use method getDSession()
     */
    public final Session getSession() {
        if (isSessionProtected) {
            throw new DNotesRuntimeException("Illegal access to notes session in domingo agent");
        } else {
            return super.getSession();
        }
    }

    /**
     * Returns the monitor of the current Domingo agent.
     *
     * @return monitor of current Domingo agent
     */
    public final DNotesMonitor getMonitor() {
        return monitor;
    }

    /**
     * Adapter from a {@link DNotesMonitor DNotesMonitor} to
     * the debug system of Lotus Notes.
     *
     * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
     */
    private class NotesAgentMonitor extends AbstractDefaultMonitor {

        /**
         * Constructor.
         *
         * @param theLevel the level of the new monitor, can be one of
         * {@link AbstractMonitor#DEBUG DEBUG},
         * {@link AbstractMonitor#INFO INFO},
         * {@link AbstractMonitor#WARN WARN},
         * {@link AbstractMonitor#ERROR ERROR} or
         * {@link AbstractMonitor#FATAL FATAL}
         */
        public NotesAgentMonitor(final int theLevel) {
            super(theLevel);
        }

        /**
         * Constructor.
         *
         * @param base the calling Notes agent
         * @param theLevel the level of the new monitor
         */
        public NotesAgentMonitor(final DAgentBase base, final int theLevel) {
            super(theLevel);
        }

        /**
         * @see AbstractDefaultMonitor#monitor(java.lang.String)
         */
        protected void monitor(final String message) {
            dbgMsg(message);
        }

        /**
         * @see AbstractDefaultMonitor#monitor(java.lang.Throwable)
         */
        protected void monitor(final Throwable throwable) {
            if (throwable != null) {
                dbgMsg(ExceptionUtil.getStackTrace(throwable));
            }
        }
    }

    /**
     * If debugging is enabled, send a debug message to the specified stream.
     *
     * @param message the message
     * @param out the output stream
     * @see #setDebug(boolean)
     */
    public final void dbgMsg(final String message, final PrintStream out) {
        super.dbgMsg(message, out);
    }

    /**
     * If debugging is enabled, send a debug message to the specified writer.
     *
     * @param message the message
     * @param out the output writer
     * @see #setDebug(boolean)
     */
    public final void dbgMsg(final String message, final PrintWriter out) {
        super.dbgMsg(message, out);
    }

    /**
     * If debugging is enabled, send a debug message to <tt>System.out</tt>.
     *
     * @param message the message
     * @see #setDebug(boolean)
     */
    public final void dbgMsg(final String message) {
        super.dbgMsg(message);
    }

    /**
     * Returns the output writer of the current agent.
     *
     * @return output writer of the current agent
     */
    public final PrintWriter getAgentOutput() {
        return super.getAgentOutput();
    }

    /**
     * Checks if the current agent is a restricted agent or not.
     *
     * @return <tt>true</tt> if the current agent is restricted, else <tt>false</tt>
     */
    public final boolean isRestricted() {
        return super.isRestricted();
    }

    /**
     * Sets whether debugging is enabled or not for the current agent.
     *
     * @param b <tt>true</tt> if debugging should be enabled, else <tt>false</tt>
     */
    public final void setDebug(final boolean b) {
        super.setDebug(b);
    }

    /**
     * Activates or deactivates tracing method calls of the JVM.
     *
     * @see java.lang.Runtime#traceMethodCalls(boolean)
     *
     * @param b <code>true</code> to enable instruction tracing;
     *            <code>false</code> to disable this feature.
     */
    public final void setTrace(final boolean b) {
        super.setTrace(b);
        monitor.setLevel(AbstractMonitor.DEBUG);
    }
}
