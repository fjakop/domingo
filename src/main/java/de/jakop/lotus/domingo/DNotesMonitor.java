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

package de.jakop.lotus.domingo;

/**
 * Interface to monitor notes threads.
 *
 * <p>Applications that use the domingo API can write an Adapter
 * to the application specific logging or can use one of the build-in
 * implementations of this interface.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DNotesMonitor {

    /** constant to indicate debug messages. */
    int DEBUG = 50;

    /** constant to indicate info messages. */
    int INFO  = 40;

    /** constant to indicate warn messages. */
    int WARN  = 30;

    /** constant to indicate error messages. */
    int ERROR = 20;

    /** constant to indicate fatal error messages. */
    int FATAL = 10;

    /** constant to indicate fatal error messages. */
    int NONE = 0;

    /** default monitoring level. */
    int DEFAULT_LEVEL = WARN;

    /**
     * debug output.
     *
     * @param message the message
     */
    void debug(String message);

    /**
     * debug output.
     *
     * @param message the message
     * @param throwable the throwable
     */
    void debug(String message, Throwable throwable);

    /**
     * Checks if debug output is enabled or not.
     *
     * @return <code>true</code>if enabled, else <code>false</code>
     */
    boolean isDebugEnabled();

    /**
     * info output.
     *
     * @param message the message
     */
    void info(String message);

    /**
     * info output.
     *
     * @param message the message
     * @param throwable the throwable
     */
    void info(String message, Throwable throwable);

    /**
     * Checks if info output is enabled or not.
     *
     * @return <code>true</code>if enabled, else <code>false</code>
     */
    boolean isInfoEnabled();

    /**
     * warn output.
     *
     * @param message the message
     */
    void warn(String message);

    /**
     * warn output.
     *
     * @param message the message
     * @param throwable the throwable
     */
    void warn(String message, Throwable throwable);

    /**
     * Checks if warn output is enabled or not.
     *
     * @return <code>true</code>if enabled, else <code>false</code>
     */
    boolean isWarnEnabled();

    /**
     * error output.
     *
     * @param message the message
     */
    void error(String message);

    /**
     * error output.
     *
     * @param message the message
     * @param throwable the throwable
     */
    void error(String message, Throwable throwable);

    /**
     * Checks if error output is enabled or not.
     *
     * @return <code>true</code>if enabled, else <code>false</code>
     */
    boolean isErrorEnabled();

    /**
     * fatal error output.
     *
     * @param message the message
     */
    void fatalError(String message);

    /**
     * fatal error output.
     *
     * @param message the message
     * @param throwable the throwable
     */
    void fatalError(String message, Throwable throwable);

    /**
     * Checks if fatal error output is enabled or not.
     *
     * @return <code>true</code>if enabled, else <code>false</code>
     */
    boolean isFatalErrorEnabled();
}
