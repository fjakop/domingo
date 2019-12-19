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

package de.jakop.lotus.domingo.map;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.jakop.lotus.domingo.DDocument;

/**
 * A set of mappers which itself is a mapper.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class MapperSet extends BaseDMapper {

    /** Internal set of related mappers. */
    private Set mappers = new HashSet();

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param mapper mapper to be added to the mapper set
     * @return <tt>true</tt> if this mapper set did not already contain the
     *         specified mapper.
     *
     * @see java.util.Set#add(java.lang.Object)
     */
    public final boolean add(final Mapper mapper) {
        return mappers.add(mapper);
    }

    /**
     * {@inheritDoc}
     * @see Mapper#map(DDocument,
     *      java.lang.Object)
     */
    public final void map(final DDocument document, final Object object) throws MappingException {
        Iterator iterator = mappers.iterator();
        while (iterator.hasNext()) {
            Mapper mapper = (Mapper) iterator.next();
            mapper.map(document, object);
        }
    }

    /**
     * {@inheritDoc}
     * @see Mapper#map(java.lang.Object,
     *      DDocument)
     */
    public final void map(final Object object, final DDocument document) throws MappingException {
        Iterator iterator = mappers.iterator();
        while (iterator.hasNext()) {
            Mapper mapper = (Mapper) iterator.next();
            mapper.map(object, document);
        }
    }
}
