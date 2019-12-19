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

package de.jakop.lotus.domingo.mock;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import de.jakop.lotus.domingo.DBaseDocument;
import de.jakop.lotus.domingo.DBaseItem;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDateRange;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DEmbeddedObject;
import de.jakop.lotus.domingo.DItem;
import de.jakop.lotus.domingo.DRichTextItem;
import de.jakop.lotus.domingo.DView;

/**
 * Transient mock implementation of interface DDocument.
 *
 * @author <a href=mailto:christian.wied@bea.de>Christian Wied</a>
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class MockDocument implements DDocument {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257009860418679352L;

    /** List of authors of a document. */
    private List authors = new ArrayList();

    /** Map of all items of a document. */
    private Map items = new Hashtable();

    /**
     * {@inheritDoc}
     *
     * @see DDocument#isNewNote()
     */
    public boolean isNewNote() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getUniversalID()
     */
    public String getUniversalID() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getNoteID()
     */
    public String getNoteID() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#send(java.lang.String)
     */
    public void send(final String recipient) {
        throw new UnsupportedOperationException("send() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#send(java.util.List)
     */
    public void send(final List recipients) {
        throw new UnsupportedOperationException("send() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#copyToDatabase(DDatabase)
     */
    public DDocument copyToDatabase(final DDatabase database) {
        throw new UnsupportedOperationException("copyToDatabase() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#isResponse()
     */
    public boolean isResponse() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getParentDocumentUNID()
     */
    public String getParentDocumentUNID() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getParentDocument()
     */
    public DDocument getParentDocument() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#makeResponse(DDocument)
     */
    public void makeResponse(final DDocument doc) {
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getResponses()
     */
    public Iterator getResponses() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#setSaveMessageOnSend(boolean)
     */
    public void setSaveMessageOnSend(final boolean saveMessageOnSend) {
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#setEncryptOnSend(boolean)
     */
    public void setEncryptOnSend(final boolean flag) {
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#setSignOnSend(boolean)
     */
    public void setSignOnSend(final boolean flag) {
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#sign()
     */
    public void sign() {
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getFirstItem(java.lang.String)
     */
    public DBaseItem getFirstItem(final String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItems()
     */
    public Iterator getItems() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getCreated()
     */
    public Calendar getCreated() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String)
     */
    public DItem appendItemValue(final String name) {
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String,
     *      java.lang.String)
     */
    public DItem appendItemValue(final String name, final String value) {
        List valueList = new ArrayList(1);
        valueList.add(0, value);
        items.put(name.toLowerCase(), valueList);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String, int)
     */
    public DItem appendItemValue(final String name, final int value) {
        List valueList = new ArrayList(1);
        valueList.add(0, new Integer(value));
        items.put(name.toLowerCase(), valueList);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String,
     *      double)
     */
    public DItem appendItemValue(final String name, final double value) {
        List valueList = new ArrayList(1);
        valueList.add(0, new Double(value));
        items.put(name.toLowerCase(), valueList);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String,
     *      java.util.Calendar)
     */
    public DItem appendItemValue(final String name, final Calendar value) {
        List valueList = new ArrayList(1);
        valueList.add(0, value);
        items.put(name.toLowerCase(), valueList);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#appendItemValue(java.lang.String,
     *      java.util.List)
     */
    public DItem appendItemValue(final String name, final List values) {
        items.remove(name.toLowerCase());
        items.put(name.toLowerCase(), values);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.lang.String)
h     */
    public DItem replaceItemValue(final String name, final String value) {
        items.remove(name.toLowerCase());
        List valueList = new ArrayList(1);
        valueList.add(0, value);
        items.put(name.toLowerCase(), valueList);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String, int)
     */
    public DItem replaceItemValue(final String name, final int value) {
        items.remove(name.toLowerCase());
        List valueList = new ArrayList(1);
        valueList.add(0, new Integer(value));
        items.put(name.toLowerCase(), valueList);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      double)
     */
    public DItem replaceItemValue(final String name, final double value) {
        items.remove(name.toLowerCase());
        List valueList = new ArrayList(1);
        valueList.add(0, new Double(value));
        items.put(name.toLowerCase(), valueList);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.util.Calendar)
     */
    public DItem replaceItemValue(final String name, final Calendar value) {
        items.remove(name.toLowerCase());
        List valueList = new ArrayList(1);
        valueList.add(0, value);
        items.put(name.toLowerCase(), valueList);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.util.List)
     */
    public DItem replaceItemValue(final String name, final List values) {
        items.remove(name.toLowerCase());
        items.put(name.toLowerCase(), values);
        return new MockItem();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#save()
     */
    public boolean save() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#save(boolean)
     */
    public boolean save(final boolean force) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#save(boolean, boolean)
     */
    public boolean save(final boolean force, final boolean makeresponse) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getAttachment(java.lang.String)
     */
    public DEmbeddedObject getAttachment(final String filename) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getEmbeddedObjects()
     */
    public Iterator getEmbeddedObjects() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getAttachments()
     */
    public List getAttachments() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#removeItem(java.lang.String)
     */
    public void removeItem(final String name) {
        items.remove(name.toLowerCase());
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#remove()
     */
    public boolean remove() {
        return remove(false);
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#remove(boolean)
     */
    public boolean remove(final boolean force) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#createRichTextItem(java.lang.String)
     */
    public DRichTextItem createRichTextItem(final String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValue(java.lang.String)
     */
    public List getItemValue(final String name) {
        return (List) items.get(name.toLowerCase());
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueString(java.lang.String)
     */
    public String getItemValueString(final String name) {
        List valueList = (List) items.get(name.toLowerCase());
        if (valueList != null && valueList.size() > 0) {
            return valueList.get(0).toString();
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueDate(java.lang.String)
     */
    public Calendar getItemValueDate(final String name) {
        List valueList = (List) items.get(name.toLowerCase());
        if (valueList != null && valueList.size() > 0 && valueList.get(0) instanceof Calendar) {
            return (Calendar) valueList.get(0);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueInteger(java.lang.String)
     */
    public Integer getItemValueInteger(final String name) {
        List valueList = (List) items.get(name.toLowerCase());
        if (valueList != null && valueList.size() > 0) {
            if (valueList.get(0) instanceof Integer) {
                return (Integer) valueList.get(0);
            } else if (valueList.get(0) instanceof Number) {
                return new Integer(((Number) valueList.get(0)).intValue());
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueDouble(java.lang.String)
     */
    public Double getItemValueDouble(final String name) {
        List valueList = (List) items.get(name.toLowerCase());
        if (valueList != null && valueList.size() > 0) {
            if (valueList.get(0) instanceof Double) {
                return (Double) valueList.get(0);
            } else if (valueList.get(0) instanceof Number) {
                return new Double(((Number) valueList.get(0)).doubleValue());
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#hasItem(java.lang.String)
     */
    public boolean hasItem(final String name) {
        return items.containsKey(name.toLowerCase());
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getAuthors()
     */
    public List getAuthors() {
        return authors;
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getLastAccessed()
     */
    public Calendar getLastAccessed() {
        return Calendar.getInstance();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getLastModified()
     */
    public Calendar getLastModified() {
        return Calendar.getInstance();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.lang.Integer)
     */
    public DItem replaceItemValue(final String name, final Integer value) {
        return replaceItemValue(name.toLowerCase(), value.intValue());
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.lang.Double)
     */
    public DItem replaceItemValue(final String name, final Double value) {
        return replaceItemValue(name.toLowerCase(), value.doubleValue());
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueSize(java.lang.String)
     */
    public int getItemValueSize(final String name) {
        return getItemValue(name.toLowerCase()).size();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getParentDatabase()
     */
    public DDatabase getParentDatabase() {
        throw new UnsupportedOperationException("getParentDatabase() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return items.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#copyAllItems(DBaseDocument,
     *      boolean)
     */
    public void copyAllItems(final DBaseDocument doc, final boolean replace) {
        throw new UnsupportedOperationException("copyAllItems() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#recycle()
     */
    public void recycle() {
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      DDateRange)
     */
    public DItem replaceItemValue(final String name, final DDateRange value) {
        throw new UnsupportedOperationException("replaceItemValue() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String, java.util.TimeZone)
     */
    public DItem replaceItemValue(final String name, final TimeZone value) {
        throw new UnsupportedOperationException("replaceItemValue() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueDateRange(java.lang.String)
     */
    public DDateRange getItemValueDateRange(final String name) {
        throw new UnsupportedOperationException("getItemValueDateRange() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValue(java.lang.String,
     *      java.util.Calendar, java.util.Calendar)
     */
    public DItem replaceItemValue(final String name, final Calendar calendar1, final Calendar calendar2) {
        throw new UnsupportedOperationException("replaceItemValue() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#replaceHTML(java.lang.String,
     *      java.lang.String)
     */
    public void replaceHTML(final String name, final String value) {
        throw new UnsupportedOperationException("replaceHTML() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#computeWithForm(boolean)
     */
    public boolean computeWithForm(final boolean raiseError) {
        throw new UnsupportedOperationException("computeWithForm(boolean) not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#computeWithForm()
     */
    public boolean computeWithForm() {
        throw new UnsupportedOperationException("computeWithForm() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getFTSearchScore()
     */
    public int getFTSearchScore() {
        throw new UnsupportedOperationException("computeWithForm() not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getParentView()
     */
    public DView getParentView() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getFolderReferences()
     */
    public List getFolderReferences() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#putInFolder(java.lang.String)
     */
    public void putInFolder(final String s) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#putInFolder(java.lang.String, boolean)
     */
    public void putInFolder(final String s, final boolean flag) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#removeFromFolder(java.lang.String)
     */
    public void removeFromFolder(final String s) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getURL()
     */
    public String getURL() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getNotesURL()
     */
    public String getNotesURL() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DDocument#getHttpURL()
     */
    public String getHttpURL() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#copyItem(DBaseItem, java.lang.String)
     */
    public DBaseItem copyItem(final DBaseItem item, final String s) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#copyItem(DBaseItem)
     */
    public DBaseItem copyItem(final DBaseItem item) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#createReplyMessage(boolean)
     */
    public DDocument createReplyMessage(final boolean flag) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#encrypt()
     */
    public void encrypt() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getColumnValues()
     */
    public List getColumnValues() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getEncryptionKeys()
     */
    public List getEncryptionKeys() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#setEncryptionKeys(java.util.List)
     */
    public void setEncryptionKeys(final List keys) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueCustomData(java.lang.String)
     */
    public Object getItemValueCustomData(final String name) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueCustomData(java.lang.String,
     *      java.lang.String)
     */
    public Object getItemValueCustomData(final String name, final String dataTypeName) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueCustomDataBytes(java.lang.String,
     *      java.lang.String)
     */
    public byte[] getItemValueCustomDataBytes(final String name, final String dataTypeName) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getItemValueDateTimeArray(java.lang.String)
     */
    public List getItemValueDateTimeArray(final String name) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getSigner()
     */
    public String getSigner() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getSize()
     */
    public int getSize() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#setUniversalID(java.lang.String)
     */
    public void setUniversalID(final String unid) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getVerifier()
     */
    public String getVerifier() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#hasEmbedded()
     */
    public boolean hasEmbedded() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isEncrypted()
     */
    public boolean isEncrypted() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isEncryptOnSend()
     */
    public boolean isEncryptOnSend() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isProfile()
     */
    public boolean isProfile() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isSigned()
     */
    public boolean isSigned() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isValid()
     */
    public boolean isValid() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isSaveMessageOnSend()
     */
    public boolean isSaveMessageOnSend() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isSentByAgent()
     */
    public boolean isSentByAgent() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isSignOnSend()
     */
    public boolean isSignOnSend() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#isDeleted()
     */
    public boolean isDeleted() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#removePermanently(boolean)
     */
    public boolean removePermanently(final boolean flag) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#renderToRTItem(DRichTextItem)
     */
    public boolean renderToRTItem(final DRichTextItem richtextitem) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValueCustomData(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public DItem replaceItemValueCustomData(final String s, final String s1, final Object obj) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValueCustomData(java.lang.String,
     *      java.lang.Object)
     */
    public DItem replaceItemValueCustomData(final String s, final Object obj) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#replaceItemValueCustomDataBytes(java.lang.String,
     *      java.lang.String, byte[])
     */
    public DItem replaceItemValueCustomDataBytes(final String s, final String s1, final byte[] abyte0) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#generateXML()
     */
    public String generateXML() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#generateXML(java.io.Writer)
     */
    public void generateXML(final Writer writer) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getReceivedItemText()
     */
    public List getReceivedItemText() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#getLockHolders()
     */
    public List getLockHolders() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock()
     */
    public boolean lock() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(boolean)
     */
    public boolean lock(final boolean provisionalOk) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(java.lang.String)
     */
    public boolean lock(final String name) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(java.lang.String, boolean)
     */
    public boolean lock(final String name, final boolean provisionalOk) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(java.util.List)
     */
    public boolean lock(final List names) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lock(java.util.List, boolean)
     */
    public boolean lock(final List names, final boolean provisionalOk) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lockProvisional()
     */
    public boolean lockProvisional() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lockProvisional(java.lang.String)
     */
    public boolean lockProvisional(final String name) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#lockProvisional(java.util.List)
     */
    public boolean lockProvisional(final List names) {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseDocument#unlock()
     */
    public void unlock() {
        throw new UnsupportedOperationException("not supported in MockDocument");
    }

}
