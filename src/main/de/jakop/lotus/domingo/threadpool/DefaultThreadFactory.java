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
package de.jakop.lotus.domingo.threadpool;

import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Default implementation of the <code>ThreadFactory</code> interface.
 */
public final class DefaultThreadFactory implements ThreadFactory {

    /** Reference to the associated monitor. */
    private final DNotesMonitor monitor;

    /**
     * @param theMonitor ThreadPool monitor
     *
     * Default constructor.
     */
    public DefaultThreadFactory(final DNotesMonitor theMonitor) {
        super();
        this.monitor = theMonitor;
    }

    /**
     * {@inheritDoc}
     * @see ThreadFactory#createThread(java.lang.Runnable)
     */
    public Thread createThread(final Runnable target) {
        return new Thread(target);
    }

    /**
     * {@inheritDoc}
     * @see ThreadFactory#initThread()
     */
    public void initThread() {
    }

    /**
     * {@inheritDoc}
     * @see ThreadFactory#termThread()
     */
    public void termThread() {
    }

    /**
     * {@inheritDoc}
     * @see ThreadFactory#handleThrowable(java.lang.Throwable)
     */
    public void handleThrowable(final Throwable throwable) {
        monitor.error(throwable.getMessage(), throwable);
    }
}
