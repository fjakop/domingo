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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Invocation task for worker thread.
 *
 * <p>An Invocation task is an implementation of the <code>Runnable</code>
 * interface where the <code>run()</code> method executes a method on an
 * object with an array of arguments.</p>
 * <p>This task is used to delegate method calls to worker threads in a generic
 * way.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class InvocationTask implements Runnable {

    /** Object to invoke method on. */
    private Object fObject = null;

    /** Method to invoke. */
    private Method fMethod = null;

    /** Arguments to the method invocation. */
    private Object[] fArgs = null;

    /** Result of the method invocation. */
    private Object fResult = null;

    /** Optionally thrown Throwable of the method invocation. */
    private Throwable fThrowable = null;

    /** Indicates if the task is completed. */
    private boolean fCompleted = false;

    private Object mutex = new Object();

    /**
     * Constructor.
     *
     * @param object the object to invoke the method on
     * @param method the method to invoke
     * @param args the arguments for the method
     */
    InvocationTask(final Object object, final Method method, final Object[] args) {
        fObject = object;
        fMethod = method;
        fArgs = args;
    }

    /**
     * Returns the result from the method invocation.
     *
     * @return result from invoked method
     */
    public Object getResult() {
        synchronized (mutex) {
            return fResult;
        }
    }

    /**
     * Returns the optional thrown throwable.
     *
     * @return Returns the optional throwable.
     */
    protected Throwable getThrowable() {
        return fThrowable;
    }

    /**
     * Invokes the method.
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        synchronized (mutex) {
            try {
                fResult = fMethod.invoke(fObject, fArgs);
            } catch (InvocationTargetException e) {
                fThrowable = e.getTargetException();
            } catch (Throwable t) {
                fThrowable = t;
            } finally {
                fCompleted = true;
                mutex.notifyAll();
            }
        }
    }

    /**
     * Indicates whether the task is completed.
     *
     * @return <code>true</code> if the task is completed, else
     * <code>false</code>
     */
    public boolean isCompleted() {
        return fCompleted;
    }
}
