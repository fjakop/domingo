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

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.jakop.lotus.domingo.DDocument;

/**
 * Class defining and performing a mapping of a map of attributes to a document.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class MapMapper extends BaseDMapper {

    /**
     * Constructor.
     */
    public MapMapper() {
        super();
    }

    /**
     * {@inheritDoc}
     *
     * @see Mapper#map(DDocument, java.lang.Object)
     */
    public final void map(final DDocument document, final Object object) throws MappingException {
        // TODO map all items into a HashSet
    }

    /**
     * {@inheritDoc}
     *
     * @see Mapper#map(java.lang.Object, DDocument)
     */
    public final void map(final Object object, final DDocument document) throws MappingException {
        Map map = getMap(object);
        if (map == null) {
            return;
        }
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            setValue(document, (String) entry.getKey(), entry.getValue());
        }
    }

    /**
     * derived classes implement this method and return the map that should be mapped.
     *
     * @param object the base object
     * @return map
     */
    protected abstract Map getMap(final Object object);

    /**
     * Must be implemented by derived classes to select allowed names.
     *
     * @param name the name of a value to check
     * @return <code>true</code> if the header name is allowed, else <code>false</code>
     */
    protected abstract boolean isNameAllowed(final String name);

    /**
     * Sets a single value in the document.
     *
     * Allowed objects are of type <code>String</code>,
     * <code>Calendar</code>, <code>Integer</code>, <code>Double</code>
     * or <code>List</code>, where if the object is a <code>List</code>, all
     * values in the list must be of one of the other types.
     *
     * @param name the name of the value
     * @param value the value
     * @param document the Notes document
     * @throws MappingException if an error occurred during mapping
     */
    private void setValue(final DDocument document, final String name, final Object value) throws MappingException {
        if (name == null) {
            return;
        }
        if (value == null) {
            document.replaceItemValue(name, "");
            return;
        }
        Class clazz = value.getClass();
        if (value instanceof String && String.class.isAssignableFrom(clazz)) {
            document.replaceItemValue(name, (String) value);
        } else if (value instanceof Calendar && Calendar.class.isAssignableFrom(clazz)) {
            document.replaceItemValue(name, (Calendar) value);
        } else if (value instanceof Double && Number.class.isAssignableFrom(clazz)) {
            document.replaceItemValue(name, (Double) value);
        } else if (value instanceof Integer && Number.class.isAssignableFrom(clazz)) {
            document.replaceItemValue(name, (Integer) value);
        } else if (value instanceof List) { // TODO test behavior with mixed type in list
            document.replaceItemValue(name, (List) value);
        } else {
            throw new MappingException("Invalid datatype: " + clazz.getName());
        }

    }
}
