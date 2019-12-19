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

/**
 * Represents the agent environment of the current program, if an agent is running it.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public interface DAgentContext {

    /**
     * Marks a given document as processed.
     *
     * @param document a document
     */
    void updateProcessedDoc(DDocument document);

    /**
     * Performs a full-text search within the unprocessed documents.
     *
     * @param query The full-text query
     * @param maxDocs The maximum number of documents you want returned; 0 means all matching documents
     * @return An interator of documents that are not yet processed and match the query
     */
    Iterator unprocessedFTSearch(String query, int maxDocs);

    /**
     * Performs a full-text search within the unprocessed documents.
     * <dl>
     * <dt>sortOpt</dt>
     * <dd>Use one of the following to specify a sorting option:
     * <ul>
     * <li>Database.FT_SCORES (default) sorts by relevance score with highest relevance first.</li>
     * <li>Database.FT_DATECREATED_DES sorts by document creation date in descending order.</li>
     * <li>Database.FT_DATECREATED_ASC sorts by document creation date in ascending order.</li>
     * <li>Database.FT_DATE_DES sorts by document date in descending order.</li>
     * <li>Database.FT_DATE_ASC sorts by document date in ascending order.</li>
     * </ul>
     * </dd>
     * </dl>
     * <dl>
     * <dt>otherOpt</dt>
     * <dd>Use the following constants to specify additional search options.
     * To specify more than one option, use a logical OR operation:
     * <ul>
     * <li>Database.FT_DATABASE includes Lotus Domino databases in the search scope.</li>
     * <li>Database.FT_FILESYSTEM includes files other than Lotus Domino databases in the search scope.</li>
     * <li>Database.FT_FUZZY specifies a fuzzy search.</li>
     * <li>Database.FT_STEMS uses stem words as the basis of the search.</li>
     * </ul>
     * </dl>
     *
     * @param query The full-text query
     * @param maxDocs The maximum number of documents you want returned; 0 means all matching documents
     * @param sortOpt sorting options
     * @param otherOpt other options
     * @return An iterator of documents that are not yet processed and match the query
     */
    Iterator unprocessedFTSearch(String query, int maxDocs, int sortOpt, int otherOpt);

    /**
     * Searches for unprocessed documents which were created or modified since the cutoff date.
     * The resulting collection is sorted by relevance with highest relevance first.
     *
     * @param query A Lotus Domino formula that defines the selection criteria
     * @param dateTime A cutoff date
     * @param maxDocs The maximum number of documents you want returned; 0 means all matching documents
     * @return An iteratorof documents that are not yet processed, match the selection criteria,
     * and were created or modified after the cutoff date.
     */
    Iterator unprocessedSearch(String query, Calendar dateTime, int maxDocs);

    /**
     * The user name that is in effect for the current agent.
     *
     * In general, <tt>getEffectiveUserName</tt> and
     * {@link DSession#getUserName() DSession.getUserName()}return the same
     * value for a given program.
     * The exceptions are agents that run on a server, where
     * <tt>getEffectiveUserName</tt> is the name of the script's owner and
     * getUserName is the name of the server on which the script is running.
     * If the user name is hierarchical, this property returns the fully
     * distinguished name.
     *
     * @return effective user name
     */
    String getEffectiveUserName();

    /**
     * The agent that's currently running.
     *
     * @return current agent
     */
    DAgent getCurrentAgent();

    /**
     * The database in which the current agent resides.
     *
     * @return current database
     */
    DDatabase getCurrentDatabase();

    /**
     * The in-memory document when an agent starts.
     *
     * <b>Usage</b><br/>
     *
     * For an agent activated in a view through the Notes client UI, the
     * in-memory document is the document highlighted in the view. For an agent
     * run from a browser with the OpenAgent URL command, the in-memory document
     * is a new document containing an item for each CGI (Common Gateway
     * Interface) variable supported by Lotus Domino. Each item has the name and
     * current value of a supported CGI variable. (No design work on your part
     * is needed; the CGI variables are available automatically.) For an agent
     * run from a browser with <tt>Command([RunAgent])</tt> or <tt>Command[ToolsRunMacro]</tt>,
     * the in-memory document is the current document. In the case of
     * WebQueryOpen, this is the document before Lotus Domino converts it to
     * HTML and sends it to the browser; in the case of WebQuerySave, this is
     * the document before Lotus Domino saves it. If the form on which the
     * document is based contains a field named the same as a Lotus
     * Domino-supported CGI variable, the in-memory document also contains the
     * value of that variable. (You must explicitly design the CGI variables
     * into the form, for example, as hidden fields.). You cannot
     * use the encrypt and remove methods on the Document object returned by
     * getDocumentContext, nor use the compact method on the Database object
     * that contains the Document object returned by getDocumentContext.
     *
     * @return The in-memory document when an agent starts.
     */
    DDocument getDocumentContext();

    /**
     * The exit status code returned by the Agent Manager the last time the current agent ran.
     *
     * @return last exit status
     */
    int getLastExitStatus();

    /**
     * The date and time when the current agent was last executed.
     *
     * @return date and time of last run
     */
    Calendar getLastRun();

    /**
     * A document that an agent uses to store information between invocations.
     * The agent can use the information in this document the next time the
     * agent runs.
     *
     * @return SavedData document
     */
    DDocument getSavedData();

    /**
     * The documents in a database that the current agent considers to be
     * unprocessed. The type of agent determines which documents are
     * considered unprocessed.
     *
     * @return iterator of unprocessed documents
     */
    Iterator getUnprocessedDocuments();
}
