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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesError;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDateRange;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DItem;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.util.GregorianDateTimeRange;
import de.jakop.lotus.domingo.util.Timezones;

/**
 * This class represents the Domino-Class <code>Item</code>.
 */
public final class ItemProxy extends BaseItemProxy implements DItem {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3761403097427162931L;

    /** Maximum size of an items value. */
    public static final int MAX_VALUE_SIZE = 15000;

    /** Size of a multiple value separator. */
    public static final int VALUE_SEPERATOR_SIZE = 2;

    /** Value size of numeric value stored in a Notes item. */
    public static final int VALUE_SIZE_NUMBER = 4;

    /** Value size of date/time value stored in a Notes item. */
    public static final int VALUE_SIZE_CALENDAR = 8;

    /**
     * Constructor for DItemImpl.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param theItem the Notes item object
     * @param monitor the monitor
     */
    protected ItemProxy(final NotesProxyFactory theFactory, final DBase parent,
                        final Item theItem, final DNotesMonitor monitor) {
        super(theFactory, parent, theItem, monitor);
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValues()
     */
    public List getValues() {
        getFactory().preprocessMethod();
        try {
            List vector = null;
            if (getItem().getType() == Item.DATETIMES) {
                vector = getItem().getValueDateTimeArray();
                final List convertedValues = convertNotesDateTimesToCalendar(vector);
                recycleDateTimeList(vector);
                vector = convertedValues;
            } else {
                vector = getItem().getValues();
            }
            if (vector == null) {
                vector = new Vector();
            }
            if (vector.size() == 0) {
                vector.add("");
            }
            return Collections.unmodifiableList(vector);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get values", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueString()
     */
    public String getValueString() {
        getFactory().preprocessMethod();
        String result = "";
        try {
            result = getItem().getText();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get string value", e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueInteger()
     */
    public Integer getValueInteger() {
        getFactory().preprocessMethod();
        try {
                return new Integer(getItem().getValueInteger());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get integer value", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueDouble()
     */
    public Double getValueDouble() {
        getFactory().preprocessMethod();
        try {
            return new Double(getItem().getValueDouble());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get double value", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueDateTime()
     */
    public Calendar getValueDateTime() {
        getFactory().preprocessMethod();

        try {
            Vector vector = ((Item) getNotesObject()).getValueDateTimeArray();
            Calendar calendar = null;
            if (vector != null && vector.size() > 0) {
                calendar = createCalendar((DateTime) vector.get(0));
                recycleDateTimeList(vector);
            }
            return calendar;
        } catch (NotesException e) {
            if (e.id == NotesError.NOTES_ERR_NOT_A_DATE_ITEM) {
                return null;
            }
            throw newRuntimeException("Cannot get calendar value of item", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueDateRange()
     */
    public DDateRange getValueDateRange() {
        getFactory().preprocessMethod();
        try {
            Vector vector = getItem().getValues();
            if (vector == null || vector.size() == 0) {
                return null;
            }
            Calendar start = null;
            Calendar end = null;
            Object object = vector.get(0);
            if (object instanceof DateRange) {
                start = createCalendar(((DateRange) object).getStartDateTime());
                end = createCalendar(((DateRange) object).getEndDateTime());
            } else if (object instanceof DateTime) {
                start = createCalendar((DateTime) object);
                end = null;
                if (vector.size() > 1) {
                    end = createCalendar((DateTime) vector.get(1));
                }
            }
            DDateRange range = null;
            range = new GregorianDateTimeRange(start, end);
            recycleDateTimeList(vector);
            return range;
        } catch (NotesException e) {
            if (e.id == NotesError.NOTES_ERR_NOT_A_DATE_ITEM) {
                return null;
            }
            throw newRuntimeException("Cannot get calendar value of item", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#appendToTextList(java.lang.String)
     */
    public void appendToTextList(final String value) {
        getFactory().preprocessMethod();
        try {
            final int size = getItem().getValueLength() + value.length();
            if (size > MAX_VALUE_SIZE) {
                throw newRuntimeException("Value size cannot be bigger than 15k: " + size);
            }
        } catch (NotesException e) {
            throw newRuntimeException("Cannot append to text list", e);
        }
        try {
            getItem().appendToTextList(value);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot append to text list", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#appendToTextList(java.util.List)
     */
    public void appendToTextList(final List values) {
        getFactory().preprocessMethod();
        try {
            final int size = getItem().getValueLength() + getValueSize(values);
            if (size > MAX_VALUE_SIZE) {
                throw newRuntimeException("Value size cannot be bigger than 15k: " + size);
            }
        } catch (NotesException e) {
            throw newRuntimeException("Cannot append to text list", e);
        }
        final List convertedKeys = convertCalendarsToNotesDateTime(values);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            getItem().appendToTextList(vector);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot append to text list", e);
        } finally {
            recycleDateTimeList(convertedKeys);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#containsValue(java.lang.String)
     */
    public boolean containsValue(final String value) {
        getFactory().preprocessMethod();
        try {
            return getItem().containsValue(value);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if item contains value " + value, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#containsValue(java.lang.Integer)
     */
    public boolean containsValue(final Integer value) {
        getFactory().preprocessMethod();
        try {
            return getItem().containsValue(value);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if item contains value " + value, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#containsValue(int)
     */
    public boolean containsValue(final int value) {
        return containsValue(new Integer(value));
    }

    /**
     * {@inheritDoc}
     * @see DItem#containsValue(java.lang.Double)
     */
    public boolean containsValue(final Double value) {
        getFactory().preprocessMethod();
        try {
            return getItem().containsValue(value);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if item contains value " + value, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#containsValue(double)
     */
    public boolean containsValue(final double value) {
        return containsValue(new Double(value));
    }

    /**
     * {@inheritDoc}
     * @see DItem#containsValue(java.util.Calendar)
     */
    public boolean containsValue(final Calendar value) {
        getFactory().preprocessMethod();
        try {
            final DateTime dateTime = createDateTime(value);
            final boolean result = getItem().containsValue(value);
            getFactory().recycle(dateTime);
            return result;
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if item contains value " + value, e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueDateTime(java.util.Calendar)
     */
    public void setValueDateTime(final Calendar value) {
        if (value == null) {
            setValueString("");
        } else {
            getFactory().preprocessMethod();
            try {
                final DateTime dateTime = createDateTime(value);
                getItem().setDateTimeValue(dateTime);
                getFactory().recycle(dateTime);
            } catch (NotesException e) {
                throw newRuntimeException("Cannot set calendar value", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueDateRange(DDateRange)
     */
    public void setValueDateRange(final DDateRange value) {
        if (value == null) {
            setValueString("");
        } else {
            setValueDateRange(value.getFrom(), value.getTo());
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueDateRange(java.util.Calendar, java.util.Calendar)
     */
    public void setValueDateRange(final Calendar calendar1, final Calendar calendar2) {
        if (calendar1 == null && calendar2 == null) {
            setValueString("");
        } else if (calendar1 == null) {
            setValueDateTime(calendar2);
        } else if (calendar2 == null) {
            setValueDateTime(calendar1);
        } else {
            getFactory().preprocessMethod();
            try {
                final DateRange dateRange = createDateRange(calendar1, calendar2);
                final Vector vector = new Vector();
                vector.addElement(dateRange);
                getItem().setValues(vector);
                getFactory().recycle(dateRange);
            } catch (NotesException e) {
                throw newRuntimeException("Cannot set calendar value", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueDouble(double)
     */
    public void setValueDouble(final double d) {
        getFactory().preprocessMethod();
        try {
            getItem().setValueDouble(d);
        } catch (NotesException e) {
            throw newRuntimeException("setValueDouble(double): ", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueDouble(java.lang.Double)
     */
    public void setValueDouble(final Double d) {
        if (d == null) {
            setValueString("");
        } else {
            setValueDouble(d.doubleValue());
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueInteger(int)
     */
    public void setValueInteger(final int i) {
        getFactory().preprocessMethod();
        try {
            getItem().setValueInteger(i);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set integer value", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueInteger(java.lang.Integer)
     */
    public void setValueInteger(final Integer i) {
        if (i == null) {
            setValueString("");
        } else {
            setValueInteger(i.intValue());
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValues(java.util.List)
     */
    public void setValues(final List values) {
        if (values == null) {
            setValueString("");
            return;
        }
        getFactory().preprocessMethod();
        final int size = getValueSize(values);
        if (size > MAX_VALUE_SIZE) {
            throw newRuntimeException("Value size cannot be bigger than 15k: " + size);
        }
        final List convertedKeys = convertCalendarsToNotesDateTime(values);
        final Vector vector = convertListToVector(convertedKeys);
        try {
            getItem().setValues(vector);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set value list", e);
        } finally {
            recycleDateTimeList(convertedKeys);
        }
    }

    /**
     * Estimates size of list of values.
     *
     * @param values a list of values
     * @return estimated size of value.
     */
    private int getValueSize(final List values) {
        final Iterator i = values.iterator();
        int size = 0;
        while (i.hasNext()) {
            size = size + getValueSize(i.next()) + VALUE_SEPERATOR_SIZE;
        }
        return size;
    }

    /**
     * Estimates the size of a value.
     *
     * @param value a value
     * @return estimated size a the value.
     * @throws DNotesRuntimeException if the type the value is unknown
     */
    private int getValueSize(final Object value) throws DNotesRuntimeException {
        if (value == null) {
            return 0;
        } else if (value instanceof String) {
            return ((String) value).length();
        } else if (value instanceof Integer) {
            return VALUE_SIZE_NUMBER;
        } else if (value instanceof Double) {
            return VALUE_SIZE_NUMBER;
        } else if (value instanceof Calendar) {
            return VALUE_SIZE_CALENDAR;
        } else {
            throw newRuntimeException("Unknown value type: " + value.getClass().getName());
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueString(java.lang.String)
     */
    public void setValueString(final String value) {
        getFactory().preprocessMethod();
        final int size = getValueSize(value);
        if (size > MAX_VALUE_SIZE) {
            throw newRuntimeException("Value size cannot be bigger than 15k: " + size);
        }
        try {
            getItem().setValueString(value);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set string value", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#isSummary()
     */
    public boolean isSummary() {
        getFactory().preprocessMethod();
        try {
            return getItem().isSummary();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get summary flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setSummary(boolean)
     */
    public void setSummary(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getItem().setSummary(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set summary flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#isNames()
     */
    public boolean isNames() {
        getFactory().preprocessMethod();
        try {
            return getItem().isNames();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get names flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setNames(boolean)
     */
    public void setNames(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getItem().setNames(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set names flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#isReaders()
     */
    public boolean isReaders() {
        getFactory().preprocessMethod();
        try {
            return getItem().isReaders();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get readers flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setReaders(boolean)
     */
    public void setReaders(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getItem().setReaders(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set readers flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#isAuthors()
     */
    public boolean isAuthors() {
        getFactory().preprocessMethod();
        try {
            return getItem().isAuthors();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get authors flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setAuthors(boolean)
     */
    public void setAuthors(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getItem().setAuthors(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set authors flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getSize()
     */
    public int getSize() {
        getFactory().preprocessMethod();
        try {
            return getItem().getValues().size();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get item size", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#isProtected()
     */
    public boolean isProtected() {
        getFactory().preprocessMethod();
        try {
            return getItem().isProtected();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set protection flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setProtected(boolean)
     */
    public void setProtected(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getItem().setProtected(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get protection flag", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#abstractText(int, boolean, boolean)
     */
    public String abstractText(final int maxlen, final boolean dropVowels, final boolean userDict) {
        getFactory().preprocessMethod();
        try {
            return getItem().abstractText(maxlen, dropVowels, userDict);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot abstract text", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#copyItemToDocument(DDocument)
     */
    public DItem copyItemToDocument(final DDocument document) {
        getFactory().preprocessMethod();
        if (!(document instanceof DocumentProxy)) {
            throw newRuntimeException("invalid document");
        }
        try {
            Document notesDocument = (Document) ((DocumentProxy) document).getNotesObject();
            Item item = ((Item) getNotesObject()).copyItemToDocument(notesDocument);
            return (DItem) getInstance(getFactory(), document, item, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot copy to document", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#copyItemToDocument(DDocument, java.lang.String)
     */
    public DItem copyItemToDocument(final DDocument document, final String newName) {
        getFactory().preprocessMethod();
        try {
            Document notesDocument = (Document) ((DocumentProxy) document).getNotesObject();
            Item item = ((Item) getNotesObject()).copyItemToDocument(notesDocument, newName);
            return (DItem) getInstance(getFactory(), document, item, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException("Cannot copy to document", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getLastModified()
     */
    public Calendar getLastModified() {
        getFactory().preprocessMethod();
        try {
            DateTime dateTime = getItem().getLastModified();
            if (dateTime != null) {
                final Calendar calendar = createCalendar(dateTime);
                getFactory().recycle(dateTime);
                return calendar;
            }
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get last modified date", e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @see DItem#getText()
     */
    public String getText() {
        getFactory().preprocessMethod();
        try {
            return getItem().getText();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get text", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getText(int)
     */
    public String getText(final int maxLen) {
        getFactory().preprocessMethod();
        try {
            return getItem().getText(maxLen);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get text", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getType()
     */
    public int getType() {
        getFactory().preprocessMethod();
        try {
            return getItem().getType();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get type", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueLength()
     */
    public int getValueLength() {
        getFactory().preprocessMethod();
        try {
            return getItem().getValueLength();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get value length", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomData(java.lang.String, java.lang.Object)
     */
    public void setValueCustomData(final String type, final Object obj) {
        getFactory().preprocessMethod();
        try {
            getItem().setValueCustomData(type, obj);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set custom data", e);
        } catch (IOException e) {
            throw newRuntimeException("Cannot set custom data", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomData(java.lang.Object)
     */
    public void setValueCustomData(final Object obj) {
        getFactory().preprocessMethod();
        try {
            getItem().setValueCustomData(obj);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set custom data", e);
        } catch (IOException e) {
            throw newRuntimeException("Cannot set custom data", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomDataBytes(java.lang.String, byte[])
     */
    public void setValueCustomDataBytes(final String type, final byte[] bytes) {
        getFactory().preprocessMethod();
        try {
            getItem().setValueCustomData(type, bytes);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set custom data bytes", e);
        } catch (IOException e) {
            throw newRuntimeException("Cannot set custom data bytes", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomData(java.lang.String)
     */
    public Object getValueCustomData(final String type) {
        getFactory().preprocessMethod();
        try {
            return getItem().getValueCustomData(type);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get custom data", e);
        } catch (IOException e) {
            throw newRuntimeException("Cannot get custom data", e);
        } catch (ClassNotFoundException e) {
            throw newRuntimeException("Cannot get custom data", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomData()
     */
    public Object getValueCustomData() {
        getFactory().preprocessMethod();
        try {
            return getItem().getValueCustomData();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get custom data", e);
        } catch (IOException e) {
            throw newRuntimeException("Cannot get custom data", e);
        } catch (ClassNotFoundException e) {
            throw newRuntimeException("Cannot get custom data", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomDataBytes(java.lang.String)
     */
    public byte[] getValueCustomDataBytes(final String type) {
        getFactory().preprocessMethod();
        try {
            return getItem().getValueCustomDataBytes(type);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get custom data bytes", e);
        } catch (IOException e) {
            throw newRuntimeException("Cannot get custom data bytes", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#isEncrypted()
     */
    public boolean isEncrypted() {
        getFactory().preprocessMethod();
        try {
            return getItem().isEncrypted();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if is encrypted", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setEncrypted(boolean)
     */
    public void setEncrypted(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getItem().setEncrypted(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set encrypted", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#isSaveToDisk()
     */
    public boolean isSaveToDisk() {
        getFactory().preprocessMethod();
        try {
            return getItem().isSaveToDisk();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if is saved to disk", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setSaveToDisk(boolean)
     */
    public void setSaveToDisk(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getItem().setSaveToDisk(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set saved to disk", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#isSigned()
     */
    public boolean isSigned() {
        getFactory().preprocessMethod();
        try {
            return getItem().isSigned();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot check if is signed", e);
        }
    }
    /**
     * {@inheritDoc}
     * @see DItem#setSigned(boolean)
     */
    public void setSigned(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getItem().setSigned(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set signed", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getReader()
     */
    public Reader getReader() {
        getFactory().preprocessMethod();
        try {
            return getItem().getReader();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get reader", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#getInputStream()
     */
    public InputStream getInputStream() {
        getFactory().preprocessMethod();
        try {
            return getItem().getInputStream();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get input stream", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueDateTime(java.util.TimeZone)
     */
    public void setValueDateTime(final TimeZone value) {
        String s = "";
        if (value == null) {
            getMonitor().warn("time zone is null; storing an empty string");
        } else {
            s = Timezones.getLotusTimeZoneString(value);
            if (s.startsWith("Unknown")) {
                getMonitor().warn("Unknown time zone identifier (using default): " + value.getID());
                s = TimeZone.getDefault().getID();
                s = Timezones.getLotusTimeZoneString(s);
            }
        }
        setValueString(s);
    }
}
