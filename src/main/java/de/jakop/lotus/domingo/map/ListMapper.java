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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.jakop.lotus.domingo.DDocument;

/**
 * Class defining and performing a mapping of Lists of business objects from and
 * to a document.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class ListMapper extends BaseDMapper {

    /** Notes item name in the Notes document. */
    private String mItemName;

    /** Name of getter method of list attribute. */
    private String mGetName;

    /** Set of list item attributes. */
    private List mAttributes = new ArrayList();

    /**
     * Constructor. Creates a direct mapper where the Notes item name is equal
     * to the <tt>itemName</tt> attribute and the names of the get/set methods
     * in the business class are equal to <tt>"get" + itemName</tt> and
     * <tt>"set" + itemName</tt>.
     *
     * @param itemName name of Notes item
     */
    public ListMapper(final String itemName) {
        this(itemName, "get" + itemName);
    }

    /**
     * Constructor. Creates a direct mapper where the Notes item name is equal
     * to the <tt>itemName</tt> attribute and the names of the get/set methods
     * in the business class are equal to <tt>getName</tt> and
     * <tt>setName</tt>.
     *
     * @param itemName name of Notes item
     * @param getName name of get-method
     */
    public ListMapper(final String itemName, final String getName) {
        this.mItemName = itemName;
        this.mGetName = getName;
    }

    /**
     * Adds a mapper where the Notes item name is equal to the <tt>itemName</tt>
     * attribute and the names of the get/set methods in the business class are
     * equal to <tt>"get" + itemName</tt> and <tt>"set" + itemName</tt>.
     *
     * @param itemName name of Notes item
     * @param clazz the value type
     */
    protected final void add(final String itemName, final Class clazz) {
        add(itemName, itemName, clazz);
    }

    /**
     * Adds a mapper where the Notes item name is equal to the <tt>itemName</tt>
     * attribute and the names of the get/set methods in the business class are
     * equal to <tt>"get" + attributeName</tt> and
     * <tt>"set" + attributeName</tt>.
     *
     * @param itemName name of Notes item
     * @param attributeName name of Java attribute
     * @param clazz the value type
     */
    protected final void add(final String itemName, final String attributeName, final Class clazz) {
        add(itemName, "get" + attributeName, "set" + attributeName, clazz);
    }

    /**
     * Adds a mapper where the Notes item name is equal to the <tt>itemName</tt>
     * attribute and the names of the get/set methods in the business class are
     * equal to <tt>getName</tt> and <tt>setName</tt>.
     *
     * @param itemName name of Notes item
     * @param getName name of get-method
     * @param setName name of set-method
     * @param clazz the value type
     */
    protected final void add(final String itemName, final String getName, final String setName, final Class clazz) {
        mAttributes.add(new Attribute(itemName, getName, setName, clazz));
    }

    private static final class Attribute {

        private final String mItemName;

        private final String mGetName;

        private final String mSetName;

        private final Class mClazz;

        private Attribute(final String itemName, final String getName, final String setName, final Class clazz) {
            this.mItemName = itemName;
            this.mGetName = getName;
            this.mSetName = setName;
            this.mClazz = clazz;
        }
    }

    /**
     * {@inheritDoc}
     * @see Mapper#map(DDocument,
     *      java.lang.Object)
     */
    public final void map(final DDocument document, final Object object) throws MappingException {
        // read value lists from document
        final Map values = new HashMap(mAttributes.size());
        int minSize = Integer.MAX_VALUE;
        int maxSize = 0;
        final Iterator i2 = mAttributes.iterator();
        while (i2.hasNext()) {
            final Attribute attribute = (Attribute) i2.next();
            final List itemValue = document.getItemValue(attribute.mItemName);
            minSize = itemValue.size() < minSize ? itemValue.size() : minSize;
            maxSize = itemValue.size() > maxSize ? itemValue.size() : maxSize;
            values.put(attribute, itemValue);
        }
        if (minSize != maxSize) {
            System.out.println("not all items have the same number of values");
        }
        for (int k = 0; k < minSize; k++) {
            Object item = createItem(object);
            final Iterator i3 = mAttributes.iterator();
            while (i3.hasNext()) {
                final Attribute attribute = (Attribute) i3.next();
                setValue(item, attribute.mSetName, ((List) values.get(attribute)).get(k), attribute.mClazz);
            }
        }
    }

    /**
     * Creates a new instance of a list item. Must be implemented by concrete
     * list mappers.
     *
     * @param object parent object
     * @return new instance of list item
     */
    protected abstract Object createItem(final Object object);

    /**
     * {@inheritDoc}
     * @see Mapper#map(java.lang.Object,
     *      DDocument)
     */
    public final void map(final Object object, final DDocument document) throws MappingException {
        List items = getList(object);
        int size = items.size();
        // create value lists for all attributes
        Map values = new HashMap(mAttributes.size());
        Iterator i = mAttributes.iterator();
        while (i.hasNext()) {
            Attribute attribute = (Attribute) i.next();
            values.put(attribute, new ArrayList(size));
        }
        // fill value lists
        Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            Iterator i3 = mAttributes.iterator();
            while (i3.hasNext()) {
                Attribute attribute = (Attribute) i3.next();
                List list = (List) values.get(attribute);
                list.add(getValue(item, attribute.mGetName));
            }
        }
        // write value lists to document
        Iterator i2 = mAttributes.iterator();
        while (i2.hasNext()) {
            Attribute attribute = (Attribute) i2.next();
            replaceItemValue(document, attribute.mItemName, (List) values.get(attribute));
        }
    }

    /**
     * Returns the list attribute defined by the getter method as defined in the
     * constructor.
     *
     * @return the list
     * @throws MappingException if the list attribute cannot be read from the
     *             given object
     */
    private List getList(final Object object) throws MappingException {
        Class[] parameterTypes = {};
        try {
            Method method = object.getClass().getMethod(mGetName, parameterTypes);
            Object[] args = {};
            return (List) method.invoke(object, args);
        } catch (Exception e) {
            throw new MappingException("Cannot get list attribute " + mItemName, e);
        }
    }
}
