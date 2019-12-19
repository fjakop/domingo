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

package de.jakop.lotus.domingo;

import de.jakop.lotus.domingo.exception.ExceptionUtil;
import de.jakop.lotus.domingo.monitor.ConsoleMonitor;
import junit.framework.TestCase;

import java.util.ResourceBundle;

/**
 * Base class for all domingo test cases.
 *
 * @author MarcusT
 */
public abstract class BaseProxyTest extends TestCase {

    private static ResourceBundle myResources = ResourceBundle.getBundle("de.bea.domingo.test");

    private static final String DOMINGO_IMPL = myResources.getString("test.impl");
    private static final String SERVER_NAME = myResources.getString("test.server");
    private static final String HOST_NAME = myResources.getString("test.host");
    private static final String USERNAME = myResources.getString("test.username");
    private static final String PASSWORD = myResources.getString("test.password");

    private DNotesFactory factory;
    private DSession session;
    private DDatabase logDatabase;
    private String serverName = SERVER_NAME;

    /**
     * @param name the name of the test
     */
    public BaseProxyTest(String name) {
        super(name);
    }

    /**
     * Sets up the test class.
     *
     * @see junit.framework.TestCase#setUp()
     */
    protected final void setUp() throws DNotesException {
        ConsoleMonitor monitor = new ConsoleMonitor();
        factory = DNotesFactory.getInstance(DOMINGO_IMPL, monitor);
        if (isAssignableFrom(factory, "de.bea.domingo.http.NotesHttpFactory")) {
            session = factory.getSession(SERVER_NAME, USERNAME, PASSWORD);
            logDatabase = session.getDatabase("", "log.nsf");
        } else if (HOST_NAME != null && HOST_NAME.length() > 0) {
            session = factory.getSession(HOST_NAME, USERNAME, PASSWORD);
            logDatabase = session.getDatabase("", "log.nsf");
        } else {
            session = factory.getSession();
            logDatabase = session.getDatabase(serverName, "log.nsf");
        }
        setUpTest();
    }

    /**
     * Can be implemented by sub classes to perform concrete setup tasks.
     */
    protected abstract void setUpTest();

    /**
     * Dynamic checks if an object is an instance of a class given by its name.
     *
     * @param object    the object to check
     * @param className the class name to check against
     * @return <code>true</code> if the object is an instance of the specified class, else <code>false</code>
     */
    private boolean isAssignableFrom(final Object object, final String className) {
        if (object == null) {
            return false;
        }
        try {
            return object.getClass().isAssignableFrom(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns the current session.
     *
     * @return session
     */
    protected final DSession getSession() {
        return session;
    }

    /**
     * Returns a log database of the specified server to test with.
     *
     * @return the log database
     */
    protected final DDatabase getDatabase() {
        return logDatabase;
    }

    /**
     * Returns the name of the server to test with.
     *
     * @return server name
     */
    protected final String getServerName() {
        return serverName;
    }

    /**
     * Returns the name of the server to test with.
     *
     * @return server name
     */
    protected final boolean isDIIOP() {
        return HOST_NAME != null && HOST_NAME.length() > 0;
    }

    /**
     * Returns the name of the server to test with.
     *
     * @return server name
     */
    protected final String getHostName() {
        return HOST_NAME;
    }

    /**
     * Indicates failure with a specific exception.
     * The stack trace of the exception is part of the message.
     *
     * @param message failure message
     * @param t       throwable causing the failure
     */
    protected final void fail(final String message, final Throwable t) {
        fail(message + ": " + ExceptionUtil.getStackTrace(t));
    }
}
