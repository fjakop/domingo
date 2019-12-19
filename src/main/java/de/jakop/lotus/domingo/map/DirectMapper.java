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
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import de.jakop.lotus.domingo.DDocument;

/**
 * Class defining and performing a direct mapping of a single attribute between
 * a document and a business object.
 *
 * The following types are supported:
 * <ul>
 * <li>java.lang.String</li>
 * <li>java.lang.Integer and <code>int</code></li>
 * <li>java.lang.Double and <code>double</code></li>
 * <li>java.util.Calendar</li>
 * </ul>
 *
 * <p>Other types must be mapped e.g. with a {@link CustomMapper}.</p>
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class DirectMapper extends BaseDMapper {

    /** Notes item name in the Notes document. */
    private String itemName;

    /** Name of getter method of instance class. */
    private String getName;

    /** Name of setter method of instance class. */
    private String setName;

    /**
     * Value class, can be {@link java.lang.Number}, {@link java.lang.String}
     * or {@link java.lang.Calendar}.
     */
    private Class clazz;

    /**
     * Constructor. Creates a direct mapper where the Notes item name is equal
     * to the <tt>itemName</tt> attribute and the names of the get/set methods
     * in the business class are equal to <tt>"get" + itemName</tt> and
     * <tt>"set" + itemName</tt>.
     *
     * @param itemName name of Notes item
     * @param clazz the value type
     * @throws MethodNotFoundException if the getter or setter method was not found for the attribute name
     */
    public DirectMapper(final String itemName, final Class clazz) throws MethodNotFoundException {
        this(itemName, itemName, clazz);
    }

    /**
     * Constructor. Creates a direct mapper where the Notes item name is equal
     * to the <tt>itemName</tt> attribute and the names of the get/set methods
     * in the business class are equal to <tt>"get" + attributeName</tt> and
     * <tt>"set" + attributeName</tt>. Also the prefix <tt>"is</tt>" is allowed
     * in case of methods returning boolean values.
     *
     * @param itemName name of Notes item
     * @param attributeName name of Java attribute
     * @param clazz the value type
     * @throws MethodNotFoundException if the getter or setter method was not found for the attribute name
     */
    public DirectMapper(final String itemName, final String attributeName, final Class clazz) throws MethodNotFoundException {
        this(itemName, getGetterName(clazz, attributeName), getSetterName(clazz, attributeName), clazz);
    }

    /**
     * Returns the name of the setter method of a class for a given attribute
     * name or throws an exception if no getter method exists.
     *
     * @param clazz the class to check
     * @param attributeName the attribute name
     * @return the name of the getter method
     * @throws MethodNotFoundException if the getter or setter method was not
     *             found for the attribute name
     */
    private static String getSetterName(final Class clazz, final String attributeName)
            throws MethodNotFoundException {
        return "set" + attributeName;
    }

    /**
     * Returns the name of the getter method of a class for a given attribute
     * name or throws an exception if no getter method exists.
     *
     * @param clazz the class to check
     * @param attributeName the attribute name
     * @return the name of the getter method
     * @throws MethodNotFoundException if the getter or setter method was not
     *             found for the attribute name
     */
    private static String getGetterName(final Class clazz, final String attributeName) throws MethodNotFoundException {
        if (clazz.equals(Boolean.TYPE)) {
            return "is" + attributeName;
        } else {
            return "get" + attributeName;
        }
    }

    /**
     * Constructor. Creates a direct mapper where the Notes item name is equal
     * to the <tt>itemName</tt> attribute and the names of the get/set methods
     * in the business class are equal to <tt>getName</tt> and
     * <tt>setName</tt>.
     *
     * @param itemName name of Notes item
     * @param getName name of get-method
     * @param setName name of set-method
     * @param clazz the value type
     */
    public DirectMapper(final String itemName, final String getName, final String setName, final Class clazz) {
        this.itemName = itemName;
        this.getName = getName;
        this.setName = setName;
        this.clazz = clazz;
    }

    /**
     * Performs the direct mapping from a document to a business object.
     *
     * @param document the Notes document
     * @param object the business object
     * @throws MappingException if an error occurred during mapping
     */
    public void map(final DDocument document, final Object object) throws MappingException {
        List itemValueList = document.getItemValue(itemName);
        if (itemValueList != null && itemValueList.size() > 0) {
            Object value = itemValueList;
            if (!Collection.class.isAssignableFrom(clazz)) {
                if (itemValueList.size() > 0) {
                    value = itemValueList.get(0);
                } else {
                    return;
                }
            }
            if (Boolean.class.equals(clazz) || Boolean.TYPE.equals(clazz)) {
                value = getBoolean(value.toString());
            }
            Class[] parameterTypes = { clazz };
            if (setName != null) {
                String className = object.getClass().getName();
                try {
                    Method method = new MethodFinder(object.getClass()).findMethod(setName, parameterTypes);
                    Object[] args = { value };
                    method.invoke(object, args);
                } catch (Exception e) {
                    throw new MappingException("Cannot map item " + itemName + " to " + className + "." + setName, e);
                }
            }
        }
    }

    /**
     * Performs the direct mapping from a business object to a document.
     *
     * Allowed objects are of type <code>String</code>,
     * <code>Calendar</code>, <code>Integer</code>, <code>Double</code>
     * or <code>List</code>, where if the object is a <code>List</code>, all
     * values in the list must be of one of the other types.
     *
     * @param object the business object
     * @param document the Notes document
     * @throws MappingException if an error occurred during mapping
     */
    public void map(final Object object, final DDocument document) throws MappingException {
        if (getName != null) {
            final Object value = getValue(object, getName);
            if (value == null) {
                document.replaceItemValue(itemName, "");
            } else if (String.class.equals(clazz)) {
                document.replaceItemValue(itemName, value.toString());
            } else if (Boolean.class.equals(clazz) || Boolean.TYPE.equals(clazz)) {
                document.replaceItemValue(itemName, getBooleanValue(value));
            } else if (value instanceof Calendar && Calendar.class.isAssignableFrom(clazz)) {
                document.replaceItemValue(itemName, (Calendar) value);
            } else if (value instanceof Double && Number.class.isAssignableFrom(clazz) || Double.TYPE.equals(clazz)) {
                document.replaceItemValue(itemName, (Double) value);
            } else if (value instanceof Integer && Number.class.isAssignableFrom(clazz) || Integer.TYPE.equals(clazz)) {
                document.replaceItemValue(itemName, (Integer) value);
            } else if (value instanceof List) {
                // TODO test behavior with mixed type in list
                document.replaceItemValue(itemName, (List) value);
            } else if (value instanceof Collection) {
                // TODO test behavior with mixed type in collection
                List list = new ArrayList();
                list.addAll((Collection) value);
                document.replaceItemValue(itemName, list);
            } else {
                throw new MappingException("Invalid datatype for method " + getName + ": " + value.getClass().getName()
                        + " is not assignable of class " + clazz.getName());
            }
        }
    }

    /**
     * Converts a value to a string representing a boolean value as stored in
     * Lotus Notes.
     *
     * @param value the value
     * @return the string representing <code>true</code> or <code>false</code>
     */
    private String getBooleanValue(final Object value) {
        if (value instanceof Boolean) {
            return ((Boolean) value).equals(Boolean.TRUE) ? "1" : "0";
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0 ? "1" : "0";
        }
        return value != null ? "1" : "0";
    }

    /**
     * Converts a string to a boolean value; only the string <code>"1"</code>
     * results to <code>true</code>.
     *
     * @param value any string
     * @return <code>true</code> if the string is <code>"1"</code>, else <code>false</code>
     */
    private Boolean getBoolean(final String value) {
        return Boolean.valueOf(value != null && value.equals("1"));
    }
}
