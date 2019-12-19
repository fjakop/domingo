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

package de.jakop.lotus.domingo.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.DViewColumn;
import de.jakop.lotus.domingo.DViewEntry;

/**
 * Http implementation of a Domingo view.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ViewHttp extends BaseHttp implements DView {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1484836582323425207L;

    private String fName;

    /**
     * Private Constructor for this class.
     *
     * @param theFactory the controlling factory
     * @param session the session that produced the database
     * @param database Notes database object
     * @param monitor the monitor that handles logging
     * @param forceOpen whether the database should be forced to be open or not
     * @see lotus.domino.Database
     */
    private ViewHttp(final NotesHttpFactory theFactory, final DBase theParent, final String name, final DNotesMonitor monitor) {
        super(theFactory, theParent, monitor);
        this.fName = name;
    }

    /**
     * Factory method for instances of this class.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param name the name of the view
     * @param monitor the monitor that handles logging
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static DView getInstance(final NotesHttpFactory theFactory, final DBase theParent, final String name,
            final DNotesMonitor monitor) {
        return new ViewHttp(theFactory, theParent, name, monitor);
    }

    /**
     * Private accessor method to enclosing view for inner classes.
     *
     * @return this
     */
    private DView getView() {
        return this;
    }

    /**
     * Returns the parent database
     *
     * @return parent database
     */
    private DDatabase getDatabase() {
        return (DDatabase) getParent();
    }

    /**
     * @see Object#toString()
     * @return a string representation of the object.
     */
    public String toString() {
        return fName;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#refresh()
     */
    public void refresh() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getName()
     */
    public String getName() {
        return fName;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getDocumentByKey(String, boolean)
     */
    public DDocument getDocumentByKey(final String key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getDocumentByKey(List, boolean)
     */
    public DDocument getDocumentByKey(final List keys, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(String)
     */
    public Iterator getAllDocumentsByKey(final String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(List)
     */
    public Iterator getAllDocumentsByKey(final List keys) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(String, boolean)
     */
    public Iterator getAllDocumentsByKey(final String key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(List, boolean)
     */
    public Iterator getAllDocumentsByKey(final List keys, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(java.util.Calendar)
     */
    public Iterator getAllDocumentsByKey(final Calendar key) {
        return getAllDocumentsByKey(key, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(double)
     */
    public Iterator getAllDocumentsByKey(final double key) {
        return getAllDocumentsByKey(key, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(int)
     */
    public Iterator getAllDocumentsByKey(final int key) {
        return getAllDocumentsByKey(key, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(java.util.Calendar, boolean)
     */
    public Iterator getAllDocumentsByKey(final Calendar key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(double, boolean)
     */
    public Iterator getAllDocumentsByKey(final double key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocumentsByKey(int, boolean)
     */
    public Iterator getAllDocumentsByKey(final int key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllDocuments()
     */
    public Iterator getAllDocuments() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllEntries()
     */
    public Iterator getAllEntries() {
        return new ViewEntryIteratorHttp();
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllEntriesReverse()
     */
    public Iterator getAllEntriesReverse() {
        return new ViewEntryIteratorHttp();
    }

    private final class ViewEntryIteratorHttp implements Iterator {

        private static final int DEFAULT_COUNT = 50;

        /** Current position in view. */
        private String fStart = "1";

        /** Current position in cache array. */
        private int fCachePosition = 0;

        /** Position of last read entry. */
        private String fLastPosition = "0";

        /** List of cached entries. */
        private List fViewEntries;

        public ViewEntryIteratorHttp() {
            readNextPage(null);
        }

        public ViewEntryIteratorHttp(final String startKey) {
            readNextPage(startKey);
        }

        /**
         * {@inheritDoc}
         *
         * @see Iterator#hasNext()
         */
        public boolean hasNext() {
            if (fCachePosition >= fViewEntries.size()) {
                readNextPage(null);
            }
            return fCachePosition < fViewEntries.size();
        }

        /**
         * {@inheritDoc}
         *
         * @see Iterator#next()
         */
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return fViewEntries.get(fCachePosition++);
        }

        /**
         * {@inheritDoc}
         *
         * @see Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Reads the next page of view entries from the view.
         *
         * @throws IOException if the next page cannot be read
         */
        private void readNextPage(final String startKey) {
            final String path = getDatabase().getFilePath();
            try {
                final String arguments;
                if (startKey != null && startKey.length() > 0) {
                    arguments = "StartKey=" + startKey;
                } else {
                    fStart = increase(fLastPosition);
                    arguments = "Start=" + fStart + "&Count=" + DEFAULT_COUNT;
                }
                final String bs = execute(path + "/" + fName, "ReadViewEntries&expandview&" + arguments);
                final SAXParser parser = getDSession().getFactory().getSAXParserFactory().newSAXParser();
                final ViewEntriesParser viewEntriesParser = new ViewEntriesParser();
                // TODO before parsing, check that answer is valid to parse as XML
                // todo e.g. check if content-tyoe is XML
                // todo e.g. check if it is a login form (has FORM tag with action arg containing 'Login'
                parser.parse(new ByteArrayInputStream(bs.getBytes()), viewEntriesParser);
                // todo catch parser exception here and signal error
                fViewEntries = viewEntriesParser.getViewEntries();
                if (fViewEntries.size() > 0) {
                    DViewEntry viewEntry = (DViewEntry) fViewEntries.get(fViewEntries.size() - 1);
                    fLastPosition = viewEntry.getPosition('.');
                }
                fCachePosition  = 0;
            } catch (IOException e) {
                throw new NotesHttpRuntimeException(e);
            } catch (ParserConfigurationException e) {
                throw new NotesHttpRuntimeException(e);
            } catch (SAXException e) {
                throw new NotesHttpRuntimeException(e);
            }
        }
    }


    /**
     * Increases a position string to the next position. In case of
     * hierarchical positions of categoized views, the rightmost position
     * number is increased.
     *
     * Example: <tt>12.3.7</tt> -> <tt>12.3.8</tt>
     *
     * @param position a position string
     * @return increased position string
     */
    public static String increase(final String position) {
        int i = position.lastIndexOf('.');
        if (i < 0) {
            return String.valueOf(Integer.parseInt(position) + 1);
        } else {
            return position.substring(0, i + 1) + String.valueOf(Integer.parseInt(position.substring(i + 1)) + 1);
        }
    }

    /**
     * SAX parser for view entries.
     */
    private class ViewEntriesParser extends BaseHandler {

        private List fViewEntries;

        private ViewEntryHttp fViewEntry;

        public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes atts)
                throws SAXException {
            if (fViewEntry != null) {
                fViewEntry.getParser().startElement(namespaceURI, localName, qName, atts);
            } else if ("viewentries".equals(qName)) {
                fViewEntries = new ArrayList();
            } else if ("viewentry".equals(qName)) {
                fViewEntry = (ViewEntryHttp) ViewEntryHttp.getInstance(getFactory(), getView(), getMonitor());
                fViewEntry.getParser().startElement(namespaceURI, localName, qName, atts);
            } else {
                super.startElement(namespaceURI, localName, qName, atts);
            }
        }

        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            if ("viewentries".equals(qName)) {
                return;
            } else if ("viewentry".equals(qName)) {
                fViewEntry.getParser().endElement(uri, localName, qName);
                fViewEntries.add(fViewEntry);
                fViewEntry = null;
            } else if (fViewEntry != null) {
                fViewEntry.getParser().endElement(uri, localName, qName);
            } else {
                super.endElement(uri, localName, qName);
            }
        }

        /**
         * @see BaseHandler#characters(char[], int, int)
         */
        public void characters(final char[] ch, final int start, final int length) throws SAXException {
            if (fViewEntry != null) {
                fViewEntry.getParser().characters(ch, start, length);
            } else {
                super.characters(ch, start, length);
            }
        }

        /**
         * Returns the parsed view entries. Only call this method after parsing
         * is completed.
         *
         * @return parsed view entries
         */
        public List getViewEntries() {
            return fViewEntries;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getEntryByKey(String)
     */
    public DViewEntry getEntryByKey(final String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getEntryByKey(String, boolean)
     */
    public DViewEntry getEntryByKey(final String key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getEntryByKey(List)
     */
    public DViewEntry getEntryByKey(final List keys) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getEntryByKey(List, boolean)
     */
    public DViewEntry getEntryByKey(final List keys, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllEntries(DViewEntry)
     */
    public Iterator getAllEntries(final DViewEntry entry) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllEntriesByKey(String)
     */
    public Iterator getAllEntriesByKey(final String key) {
        return new ViewEntryIteratorHttp(key);
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllEntriesByKey(String, boolean)
     */
    public Iterator getAllEntriesByKey(final String key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllEntriesByKey(List)
     */
    public Iterator getAllEntriesByKey(final List key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllEntriesByKey(List, boolean)
     */
    public Iterator getAllEntriesByKey(final List key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategories()
     */
    public Iterator getAllCategories() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategories(int)
     */
    public Iterator getAllCategories(final int level) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategoriesByKey(String)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategoriesByKey(String, int)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final String key, final int level) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategoriesByKey(String, boolean)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final String key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategoriesByKey(String, int, boolean)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final String key, final int level, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategoriesByKey(List)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final List key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategoriesByKey(List, int)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final List key, final int level) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategoriesByKey(List, boolean)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final List key, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getAllCategoriesByKey(List, int, boolean)
     * @deprecated
     */
    public Iterator getAllCategoriesByKey(final List key, final int level, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#fullTextSearch(java.lang.String)
     */
    public int fullTextSearch(final String query) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#fullTextSearch(java.lang.String, int)
     */
    public int fullTextSearch(final String query, final int maxdocs) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#clear()
     */
    public void clear() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#getSelectionFormula()
     */
    public String getSelectionFormula() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DView#setSelectionFormula(java.lang.String)
     */
    public void setSelectionFormula(final String formula) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * @see DView#getColumn(int)
     */
    public DViewColumn getColumn(final int i) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see DView#getColumnCount()
     */
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     * @see DView#getColumnNames()
     */
    public List getColumnNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see DView#getColumns()
     */
    public List getColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see DView#getAllEntriesByKey(java.util.Calendar, java.util.Calendar, boolean)
     */
    public Iterator getAllEntriesByKey(final Calendar start, final Calendar end, final boolean exact) {
        // TODO Auto-generated method stub
        return null;
    }
}
