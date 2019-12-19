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

package de.jakop.lotus.domingo.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Simple cache implementation using a HashMap.
 *
 * <b>Note that this implementation is not synchronized.</b> If multiple
 * threads access this cache concurrently, and at least one of the threads
 * modifies the cache structurally, it <i>must</i> be synchronized externally.
 * (A structural modification is any operation that adds or deletes one or
 * more mappings; merely changing the value associated with a key that an
 * instance already contains is not a structural modification.)<p>
 *
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class SimpleCache extends AbstractBaseCache implements Serializable {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 4740967132553277227L;

    /**
     * Constructor.
     */
    public SimpleCache() {
        super();
    }

    /**
     * {@inheritDoc}
     * @see AbstractBaseCache#createMap()
     */
    protected Map createMap() {
        return new HashMap();
    }

    /**
     * {@inheritDoc}
     * @see Cache#get(java.lang.Object)
     */
    public Object get(final Object key) {
        return getMap().get(key);
    }

    /**
     * {@inheritDoc}
     * @see Cache#put(java.lang.Object, java.lang.Object)
     */
    public void put(final Object key, final Object value) {
        getMap().put(key, value);
    }

    /**
     * {@inheritDoc}
     * @see Cache#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) {
        return getMap().containsKey(key);
    }

    /**
     * {@inheritDoc}
     * @see Cache#remove(java.lang.Object)
     */
    public Object remove(final Object key) {
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
      return getMap().keySet();
    }

    /**
     * {@inheritDoc}
     * @see AbstractBaseCache#values()
     */
    public Collection values() {
      return getMap().values();
    }
}
