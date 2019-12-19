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

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a view or folder of a database and provides access to documents
 * within it.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DView extends DBase {

    /**
     * Updates view contents with any changes that have occurred to the
     * database since the View object was created, or since the last refresh.
     */
    void refresh();

    /**
     * Returns the name of a view.
     *
     * @return view name
     */
    String getName();

    /**
     * Finds a document based on the first column value within a view.
     * Returns <code>null</code> if there are no matching document.
     *
     * @param key - String
     *         A partial key must not be in a categorized colum!
     * @param exact - boolean
     *
     * @return DDocument
     */
    DDocument getDocumentByKey(String key, boolean exact);

    /**
     * Finds a document based on its column values within a view.
     * Returns <code>null</code> if there are no matching document.
     *
     * @param keys - List(Column-Value)
     *         A partial key must not be in a categorized colum!
     * @param exact - boolean
     *
     * @return DDocument
     */
    DDocument getDocumentByKey(List keys, boolean exact);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values partially match
     * one of the keys.  If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param key key
     *
     * @return Iterator
     */
    Iterator getAllDocumentsByKey(String key);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values partially match
     * one of the keys.  If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param key key
     *
     * @return Iterator
     */
    Iterator getAllDocumentsByKey(Calendar key);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values partially match
     * one of the keys.  If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param key key
     *
     * @return Iterator
     */
    Iterator getAllDocumentsByKey(double key);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values partially match
     * one of the keys.  If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param key key
     *
     * @return Iterator
     */
    Iterator getAllDocumentsByKey(int key);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values partially match
     * one of the keys. If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param keys list of keys
     *
     * @return Iterator
     */
    Iterator getAllDocumentsByKey(List keys);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values match
     * each of the keys. If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param key key
     * @param exact True if you want to find an exact match. If you specify
     *          false or omit this parameter, a partial match succeeds.
     *          The use of partial matches with multiple keys may result in
     *          missed documents. If the first key is partial and the second
     *          column does not sort the same with the partial key as with the
     *          exact key, documents that fall out of sequence are missed.
     *         A partial key must not be in a categorized colum!
     * @return Iterator all documents in the view whose column values match
     *          the keys. If no documents match, the collection is empty
     *          and the count is zero.
     */
    Iterator getAllDocumentsByKey(String key, boolean exact);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values match
     * each of the keys.  If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param key key
     * @param exact True if you want to find an exact match. If you specify
     *          false or omit this parameter, a partial match succeeds.
     *          The use of partial matches with multiple keys may result in
     *          missed documents. If the first key is partial and the second
     *          column does not sort the same with the partial key as with the
     *          exact key, documents that fall out of sequence are missed.
     *         A partial key must not be in a categorized colum!
     * @return Iterator all documents in the view whose column values match
     *          the keys. If no documents match, the collection is empty
     *          and the count is zero.
     */
    Iterator getAllDocumentsByKey(Calendar key, boolean exact);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values match
     * each of the keys.  If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param key key
     * @param exact True if you want to find an exact match. If you specify
     *          false or omit this parameter, a partial match succeeds.
     *          The use of partial matches with multiple keys may result in
     *          missed documents. If the first key is partial and the second
     *          column does not sort the same with the partial key as with the
     *          exact key, documents that fall out of sequence are missed.
     *         A partial key must not be in a categorized colum!
     * @return Iterator all documents in the view whose column values match
     *          the keys. If no documents match, the collection is empty
     *          and the count is zero.
     */
    Iterator getAllDocumentsByKey(double key, boolean exact);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values match
     * each of the keys.  If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param key key
     * @param exact True if you want to find an exact match. If you specify
     *          false or omit this parameter, a partial match succeeds.
     *          The use of partial matches with multiple keys may result in
     *          missed documents. If the first key is partial and the second
     *          column does not sort the same with the partial key as with the
     *          exact key, documents that fall out of sequence are missed.
     *         A partial key must not be in a categorized colum!
     * @return Iterator all documents in the view whose column values match
     *          the keys. If no documents match, the collection is empty
     *          and the count is zero.
     */
    Iterator getAllDocumentsByKey(int key, boolean exact);

    /**
     * Finds documents based on their column values within a view.
     * Returns all documents in the view whose column values match
     * each of the keys.  If no documents match, the collection is empty and the
     * count is zero.
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.
     * A partial key must not be in a categorized colum!</p>
     *
     * @param keys  list of keys
     * @param exact True if you want to find an exact match. If you specify
     *          false or omit this parameter, a partial match succeeds.
     *          The use of partial matches with multiple keys may result in
     *          missed documents. If the first key is partial and the second
     *          column does not sort the same with the partial key as with the
     *          exact key, documents that fall out of sequence are missed.
     *         A partial key must not be in a categorized colum!
     * @return Iterator all documents in the view whose column values match
     *          the keys. If no documents match, the collection is empty
     *          and the count is zero.
     */
    Iterator getAllDocumentsByKey(List keys, boolean exact);

    /**
     * Returns an Iterator to loop over all documents in the view.
     *
     * @return Iterator
     */
    Iterator getAllDocuments();

    /**
     * Iterator over all entries in a view in view order.
     *
     * <p>A view entry collection contains only document entries (no categories
     * or totals). If a view is filtered by FTSearch, this property returns the
     * entries in the filtered view.</p>
     *
     * @return Iterator over all entries in a view in view order.
     * @see DViewEntry
     */
    Iterator getAllEntries();

    /**
     * Iterator over all entries in a view in reverse view order.
     *
     * <p>A view entry collection contains only document entries (no categories
     * or totals). If a view is filtered by FTSearch, this property returns the
     * entries in the filtered view.</p>
     *
     * @return Iterator over all entries in a view in view order.
     * @see DViewEntry
     */
    Iterator getAllEntriesReverse();

    /**
     * Finds a view entry based on its column values within a view.
     *
     * <p>You create a key or vector of keys, where each key corresponds to a
     * value in a sorted column in the view. The method returns the first
     * entry with column values that match the keys.</p>
     *
     * <p>This method returns only the first entry with column values that
     * match the strings you indicate. To locate all matching documents, use
     * {@link #getAllEntriesByKey(String)}</p>
     *
     * @param key String that is compared to the first sorted column in the
     *        view.
     * @return The first entry in the view with column values that match the
     *        keys. Returns <code>null</code> if there are no matching entries.
     */
    DViewEntry getEntryByKey(String key);

    /**
     * Finds a view entry based on its column values within a view.
     *
     * <p>You create a key or vector of keys, where each key corresponds to a
     * value in a sorted column in the view. The method returns the first
     * entry with column values that match the keys.</p>
     *
     * <p>This method returns only the first entry with column values that
     * match the strings you indicate. To locate all matching documents, use
     * {@link #getAllEntriesByKey(String)}</p>
     *
     * @param key String that is compared to the first sorted column in the view.
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @return The first entry in the view with column values that match the
     *        keys. Returns <code>null</code> if there are no matching entries.
     */
    DViewEntry getEntryByKey(String key, boolean exact);

    /**
     * Finds a view entry based on its column values within a view.
     *
     * <p>You create a key or vector of keys, where each key corresponds to a
     * value in a sorted column in the view. The method returns the first
     * entry with column values that match the keys.</p>
     *
     * <p>This method returns only the first entry with column values that
     * match the strings you indicate. To locate all matching documents, use
     * {@link #getAllEntriesByKey(String)}</p>
     *
     * @param keys A String object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @return The first entry in the view with column values that match the
     *        keys. Returns <code>null</code> if there are no matching entries.
     */
    DViewEntry getEntryByKey(List keys);

    /**
     * Finds a view entry based on its column values within a view.
     *
     * <p>You create a key or vector of keys, where each key corresponds to a
     * value in a sorted column in the view. The method returns the first
     * entry with column values that match the keys.</p>
     *
     * <p>This method returns only the first entry with column values that
     * match the strings you indicate. To locate all matching documents, use
     * {@link #getAllEntriesByKey(String)}</p>
     *
     * @param keys A String object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @return The first entry in the view with column values that match the keys.
     *         Returns <code>null</code> if there are no matching entries.
     */
    DViewEntry getEntryByKey(List keys, boolean exact);

    /**
     * Iterator over all entries in a view in view order starting with a given
     * entry.
     *
     * <p>A view entry collection contains only document entries (no categories
     * or totals). If a view is filtered by FTSearch, this property returns the
     * entries in the filtered view.</p>
     *
     * @param entry A Document or ViewEntry object. Cannot be null.
     * @return Iterator over all entries in a view in view order starting with
     *         a given entry.
     * @see DViewEntry
     */
    Iterator getAllEntries(DViewEntry entry);

    /**
     * Finds view entries of type document based on their column values within
     * a view. You create a key or vector of keys, where each key corresponds
     * to a value in a sorted column in the view. The method returns all
     * entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllEntriesByKey method to work using a key, you must have at
     * least one column sorted for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A String object that is compared to the first sorted column
     *        in the view.
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     */
    Iterator getAllEntriesByKey(String key);

    /**
     * Finds view entries of type document based on their column values within
     * a view. You create a key or vector of keys, where each key corresponds
     * to a value in a sorted column in the view. The method returns all
     * entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllEntriesByKey method to work using a key, you must have at
     * least one column sorted for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A String object that is compared to the first sorted column
     *        in the view.
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     */
    Iterator getAllEntriesByKey(String key, boolean exact);

    /**
     * Finds view entries of type document based on their column values within
     * a view. You create a key or vector of keys, where each key corresponds
     * to a value in a sorted column in the view. The method returns all
     * entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllEntriesByKey method to work using a key, you must have at
     * least one column sorted for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A String object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     */
    Iterator getAllEntriesByKey(List key);

    /**
     * Finds view entries of type document based on their column values within
     * a view. You create a key or vector of keys, where each key corresponds
     * to a value in a sorted column in the view. The method returns all
     * entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllEntriesByKey method to work using a key, you must have at
     * least one column sorted for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A List object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     */
    Iterator getAllEntriesByKey(List key, boolean exact);

    /**
     * Finds view entries of type document based on their column values within
     * a view. You create a key or vector of keys, where each key corresponds
     * to a value in a sorted column in the view. The method returns all
     * entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllEntriesByKey method to work using a key, you must have at
     * least one column sorted for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param start A Calendar object that represents the start of a date range.
     * Compared to the first column in the view.
     * @param end A Calendar object that represents the start of a end range.
     * Compared to the first column in the view.
     *
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     */
    Iterator getAllEntriesByKey(Calendar start, Calendar end, boolean exact);

    /**
     * Finds view entries of type category.
     * This method returns an iterator over all top level categories.
     * Sub categories are not included. Use {@link #getAllCategories(int)}
     * to retrieve sub categories.
     *
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     */
    Iterator getAllCategories();

    /**
     * Finds view entries of type category upto a given level.
     *
     * @param level The maximum level of navigation 0 (top level) through 30
     *        (default).
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     */
    Iterator getAllCategories(int level);

    /**
     * Finds view entries of type category where the category partially
     * matches the given key.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllCategoriesByKey method to work using a key, you must have at
     * least one column categorized.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A String that is compared to the first categorized column
     *        in the view.
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     * @deprecated not yet fully implemented and not yet tested
     */
    Iterator getAllCategoriesByKey(String key);

    /**
     * Finds view entries of type category upto a given level where the
     * categories partially match the given key.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllCategoriesByKey method to work using a key, you must have at
     * least one column categorized for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A List object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @param level The maximum level of navigation 0 (top level) through 30
     *        (default).
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     * @deprecated not yet fully implemented and not yet tested
     */
    Iterator getAllCategoriesByKey(String key, int level);

    /**
     * Finds view entries of type category upto a given level where the
     * categories match the given key.
     *
     * Finds view entries of type category upto a given level based on their
     * column values within a view. You create a key or vector of keys, where
     * each key corresponds to a value in a sorted column in the view. The
     * method returns all entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllCategoriesByKey method to work using a key, you must have at
     * least one column categorized for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A List object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     * @deprecated not yet fully implemented and not yet tested
     */
    Iterator getAllCategoriesByKey(String key, boolean exact);

    /**
     * Finds view entries of type category upto a given level based on their
     * column values within a view. You create a key or vector of keys, where
     * each key corresponds to a value in a sorted column in the view. The
     * method returns all entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllCategoriesByKey method to work using a key, you must have at
     * least one column categorized for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A List object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @param level The maximum level of navigation 0 (top level) through 30
     *        (default).
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     * @deprecated not yet fully implemented and not yet tested
     */
    Iterator getAllCategoriesByKey(String key, int level, boolean exact);

    /**
     * Finds view entries of type category upto a given level based on their
     * column values within a view. You create a key or vector of keys, where
     * each key corresponds to a value in a sorted column in the view. The
     * method returns all entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllCategoriesByKey method to work using a key, you must have at
     * least one column categorized for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A List object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     * @deprecated not yet fully implemented and not yet tested
     */
    Iterator getAllCategoriesByKey(List key);

    /**
     * Finds view entries of type category upto a given level based on their
     * column values within a view. You create a key or vector of keys, where
     * each key corresponds to a value in a sorted column in the view. The
     * method returns all entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllCategoriesByKey method to work using a key, you must have at
     * least one column categorized for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A List object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @param level The maximum level of navigation 0 (top level) through 30
     *        (default).
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     * @deprecated not yet fully implemented and not yet tested
     */
    Iterator getAllCategoriesByKey(List key, int level);

    /**
     * Finds view entries of type category upto a given level based on their
     * column values within a view. You create a key or vector of keys, where
     * each key corresponds to a value in a sorted column in the view. The
     * method returns all entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllCategoriesByKey method to work using a key, you must have at
     * least one column categorized for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A List object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     * @deprecated not yet fully implemented and not yet tested
     */
    Iterator getAllCategoriesByKey(List key, boolean exact);

    /**
     * Finds view entries of type category upto a given level based on their
     * column values within a view. You create a key or vector of keys, where
     * each key corresponds to a value in a sorted column in the view. The
     * method returns all entries whose column values match the keys.
     *
     * Matches are not case-sensitive.
     *
     * <p><b>Note</b>
     * For the getAllCategoriesByKey method to work using a key, you must have at
     * least one column categorized for every key in the array.</p>
     *
     * <p><b>Note</b>
     * The use of partial matches with multiple keys may result in missed
     * entries. If the first key is partial and the second column does not
     * sort the same with the partial key as with the exact key, entries that
     * fall out of sequence are missed.</p>
     *
     * @param key A List object that is compared to the first sorted column
     *        in the view. The List only must contain the types String, Date,
     *        Double and Integer
     * @param exact use true if you want an exact match or false for a partial
     *        one
     * @param level The maximum level of navigation 0 (top level) through 30
     *        (default).
     * @return a iterator providing all matching <code>ViewEntry</code>s,
     *         Entries returned by this method are in view order.
     * @see DViewEntry
     * @deprecated not yet fully implemented and not yet tested
     */
    Iterator getAllCategoriesByKey(List key, int level, boolean exact);

    /**
     * Conducts a full-text search on all documents in a view and filters the
     * view so it represents only those documents that match the full-text
     * query. This method does not find word variants.
     *
     * <p>See {@link #fullTextSearch(String, int)} for more details.</p>
     *
     * @param query The full-text query. See the "Query Syntax" for details
     * @return The number of documents in the view after the search. Each of these documents matches the query.
     */
    int fullTextSearch(String query);

    /**
     * Conducts a full-text search on all documents in a view and filters the
     * view so it represents only those documents that match the full-text
     * query. This method does not find word variants.
     *
     * <p><b>Usage</b></p>
     * <p>After calling FTSearch, you can use the regular View methods to
     * navigate the result, which is a subset of the documents in the view. If
     * the database is not full-text indexed, the documents in the subset are in
     * the same order as they are in the original view. However, if the database
     * is full-text indexed, the documents in the subset are sorted into
     * descending order of relevance. The method getFirstDocument returns the
     * first document in the subset, getLastDocument returns the last document,
     * and so on.</p>
     *
     * <p>Use the {@link #clear} method to clear the full-text search filtering.
     * The View methods now navigate to the full set of documents in the view.</p>
     *
     * <p>If the database is not full-text indexed, this method works,
     * but less efficiently. To test for an index, use {@link DDatabase#isFTIndexed()}.</p>
     * <p>To create an index on a local database, use {@link DDatabase#updateFTIndex(boolean)}.</p>
     *
     * <p><b>Query syntax</b></p>
     * <p>To search for a word or phrase, enter the word or phrase as is,
     * except that search keywords must be enclosed in quotes. Remember to
     * escape quotes if you are inside a literal. Wildcards, operators, and
     * other syntax are permitted. For the complete syntax rules, see "Finding
     * documents in a database" in Lotus Notes 6 Help.</p>
     *
     * @param query The full-text query. See the "Query Syntax" for details
     * @param maxdocs The maximum number of documents you want returned from the
     *            search. If you want to receive all documents that match the
     *            query, specify 0
     * @return The number of documents in the view after the search. Each of these documents matches the query.
     */
    int fullTextSearch(String query, int maxdocs);

    /**
     * Clears the full-text search filtering on a view. Subsequent calls to
     * getDocument methods get all documents in the view, not just the search
     * results.
     */
    void clear();

    /**
     * Sets the selection formula of a view.
     *
     * @param formula new selection formula
     * @since Lotus Notes R6.5
     */
    void setSelectionFormula(String formula);

    /**
     * Returns the selection formula of a view.
     *
     * @return selection formula
     * @since Lotus Notes R6.5
     */
    String getSelectionFormula();

    /**
     * Retutrns the names of the columns in a view.
     *
     * The order of the column names in the list corresponds to the order of
     * the columns in the view, from left to right.
     *
     * @return list ofd column names
     */
    List getColumnNames();

    /**
     * Returns the number of columns in a view.
     *
     * @return number of columns
     */
    int getColumnCount();

    /**
     * Returns a specified column in a view.
     *
     * @param i A column number where 1 is the first column. Cannot be less than 1
     *            or greater than the number of columns in the view.
     * @return the specified column.
     */
    DViewColumn getColumn(int i);

    /**
     * Returns the columns in a view.
     *
     * <p>The order of ViewColumn objects in the vector corresponds to the
     * order of the columns in the view, from left to right.</p>
     *
     * @return list of all columns
     */
    List getColumns();
}
