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

import java.util.ArrayList;
import java.util.List;

import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DViewEntry;

/**
 * Transient mock implementation of interface DViewEntry.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class MockViewEntry implements DViewEntry {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257850982569357616L;

    /** list of column values. */
    private List mColumnValues = new ArrayList();

    /** universal ID. */
    private String mUniversalID = "";

    /** number of children. */
    private int mChildCount = 0;

    /** if it is a category entry. */
    private boolean mIsCategory = false;

    /** if it is a document entry. */
    private boolean mIsDocument = false;

    /** if it is a total entry. */
    private boolean mIsTotal = false;

    /** if it is a conflict entry. */
    private boolean mIsConflict = false;

    /** Number of descendants. */
    private int descendants;

    /** Number of sibblings. */
    private int sibblings;

    /** the NotesID. */
    private String noteId;

    private boolean valid;

    private int indentLevel;

    private String position;

    /**
     * Constructor.
     *
     * @param columnValues list of column values
     * @param universalID universal ID
     * @param childCount number of children
     * @param isCategory if it is a category entry
     * @param isDocument if it is a document entry
     * @param isTotal if it is a total entry
     * @param isConflict if it is a conflict entry
     */
    public MockViewEntry(final List columnValues, final String universalID, final int childCount, final boolean isCategory,
            final boolean isDocument, final boolean isTotal, final boolean isConflict) {
        mUniversalID = universalID;
        mChildCount = childCount;
        mColumnValues = columnValues;
        mIsCategory = isCategory;
        mIsDocument = isDocument;
        mIsTotal = isTotal;
        mIsConflict = isConflict;
    }

    /**
     * Constructor.
     *
     * @param entry any object implementing the interface DViewEntry
     */
    public MockViewEntry(final DViewEntry entry) {
        mColumnValues = entry.getColumnValues();
        mIsCategory = entry.isCategory();
        mIsDocument = entry.isDocument();
        mIsTotal = entry.isTotal();
        mIsConflict = entry.isConflict();
        mUniversalID = entry.getUniversalID();
        mChildCount = entry.getChildCount();
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getColumnValues()
     */
    public List getColumnValues() {
        return mColumnValues;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getDocument()
     */
    public DDocument getDocument() {
        throw new UnsupportedOperationException("getDocument() not supported in class MockViewEntry");
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isCategory()
     */
    public boolean isCategory() {
        return mIsCategory;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isDocument()
     */
    public boolean isDocument() {
        return mIsDocument;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isTotal()
     */
    public boolean isTotal() {
        return mIsTotal;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getUniversalID()
     */
    public String getUniversalID() {
        return mUniversalID;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getChildCount()
     */
    public int getChildCount() {
        return mChildCount;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isConflict()
     */
    public boolean isConflict() {
        return mIsConflict;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return mColumnValues.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getDescendantCount()
     */
    public int getDescendantCount() {
        return descendants;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getSiblingCount()
     */
    public int getSiblingCount() {
        return sibblings;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getSibblingCount()
     * @deprecated use method {@link #getSiblingCount()} instead
     */
    public int getSibblingCount() {
        return sibblings;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getIndentLevel()
     */
    public int getIndentLevel() {
        return indentLevel;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#isValid()
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getNoteID()
     */
    public String getNoteID() {
        return noteId;
    }

    /**
     * {@inheritDoc}
     *
     * @see DViewEntry#getPosition(char)
     */
    public String getPosition(final char seperator) {
        return position;
    }
}
