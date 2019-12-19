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

import lotus.domino.Document;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DProfileDocument;

/**
 * Represents a document in a database.
 */
public final class ProfileDocumentProxy extends BaseDocumentProxy implements DProfileDocument {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3690193256935994673L;

    /**
     * Constructor for DDocumentImpl.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param theDocument the Notes document object
     * @param monitor the monitor
     */
    protected ProfileDocumentProxy(final NotesProxyFactory theFactory, final DBase parent,
                                   final Document theDocument, final DNotesMonitor monitor) {
        super(theFactory, parent, theDocument, monitor);
    }

    /**
     * {@inheritDoc}
     * @see DProfileDocument#getKey()
     */
    public String getKey() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getKey();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DProfileDocument#getNameOfProfile()
     */
    public String getNameOfProfile() {
        getFactory().preprocessMethod();
        try {
            return getDocument().getNameOfProfile();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get name of profile", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see BaseProxy#toString()
     */
    public String toString() {
        return getKey() + "_" + getNameOfProfile();
    }
}
