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

package de.jakop.lotus.domingo.proxy;

import lotus.domino.EmbeddedObject;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DEmbeddedObject;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * This class represents the Domino-Class <code>EmbeddedObject</code>.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class EmbeddedObjectProxy extends BaseProxy implements DEmbeddedObject {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3545519508262432819L;

    /**
     * Constructor for DEmbeddedObjectImpl.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object (mostly a Document or a RichTextItem)
     * @param embeddedObject the notes object to attach
     * @param monitor the monitor
     */
    private EmbeddedObjectProxy(final NotesProxyFactory theFactory, final DBase parent,
                                final EmbeddedObject embeddedObject, final DNotesMonitor monitor) {
        super(theFactory, parent, embeddedObject, monitor);
    }

    /**
     * Creates a new DEmbeddedObject and enables logging.
     *
     * @param theFactory the controlling factory
     * @param parent the objects parent
     * @param embeddedObject the corresponding notes object
     * @param monitor the monitor
     * @return a new DEmbeddedObject
     */
    static DEmbeddedObject getInstance(final NotesProxyFactory theFactory, final DBase parent,
                                       final EmbeddedObject embeddedObject, final DNotesMonitor monitor) {
        if (embeddedObject == null) {
            return null;
        }
        EmbeddedObjectProxy embeddedObjectProxy = (EmbeddedObjectProxy) theFactory.getBaseCache().get(embeddedObject);
        if (embeddedObjectProxy == null) {
            embeddedObjectProxy = new EmbeddedObjectProxy(theFactory, parent, embeddedObject, monitor);
            theFactory.getBaseCache().put(embeddedObject, embeddedObjectProxy);
        }
        return embeddedObjectProxy;
    }

    /**
     * Returns the associated notes document embedded object object.
     *
     * @return notes database object
     */
    private EmbeddedObject getEmbeddedObject() {
        return (EmbeddedObject) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see DEmbeddedObject#extractFile(String)
     */
    public void extractFile(final String path) {
        getFactory().preprocessMethod();
        try {
            getEmbeddedObject().extractFile(path);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot extract file " + path, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DEmbeddedObject#getName()
     */
    public String getName() {
        try {
            return getEmbeddedObject().getName();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get name", e);
        }
    }

    /**
     * @see DEmbeddedObject#remove()
     */
    public void remove() {
        getFactory().preprocessMethod();
        try {
            getEmbeddedObject().remove();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot remove embedded object", e);
        }
    }

    /**
     * @see BaseProxy#toString()
     * @return the name of the embedded object
     */
    public String toString() {
        return getName();
    }

    /**
     * {@inheritDoc}
     * @see DEmbeddedObject#getSource()
     */
    public String getSource() {
        try {
            return getEmbeddedObject().getSource();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get source", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DEmbeddedObject#getType()
     */
    public int getType() {
        try {
            return getEmbeddedObject().getType();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get type", e);
        }
    }
}
