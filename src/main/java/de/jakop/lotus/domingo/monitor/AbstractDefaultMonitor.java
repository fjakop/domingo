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

package de.jakop.lotus.domingo.monitor;

import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Abstract base class for implementations of the Monitor interface.
 *
 * <p>This class unifies logging to a single method for easy sub classing.
 * Subclasses only have to implement the two methods
 * <code>monitor(java.lang.String)</code> and
 * <code>monitor(java.lang.Throwable)</code>.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class AbstractDefaultMonitor extends AbstractMonitor {

    /**
     * Default constructor.
     */
    public AbstractDefaultMonitor() {
        super();
    }

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
    public AbstractDefaultMonitor(final int theLevel) {
        super(theLevel);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#debug(java.lang.String)
     */
    public final void debug(final String s) {
        if (isDebugEnabled()) {
            monitor("DEBUG: " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#debug(java.lang.String, java.lang.Throwable)
     */
    public final void debug(final String s, final Throwable throwable) {
        if (isDebugEnabled()) {
            monitor("DEBUG: " + s);
            monitor(throwable);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#info(java.lang.String)
     */
    public final void info(final String s) {
        if (isInfoEnabled()) {
            monitor("INFO:  " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#info(java.lang.String, java.lang.Throwable)
     */
    public final void info(final String s, final Throwable throwable) {
        if (isInfoEnabled()) {
            monitor("INFO:  " + s);
            monitor(throwable);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#warn(java.lang.String)
     */
    public final void warn(final String s) {
        if (isWarnEnabled()) {
            monitor("WARN:  " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#warn(java.lang.String, java.lang.Throwable)
     */
    public final void warn(final String s, final Throwable throwable) {
        if (isWarnEnabled()) {
            monitor("WARN:  " + s);
            monitor(throwable);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#error(java.lang.String)
     */
    public final void error(final String s) {
        if (isErrorEnabled()) {
            monitor("ERROR: " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#error(java.lang.String, java.lang.Throwable)
     */
    public final void error(final String s, final Throwable throwable) {
        if (isErrorEnabled()) {
            monitor("ERROR: " + s);
            monitor(throwable);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#fatalError(java.lang.String)
     */
    public final void fatalError(final String s) {
        if (isFatalErrorEnabled()) {
            monitor("FATAL: " + s);
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#fatalError(java.lang.String, java.lang.Throwable)
     */
    public final void fatalError(final String s, final Throwable throwable) {
        if (isFatalErrorEnabled()) {
            monitor("FATAL: " + s);
            monitor(throwable);
        }
    }

    /**
     * Abstract monitoring method, must be implemented by concrete monitors.
     *
     * @param message a message to monitor
     */
    protected abstract void monitor(final String message);

    /**
     * Abstract monitoring method, must be implemented by concrete monitors.
     *
     * @param throwable a throwable to monitor
     */
    protected abstract void monitor(final Throwable throwable);
}
