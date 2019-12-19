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

import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDocumentCollection;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Represents the Domino-Class <code>DocumentCollection</code>.
 */
public final class DocumentCollectionProxy extends BaseCollectionProxy implements DDocumentCollection {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3977580290371827769L;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param documentCollection the Notes DocumentCollection
     * @param monitor the monitor
     *
     * @see DocumentCollection
     */
    public DocumentCollectionProxy(final NotesProxyFactory theFactory, final DBase parent,
                                   final DocumentCollection documentCollection, final DNotesMonitor monitor) {
        super(theFactory, parent, documentCollection, monitor);
        getFactory().preprocessMethod();
    }

    /**
     * Returns the associated notes document collection object.
     *
     * @return notes database object
     */
    private DocumentCollection getDocumentCollection() {
        return (DocumentCollection) getNotesObject();
    }

    /**
     * @see BaseProxy#toString()
     * @return "DocumentCollection"
     */
    public String toString() {
        return "DocumentCollection";
    }

    /**
     * {@inheritDoc}
     * @see DDocumentCollection#getAllDocuments()
     */
    public Iterator getAllDocuments() {
        return new DocumentCollectionIterator(getFactory(), getParent(), getDocumentCollection(), getMonitor());
    }

    /**
     * {@inheritDoc}
     * @see DDocumentCollection#fullTextSearch(java.lang.String)
     */
    public void fullTextSearch(final String query) {
        getFactory().preprocessMethod();
        try {
            getDocumentCollection().FTSearch(query);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot search documents in collection", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDocumentCollection#fullTextSearch(java.lang.String, int)
     */
    public void fullTextSearch(final String query, final int maxdocs) {
        getFactory().preprocessMethod();
        try {
            getDocumentCollection().FTSearch(query, maxdocs);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot search documents in collection", e);
        }
    }
}
