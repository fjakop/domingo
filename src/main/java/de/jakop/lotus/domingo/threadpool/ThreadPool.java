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

package de.jakop.lotus.domingo.threadpool;

/**
 * An interface representing a resizable thread pool which allows
 * asynchronous dispatching of Runnable tasks. It is the responsibility
 * of the Runnable task to handle exceptions gracefully. Any non handled
 * exception will typically just be logged.
 * Though a ThreadPool implementation could have some custom Exception handler
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public interface ThreadPool {

    /**
     * Dispatch a new task onto this pool
     * to be invoked asynchronously later.
     *
     * @param task the task to invoke
     */
    void invokeLater(Runnable task);

    /**
     * Stops the pool.
     */
    void stop();

    /**
     * Resize the thread pool.
     *
     * @param newSize new number of threads in the thread pool
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    void resize(int newSize) throws ThreadPoolException;
}
