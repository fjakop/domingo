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
import de.jakop.lotus.domingo.i18n.ResourceManager;
import de.jakop.lotus.domingo.i18n.Resources;
import de.jakop.lotus.domingo.monitor.ConsoleMonitor;
import de.jakop.lotus.domingo.proxy.DNotesThread;
import de.jakop.lotus.domingo.queue.MTQueue;

/**
 * A simple implementation of a ThreadPool which is constructed with a given
 * number of threads.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class SimpleThreadPool implements Runnable, ThreadPool {

    /** Maximum amount of time to wait for a task from the queue in milli seconds. */
    public static final int MAX_WAIT_FOR_TASK = 10000;

    /** Maximum amount of time to wait for a thread to stop. */
    public static final int MAX_WAIT_FOR_STOP = 5000;

    /** Default number of threads in the thread pool. */
    public static final int DEFAULT_NUM_THREAD = 1;

    /** Internationalized resources. */
    private static final Resources RESOURCES = ResourceManager.getPackageResources(SimpleThreadPool.class);

    /** Reference to the associated queue. */
    private final MTQueue queue;

    /** Indicates if the ThreadPool has been stopped. */
    private boolean stopped = false;

    /** Number of threads that should be running. */
    private int size = 0;

    /** Number of threads currently running. */
    private int threadCount = 0;

    /** Priority of threads in the pool. */
    private int threadPriority = 0;

    /** Reference to the associated monitor. */
    private final DNotesMonitor monitor;

    /** Reference to the thread factory. */
    private ThreadFactory threadFactory;

    /** First exception during initialization. */
    private Throwable initException;

    private boolean initialized;

    private Object mutex = new Object();

    /**
     * Constructor.
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    public SimpleThreadPool() throws ThreadPoolException {
        this(null, DEFAULT_NUM_THREAD);
    }

    /**
     * Constructor.
     *
     * @param monitor the monitor
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    public SimpleThreadPool(final DNotesMonitor monitor) throws ThreadPoolException {
        this(monitor, DEFAULT_NUM_THREAD);
    }

    /**
     * Constructor.
     *
     * @param numberOfThreads number of threads in pool
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    public SimpleThreadPool(final int numberOfThreads) throws ThreadPoolException {
        this(1, numberOfThreads);
    }

    /**
     * Constructor.
     *
     * @param monitor ThreadPool Monitor
     * @param numberOfThreads number of threads in pool
     * @throws ThreadPoolException if any error occurs during starting threads
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    public SimpleThreadPool(final DNotesMonitor monitor, final int numberOfThreads) throws ThreadPoolException {
        this(monitor, numberOfThreads, Thread.NORM_PRIORITY);
    }

    /**
     * Constructor.
     *
     * @param numberOfThreads number of threads in pool
     * @param threadPriority priority of threads in pool
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    public SimpleThreadPool(final int numberOfThreads, final int threadPriority) throws ThreadPoolException {
        this(new ConsoleMonitor(), numberOfThreads, threadPriority);
    }

    /**
     * Constructor.
     *
     * @param monitor ThreadPool Monitor
     * @param numberOfThreads number of threads in pool
     * @param threadPriority priority of threads in pool
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    public SimpleThreadPool(final DNotesMonitor monitor, final int numberOfThreads,
                            final int threadPriority) throws ThreadPoolException {
        this(monitor, null, numberOfThreads, threadPriority);
    }

    /**
     * Constructor.
     *
     * @param monitor ThreadPool Monitor
     * @param threadFactory ThreadFactory to us to create the new Threads
     * @param numberOfThreads number of threads in pool
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    public SimpleThreadPool(final DNotesMonitor monitor, final ThreadFactory threadFactory,
                            final int numberOfThreads) throws ThreadPoolException {
        this(monitor, threadFactory, numberOfThreads, Thread.NORM_PRIORITY);
    }

    /**
     * Constructor.
     *
     * @param theMonitor ThreadPool monitor
     * @param theThreadFactory ThreadFactory to us to create the new Threads
     * @param theNumberOfThreads number of threads in pool
     * @param theThreadPriority priority of threads in pool
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    public SimpleThreadPool(final DNotesMonitor theMonitor, final ThreadFactory theThreadFactory,
                            final int theNumberOfThreads, final int theThreadPriority) throws ThreadPoolException {
        threadFactory = theThreadFactory;
        size = theNumberOfThreads;
        threadPriority = theThreadPriority;
        if (theMonitor != null) {
            monitor = theMonitor;
        } else {
            monitor = new ConsoleMonitor();
        }
        if (theThreadFactory != null) {
            threadFactory = theThreadFactory;
        } else {
            threadFactory = new DefaultThreadFactory(theMonitor);
        }
        queue = new MTQueue(mutex, monitor);
        initThreads(theNumberOfThreads, threadPriority);
    }


    ////////////////////////////////////////////////
    //    private helper methods
    ////////////////////////////////////////////////

    /**
     * Initialize a number of threads in the pool.
     *
     * @param count number of threads in pool
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    private void initThreads(final int count) throws ThreadPoolException {
        initThreads(count, Thread.NORM_PRIORITY);
    }

    /**
     * Initialize a number of threads in the pool with given priority.
     *
     * @param count number of threads in pool
     * @param priority priority of threads in pool
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    private void initThreads(final int count, final int priority) throws ThreadPoolException {
        for (int i = 0; i < count; i++) {
            startThread(priority);
        }
    }

    /**
     * Create and start a new thread.
     *
     * @param priority priority of new thread
     * @return new Thread
     * @throws ThreadPoolException if any error occurs during starting threads
     */
    private Thread startThread(final int priority) throws ThreadPoolException {
        initialized = false;
        initException = null;
        final DNotesThread thread = (DNotesThread) threadFactory.createThread(this);
        if (priority != Thread.NORM_PRIORITY) {
            thread.setPriority(priority);
        }
        try {
            thread.start();
        } catch (Throwable t) {
            throw new ThreadPoolException("Thread cannot be started", t);
        }
        synchronized (mutex) {
            while (!isInitialized() && !isFailed()) {
                try {
                    mutex.wait();
                } catch (InterruptedException e) {
                    // ignore the interrupt exception an continue
                }
            }
        }
        if (isFailed()) {
            throw new ThreadPoolException("Cannot start thread", initException);
        }
        return thread;
    }

    private boolean isInitialized() {
        synchronized (mutex) {
            return initialized;
        }
    }

    private boolean isFailed() {
        synchronized (mutex) {
            return initException != null;
        }
    }

    ////////////////////////////////////////////////
    //    public methods
    ////////////////////////////////////////////////

    /**
     * Returns the number of currently active threads.
     *
     * @return number of currently active threads
     */
    private int getThreadCount() {
        return threadCount;
    }

    /**
     * Returns number of runnable objects in the queue.
     * @return number of objects in the queue
     */
    public int getRunnableCount() {
        return queue.size();
    }

    ////////////////////////////////////////////////
    //    interface ThreadPool
    ////////////////////////////////////////////////

    /**
     * Dispatch a new task onto this pool to be invoked asynchronously later.
     *
     * @param task the task to execute
     */
    public void invokeLater(final Runnable task) {
        if (stopped) {
            throw new IllegalStateException(RESOURCES.getString("threadpool.not.started"));
        }
        queue.enqueue(task);
    }

    /**
     * @see ThreadPool#stop()
     */
    public void stop() {
        synchronized (mutex) {
            stopped = true;
            synchronized (queue) {
                mutex.notifyAll();
            }
            while (getThreadCount() > 0) {
                try {
                    mutex.wait(MAX_WAIT_FOR_STOP);
                } catch (InterruptedException e) {
                    monitor.debug(RESOURCES.getString("threadpool.wait.stop"));
                }
            }
            monitor.info(RESOURCES.getString("threadpool.stopped"));
        }
    }

    /**
     * {@inheritDoc}
     * @see ThreadPool#resize(int)
     */
    public void resize(final int newSize) throws ThreadPoolException {
        synchronized (mutex) {
            size = newSize;
            if (size > threadCount) {
                initThreads(size - threadCount);
            } else if (size < threadCount) {
                while (threadCount > size) {
                    try {
                        mutex.wait(MAX_WAIT_FOR_STOP);
                    } catch (InterruptedException e) {
                        monitor.debug(RESOURCES.getString("threadpool.wait.resize"));
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////
    //    interface Runnable
    ////////////////////////////////////////////////

    /**
     * The method ran by the pool of background threads.
     */
    public void run() {
        monitor.info(RESOURCES.getString("thread.started"));
        try {
            threadFactory.initThread();
        } catch (Throwable t) {
            synchronized (mutex) {
                initException = t;
                mutex.notifyAll();
            }
            return;
        }
        synchronized (mutex) {
            initialized = true;
            threadCount++;
            mutex.notifyAll();
        }
        while (!stopped) {
            synchronized (mutex) {
                if (threadCount > size) {
                    break;
                }
            }
            final Runnable task = (Runnable) queue.dequeue(MAX_WAIT_FOR_TASK);
            if (task != null) {
                try {
                    task.run();
                } catch (Throwable t) {
                    monitor.fatalError(RESOURCES.getString("task.execute.failed"), t);
                }
                synchronized (task) {
                    task.notifyAll();
                }
            }
        }
        synchronized (mutex) {
            threadCount--;
        }
        try {
            threadFactory.termThread();
        } catch (Throwable t) {
            threadFactory.handleThrowable(t);
        }
        monitor.info(RESOURCES.getString("thread.stopped"));
        synchronized (mutex) {
            mutex.notifyAll();
        }
    }
}
