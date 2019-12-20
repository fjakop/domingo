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

package de.jakop.lotus.domingo.proxy;

import java.applet.Applet;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.jakop.lotus.domingo.exception.DominoException;
import de.jakop.lotus.domingo.monitor.MonitorEnabled;
import de.jakop.lotus.domingo.monitor.NullMonitor;
import lotus.domino.NotesError;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.cache.Cache;
import de.jakop.lotus.domingo.cache.WeakCache;
import de.jakop.lotus.domingo.i18n.ResourceManager;
import de.jakop.lotus.domingo.i18n.Resources;

/**
 * Factory for sessions to Notes/Domino.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class NotesProxyFactory extends DNotesFactory implements MonitorEnabled {

    ////////////////////////////////////////////////
    // constants
    ////////////////////////////////////////////////

    /** Retry count while waiting for disposal. */
    public static final int MAX_DISPOSE_TRIES = 10;

    /** Time to wait for garbage collector [milliseconds]. */
    public static final int TIME_WAIT_FOR_GC = 100;

    /** Threshold size for weak cache. */
    public static final int DEFAULT_CACHE_THRESHOLD = 2000;

    /** Key for map of default IIOP session. */
    public static final String DEFAULT_IIOP_SESSION_KEY = "defaultIIOPSession";

    /** Internationalized resources. */
    private static final Resources RESOURCES = ResourceManager.getPackageResources(NotesProxyFactory.class);

    ////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////

    /** Associated internal session (local call only). */
    private DSession fInternalSession;

    /** Map of all IIOP session (one session for each host and username). */
    private Map fIiopSessions = new HashMap();

    /** Base Monitor instance. */
    private DNotesMonitor fMonitor = null;

    /** Reference to recycle strategy implementation. */
    private NotesRecycler fRecycler = null;

    /** Central weak cache for all Notes Proxy classes. */
    private Cache fBaseCache = new WeakCache();

    /** Threshold size for weak cache. */
    private int fCacheThreshold = DEFAULT_CACHE_THRESHOLD;

    ////////////////////////////////////////////////
    // creation
    ////////////////////////////////////////////////

    /**
     * Default constructor.
     *
     * <p>Must be public to allow abstract factory (the base class) to create
     * an instance of this class.</p>
     */
    public NotesProxyFactory() {
        setMonitor(NullMonitor.getInstance());
        fCacheThreshold = DNotesFactory.getIntProperty("de.jakop.lotus.domingo.cache.threshold", DEFAULT_CACHE_THRESHOLD);
    }

    /**
     * Package-private constructor to create a factory from within the Lotus Notes VM with
     * restricted security.
     *
     * @param threshold Threshold size for weak cache
     */
    public NotesProxyFactory(final int threshold) {
        setMonitor(NullMonitor.getInstance());
        if (threshold > 0) {
            fCacheThreshold = threshold;
        } else {
            fCacheThreshold = DEFAULT_CACHE_THRESHOLD;
        }
    }

    /**
     * Shutdown thread of Domingo, disposes all resources.
     *
     * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
     */
    public final class DNotesShutdownThread extends Thread {

        /**
         * Tries to disposes all related resources.
         *
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                getMonitor().debug(RESOURCES.getString("shutdownhook.starting"));
                getMonitor().debug("baseCache.size = " + getBaseCache().size());
                DNotesFactory.dispose();
                getMonitor().debug(RESOURCES.getString("shutdownhook.finished"));
            } catch (DNotesRuntimeException e) {
                getMonitor().warn(e.getLocalizedMessage(), e);
            }
            super.run();
        }
    }

    /**
     * Chooses a recycling strategy based on the Notes Client Release.
     *
     * Currently we think that even with R6 and later, the
     * {@link RecycleStrategy} is the best choice and we don't use the
     * {@link NoRecycleStrategy}. Especially the indeterministic caching of
     * documents prohibits the {@link NoRecycleStrategy}.
     *
     * @param session the Notes Session
     */
    private void setRecycler(final Session session) {
        fRecycler = new RecycleStrategy(getMonitor());
    }

    /**
     * Returns the central weak cache of all notes objects.
     *
     * @return central weak cache of all notes objects
     */
    public Cache getBaseCache() {
        return fBaseCache;
    }

    /**
     * @see DNotesFactory#gc()
     * @deprecated only use this method for testing
     */
    public void gc() {
        preprocessMethod();
    }

    /**
     * <p>In this single threaded implementation, first the own reference
     * to the session is nulled and then we wait for the weak cache to
     * be finalized.
     *
     * @param force indicates if disposal should happen even if still any
     *              strong or soft reference exists. if <code>false</code>,
     *              only weak references must remain.
     *
     * @see DNotesFactory#dispose()
     * @see DNotesFactory#dispose(boolean)
     * @see java.lang.ref.WeakReference
     * @deprecated use {@link #disposeInternal(boolean)} instead
     */
    public void disposeInternal(final boolean force) {
        disposeInstance(force);
    }

    /**
     * <p>In this single threaded implementation, first the own reference
     * to the session is nulled and then we wait for the weak cache to
     * be finalized.
     *
     * @see DNotesFactory#dispose()
     * @see DNotesFactory#dispose(boolean)
     * @see java.lang.ref.WeakReference
     */
    public void disposeInstance() {
        disposeInstance(false);
    }

    /**
     * <p>In this single threaded implementation, first the own reference
     * to the session is nulled and then we wait for the weak cache to
     * be finalized.
     *
     * <p>Equivalent to
     * <code>{link {@link #disposeInstance(boolean) disposeInstance(false)}</code></p>.
     *
     * @param force indicates if disposal should happen even if still any
     *              strong or soft reference exists. if <code>false</code>,
     *              only weak references must remain.
     *
     * @see DNotesFactory#dispose()
     * @see DNotesFactory#dispose(boolean)
     * @see java.lang.ref.WeakReference
     */
    public void disposeInstance(final boolean force) {
        System.gc();
        recycleQueue();
        fInternalSession = null;
        if (force) {
            disposeStrong();
        } else {
            disposeWeak();
        }
        if (fBaseCache.size() > 0) {
            final int k = logUndisposedObjects();
            if (k > 1) {
                getMonitor().warn("There are still " + k + " undisposed objects in the object cache:");
            } else {
                getMonitor().warn("There is still one undisposed object in the object cache:");
            }
            if (k > 0) {
                getMonitor().warn("Unable to dispose all objects.");
            } else {
                getMonitor().info("All objects in object cache disposed successfully.");
            }
        }
        if (getMonitor().isDebugEnabled()) {
            getMonitor().debug("DateTime counter is " + BaseProxy.getCountDateTime());
        }
    }

    /**
     * Disposes all objects that have only weak references.
     * All objects that still have strong references, are not disposed.
     */
    private void disposeWeak() {
        int tries = 0;
        while (fBaseCache.size() > 0 && tries < MAX_DISPOSE_TRIES) {
            tries++;
            getMonitor().debug("Waiting for notes disposal. (" + fBaseCache.size() + " objects in queue)");
            System.gc();
            recycleQueue();
            sleep(TIME_WAIT_FOR_GC);
        }
    }

    /**
     * Disposes all objects even if there are still strong references left.
     * All objects that still have strong references, are not valid anymore afterwards.
     */
    private void disposeStrong() {
        Collection values = fBaseCache.values();
        while (values.size() > 0) {
            try {
                final Iterator objects = values.iterator();
                while (objects.hasNext()) {
                    Object obj = objects.next();
                    if (obj != null) {
                        if (obj instanceof WeakReference) {
                            obj = ((WeakReference) obj).get();
                        }
                        if (obj != null) {
                            recycleLater(obj);
                        }
                    }
                }
                recycleQueue();
                fBaseCache.clear();
            } catch (ConcurrentModificationException e) {
                getMonitor().debug("Concurrent modification in disposeStrong(); retry recycle");
            }
        }
    }

    /**
     * Logs and counts all undisposed objects to the current monitor.
     * @return number of undisposed objects
     */
    private int logUndisposedObjects() {
        final Iterator iterator = fBaseCache.values().iterator();
        int i = 0;
        int k = 0;
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            i++;
            if (obj != null) {
                if (obj instanceof WeakReference) {
                    obj = ((WeakReference) obj).get();
                }
                if (obj != null) {
                    k = k + 1;
                    getMonitor().warn("undisposed object " + k + ": " + obj.getClass().getName() + "." + obj.toString());
                }
            }
        }
        return k;
    }

    /**
     * Sleep some milliseconds unless interrupted.
     *
     * @param millis time to sleep in milliseconds
     */
    private synchronized void sleep(final int millis) {
        try {
            wait(millis);
        } catch (InterruptedException e) {
            return;
        }
    }

    /**
     * Recycles resources associated with an object.
     *
     * <p>Static delegation method to associated recycle strategy.</p>
     *
     * @param object the object to recycle
     *
     * @see NotesRecycler#recycle(java.lang.Object)
     */
    void recycle(final Object object) {
        if (fRecycler != null) {
            fRecycler.recycle(object);
        }
    }

    /**
     * @see NotesRecycler#recycleLater(java.lang.Object)
     */
    void recycleLater(final Object object) {
        if (fRecycler != null) {
            fRecycler.recycleLater(object);
        }
    }

    /**
     * @see NotesRecycler#recycleQueue()
     */
    void recycleQueue() {
        if (fRecycler != null) {
            fRecycler.recycleQueue();
        }
    }

    ////////////////////////////////////////////////
    //    interface DNotesFactory
    ////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSession()
     */
    public DSession getSession() throws DNotesRuntimeException {
        if (fInternalSession == null) {
            try {
                preprocessMethod();
                final Session session = NotesFactory.createSession();
                fInternalSession = SessionProxy.getInstance(this, session, getMonitor());
                setRecycler(session);
                logSsession(session);
                handleShutdownHook();
            } catch (NotesException e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("cannot.create.session"), new DominoException(e));
            } catch (UnsatisfiedLinkError e) {
                if (e.getMessage().indexOf("java.library.path") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("notes.installation.not.found"), e);
                } else if (e.getMessage().indexOf("NGetWrapper") >= 0 || e.getMessage().indexOf("NCreateSession") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("notes.installation.not.found"), e);
                } else {
                    throw new NotesProxyRuntimeException(e.getMessage(), e);
                }
            } catch (NoClassDefFoundError e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("notes.jar.missing.in.classpath"), e);
            }
        }
        return fInternalSession;
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSession(java.lang.String)
     */
    public DSession getSession(final String serverUrl) throws DNotesRuntimeException {
        DSession session = (DSession) fIiopSessions.get(serverUrl);
        if (session == null) {
            try {
                preprocessMethod();
                final Session notesSession = NotesFactory.createSession(serverUrl);
                session = SessionProxy.getInstance(this, notesSession, getMonitor());
                setRecycler(notesSession);
                handleShutdownHook();
                fIiopSessions.put(serverUrl, session);
            } catch (NotesException e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("cannot.create.session"), new DominoException(e));
            } catch (NoClassDefFoundError e) {
                if (e.getMessage().indexOf("lotus/domino/cso/Session") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("ncso.missing"), e);
                }
            }
       }
        return session;
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSession(java.lang.String, java.lang.String, java.lang.String)
     */
    public DSession getSession(final String host, final String user, final String passwd) throws DNotesRuntimeException {
        DSession session = (DSession) fIiopSessions.get(host + ":" + user);
        if (session == null || !session.isValid()) {
            try {
                preprocessMethod();
                final Session notesSession = NotesFactory.createSession(host, user, passwd);
                session = SessionProxy.getInstance(this, notesSession, getMonitor());
                setRecycler(notesSession);
                fIiopSessions.put(host + ":" + user, session);
            } catch (NotesException e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("cannot.create.session"), new DominoException(e));
            } catch (NoClassDefFoundError e) {
                if (e.getMessage().indexOf("lotus/domino/cso/Session") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("ncso.missing"), e);
                } else {
                    throw e;
                }
            }
        }
        return session;
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSessionSSL(java.lang.String, java.lang.String, java.lang.String)
     */
    public DSession getSessionSSL(final String host, final String user, final String passwd) throws DNotesRuntimeException {
        String[] args = new String[1];
        args[0] = "-ORBEnableSSLSecurity";
        return getSession(host, args, user, passwd);
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSession(java.lang.String, java.lang.String[], java.lang.String, java.lang.String)
     */
    public DSession getSession(final String host, final String[] args, final String user, final String passwd)
            throws DNotesRuntimeException {
        DSession session = (DSession) fIiopSessions.get(host + ":" + user + ":" + args);
        if (session == null || !session.isValid()) {
            try {
                preprocessMethod();
                final Session notesSession = NotesFactory.createSession(host, args, user, passwd);
                session = SessionProxy.getInstance(this, notesSession, getMonitor());
                setRecycler(notesSession);
                fIiopSessions.put(host + ":" + user, session);
            } catch (NotesException e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("cannot.create.session"), new DominoException(e));
            } catch (NoClassDefFoundError e) {
                if (e.getMessage().indexOf("lotus/domino/cso/Session") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("ncso.missing"), e);
                }
            }
        }
        return session;
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSession(java.applet.Applet, java.lang.String, java.lang.String)
     */
    public DSession getSession(final Applet applet, final String user, final String passwd) throws DNotesRuntimeException {
        DSession session = null;
        try {
            preprocessMethod();
            final Session notesSession = NotesFactory.createSession(applet, user, passwd);
            session = SessionProxy.getInstance(this, notesSession, getMonitor());
            setRecycler(notesSession);
        } catch (NotesException e) {
            if (e.id == NotesError.NOTES_ERR_SESOPEN_FAILED) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("cannot.create.session"), new DominoException(e));
            } else {
                throw new NotesProxyRuntimeException(RESOURCES.getString("cannot.create.session"), new DominoException(e));
            }
        } catch (NoClassDefFoundError e) {
            if (e.getMessage().indexOf("lotus/domino/cso/Session") >= 0) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("ncso.missing"), e);
            }
        }
        return session;
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSession(java.lang.Object)
     */
    public DSession getSession(final Object notesSession) throws DNotesRuntimeException {
        return SessionProxy.getInstance(this, (Session) notesSession, getMonitor());
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSessionWithFullAccess()
     */
    public DSession getSessionWithFullAccess() throws DNotesRuntimeException {
        if (fInternalSession == null) {
            try {
                preprocessMethod();
                final Session session = NotesFactory.createSessionWithFullAccess();
                fInternalSession = SessionProxy.getInstance(this, session, getMonitor());
                setRecycler(session);
                logSsession(session);
                handleShutdownHook();
            } catch (NotesException e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("cannot.create.session"), new DominoException(e));
            } catch (UnsatisfiedLinkError e) {
                if (e.getMessage().indexOf("java.library.path") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("notes.installation.not.found"), e);
                } else if (e.getMessage().indexOf("NGetWrapper") >= 0 || e.getMessage().indexOf("NCreateSession") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("notes.installation.not.found"), e);
                } else {
                    throw new NotesProxyRuntimeException(e.getMessage(), e);
                }
            } catch (NoClassDefFoundError e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("notes.jar.missing.in.classpath"), e);
            }
        }
        return fInternalSession;
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getSessionWithFullAccess(java.lang.String)
     */
    public DSession getSessionWithFullAccess(final String password) throws DNotesRuntimeException {
        if (fInternalSession == null) {
            try {
                preprocessMethod();
                final Session session = NotesFactory.createSessionWithFullAccess(password);
                fInternalSession = SessionProxy.getInstance(this, session, getMonitor());
                setRecycler(session);
                logSsession(session);
                handleShutdownHook();
            } catch (NotesException e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("cannot.create.session"), new DominoException(e));
            } catch (UnsatisfiedLinkError e) {
                if (e.getMessage().indexOf("java.library.path") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("notes.installation.not.found"), e);
                } else if (e.getMessage().indexOf("NGetWrapper") >= 0 || e.getMessage().indexOf("NCreateSession") >= 0) {
                    throw new NotesProxyRuntimeException(RESOURCES.getString("notes.installation.not.found"), e);
                } else {
                    throw new NotesProxyRuntimeException(e.getMessage(), e);
                }
            } catch (NoClassDefFoundError e) {
                throw new NotesProxyRuntimeException(RESOURCES.getString("notes.jar.missing.in.classpath"), e);
            }
        }
        return fInternalSession;
    }

    /**
     * If requested and possible, adds a shutdown hook to dispose domingo
     * on termination of the virtual machine.
     */
    private void handleShutdownHook() {
        final boolean shutdownhook = DNotesFactory.getBooleanProperty("de.jakop.lotus.domingo.shutdownhook", true);
        if (shutdownhook) {
            if (checkPermission("shutdownHooks")) {
                Runtime.getRuntime().addShutdownHook(new DNotesShutdownThread());
            } else {
                getMonitor().warn(RESOURCES.getString("shutdownhook.not.allowed"));
            }
        }
    }

    /**
     * Checks if the requested access, specified by the given permission name,
     * is permitted based on the security policy currently in effect.
     *
     * @param name the name of the RuntimePermission.
     * @return <code>true</code> if access is permitted based on the current
     *         security policy, else <code>false</code>
     * @see java.lang.SecurityManager#checkPermission(java.security.Permission)
     */
    private boolean checkPermission(final String name) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm == null) {
            return true;
        }
        try {
            sm.checkPermission(new RuntimePermission(name));
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    /**
     * Logs information of a session to the current monitor.
     *
     * @param session a session
     * @throws NotesException if any problem occured with the session
     */
    private void logSsession(final Session session) throws NotesException {
        final DNotesMonitor monitor = getMonitor();
        String currentVersion = "(unknown version)";
        try {
            currentVersion = session.getNotesVersion();
        } catch (NotesException e) {
            monitor.warn(RESOURCES.getString("cannot.get.notes.version"), new DominoException(e));
        }
        if (session.getClass().getName().equals("lotus.domino.cso.Session")) {
            monitor.info(RESOURCES.getString("connect.to.domino.1", currentVersion));
        } else {
            monitor.info(RESOURCES.getString("connect.to.notes.1", currentVersion));
        }
        monitor.debug(RESOURCES.getString("local.session.established"));
        monitor.debug("    " + RESOURCES.getString("notes.user") + ": " + session.getUserName());
        monitor.debug("    " + RESOURCES.getString("notes.version") + ": " + session.getNotesVersion());
        monitor.debug("    " + RESOURCES.getString("notes.platform") + ": " + session.getPlatform());
        monitor.debug("    " + RESOURCES.getString("notes.data") + ": " + session.getEnvironmentString("Directory", true));
    }

    /**
     * @see DNotesFactory#sinitThread()
     */
    public void sinitThread() {
        getMonitor().debug(RESOURCES.getString("thread.initialize"));
        try {
            NotesThread.sinitThread();
        } catch (UnsatisfiedLinkError e) {
            getMonitor().debug("Local Notes Client not accessable; only remote connections possible");
        } catch (NoClassDefFoundError e) {
            getMonitor().debug("Local Notes Client not accessable; only remote connections possible");
        }
    }

    /**
     * @see DNotesFactory#stermThread()
     */
    public void stermThread() {
        getMonitor().debug(RESOURCES.getString("thread.terminate"));
        try {
            NotesThread.stermThread();
        } catch (UnsatisfiedLinkError e) {
            getMonitor().debug("Local Notes Client not accessable.");
        } catch (NoClassDefFoundError e) {
            getMonitor().debug("Local Notes Client not accessable.");
        }
    }

    /**
     * Preprocessing before each method invocation.
     */
    void preprocessMethod() {
        final int size1 = getBaseCache().size();
        if (fCacheThreshold != 0) {
            if (size1 > fCacheThreshold) {
                getMonitor().debug("baseCache.size = " + size1 + "; recycle queue now");
                System.gc();
            }
        }
        recycleQueue();
        final int size2 = getBaseCache().size();
        if (size1 != size2) {
            getMonitor().debug("baseCache.size = " + size2 + "; after recycling queue");
        }
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#getMonitor()
     */
    public DNotesMonitor getMonitor() {
        return fMonitor;
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#setMonitor(DNotesMonitor)
     */
    public void setMonitor(final DNotesMonitor theMonitor) {
        this.fMonitor = theMonitor;
    }
}
