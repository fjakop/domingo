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

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DViewEntry;

/**
 * Represents a view entry. A view entry describes a row in a view.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ViewEntryProxy extends BaseProxy implements DViewEntry {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3978430204404511538L;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param viewEntry a view entry
     * @param monitor the monitor
     */
    private ViewEntryProxy(final NotesProxyFactory theFactory, final DBase theParent,
                           final ViewEntry viewEntry, final DNotesMonitor monitor) {
        super(theFactory, theParent, viewEntry, monitor);
    }

    /**
     * Creates an encapsulated notes session object.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param viewEntry the Notes ViewEntry
     * @param monitor the monitor
     * @return a session object
     */
    public static ViewEntryProxy getInstance(final NotesProxyFactory theFactory,  final DBase parent,
                                             final ViewEntry viewEntry, final DNotesMonitor monitor) {
        if (viewEntry == null) {
            return null;
        }
        return new ViewEntryProxy(theFactory, parent, viewEntry, monitor);
    }

    /**
     * Returns the associated Notes view entry.
     *
     * @return associated notes view entry
     */
    private ViewEntry getViewEntry() {
        return (ViewEntry) getNotesObject();
    }

    /**
     * @see java.lang.Object#toString()
     * @return  a string representation of the object.
     */
    public String toString() {
        return "ViewEntryProxy";
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getColumnValues()
     */
    public List getColumnValues() {
        getFactory().preprocessMethod();
        try {
            final Vector vector = getViewEntry().getColumnValues();
            final List convertedValues = convertNotesDateTimesToCalendar(vector);
            recycleDateTimeList(vector);
            return Collections.unmodifiableList(convertedValues);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get column values", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getDocument()
     */
    public DDocument getDocument() {
        getFactory().preprocessMethod();
        try {
            final Document doc = getViewEntry().getDocument();
            final BaseDocumentProxy proxy = BaseDocumentProxy.getInstance(getFactory(), this, doc, getMonitor());
            return (DDocument) proxy;
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get document", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#isCategory()
     */
    public boolean isCategory() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().isCategory();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if is category", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#isDocument()
     */
    public boolean isDocument() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().isDocument();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if is document", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#isTotal()
     */
    public boolean isTotal() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().isTotal();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if is total", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getUniversalID()
     */
    public String getUniversalID() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().getUniversalID();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get universalID", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getChildCount()
     */
    public int getChildCount() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().getChildCount();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get child count", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#isConflict()
     */
    public boolean isConflict() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().isConflict();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if is conflict", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getDescendantCount()
     */
    public int getDescendantCount() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().getDescendantCount();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get descendant count", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getSiblingCount()
     */
    public int getSiblingCount() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().getSiblingCount();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get sibling count", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getSibblingCount()
     * @deprecated use method {@link #getSiblingCount()} instead
     */
    public int getSibblingCount() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().getSiblingCount();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get sibling count", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getIndentLevel()
     */
    public int getIndentLevel() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().getIndentLevel();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get indent level", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#isValid()
     */
    public boolean isValid() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().isValid();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if it valid", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getNoteID()
     */
    public String getNoteID() {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().getNoteID();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get NoteID", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewEntry#getPosition(char)
     */
    public String getPosition(final char seperator) {
        getFactory().preprocessMethod();
        try {
            return getViewEntry().getPosition(seperator);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get position", e);
        }
    }
}
