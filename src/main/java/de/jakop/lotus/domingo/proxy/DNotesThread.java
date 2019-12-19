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

import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.monitor.NullMonitor;

/**
 * The NotesThread class extends java.lang.Thread to include special
 * initialization and termination code for Domino.
 *
 * <p>This extension to Thread is required to run Java programs that make local
 * calls to the Domino classes, but is not allowed for applications that make
 * remote calls. An application that makes both local and remote calls must
 * determine dynamically when to use the static methods sinitThread and
 * stermThread. This includes threads invoked by AWT that access Domino objects.</p>
 *
 * <p>To execute threads through the Runnable interface, implement Runnable and
 * include a run method as you would for any class using threads.</p>
 *
 * <p>To execute threads through inheritance, extend NotesThread instead of
 * Thread and include a runNotes method instead of run.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public class DNotesThread extends Thread {

    /** Indicates whether an instance is initialized. */
    private boolean initialized = false;

    /** Reference to optional associated Runnable. */
    private Runnable target = null;

    /** Base Monitor instance. */
    private DNotesMonitor monitor = NullMonitor.getInstance();

    /**
     * Allocates a new <code>DNotesThread</code> object. This constructor has
     * the same effect as <code>DNotesThread(null, null,</code>
     * <i>name</i><code>)</code>, where <b><i>name</i></b> is
     * a newly generated name. Automatically generated names are of the
     * form <code>"Thread-"+</code><i>n</i>, where <i>n</i> is an integer.
     *
     * @see DNotesThread#DNotesThread(ThreadGroup, Runnable, String)
     */
    public DNotesThread() {
        super();
        setDaemon(true);
    }

    /**
     * Allocates a new <code>Thread</code> object. This constructor has the
     * same effect as <code>Thread(null, null, name)</code>.
     *
     * @param name the name of the new thread.
     *
     * @see DNotesThread#DNotesThread(ThreadGroup, Runnable, String)
     */
    public DNotesThread(final String name) {
        super(name);
        setDaemon(true);
    }

    /**
     * Allocates a new <code>DNotesThread</code> object. This constructor has
     * the same effect as <code>DNotesThread(null, target,</code> <i>name</i><code>)</code>,
     * where <i>name</i> is a newly generated name. Automatically generated
     * names are of the form <code>"Thread-"+</code><i>n</i>, where <i>n</i>
     * is an integer.
     *
     * @param theTarget the object whose <code>run</code> method is called.
     *
     * @see DNotesThread#DNotesThread(ThreadGroup, Runnable, String)
     */
    public DNotesThread(final Runnable theTarget) {
        super();
        this.target = theTarget;
        setDaemon(true);
    }

    /**
     * Allocates a new <code>DNotesThread</code> object. This constructor has
     * the same effect as <code>DNotesThread(group, target,</code> <i>name</i><code>)</code>,
     * where <i>name</i> is a newly generated name. Automatically generated
     * names are of the form <code>"Thread-"+</code><i>n</i>, where <i>n</i>
     * is an integer.
     *
     * @param group the thread group.
     * @param theTarget the object whose <code>run</code> method is called.
     *
     * @see DNotesThread#DNotesThread(ThreadGroup, Runnable, String)
     */
    public DNotesThread(final ThreadGroup group, final Runnable theTarget) {
        super(group, "domingo thread group");
        this.target = theTarget;
        setDaemon(true);
    }

    /**
     * Allocates a new <code>DNotesThread</code> object. This constructor has
     * the same effect as <code>DNotesThread(null, target, name)</code>.
     *
     * @param theTarget the object whose <code>run</code> method is called.
     * @param name the name of the new thread.
     *
     * @see DNotesThread#DNotesThread(ThreadGroup, Runnable, String)
     */
    public DNotesThread(final Runnable theTarget, final String name) {
        super(name);
        this.target = theTarget;
        setDaemon(true);
    }

    /**
     * Allocates a new <code>Thread</code> object. This constructor has the
     * same effect as <code>Thread(null, target, name)</code>.
     *
     * @param group the thread group.
     * @param name the name of the new thread.
     *
     * @see DNotesThread#DNotesThread(ThreadGroup, Runnable, String)
     */
    public DNotesThread(final ThreadGroup group, final String name) {        super(group, name);
        setDaemon(true);
    }

    /**
     * Allocates a new <code>DNotesThread</code> object so that it has
     * <code>target</code> as its run object, has the specified
     * <code>name</code> as its name, and belongs to the thread group referred
     * to by <code>group</code>. <p> If <code>group</code> is
     * <code>null</code> and there is a security manager, the group is
     * determined by the security manager's <code>getThreadGroup</code>
     * method. If <code>group</code> is <code>null</code> and there is not a
     * security manager, or the security manager's <code>getThreadGroup</code>
     * method returns <code>null</code>, the group is set to be the same
     * ThreadGroup as the thread that is creating the new thread.
     *
     * <p>If there is a security manager, its <code>checkAccess</code> method
     * is called with the ThreadGroup as its argument. <p>In addition, its
     * <code>checkPermission</code> method is called with the
     * <code>RuntimePermission("enableContextClassLoaderOverride")</code>
     * permission when invoked directly or indirectly by the constructor of a
     * subclass which overrides the <code>getContextClassLoader</code> or
     * <code>setContextClassLoader</code> methods. This may result in a
     * SecurityException.</p>
     *
     * <p>If the <code>target</code> argument is not <code>null</code>,
     * the <code>run</code> method of the <code>target</code> is called when
     * this thread is started. If the target argument is <code>null</code>,
     * this thread's <code>run</code> method is called when this thread is
     * started. <p> The priority of the newly created thread is set equal to the
     * priority of the thread creating it, that is, the currently running
     * thread. The method <code>setPriority</code> may be used to change the
     * priority to a new value.</p>
     *
     * <p>The newly created thread is initially marked as being a daemon
     * thread.</p>
     *
     * @param group the thread group.
     * @param theTarget the object whose <code>run</code> method is called.
     * @param name the name of the new thread.
     *
     * @see java.lang.Thread#Thread(java.lang.ThreadGroup, java.lang.Runnable,
     *      java.lang.String)
     */
    public DNotesThread(final ThreadGroup group, final Runnable theTarget, final String name) {
        super(group, name);
        target = theTarget;
        setDaemon(true);
    }

    /**
     * Set the monitor.
     *
     * @param theMonitor the monitor
     * @see DNotesFactory#setMonitor(DNotesMonitor)
     */
    public final void setMonitor(final DNotesMonitor theMonitor) {
        this.monitor = theMonitor;
    }

    /**
     * @see DNotesThread#initThread()
     */
    protected final void initThread() {
        if (!initialized) {
            DNotesFactory.getInstance().sinitThread();
            initialized = true;
        }
        try {
            notifyAll();
        } catch (RuntimeException e) {
            monitor.error("Cannot initialize domingo thread", e);
        }
    }

    /**
     * @see DNotesThread#termThread()
     */
    protected final void termThread() {
        if (initialized) {
            DNotesFactory.getInstance().stermThread();
            initialized = false;
        }
    }

    /**
     * Runs a Notes process.
     *
     * @throws DNotesException if any error occurred in running the thread
     */
    public void runNotes() throws DNotesException {
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public final void run() {
        try {
            if (target != null) {
                target.run();
            } else {
                runNotes();
            }
        } catch (Throwable e) {
            monitor.error("Cannot run domingo thread", e);
        }
    }

    /**
     * @see java.lang.Object#finalize()
     *
     * @throws Throwable the <code>Exception</code> raised by this method
     */
    protected final void finalize() throws Throwable {
        if (initialized) {
            System.err.println("Thread not properly terminated.");
        }
        super.finalize();
    }
}
