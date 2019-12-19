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

package de.jakop.lotus.domingo;

import java.util.List;

/**
 * Represents a view entry.
 * A view entry describes a row in a view.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DViewEntry extends DBase {

    /**
     * The value of each column in the view entry.
     *
     * @return list of Double, java.util.Calendar, or String.
     */
    List getColumnValues();

    /**
     * The document associated with the view entry.
     *
     * <p>Returns null if the view entry is not a document. Returns null if the
     * document is deleted after the ViewEntry object is created.</p>
     *
     * @return the document.
     */
    DDocument getDocument();

    /**
     * Indicates whether a view entry is a category.
     *
     * @return <code>true</code> if the entry is a category,
     *         <code>false</code> if the entry is not a category
     */
    boolean isCategory();

    /**
     * Indicates whether a view entry is a document.
     *
     * @return <code>true</code> if the entry is a document,
     *         <code>false</code> if the entry is not a document
     */
    boolean isDocument();

    /**
     * Indicates whether a view entry is a total.
     *
     * @return <code>true</code> if the entry is a total,
     *         <code>false</code> if the entry is not a total
     */
    boolean isTotal();

    /**
     * @return unid.
     */
    String getUniversalID();

    /**
     * The number of immediate children belonging to the current view entry.
     *
     * @return number of immediate children belonging to the current view entry
     */
    int getChildCount();

    /**
     * Indicates if a view entry is for a document on which a replication or
     * save conflict occurred.
     *
     * @return <code>true</code> if the entry is a conflict document,
     *         <code>false</code> if the entry is not a conflict document
     */
    boolean isConflict();

    /**
     * The number of descendants belonging to the current view entry.
     *
     * @return number of descendants
     * @since domingo 1.1
     */
    int getDescendantCount();

    /**
     * The number of siblings belonging to the current view entry.
     *
     * <p>The sibling count includes the current entry unless it is a total.</p>
     *
     * @return number of siblings
     * @since domingo 1.1
     */
    int getSiblingCount();

    /**
     * The number of siblings belonging to the current view entry.
     *
     * <p>The sibling count includes the current entry unless it is a total.</p>
     *
     * @return number of siblings
     * @since domingo 1.1
     * @deprecated use method {@link #getSiblingCount()} instead
     */
    int getSibblingCount();

    /**
     * The indent level of a view entry within the view.
     *
     * <p>The indent level corresponds to the number of levels in the position.
     * Position 1 is indent level 0, position 1.1 is indent level 1,
     * position 1.1.1 is indent level 2, and so on.</p>
     *
     * @return indent level
     * @since domingo 1.1
     */
    int getIndentLevel();

    /**
     * Indicates whether a view entry is a valid entry and not a deletion stub.
     *
     * <p>If a document is removed after a view entry collection containing it
     * is created, you can use the corresponding view entry for navigation but
     * cannot access the document. If the possibility of removal exists, you
     * should check isValid before attempting to access the document.</p>
     *
     * @return <code>true</code> if a view entry is a valid entry and not a
     *         deletion stub
     * @since domingo 1.1
     */
    boolean isValid();

    /**
     * The note ID of a view entry of type document.
     * <p>This property returns the empty string for entries of type category and total.</p>
     * @return noteID
     * @since domingo 1.1
     */
    String getNoteID();

    /**
     * Returns the position of the entry in the view hierarchy, for example,
     * <tt>"2.3"</tt> for the third document of the second category.
     *
     * @param seperator The separator to be used in the return value.
     * @return A series of integers (in String format) and separators. The
     *         integers indicate the positions of the view entry at each
     *         level, where 1 is the first position. The first integer
     *         indicates the first level and so on.
     * @since domingo 1.1
     */
    String getPosition(char seperator);
}
