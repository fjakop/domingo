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

package de.jakop.lotus.domingo.http;

import java.applet.Applet;
import java.io.IOException;

import javax.xml.parsers.SAXParserFactory;

import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.monitor.MonitorEnabled;

/**
 * Factory for sessions to Notes/Domino.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class NotesHttpFactory extends DNotesFactory implements MonitorEnabled {

    // //////////////////////////////////////////////
    // constants
    // //////////////////////////////////////////////

    /** Retry count while waiting for disposal. */
    public static final int MAX_DISPOSE_TRIES = 10;

    /** Time to wait for garbage collector [milliseconds]. */
    public static final int TIME_WAIT_FOR_GC = 100;

    /** Threshold size for weak cache. */
    public static final int DEFAULT_CACHE_THRESHOLD = 2000;

    /** Key for map of default IIOP session. */
    public static final String DEFAULT_IIOP_SESSION_KEY = "defaultIIOPSession";

    /** Default file/path of the domingo support database. */
    public static final String DEFAULT_DOMINGO_DATABASE = "domingo.nsf";

    /** File/path of the domingo support database as configured. */
    private final String fDomingoDatabase;

    // //////////////////////////////////////////////
    // instance attributes
    // //////////////////////////////////////////////

    /** Base Monitor instance. */
    private DNotesMonitor fMonitor = null;

    private SAXParserFactory fFactory = SAXParserFactory.newInstance();

    // //////////////////////////////////////////////
    // creation
    // //////////////////////////////////////////////

    /**
     * Default constructor.
     *
     * <p>Must be public to allow abstract factory (the base class) to create
     * an instance of this class.</p>
     */
    public NotesHttpFactory() {
        String property = getProperty("de.jakop.lotus.domingo.http.database", DEFAULT_DOMINGO_DATABASE);
        if (property == null || property.length() == 0) {
            fDomingoDatabase = DEFAULT_DOMINGO_DATABASE;
        } else {
            fDomingoDatabase = property;
        }
    }

    /**
     * Package-private constructor to create a factory from with the Lotus Notes
     * VM with restricted security.
     *
     * <p>Must be public to allow abstract factory (the base class) to create
     * an instance of this class.</p>
     */
    NotesHttpFactory(final int threshold) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the shared SAX parser factory.
     *
     * @return SAX parser factory
     */
    public SAXParserFactory getSAXParserFactory() {
        return fFactory;
    }

    /**
     * @see DNotesFactory#gc()
     * @deprecated only use this method for testing
     */
    public void gc() {
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#disposeInternal(boolean)
     * @deprecated use {@link #disposeInternal(boolean)} instead
     */
    public void disposeInternal(final boolean force) throws DNotesRuntimeException {
        disposeInstance(force);
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#disposeInstance(boolean)
     */
    public void disposeInstance(final boolean force) throws DNotesRuntimeException {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * @see DNotesFactory#disposeInstance()
     */
    public void disposeInstance() throws DNotesRuntimeException {
        // TODO Auto-generated method stub
    }

    // //////////////////////////////////////////////
    // interface DNotesFactory
    // //////////////////////////////////////////////

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession()
     */
    public DSession getSession() throws DNotesRuntimeException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.lang.String)
     */
    public DSession getSession(final String passwd) throws DNotesRuntimeException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public DSession getSession(final String host, final String user, final String passwd) throws DNotesRuntimeException {
        try {
            return SessionHttp.getInstance(this, host, user, passwd, getMonitor());
        } catch (IOException e) {
            throw new NotesHttpRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.lang.String, java.lang.String[], java.lang.String, java.lang.String)
     */
    public DSession getSession(final String serverUrl, final String[] args, final String user, final String password)
            throws DNotesRuntimeException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSessionSSL(java.lang.String, java.lang.String, java.lang.String)
     */
    public DSession getSessionSSL(final String serverUrl, final String user, final String password)
            throws DNotesRuntimeException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.applet.Applet,
     *      java.lang.String, java.lang.String)
     */
    public DSession getSession(final Applet applet, final String user, final String passwd) throws DNotesRuntimeException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSession(java.lang.Object)
     */
    public DSession getSession(final Object notesSession) throws DNotesRuntimeException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSessionWithFullAccess()
     */
    public DSession getSessionWithFullAccess() throws DNotesRuntimeException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getSessionWithFullAccess(java.lang.String)
     */
    public DSession getSessionWithFullAccess(final String password) throws DNotesRuntimeException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#sinitThread()
     */
    public void sinitThread() {
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#stermThread()
     */
    public void stermThread() {
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#getMonitor()
     */
    public DNotesMonitor getMonitor() {
        return fMonitor;
    }

    /**
     * {@inheritDoc}
     *
     * @see DNotesFactory#setMonitor(DNotesMonitor)
     */
    public void setMonitor(final DNotesMonitor theMonitor) {
        this.fMonitor = theMonitor;
    }

    /**
     * Returns the file/path of the domingo suport database as configured.
     *
     * @return file/path of domingo suport database
     */
    public String getDomingoDatabase() {
        return fDomingoDatabase;
    }
}
