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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DViewEntry;

/**
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ViewEntryHttp extends BaseHttp implements DViewEntry {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = -3064457774812021395L;

    private boolean fConflict;

    private String fUniversalId;

    private String fNoteId;

    private boolean fDocument;

    private boolean fCategory;

    private int fChildren;

    private int fDescendents;

    private int fSibblings;

    private List fColumnValues;

    private boolean fTotal;

    private BaseHandler fParser = new ViewEntryParser();

    private String fPosition;

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
    private ViewEntryHttp(final NotesHttpFactory theFactory, final DBase theParent, final DNotesMonitor monitor) {
        super(theFactory, theParent, monitor);
    }

    /**
     * Factory method for instances of this class.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param monitor the monitor that handles logging
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static DViewEntry getInstance(final NotesHttpFactory theFactory, final DBase theParent, final DNotesMonitor monitor) {
        return new ViewEntryHttp(theFactory, theParent, monitor);
    }

    /**
     * Returns a SAX parser for the current view entry.
     *
     * @return a SAX parser
     */
    protected BaseHandler getParser() {
        return fParser;
    }

    /**
     * @see java.lang.Object#toString()
     * @return a string representation of the object.
     */
    public String toString() {
        return "[" + fUniversalId + ", " + fColumnValues + "]";
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getColumnValues()
     */
    public List getColumnValues() {
        return fColumnValues;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getDocument()
     */
    public DDocument getDocument() {
        return DocumentHttp.getInstance(getFactory(), getParent().getParent(), fUniversalId, getMonitor());
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isCategory()
     */
    public boolean isCategory() {
        return fCategory;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isDocument()
     */
    public boolean isDocument() {
        return fDocument;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isTotal()
     */
    public boolean isTotal() {
        return fTotal;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getUniversalID()
     */
    public String getUniversalID() {
        return fUniversalId;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getChildCount()
     */
    public int getChildCount() {
        return fChildren;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isConflict()
     */
    public boolean isConflict() {
        return fConflict;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getDescendantCount()
     */
    public int getDescendantCount() {
        return fDescendents;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getSiblingCount()
     */
    public int getSiblingCount() {
        return fSibblings;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getSibblingCount()
    * @deprecated use method {@link #getSiblingCount()} instead
     */
    public int getSibblingCount() {
        return fSibblings;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getIndentLevel()
     */
    public int getIndentLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isValid()
     */
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getNoteID()
     */
    public String getNoteID() {
        return fNoteId;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getPosition(char)
     */
    public String getPosition(final char seperator) {
        return fPosition.replace('.', seperator);
    }

    /**
     * SAX parser for view entries.
     */
    class ViewEntryParser extends BaseHandler {

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
         *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes atts)
                throws SAXException {
            if ("viewentry".equals(qName)) {
                fUniversalId = atts.getValue("unid");
                fDescendents = parseInt(atts, "descendents");
                fSibblings = parseInt(atts, "sibblings");
                fChildren = parseInt(atts, "children");
                fPosition = atts.getValue("position");
                fColumnValues = new ArrayList();
            } else if ("entrydata".equals(qName)) {
                reset();
                if ("true".equals(atts.getValue("category"))) {
                    fCategory = true;
                }
            } else {
                super.startElement(namespaceURI, localName, qName, atts);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String,
         *      java.lang.String)
         */
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            if ("viewentry".equals(qName)) {
                return;
            } else if ("entrydata".equals(qName)) {
                fColumnValues.add(getValues());
            } else {
                super.endElement(uri, localName, qName);
            }
        }

        /**
         * Parses an attribute value to a number, null-values are converted to
         * zero.
         *
         * @param atts Attributes
         * @param name name of attribute to parse
         * @return attribute value, parsed as integer
         */
        private int parseInt(final Attributes atts, final String name) {
            String value = atts.getValue(name);
            return value == null ? 0 : Integer.parseInt(value);
        }
    }
}
