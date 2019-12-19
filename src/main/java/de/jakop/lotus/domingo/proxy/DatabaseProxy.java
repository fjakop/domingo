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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import lotus.domino.Agent;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Form;
import lotus.domino.NotesException;
import lotus.domino.View;
import de.jakop.lotus.domingo.DAgent;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DForm;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DProfileDocument;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.cache.Cache;
import de.jakop.lotus.domingo.cache.SimpleCache;

/**
 * Represents a Notes database.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DatabaseProxy extends BaseProxy implements DDatabase {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3834028048088380976L;

    /**
     * Cache of all views of a database.
     *
     * <p>viewName -> WeakReference(View)</p>
     */
    private final Cache viewCache;

    /** Flag, if the Notes database was found and is open. */
    private boolean databaseNotFound;

    /** The file/path of the database for fast access. */
    private String filePath;

    /** The server of the database for fast access. */
    private String server;

    /**
     * Private Constructor for this class.
     *
     * @param theFactory the controlling factory
     * @param session the session that produced the database
     * @param database Notes database object
     * @param monitor the monitor that handles logging
     * @param forceOpen whether the database should be forced to be open or not
     * @see lotus.domino.Database
     */
    private DatabaseProxy(final NotesProxyFactory theFactory, final DSession session,
                          final Database database, final DNotesMonitor monitor, final boolean forceOpen) {
        super(theFactory, session, database, monitor);
        this.viewCache = new SimpleCache();
        getFactory().preprocessMethod();
        server = getServerIntern();
        filePath = getFilePathIntern();
        try {
            if (forceOpen) {
                if (!database.isOpen()) {
                    try {
                        database.open();
                    } catch (NotesException e) {
                        monitor.warn(RESOURCES.getString("database.tried.open.database"), e);
                    }
                }
                if (!database.isOpen()) {
                    getMonitor().error(RESOURCES.getString("database.database.not.open.1", toString()));
                    databaseNotFound = true;
                }
            }
        } catch (NotesException e) {
            databaseNotFound = true;
            monitor.error(RESOURCES.getString("database.cannot.get.database.1", toString()), e);
        }
    }

    /**
     * Factory method for instances of this class.
     *
     * @param theFactory the controlling factory
     * @param session the session that produced the database
     * @param theDatabase Notes database object
     * @param monitor the monitor that handles logging
     * @param forceOpen whether the database should be forced to be open or not
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static DDatabase getInstance(final NotesProxyFactory theFactory, final DSession session,
                                 final Database theDatabase, final DNotesMonitor monitor, final boolean forceOpen) {
        if (theDatabase == null) {
            return null;
        }
        DatabaseProxy databaseProxy = (DatabaseProxy) theFactory.getBaseCache().get(theDatabase);
        if (databaseProxy == null) {
            databaseProxy = new DatabaseProxy(theFactory, session, theDatabase, monitor, forceOpen);
            if (databaseProxy.databaseNotFound) {
                return null;
            }
            theFactory.getBaseCache().put(theDatabase, databaseProxy);
        }
        try {
            final String key1 = getDatabaseKey(databaseProxy.getServer(), databaseProxy.getFilePath());
            final String key2 = getDatabaseKey(theDatabase.getServer(), theDatabase.getFilePath());
            if (!key1.equals(key2)) {
                monitor.warn(RESOURCES.getString("database.invalid.database.proxy.2", key2, key1));
                return new DatabaseProxy(theFactory, session, theDatabase, monitor, forceOpen);
            }
        } catch (NotesException e) {
            monitor.fatalError(RESOURCES.getString("database.cannot.get.database.proxy.instance"), e);
        }
        return databaseProxy;
    }

    /**
     * Returns the database key for a given server and filename.
     * To ensure platform independence, path separators get unified to <tt>'/'</tt>.
     *
     * @param serverName name of a notes server
     * @param databaseName file path of database
     * @return database key that can be used for caching database objects in a map.
     */
    static String getDatabaseKey(final String serverName, final String databaseName) {
        if (databaseName == null) {
            return serverName;
        }
        String server = ("".equals(serverName) ? "local" : serverName);
        return server + "!!" + databaseName.replace('\\', '/');
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getSession()
     */
    public DSession getSession() {
        return (DSession) getParent();
    }

    /**
     * Returns the associated notes database object.
     *
     * @return notes database object
     */
    private Database getDatabase() {
        return (Database) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#replicate(java.lang.String)
     */
    public boolean replicate(final String targetServer) {
        getFactory().preprocessMethod();
        try {
            return getDatabase().replicate(targetServer);
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.replicate.database"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#remove()
     */
    public boolean remove() {
        getFactory().preprocessMethod();
        // todo shrink thread pool size to one before removing database, then resize pool again
        // (Otherwise the database might be locked by another thread and thus removal fails)
        try {
            getDatabase().remove();
            return true;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.remove.database.1", getFileName()), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getDocumentByUNID(String)
     * todo check if it is possible and helpfull to reuse existing instances of a document
     */
    public DDocument getDocumentByUNID(final String docId) {
        getFactory().preprocessMethod();
        try {
            final Document doc = getDatabase().getDocumentByUNID(docId);
            if (doc.isProfile()) {
                final String message = RESOURCES.getString("database.cannot.query.profile");
                getMonitor().error(message);
                return null;
            }
            return (DDocument) BaseDocumentProxy.getInstance(getFactory(), this, doc, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.unid.1", docId), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getProfileDocument(String, String)
     */
    public DProfileDocument getProfileDocument(final String profileName, final String profileKey) {
        getFactory().preprocessMethod();
        try {
            final Document document = getDatabase().getProfileDocument(profileName, profileKey);
            return (DProfileDocument) BaseDocumentProxy.getInstance(getFactory(), this, document, getMonitor());
        } catch (NotesException e) {
            String key = profileName + "_" + profileKey;
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.profile.1", key), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getView(String)
     */
    public DView getView(final String viewName) {
        getFactory().preprocessMethod();
        final WeakReference weakReference = (WeakReference) viewCache.get(viewName);
        final DView cachedView = (weakReference == null) ? null : (DView) weakReference.get();
        if (cachedView != null) {
            return cachedView;
        }
        try {
            final View notesView = getDatabase().getView(viewName);
            final DView view = ViewProxy.getInstance(getFactory(), this, notesView, getMonitor());
            viewCache.put(viewName, new WeakReference(view));
            return view;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.view.1", viewName), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#createDocument()
     */
    public DDocument createDocument() {
        getFactory().preprocessMethod();
        try {
            final Document doc = getDatabase().createDocument();
            return (DDocument) BaseDocumentProxy.getInstance(getFactory(), this, doc, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.create.document"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getDocumentByID(java.lang.String)
     * todo check if it is possible and helpfull to reuse existing instances of a document
     */
    public DDocument getDocumentByID(final String noteId) {
        getFactory().preprocessMethod();
        try {
            final Document doc = getDatabase().getDocumentByID(noteId);
            return (DDocument) BaseDocumentProxy.getInstance(getFactory(), this, doc, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.documentbyid.1", noteId), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getFilePath()
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @see DDatabase#getFilePath()
     */
    private String getFilePathIntern() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getFilePath();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.filepath"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getFileName()
     */
    public String getFileName() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getFileName();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.filename"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return server + "!!" + filePath;
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getAllDocuments()
     */
    public Iterator getAllDocuments() {
        getFactory().preprocessMethod();
        try {
            return new DocumentCollectionIterator(getFactory(), this, getDatabase().getAllDocuments(), getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.alldocuments"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#createDatabaseFromTemplate(java.lang.String, java.lang.String, boolean)
     */
    public DDatabase createDatabaseFromTemplate(final String serverName, final String dbName, final boolean inherit)
        throws DNotesException {
        getFactory().preprocessMethod();
        try {
            final Database newDatabase = getDatabase().createFromTemplate(serverName, dbName, inherit);
            final DSession session = (DSession) getParent();
            return getInstance(getFactory(), session, newDatabase, getMonitor(), true);
        } catch (NotesException e) {
            String templ = getServer() + "!!" + getFileName();
            String db = serverName + "!!" + dbName;
            String msg = RESOURCES.getString("database.cannot.create.database.from.template.2", db, templ);
            throw newException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#createReplica(java.lang.String, java.lang.String)
     */
    public DDatabase createReplica(final String serverName, final String dbName) throws DNotesException {
        getFactory().preprocessMethod();
        try {
            final Database newReplica = getDatabase().createReplica(serverName, dbName);
            final DSession session = (DSession) getParent();
            return getInstance(getFactory(), session, newReplica, getMonitor(), true);
        } catch (NotesException e) {
            throw newRuntimeException(e.text, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getCurrentAccessLevel()
     */
    public int getCurrentAccessLevel() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getCurrentAccessLevel();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.accesslevel"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getServer()
     */
    public String getServer() {
        return server;
    }

    /**
     * @see DDatabase#getServer()
     */
    private String getServerIntern() {
        getFactory().preprocessMethod();
        try {
            String s = getDatabase().getServer();
            if (s == null || "".equals(s)) {
                s = "local";
            }
            return s;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.server"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getAgent(java.lang.String)
     */
    public DAgent getAgent(final String name) {
        getFactory().preprocessMethod();
        try {
            final Agent agent = getDatabase().getAgent(name);
            return AgentProxy.getInstance(getFactory(), this, agent, getMonitor());

        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.agent.1", name), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#isPublicAddressBook()
     */
    public boolean isPublicAddressBook() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().isPublicAddressBook();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#isPrivateAddressBook()
     */
    public boolean isPrivateAddressBook() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().isPrivateAddressBook();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#isOpen()
     */
    public boolean isOpen() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().isOpen();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#open()
     */
    public boolean open() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().open();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getCategories()
     */
    public String getCategories() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getCategories();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#isDelayUpdates()
     */
    public boolean isDelayUpdates() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().isDelayUpdates();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getReplicaID()
     */
    public String getReplicaID() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getReplicaID();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getSizeQuota()
     */
    public int getSizeQuota() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getSizeQuota();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getTemplateName()
     */
    public String getTemplateName() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getTemplateName();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getTitle()
     */
    public String getTitle() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getTitle();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#setTitle(java.lang.String)
     */
    public void setTitle(final String title) {
        getFactory().preprocessMethod();
        try {
            getDatabase().setTitle(title);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setTitle", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getDesignTemplateName()
     */
    public String getDesignTemplateName() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getDesignTemplateName();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#search(String, Calendar, int)
     */
    public Iterator search(final String formula) {
        getFactory().preprocessMethod();
        try {
            return new DocumentCollectionIterator(getFactory(), this, getDatabase().search(formula), getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.search.documents.1", formula), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#search(java.lang.String, java.util.Calendar, int)
     */
    public Iterator search(final String formula, final Calendar dt, final int max) {
        getFactory().preprocessMethod();
        try {
            final DateTime dateTime = (dt == null) ? null : createDateTime(dt);
            final DocumentCollection collection = getDatabase().search(formula, dateTime, max);
            final Iterator iterator = new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
            getFactory().recycle(dateTime);
            return iterator;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.search.documents.1", formula), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#search(java.lang.String, java.util.Calendar)
     */
    public Iterator search(final String formula, final Calendar dt) {
        getFactory().preprocessMethod();
        try {
            final DateTime dateTime = createDateTime(dt);
            final DocumentCollection collection = getDatabase().search(formula, dateTime);
            final Iterator iterator = new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
            getFactory().recycle(dateTime);
            return iterator;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.search.documents.1", formula), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#isFTIndexed()
     */
    public boolean isFTIndexed() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().isFTIndexed();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#updateFTIndex(boolean)
     */
    public void updateFTIndex(final boolean create) {
        getFactory().preprocessMethod();
        try {
            getDatabase().updateFTIndex(create);
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#fullTextSearchRange(java.lang.String, int, int, int, int)
     */
    public Iterator fullTextSearchRange(final String query, final int max, final int sortopt, final int otheropt,
            final int start) {
        getFactory().preprocessMethod();
        try {
            DocumentCollection collection = getDatabase().FTSearchRange(query, max, sortopt, otheropt, start);
            return new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.ftsearch.documents.1", query), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#fullTextSearch(java.lang.String)
     */
    public Iterator fullTextSearch(final String query) {
        getFactory().preprocessMethod();
        try {
            DocumentCollection collection = getDatabase().FTSearch(query);
            return new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.ftsearch.documents.1", query), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#fullTextSearch(java.lang.String, int)
     */
    public Iterator fullTextSearch(final String query, final int max) {
        getFactory().preprocessMethod();
        try {
            DocumentCollection collection = getDatabase().FTSearch(query, max);
            return new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.ftsearch.documents.1", query), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#fullTextSearch(java.lang.String, int, int, int)
     */
    public Iterator fullTextSearch(final String query, final int max, final int sortopt, final int otheropt) {
        getFactory().preprocessMethod();
        try {
            DocumentCollection collection = getDatabase().FTSearch(query, max, sortopt, otheropt);
            return new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.ftsearch.documents.1", query), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#isDocumentLockingEnabled()
     */
    public boolean isDocumentLockingEnabled() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().isDocumentLockingEnabled();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#setDocumentLockingEnabled(boolean)
     */
    public void setDocumentLockingEnabled(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getDatabase().setDocumentLockingEnabled(flag);
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getFolderReferencesEnabled()
     */
    public boolean getFolderReferencesEnabled() {
        getFactory().preprocessMethod();
        try {
            return getDatabase().getFolderReferencesEnabled();
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#setFolderReferencesEnabled(boolean)
     */
    public void setFolderReferencesEnabled(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getDatabase().setFolderReferencesEnabled(flag);
        } catch (NotesException e) {
            throw newRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getViews()
     */
    public List getViews() {
        getFactory().preprocessMethod();
        try {
            Vector views = getDatabase().getViews();
            return convertNotesList(views);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getViews", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getForm(java.lang.String)
     */
    public DForm getForm(final String formName) {
        getFactory().preprocessMethod();
        try {
            final Form notesForm = getDatabase().getForm(formName);
            return FormProxy.getInstance(getFactory(), this, notesForm, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("database.cannot.get.from.1", formName), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getForms()
     */
    public List getForms() {
        getFactory().preprocessMethod();
        try {
            Vector forms = getDatabase().getForms();
            return convertNotesList(forms);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getForms", e);
        }
    }

    /**
     * Converts a list of notes views into a list of corresponding domingo views.
     *
     * @param list list of notes views
     * @return list of corresponding domingo views
     */
    private List convertNotesList(final List list) {
        if (list == null) {
            return null;
        }
        final List result = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            final Object object = list.get(i);
            if (object instanceof View) {
                result.add(ViewProxy.getInstance(getFactory(), this, (View) object, getMonitor()));
            } else if (object instanceof Form) {
                result.add(FormProxy.getInstance(getFactory(), this, (Form) object, getMonitor()));
            } else {
                result.add(object);
            }
        }
        return Collections.unmodifiableList(result);
    }
}
