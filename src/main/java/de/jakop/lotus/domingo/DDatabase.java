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

package de.jakop.lotus.domingo;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a Notes database.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DDatabase extends DBase {

    /**
     * Sorts by relevance score. When a collection is sorted by relevance, the
     * highest relevance appears first. To access the relevance score of each
     * document in the collection, use the {@link DDocument#getFTSearchScore()}
     * property in Document.
     */
    int FT_SCORES = 8;

    /** Sorts by document creation date in descending order. */
    int FT_DATE_DESCENDING = 32;

    /** Sorts by document creation date in ascending order. */
    int FT_DATE_ASCENDING = 64;

    /** Sorts by document date in descending order. */
    int FT_DATECREATED_DESCENDING = 1542;

    /** Sorts by document date in ascending order. */
    int FT_DATECREATED_ASCENDING = 1543;

    /** Specifies a fuzzy search. */
    int FT_FUZZY = 16384;

    /** Uses stem words as the basis of the search. */
    int FT_STEMS = 512;

    /**
     * The Notes session that contains a database.
     * @return the Notes session that contains a database.
     */
    DSession getSession();

    /**
     * Replicates a local database with its replica(s) on a server.
     * @param server The name of the server with which you want to replicate.
     *        Any replica of the source database that exists on the server
     *        will replicate.
     * @return <code>true</code> if the database replicates without errors
     *         <code>false</code> if replication errors occur
     */
    boolean replicate(String server);

    /**
     * Permanently deletes the database.
     *
     * @return <code>true</code> if the database was removed successfully, else <code>false</code>
     */
    boolean remove();

    /**
     * Returns an An unsorted collection containing all the documents in a
     * database.
     *
     * @return Iterator
     */
    Iterator getAllDocuments();

    /**
     * Finds a document in a database, given the document universal ID (UNID).
     *
     * <p>If the document document is already loaded, this method might return
     * the same instance of the document or a new instance representing the
     * same document. Mostly via local call, the same instance is returned and
     * mostly via remote calls a new instance is returned. Your code should not
     * rely on this behaviour.</p>
     *
     * @param docId Universal ID of a document
     * @return DDocument or <code>null</code> if the document was not found
     */
    DDocument getDocumentByUNID(String docId);

    /**
     * Finds a document in a database, given the document note ID.
     *
     * <p>If the document document is already loaded, this method might return
     * the same instance of the document or a new instance representing the
     * same document. Mostly via local call, the same instance is returned and
     * mostly via remote calls a new instance is returned. Your code should not
     * rely on this behaviour.</p>
     *
     * @param noteId NoteID
     * @return DDocument
     */
    DDocument getDocumentByID(String noteId);

    /**
     * Retrieves or creates a profile document.
     *
     * @param profileName name of a profile
     * @param profileKey The unique key associated with the profile document.
     *      Usually the userName. Can be <code>null</code> which indicates that
     *      the returned document is a public profile.
     * @return DProfileDocument or <code>null</code> if the document was not found
     */
    DProfileDocument getProfileDocument(String profileName, String profileKey);

    /**
     * Creates a document in a database and returns a Document object that
     * represents the new document.
     *
     * @return DDocument
     */
    DDocument createDocument();

    /**
     * Finds a view or folder in a database, given the name or alias of the view
     * or folder.
     *
     * @param viewName name of a view
     * @return DView
     */
    DView getView(String viewName);

    /**
     * The path and file name of a database.
     *
     * @return String
     */
    String getFilePath();

    /**
     * The file name of a database, excluding the path.
     *
     * @return String
     */
    String getFileName();

    /**
     * Create an empty database with the design of the given template.
     *
     * @param serverName notes server name
     * @param newDatabaseName notes database filename
     * @param inherit Specify <code>true</code> if you want the new database to
     *        inherit future design changes from the template;
     *        otherwise, specify false.
     * @return DDatabase
     * @throws DNotesException if current database is not a template
     */
    DDatabase createDatabaseFromTemplate(String serverName, String newDatabaseName, boolean inherit)
        throws DNotesException;

    /**
     * Creates a replica of the current database at a new location.
     * The new replica has an identical access control list.
     *
     * <p><b>Usage</b><br/>
     * If a database with the specified file name already exists, an exception
     * is thrown. Programs running on a server or making remote (IIOP) calls
     * to a server can't create or access databases on other servers.
     * In these cases, the server parameter must correspond to the server the
     * program is running on. There are two ways to do this:
     * <ul>
     * <li>Use null or an empty string ("") to indicate the current computer.
     * This is the safer method.</li>
     * <li>Make sure the name of the server that the program runs on matches
     * the name of the server parameter.</li>
     * </ul>
     * </p>
     * <p>Programs running on a workstation can access several different
     * servers in a single program.</p>
     *
     * @param server The name of the server where the replica will reside.
     *          Specify null or an empty string ("") to create a replica
     *          on the current computer.
     * @param filePath The file name of the replica.
     * @return The new replica
     * @throws DNotesException if creation of the new replica failed
     */
    DDatabase createReplica(String server, String filePath) throws DNotesException;

    /**
     * The current user's access level to a database.
     * <p>If a program runs on a workstation or is remote (IIOP),
     * CurrentAccessLevel is determined by the access level of the current
     * user.</p>
     * <p>If a program runs on a server, CurrentAccessLevel is determined by the
     * access level of the person who last saved the program (the owner).</p>
     *
     * <p><b>Legal values:</b><br/>
     * <pre>DACL.LEVEL_AUTHOR
     * DACL.LEVEL_DEPOSITOR
     * DACL.LEVEL_DESIGNER
     * DACL.LEVEL_EDITOR
     * DACL.LEVEL_MANAGER
     * DACL.LEVEL_NOACCESS
     * DACL.LEVEL_READER</pre>
     * </p>
     * @return access level
     *
     * @see DACL
     */
    int getCurrentAccessLevel();

    /**
     * The name of the server where a database resides.
     * <p>If the database is on a workstation, the property returns an empty
     * string.</p>
     *
     * @return name of the server
     */
    String getServer();

    /**
     * Finds an agent in a database, given the agent name.
     *
     * <p>The return value is null if the current user (as obtained by
     * <code>Session.getUserName</code>) is not the owner of the private agent.</p>
     *
     * @param name the name of the agent.
     * @return the agent whose name matches the parameter.
     */
    DAgent getAgent(String name);

    /**
     * Indicates if a database is a Domino Directory.
     *
     * <p>This property is available for Database objects retrieved by
     * {@link DSession#getAddressBooks()} in
     * {@link DSession}. For other Database objects,
     * this property has no value and evaluates to false.
     *
     * @return <code>true</code> if the database is a Domino Directory,
     *         <code>false</code> if the database is not a Domino Directory
     */
    boolean isPublicAddressBook();

    /**
     * Indicates if a database is a Personal Address Book.
     *
     * <p>This property is available for Database objects retrieved by
     * {@link DSession#getAddressBooks()} in
     * {@link DSession}. For other Database objects,
     * this property has no value and evaluates to false.
     *
     * @return <code>true</code> if the database is a Personal Address Book,
     *         <code>false</code> if the database is not Personal Address Book
     */
    boolean isPrivateAddressBook ();

    /**
     * Indicates whether a database is open.
     *
     * <p><b>Usage</b></p>
     * <p>A database must be open to use the Database methods except:
     * {@link DDatabase#getCategories()},
     * {@link DDatabase#isDelayUpdates()},
     * {@link DDatabase#getDesignTemplateName()},
     * {@link DDatabase#getFileName()},
     * {@link DDatabase#getFilePath()},
     * {@link DDatabase#isOpen()},
     * {@link DDatabase#isPrivateAddressBook()},
     * {@link DDatabase#isPublicAddressBook()},
     * {@link DDatabase#getSession()},
     * {@link DDatabase#getReplicaID()},
     * {@link DDatabase#getServer()},
     * {@link DDatabase#getSizeQuota()},
     * {@link DDatabase#getTemplateName()}, and
     * {@link DDatabase#getTitle()}.</p>
     * <p>The following methods do not open a database:
     * DbDirectory.getFirstDatabase, DbDirectory.getNextDatabase, and Session.getAddressBooks.
     * You must explicitly call Database.open.</p>
     *
     * @return <code>true</code> if the database is open,
     *         <code>false</code> if the database is not open
     */
    boolean isOpen();

    /**
     * Opens a database.
     *
     * <p>A database must be open to use the Database properties and methods
     * with some exceptions. Most methods that access a database open it,
     * but some do not. See isOpen for details.</p>
     * <p>An error is returned if the user does not have access rights to the
     * database or server.</p>
     *
     * @return <code>true</code> if the database exists and is opened,
     *         <code>false</code> if no database with this name exists
     */
    boolean open();

    /**
     * The categories under which a database appears in the Database Library.
     * Multiple categories are separated by a comma or semicolon.
     *
     * @return categories as a string
     */
    String getCategories();

    /**
     * Indicates whether updates to a server are delayed (batched) for better
     * performance.
     *
     * <p>If DelayUpdates is false, the program waits for updates to the server
     * to be posted. If you set DelayUpdates to true, server updates are cached
     * and the program proceeds immediately. At a convenient time, the cached
     * updates are posted. This makes for better performance but risks losing
     * the cached updates in the event of a crash. This method applies to save
     * and remove operations on documents.</p>
     * <p>Set this property each time you open a database. The property is not
     * saved.</p>
     *
     * @return <code>true</code> if server delays updates,
     *         <code>false</code> if server posts updates immediately
     */
    boolean isDelayUpdates();

    /**
     * A 16-digit hexadecimal number that represents the replica ID of a Notes
     * database. Databases with the same replica ID are replicas of one another.
     *
     * @return replica ID
     */
    String getReplicaID();

    /**
     * The size quota of a database, in kilobytes.
     *
     * <p>The size quota for a database specifies the amount of disk space that
     * the server administrator is willing to provide for the database.
     * Therefore, the SizeQuota property can only be set by a program that has
     * administrator access to the server on which the database resides. The
     * size quota is not the same as the size limit that a user specifies when
     * creating a new database.</p>
     * <p>If the database has no size quota, this property returns 0.</p>
     *
     * @return size quota of a database in kilobytes
     */
    int getSizeQuota();

    /**
     * The template name of a database, if the database is a template.
     * If the database is not a template, returns an empty string.
     *
     * @return template name
     */
    String getTemplateName();

    /**
     * The title of a database.
     *
     * The database does not need to be open to use this property.
     *
     * @return database title.
     */
    String getTitle();

    /**
     * Sets the title of a database.
     *
     * A program cannot change the title of the database in which the program is currently running.
     * The database does not need to be open to use this property.
     *
     * @param title new database title
     */
    void setTitle(String title);

    /**
     * The name of the design template from which a database inherits its
     * design. If the database does not inherit its design from a design
     * template, returns an empty string.
     *
     * <p>If a database inherits a specific design element (such as a form)
     * but not its entire design from a template, this property returns an
     * empty string.</p>
     *
     * @return name of the design template from which a database inherits
     *         its design
     */
    String getDesignTemplateName();

    /**
     * Given selection criteria for a document, returns all documents
     * in a database that meet the criteria.
     *
     * <p><b>Usage</b><br/>
     * This method returns a maximum of 5,000 documents by default. The
     * Notes.ini variable <tt>FT_MAX_SEARCH_RESULTS</tt> overrides this limit
     * for indexed databases or databases that are not indexed but that are
     * running an agent on the client. For a database that is not indexed and
     * is running in an agent on the server, you must set the
     * <tt>TEMP_INDEX_MAX_DOC</tt> Notes.ini variable as well.
     * The absolute maximum value is 2,147,483,647.</p>
     *
     * @param formula Notes @-function formula that specifies the selection criteria
     * @param dt A cutoff date. The method searches only documents created or
     *              modified since the cutoff date. Can be null to indicate no
     *              cutoff date.
     * @param max The maximum number of documents you want returned.
     *              Specify <tt>0</tt> to receive all matching documents
     *              (up to 5,000. See Usage section.).
     * @return An unsorted collection of documents that match the selection criteria.
     */
    Iterator search(String formula, Calendar dt, int max);

    /**
     * Given selection criteria for a document, returns all documents
     * in a database that meet the criteria.
     *
     * <p><b>Usage</b><br/>
     * This method returns a maximum of 5,000 documents by default. The
     * Notes.ini variable <tt>FT_MAX_SEARCH_RESULTS</tt> overrides this limit
     * for indexed databases or databases that are not indexed but that are
     * running an agent on the client. For a database that is not indexed and
     * is running in an agent on the server, you must set the
     * <tt>TEMP_INDEX_MAX_DOC</tt> Notes.ini variable as well.
     * The absolute maximum value is 2,147,483,647.</p>
     *
     * @param formula Notes @-function formula that specifies the selection criteria
     * @param dt A cutoff date. The method searches only documents created or
     *              modified since the cutoff date. Can be null to indicate no
     *              cutoff date.
     * @return An unsorted collection of documents that match the selection criteria.
     */
    Iterator search(String formula, Calendar dt);

    /**
     * Given selection criteria for a document, returns all documents
     * in a database that meet the criteria.
     *
     * <p><b>Usage</b><br/>
     * This method returns a maximum of 5,000 documents by default. The
     * Notes.ini variable <tt>FT_MAX_SEARCH_RESULTS</tt> overrides this limit
     * for indexed databases or databases that are not indexed but that are
     * running an agent on the client. For a database that is not indexed and
     * is running in an agent on the server, you must set the
     * <tt>TEMP_INDEX_MAX_DOC</tt> Notes.ini variable as well.
     * The absolute maximum value is 2,147,483,647.</p>
     *
     * @param formula Notes @-function formula that specifies the selection criteria
     * @return An unsorted collection of documents that match the selection criteria.
     */
    Iterator search(String formula);

    /**
     * Indicates whether or not a database has a full-text index.
     *
     * @return <code>true</code> if the database has a full-text index, else <code>false</code>
     */
    boolean isFTIndexed();

    /**
     * Updates the full-text index of a database.
     *
     * <p><b>Usage</b></p>
     * <p>An exception is thrown if you attempt to create a full-text index on a
     * database that is not local. A database must contain at least one document
     * in order for an index to be created, even if the create parameter is set
     * to true.</p>
     *
     * @param create Specify <code>true</code> if you want to create an index
     *            if none exists (valid only for local databases). Otherwise,
     *            specify <code>false</code>.
     */
    void updateFTIndex(boolean create);

    /**
     * Conducts a full-text search of all the documents in a database.
     *
     * <p><b>Usage</b></p>
     * <p>This method is the same the same as
     * {@link #fullTextSearch(String, int, int, int)} plus the start parameter. If the
     * database is not full-text indexed, this method works, but less
     * efficiently. To test for an index, use the {@link #isFTIndexed()}
     * property. To create an index on a local database, use the
     * {@link #updateFTIndex(boolean)} method. This method returns a maximum of
     * 5,000 documents by default. The Notes.ini variable
     * <code>FT_MAX_SEARCH_RESULTS</code> overrides this limit for indexed
     * databases or databases that are not indexed but that are running in an
     * agent on the client. For a database that is not indexed and is running in
     * an agent on the server, you must set the <code>TEMP_INDEX_MAX_DOC</code>
     * Notes.ini variable as well. The absolute maximum is 2,147,483,647. This
     * method searches all documents in a database. To search only documents
     * found in a particular view, use the {@link DView#fullTextSearch(String, int)}
     * method in View. To search only documents found in a particular document
     * collection, use the {@link DDocumentCollection#fullTextSearch(String, int)}
     * method in class {@link DDocumentCollection}. If you don't specify any
     * sort options, you get the documents sorted by relevance score. If you ask
     * for a sort by date, you don't get relevance scores. A Newsletter object
     * formats its doclink report with either the document creation date or the
     * relevance score, depending on the sort options you use in the document
     * collection. If the database has a multi-database index, you get a
     * multi-database search. Navigation through the resulting document
     * collection may be slow, but you can create a newsletter from the
     * collection.</p>
     *
     * <p><b>Query syntax</b></p>
     * <p>To search for a word or phrase, enter the word or phrase as is, except
     * that search keywords must be enclosed in quotes. Remember to escape
     * quotes if you are inside a literal. Wildcards, operators, and other
     * syntax are permitted. For the complete syntax rules, see "Finding
     * documents in a database" in Lotus Notes 6 Help.</p>
     *
     * @since Notes/Domino Release 6
     *
     * @param query The full-text query. See the "Query Syntax" for details
     * @param max The maximum number of documents you want returned from the
     *            query. Set this parameter to 0 to receive all matching
     *            documents
     * @param sortopt Use one of the following constants to specify a sorting
     *            option: {@link #FT_SCORES},
     *            {@link #FT_DATECREATED_DESCENDING},
     *            {@link #FT_DATE_DESCENDING} or
     *            {@link #FT_DATE_ASCENDING}
     * @param otheropt Use the following constants to specify additional search
     *            options. To specify more than one option, use a logical or
     *            operation: {@link #FT_FUZZY} or
     *            {@link #FT_STEMS}
     * @param start The starting document to return.
     * @return An iterator over documents that match the full-text query, sorted
     *         by the selected option. If no matches are found, the iterator has
     *         no elements.
     */
    Iterator fullTextSearchRange(String query, int max, int sortopt, int otheropt, int start);

    /**
     * Conducts a full-text search of all the documents in a database.
     *
     * <p>See {@link #fullTextSearch(String, int, int, int)} for more details.</p>
     *
     * @param query The full-text query. See the "Query Syntax" for details
     * @return An iterator over documents that match the full-text query, sorted
     *         by the selected option. If no matches are found, the iterator has
     *         no elements.
     */
    Iterator fullTextSearch(String query);

    /**
     * Conducts a full-text search of all the documents in a database.
     *
     * <p>See {@link #fullTextSearch(String, int, int, int)} for more details.</p>
     *
     * @param query The full-text query. See the "Query Syntax" for details
     * @param max The maximum number of documents you want returned from the
     *            query. Set this parameter to 0 to receive all matching
     *            documents (up to 5,000. See Usage)
     * @return An iterator over documents that match the full-text query, sorted
     *         by the selected option. If no matches are found, the iterator has
     *         no elements.
     */
    Iterator fullTextSearch(String query, int max);

    /**
     * Conducts a full-text search of all the documents in a database.
     *
     * <p><b>Usage</b></p> <p>This method is the same the same as
     * {@link #fullTextSearchRange(String, int, int, int, int)} minus the start
     * parameter. If the database is not full-text indexed, this method works,
     * but less efficiently. To test for an index, use the
     * {@link #isFTIndexed()} property. To create an index on a local database,
     * use the {@link #updateFTIndex(boolean)} method. This method returns a
     * maximum of 5,000 documents by default. The Notes.ini variable
     * <code>FT_MAX_SEARCH_RESULTS</code> overrides this limit for indexed
     * databases or databases that are not indexed but that are running in an
     * agent on the client. For a database that is not indexed and is running in
     * an agent on the server, you must set the <code>TEMP_INDEX_MAX_DOC</code>
     * Notes.ini variable as well. The absolute maximum is 2,147,483,647. This
     * method searches all documents in a database. To search only documents
     * found in a particular view, use the {@link DView#fullTextSearch(String, int)}
     * method in View. To search only documents found in a particular document
     * collection, use the {@link DDocumentCollection#fullTextSearch(String, int)}
     * method in class {@link DDocumentCollection}. If you don't specify any
     * sort options, you get the documents sorted by relevance score. If you ask
     * for a sort by date, you don't get relevance scores. A Newsletter object
     * formats its doclink report with either the document creation date or the
     * relevance score, depending on the sort options you use in the document
     * collection. If the database has a multi-database index, you get a
     * multi-database search. Navigation through the resulting document
     * collection may be slow, but you can create a newsletter from the
     * collection.</p>
     *
     * <p><b>Query syntax</b></p> <p>To search for a word or phrase, enter
     * the word or phrase as is, except that search keywords must be enclosed in
     * quotes. Remember to escape quotes if you are inside a literal. Wildcards,
     * operators, and other syntax are permitted. For the complete syntax rules,
     * see "Finding documents in a database" in Lotus Notes 6 Help.</p>
     *
     * @param query The full-text query. See the "Query Syntax" for details
     * @param max The maximum number of documents you want returned from the
     *            query. Set this parameter to 0 to receive all matching
     *            documents (up to 5,000. See Usage)
     * @param sortopt Use one of the following constants to specify a sorting
     *            option: {@link #FT_SCORES},
     *            {@link #FT_DATECREATED_DESCENDING},
     *            {@link #FT_DATE_DESCENDING} or
     *            {@link #FT_DATE_ASCENDING}
     * @param otheropt Use the following constants to specify additional search
     *            options. To specify more than one option, use a logical or
     *            operation: {@link #FT_FUZZY} or
     *            {@link #FT_STEMS}
     * @return An iterator over documents that match the full-text query, sorted
     *         by the selected option. If no matches are found, the iterator has
     *         no elements.
     */
    Iterator fullTextSearch(String query, int max, int sortopt, int otheropt);

    /**
     * Indicates whether document locking is enabled for a database.
     *
     * @return <code>true</code> if document locking is enabled,
     *         <code>false</code> if document locking is not enabled
     * @since Notes/Domino Release 6.5
     */
    boolean isDocumentLockingEnabled();

    /**
     * Sets whether document locking is enabled for a database.
     *
     * @param flag <code>true</code> if document locking is enabled,
     *         <code>false</code> if document locking is not enabled
     * @since Notes/Domino Release 6.5
     */
    void setDocumentLockingEnabled(boolean flag);

    /**
     * Indicates whether this database maintains folder references for
     * documents.
     *
     * <p>The database must have the $FolderInfo and $FolderRefInfo hidden
     * views to support folder references. These views can be copied from the
     * mail template. This property does not return view references.</p>
     *
     * <p>The database must be at the Release 5 file format level or greater.</p>
     *
     * <p>Maintaining folder references impacts performance.</p>
     *
     * @return <code>true</code> maintains folder references,
     *         <code>false</code> doesn't
     */
    boolean getFolderReferencesEnabled();

    /**
     * Indicates whether this database maintains folder references for
     * documents.
     *
     * <p>The database must have the $FolderInfo and $FolderRefInfo hidden
     * views to support folder references. These views can be copied from the
     * mail template. This property does not return view references.</p>
     *
     * <p>The database must be at the Release 5 file format level or greater.</p>
     *
     * <p>Maintaining folder references impacts performance.</p>
     *
     * @param flag <code>true</code> maintains folder references,
     *         <code>false</code> doesn't
     */
    void setFolderReferencesEnabled(boolean flag);

    /**
     * The views and folders in a database.
     *
     * Each element of the list represents a public view or folder in the
     * database. If the database is local, personal folders are also included.
     * The database must be open to use this property.
     *
     * @return list of views and folders in a database
     */
    List getViews();

    /**
     * Returns a list of all forms in a database.
     *
     * @return list of {@link DForm}
     */
    List getForms();

    /**
     * Returns the form with a given name.
     * The database must be open to use this property.
     * @param name name of the form
     * @return {@link DForm}
     */
    DForm getForm(String name);
}
