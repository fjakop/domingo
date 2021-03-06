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

import de.jakop.lotus.domingo.DDocument;

/**
 * Class defining and performing a mapping of an attribute that itself is a
 * complex sub object of a business object.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class SubMapper implements Mapper {

    /** Reference to the associated mapper. */
    private final Mapper mapper;

    /**
     * Constructor. Creates a direct mapper where the Notes item name is equal
     * to the <tt>itemName</tt> attribute and the names of the get/set methods
     * in the business class are equal to <tt>"get" + itemName</tt> and
     * <tt>"set" + itemName</tt>.
     *
     * @param mapper the mapper for the sub-object
     */
    public SubMapper(final Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Implementing class must return the sub-object to map.
     *
     * @param object base object
     * @return sub-object
     */
    protected abstract Object getObject(final Object object);

    /**
     * Performs the mapping from a document to a business object.
     *
     * @param document the Notes document
     * @param object the business object
     * @throws MappingException if an error occurred during mapping
     */
    public final void map(final DDocument document, final Object object) throws MappingException {
        mapper.map(document, getObject(object));
    }

    /**
     * Performs the mapping from a business object to a document.
     *
     * @param object the business object
     * @param document the Notes document
     * @throws MappingException if an error occurred during mapping
     */
    public final void map(final Object object, final DDocument document) throws MappingException {
        mapper.map(getObject(object), document);
    }
}
