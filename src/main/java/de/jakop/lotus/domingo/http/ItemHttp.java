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

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import de.jakop.lotus.domingo.*;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.util.GregorianDateRange;

/**
 * Simple implementation of an item in a Notes document.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ItemHttp extends BaseHttp implements DItem {

    /** Size on disk in bytes of a number value. */
    private static final int NUMBER_VALUE_SIZE = 8; // TODO check for correct value size

    /** Size on disk in bytes of a calendar value. */
    private static final int CALENDAR_VALUE_SIZE = 8; // TODO check for correct value size

    /** Size on disk in bytes of a null value. */
    private static final int NULL_VALUE_SIZE = 4; // TODO check for correct value size

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 963751252547033826L;

    private String fName;

    private List fValues;

    private boolean fAuthors;

    private boolean fReaders;

    private boolean fNames;

    private boolean fIsProtected;

    private boolean fIsSummary;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param theParent the document that contains the item
     * @param theName the item name
     * @param monitor the monitor that handles logging
     */
    public ItemHttp(final NotesHttpFactory theFactory, final DBase theParent, final String theName,
            final DNotesMonitor monitor) {
        this(theFactory, theParent, theName, null, monitor);
    }

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param theParent the document that contains the item
     * @param theName the item name
     * @param theValues the values
     * @param monitor the monitor that handles logging
     */
    public ItemHttp(final NotesHttpFactory theFactory, final DBase theParent, final String theName,
            final List theValues, final DNotesMonitor monitor) {
        super(theFactory, theParent, monitor);
        fName = theName;
        fValues = theValues;
    }

    /** {@inheritDoc}
     * @see DItem#getValues()
     */
    public List getValues() {
        return fValues;
    }

    /** {@inheritDoc}
     * @see DItem#setValues(java.util.List)
     */
    public void setValues(final List theValues) {
        fValues = theValues;
    }

    /** {@inheritDoc}
     * @see DItem#getValueString()
     */
    public String getValueString() {
        return fValues.get(0).toString();
    }

    /** {@inheritDoc}
     * @see DItem#getValueInteger()
     */
    public Integer getValueInteger() {
        Object value = getValue(0);
        if (value == null) {
            return null;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return new Integer(((Number) value).intValue());
        }
        return null;
    }

    /** {@inheritDoc}
     * @see DItem#getValueDouble()
     */
    public Double getValueDouble() {
        Object value = getValue(0);
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return new Double(((Number) value).doubleValue());
        }
        return null;
    }

    /** {@inheritDoc}
     * @see DItem#getValueDateTime()
     */
    public Calendar getValueDateTime() {
        Object value = getValue(0);
        if (value == null) {
            return null;
        } else if (value instanceof Calendar) {
            return (Calendar) value;
        }
        return null;
    }

    /** {@inheritDoc}
     * @see DItem#getValueDateRange()
     */
    public DDateRange getValueDateRange() {
        Object from = getValue(0);
        if (from == null || !(from instanceof Calendar)) {
            return null;
        }
        Object to = getValue(1);
        if (to == null || !(to instanceof Calendar)) {
            return null;
        }
        return new GregorianDateRange((Calendar) from, (Calendar) to);
    }

    /** {@inheritDoc}
     * @see DItem#setValueString(java.lang.String)
     */
    public void setValueString(final String value) {
        fValues = new ArrayList(1);
        fValues.set(0, value);
    }

    /** {@inheritDoc}
     * @see DItem#setValueInteger(int)
     */
    public void setValueInteger(final int i) {
        fValues = new ArrayList(1);
        fValues.set(0, new Integer(i));
    }

    /** {@inheritDoc}
     * @see DItem#setValueInteger(java.lang.Integer)
     */
    public void setValueInteger(final Integer i) {
        fValues = new ArrayList(1);
        fValues.set(0, i);
    }

    /** {@inheritDoc}
     * @see DItem#setValueDouble(double)
     */
    public void setValueDouble(final double d) {
        fValues = new ArrayList(1);
        fValues.set(0, new Double(d));
    }

    /** {@inheritDoc}
     * @see DItem#setValueDouble(java.lang.Double)
     */
    public void setValueDouble(final Double d) {
        fValues = new ArrayList(1);
        fValues.set(0, d);
    }

    /** {@inheritDoc}
     * @see DItem#setValueDateTime(java.util.Calendar)
     */
    public void setValueDateTime(final Calendar calendar) {
        fValues = new ArrayList(1);
        fValues.set(0, calendar);
    }

    /** {@inheritDoc}
     * @see DItem#setValueDateRange(DDateRange)
     */
    public void setValueDateRange(final DDateRange dateRange) {
        fValues = new ArrayList(2);
        fValues.set(0, dateRange.getFrom());
        fValues.set(0, dateRange.getTo());
    }

    /** {@inheritDoc}
     * @see DItem#setValueDateRange(java.util.Calendar, java.util.Calendar)
     */
    public void setValueDateRange(final Calendar calendar1, final Calendar calendar2) {
        fValues = new ArrayList(2);
        fValues.set(0, calendar1);
        fValues.set(0, calendar2);
    }

    /** {@inheritDoc}
     * @see DItem#appendToTextList(java.lang.String)
     */
    public void appendToTextList(final String value) {
        fValues.add(value);
    }

    /** {@inheritDoc}
     * @see DItem#appendToTextList(java.util.List)
     */
    public void appendToTextList(final List theValues) {
        fValues.addAll(theValues);
    }

    /** {@inheritDoc}
     * @see DItem#containsValue(java.lang.String)
     */
    public boolean containsValue(final String value) {
        Iterator i = fValues.iterator();
        while (i.hasNext()) {
            if (i.next().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc}
     * @see DItem#containsValue(java.lang.Integer)
     */
    public boolean containsValue(final Integer value) {
        return containsValueIntern(value);
    }

    /** {@inheritDoc}
     * @see DItem#containsValue(int)
     */
    public boolean containsValue(final int value) {
        return containsValueIntern(new Integer(value));
    }

    /** {@inheritDoc}
     * @see DItem#containsValue(java.lang.Double)
     */
    public boolean containsValue(final Double value) {
        return containsValueIntern(value);
    }

    /** {@inheritDoc}
     * @see DItem#containsValue(double)
     */
    public boolean containsValue(final double value) {
        return containsValueIntern(new Double(value));
    }

    /** {@inheritDoc}
     * @see DItem#containsValue(java.util.Calendar)
     */
    public boolean containsValue(final Calendar value) {
        return containsValueIntern(value);
    }

    /** {@inheritDoc}
     * @see DItem#isSummary()
     */
    public boolean isSummary() {
        return fIsSummary;
    }

    /** {@inheritDoc}
     * @see DItem#setSummary(boolean)
     */
    public void setSummary(final boolean flag) {
        fIsSummary = flag;
    }

    /** {@inheritDoc}
     * @see DItem#isReaders()
     */
    public boolean isReaders() {
        return fReaders;
    }

    /** {@inheritDoc}
     * @see DItem#isNames()
     */
    public boolean isNames() {
        return fNames;
    }

    /** {@inheritDoc}
     * @see DItem#setNames(boolean)
     */
    public void setNames(final boolean flag) {
        fNames = flag;
    }

    /** {@inheritDoc}
     * @see DItem#setReaders(boolean)
     */
    public void setReaders(final boolean flag) {
        fReaders = flag;
    }

    /** {@inheritDoc}
     * @see DItem#isAuthors()
     */
    public boolean isAuthors() {
        return fAuthors;
    }

    /** {@inheritDoc}
     * @see DItem#isProtected()
     */
    public boolean isProtected() {
        return fIsProtected;
    }

    /** {@inheritDoc}
     * @see DItem#setAuthors(boolean)
     */
    public void setAuthors(final boolean flag) {
        fAuthors = flag;
    }

    /** {@inheritDoc}
     * @see DItem#setProtected(boolean)
     */
    public void setProtected(final boolean flag) {
        fIsProtected = flag;
    }

    /** {@inheritDoc}
     * @see DItem#getSize()
     */
    public int getSize() {
        int size = 0;
        Iterator i = fValues.iterator();
        while (i.hasNext()) {
            size += getSize(i.next());
        }
        return size;
    }

    /** {@inheritDoc}
     * @see DBaseItem#getName()
     */
    public String getName() {
        return fName;
    }

    /** {@inheritDoc}
     * @see DBaseItem#remove()
     */
    public void remove() {
        ((DocumentHttp) getParent()).removeItem(this.fName);
    }

    /** {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return fName + "=" + fValues;
    }

    /** {@inheritDoc}
     * Returns the n-th value of exists, else <code>null</code>.
     *
     * @param i value index
     * @return the n-th value of exists, else <code>null</code>
     */
    private Object getValue(final int i) {
        try {
            return fValues.get(i);
        } catch (IndexOutOfBoundsException e) {
            getMonitor().warn("Cannot access value " + i + " of item " + fName);
        }
        return null;
    }

    /** {@inheritDoc}
     * Checks if the item contains a specific value, independent of its time.
     *
     * @param value any possible value
     * @return <code>true</code> if the item contains the value, else <code>false</code>
     */
    private boolean containsValueIntern(final Object value) {
        Iterator i = fValues.iterator();
        while (i.hasNext()) {
            if (i.next().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc}
     * Returns the size on disk of a single value.
     *
     * @param object any possible value
     * @return size size on disk of the value
     */
    private int getSize(final Object object) {
        if (object == null) {
            return NULL_VALUE_SIZE;
        } else if (object instanceof Calendar) {
            return CALENDAR_VALUE_SIZE;
        } else if (object instanceof String) {
            return ((String) object).length();
        } else if (object instanceof Number) {
            return NUMBER_VALUE_SIZE;
        }
        return NULL_VALUE_SIZE;
    }

    /**
     * {@inheritDoc}
     * @see DItem#abstractText(int, boolean, boolean)
     */
    public String abstractText(final int maxlen, final boolean dropVowels, final boolean userDict) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#copyItemToDocument(DDocument)
     */
    public DItem copyItemToDocument(final DDocument document) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#copyItemToDocument(DDocument, java.lang.String)
     */
    public DItem copyItemToDocument(final DDocument document, final String newName) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getLastModified()
     */
    public Calendar getLastModified() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getText()
     */
    public String getText() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getText(int)
     */
    public String getText(final int maxLen) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getType()
     */
    public int getType() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueLength()
     */
    public int getValueLength() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomData(java.lang.String, java.lang.Object)
     */
    public void setValueCustomData(final String type, final Object obj) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomData(java.lang.Object)
     */
    public void setValueCustomData(final Object obj) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomDataBytes(java.lang.String, byte[])
     */
    public void setValueCustomDataBytes(final String type, final byte[] bytes) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomData(java.lang.String)
     */
    public Object getValueCustomData(final String type) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomData()
     */
    public Object getValueCustomData() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomDataBytes(java.lang.String)
     */
    public byte[] getValueCustomDataBytes(final String type) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#isEncrypted()
     */
    public boolean isEncrypted() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setEncrypted(boolean)
     */
    public void setEncrypted(final boolean flag) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#isSaveToDisk()
     */
    public boolean isSaveToDisk() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setSaveToDisk(boolean)
     */
    public void setSaveToDisk(final boolean flag) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#isSigned()
     */
    public boolean isSigned() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setSigned(boolean)
     */
    public void setSigned(final boolean flag) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
      * {@inheritDoc}
     * @see DItem#getReader()
     */
    public Reader getReader() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getInputStream()
     */
    public InputStream getInputStream() {
        throw new UnsupportedOperationException("not supported in Http Item");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueDateTime(java.util.TimeZone)
     */
    public void setValueDateTime(final TimeZone timezone) {
        throw new UnsupportedOperationException("not supported in Http Item");
    }
}
