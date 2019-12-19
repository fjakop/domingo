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

import java.util.Iterator;
import java.util.List;

/**
 * Represents a document in a database.
 *
 * <p>To create a new Document object, use
 * {@link DDatabase#createDocument()} in DDatabase.</p>
 *
 * <p>To access existing documents:
 * <ul>
 * <li>To get all the documents in a database, use
 * {@link DDatabase#getAllDocuments()} in DDatabase.</li>
 * <li>To get all documents that are responses to
 * a particular document, use {@link DDocument#getResponses()} in
 * DDocument. To get a response document's parent document, use
 * {@link DDocument#getParentDocument()} in DDocument.</li>
 * <li>To get a document based on its note ID or UNID, use
 * {@link DDatabase#getDocumentByID(java.lang.String)} or
 * {@link DDatabase#getDocumentByUNID(java.lang.String)}
 * in DDatabase.</li></ul></p>
 *
 * <p>Once you have a view, you can navigate to a specific document using
 * methods in the View class.</p>
 *
 * <p>Once you have a collection of documents, you can navigate to a specific
 * document using methods in the DocumentCollection class.</p>
 *
 * <p>After you create, modify, or delete a document, you must save the changes
 * by calling the {@link DDocument#save()} method.
 * If you don't call save before the program finishes, all of your changes to
 * a Document are lost.</p>
 *
 * <p>If you create and save a new document without adding any items to it, the
 * document is saved with one item "$UpdatedBy." This item contains the name of
 * the creator of the document.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DDocument extends DBaseDocument {

    /**
     * Indicates whether a document is new.
     * A document is new if it has not been saved.
     *
     * @return Boolean <code>true</code> if the document was created, but has
     *         not been saved
     */
    boolean isNewNote();

    /**
     * The universal ID, which uniquely identifies a document across all
     * replicas of a database.
     *
     * <p>In character format, the universal ID is a 32-character combination of
     * hexadecimal digits (0-9, A-F). The universal ID is also known as the
     * unique ID or UNID.</p>
     *
     * @return String
     */
    String getUniversalID();

    /**
     * The note ID of a document, which is a hexadecimal value of up to
     * 8 characters that uniquely identifies a document within a particular
     * database.
     *
     * <p>A typical note ID looks like this: 20FA. A note ID represents the
     * location of a document within a specific database file, so documents that
     * are replicas of one another generally have different note IDs. A note ID
     * does not change, unless the document is deleted.</p>
     *
     * @return the notes ID
     */
    String getNoteID();

    /**
     * Mails a document.
     *
     * <p>The following rules apply to specification of the recipient or
     * recipients:
     * <ul>
     * <li>Ignored if the document contains a SendTo item, in which case the
     * document is mailed to recipients listed in SendTo.</li>
     * <li>Required if the document does not contain a SendTo item.</li>
     * <li>May include people, groups, or mail-in databases.</li></ul></p>
     * <p>If you have only Reader access to a database, you can run an agent
     * that creates and sends a document, but the agent will not work if you
     * attach a file to that document.</p>
     * <p>Two kinds of items can affect the mailing of the document when you
     * use send:
     * <ul>
     * <li>If the document contains additional recipient items, such as CopyTo
     * or BlindCopyTo, the documents mailed to those recipients.</li>
     * <li>If the document contains items to control the routing of mail, such
     * as DeliveryPriority, DeliveryReport, or ReturnReceipt, they are used
     * when sending the document.</li></ul></p>
     * <p>The IsSaveMessageOnSend property controls whether the sent document
     * is saved in the database. If IsSaveMessageOnSend is true and you attach
     * the form to the document, the form is saved with the document.</p>
     * <p>The send method automatically creates an item called $AssistMail on
     * the sent document. The SentByAgent property uses this item to determine
     * if a document was mailed by an agent.</p>
     * <p>If a program runs on a workstation, the mailed document contains the
     * current user's name in the From item. If a program runs as an agent
     * on a server, the mailed document contains the server's name in the
     * From item.</p>
     *
     * @param recipient name of recipient
     */
    void send(String recipient);

    /**
     * Mails a document.
     *
     * @param recipients list of Strings with recipients
     * @see #send(java.lang.String)
     */
    void send(List recipients);

    /**
     * Copies a document to a specified database.
     *
     * @param database The database to which you want to copy the document.
     * Cannot be null.
     * @return The new document in the specified database or <code>null</code> if the document cannot be created
     */
    DDocument copyToDatabase(DDatabase database);

    /**
     * Indicates whether a document is a response to another document.
     *
     * @return <code>true</code> if the document is a response to another
     *         document, else <code>false</code>
     */
    boolean isResponse();

    /**
     * The universal ID of a document's parent, if the document is a response.
     * Returns an empty string if a document doesn't have a parent.
     *
     * @return the universal ID
     */
    String getParentDocumentUNID();

    /**
     * The parent document if the document is a response document.
     *
     * @return the parent document or <code>null</code> if the document is not
     * a response document.
     */
    DDocument getParentDocument();

    /**
     * Makes one document a response to another. The two documents must be in the same database.
     *
     * @param doc The document to which the current document becomes a response.
     */
    void makeResponse(DDocument doc);

    /**
     * The immediate responses to a document.
     *
     * <p>Each document returned is an immediate response to the first document.
     * Responses-to-responses are not included.
     * If the current document has no responses, the vector contains zero
     * documents.</p>
     *
     * <p><b>Responses-to-Responses</b><br/>
     * This property returns only immediate responses to a document, but you
     * can write a recursive sub or function to access all the descendants of
     * a particular document. A recursive sub calls itself in the same way that
     * a non-recursive sub calls any other sub or function.</p>
     *
     * @return The immediate responses to a document.
     */
    Iterator getResponses();

    /**
     * Indicates if a document is saved to a database when mailed.
     * Applies only to new documents that have not yet been saved.
     * <p>When SaveMessageOnSend is true, the document is saved just after
     * being mailed.</p>
     *
     * @param saveMessageOnSend <code>true</code> if the document is saved when
     *                          mailed, else <code>false</code>.
     */
    void setSaveMessageOnSend(boolean saveMessageOnSend);

    /**
     * Indicates if a document is encrypted when mailed.
     * <p>To encrypt a document when mailed, this method looks for the public
     * key of each recipient in the Domino Directory. If it cannot find a
     * recipient's public key, the method sends an unencrypted copy of the
     * document to that recipient. All other recipients receive an encrypted
     * copy of the document.</p>
     * <p>This property has no effect on whether a document is encrypted when
     * saved to a database.</p>
     *
     * @param flag <code>true</code> if the document is encrypted when mailed,
     *             else <code>false</code>.
     */
    void setEncryptOnSend(boolean flag);

    /**
     * Indicates if a document is signed when mailed.
     *
     * @param flag <code>true</code> if the document is signed when mailed;
     *             else <code>false</code>.
     */
    void setSignOnSend(boolean flag);

    /**
     * Signs a document.
     * <p>If you want the signature to be saved, you must call the Save method
     * after signing the document.</p>
     * <p>If the program is running on a server, this method has no effect.</p>
     */
    void sign();

    /**
     * Replaces a MIMEEntity item with a new item with the given HTML code.
     * <p>If the document does not contain an item with the specified name, this
     * method creates a new MIMEEntity item and adds it to the document.</p>
     *
     * @param name name of an item
     * @param value HTML code for the new item
     */
    void replaceHTML(String name, String value);

    /**
     * The full-text search score of a document, if it was retrieved as part of
     * a full-text search.
     *
     * <p><b>Usage</b></p>
     * <p>The score is determined by the number of target words that are found in
     * the document, the term weights assigned to the target words, and any
     * proximity operators in the search query. If the document is not retrieved
     * as part of a full-text search, returns 0. If the document is retrieved
     * using an FTSearch method on a database without a full-text index, returns
     * an unpredictable number.</p>
     * <p>If a document is in more than one DocumentCollection or
     * ViewEntryCollection, its score is that of the last collection from which
     * it was retrieved. The score is correct unless you get the score from the
     * current object after retrieving the same document from another
     * collection.</p>
     * <p>Documents added to a collection have a search score of 0.</p>
     * <p>Documents deleted from a view have a search score of 0.</p>
     *
     * @return The full-text search score of a document, if it was retrieved as
     *         part of a full-text search
     */
    int getFTSearchScore();

    /**
     * The view from which a document was retrieved, if any.
     *
     * <p>If the document was retrieved directly from the database or a
     * document collection, getParentView returns null.</p>
     *
     * @return view from which a document was retrieved
     */
    DView getParentView();

    /**
     * The names of the folders containing a document.
     *
     * <p>The database must have the hidden views <code>$FolderInfo</code>
     * and <code>$FolderRefInfo</code> to support folder references. These
     * views can be copied from the mail template. This property does not return
     * view references.</p>
     *
     * <p>Folder references must be enabled for the database. See the
     * FolderReferencesEnabled property of Database.</p>
     *
     * @return names of the folders containing a document
     *
     * @see DDatabase#getFolderReferencesEnabled()
     */
    List getFolderReferences();

    /**
     * Adds a document to a folder. If the folder does not exist, it is created.
     *
     * <p>If the document is already inside the folder you specify, putInFolder
     * does nothing. If you specify a path to a folder, and none of the folders
     * exists, the method creates all of them for you. For example:</p>
     *
     * <pre>doc.putInFolder( "Vehicles\\Bikes" );</pre>
     *
     * <p>If neither Vehicles nor Bikes exists, putInFolder creates both,
     * placing the Bikes folder inside the Vehicles folder.</p>
     *
     * <p>This method cannot add the first document to a folder that is
     * "Shared, Personal on first use."</p>
     *
     * @param name The name of the folder in which to place the document. The
     *            folder may be personal if the program is running on a
     *            workstation. If the folder is within another folder, specify a
     *            path to it, separating folder names with backslashes. For
     *            example: "Vehicles\\Bikes"
     *
     * @see #putInFolder(String, boolean)
     */
    void putInFolder(String name);

    /**
     * Adds a document to a folder. If the folder does not exist, it is created.
     *
     * <p>If the document is already inside the folder you specify, putInFolder
     * does nothing. If you specify a path to a folder, and none of the folders
     * exists, the method creates all of them for you. For example:</p>
     *
     * <pre>doc.putInFolder( "Vehicles\\Bikes" );</pre>
     *
     * <p>If neither Vehicles nor Bikes exists, putInFolder creates both,
     * placing the Bikes folder inside the Vehicles folder.</p>
     *
     * <p>This method cannot add the first document to a folder that is
     * "Shared, Personal on first use."</p>
     *
     * @param name The name of the folder in which to place the document. The
     *            folder may be personal if the program is running on a
     *            workstation. If the folder is within another folder, specify a
     *            path to it, separating folder names with backslashes. For
     *            example: "Vehicles\\Bikes"
     * @param create If <code>true</code> (default), creates the folder if it
     *            does not exist.
     */
    void putInFolder(String name, boolean create);

    /**
     * Removes a document from a folder.
     *
     * <p>The method does nothing if the document is not in the folder you
     * specify, or if the folder you specify does not exist.</p>
     *
     * @param name The name of the folder from which to remove the document. The
     *            folder may be personal if the program is running on a
     *            workstation. If the folder is within another folder, specify a
     *            path to it, separating folder names with backslashes. For
     *            example: "Vehicles\\Bikes"
     */
    void removeFromFolder(String name);

    /**
     * Returns the Domino URL for its parent object.
     *
     * <p>See {@link DSession#resolve(String)} for additional information and examples.</p>
     *
     * @return Domino URL
     *
     * @see #getHttpURL()
     * @see #getNotesURL()
     * @see #getURL()
     * @see DSession#resolve(String)
     */
    String getURL();

    /**
     * The Domino URL of a form when Notes protocols are in effect.
     *
     * <p>If Notes protocols are not available, this property returns an empty
     * string.</p>
     *
     * <p>See {@link DSession#resolve(String)} for additional information and examples.</p>
     *
     * @return Domino Notes URL
     *
     * @see #getHttpURL()
     * @see #getURL()
     * @see DSession#resolve(String)
     */
    String getNotesURL();

    /**
     * The Domino URL of a form when HTTP protocols are in effect.
     *
     * <p>If HTTP protocols are not available, this property returns an empty
     * string.</p>
     *
     * <p>See {@link DSession#resolve(String)} for additional information and examples.</p>
     *
     * @return The Domino HTTP URL
     *
     * @see #getNotesURL()
     * @see #getURL()
     * @see DSession#resolve(String)
     */
    String getHttpURL();
}
