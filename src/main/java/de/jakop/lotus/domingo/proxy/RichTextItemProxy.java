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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import lotus.domino.EmbeddedObject;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DEmbeddedObject;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DRichTextItem;

/**
 * This class represents the Domino-Class <code>RichTextItem</code>.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class RichTextItemProxy extends BaseItemProxy implements DRichTextItem {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257850978240969009L;

    /**
     * Constructor for DRichTextItemImpl.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param theRichTextItem the Notes TichTextItem
     * @param monitor the monitor
     */
    protected RichTextItemProxy(final NotesProxyFactory theFactory, final DBase parent,
                                final RichTextItem theRichTextItem, final DNotesMonitor monitor) {
        super(theFactory, parent, theRichTextItem, monitor);
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#embedAttachment(java.lang.String)
     */
    public DEmbeddedObject embedAttachment(final String path) {
        getFactory().preprocessMethod();
        try {
            final RichTextItem richTextItem = (RichTextItem) getNotesObject();
            final EmbeddedObject eo = richTextItem.embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", path, "");
            final DEmbeddedObject proxy = EmbeddedObjectProxy.getInstance(getFactory(), this, eo, getMonitor());
            return proxy;
        } catch (NotesException e) {
            throw newRuntimeException("Cannot embed attachment", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#getEmbeddedAttachment(java.lang.String)
     */
    public DEmbeddedObject getEmbeddedAttachment(final String fileName) {
        getFactory().preprocessMethod();
        try {
            final RichTextItem richTextItem = (RichTextItem) getNotesObject();
            final EmbeddedObject eo = richTextItem.getEmbeddedObject(fileName);
            final DEmbeddedObject proxy = EmbeddedObjectProxy.getInstance(getFactory(), this, eo, getMonitor());
            return proxy;
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get attachment " + fileName, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#getEmbeddedObjects()
     */
    public Iterator getEmbeddedObjects() {
        getFactory().preprocessMethod();
        try {
            final Vector vector = ((RichTextItem) getNotesObject()).getEmbeddedObjects();
            final List list = new ArrayList();
            for (final Iterator it = vector.iterator(); it.hasNext();) {
                final EmbeddedObject eo = (EmbeddedObject) it.next();
                final DEmbeddedObject deo = EmbeddedObjectProxy.getInstance(getFactory(), this, eo, getMonitor());
                list.add(deo);
            }
            return new EmbeddedObjectsIterator(list);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get embedded objects", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#appendText(java.lang.String)
     */
    public void appendText(final String text) {
        getFactory().preprocessMethod();
        final RichTextItem richTextItem = (RichTextItem) getNotesObject();
        try {
            richTextItem.appendText(text);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot append text", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#addNewLine()
     */
    public void addNewLine() {
        getFactory().preprocessMethod();
        final RichTextItem richTextItem = (RichTextItem) getNotesObject();
        try {
            richTextItem.addNewLine();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot add a new line", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#addNewLine(int)
     */
    public void addNewLine(final int n) {
        getFactory().preprocessMethod();
        final RichTextItem richTextItem = (RichTextItem) getNotesObject();
        try {
            richTextItem.addNewLine(n);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot add new lines", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#addNewLine(int, boolean)
     */
    public void addNewLine(final int n, final boolean newparagraph) {
        getFactory().preprocessMethod();
        final RichTextItem richTextItem = (RichTextItem) getNotesObject();
        try {
            richTextItem.addNewLine(n, newparagraph);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot add new lines", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#addTab()
     */
    public void addTab() {
        getFactory().preprocessMethod();
        final RichTextItem richTextItem = (RichTextItem) getNotesObject();
        try {
            richTextItem.addTab();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot add tab", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#addTab(int)
     */
    public void addTab(final int count) {
        getFactory().preprocessMethod();
        final RichTextItem richTextItem = (RichTextItem) getNotesObject();
        try {
            richTextItem.addTab(count);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot add tabs", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DRichTextItem#getUnformattedText()
     */
    public String getUnformattedText() {
       getFactory().preprocessMethod();
       final RichTextItem richTextItem = (RichTextItem) getNotesObject();
       try {
           return richTextItem.getUnformattedText();
       } catch (NotesException e) {
           throw newRuntimeException("Cannot get unformatted text", e);
       }
    }

    ////////////////////////////////////////////////
    //    Iterator classes
    ////////////////////////////////////////////////

    /**
     * A <code>EmbeddedObjectsIterator</code> allows iteration over a set of
     * <code>lotus.domino.EmbeddedObject</code>.
     *
     * #see de.bea.domingo.DEmbeddedObject
     *
     * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
     */
    protected static final class EmbeddedObjectsIterator implements Iterator {

        /** The iterator of the notes items.*/
        private final Iterator embeddedObjectsIterator;

        /**
         * Constructs an Iterator by use of a vector of
         * <code>lotus.domino.EmbeddedObject</code>s.
         *
         * @param embeddedObjects a vector of <code>lotus.domino.EmbeddedObject</code>s
         */
        protected EmbeddedObjectsIterator(final List embeddedObjects) {
            this.embeddedObjectsIterator = embeddedObjects.iterator();
        }

        /**
         * Indicates whether this iterator has a next element or not.
         *
         * @return boolean true if there is a next
         */
        public boolean hasNext() {
            return embeddedObjectsIterator.hasNext();
        }

        /**
         * Returns the next element of this iterator (hasNext()==true)
         * or <code>null</code> if hasNext()==false.
         *
         * @return an <code>DEmbeddedObject</code>
         */
        public Object next() {
            return embeddedObjectsIterator.next();
        }

        /**
         * Throws an UnsupportedOperationException: The <tt>remove</tt>
         * operation is not supported by this Iterator.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
