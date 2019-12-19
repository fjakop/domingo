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

import java.util.logging.Level;
import java.util.logging.Logger;

import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Adapter from Java Logging to the domingo monitor interface.
 *
 * <p>Use an instance of this class as argument to
 * DNotesFactory.getInstance(DNotesMonitor theMonitor).</p>
 *
 * @since domingo 1.1
 */
public final class Jdk14LoggerMonitor implements DNotesMonitor {

    /** Reference to the log that will receive all log messages from Domingo. */
    private Logger logger;

    /**
     * Constructor.
     *
     * <p>Creates a monitor with a default log.</p>
     */
    public Jdk14LoggerMonitor() {
        this(Logger.getLogger("JDK14LoggerMonitor"));
    }

    /**
     * Constructor.
     *
     * @param log a log instance
     */
    public Jdk14LoggerMonitor(final Logger log) {
        logger = log;
    }

    /**
     * Returns the current log.
     *
     * @return current log
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Sets a new log.
     *
     * @param log the new log
     */
    public void setLogger(final Logger log) {
        this.logger = log;
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#debug(java.lang.String, java.lang.Throwable)
     */
    public void debug(final String message, final Throwable throwable) {
        log(Level.FINE, message, throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#debug(java.lang.String)
     */
    public void debug(final String message) {
        log(Level.FINE, message, null);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#info(java.lang.String, java.lang.Throwable)
     */
    public void info(final String message, final Throwable throwable) {
        log(Level.INFO, String.valueOf(message), throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#info(java.lang.String)
     */
    public void info(final String message) {
        log(Level.INFO, String.valueOf(message), null);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#warn(java.lang.String, java.lang.Throwable)
     */
    public void warn(final String message, final Throwable throwable) {
        log(Level.WARNING, String.valueOf(message), throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#warn(java.lang.String)
     */
    public void warn(final String message) {
        log(Level.WARNING, String.valueOf(message), null);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#error(java.lang.String, java.lang.Throwable)
     */
    public void error(final String message, final Throwable throwable) {
        log(Level.SEVERE, String.valueOf(message), throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#error(java.lang.String)
     */
    public void error(final String message) {
        log(Level.SEVERE, String.valueOf(message), null);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#fatalError(java.lang.String, java.lang.Throwable)
     */
    public void fatalError(final String message, final Throwable throwable) {
        log(Level.SEVERE, String.valueOf(message), throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#fatalError(java.lang.String)
     */
    public void fatalError(final String message) {
        log(Level.SEVERE, String.valueOf(message), null);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isDebugEnabled()
     */
    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isInfoEnabled()
     */
    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isWarnEnabled()
     */
    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isErrorEnabled()
     */
    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isFatalErrorEnabled()
     */
    public boolean isFatalErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    /**
     * Logs a log event depending on the level of the logger.
     *
     * @param level the log level
     * @param msg the message
     * @param throwable optional throwable
     */
    private void log(final Level level, final String msg, final Throwable throwable) {
        if (logger.isLoggable(level)) {
            Throwable dummyException = new Throwable();
            StackTraceElement[] locations = dummyException.getStackTrace();
            String cname = "unknown";
            String method = "unknown";
            if (locations != null && locations.length > 2) {
                StackTraceElement caller = locations[2];
                cname = caller.getClassName();
                method = caller.getMethodName();
            }
            if (throwable == null) {
                logger.logp(level, cname, method, msg);
            } else {
                logger.logp(level, cname, method, msg, throwable);
            }
        }
    }
}
