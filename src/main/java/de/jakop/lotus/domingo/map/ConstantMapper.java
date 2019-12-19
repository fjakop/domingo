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

package de.jakop.lotus.domingo.map;

import de.jakop.lotus.domingo.DDocument;

/**
 * Class defining a constant value of an item value.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class ConstantMapper extends BaseDMapper {

    /** Notes item name in the Notes document. */
    private String itemName;

    /** Constant value. */
    private String constant;

    /**
     * Constructor. Creates a constant mapper where the Notes item name is equal
     * to the <tt>itemName</tt> attribute and the value if the item will always
     * be the given constant. The business object will never be changed by this
     * mapper.
     *
     * @param itemName name of Notes item
     * @param constant the constant value for the item
     */
    public ConstantMapper(final String itemName, final String constant) {
        this.itemName = itemName;
        this.constant = constant;
    }

    /**
     * Performs the direct mapping from a document to a business object.
     *
     * @param document the Notes document
     * @param object the business object
     * @throws MappingException if an error occurred during mapping
     */
    public void map(final DDocument document, final Object object) throws MappingException {
    }

    /**
     * Performs the direct mapping from a business object to a document.
     *
     * @param object the business object
     * @param document the Notes document
     * @throws MappingException if an error occurred during mapping
     */
    public void map(final Object object, final DDocument document) throws MappingException {
        document.replaceItemValue(itemName, constant);
    }
}
