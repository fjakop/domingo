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

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import de.jakop.lotus.domingo.DAgentContext;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DBaseDocument;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDxlExporter;
import de.jakop.lotus.domingo.DLog;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.DViewEntry;
import de.jakop.lotus.domingo.proxy.BaseProxy;

/**
 * Notes session.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class SessionHttp extends BaseHttp implements DSession {

    /** Column number of <tt>MailFile</tt> column in view <tt>($Users)</tt>. */
    private static final int MAIL_FILE_COLUMN = 6;

    /** File extension of Lotus Notes databases. */
    private static final String DATABASE_EXT = ".nsf";

    /** serial version ID for serialization. */
    private static final long serialVersionUID = -490910110376926306L;

    private DominoHttpClient fHttpClient;

    /**
     * Constructor.
     *
     * @throws IOException if the session to the server cannot be created
     */
    private SessionHttp(final NotesHttpFactory theFactory, final String host, final String user, final String passwd,
            final DNotesMonitor monitor) throws IOException {
        super(theFactory, null, monitor);
        fHttpClient = new DominoHttpClient(getMonitor(), host, user, passwd);
        fHttpClient.login();
    }

    /**
     * Creates an Http session object.
     *
     * @param factory the controling factory
     * @param host the host for the session to connect
     * @param user the username for login
     * @param passwd the password for login
     * @param monitor the monitor
     * @return a session object
     * @throws IOException if the session to the server cannot be created
     */
    static DSession getInstance(final NotesHttpFactory factory, final String host, final String user, final String passwd,
            final DNotesMonitor monitor) throws IOException {
        return new SessionHttp(factory, host, user, passwd, monitor);
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#isOnServer()
     */
    public boolean isOnServer() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getDatabase(java.lang.String,
     *      java.lang.String)
     */
    public DDatabase getDatabase(final String serverName, final String databaseName) throws DNotesRuntimeException {
        return DatabaseHttp.getInstance(getFactory(), this, databaseName, getMonitor());
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseProxy#toString()
     */
    public String toString() {
        return BaseHttp.toStringIntern(this);
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getUserName()
     */
    public String getUserName() {
        return fHttpClient.getUserName();
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#createDatabase(java.lang.String,
     *      java.lang.String)
     */
    public DDatabase createDatabase(final String serverName, final String databaseName) {
        final String path = databaseName.replace('\\', '/');
        final String bs;
        try {
            bs = execute("cmd=CreateDatabase&file=" + path);
        } catch (IOException e) {
            throw new NotesHttpRuntimeException("Cannot create database: " + path, e);
        }
        if (bs.indexOf("Database created") >= 0) {
            return DatabaseHttp.getInstance(getFactory(), this, databaseName, getMonitor());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getCommonUserName()
     */
    public String getCommonUserName() {
        return fHttpClient.getCommonUserName();
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getCanonicalUserName()
     */
    public String getCanonicalUserName() {
        return fHttpClient.getCanonicalUserName();
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#evaluate(java.lang.String)
     */
    public List evaluate(final String formula) throws DNotesRuntimeException {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws DNotesRuntimException
     * @see DSession#evaluate(java.lang.String,
     *      DBaseDocument)
     */
    public List evaluate(final String formula, final DBaseDocument doc) throws DNotesRuntimeException {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getEnvironmentString(java.lang.String)
     */
    public String getEnvironmentString(final String name) {
        return getEnvironmentString(name, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getEnvironmentValue(java.lang.String)
     */
    public Object getEnvironmentValue(final String name) {
        return getEnvironmentValue(name, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getEnvironmentString(java.lang.String,
     *      boolean)
     */
    public String getEnvironmentString(final String name, final boolean isSystem) {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getEnvironmentValue(java.lang.String,
     *      boolean)
     */
    public Object getEnvironmentValue(final String name, final boolean isSystem) {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#setEnvironmentString(java.lang.String,
     *      java.lang.String)
     */
    public void setEnvironmentString(final String name, final String value) {
        // TODO
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#setEnvironmentString(java.lang.String,
     *      java.lang.String, boolean)
     */
    public void setEnvironmentString(final String name, final String value, final boolean isSystem) {
        // TODO
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#createLog(java.lang.String)
     */
    public DLog createLog(final String name) {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getAddressBooks()
     */
    public List getAddressBooks() {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getAbbreviatedName(java.lang.String)
     */
    public String getAbbreviatedName(final String canonicalName) {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getCanonicalName(java.lang.String)
     */
    public String getCanonicalName(final String abreviatedName) {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getAgentContext()
     */
    public DAgentContext getAgentContext() {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getCurrentTime()
     */
    public Calendar getCurrentTime() {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#isValid()
     */
    public boolean isValid() {
        // TODO
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getNotesVersion()
     */
    public String getNotesVersion() {
        return "unknown"; // TODO getNotesVersion()
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getPlatform()
     */
    public String getPlatform() {
        return "unknown"; // TODO getPlatform()
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#createDxlExporter()
     */
    public DDxlExporter createDxlExporter() throws DNotesException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getMailServer()
     */
    public String getMailServer() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getMailDatabaseName()
     */
    public String getMailDatabaseName() {
        return getMailDatabaseName(fHttpClient.getUserName());
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getMailDatabaseName()
     */
    private String getMailDatabaseName(final String username) {
        DViewEntry entry = getUserEntry(username);
        String mailFile = getColumnValueString(entry, MAIL_FILE_COLUMN);
        if (mailFile == null || mailFile.length() == 0) {
            return null;
        }
        mailFile = mailFile.replace('\\', '/');
        if (!mailFile.endsWith(DATABASE_EXT)) {
            return mailFile + DATABASE_EXT;
        }
        return mailFile;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getMailDatabase()
     */
    public DDatabase getMailDatabase() throws DNotesException {
        return getMailDatabase(getMailDatabaseName());
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getMailDatabase(java.lang.String)
     */
    public DDatabase getMailDatabase(final String username) throws DNotesException {
      String databaseName = getMailDatabaseName(username);
      return DatabaseHttp.getInstance(getFactory(), this, databaseName, getMonitor());
    }

    /**
     * Returns the column value of a view entry at a given index as a single string.
     * If the column value is a list of values, only the first value of the lust is returned.
     * If the column value is not a string, the value is converted to a string with the
     * <tt>toString()</tt> method of the value.
     *
     * @param entry the view entry
     * @param index the column index
     * @return column value as string
     */
    private String getColumnValueString(final DViewEntry entry, final int index) {
        Object object = entry.getColumnValues().get(index);
        if (object instanceof List) {
            return ((List) object).get(0).toString();
        } else if (object instanceof String) {
            return (String) object;
        } else {
            return object.toString();
        }
    }

    /**
     * Returns the person document of the current user from the servers
     * names.nsf.
     *
     * @param username a username
     * @return person document of the user
     */
    private DViewEntry getUserEntry(final String username) {
        DDatabase database = getDatabase("", "names.nsf");
        DView view = database.getView("($Users)");
        Iterator iterator = view.getAllEntriesByKey(username);
        if (iterator.hasNext()) {
            return (DViewEntry) iterator.next();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getMailDomain()
     */
    public String getMailDomain() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#resolve(java.lang.String)
     */
    public DBase resolve(final String url) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see DSession#getServerName()
     */
    public String getServerName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Factory method for GET methods.
     * @param pathInfo path_info of the method
     * @return Http GET method
     */
    public DominoGetMethod createGetMethod(final String pathInfo) {
        return fHttpClient.createGetMethod(pathInfo);
    }

    /**
     * Factory method for POST methods.
     * @param pathInfo path_info of the method
     * @return Http POST method
     */
    public DominoPostMethod createPostMethod(final String pathInfo) {
        return fHttpClient.createPost(pathInfo);
    }

    /**
     * Executes the given {@link DominoHttpMethod HTTP method}.
     *
     * @param method the {@link DominoHttpMethod HTTP method} to execute
     * @return the method's response code
     *
     * @throws IOException If an I/O (transport) error occurs. Some transport
     *             exceptions can be recovered from.
     */
    protected int executeMethod(final DominoHttpMethod method) throws IOException {
        return fHttpClient.executeMethod(method);
    }

    /**
     * Checks if the Domingo database is available on the server or not.
     *
     * @return <code>true</code> if the Domingo database is available, else
     *         <code>false</code>
     */
    public boolean isDomingoAvailable() {
        // TODO implement isDomingoAvailable()
        return false;
    }

    /**
     * {@inheritDoc}
     * @see DSession#getTimeZone()
     */
    public TimeZone getTimeZone() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see DSession#setTimeZone(java.util.TimeZone)
     */
    public void setTimeZone(final TimeZone zone) {
        // TODO Auto-generated method stub
    }
}
