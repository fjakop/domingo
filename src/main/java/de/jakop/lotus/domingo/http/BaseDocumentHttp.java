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

package de.jakop.lotus.domingo.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import de.jakop.lotus.domingo.util.Timezones;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DBaseDocument;
import de.jakop.lotus.domingo.DBaseItem;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDateRange;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DEmbeddedObject;
import de.jakop.lotus.domingo.DItem;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.DRichTextItem;
import de.jakop.lotus.domingo.DView;

/**
 * Http implementation of a Domingo view.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class BaseDocumentHttp extends BaseHttp implements DBaseDocument {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1484836582323425207L;

    /** Name of default view (<tt>"0"</tt>). */
    private static final String DEFAULT_VIEW_NAME = "0";

    private String fUniversalId;

    private Map fItemsMap = new HashMap();

    /**
     * Private Constructor for this class.
     *
     * @param factory the controlling factory
     * @param parent the parent object
     * @param unid the universal ID of the document
     * @param monitor the monitor that handles logging
     * @see lotus.domino.Database
     */
    protected BaseDocumentHttp(final NotesHttpFactory factory, final DBase parent, final String unid,
            final DNotesMonitor monitor) {
        super(factory, parent, monitor);
        this.fUniversalId = unid;
        if (unid != null && unid.length() > 0) {
            readDocument();
        }
    }

    /**
     * Private Constructor for this class.
     *
     * @param factory the controlling factory
     * @param parent the parent object
     * @param monitor the monitor that handles logging
     * @see lotus.domino.Database
     */
    protected BaseDocumentHttp(final NotesHttpFactory factory, final DBase parent, final DNotesMonitor monitor) {
        super(factory, parent, monitor);
        this.fUniversalId = "";
    }

    /**
     * Factory method for instances of this class.
     *
     * @param theFactory the controlling factory
     * @param theParent the session that produced the database
     * @param unid the universal id of a Notes document
     * @param monitor the monitor that handles logging
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static DDocument getInstance(final NotesHttpFactory theFactory, final DBase theParent, final String unid,
            final DNotesMonitor monitor) {
        return new DocumentHttp(theFactory, theParent, unid, monitor);
    }

    /**
     * Factory method for instances of this class.
     *
     * @param factory the controlling factory
     * @param parent the session that produced the database
     * @param monitor the monitor that handles logging
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static DDocument getInstance(final NotesHttpFactory factory, final DBase parent,
            final DNotesMonitor monitor) {
        return new DocumentHttp(factory, parent, monitor);
    }

    private void readDocument() {
        if (getDSession().isDomingoAvailable()) {
            readDocumentByXML();
        } else {
            readDocumentByHtml();
        }
    }

    /**
     * Reads a document in HTML format directly from the Http server.
     */
    private void readDocumentByHtml() {
        BaseHttp parent = getParent();
        String viewName;
        if (parent instanceof DView) {
            viewName = ((DView) parent).getName();
            parent = parent.getParent();
        } else {
            viewName = DEFAULT_VIEW_NAME;
        }
        if (!(parent instanceof DDatabase)) {
            throw new NotesHttpRuntimeException("Unsupported parent: " + parent.getClass().getName());
        }
        String databaseName = ((DDatabase) parent).getFileName();
        String url = databaseName + "/" + viewName + "/" + this.fUniversalId + "?EditDocument" + "?OpenDocument";
        try {
            final String bs = executeUrl(url);
            // TODO parse the HTML an extract of the values from all form fields.
        } catch (IOException e) {
            throw new NotesHttpRuntimeException(e);
        }
    }

    /**
     * Reads a document in XML format from the Domingo database.
     */
    private void readDocumentByXML() {
        try {
            final String bs = execute("cmd=ReadDocument&unid=" + fUniversalId);
            final SAXParser parser = getDSession().getFactory().getSAXParserFactory().newSAXParser();
            final DocumentParser documentParser = new DocumentParser();
            parser.parse(new ByteArrayInputStream(bs.getBytes()), documentParser);
        } catch (IOException e) {
            throw new NotesHttpRuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new NotesHttpRuntimeException(e);
        } catch (SAXException e) {
            throw new NotesHttpRuntimeException(e);
        }
    }

    /**
     * @see Object#toString()
     * @return a string representation of the object.
     */
    public final String toString() {
        return "[" + fUniversalId + ", " + fItemsMap.values() + "]";
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getParentDatabase()
     */
    public final DDatabase getParentDatabase() {
        return (DDatabase) getParent();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getFirstItem(java.lang.String)
     */
    public final DBaseItem getFirstItem(final String name) {
        return (DBaseItem) fItemsMap.get(name);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItems()
     */
    public final Iterator getItems() {
        return fItemsMap.values().iterator();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getCreated()
     */
    public final Calendar getCreated() {
        throw new UnsupportedOperationException("getceated()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String)
     */
    public final DItem appendItemValue(final String name) {
        return appendItemValue(name, "");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String,
     *      java.lang.String)
     */
    public final DItem appendItemValue(final String name, final String value) {
        throw new UnsupportedOperationException("appendItemValue");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String, int)
     */
    public final DItem appendItemValue(final String name, final int value) {
        throw new UnsupportedOperationException("appendItemValue");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String,
     *      double)
     */
    public final DItem appendItemValue(final String name, final double value) {
        throw new UnsupportedOperationException("appendItemValue");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String,
     *      java.util.Calendar)
     */
    public final DItem appendItemValue(final String name, final Calendar value) {
        throw new UnsupportedOperationException("appendItemValue");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String,
     *      java.util.List)
     */
    public final DItem appendItemValue(final String name, final List values) {
        throw new UnsupportedOperationException("appendItemValue");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.lang.String)
     */
    public final DItem replaceItemValue(final String name, final String value) {
        final List list = new ArrayList();
        list.add(value);
        return replaceItemValue(name, list);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String, int)
     */
    public final DItem replaceItemValue(final String name, final int value) {
        final List list = new ArrayList();
        list.add(new Integer(value));
        return replaceItemValue(name, list);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.lang.Integer)
     */
    public final DItem replaceItemValue(final String name, final Integer value) {
        final List list = new ArrayList();
        list.add(value);
        return replaceItemValue(name, list);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      double)
     */
    public final DItem replaceItemValue(final String name, final double value) {
        final List list = new ArrayList();
        list.add(new Double(value));
        return replaceItemValue(name, list);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.lang.Double)
     */
    public final DItem replaceItemValue(final String name, final Double value) {
        List values = new ArrayList(1);
        values.add(0, value);
        return replaceItemValue(name, values);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.util.Calendar)
     */
    public final DItem replaceItemValue(final String name, final Calendar value) {
        List values = new ArrayList();
        values.add(0, value);
        return replaceItemValue(name, values);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String, java.util.TimeZone)
     */
    public final DItem replaceItemValue(final String name, final TimeZone value) {
        String s = "";
        if (value == null) {
            getMonitor().warn("time zone is null; storing an empty string in item " + name);
        } else {
            s = Timezones.getLotusTimeZoneString(value);
            if (s.startsWith("Unknown")) {
                getMonitor().warn("Unknown time zone identifier (using default): " + value.getID());
            }
        }
        return replaceItemValue(name, s);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      DDateRange)
     */
    public final DItem replaceItemValue(final String name, final DDateRange value) {
        List values = new ArrayList(2);
        values.add(0, value.getFrom());
        values.add(1, value.getTo());
        return replaceItemValue(name, values);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.util.Calendar, java.util.Calendar)
     */
    public final DItem replaceItemValue(final String name, final Calendar calendar1, final Calendar calendar2) {
        List values = new ArrayList(2);
        values.add(0, calendar1);
        values.add(1, calendar2);
        return replaceItemValue(name, values);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.util.List)
     */
    public final DItem replaceItemValue(final String name, final List values) {
        DItem item = (DItem) fItemsMap.get(name);
        if (item == null) {
            item = new ItemHttp(getFactory(), this, name, values, getMonitor());
            fItemsMap.put(name, item);
        } else {
            item.setValues(values);
        }
        return item;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#save()
     */
    public final boolean save() throws DNotesRuntimeException {
        return save(true, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#save(boolean)
     */
    public final boolean save(final boolean force) throws DNotesRuntimeException {
        return save(force, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getAttachment(java.lang.String)
     */
    public final DEmbeddedObject getAttachment(final String filename) {
        throw new UnsupportedOperationException("getAttachment()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getEmbeddedObjects()
     */
    public final Iterator getEmbeddedObjects() {
        throw new UnsupportedOperationException("getEmbeddedObjects()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getAttachments()
     */
    public final List getAttachments() {
        throw new UnsupportedOperationException("getAttachments()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#removeItem(java.lang.String)
     */
    public final void removeItem(final String name) {
        fItemsMap.remove(name);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#remove()
     */
    public final boolean remove() {
        return remove(false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#remove(boolean)
     */
    public final boolean remove(final boolean force) {
        if (fUniversalId != null && fUniversalId.length() > 0) {
            final String path = getParentDatabase().getFilePath().replace('\\', '/');
            try {
                final String result = execute(path + "/0/" + fUniversalId, "DeleteDocument");
                return result.indexOf("Document deleted") >= 0;
            } catch (IOException e) {
                throw new NotesHttpRuntimeException("Cannot delete document: " + e.getMessage(), e);
            }
        } else {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#createRichTextItem(java.lang.String)
     */
    public final DRichTextItem createRichTextItem(final String name) {
        throw new UnsupportedOperationException("createRichTextItem()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValue(java.lang.String)
     */
    public final List getItemValue(final String name) {
        final DItem item = ((DItem) fItemsMap.get(name));
        return item == null ? new ArrayList(0) : item.getValues();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueString(java.lang.String)
     */
    public final String getItemValueString(final String name) {
        final DItem item = ((DItem) fItemsMap.get(name));
        return item == null ? "" : item.getValues().get(0).toString();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueDate(java.lang.String)
     */
    public final Calendar getItemValueDate(final String name) {
        final DItem item = ((DItem) fItemsMap.get(name));
        return (Calendar) (item == null ? null : item.getValues().get(0));
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueDateRange(java.lang.String)
     */
    public final DDateRange getItemValueDateRange(final String name) {
        final DItem item = ((DItem) fItemsMap.get(name));
        return item == null ? null : item.getValueDateRange();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueInteger(java.lang.String)
     */
    public final Integer getItemValueInteger(final String name) {
        final DItem item = ((DItem) fItemsMap.get(name));
        return (Integer) (item == null ? "" : item.getValues().get(0));
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueDouble(java.lang.String)
     */
    public final Double getItemValueDouble(final String name) {
        final DItem item = ((DItem) fItemsMap.get(name));
        return (Double) (item == null ? "" : item.getValues().get(0));
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#hasItem(java.lang.String)
     */
    public final boolean hasItem(final String name) {
        return fItemsMap.containsKey(name);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getAuthors()
     */
    public final List getAuthors() {
        throw new UnsupportedOperationException("getAuthors()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getLastAccessed()
     */
    public final Calendar getLastAccessed() {
        throw new UnsupportedOperationException("getLastAccessed()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getLastModified()
     */
    public final Calendar getLastModified() {
        throw new UnsupportedOperationException("getLastModified()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueSize(java.lang.String)
     */
    public final int getItemValueSize(final String name) {
        return ((ItemHttp) getFirstItem(name)).getSize();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#copyAllItems(DBaseDocument,
     *      boolean)
     */
    public final void copyAllItems(final DBaseDocument doc, final boolean replace) {
        Iterator iterator = fItemsMap.values().iterator();
        while (iterator.hasNext()) {
            DItem item = (DItem) iterator.next();
            doc.appendItemValue(item.getName(), item.getValues());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#computeWithForm(boolean)
     */
    public final boolean computeWithForm(final boolean raiseError) {
        throw new UnsupportedOperationException("computeWithForm()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#computeWithForm()
     */
    public final boolean computeWithForm() {
        throw new UnsupportedOperationException("computeWithForm()");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#recycle()
     */
    public final void recycle() {
    }

    /**
     * SAX parser for documents.
     */
    private class DocumentParser extends BaseHandler {

        private String fName;

        private boolean fNames;

        private boolean fReaders;

        private boolean fAuthors;

        public final void startElement(final String namespaceURI, final String localName, final String qName,
                final Attributes atts)
                throws SAXException {
            if ("item".equals(qName)) {
                fName = atts.getValue("name");
                String n = atts.getValue("names");
                fNames = n != null && n.equals("true");
                String r = atts.getValue("readers");
                fReaders = r != null && r.equals("true");
                String a = atts.getValue("authors");
                fAuthors = a != null && a.equals("true");
                reset();
            } else {
                super.startElement(namespaceURI, localName, qName, atts);
            }
        }

        public final void endElement(final String uri, final String localName, final String qName) throws SAXException {
            if ("item".equals(qName)) {
                final DItem item = replaceItemValue(fName, getValues());
                item.setNames(fNames);
                item.setReaders(fReaders);
                item.setAuthors(fAuthors);
            } else {
                super.endElement(uri, localName, qName);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#copyItem(DBaseItem,
     *      java.lang.String)
     */
    public final DBaseItem copyItem(final DBaseItem item, final String s) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#copyItem(DBaseItem)
     */
    public final DBaseItem copyItem(final DBaseItem item) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#createReplyMessage(boolean)
     */
    public final DDocument createReplyMessage(final boolean flag) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#encrypt()
     */
    public final void encrypt() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getColumnValues()
     */
    public final List getColumnValues() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getEncryptionKeys()
     */
    public final List getEncryptionKeys() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#setEncryptionKeys(java.util.List)
     */
    public final void setEncryptionKeys(final List keys) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueCustomData(java.lang.String)
     */
    public final Object getItemValueCustomData(final String name) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueCustomData(java.lang.String,
     *      java.lang.String)
     */
    public final Object getItemValueCustomData(final String name, final String dataTypeName) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueCustomDataBytes(java.lang.String,
     *      java.lang.String)
     */
    public final byte[] getItemValueCustomDataBytes(final String name, final String dataTypeName) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueDateTimeArray(java.lang.String)
     */
    public final List getItemValueDateTimeArray(final String name) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getSigner()
     */
    public final String getSigner() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getSize()
     */
    public final int getSize() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#setUniversalID(java.lang.String)
     */
    public final void setUniversalID(final String unid) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getVerifier()
     */
    public final String getVerifier() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#hasEmbedded()
     */
    public final boolean hasEmbedded() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isEncrypted()
     */
    public final boolean isEncrypted() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isEncryptOnSend()
     */
    public final boolean isEncryptOnSend() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isProfile()
     */
    public final boolean isProfile() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isSigned()
     */
    public final boolean isSigned() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isValid()
     */
    public final boolean isValid() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isSaveMessageOnSend()
     */
    public final boolean isSaveMessageOnSend() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isSentByAgent()
     */
    public final boolean isSentByAgent() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isSignOnSend()
     */
    public final boolean isSignOnSend() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isDeleted()
     */
    public final boolean isDeleted() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#removePermanently(boolean)
     */
    public final boolean removePermanently(final boolean flag) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#renderToRTItem(DRichTextItem)
     */
    public final boolean renderToRTItem(final DRichTextItem richtextitem) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValueCustomData(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public final DItem replaceItemValueCustomData(final String s, final String s1, final Object obj) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValueCustomData(java.lang.String,
     *      java.lang.Object)
     */
    public final DItem replaceItemValueCustomData(final String s, final Object obj) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValueCustomDataBytes(java.lang.String,
     *      java.lang.String, byte[])
     */
    public final DItem replaceItemValueCustomDataBytes(final String s, final String s1, final byte[] abyte0) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#generateXML()
     */
    public final String generateXML() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#generateXML(java.io.Writer)
     */
    public final void generateXML(final Writer writer) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getReceivedItemText()
     */
    public final List getReceivedItemText() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getLockHolders()
     */
    public final List getLockHolders() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock()
     */
    public final boolean lock() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(boolean)
     */
    public final boolean lock(final boolean provisionalOk) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(java.lang.String)
     */
    public final boolean lock(final String name) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(java.lang.String, boolean)
     */
    public final boolean lock(final String name, final boolean provisionalOk) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(java.util.List)
     */
    public final boolean lock(final List names) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(java.util.List, boolean)
     */
    public final boolean lock(final List names, final boolean provisionalOk) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lockProvisional()
     */
    public final boolean lockProvisional() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lockProvisional(java.lang.String)
     */
    public final boolean lockProvisional(final String name) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lockProvisional(java.util.List)
     */
    public final boolean lockProvisional(final List names) {
        throw new UnsupportedOperationException("not supported in Http Document");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#unlock()
     */
    public final void unlock() {
        throw new UnsupportedOperationException("not supported in Http Document");
    }
}
