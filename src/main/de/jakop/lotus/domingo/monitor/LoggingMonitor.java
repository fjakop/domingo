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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Adapter from Apache Commons Logging to the domingo monitor interface.
 *
 * <p>Use an instance of this class as argument to
 * DNotesFactory.getInstance(DNotesMonitor theMonitor).</p>
 *
 * <p>If Apache Commons Logging should be used for logging, the commons-logging.jar
 * file must be available in the classpath. Apache Commons Logging can be downloaded at
 * <a target="_blank" href="http://jakarta.apache.org/commons/logging/"></a>.</p>
 *
 * @since domingo 1.1
 */
public final class LoggingMonitor implements DNotesMonitor {

    /** Reference to the log that will receive all log messages from Domingo. */
    private Log logger;

    /**
     * Constructor.
     *
     * <p>Creates a monitor with a default log.</p>
     */
    public LoggingMonitor() {
        this(LogFactory.getLog(LoggingMonitor.class));
    }

    /**
     * Constructor.
     *
     * @param log a log instance
     */
    public LoggingMonitor(final Log log) {
        logger = log;
    }

    /**
     * Returns the current log.
     *
     * @return current log
     */
    public Log getLogger() {
        return logger;
    }

    /**
     * Sets a new log.
     *
     * @param log the new log
     */
    public void setLogger(final Log log) {
        this.logger = log;
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#debug(java.lang.String, java.lang.Throwable)
     */
    public void debug(final String message, final Throwable throwable) {
        logger.debug(message, throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#debug(java.lang.String)
     */
    public void debug(final String message) {
        logger.debug(message);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#info(java.lang.String, java.lang.Throwable)
     */
    public void info(final String message, final Throwable throwable) {
        logger.info(message, throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#info(java.lang.String)
     */
    public void info(final String message) {
        logger.info(message);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#warn(java.lang.String, java.lang.Throwable)
     */
    public void warn(final String message, final Throwable throwable) {
        logger.warn(message, throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#warn(java.lang.String)
     */
    public void warn(final String message) {
        logger.warn(message);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#error(java.lang.String, java.lang.Throwable)
     */
    public void error(final String message, final Throwable throwable) {
        logger.error(message, throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#error(java.lang.String)
     */
    public void error(final String message) {
        logger.error(message);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#fatalError(java.lang.String, java.lang.Throwable)
     */
    public void fatalError(final String message, final Throwable throwable) {
        logger.fatal(message, throwable);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#fatalError(java.lang.String)
     */
    public void fatalError(final String message) {
        logger.fatal(message);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isDebugEnabled()
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isInfoEnabled()
     */
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isWarnEnabled()
     */
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isErrorEnabled()
     */
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isFatalErrorEnabled()
     */
    public boolean isFatalErrorEnabled() {
        return logger.isFatalEnabled();
    }
}
