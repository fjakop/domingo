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

package de.jakop.lotus.domingo.cache;

import java.util.Collection;
import java.util.Set;

/**
 * Cache Interface.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface Cache {

    /**
     * Returns an object with a given key from the cache.
     *
     * @param key the key
     *
     * @return object with given key
     */
    Object get(Object key);

    /**
     * Puts an object with a key into the cache.
     *
     * @param key the key
     * @param value the object
     */
    void put(Object key, Object value);

    /**
     * Checks if a given key exists in the cache.
     *
     * @param key the key
     * @return <code>true</code> if the given key exists in the cache, else
     *         <code>false</code>
     */
    boolean containsKey(Object key);

    /**
     * Returns the number of elements in this cache (its cardinality). If this
     * set contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this set (its cardinality).
     */
    int size();

    /**
     * Removes the mapping for this key from this cache if present (optional
     * operation). <p>
     *
     * This implementation iterates over <tt>entrySet()</tt> searching for an
     * entry with the specified key.  If such an entry is found, its value is
     * obtained with its <tt>getValue</tt> operation, the entry is removed
     * from the Collection (and the backing cache) with the iterator's
     * <tt>remove</tt> operation, and the saved value is returned.  If the
     * iteration terminates without finding such an entry, <tt>null</tt> is
     * returned.  Note that this implementation requires linear time in the
     * size of the cache; many implementations will override this method.
     *
     * @param key key whose mapping is to be removed from the cache.
     * @return previous value associated with specified key, or <tt>null</tt>
     *         if there was no entry for key.  (A <tt>null</tt> return can
     *         also indicate that the cache previously associated <tt>null</tt>
     *         with the specified key, if the implementation supports
     *         <tt>null</tt> values.)
     */
    Object remove(Object key);

    /**
     * Removes all mappings from this map (optional operation).
     */
    void clear();

    /**
     * Returns a set view of the keys contained in this cache.  The set is
     * backed by the cache, so changes to the cache are reflected in the set, and
     * vice-versa.  The set supports element removal, which removes the
     * corresponding mapping from this cache, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
     * <tt>clear</tt> operations.  It does not support the <tt>add</tt> or
     * <tt>addAll</tt> operations.
     *
     * @return a set view of the keys contained in this cache.
     */
    Set keySet();

    /**
     * Returns a collection view of the values contained in this cache.  The
     * collection is backed by the cache, so changes to the cache are reflected in
     * the collection, and vice-versa.  The collection supports element
     * removal, which removes the corresponding mapping from this cache, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this cache.
     */
    Collection values();
}
