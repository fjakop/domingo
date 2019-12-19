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

package de.jakop.lotus.domingo.monitor;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Adapter from Apache Log4j to the domingo monitor interface.
 *
 * <p>Use an instance of this class as argument to
 * DNotesFactory.getInstance(DNotesMonitor theMonitor).</p>
 *
 * <p>If Log4j should be used for logging, the log4j.jar file must be available
 * in the classpath. Log4h can be downloaded at
 * <a target="_blank" href="http://logging.apache.org/log4j/"></a>.</p>
 *
 * @since domingo 1.1
 */
public final class Log4jMonitor implements DNotesMonitor {

    /** Reference to the log4j log that will receive all log messages from Domingo. */
    private Logger logger;

    /**
     * Constructor.
     *
     * <p>Creates a monitor with a default Logj log.</p>
     */
    public Log4jMonitor() {
        this(Logger.getLogger(Log4jMonitor.class));
    }

    /**
     * Constructor.
     *
     * @param log a Log4j log instance
     */
    public Log4jMonitor(final Logger log) {
        logger = log;
    }

    /**
     * Returns the current Log4j log.
     *
     * @return current Log4j log
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Sets a new Log4j log.
     *
     * @param log the new Log4j log
     */
    public void setLogger(final Logger log) {
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
        return logger.isEnabledFor(Priority.DEBUG);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isInfoEnabled()
     */
    public boolean isInfoEnabled() {
        return logger.isEnabledFor(Priority.INFO);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isWarnEnabled()
     */
    public boolean isWarnEnabled() {
        return logger.isEnabledFor(Priority.WARN);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isErrorEnabled()
     */
    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Priority.ERROR);
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isFatalErrorEnabled()
     */
    public boolean isFatalErrorEnabled() {
        return logger.isEnabledFor(Priority.FATAL);
    }
}
