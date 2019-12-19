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

package de.jakop.lotus.domingo.queue;

import java.util.LinkedList;

import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.monitor.ConsoleMonitor;

/**
 * A multithreaded blocking queue which is very useful for
 * implementing producer-consumer style threading patterns.
 * <p>
 * Multiple blocking threads can wait for items being added
 * to the queue while other threads add to the queue.
 * <p>
 * Non blocking and timeout based modes of access are possible as well.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class MTQueue implements Queue {

    /** Default amount of time to wait for a task from the queue in milli seconds. */
    private static final long DEFAULT_TIMEOUT = 1000;

    /** List of queued objects. */
    private LinkedList list = new LinkedList();

    /** Reference to the associated monitor. */
    private final DNotesMonitor monitor;

    private Object fMutex = new Object();

    /**
     * Constructor.
     *
     * @param mutex a mutex for syncronization
     * @param theMonitor the monitor
     */
    public MTQueue(final Object mutex, final DNotesMonitor theMonitor) {
        fMutex = mutex;
        if (theMonitor != null) {
            monitor = theMonitor;
        } else {
            monitor = new ConsoleMonitor();
        }
    }

    /**
     * {@inheritDoc}
     * @see Queue#size()
     */
    public int size() {
        synchronized (fMutex) {
            return list.size();
        }
    }

    /**
     * {@inheritDoc}
     * @see Queue#isEmpty()
     */
    public boolean isEmpty() {
        synchronized (fMutex) {
            return size() == 0;
        }
    }
    /**
     * Adds a new object to the end of the queue.
     * At least one thread will be notified.
     * @param object the object to add to the queue
     */
    public void enqueue(final Object object) {
        synchronized (fMutex) {
            list.add(object);
            fMutex.notifyAll();
        }
    }

    /**
     * Removes the first object from the queue, blocking until one is available.
     * Note that this method will never return <code>null</code> and could block forever.
     *
     * @return next object from the queue
     */
    public Object dequeue() {
        synchronized (fMutex) {
            while (true) {
                final Object answer = dequeueNoWait();
                if (answer != null) {
                    return answer;
                }
                try {
                    fMutex.wait(DEFAULT_TIMEOUT);
                } catch (InterruptedException e) {
                    monitor.fatalError(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Removes the first object from the queue, blocking only up to the given
     * timeout time.
     *
     * A thread can also wake up without being notified, interrupted, or
     * timing out, a so-called <i>spurious wakeup</i>.  This will rarely
     * occur in practice, but it can occur and in such a case, this method
     * will not wait upto the timeout, but come back earlier without a
     * dequeued object.
     *
     * @param timeout maximum time to wait for an object from the queue
     * @return next object from the queue
     */
    public Object dequeue(final long timeout) {
        synchronized (fMutex) {
            Object answer = dequeueNoWait();
            if (answer == null) {
                try {
                    fMutex.wait(timeout);
                } catch (InterruptedException e) {
                    monitor.fatalError(e.getMessage(), e);
                }
                answer = dequeueNoWait();
            }
            return answer;
        }
    }

    /**
     * Removes the first object from the queue without blocking.
     * This method will return immediately with an item from the queue or <code>null</code>.
     *
     * @return the first object removed from the queue or null if the
     * queue is empty
     */
    public Object dequeueNoWait() {
        synchronized (fMutex) {
            if (!list.isEmpty()) {
                return list.removeFirst();
            }
            return null;
        }
    }
}
