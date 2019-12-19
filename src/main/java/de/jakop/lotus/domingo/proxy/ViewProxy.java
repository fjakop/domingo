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

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewColumn;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesIterator;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.DViewColumn;
import de.jakop.lotus.domingo.DViewEntry;

/**
 * Represents the Domino-Class <code>View</code>.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ViewProxy extends BaseCollectionProxy implements DView {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3832616283597780788L;

    /** The name of the view for fast access. */
    private String name = null;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param database Notes database
     * @param view the Notes View
     * @param monitor the monitor
     */
    private ViewProxy(final NotesProxyFactory theFactory, final DDatabase database,
                      final View view, final DNotesMonitor monitor) {
        super(theFactory, database, view, monitor);
        name = getNameIntern();
    }

    /**
     * Returns a view object.
     *
     * @param theFactory the controlling factory
     * @param database Notes database
     * @param view the notes view object
     * @param monitor the monitor
     * @return a view object
     */
    static ViewProxy getInstance(final NotesProxyFactory theFactory, final DDatabase database,
                                 final View view, final DNotesMonitor monitor) {
        if (view == null) {
            return null;
        }
        ViewProxy viewProxy = (ViewProxy) theFactory.getBaseCache().get(view);
        if (viewProxy == null) {
            viewProxy = new ViewProxy(theFactory, database, view, monitor);
            viewProxy.setMonitor(monitor);
            theFactory.getBaseCache().put(view, viewProxy);
        }
        return viewProxy;
    }

    /**
     * Returns the associated Notes view.
     *
     * @return associated Notes view
     */
    private View getView() {
        return (View) getNotesObject();
    }

    /**
     * @see DView#refresh()
     */
    public void refresh() {
        getFactory().preprocessMethod();
        try {
            getView().refresh();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot refresh view", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * @see DView#getName()
     */
    private String getNameIntern() {
        getFactory().preprocessMethod();
        try {
            return getView().getName();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get name", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getDocumentByKey(String, boolean)
     */
    public DDocument getDocumentByKey(final String key, final boolean exact) {
        getFactory().preprocessMethod();
        try {
            final Document doc = getView().getDocumentByKey(key, exact);
            return (DDocument) BaseDocumentProxy.getInstance(getFactory(), this, doc, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get document by key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getDocumentByKey(List,boolean)
     */
    public DDocument getDocumentByKey(final List keys, final boolean exact) {
        getFactory().preprocessMethod();
        final List convertedKeys = convertCalendarsToNotesDateTime(keys);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            final Document document = getView().getDocumentByKey(vector, exact);
            return (DDocument) BaseDocumentProxy.getInstance(getFactory(), this, document, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get document by key list", e);
        } finally {
            recycleDateTimeList(convertedKeys);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(java.util.List)
     */
    public Iterator getAllDocumentsByKey(final List keys) {
        getFactory().preprocessMethod();
        final List convertedKeys = convertCalendarsToNotesDateTime(keys);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            final DocumentCollection coll = getView().getAllDocumentsByKey(vector);
            return new DocumentCollectionIterator(getFactory(), getParent(), coll, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all documents by key list", e);
        } finally {
            recycleDateTimeList(convertedKeys);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(java.lang.String)
     */
    public Iterator getAllDocumentsByKey(final String key) {
        return getAllDocumentsByKey(key, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(java.lang.String, boolean)
     */
    public Iterator getAllDocumentsByKey(final String key, final boolean exact) {
        getFactory().preprocessMethod();
        try {
            final DocumentCollection collection = getView().getAllDocumentsByKey(key, exact);
            return new DocumentCollectionIterator(getFactory(), getParent(), collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all documents by key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(java.util.List, boolean)
     */
    public Iterator getAllDocumentsByKey(final List keys, final boolean exact) {
        getFactory().preprocessMethod();
        final List convertedKeys = convertCalendarsToNotesDateTime(keys);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            final DocumentCollection coll = getView().getAllDocumentsByKey(vector, exact);
            return new DocumentCollectionIterator(getFactory(), getParent(), coll, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all documents by list", e);
        } finally {
            recycleDateTimeList(convertedKeys);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(java.util.Calendar)
     */
    public Iterator getAllDocumentsByKey(final Calendar key) {
        return getAllDocumentsByKey(key, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(double)
     */
    public Iterator getAllDocumentsByKey(final double key) {
        return getAllDocumentsByKey(key, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(int)
     */
    public Iterator getAllDocumentsByKey(final int key) {
        return getAllDocumentsByKey(key, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(java.util.Calendar, boolean)
     */
    public Iterator getAllDocumentsByKey(final Calendar key, final boolean exact) {
        getFactory().preprocessMethod();
        try {
            final DateTime dateTime = createDateTime(key);
            final DocumentCollection collection = getView().getAllDocumentsByKey(dateTime, exact);
            getFactory().recycle(dateTime);
            return new DocumentCollectionIterator(getFactory(), getParent(), collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all documents by key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(double, boolean)
     */
    public Iterator getAllDocumentsByKey(final double key, final boolean exact) {
        getFactory().preprocessMethod();
        try {
            final DocumentCollection collection = getView().getAllDocumentsByKey(new Double(key), exact);
            return new DocumentCollectionIterator(getFactory(), getParent(), collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all documents by key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocumentsByKey(int, boolean)
     */
    public Iterator getAllDocumentsByKey(final int key, final boolean exact) {
        getFactory().preprocessMethod();
        try {
            final DocumentCollection collection = getView().getAllDocumentsByKey(new Integer(key), exact);
            return new DocumentCollectionIterator(getFactory(), getParent(), collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all documents by key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllDocuments()
     */
    public Iterator getAllDocuments() {
        return new ViewIterator(this);
    }

    /**
     * {@inheritDoc}
     * @see BaseProxy#toString()
     */
    public String toString() {
        return getName();
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllEntries()
     */
    public Iterator getAllEntries() {
        getFactory().preprocessMethod();
        try {
            final ViewEntryCollection coll = getView().getAllEntries();
            return new ViewEntriesIterator(coll);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllEntriesReverse()()
     */
    public Iterator getAllEntriesReverse() {
        getFactory().preprocessMethod();
        try {
            final ViewEntryCollection coll = getView().getAllEntries();
            return new ViewEntriesReverseIterator(coll);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getEntryByKey(java.lang.String)
     */
    public DViewEntry getEntryByKey(final String key) {
        getFactory().preprocessMethod();
        try {
            final ViewEntry entry = getView().getEntryByKey(key);
            return (DViewEntry) ViewEntryProxy.getInstance(getFactory(), this, entry, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getEntryByKey(java.lang.String, boolean)
     */
    public DViewEntry getEntryByKey(final String key, final boolean exact) {
        getFactory().preprocessMethod();
        try {
            final ViewEntry entry = getView().getEntryByKey(key, exact);
            return (DViewEntry) ViewEntryProxy.getInstance(getFactory(), this, entry, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getEntryByKey(java.util.List)
     */
    public DViewEntry getEntryByKey(final List keys) {
        getFactory().preprocessMethod();
        final List convertedKeys = convertCalendarsToNotesDateTime(keys);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            final ViewEntry entry = getView().getEntryByKey(vector);
            return (DViewEntry) ViewEntryProxy.getInstance(getFactory(), this, entry, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getEntryByKey(java.util.List, boolean)
     */
    public DViewEntry getEntryByKey(final List keys, final boolean exact) {
        getFactory().preprocessMethod();
        final List convertedKeys = convertCalendarsToNotesDateTime(keys);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            final ViewEntry entry = getView().getEntryByKey(vector, exact);
            return (DViewEntry) ViewEntryProxy.getInstance(getFactory(), this, entry, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries", e);
        }
    }
    /**
     * {@inheritDoc}
     * @see DView#getAllEntries(DViewEntry)
     */
    public Iterator getAllEntries(final DViewEntry entry) {
        getFactory().preprocessMethod();
        if (entry == null) {
            return new EmptyIterator();
        }
        try {
            final ViewEntry notesViewEntry = (ViewEntry) ((ViewEntryProxy) entry).getNotesObject();
            final ViewNavigator navigator = getView().createViewNavFrom(notesViewEntry);
            return new ViewNavigatorIterator(navigator, entry);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries by key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllEntriesByKey(java.lang.String)
     */
    public Iterator getAllEntriesByKey(final String key) {
        getFactory().preprocessMethod();
        try {
            final ViewEntryCollection coll = getView().getAllEntriesByKey(key);
            return new ViewEntriesIterator(coll);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries by key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllEntriesByKey(java.lang.String, boolean)
     */
    public Iterator getAllEntriesByKey(final String key, final boolean exact) {
        getFactory().preprocessMethod();
        try {
            final ViewEntryCollection coll = getView().getAllEntriesByKey(key, exact);
            return new ViewEntriesIterator(coll);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries by key", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllEntriesByKey(java.util.List)
     */
    public Iterator getAllEntriesByKey(final List keys) {
        getFactory().preprocessMethod();
        final List convertedKeys = convertCalendarsToNotesDateTime(keys);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            final ViewEntryCollection coll = getView().getAllEntriesByKey(vector);
            return new ViewEntriesIterator(coll);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries by key list", e);
        } finally {
            recycleDateTimeList(convertedKeys);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllEntriesByKey(java.util.List, boolean)
     */
    public Iterator getAllEntriesByKey(final List keys, final boolean exact) {
        getFactory().preprocessMethod();
        final List convertedKeys = convertCalendarsToNotesDateTime(keys);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            final ViewEntryCollection coll = getView().getAllEntriesByKey(vector, exact);
            return new ViewEntriesIterator(coll);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries by key", e);
        } finally {
            recycleDateTimeList(convertedKeys);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllEntriesByKey(java.util.Calendar, java.util.Calendar, boolean)
     */
    public Iterator getAllEntriesByKey(final Calendar start, final Calendar end, final boolean exact) {
        getFactory().preprocessMethod();
        DateRange dr = createDateRange(start, end);
        try {
            final ViewEntryCollection coll = getView().getAllEntriesByKey(dr, exact);
            return new ViewEntriesIterator(coll);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get all entries by key", e);
        }
    }


    /**
     * Creates and returns a Proxy for a ViewEntry.
     * Returns <code>null</code> if the <code>viewEntry</code> parameter is <code>null</code>.
     *
     * @param parent the parent object
     * @param viewEntry to Notes ViewEntry
     * @return Proxy for a ViewEntry
     */
    ViewEntryProxy getDViewEntry(final DBase parent, final ViewEntry viewEntry) {
        if (viewEntry == null) {
            return null;
        }
        final ViewEntryProxy entry = ViewEntryProxy.getInstance(getFactory(), parent, viewEntry, getMonitor());
        entry.setMonitor(getMonitor());
        return entry;
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategories()
     */
    public Iterator getAllCategories() {
        return getAllCategories(0);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategories(int)
     */
    public Iterator getAllCategories(final int level) {
        final ViewNavigator nav;
        final ViewEntry entry;
        try {
            nav = getView().createViewNavMaxLevel(level);
            entry = nav.getFirst();
            return new CategoryIterator(nav, entry);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get next item", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategoriesByKey(java.lang.String)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final String key) {
        return getAllCategoriesByKey(key, 0, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategoriesByKey(java.lang.String, int)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final String key, final int level) {
        return getAllCategoriesByKey(key, level, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategoriesByKey(java.lang.String, boolean)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final String key, final boolean exact) {
        return getAllCategoriesByKey(key, 0, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategoriesByKey(java.lang.String, int, boolean)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final String key, final int level, final boolean exact) {
//        final ViewNavigator navigator;
//        final ViewEntry entry;
//        try {
//            navigator = getView().createViewNavMaxLevel(level);
//            entry = getView().getEntryByKey(key, exact);
//            if (!entry.isCategory()) {
//                while (entry != null) {
//                    entry = getDViewEntry(ViewProxy.this, getView().getParent());
//                }
//            }
//            return new CategoryIterator(navigator, entry, key, exact);
//        } catch (NotesException e) {
//            throw newRuntimeException("Cannot get next item", e);
//        }
        return null; // TODO getAllCategoriesByKey()
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategoriesByKey(java.util.List)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final List key) {
        return getAllCategoriesByKey(key, 0, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategoriesByKey(java.util.List, int)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final List key, final int level) {
        return getAllCategoriesByKey(key, level, false);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategoriesByKey(java.util.List, boolean)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final List key, final boolean exact) {
        return getAllCategoriesByKey(key, 0, exact);
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllCategoriesByKey(java.util.List, int, boolean)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final List key, final int level, final boolean exact) {
        final ViewNavigator navigator;
        final ViewEntry entry;
        try {
            navigator = getView().createViewNavMaxLevel(level);
            entry = getView().getEntryByKey(key, exact);
            return new CategoryIterator(navigator, entry, key, exact);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get next item", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#fullTextSearch(java.lang.String)
     */
    public int fullTextSearch(final String query) {
        getFactory().preprocessMethod();
        try {
            return getView().FTSearch(query);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot search documents in view", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#fullTextSearch(java.lang.String, int)
     */
    public int fullTextSearch(final String query, final int maxdocs) {
        getFactory().preprocessMethod();
        try {
            return getView().FTSearch(query, maxdocs);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot search documents in view", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#clear()
     */
    public void clear() {
        getFactory().preprocessMethod();
        try {
            getView().clear();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot clear search in view", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getSelectionFormula()
     */
    public String getSelectionFormula() {
        getFactory().preprocessMethod();
        try {
            return getView().getSelectionFormula();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get selection formula", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#setSelectionFormula(java.lang.String)
     */
    public void setSelectionFormula(final String formula) {
        getFactory().preprocessMethod();
        try {
            getView().setSelectionFormula(formula);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set selection formula", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getColumnNames()
     */
    public List getColumnNames() {
        getFactory().preprocessMethod();
        try {
            return getView().getColumnNames();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get column names", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getColumnNames()
     */
    public int getColumnCount() {
        getFactory().preprocessMethod();
        try {
            return getView().getColumnCount();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get column names", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getColumnNames()
     */
    public DViewColumn getColumn(final int i) {
        getFactory().preprocessMethod();
        try {
            ViewColumn notesViewColumn = getView().getColumn(i);
            final ViewColumnProxy viewColumn = ViewColumnProxy.getInstance(getFactory(), this, notesViewColumn, getMonitor());
            return viewColumn;
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get column names", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getColumnNames()
     */
    public List getColumns() {
        getFactory().preprocessMethod();
        try {
            return getView().getColumns();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get column names", e);
        }
    }

    ////////////////////////////////////////////////
    //    Iterator classes
    ////////////////////////////////////////////////

    /**
     * Uses a ViewNavigator to iterate over all entries in a view,
     * starting with a given entry.
     *
     * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
     */
    public final class ViewNavigatorIterator implements DNotesIterator {

        /** serial version ID for serialization. */
        private static final long serialVersionUID = -5944222847469877866L;

        /** Reference to the internal view navigator. */
        private final ViewNavigator viewNavigator;

        /** Reference to current viewEntry. */
        private DViewEntry viewEntry = null;

        /**
         * Constructs an Iterator using a ViewNavigator and a current entry to start from.
         *
         * @param theViewNavigator the ViewNavigator
         * @param currentEntry the current entry
         */
        protected ViewNavigatorIterator(final ViewNavigator theViewNavigator, final DViewEntry currentEntry) {
            if (theViewNavigator == null) {
                throw newRuntimeException("View navigator missing");
            }
            viewNavigator = theViewNavigator;
            viewEntry = currentEntry;
        }

        /**
         * {@inheritDoc}
         * @see DNotesIterator#getSize()
         */
        public int getSize() {
            return viewNavigator.getCount();
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return viewEntry != null;
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#next()
         */
        public Object next() {
            if (viewEntry == null) {
                throw new NoSuchElementException();
            }
            getFactory().preprocessMethod();
            final DViewEntry result = viewEntry;
            try {
                if (!viewNavigator.gotoNext()) {
                    viewEntry = null;
                } else {
                    viewEntry = getDViewEntry(ViewProxy.this, viewNavigator.getCurrent());
                }
            } catch (NotesException e) {
                throw newRuntimeException("Cannot get next item", e);
            }
            return result;
        }

        /**
         * Throws an UnsupportedOperationException: The <tt>remove</tt>
         * operation is not supported by this Iterator.
         *
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * The items iterator doesn't have to recycle anything.
         * All Items created within this Iterator are recycled by the Notes API.
         *
         * @see java.lang.Object#finalize()
         * @throws Throwable the <code>Exception</code> raised by this method
         */
        protected void finalize() throws Throwable {
            if (viewNavigator != null) {
                getFactory().recycleLater(viewNavigator);
            }
            super.finalize();
        }
    }

    /**
     * A <code>ViewEntriesIterator</code> allows iteration over a set of
     * <code>lotus.domino.ViewEntry</code>s in view order.
     *
     * #see getAllEntries
     *
     * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
     */
    public final class ViewEntriesIterator implements DNotesIterator {

        /** serial version ID for serialization. */
        private static final long serialVersionUID = 2954764194323549206L;

        /** Reference to the internal view entry collection. */
        private final ViewEntryCollection viewEntries;

        /** Reference to current viewEntry. */
        private ViewEntryProxy viewEntry = null;

        /**
         * Constructs an Iterator by use of a vector of
         * <code>lotus.domino.Item</code>s.
         *
         * @param theViewEntries the ViewEntryCollection
         */
        protected ViewEntriesIterator(final ViewEntryCollection theViewEntries) {
            this.viewEntries = theViewEntries;
            initialize();
        }

        /**
         * initialization.
         */
        private void initialize() {
            try {
                if (viewEntries != null && viewEntries.getCount() > 0) {
                    final ViewEntry currentEntry = viewEntries.getFirstEntry();
                    this.viewEntry = getDViewEntry(ViewProxy.this, currentEntry);
                }
            } catch (NotesException e) {
                throw newRuntimeException("Cannot initialize iterator", e);
            }
        }

        /**
         * {@inheritDoc}
         * @see DNotesIterator#getSize()
         */
        public int getSize() {
            try {
                return viewEntries.getCount();
            } catch (NotesException e) {
                throw newRuntimeException("Cannot get size of iterator", e);
            }
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return viewEntry != null;
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#next()
         */
        public Object next() {
            if (viewEntry == null) {
                throw new NoSuchElementException();
            }
            getFactory().preprocessMethod();
            final Object result = viewEntry;
            try {
                final ViewEntry nextViewEntry = viewEntries.getNextEntry();
                viewEntry = getDViewEntry(ViewProxy.this, nextViewEntry);
            } catch (NotesException e) {
                viewEntry = null;
                throw newRuntimeException("Cannot get next item", e);
            }
            return result;
        }

        /**
         * Throws an UnsupportedOperationException: The <tt>remove</tt>
         * operation is not supported by this Iterator.
         *
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * The items iterator doesn't have to recycle anything.
         * All Items created within this Iterator are recycled by the Notes API.
         *
         * @see java.lang.Object#finalize()
         * @throws Throwable the <code>Exception</code> raised by this method
         */
        protected void finalize() throws Throwable {
            if (viewEntries != null) {
                getFactory().recycleLater(viewEntries);
            }
            super.finalize();
        }
    }

    /**
     * A <code>ViewEntriesIterator</code> allows iteration over a set of
     * <code>lotus.domino.ViewEntry</code>s in reverse order.
     *
     * #see getAllEntries
     *
     * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
     */
    public final class ViewEntriesReverseIterator implements DNotesIterator {

        /** serial version ID for serialization. */
        private static final long serialVersionUID = 2954764194323549206L;

        /** Reference to the internal view entry collection. */
        private final ViewEntryCollection viewEntries;

        /** Reference to current viewEntry. */
        private ViewEntryProxy viewEntry = null;

        /**
         * Constructs an Iterator by use of a vector of
         * <code>lotus.domino.Item</code>s.
         *
         * @param theViewEntries the ViewEntryCollection
         */
        protected ViewEntriesReverseIterator(final ViewEntryCollection theViewEntries) {
            this.viewEntries = theViewEntries;
            initialize();
        }

        /**
         * initialization.
         */
        private void initialize() {
            try {
                if (viewEntries != null && viewEntries.getCount() > 0) {
                    final ViewEntry currentEntry = viewEntries.getLastEntry();
                    this.viewEntry = getDViewEntry(ViewProxy.this, currentEntry);
                }
            } catch (NotesException e) {
                throw newRuntimeException("Cannot initialize iterator", e);
            }
        }

        /**
         * {@inheritDoc}
         * @see DNotesIterator#getSize()
         */
        public int getSize() {
            try {
                return viewEntries.getCount();
            } catch (NotesException e) {
                throw newRuntimeException("Cannot get size of iterator", e);
            }
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return viewEntry != null;
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#next()
         */
        public Object next() {
            if (viewEntry == null) {
                throw new NoSuchElementException();
            }
            getFactory().preprocessMethod();
            final Object result = viewEntry;
            try {
                final ViewEntry nextViewEntry = viewEntries.getPrevEntry();
                viewEntry = null;
                viewEntry = getDViewEntry(ViewProxy.this, nextViewEntry);
            } catch (NotesException e) {
                throw newRuntimeException("Cannot get next item: " + e.getMessage());
            }
            return result;
        }

        /**
         * Throws an UnsupportedOperationException: The <tt>remove</tt>
         * operation is not supported by this Iterator.
         *
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * The items iterator doesn't have to recycle anything.
         * All Items created within this Iterator are recycled by the Notes API.
         *
         * @see java.lang.Object#finalize()
         * @throws Throwable the <code>Exception</code> raised by this method
         */
        protected void finalize() throws Throwable {
            if (viewEntries != null) {
                getFactory().recycleLater(viewEntries);
            }
            super.finalize();
        }
    }

    /**
     * A <code>CategoriesIterator</code> allows iteration over a set of
     * <code>lotus.domino.ViewEntry</code>s that are categories.
     *
     * @see #getAllCategories()
     * @see #getAllCategories(int)
     * @see #getAllCategoriesByKey(java.lang.String, int, boolean)
     * @see #getAllCategoriesByKey(java.util.List, int, boolean)
     *
     * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
     */
    public final class CategoryIterator implements Iterator {

        /** Reference to the internal view navigator. */
        private final ViewNavigator fViewNavigator;

        /** The optional key. */
        private final Object fKey;

        /** If the key should match partial or exact. */
        private final boolean fExact;

        /** Reference to current viewEntry. */
        private DViewEntry fNextEntry = null;

        /**
         * Constructs an CategoryIterator.
         *
         * @param theViewNavigator the ViewNavigator
         * @param firstEntry the first entry or null of no entry is available
         */
        protected CategoryIterator(final ViewNavigator theViewNavigator, final ViewEntry firstEntry) {
            fViewNavigator = theViewNavigator;
            fKey = null;
            fExact = false;
            initialize(firstEntry);
        }

        /**
         * Constructor.
         *
         * @param theViewNavigator the ViewNavigator
         * @param firstEntry the first matching entry or null of no entry matching entry is available
         * @param theKey the key, either a String a List
         * @param isExact use true if you want an exact match or false for a partial
         */
        public CategoryIterator(final ViewNavigator theViewNavigator, final ViewEntry firstEntry,
                                final Object theKey, final boolean isExact) {
            fViewNavigator = theViewNavigator;
            fKey = theKey;
            fExact = isExact;
            initialize(firstEntry);
        }

        /**
         * initialization.
         *
         * @param firstEntry the first matching entry or null of no entry matching entry is available
         */
        private void initialize(final ViewEntry firstEntry) {
            getFactory().preprocessMethod();
            if (firstEntry != null) {
                fNextEntry = getDViewEntry(ViewProxy.this, firstEntry);
                try {
                    if (!fViewNavigator.gotoEntry(firstEntry)) {
                        fNextEntry = null;
                    }
                } catch (NotesException e) {
                    throw new DNotesRuntimeException("Entry not found in view");
                }
            }
        }

        /**
         * Indicates whether this iterator has a next element or not.
         *
         * @return boolean true if there is a next
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return fNextEntry != null;
        }

        /**
         * Returns the next element of this iterator
         * or <code>null</code> if <code>hasNext()==false</code>.
         *
         * @return a <code>DBaseItem</code>
         * @see java.util.Iterator#next()
         */
        public Object next() {
            if (fNextEntry == null) {
                throw new NoSuchElementException();
            }
            getFactory().preprocessMethod();
            final DViewEntry next = fNextEntry;
            fNextEntry = getNextEntry();
            return next;
        }

        /**
         * Throws an UnsupportedOperationException: The <tt>remove</tt>
         * operation is not supported by this Iterator.
         *
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Returns the next entry that matches the key
         * or <code>null</code> if no more entries available.
         *
         * @return next item or <code>null</code>
         */
        private DViewEntry getNextEntry() {
            DViewEntry next = fNextEntry;
            while (next != null) {
                try {
                    next = getDViewEntry(ViewProxy.this, fViewNavigator.getNext());
                } catch (NotesException e) {
                    throw newRuntimeException("Cannot get next entry", e);
                }
                if (fKey == null) {
                    return next;
                }
                if (match(next)) {
                    return next;
                }
            }
            return null;
        }

        /**
         * Checks if the column values of a view entry match a given key.
         * @param entry the entry to check
         * @return if matching or not
         */
        private boolean match(final DViewEntry entry) {
            final List valueList = entry.getColumnValues();
            if (valueList.size() == 0) {
                return false;
            }
            if (fKey instanceof List) {
                final List keyList = (List) fKey;
                if (valueList.size() < keyList.size()) {
                    return false;
                }
                for (int i = 0; i < keyList.size() - 1; i++) {
                    if (keyList.get(i) != valueList.get(i)) {
                        return false;
                    }
                }
                final String lastValue = (String) valueList.get(keyList.size());
                final String lastKey = (String) keyList.get(keyList.size());
                if (fExact) {
                    return lastValue.startsWith(lastKey);
                } else {
                    return lastValue.equals(lastKey);
                }
            } else {
                final String value0 = (String) valueList.get(0);
                if (value0 == null) {
                    return false;
                }
                return match(value0, fKey);
            }
        }

        /**
         * Compares a single value with a single key.
         * @param value the value
         * @param key the key
         * @return <tt>true</tt> if the value matches
         */
        private boolean match(final Object value, final Object key) {
            if (fExact) {
                return value.equals(key);
            }
            if (value instanceof String) {
                return partialMatch((String) value, (String) key);
            } else if (value instanceof Calendar) {
                return partialMatch((Calendar) value, (Calendar) key);
            } else if (value instanceof Double) {
                return partialMatch((Double) value, (Double) key);
            }
            return false;
        }

        /**
         * Partially matches a String values.
         * @param value the value
         * @param theKey the key
         * @return <tt>true</tt> if the value partially matches
         */
        private boolean partialMatch(final String value, final String theKey) {
            return value.startsWith(theKey);
        }

        /**
         * Partially matches a calendar value.
         * @param value the value
         * @param theKey the key
         * @return <tt>true</tt> if the value partially matches
         */
        private boolean partialMatch(final Calendar value, final Calendar theKey) {
            return value.after(theKey);
        }

        /**
         * Partially matches a double value.
         * @param value the value
         * @param theKey the key
         * @return <tt>true</tt> if the value partially matches
         */
        private boolean partialMatch(final Double value, final Double theKey) {
            return value.doubleValue() > theKey.doubleValue();
        }

        /**
         * The items iterator doesn't have to recycle anything.
         * All Items created within this Iterator are recycled by the Notes API.
         *
         * @see java.lang.Object#finalize()
         * @throws Throwable the <code>Exception</code> raised by this method
         */
        protected void finalize() throws Throwable {
            if (fViewNavigator != null) {
                getFactory().recycleLater(fViewNavigator);
            }
            super.finalize();
        }
    }

    /**
     * An empty iterator.
     */
    private static class EmptyIterator implements Iterator {

        /** @see java.util.Iterator#hasNext() */
        public boolean hasNext() {
            return false;
        }

        /** @see java.util.Iterator#next() */
        public Object next() {
            throw new NoSuchElementException("EmtyIterator");
        }

        /** @see java.util.Iterator#remove() */
        public void remove() {
            throw new IllegalStateException("EmtyIterator");
        }
    }
}
