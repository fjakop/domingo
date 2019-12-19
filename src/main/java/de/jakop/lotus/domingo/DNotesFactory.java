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

import java.applet.Applet;

import de.jakop.lotus.domingo.i18n.ResourceManager;
import de.jakop.lotus.domingo.i18n.Resources;
import de.jakop.lotus.domingo.proxy.NotesProxyFactory;


/**
 * Main entry point for applications to the domingo-API.
 *
 * <p>Stand-alond client applications shall call the methos {@link #getInstance()}
 * method to obtain a singleton instance of the {@link DSession} interface.
 * Domingo takes care of proper disposual of this singleton session instance.</p>
 *
 * <p>Server based applications must call the {@link #newInstance(String)} method
 * to obtain a new instance of the </p>
 *
 * <p>Once an application has obtained a reference to a
 * <code>DNotesFactory</code> it can use the factory to obtain a Notes
 * session.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class DNotesFactory {

    /** Singleton instance of this class. */
    private static DNotesFactory instance = null;

    /** Internationalized resources. */
    private static final Resources RESOURCES = ResourceManager.getPackageResources(DNotesFactory.class);

    /**
     * Protected Constructor to restrict creation to sub classes.
     */
    protected DNotesFactory() {
        super();
    }

    /**
     * Returns a singleton instance of class DNotesFactory.
     *
     * <p>This method uses the following ordered lookup procedure to determine
     * the <code>DNotesFactory</code> implementation class to load:
     * <ul>
     * <li>Use the <code>de.bea.domingo.factory</code> system property.</li>
     * <li>Use the resource file <code>de/bea/domingo/domingo.properties</code>
     * This configuration file must be in standard
     * <code>java.util.Properties</code> format and contains the fully qualified
     * name of the implementation class with the key being the system property
     * defined above.</li>
     * <li>Defaults to class {@link NotesProxyFactory}.</li>
     * </ul></p>
     *
     * @return singleton instance of this factory
     * @throws DNotesRuntimeException if the instance cannot be created
     */
    public static synchronized DNotesFactory getInstance() throws DNotesRuntimeException {
        if (instance == null) {
            instance = newInstance();
        }
        return instance;
    }

    /**
     * Returns a singleton instance of class DNotesFactory.
     *
     * @param implementingClassName class name of implementation to use
     * @return singleton instance of this factory
     * @throws DNotesRuntimeException if the instance cannot be created
     */
    public static synchronized DNotesFactory getInstance(final String implementingClassName) throws DNotesRuntimeException {
        if (instance == null) {
            instance = newInstance(implementingClassName);
        }
        return instance;
    }

    /**
     * Returns a singleton instance of class DNotesFactory.
     *
     * @param implementingClassName class name of implementation to use
     * @param theMonitor the monitor
     * @return singleton instance of this factory
     * @throws DNotesRuntimeException if the instance cannot be created
     */
    public static DNotesFactory getInstance(final String implementingClassName, final DNotesMonitor theMonitor)
            throws DNotesRuntimeException {
        instance = getInstance(implementingClassName);
        instance.setMonitor(theMonitor);
        return instance;
    }

    /**
     * Returns a singleton instance of class DNotesFactory.
     *
     * <p>This method uses the following ordered lookup procedure to determine
     * the <code>DNotesFactory</code> implementation class to load:
     * <ul>
     * <li>Use the <code>de.bea.domingo.factory</code> system property.</li>
     * <li>Use the resource file de/bea/domingo/domingo.properties
     * This configuration file must be in standard
     * <code>java.util.Properties</code> format and contains the fully qualified
     * name of the implementation class with the key being the system property
     * defined above.</li>
     * <li>Defaults to class {@link NotesProxyFactory}.</li>
     * </ul></p>
     *
     * @param theMonitor the monitor
     * @return singleton instance of this factory
     * @throws DNotesRuntimeException if the instance cannot be created
     */
    public static DNotesFactory getInstance(final DNotesMonitor theMonitor) throws DNotesRuntimeException {
        instance = getInstance();
        if (theMonitor != null) {
            instance.setMonitor(theMonitor);
        }
        return instance;
    }

    /**
     * Returns a new instance of class DNotesFactory.
     *
     * <p>It is up to the user to remember this instance and to keep it as a singleton if needed.</p>
     * <p>By default the implementing class is {@link NotesProxyFactory}.</p>
     *
     * @return new instance of this factory
     * @throws DNotesRuntimeException if the instance cannot be created
     * @since domingo 1.4
     */
    public static DNotesFactory newInstance() throws DNotesRuntimeException {
        try {
            return (DNotesFactory) DNotesFactoryFinder.find();
        } catch (InstantiationException e) {
            throw new DNotesRuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new DNotesRuntimeException(e.getMessage());
        } catch (NoClassDefFoundError e) {
            if (e.getMessage().indexOf("lotus/domino") >= 0) {
                throw new DNotesRuntimeException(RESOURCES.getString("notes.jar.missing"), e);
            } else {
                throw new DNotesRuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * Returns a new instance of class DNotesFactory.
     *
     * <p>It is up to the user to remember this instance and to keep it as a singleton if needed.</p>
     * <p>By default the implementing class is {@link NotesProxyFactory}.</p>
     *
     * @param theMonitor the monitor
     * @return new instance of this factory
     * @throws DNotesRuntimeException if the instance cannot be created
     * @since domingo 1.4
     */
    public static DNotesFactory newInstance(final DNotesMonitor theMonitor) throws DNotesRuntimeException {
        DNotesFactory newInstance = newInstance();
        if (theMonitor != null) {
            newInstance.setMonitor(theMonitor);
        }
        return newInstance;
    }

    /**
     * Returns a new instance of class DNotesFactory.
     *
     * <p>It is up to the user to remember this instance and to keep it as a singleton if needed.</p>
     *
     * @param implementingClassName class name of implementation to use
     * @param theMonitor the monitor
     * @return new instance of this factory
     * @throws DNotesRuntimeException if the instance cannot be created
     * @since domingo 1.4
     */
    public static DNotesFactory newInstance(final String implementingClassName, final DNotesMonitor theMonitor)
        throws DNotesRuntimeException {
        DNotesFactory newInstance = newInstance(implementingClassName);
        newInstance.setMonitor(theMonitor);
        return newInstance;
    }

    /**
     * Returns a new instance of class DNotesFactory.
     *
     * <p>It is up to the user to remember this instance and to keep it as a singleton if needed.</p>
     *
     * @param implementingClassName class name of implementation to use
     * @return new instance of this factory
     * @throws DNotesRuntimeException if the instance cannot be created
     * @since domingo 1.4
     */
    public static DNotesFactory newInstance(final String implementingClassName) throws DNotesRuntimeException {
        try {
            return (DNotesFactory) DNotesFactoryFinder.find(implementingClassName);
        } catch (InstantiationException e) {
            throw new DNotesRuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new DNotesRuntimeException(e.getMessage());
        } catch (NoClassDefFoundError e) {
            if (e.getMessage().indexOf("lotus/domino") >= 0) {
                throw new DNotesRuntimeException(RESOURCES.getString("notes.jar.missing"), e);
            } else {
                throw new DNotesRuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * Creates a local session.
     * <p>(Notes client must be installed)</p>
     *
     * @return a local session
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSession() throws DNotesRuntimeException;

    /**
     * Creates a DIIOP session.
     *
     * @param serverUrl URL of server (e.g. <tt>"https://plato.acme:8080"</tt>)
     * @return a local session
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSession(final String serverUrl) throws DNotesRuntimeException;

    /**
     * Creates a remote (IIOP or Http) session using host name.
     *
     * @param serverUrl URL of server (e.g. <tt>"https://plato.acme:8080"</tt>)
     * @param user user name for authentication
     * @param password password for for authentication
     * @return a remote session
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSession(final String serverUrl, final String user, final String password)
        throws DNotesRuntimeException;

    /**
     * Creates a remote (IIOP or Http) session with SSL using host name.
     *
     * @param serverUrl URL of server (e.g. <tt>"https://plato.acme:8080"</tt>)
     * @param user user name for authentication
     * @param password password for for authentication
     * @return a remote session
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSessionSSL(final String serverUrl, final String user, final String password)
        throws DNotesRuntimeException;

    /**
     * Creates a remote (IIOP or Http) session with arguments using host name.
     *
     * @param serverUrl URL of server (e.g. <tt>"https://plato.acme:8080"</tt>)
     * @param args array of additional arguments
     * @param user user name for authentication
     * @param password password for for authentication
     * @return a remote session
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSession(final String serverUrl, final String[] args, final String user, final String password)
        throws DNotesRuntimeException;

    /**
     * Creates a remote (IIOP) session for an applet.
     *
     * @param applet applet instance
     * @param user user name for authentication
     * @param password password for for authentication
     * @return a remote session for an applet
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSession(final Applet applet, final String user, final String password)
        throws DNotesRuntimeException;

    /**
     * Creates a Domingo session for an existing Notes session.
     *
     * <p>This method is used only internally in Notes agents and Notes applets.</p>
     *
     * @param notesSession existing Notes session
     * @return a Domingo session for the given Notes session
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSession(final Object notesSession) throws DNotesRuntimeException;

    /**
     * Creates a local session.
     * <p>(Notes client must be installed)</p>
     * <p>Access restrictions according to readers items are bypassed.</p>
     *
     * @return a local session
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSessionWithFullAccess() throws DNotesRuntimeException;

    /**
     * Creates a local session.
     * <p>(Notes client must be installed)</p>
     * <p>Access restrictions according to readers items are bypassed.</p>
     *
     * @param password password for for authentication
     * @return a local session
     * @throws DNotesRuntimeException if the session cannot be created
     */
    public abstract DSession getSessionWithFullAccess(final String password) throws DNotesRuntimeException;

    /**
     * Returns the value of a property or returns the given default value if no
     * configuration found.
     *
     * @param key the key of the property
     * @param defaultValue default value, if no configuration found
     * @return the value of the property
     */
    public static final String getProperty(final String key, final String defaultValue) {
        return DNotesFactoryFinder.getProperty(key, defaultValue);
    }

    /**
     * Sets a configuration value and overwrites any default or configuration settings.
     *
     * @param key the key of the property
     * @param value the new value
     */
    public static void setProperty(final String key, final String value) {
        DNotesFactoryFinder.setProperty(key, value);
    }

    /**
     * Returns the integer value of a property or returns the given default value if no
     * configuration found or if the value cannot be converted to an integer value.
     *
     * @param key the key of the property
     * @param defaultValue default value, if no configuration found
     * @return the value of the property
     */
    public static final int getIntProperty(final String key, final int defaultValue) {
        String value = getProperty(key, Integer.toString(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the integer value of a property or returns the given default value if no
     * configuration found or if the value cannot be converted to an integer value.
     *
     * @param key the key of the property
     * @param defaultValue default value, if no configuration found
     * @return the value of the property
     */
    public static final boolean getBooleanProperty(final String key, final boolean defaultValue) {
        String value = getProperty(key, defaultValue ? "true" : "false");
        try {
            return Boolean.getBoolean(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Enables Notes access for the current thread.
     */
    public abstract void sinitThread();

    /**
     * Disables Notes access for the current thread.
     */
    public abstract void stermThread();

    /**
     * Runs the garbage collector and tries to recycle all internal
     * notes objects.
     * <p><b>ATTENTION:</b></p>
     * <p>This method should not be used in productive code. It only
     * exists for tests, e.g. to ensure clean memory before analyzing
     * with JProbe.</p>
     *
     * @see java.lang.Runtime#gc()
     * @deprecated only use this method for testing
     */
    protected abstract void gc();

    /**
     * Disposes the singleton instance of the factory.
     *
     * <p>It is strictly recommended to call this method before exiting
     * applications in order to not destabilize the Lotus Notes Client.</p>
     *
     * <p>If a factory was created with a
     * {@link #newInstance(String, DNotesMonitor)} method, the factory must be
     * disposed with the instance method {@link #disposeInstance(boolean)}.
     *
     * <p>Equivalent to
     * <code>{link {@link #dispose(boolean) disposeInstance(true)}</code></p>.
     *
     * @throws DNotesRuntimeException if an error occurs during disposal or
     *              if not all objects can be disposed
     */
    public static synchronized void dispose() throws DNotesRuntimeException {
        if (instance != null) {
            instance.disposeInstance(true);
            instance = null;
        }
        instance = null;
    }

    /**
     * Disposes the singleton instance of the factory.
     *
     * <p>It is strictly recommended to call this method before exiting
     * applications in order to not destabilize the Lotus Notes Client.</p>
     *
     * <p>If a factory was created with a
     * {@link #newInstance(String, DNotesMonitor)} method, the factory must be
     * disposed with the instance method {@link #disposeInstance(boolean)}.
     *
     * @param force indicates if disposal should happen even if still any
     *              string or soft reference exists. if <code>false</code>,
     *              only weak references must remain.
     * @throws DNotesException if an error occurs during disposal or
     *              if not all objects can be disposed
     */
    public static synchronized void dispose(final boolean force) throws DNotesException {
        if (instance != null) {
            instance.disposeInstance(force);
            instance = null;
        }
        instance = null;
    }

    /**
     * Disposes all internal resources of the Notes connection.
     *
     * @param force indicates if disposal should happen even if still any
     *              string or soft reference exists. if <code>false</code>,
     *              only weak references must remain.
     * @throws DNotesRuntimeException if an error occurs during disposal or
     *              if not all objects can be disposed
     * @deprecated use {@link #disposeInstance(boolean)} instead
     */
    public abstract void disposeInternal(final boolean force) throws DNotesRuntimeException;

    /**
     * Disposes all internal resources of the Notes connection.
     *
     * @param force indicates if disposal should happen even if still any
     *              string or soft reference exists. if <code>false</code>,
     *              only weak references must remain.
     * @throws DNotesRuntimeException if an error occurs during disposal or
     *              if not all objects can be disposed
     */
    public abstract void disposeInstance(final boolean force) throws DNotesRuntimeException;

    /**
     * Disposes all internal resources of the Notes connection.
     *
     * <p>Equivalent to
     * <code>{link {@link #disposeInstance(boolean) disposeInstance(false)}</code></p>.
     *
     * @throws DNotesRuntimeException if an error occurs during disposal or
     *              if not all objects can be disposed
     */
    public abstract void disposeInstance() throws DNotesRuntimeException;

    /**
     * Get the current monitor.
     * @return current monitor
     */
    protected abstract DNotesMonitor getMonitor();

    /**
     * Set the monitor.
     * @param theMonitor the monitor
     */
    protected abstract void setMonitor(final DNotesMonitor theMonitor);
}
