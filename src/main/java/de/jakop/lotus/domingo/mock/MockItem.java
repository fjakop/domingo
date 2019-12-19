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

package de.jakop.lotus.domingo.mock;

import java.io.InputStream;
import java.io.Reader;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import de.jakop.lotus.domingo.DBaseItem;
import de.jakop.lotus.domingo.DDateRange;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DItem;

/**
 * Transient mock implementation of interface DItem.
 *
 * @author <a href=mailto:christian.wied@bea.de>Christian Wied</a>
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class MockItem implements DItem {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257572823290622007L;

    /**
     * {@inheritDoc}
     *
     * @see DItem#getValues()
     */
    public List getValues() {
        throw new UnsupportedOperationException("getValues() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValues(java.util.List)
     */
    public void setValues(final List values) {
        throw new UnsupportedOperationException("setValues() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#getValueString()
     */
    public String getValueString() {
        throw new UnsupportedOperationException("getValueString() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#getValueInteger()
     */
    public Integer getValueInteger() {
        throw new UnsupportedOperationException("getInteger() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#getValueDouble()
     */
    public Double getValueDouble() {
        throw new UnsupportedOperationException("getValueDouble() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#getValueDateTime()
     */
    public Calendar getValueDateTime() {
        throw new UnsupportedOperationException("getValueDateTime() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValueString(java.lang.String)
     */
    public void setValueString(final String value) {
        throw new UnsupportedOperationException("setValueString() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValueInteger(int)
     */
    public void setValueInteger(final int i) {
        throw new UnsupportedOperationException("setValueInteger() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValueInteger(java.lang.Integer)
     */
    public void setValueInteger(final Integer i) {
        throw new UnsupportedOperationException("setValueInteger() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValueDouble(double)
     */
    public void setValueDouble(final double d) {
        throw new UnsupportedOperationException("setValueDouble() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValueDouble(java.lang.Double)
     */
    public void setValueDouble(final Double d) {
        throw new UnsupportedOperationException("setValueDouble() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValueDateTime(java.util.Calendar)
     */
    public void setValueDateTime(final Calendar calendar) {
        throw new UnsupportedOperationException("setValueDateTime() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#appendToTextList(java.lang.String)
     */
    public void appendToTextList(final String value) {
        throw new UnsupportedOperationException("appendToTextList() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#isSummary()
     */
    public boolean isSummary() {
        throw new UnsupportedOperationException("isSummary() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setSummary(boolean)
     */
    public void setSummary(final boolean flag) {
        throw new UnsupportedOperationException("setSummary() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#isNames()
     */
    public boolean isNames() {
        throw new UnsupportedOperationException("isNames() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setNames(boolean)
     */
    public void setNames(final boolean flag) {
        throw new UnsupportedOperationException("isNames() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#isReaders()
     */
    public boolean isReaders() {
        throw new UnsupportedOperationException("isReaders() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setReaders(boolean)
     */
    public void setReaders(final boolean flag) {
        throw new UnsupportedOperationException("setReaders() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#isAuthors()
     */
    public boolean isAuthors() {
        throw new UnsupportedOperationException("isAuthors() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setAuthors(boolean)
     */
    public void setAuthors(final boolean flag) {
        throw new UnsupportedOperationException("setAuthors() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#getSize()
     */
    public int getSize() {
        throw new UnsupportedOperationException("getSize() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseItem#getName()
     */
    public String getName() {
        throw new UnsupportedOperationException("getName() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DBaseItem#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException("remove() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#getValueDateRange()
     */
    public DDateRange getValueDateRange() {
        throw new UnsupportedOperationException("getValueDateRange() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValueDateRange(DDateRange)
     */
    public void setValueDateRange(final DDateRange dateRange) {
        throw new UnsupportedOperationException("setValueDateRange() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setValueDateRange(java.util.Calendar,
     *      java.util.Calendar)
     */
    public void setValueDateRange(final Calendar calendar1, final Calendar calendar2) {
        throw new UnsupportedOperationException("setValueDateRange() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#appendToTextList(java.util.List)
     */
    public void appendToTextList(final List values) {
        throw new UnsupportedOperationException("appendToTextList() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#containsValue(java.lang.String)
     */
    public boolean containsValue(final String value) {
        throw new UnsupportedOperationException("containsValue() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#containsValue(java.lang.Integer)
     */
    public boolean containsValue(final Integer value) {
        throw new UnsupportedOperationException("containsValue() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#containsValue(int)
     */
    public boolean containsValue(final int value) {
        throw new UnsupportedOperationException("containsValue() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#containsValue(java.lang.Double)
     */
    public boolean containsValue(final Double value) {
        throw new UnsupportedOperationException("containsValue() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#containsValue(double)
     */
    public boolean containsValue(final double value) {
        throw new UnsupportedOperationException("containsValue() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#containsValue(java.util.Calendar)
     */
    public boolean containsValue(final Calendar value) {
        throw new UnsupportedOperationException("containsValue() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#isProtected()
     */
    public boolean isProtected() {
        throw new UnsupportedOperationException("isProtected() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     *
     * @see DItem#setProtected(boolean)
     */
    public void setProtected(final boolean flag) {
        throw new UnsupportedOperationException("setProtected() not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#abstractText(int, boolean, boolean)
     */
    public String abstractText(final int maxlen, final boolean dropVowels, final boolean userDict) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#copyItemToDocument(DDocument)
     */
    public DItem copyItemToDocument(final DDocument document) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#copyItemToDocument(DDocument, java.lang.String)
     */
    public DItem copyItemToDocument(final DDocument document, final String newName) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getLastModified()
     */
    public Calendar getLastModified() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getText()
     */
    public String getText() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getText(int)
     */
    public String getText(final int maxLen) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getType()
     */
    public int getType() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueLength()
     */
    public int getValueLength() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomData(java.lang.String, java.lang.Object)
     */
    public void setValueCustomData(final String type, final Object obj) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomData(java.lang.Object)
     */
    public void setValueCustomData(final Object obj) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueCustomDataBytes(java.lang.String, byte[])
     */
    public void setValueCustomDataBytes(final String type, final byte[] bytes) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomData(java.lang.String)
     */
    public Object getValueCustomData(final String type) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomData()
     */
    public Object getValueCustomData() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getValueCustomDataBytes(java.lang.String)
     */
    public byte[] getValueCustomDataBytes(final String type) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#isEncrypted()
     */
    public boolean isEncrypted() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setEncrypted(boolean)
     */
    public void setEncrypted(final boolean flag) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#isSaveToDisk()
     */
    public boolean isSaveToDisk() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setSaveToDisk(boolean)
     */
    public void setSaveToDisk(final boolean flag) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#isSigned()
     */
    public boolean isSigned() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setSigned(boolean)
     */
    public void setSigned(final boolean flag) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
      * {@inheritDoc}
     * @see DItem#getReader()
     */
    public Reader getReader() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#getInputStream()
     */
    public InputStream getInputStream() {
        throw new UnsupportedOperationException("not supported in MockItem");
    }

    /**
     * {@inheritDoc}
     * @see DItem#setValueDateTime(java.util.TimeZone)
     */
    public void setValueDateTime(final TimeZone timezone) {
        throw new UnsupportedOperationException("not supported in MockItem");
    }
}
