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

import java.util.HashMap;
import java.util.Map;

import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Abstract base class for implementations of the Monitor interface.
 *
 * <p>This class handles the level of monitors.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class AbstractMonitor implements DNotesMonitor {

    /**
     * Map of all string representing valid levels to their corresponding integer value.
     *
     * <p>The standard values are represented by the following strings (not case sensitive):
     * <code>"{@link #DEBUG}"</code>,
     * <code>"{@link #INFO}"</code>,
     * <code>"{@link #WARN}"</code>,
     * <code>"{@link #ERROR}"</code>,
     * <code>"{@link #FATAL}"</code> and
     * <code>"{@link #NONE}"</code>.</p>
     *
     * <p>For convenience, also the following values are synonyms:</p>
     * <pre>"TRACE"       -> DEBUG
     *"INFORMATION" -> INFO
     *"WARNING"     -> WARN
     *"FATALERROR"  -> FATAL
     *"NOTHING"     -> NONE
     *"DEFAULT"     -> WARN</pre>
     */
    public static final Map LEVELS = new HashMap();

    static {
        LEVELS.put("TRACE", new Integer(DEBUG));
        LEVELS.put("DEBUG", new Integer(DEBUG));
        LEVELS.put("INFO", new Integer(INFO));
        LEVELS.put("INFORMATION", new Integer(INFO));
        LEVELS.put("WARN", new Integer(WARN));
        LEVELS.put("WARNING", new Integer(WARN));
        LEVELS.put("ERROR", new Integer(ERROR));
        LEVELS.put("FATAL", new Integer(FATAL));
        LEVELS.put("FATALERROR", new Integer(FATAL));
        LEVELS.put("NONE", new Integer(NONE));
        LEVELS.put("NOTHING", new Integer(NONE));
        LEVELS.put("DEFAULT", new Integer(WARN));
    }

    /** Current monitoring level. */
    private int level = DEFAULT_LEVEL;

    /**
     * Constructor.
     */
    public AbstractMonitor() {
        final String levelString = DNotesFactory.getProperty("de.bea.domingo.monitor.level", "INFO");
        level = getLevel(levelString);
    }

    /**
     * Constructor.
     *
     * @param theLevel the level of the new monitor
     */
    public AbstractMonitor(final int theLevel) {
        level = theLevel;
    }

    /**
     * Sets the monitoring level of the monitor.
     *
     * <p>See {@link #LEVELS} for allowed values.</p>
     * {@link #DEBUG}
     * @param theLevel the new level
     */
    public final void setLevel(final int theLevel) {
        this.level = theLevel;
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isFatalErrorEnabled()
     */
    public final boolean isFatalErrorEnabled() {
        return level >= FATAL;
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isErrorEnabled()
     */
    public final boolean isErrorEnabled() {
        return level >= ERROR;
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isWarnEnabled()
     */
    public final boolean isWarnEnabled() {
        return level >= WARN;
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isInfoEnabled()
     */
    public final boolean isInfoEnabled() {
        return level >= INFO;
    }

    /**
     * {@inheritDoc}
     * @see DNotesMonitor#isDebugEnabled()
     */
    public final boolean isDebugEnabled() {
        return level >= DEBUG;
    }

    /**
     * Checks if the monitor level is set to <code>NONE</code>.
     *
     *  @return <code>true</code> if the monitor level is set to <code>NONE</code>, else <code>false</code>
     */
    public final boolean isNoMesssages() {
        return level == NONE;
    }

    /**
     * Given a string, returns the monitoring level as integer.
     *
     * <p>See {@link #LEVELS} for allowed values.</p>
     *
     * @param levelString the level as a string
     * @return the level as an integer
     */
    private int getLevel(final String levelString) {
        if (levelString == null) {
            return DEFAULT_LEVEL;
        }
        Integer integer = (Integer) LEVELS.get(levelString.toUpperCase());
        return integer == null ? DEFAULT_LEVEL : integer.intValue();
    }
}
