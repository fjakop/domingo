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
import java.util.Map;

/**
 * Simple cache implementation using a HashMap.
 *
 * <b>Note that this implementation is not synchronized.</b> If multiple
 * threads access this cache concurrently, and at least one of the threads
 * modifies the cache structurally, it <i>must</i> be synchronized externally.
 * (A structural modification is any operation that adds or deletes one or
 * more mappings; merely changing the value associated with a key that an
 * instance already contains is not a structural modification.)  This is
 * typically accomplished by synchronizing on some object that naturally
 * encapsulates the map.  If no such object exists, the map should be
 * "wrapped" using the <tt>Collections.synchronizedMap</tt> method.  This is
 * best done at creation time, to prevent accidental unsynchronized access to
 * the map: <pre> Map map = Collections.synchronizedMap(new HashMap(...));
 * </pre><p>
 *
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class AbstractBaseCache implements Cache, Serializable {

    /**
     * the cache.
     *
     * <p>key -> object</p>
     */
    private final Map map;

    /**
     * Constructor.
     */
    public AbstractBaseCache() {
        super();
        map = createMap();
    }

    /**
     * Protected getter for map attribute to allow concrete classes to access
     * the map.
     *
     * @return the internal map
     */
    protected final Map getMap() {
        return map;
    }

    /**
     * Creates the map to be used with the cache.
     *
     * <p>Concrete classes must implement this method and create
     * a concrete map for the cache.</p>
     *
     * @return a map for the cache
     */
    protected abstract Map createMap();

    /**
     * {@inheritDoc}
     * @see Cache#size()
     */
    public final synchronized int size() {
        return map.size();
    }
}
