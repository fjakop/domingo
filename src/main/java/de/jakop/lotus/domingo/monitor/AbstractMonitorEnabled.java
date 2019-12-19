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
 * Base class for all monitor enabled classes.
 *
 * <p>As default, the associated monitor is a <code>NullMonitor</code> that
 * simply does nothing. To monitor events the application should either set a
 * ConsoleMonitor or implement itself the <code>Monitor</code> interface set an
 * instance of this class with the <code>setMonitor</code> method.</p>
 *
 * @see DNotesMonitor
 * @see NullMonitor
 * @see ConsoleMonitor
 * @see MonitorEnabled#setMonitor(DNotesMonitor)
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class AbstractMonitorEnabled implements MonitorEnabled {

    /** Default monitor. */
    private static final DNotesMonitor DEFAULT_MONITOR = NullMonitor.getInstance();

    /** Reference to current monitor. */
    private DNotesMonitor monitor = DEFAULT_MONITOR;

    /**
     * Constructor.
     */
    public AbstractMonitorEnabled() {
        super();
    }

    /**
     * Constructor.
     *
     * @param theMonitor the monitor
     */
    public AbstractMonitorEnabled(final DNotesMonitor theMonitor) {
        super();
        setMonitor(theMonitor);
    }

    /**
     * {@inheritDoc}
     * @see MonitorEnabled#getMonitor()
     */
    public final DNotesMonitor getMonitor() {
        return monitor;
    }

    /**
     * {@inheritDoc}
     * @see MonitorEnabled#setMonitor(DNotesMonitor)
     */
    public final void setMonitor(final DNotesMonitor theMonitor) {
        if (monitor != null) {
            this.monitor = theMonitor;
        }
    }
}
