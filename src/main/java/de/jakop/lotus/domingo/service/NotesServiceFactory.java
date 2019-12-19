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

package de.jakop.lotus.domingo.service;

import java.applet.Applet;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.i18n.ResourceManager;
import de.jakop.lotus.domingo.i18n.Resources;
import de.jakop.lotus.domingo.monitor.NullMonitor;
import de.jakop.lotus.domingo.proxy.DNotesThread;
import de.jakop.lotus.domingo.proxy.NotesProxyException;
import de.jakop.lotus.domingo.proxy.NotesProxyFactory;
import de.jakop.lotus.domingo.threadpool.SimpleThreadPool;
import de.jakop.lotus.domingo.threadpool.ThreadFactory;
import de.jakop.lotus.domingo.threadpool.ThreadPool;
import de.jakop.lotus.domingo.threadpool.ThreadPoolException;

/**
 * Factory for sessions to Notes/Domino.
 *
 * <p>Local calls are processed in a thread pool with notes thread registered
 * with a local client installation. Each call to the getSession() method
 * returns the same instance.</p> <p>Corba calls are always processed in the
 * calling thread. Each call to a getSession(...) with arguments returns a new
 * instance, so it is up to the caller to ensure proper disposal of the
 * sessions.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class NotesServiceFactory extends DNotesFactory {

    // //////////////////////////////////////////////
    // constants
    // //////////////////////////////////////////////

    /** Empty array of parameter types for reflection. */
    private static final Class[] PARAMS_EMPTY = new Class[]{};

    /** Array of parameter types for reflection: <code>{ String }</code>. */
    private static final Class[] PARAMS_STRING = { String.class};

    /** Array of parameter types for reflection: <code>{ String, String, String }</code>. */
    private static final Class[] PARAMS_STRING3 = { String.class, String.class, String.class};

    /** Array of parameter types for reflection: <code>{ String, Array, String, String }</code>. */
    private static final Class[] PARAMS_STRING_ARRAY_STRING_STRING = { String.class, Array.class, String.class, String.class};

    /** Array of parameter types for reflection: <code>{ Applet, String, String }</code>. */
    private static final Class[] PARAMS_APPLET_STRING_STRING = new Class[]{ Applet.class, String.class, String.class};

    /** Array of parameter types for reflection: <code>{ Boolean }</code>. */
    private static final Class[] PARAMS_BOOLEAN = new Class[] { Boolean.TYPE };

    /** Array of parameter types for reflection: <code>{ DSession }</code>. */
    private static final Class[] PARAMS_SESSION = new Class[] { DSession.class };

    /** Empty array of arguments for reflection. */
    private static final Object[] ARGS_EMPTY = new Object[]{};

    /** Array of argument values for reflection: <code>{ Boolean.TRUE }</code>. */
    private static final Object[] ARGS_TRUE = { Boolean.TRUE };

    /** Array of argument values for reflection: <code>{  Boolean.FALSE }</code>. */
    private static final Object[] ARGS_FALSE = { Boolean.FALSE};

    /** Default timeout. */
    public static final long DEFAULT_TIMEOUT = 10000;

    /** Default number of threads in thread pool. */
    public static final int DEFAULT_THREADPOOL_SIZE = 1;

    /** Internationalized resources. */
    private static final Resources RESOURCES = ResourceManager.getPackageResources(NotesServiceFactory.class);

    // //////////////////////////////////////////////
    // static attributes
    // //////////////////////////////////////////////

    /** Reference to associated ThreadPool. */
    private ThreadPool threadPool = null;

    // //////////////////////////////////////////////
    // instance attributes
    // //////////////////////////////////////////////

    /** Base Logger instance. */
    private DNotesMonitor monitor;

    /** Associated internal factory. */
    private NotesProxyFactory factory = null;

    /** Associated thread factory. */
    private NotesThreadFactory threadFactory;

    // //////////////////////////////////////////////
    // creation
    // //////////////////////////////////////////////

    /**
     * Default constructor.
     *
     * <p>Must be public to allow abstract factory (the base class) to create
     * an instance of this class.</p>
     *
     * @throws DNotesRuntimeException if the factory cannot be created
     */
    public NotesServiceFactory() throws DNotesRuntimeException {
        try {
            factory = new NotesProxyFactory();
        } catch (NoClassDefFoundError t) {
            throwWrappedException(t);
        }
        setMonitor(NullMonitor.getInstance());
    }

    // //////////////////////////////////////////////
    // interface DNotesFactory
    // //////////////////////////////////////////////

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.lang.Object)
     */
    public DSession getSession(final Object notesSession) throws DNotesRuntimeException {
        throw new NotesServiceRuntimeException("Invalid method call");
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession()
     */
    public DSession getSession() throws DNotesRuntimeException {
        return createSession("getSession", ARGS_EMPTY, PARAMS_EMPTY);
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.lang.String)
     */
    public DSession getSession(final String serverUrl) throws DNotesRuntimeException {
        final Object[] arguments = { serverUrl };
        return createSession("getSession", arguments, PARAMS_STRING);
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public DSession getSession(final String host, final String user, final String passwd) throws DNotesRuntimeException {
        final Object[] arguments = { host, user, passwd };
        return createSession("getSession", arguments, PARAMS_STRING3);
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.lang.String, java.lang.String[], java.lang.String, java.lang.String)
     */
    public DSession getSession(final String host, final String[] args, final String user, final String passwd) throws DNotesRuntimeException {
        final Object[] arguments = { host, args, user, passwd };
        return createSession("getSession", arguments, PARAMS_STRING_ARRAY_STRING_STRING);
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSessionSSL(java.lang.String, java.lang.String, java.lang.String)
     */
    public DSession getSessionSSL(final String host, final String user, final String passwd) throws DNotesRuntimeException {
        final Object[] arguments = { host, user, passwd };
        return createSession("getSession", arguments, PARAMS_STRING3);
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.applet.Applet,
     *      java.lang.String, java.lang.String)
     */
    public DSession getSession(final Applet applet, final String user, final String password) throws DNotesRuntimeException {
        final Object[] args = { applet, user, password };
        return createSession("getSession", args, PARAMS_APPLET_STRING_STRING);
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSessionWithFullAccess()
     */
    public DSession getSessionWithFullAccess() throws DNotesRuntimeException {
        return createSession("getSessionWithFullAccess", ARGS_EMPTY, PARAMS_EMPTY);
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSessionWithFullAccess(java.lang.String)
     */
    public DSession getSessionWithFullAccess(final String password) throws DNotesRuntimeException {
        final Object[] args = { password};
        return createSession("getSessionWithFullAccess", args, PARAMS_STRING);
    }

    /**
     * @see DNotesFactory#gc()
     * @deprecated only use this method for testing
     */
    public void gc() {
        factory.gc();
    }

    /**
     * Creates a new session with the given method and arguments.
     *
     * @param methodName name of method to call on the factory
     * @param args array of arguments
     * @param types array of types
     * @return a session proxy
     */
    private DSession createSession(final String methodName, final Object[] args, final Class[] types) {
        initThreadPool();
        final Object result;
        try {
            result = invoke(factory, DNotesFactory.class.getMethod(methodName, types), args);
        } catch (Throwable t) {
            if (t instanceof NotesServiceRuntimeException) {
                throw (NotesServiceRuntimeException) t;
            } else {
                throw new NotesServiceRuntimeException(t);
            }
        }
        return (DSession) NotesInvocationHandler.getNotesProxy(PARAMS_SESSION, result);
    }

    /**
     * In this multi-threaded implementation the single-threaded dispose method
     * is invoked via a dynamic proxy into the thread pool.
     *
     * @param force indicates if disposal should happen even if still any string
     *            or soft reference exists. if <code>false</code>, only weak
     *            references must remain.
     * @throws DNotesRuntimeException if an error occurs during disposal or if
     *             not all objects can be disposed
     *
     * @see DNotesFactory#dispose()
     * @see DNotesFactory#disposeInternal(boolean)
     * @deprecated use {@link #disposeInstance(boolean)} instead
     */
    public void disposeInternal(final boolean force) throws DNotesRuntimeException {
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#disposeInstance(boolean)
     */
    public void disposeInstance(final boolean force) throws DNotesRuntimeException {
        try {
            final Object[] args = force ? ARGS_TRUE : ARGS_FALSE;
            invoke(factory, DNotesFactory.class.getMethod("disposeInstance", PARAMS_BOOLEAN), args);
        } catch (NoSuchMethodException e) {
            throw new NotesServiceRuntimeException(e.getClass().getName() + ": " + "dispose", e);
        } catch (Throwable e) {
            throw new NotesServiceRuntimeException(e.getMessage(), e);
        }
        threadPool.stop();
        factory = null;
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#disposeInstance()
     */
    public void disposeInstance() throws DNotesRuntimeException {
        disposeInstance(false);
    }

    /**
     * Initializes a notes thread pool to process Notes local calls.
     */
    private void initThreadPool() {
        if (threadPool != null) {
            return;
        }
        final int threadPoolSize = getIntProperty("de.bea.domingo.threadpool.size", DEFAULT_THREADPOOL_SIZE);
        threadFactory = new NotesThreadFactory();
        try {
            threadPool = new SimpleThreadPool(getMonitor(), threadFactory, threadPoolSize);
        } catch (ThreadPoolException e) {
            throwWrappedException(e);
        }
        final Throwable t = threadFactory.getFirstThrowable();
        if (t != null) {
            throwWrappedException(t);
        }
    }

    private void throwWrappedException(final Throwable tt) {
        Throwable t = tt;
        if (t instanceof ThreadPoolException) {
            t = ((ThreadPoolException) tt).getCause();
        }
        if (t instanceof NoClassDefFoundError) {
            if (t.getMessage().indexOf("lotus/domino") >= 0) {
                throw new NotesServiceRuntimeException(RESOURCES.getString("notes.jar.missing"), t);
            }
        } else if (t instanceof UnsatisfiedLinkError) {
            if (t.getMessage().indexOf("java.library.path") >= 0) {
                throw new NotesServiceRuntimeException(RESOURCES.getString("notes.installation.not.found"), t);
            } else if (t.getMessage().indexOf("NGetWrapper") >= 0 || t.getMessage().indexOf("NCreateSession") >= 0) {
                throw new NotesServiceRuntimeException(RESOURCES.getString("notes.installation.not.found"), t);
            }
        }
        throw new NotesServiceRuntimeException(t.getMessage(), t);
    }

    /**
     * Invokes a method within a Thread from the thread pool.
     *
     * @param object the object to invoke the method on
     * @param method the method to invoke
     * @param args the arguments for the method
     * @return result object
     * @throws Throwable if the method cannot be invoked
     */
    Object invoke(final Object object, final Method method, final Object[] args)
            throws Throwable {
        final InvocationTask task = new InvocationTask(object, method, args);
        threadPool.invokeLater(task);
        while (!task.isCompleted()) {
            try {
                synchronized (task) {
                    // We need to check for completeness here inside the
                    // synchronized block again, because on some Operating
                    // systems the task might be completed while waiting for
                    // the monitor for synchronization. In this case we would
                    // every time wait at least once the whole timeout if we
                    // don't check for completeness again.
                    if (!task.isCompleted()) {
                        task.wait(DEFAULT_TIMEOUT);
                    }
                }
            } catch (InterruptedException e) {
                continue;
            }
        }
        final Throwable t = task.getThrowable();
        if (t != null) {
            if (t instanceof NotesServiceRuntimeException) {
                throw t;
            } else if (t instanceof RuntimeException) {
                throw new NotesServiceRuntimeException(t.getMessage(), t);
            } else if (t instanceof NotesProxyException) {
                throw new NotesServiceException(t.getMessage(), t);
            } else {
                throw new NotesServiceException("NotesServiceFactory.invoke() " + t.getMessage(), t);
            }
        }
        return task.getResult();
    }

    /**
     * @see DNotesFactory#sinitThread()
     */
    public void sinitThread() {
        factory.sinitThread();
    }

    /**
     * @see DNotesFactory#stermThread()
     */
    public void stermThread() {
        factory.stermThread();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getMonitor()
     */
    public DNotesMonitor getMonitor() {
        return monitor;
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#setMonitor(DNotesMonitor)
     */
    public void setMonitor(final DNotesMonitor theMonitor) {
        monitor = theMonitor;
        factory.setMonitor(monitor);
    }

    // //////////////////////////////////////////////
    // inner classes
    // //////////////////////////////////////////////

    /**
     * Thread factory for notes enabled threads.
     */
    private final class NotesThreadFactory implements ThreadFactory {

        /** For auto-numbering anonymous threads. */
        private int threadInitNumber = 0;

        /** The first throwable that occurs in a thread. */
        private Throwable firstThrowable = null;

        /** The last throwable that occurs in a thread. */
        private Throwable lastThrowable;

        /**
         * Private constructor.
         */
        private NotesThreadFactory() {
        }

        /**
         * @see ThreadFactory#createThread(java.lang.Runnable)
         */
        public Thread createThread(final Runnable target) {
            DNotesThread domingoThread = new DNotesThread(target, "Domingo Thread " + nextThreadNum());
            domingoThread.setMonitor(monitor);
            return domingoThread;
        }

        /**
         * @see ThreadFactory#initThread()
         */
        public void initThread() {
            sinitThread();
        }

        /**
         * @see ThreadFactory#termThread()
         */
        public void termThread() {
            stermThread();
        }

        /**
         * Returns the next free number for a thread.
         *
         * @return next free number for a thread
         */
        private synchronized int nextThreadNum() {
            return threadInitNumber++;
        }

        /**
         * Remember the first throwable that occurs, ignore further throwables.
         * This should be enough for further processing, because the first
         * throwable mostly is the main cause, and always the first thing to
         * analyze.
         *
         * @see ThreadFactory#handleThrowable(java.lang.Throwable)
         */
        public void handleThrowable(final Throwable t) {
            if (firstThrowable == null) {
                firstThrowable = t;
            }
            lastThrowable = t;
        }

        /**
         * Returns the first throwable that has occurred so far.
         *
         * @return first throwable that has occurred
         */
        protected Throwable getFirstThrowable() {
            return firstThrowable;
        }

        /**
         * Returns the last throwable that has occurred so far.
         *
         * @return last throwable that has occurred
         */
        protected Throwable getLastThrowable() {
            return lastThrowable;
        }
    }
}
