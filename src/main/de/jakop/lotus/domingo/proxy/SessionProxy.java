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

package de.jakop.lotus.domingo.proxy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import de.jakop.lotus.domingo.exception.DominoException;
import lotus.domino.ACL;
import lotus.domino.Agent;
import lotus.domino.AgentContext;
import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.DbDirectory;
import lotus.domino.Document;
import lotus.domino.DxlExporter;
import lotus.domino.Form;
import lotus.domino.Log;
import lotus.domino.Name;
import lotus.domino.NotesError;
import lotus.domino.NotesException;
import lotus.domino.Registration;
import lotus.domino.Session;
import lotus.domino.View;
import de.jakop.lotus.domingo.DAgentContext;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DBaseDocument;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DDxlExporter;
import de.jakop.lotus.domingo.DLog;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.cache.Cache;
import de.jakop.lotus.domingo.cache.SimpleCache;

/**
 * Notes session.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class SessionProxy extends BaseProxy implements DSession {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3617290125554628918L;

    /** Cache of all databases within a session.
     * <p>Maps database file names to database proxies.</p>*/
    private final Cache databaseCache = new SimpleCache();

    /** Reference to the international settings of a session. */
    private final InternationalProxy international;

    /** Cached canonical form of name of current user. */
    private String canonicalUserName = null;

    /** mail server of current user. */
    private String mailServer;

    /** name of mail database of current user. */
    private String mailDatabaseName;

    /** mutex to synchronize fetching mail info. */
    private Object fMailInfoMutex = new Object();

    /** name of mail domain of current user. */
    private String mailDomain;

    /** Time zone to use when creating calendar instances. */
    private TimeZone fZone;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param session the encapsulated Notes-Session.
     * @param monitor the monitor
     */
    private SessionProxy(final NotesProxyFactory theFactory, final Session session, final DNotesMonitor monitor) {
        super(theFactory, null, session, monitor);
        international = new InternationalProxy(session, monitor);
    }

    /**
     * Creates an encapsulated notes session object.
     *
     * @param theFactory the controlling factory
     * @param theSession the Notes Session
     * @param monitor the monitor
     * @return a session object
     */
    public static DSession getInstance(final NotesProxyFactory theFactory, final Session theSession,
                                final DNotesMonitor monitor) {
        if (theSession == null) {
            return null;
        }
        SessionProxy sessionProxy = (SessionProxy) theFactory.getBaseCache().get(theSession);
        if (sessionProxy == null) {
            sessionProxy = new SessionProxy(theFactory, theSession, monitor);
            theFactory.getBaseCache().put(theSession, sessionProxy);
        }
        return sessionProxy;
    }

    /**
     * Returns the associated Notes session.
     *
     * @return associated Notes session
     */
    private Session getSession() {
        getFactory().preprocessMethod();
        return (Session) this.getNotesObject();
    }

    /**
     * @return Returns the international.
     */
    public InternationalProxy getInternational() {
        return international;
    }

    /**
     * {@inheritDoc}
     * @see DSession#isOnServer()
     */
    public boolean isOnServer() {
        getFactory().preprocessMethod();
        try {
            return getSession().isOnServer();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.check.isonserver"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#getDatabase(java.lang.String, java.lang.String)
     */
    public DDatabase getDatabase(final String serverName, final String databaseName) throws DNotesException {
        getFactory().preprocessMethod();
        final String dbKey = DatabaseProxy.getDatabaseKey(serverName, databaseName);
        DDatabase database = (DDatabase) databaseCache.get(dbKey);
        String userName;
        try {
            userName = getCanonicalUserName();
        } catch (RuntimeException re) {
            userName = null;
        }
        if (database == null) {
            try {
                database = getDatabaseIntern(serverName, databaseName, dbKey, userName);
                databaseCache.put(dbKey, database);
            } catch (NotesException e) {
                if (userName == null || "".equals(userName)) {
                    throw newException(RESOURCES.getString("session.cannot.currentuser.unknown"), e);
                } else if (e.id == NotesError.NOTES_ERR_DBSECURITY) {
                    throw newException("Current user is unknown. Maybe the notes.ini is corrupt.", e);
                } else if (e.id == NotesError.NOTES_ERR_DBNOACCESS) {
                    final DominoException d = new DominoException(e);
                    final String msg = RESOURCES.getString("session.database.no.access.2", userName, dbKey);
                    throw new NotesProxyException(msg, d);
                } else {
                    final DominoException d = new DominoException(e);
                    String msg = RESOURCES.getString("session.cannot.find.database.2", userName, dbKey);
                    throw new NotesProxyException(msg, d);
                }
            }
        }
        return database;
    }

    /**
     * For a given Notes database instance, returns the corresponding existing
     * domingo database instance if it already exists in the cache, or creates
     * and returns a new domingo database instance.
     *
     * @param database a Notes database instance
     * @return the corresponding domingo database instance
     * @throws DNotesException if accessing the database causes an error
     */
    private DatabaseProxy getDatabaseProxy(final Database database) throws DNotesException {
        try {
            return (DatabaseProxy) getDatabase(database.getServer(), database.getFilePath());
        } catch (NotesException e) {
            final DominoException d = new DominoException(e);
            throw new NotesProxyException(RESOURCES.getString("session.cannot.get.databasekey"), d);
        }
    }

    /**
     * Internal implementation of getting a database.
     *
     * @param serverName server name
     * @param databaseName database name
     * @param dbKey database key
     * @param userName name of current user
     * @return the database
     * @throws NotesException if the database cannot be opened due to a notes exception
     * @throws NotesProxyException if the database cannot be opened even if no notes exception occurred
     */
    private DDatabase getDatabaseIntern(final String serverName, final String databaseName, final String dbKey,
            final String userName) throws NotesException, DNotesException {
        DDatabase database;
        Database notesDatabase = getSession().getDatabase(serverName, databaseName);
        if (notesDatabase == null) {
            getMonitor().error(RESOURCES.getString("session.cannot.get.database.2", userName, dbKey));
            throw new NotesProxyException(RESOURCES.getString("session.database.not.found.1", dbKey));
        }
        if (notesDatabase.getCurrentAccessLevel() == ACL.LEVEL_NOACCESS) {
            getMonitor().warn(RESOURCES.getString("session.cannot.access.database.2", userName, dbKey));
        }
        if (!notesDatabase.isOpen()) {
            try {
                notesDatabase.open();
            } catch (NotesException e) {
                getMonitor().debug(RESOURCES.getString("session.cannot.open.database.2", userName, dbKey), e);
            }
        }
        if (!notesDatabase.isOpen()) {
            notesDatabase.recycle();
            notesDatabase = getSession().getDatabase(serverName, databaseName);
        }
        if (!notesDatabase.isOpen()) {
            try {
                notesDatabase.open();
            } catch (NotesException e) {
                getMonitor().debug(RESOURCES.getString("session.failed.open.database.2", userName, dbKey), e);
            }
        }
        if (!notesDatabase.isOpen()) {
            getMonitor().error(RESOURCES.getString("session.cannot.open.database.2" + userName, dbKey));
            throw new NotesProxyException(RESOURCES.getString("session.cannot.find.database.2", userName, dbKey));
        }
        database = DatabaseProxy.getInstance(getFactory(), this, notesDatabase, getMonitor(), true);
        return database;
    }

    /**
     * @see BaseProxy#toString()
     * @return  a string representation of the object.
     */
    public String toString() {
        return toStringIntern(this);
    }

    /**
     * {@inheritDoc}
     * @see DSession#getUserName()
     */
    public String getUserName() {
        getFactory().preprocessMethod();
        try {
            return getSession().getUserName();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.username"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#createDatabase(java.lang.String, java.lang.String)
     */
    public DDatabase createDatabase(final String serverName, final String databaseName) {
        getFactory().preprocessMethod();
        try {
            final DbDirectory directory = getSession().getDbDirectory(serverName);
            final Database newDatabase = directory.createDatabase(databaseName, true);
            databaseCache.put(DatabaseProxy.getDatabaseKey(serverName, databaseName), newDatabase);
            return DatabaseProxy.getInstance(getFactory(), this, newDatabase, getMonitor(), true);
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.create.database"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#getCommonUserName()
     */
    public String getCommonUserName() {
        getFactory().preprocessMethod();
        try {
            return getSession().getCommonUserName();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.commonusername"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#getCanonicalUserName()
     */
    public String getCanonicalUserName() {
        if (canonicalUserName != null) {
            return canonicalUserName;
        }
        getFactory().preprocessMethod();
        Name name = null;
        try {
            name = getSession().getUserNameObject();
            canonicalUserName = name.getCanonical();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.canonicalusername"), e);
        } finally {
            if (name != null) {
                try {
                    name.recycle();
                    name = null;
                } catch (NotesException e) {
                    getMonitor().error("Cannot recycle name object", new DominoException(e));
                }
            }
        }
        return canonicalUserName;
    }

    /**
     * {@inheritDoc}
     * @see DSession#evaluate(java.lang.String)
     */
    public List evaluate(final String formula) throws DNotesException {
        getFactory().preprocessMethod();
        try {
            return getSession().evaluate(formula);
        } catch (NotesException e) {
            throw newException(RESOURCES.getString("session.cannot.evaluate.formula.1", formula), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#evaluate(java.lang.String, DBaseDocument)
     */
    public List evaluate(final String formula, final DBaseDocument doc) throws DNotesException {
        if (!(doc instanceof BaseDocumentProxy)) {
            throw newRuntimeException("parameter doc is not a valid document");
        }
        getFactory().preprocessMethod();
        try {
             final Vector vector = getSession().evaluate(formula, ((BaseDocumentProxy) doc).getDocument());
             final List convertedValues = convertNotesDateTimesToCalendar(vector);
             recycleDateTimeList(vector);
             return Collections.unmodifiableList(convertedValues);
        } catch (NotesException e) {
            throw newException(RESOURCES.getString("session.cannot.evaluate.formula.1", formula), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#getEnvironmentString(java.lang.String)
     */
    public String getEnvironmentString(final String name) {
        return getEnvironmentString(name, false);
    }

    /**
     * {@inheritDoc}
     * @see DSession#getEnvironmentValue(java.lang.String)
     */
    public Object getEnvironmentValue(final String name) {
        return getEnvironmentValue(name, false);
    }

    /**
     * {@inheritDoc}
     * @see DSession#getEnvironmentString(java.lang.String, boolean)
     */
    public String getEnvironmentString(final String name, final boolean isSystem) {
        getFactory().preprocessMethod();
        try {
            return getSession().getEnvironmentString(name, isSystem);
        } catch (NotesException e) {
            String env = isSystem ? "" : "$" + name;
            String msg = RESOURCES.getString("session.cannot.get.environmentstring.1", env);
            throw newRuntimeException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#getEnvironmentValue(java.lang.String, boolean)
     */
    public Object getEnvironmentValue(final String name, final boolean isSystem) {
        getFactory().preprocessMethod();
        try {
            return getSession().getEnvironmentString(name, isSystem);
        } catch (NotesException e) {
            String env = isSystem ? "" : "$" + name;
            String msg = RESOURCES.getString("session.cannot.get.environmentvalue.1", env);
            throw newRuntimeException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#setEnvironmentString(java.lang.String, java.lang.String)
     */
    public void setEnvironmentString(final String name, final String value) {
        getFactory().preprocessMethod();
        try {
            getSession().setEnvironmentVar(name, value);
        } catch (NotesException e) {
            String env = "$" + name;
            throw newRuntimeException(RESOURCES.getString("session.cannot.set.environmentstring.1", env), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#setEnvironmentString(java.lang.String, java.lang.String, boolean)
     */
    public void setEnvironmentString(final String name, final String value, final boolean isSystem) {
        getFactory().preprocessMethod();
        try {
            getSession().setEnvironmentVar(name, value, isSystem);
        } catch (NotesException e) {
            String env = isSystem ? "" : "$" + name;
            throw newRuntimeException(RESOURCES.getString("session.cannot.set.environmentstring.1", env), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#createLog(java.lang.String)
     */
    public DLog createLog(final String name) {
        getFactory().preprocessMethod();
        try {
            final Log log = getSession().createLog(name);
            return LogProxy.getInstance(getFactory(), this, log, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.create.log"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#getAddressBooks()
     */
    public List getAddressBooks() {
        getFactory().preprocessMethod();
        try {
            final Vector vector = getSession().getAddressBooks();
            final List list = new ArrayList();
            for (int i = 0; i < vector.size(); i++) {
                final Database notesDatabase = (Database) vector.get(i);
                String key = notesDatabase.toString();
                try {
                    key = DatabaseProxy.getDatabaseKey(notesDatabase.getServer(), notesDatabase.getFilePath());
                    notesDatabase.open();
                    final DDatabase proxy = DatabaseProxy.getInstance(getFactory(), this, notesDatabase, getMonitor(), false);
                    databaseCache.put(key, proxy);
                    list.add(proxy);
                } catch (NotesException ne) {
                    getMonitor().warn(RESOURCES.getString("session.cannot.open.addressbook.1", key));
                }
            }
            return list;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.create.log"), e);
        }
    }


    /**
     * {@inheritDoc}
     * @see DSession#getAbbreviatedName(java.lang.String)
     */
    public String getAbbreviatedName(final String canonicalName) {
        getFactory().preprocessMethod();
        final Name name;
        final String abbreviatedName;
        try {
            name = this.getSession().createName(canonicalName);
        } catch (NotesException e) {
            getMonitor().debug(RESOURCES.getString("session.cannot.create.name.1", canonicalName), e);
            return null;
        }
        try {
            abbreviatedName = name.getAbbreviated();
        } catch (NotesException e) {
            getMonitor().debug(RESOURCES.getString("session.cannot.abbreviate.name.1", canonicalName), e);
            return null;
        }
        try {
            name.recycle();
        } catch (NotesException e) {
            getMonitor().debug(RESOURCES.getString("session.cannot.recycle.name.1", canonicalName), e);
        }
        return abbreviatedName;
    }

    /**
     * {@inheritDoc}
     * @see DSession#getCanonicalName(java.lang.String)
     */
    public String getCanonicalName(final String abreviatedName) {
        getFactory().preprocessMethod();
        final Name name;
        final String canonicalName;
        try {
            name = this.getSession().createName(abreviatedName);
        } catch (NotesException e) {
            getMonitor().debug(RESOURCES.getString("session.cannot.create.name.1", abreviatedName), e);
            return null;
        }
        try {
            canonicalName = name.getCanonical();
        } catch (NotesException e) {
            getMonitor().debug(RESOURCES.getString("session.cannot.abbreviate.name.1", abreviatedName), e);
            return null;
        }
        try {
            name.recycle();
        } catch (NotesException e) {
            getMonitor().debug(RESOURCES.getString("session.cannot.recycle.name.1", abreviatedName), e);
        }
        return canonicalName;
    }

    /**
     * {@inheritDoc}
     * @see DSession#getAgentContext()
     */
    public DAgentContext getAgentContext() {
        getFactory().preprocessMethod();
        try {
            final AgentContext agentContext = getSession().getAgentContext();
            return AgentContextProxy.getInstance(getFactory(), this, agentContext, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.agentcontext"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#getCurrentTime()
     */
    public Calendar getCurrentTime() {
        getFactory().preprocessMethod();
        try {
            final DateTime dateTime = getSession().createDateTime("Today");
            dateTime.setNow();
            final Calendar calendar = this.createCalendar(dateTime);
            getFactory().recycle(dateTime);
            return calendar;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.currenttime"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#isValid()
     */
    public boolean isValid() {
        getFactory().preprocessMethod();
        return getSession().isValid();
    }

    /**
     * {@inheritDoc}
     * @see DSession#getNotesVersion()
     */
    public String getNotesVersion() {
        try {
            return this.getSession().getNotesVersion();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.notesversion"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#getPlatform()
     */
    public String getPlatform() {
        try {
            return this.getSession().getPlatform();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.notesplatform"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#createDxlExporter()
     */
    public DDxlExporter createDxlExporter() throws DNotesException {
        try {
            final DxlExporter dxlExporter = this.getSession().createDxlExporter();
            return DxlExporterProxy.getInstance(getFactory(), this, dxlExporter, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.create.dxlexporter"), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>The two methods {@link #getMailServer()()} and {@link #getMailDatabaseName()()}
     * are synchronized to prevent double-reading of mail information. The field
     * <code>fMailInfoMutex</code> is used as the locking mutex.</p>
     *
     * @see DSession#getMailServer()
     */
    public String getMailServer() {
        synchronized (fMailInfoMutex) {
            if (mailServer == null) {
                readMailInfo();
            }
            return mailServer;

        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>The two methods {@link #getMailServer()()} and {@link #getMailDatabaseName()()}
     * are synchronized to prevent double-reading of mail information. The field
     * <code>mailServer</code> is used as the locking mutex.</p>
     *
     * @see DSession#getMailDatabaseName()
     */
    public String getMailDatabaseName() {
        synchronized (fMailInfoMutex) {
            if (mailDatabaseName == null) {
                readMailInfo();
            }
            return mailDatabaseName;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DSession#getMailDatabase()
     */
    public DDatabase getMailDatabase() throws DNotesException {
        return getDSession().getDatabase(getMailServer(), getMailDatabaseName());
    }

    /**
     * {@inheritDoc}
     *
     * <p>The mail server of the current user is used as registration server.</p>
     *
     * @see DSession#getMailDatabase()
     * @see DSession#getMailDatabase(String)
     */
    public DDatabase getMailDatabase(final String username) throws DNotesException {
        return getMailDatabase(getMailServer(), username);
    }

    /**
     * <p>The two methods {@link #getMailServer()()} and {@link #getMailDatabaseName()()}
     * are synchronized to prevent double-reading of mail information. The field
     * <code>fMailInfoMutex</code> is used as the locking mutex.</p>
     *
     * @see DSession#getMailDatabase()
     */
    private DDatabase getMailDatabase(final String registrationServer, final String username) throws DNotesException {
        Registration registration = null;
        StringBuffer server = new StringBuffer();
        StringBuffer file = new StringBuffer();
        StringBuffer domain = new StringBuffer();
        StringBuffer system = new StringBuffer();
        Vector profile = new Vector();
        try {
            registration = getSession().createRegistration();
            registration.setRegistrationServer(registrationServer);
            registration.getUserInfo(username, server, file, domain, system, profile);
        } catch (NotesException e) {
            getMonitor().warn("Cannot access registration", e);
        } finally {
            if (registration != null) {
                try {
                    registration.recycle();
                } catch (NotesException e) {
                    getMonitor().warn("Cannot recycle registration", e);
                }
            }
        }
        return getDSession().getDatabase(server.toString(), file.toString());
    }

    /**
     * {@inheritDoc}
     * @see DSession#getMailDomain()
     */
    public String getMailDomain() {
        if (mailDomain == null) {
            readMailDomain();
        }
        return mailDomain;
    }

    /**
     * {@inheritDoc}
     * @see DSession#resolve(String)
     */
    public DBase resolve(final String url) {
        getFactory().preprocessMethod();
        try {
            Base base = getSession().resolve(url);
            if (base instanceof Database) {
                return DatabaseProxy.getInstance(getFactory(), this, (Database) base, getMonitor(), false);
            } else if (base instanceof View) {
                Database notesDatabase = ((View) base).getParent();
                DatabaseProxy database = (DatabaseProxy) getDatabaseProxy(notesDatabase);
                return ViewProxy.getInstance(getFactory(), database, (View) base, getMonitor());
            } else if (base instanceof Form) {
                getMonitor().warn("resolving URLs to forms is not yet supported");
                // TODO support class DForm
                return null;
            } else if (base instanceof Agent) {
                Database notesDatabase = ((Agent) base).getParent();
                DatabaseProxy database = (DatabaseProxy) getDatabaseProxy(notesDatabase);
                return AgentProxy.getInstance(getFactory(), database, (Agent) base, getMonitor());
            } else if (base instanceof Document) {
                return DatabaseProxy.getInstance(getFactory(), this, (Database) base, getMonitor(), false);
            } else {
                return null;
            }
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.resolve.url.1", url), e);
        } catch (DNotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.resolve.url.1", url), e);
        }
    }

    /**
     * Reads and caches both, the mail server and the mail database name
     * of the current user.
     */
    private void readMailInfo() {
        String server = getServerName();
        // TODO check if this test is ok... shouldn't we check if session.isOnServer?
        if (server == null || server.length() == 0) {
            readLocalMailInfo();
        } else {
            readServerMailInfo();
        }
    }

    /**
     * Reads the location of the current mail file from the location
     * that is currently selected in the Notes client.
     */
    private void readLocalMailInfo() {
        String locationValue = getEnvironmentString("Location", true);
        if (locationValue == null || locationValue.length() == 0) {
            readServerMailInfo();
            return;
        }
        String locationNoteId = parseLocationNoteId(locationValue);
        try {
            DDatabase database = getDatabase("", "names.nsf");
            DDocument location = database.getDocumentByID(locationNoteId);
            String mailType = location.getItemValueString("MailType");
            mailDatabaseName = location.getItemValueString("MailFile");
            if ("1".equals(mailType)) { // local mail file
                mailServer = "";
            } else {
                mailServer = location.getItemValueString("MailServer");
            }
        } catch (DNotesException e) {
            throw new NotesProxyRuntimeException("Cannot use local names.nsf", e);
        }
    }

    /**
     * Extracts the note-id of the current location from a location value as
     * used in the file <code>notes.ini</code>.
     * <p>The note-id is between the last two commas in the string.</p>
     * <p>Example from notes.ini: <code>Location=Office (Network),9EA,CN=Kurt Riede/O=acme</code></p>
     *
     * @param locationValue
     * @return note-Id of location value
     */
    static String parseLocationNoteId(final String locationValue) {
        int pos2 = locationValue.lastIndexOf(',');
        if (pos2 < 0) {
            throw new IllegalArgumentException("invalid location value: " + locationValue);
        }
        int pos1 = locationValue.lastIndexOf(',', pos2 - 1);
        if (pos2 < 0) {
            throw new IllegalArgumentException("invalid location value: " + locationValue);
        }
        return locationValue.substring(pos1 + 1, pos2);
    }

    private void readServerMailInfo() {
        // TODO get mail info Registration from Session
        Vector vector;
        try {
            vector = this.getSession().evaluate("@MailDbName");
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.read.mailinfo"));
        }
        if (vector.size() == 0) {
            throw newRuntimeException("Cannot get mail server");
        }
        mailServer = (String) vector.get(0);
        if (vector.size() < 2) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.maildatabase"));
        }
        mailDatabaseName = (String) vector.get(1);
    }

    private void readMailDomain() {
        Vector vector;
        try {
            vector = this.getSession().evaluate("@Domain");
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get mail domain", e);
        }
        if (vector.size() < 2) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.maildomain"));
        }
        mailDomain = (String) vector.get(1);
    }

    /**
     * {@inheritDoc}
     * @see DSession#getServerName()
     */
    public String getServerName() {
        try {
            return getSession().getServerName();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("session.cannot.get.servername"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DSession#setTimeZone(java.util.TimeZone)
     */
    public void setTimeZone(final TimeZone zone) {
        fZone = zone;
    }

    /**
     * {@inheritDoc}
     * @see DSession#getTimeZone()
     */
    public TimeZone getTimeZone() {
        return fZone;
    }
}
