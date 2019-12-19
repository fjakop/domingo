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

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Weak Cache.
 *
 * <p>This implementation of the <code>Cache</code> interface uses
 * a <code>WeakHashMap</code> to store the keys. The values are stored
 * as <code>WeakReference</code>s.</p>
 *
 * <p><b>Note that this implementation is synchronized.</b> Multiple
 * threads can access this map concurrently.</p>
 *
 * @see java.util.WeakHashMap
 * @see java.lang.ref.WeakReference
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class WeakCache extends AbstractBaseCache implements Serializable {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = -4673439673337362588L;

    /**
     * Creates a new WeakCache object.
     */
    public WeakCache() {
        super();
    }

    /**
     * {@inheritDoc}
     * @see AbstractBaseCache#createMap()
     */
    protected Map createMap() {
        return new WeakHashMap();
    }

    /**
     * {@inheritDoc}
     * @see Cache#put(java.lang.Object, java.lang.Object)
     * @see java.lang.ref.WeakReference
     * @see Cache#put(java.lang.Object, java.lang.Object)
     */
    public synchronized void put(final Object key, final Object value) {
        getMap().put(key, new WeakReference(value));
    }

    /**
     * {@inheritDoc}
     * @see Cache#get(java.lang.Object)
     */
    public synchronized Object get(final Object key) {
        final WeakReference reference = (WeakReference) getMap().get(key);
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @see Cache#containsKey(java.lang.Object)
     */
    public synchronized boolean containsKey(final Object key) {
        return getMap().containsKey(key);
    }

    /**
     * {@inheritDoc}
     * @see Cache#remove(java.lang.Object)
     */
    public synchronized Object remove(final Object key) {
        return getMap().remove(key);
    }

    /**
     * {@inheritDoc}
     * @see Cache#clear()
     */
    public void clear() {
        getMap().clear();
    }

    /**
     * {@inheritDoc}
     * @see AbstractBaseCache#keySet()
     */
    public Set keySet() {
      return Collections.synchronizedSet(getMap().keySet());
    }

    /**
     * {@inheritDoc}
     * @see AbstractBaseCache#values()
     */
    public Collection values() {
      return Collections.synchronizedCollection(getMap().values());
    }
}
