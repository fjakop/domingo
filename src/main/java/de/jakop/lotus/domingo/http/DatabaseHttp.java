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

package de.jakop.lotus.domingo.http;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.jakop.lotus.domingo.DAgent;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DForm;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DProfileDocument;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DView;

/**
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DatabaseHttp extends BaseHttp implements DDatabase {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1484836582323425207L;

    private String fFilePath;

    private String fReplicaId;

    /**
     * Private Constructor for this class.
     *
     * @param factory the controlling factory
     * @param session the session that produced the database
     * @param database Notes database object
     * @param monitor the monitor that handles logging
     * @param forceOpen whether the database should be forced to be open or not
     * @see lotus.domino.Database
     */
    private DatabaseHttp(final NotesHttpFactory factory, final DBase parent, final String filePath,
            final DNotesMonitor monitor) {
        super(factory, parent, monitor);
        fFilePath = filePath.replace('\\', '/');
        readDatabase();
    }

    /**
     * Factory method for instances of this class.
     *
     * @param factory the controlling factory
     * @param parent the session that produced the database
     * @param filePath file/path to the Notes database
     * @param monitor the monitor that handles logging
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static DDatabase getInstance(final NotesHttpFactory factory, final DBase parent, final String filePath,
            final DNotesMonitor monitor) {
        return new DatabaseHttp(factory, parent, filePath, monitor);
    }

    /**
     * Creates a connection to the server and reads necessary database
     * properties.
     */
    private void readDatabase() {
        // TODO create a connection to the server and read necessary database
        // TODO do we reallly need this?
        // properties
    }

    /**
     * @see java.lang.Object#toString()
     * @return the file/path
     */
    public String toString() {
        return fFilePath;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getSession()
     */
    public DSession getSession() {
        return (DSession) getParent();
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#replicate(java.lang.String)
     */
    public boolean replicate(final String server) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#remove()
     */
    public boolean remove() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getAllDocuments()
     */
    public Iterator getAllDocuments() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getDocumentByUNID(java.lang.String)
     */
    public DDocument getDocumentByUNID(final String universalId) {
        if (universalId != null && universalId.length() > 0) {
            return DocumentHttp.getInstance(getFactory(), getParent(), universalId, getMonitor());
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getDocumentByID(java.lang.String)
     */
    public DDocument getDocumentByID(final String noteId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getProfileDocument(java.lang.String,
     *      java.lang.String)
     */
    public DProfileDocument getProfileDocument(final String profileName, final String profileKey) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#createDocument()
     */
    public DDocument createDocument() {
        return DocumentHttp.getInstance(getFactory(), this, getMonitor());
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getView(java.lang.String)
     */
    public DView getView(final String viewName) {
        return ViewHttp.getInstance(getFactory(), this, viewName, getMonitor());
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getFilePath()
     */
    public String getFilePath() {
        return fFilePath;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getFileName()
     */
    public String getFileName() {
        final int pos = fFilePath.indexOf("/");
        if (pos >= 0) {
            return fFilePath.substring(pos + 1);
        }
        return fFilePath;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#createDatabaseFromTemplate(java.lang.String,
     *      java.lang.String, boolean)
     */
    public DDatabase createDatabaseFromTemplate(final String serverName, final String databaseName, final boolean inherit)
            throws DNotesException {
        final String templatePath = fFilePath;
        final String path = databaseName.replace('\\', '/');
        final String bs;
        try {
            bs = execute("cmd=CreateDatabaseFromTemplate&template=" + templatePath + "&file=" + path);
        } catch (IOException e) {
            throw new NotesHttpRuntimeException("Cannot create database from template: " + path, e);
        }
        if (bs.indexOf("Database created") >= 0) {
            return DatabaseHttp.getInstance(getFactory(), this, databaseName, getMonitor());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#createReplica(java.lang.String,
     *      java.lang.String)
     */
    public DDatabase createReplica(final String server, final String fPath) throws DNotesException {
        final String databasePath = fFilePath.replace('\\', '/');
        final String path = fPath.replace('\\', '/');
        final String bs;
        try {
            bs = execute("cmd=CreateReplica&database=" + databasePath + "&file=" + path);
        } catch (IOException e) {
            throw new NotesHttpRuntimeException("Cannot create replica: " + path, e);
        }
        if (bs.indexOf("Database created") >= 0) {
            return DatabaseHttp.getInstance(getFactory(), this, path, getMonitor());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getCurrentAccessLevel()
     */
    public int getCurrentAccessLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getServer()
     */
    public String getServer() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getAgent(java.lang.String)
     */
    public DAgent getAgent(final String name) {
        return AgentHttp.getInstance(getFactory(), this, getMonitor(), name);
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#isPublicAddressBook()
     */
    public boolean isPublicAddressBook() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#isPrivateAddressBook()
     */
    public boolean isPrivateAddressBook() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#isOpen()
     */
    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#open()
     */
    public boolean open() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getCategories()
     */
    public String getCategories() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#isDelayUpdates()
     */
    public boolean isDelayUpdates() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getReplicaID()
     */
    public String getReplicaID() {
        return fReplicaId;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getSizeQuota()
     */
    public int getSizeQuota() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getTemplateName()
     */
    public String getTemplateName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getTitle()
     */
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#setTitle(String)
     */
    public void setTitle(final String title) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getDesignTemplateName()
     */
    public String getDesignTemplateName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#search(java.lang.String,
     *      java.util.Calendar, int)
     */
    public Iterator search(final String formula, final Calendar dt, final int max) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#search(java.lang.String,
     *      java.util.Calendar)
     */
    public Iterator search(final String formula, final Calendar dt) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#search(java.lang.String)
     */
    public Iterator search(final String formula) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#isFTIndexed()
     */
    public boolean isFTIndexed() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#updateFTIndex(boolean)
     */
    public void updateFTIndex(final boolean create) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#fullTextSearchRange(java.lang.String, int,
     *      int, int, int)
     */
    public Iterator fullTextSearchRange(final String query, final int max, final int sortopt, final int otheropt,
            final int start) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#fullTextSearch(java.lang.String)
     */
    public Iterator fullTextSearch(final String query) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#fullTextSearch(java.lang.String, int)
     */
    public Iterator fullTextSearch(final String query, final int max) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#fullTextSearch(java.lang.String, int, int,
     *      int)
     */
    public Iterator fullTextSearch(final String query, final int max, final int sortopt, final int otheropt) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#isDocumentLockingEnabled()
     */
    public boolean isDocumentLockingEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#setDocumentLockingEnabled(boolean)
     */
    public void setDocumentLockingEnabled(final boolean flag) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getFolderReferencesEnabled()
     */
    public boolean getFolderReferencesEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#setFolderReferencesEnabled(boolean)
     */
    public void setFolderReferencesEnabled(final boolean flag) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DDatabase#getViews()
     */
    public List getViews() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getForm(java.lang.String)
     */
    public DForm getForm(final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see DDatabase#getForms()
     */
    public List getForms() {
        // TODO Auto-generated method stub
        return null;
    }
}
