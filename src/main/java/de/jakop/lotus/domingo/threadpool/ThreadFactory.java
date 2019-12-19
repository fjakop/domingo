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

/**
 * Interface for classes generating threads.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface ThreadFactory {

    /**
     * Factory method to create Thread objects.
     *
     * <p>The resulting object must not be of class <code>Thread</code>, but can
     * also be of any Class derived from class <code>Thread</code>.</p>
     *
     * @param target the object whose run() method gets called
     * @return a Thread
     */
    Thread createThread(Runnable target);

    /**
     * Initialize resources of a new thread.
     */
    void initThread();

    /**
     * Free resources of a new thread.
     */
    void termThread();

    /**
     * Callback method if an Throwable occurs during starting a thread.
     *
     * <p>A component that uses a thread factory (e.g. a thread pool) can call this method
     * to notify the thread factory about problems when creating a thread.</p>
     *
     * @param throwable a throwable that occurred during starting a thread
     */
    void handleThrowable(Throwable throwable);
}
