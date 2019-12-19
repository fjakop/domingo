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

import java.util.Iterator;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.Stream;
import lotus.domino.View;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DView;

/**
 * Represents a document in a database.
 */
public final class DocumentProxy extends BaseDocumentProxy implements DDocument {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3258413915393045559L;

    /** private cache of universal ID.
     * (to make the UNID available even after recycle of the document) */
    private String universalID;

    /**
     * Constructor for DDocumentImpl.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param theDocument the Notes document object
     * @param monitor the monitor
     */
    protected DocumentProxy(final NotesProxyFactory theFactory, final DBase parent,
                            final Document theDocument, final DNotesMonitor monitor) {
        super(theFactory, parent, theDocument, monitor);
        universalID = getUniversalID();
    }

    /**
     * {@inheritDoc}
     * @see DDocument#isNewNote()
     */
    public boolean isNewNote() {
        getFactory().preprocessMethod();
        try {
            return getDocument().isNewNote();
        } catch (NotesException e) {
            throw newRuntimeException("isNewNote(): ", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getUniversalID()
     */
    public String getUniversalID() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getUniversalID();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get universalID", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getNoteID()
     */
    public String getNoteID() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getNoteID();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get NoteID", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#isResponse()
     */
    public boolean isResponse() {
        getFactory().preprocessMethod();
        try {
            return getDocument().isResponse();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if is response", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#copyToDatabase(DDatabase)
     */
    public DDocument copyToDatabase(final DDatabase database) {
        if (!(database instanceof DatabaseProxy)) {
            throw newRuntimeException("Cannot copy database (invalid argument)");
        }
        getFactory().preprocessMethod();
        try {
            final Database targetNotesDatabase = (Database) ((DatabaseProxy) database).getNotesObject();
            final Document newNotesDoc = getDocument().copyToDatabase(targetNotesDatabase);
            return (DDocument) getInstance(getFactory(), database, newNotesDoc, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot copy database", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see BaseDocumentProxy#toString()
     */
    public String toString() {
        return universalID;
    }

    /**
     * {@inheritDoc}
     * @see DDocument#send(java.lang.String)
     */
    public void send(final String recipient) {
        getFactory().preprocessMethod();
        try {
            if (recipient == null || "".equals(recipient)) {
                final List docRecipients = getRecipients();
                if (docRecipients == null) {
                    throw newRuntimeException("recipients.missing");
                }
                send0(docRecipients);
                return;
            }
            send0(recipient);
        } catch (NotesException e) {
            throw newRuntimeException("send(String): ", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#send(java.util.List)
     */
    public void send(final List recipients) {
        getFactory().preprocessMethod();
        try {
            if (recipients == null || getRecipients(recipients) == null) {
                final List docRecipients = getRecipients();
                if (docRecipients == null) {
                    throw newRuntimeException("recipients.missing");
                }
                send0(docRecipients);
                return;
            }
            send0(recipients);
        } catch (NotesException e) {
            throw newRuntimeException("send(List): ", e);
        }
    }

    /**
     * Returns a list of recipients in the <tt>sendTo</tt> item, if at least
     * one value is not an empty string.
     *
     * @return list of recipients or <code>null</code> if no recipients available.
     * @throws NotesException if recipients cannot be read from the document.
     */
    private List getRecipients() throws NotesException {
        final List recipients = getDocument().getItemValue("SendTo");
        return getRecipients(recipients);
    }

    /**
     * Returns the given list of recipients, if at least
     * one value is not an empty string, else <code>null</code>.
     *
     * @param recipients list of recipients
     * @return given list of recipients or <code>null</code> no recipients in list found
     */
    private List getRecipients(final List recipients) {
        if (recipients != null) {
            for (int i = 0; i < recipients.size(); i++) {
                if (recipients.get(i) instanceof String) {
                    if (((String) recipients.get(i)).length() > 0) {
                        return recipients;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Sends a document.
     *
     * @param recipient recipient
     * @throws NotesException if send failed
     */
    private void send0(final String recipient) throws NotesException {
        getDocument().send(recipient);
    }

    /**
     * Send a document.
     *
     * @param recipients list of recipients
     * @throws NotesException if send failed
     */
    private void send0(final List recipients) throws NotesException {
        getDocument().send(convertListToVector(recipients));
    }

    /**
     * {@inheritDoc}
     * @see DDocument#makeResponse(DDocument)
     */
    public void makeResponse(final DDocument parent) {
        if (!(parent instanceof BaseDocumentProxy)) {
            throw newRuntimeException("Cannot make response", new ClassCastException(parent.getClass().getName()));
        }
        getFactory().preprocessMethod();
        try {
            final Document document = ((BaseDocumentProxy) parent).getDocument();
            getDocument().makeResponse(document);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot make response", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getParentDocument()
     */
    public DDocument getParentDocument() {
        getFactory().preprocessMethod();
        final String parentDocUnID = getParentDocumentUNID();
        if (parentDocUnID == null || "".equals(parentDocUnID)) {
            return null;
        }
        final DDatabase database = (DDatabase) getParent();
        return database.getDocumentByUNID(parentDocUnID);
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getParentDocumentUNID()
     */
    public String getParentDocumentUNID() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getParentDocumentUNID();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get UNID of parent document", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getResponses()
     */
    public Iterator getResponses() {
        getFactory().preprocessMethod();
        try {
            final DocumentCollection docColl = getDocument().getResponses();
            return new DocumentCollectionIterator(getFactory(), this.getParent(), docColl, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get responses", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#setSaveMessageOnSend(boolean)
     */
    public void setSaveMessageOnSend(final boolean saveMessageOnSend) {
        getFactory().preprocessMethod();
        try {
            getDocument().setSaveMessageOnSend(saveMessageOnSend);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set to save message on send", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#setEncryptOnSend(boolean)
     */
    public void setEncryptOnSend(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getDocument().setEncryptOnSend(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set to encrypt mail on send.", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#setSignOnSend(boolean)
     */
    public void setSignOnSend(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getDocument().setSignOnSend(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set to sign on send.", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#sign()
     */
    public void sign() {
        getFactory().preprocessMethod();
        try {
            getDocument().sign();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot sign document.", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#replaceHTML(java.lang.String, java.lang.String)
     */
    public void replaceHTML(final String name, final String value) {
        getFactory().preprocessMethod();
        try {
            getDocument().removeItem(name);
            MIMEEntity entity = getDocument().createMIMEEntity(name);
            Stream stream = ((Session) getDSession().getNotesObject()).createStream();
            stream.writeText(value);
            entity.setContentFromText(stream, "text/html; charset=ISO-8859-1", MIMEEntity.ENC_NONE);

        } catch (NotesException e) {
            throw newRuntimeException("Cannot replace HTML in item " + name, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getFTSearchScore()
     */
    public int getFTSearchScore() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getFTSearchScore();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get FTSearch scope.", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getParentView()
     */
    public DView getParentView() {
        getFactory().preprocessMethod();
        try {
            final View parentView = getDocument().getParentView();
            return ViewProxy.getInstance(getFactory(), this.getParentDatabase(), parentView, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot put in folder", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getFolderReferences()
     */
    public List getFolderReferences() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getFolderReferences();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get folder references", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#putInFolder(java.lang.String)
     */
    public void putInFolder(final String name) {
        getFactory().preprocessMethod();
        try {
            getDocument().putInFolder(name);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot put in folder", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#putInFolder(java.lang.String, boolean)
     */
    public void putInFolder(final String name, final boolean create) {
        getFactory().preprocessMethod();
        try {
            getDocument().putInFolder(name, create);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot put in folder", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#removeFromFolder(java.lang.String)
     */
    public void removeFromFolder(final String name) {
        getFactory().preprocessMethod();
        try {
            getDocument().removeFromFolder(name);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot remove from folder", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getURL()
     */
    public String getURL() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get URL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getNotesURL()
     */
    public String getNotesURL() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getNotesURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get Notes URL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocument#getHttpURL()
     */
    public String getHttpURL() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getHttpURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get Http URL", e);
        }
    }
}
