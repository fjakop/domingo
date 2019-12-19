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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.jakop.lotus.domingo.DDateRange;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesRuntimeException;

/**
 * Abstract base class for domingo mappers providing useful methods to access
 * Notes. Converts
 * {@link DNotesRuntimeException DNotesRuntimeException}s into
 * {@link MappingException MappingException}s.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class BaseDMapper implements Mapper {

    /** Notes default date/time. */
    private static final Calendar NOTES_DEFAULT_DATE_TIME = Calendar.getInstance();

    /** Year of Notes default date. */
    private static final int NOTES_DEFAULT_YEAR = 1899;

    /** Day of Notes default date. */
    private static final int NOTES_DEFAULT_DAY = 30;

    /** Hour of Notes default date. */
    private static final int NOTES_DEFAULT_HOUR = 12;

    static {
        NOTES_DEFAULT_DATE_TIME.set(NOTES_DEFAULT_YEAR, Calendar.DECEMBER, NOTES_DEFAULT_DAY, NOTES_DEFAULT_HOUR, 0, 0);
        NOTES_DEFAULT_DATE_TIME.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Constructor.
     */
    public BaseDMapper() {
    }

    /**
     * Replaces the value of an item with a String.
     *
     * @param document document to replace item in
     * @param itemName name of item to replace
     * @param value the value to map
     * @throws MappingException if the value cannot be mapped to the item
     */
    protected final void replaceItemValue(final DDocument document, final String itemName, final String value)
            throws MappingException {
        try {
            document.replaceItemValue(itemName, value);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot map String value '" + value + "' to item " + itemName, e);
        }
    }

    /**
     * Replaces the value of an item with a Calendar.
     *
     * @param document document to replace item in
     * @param itemName name of item to replace
     * @param value the value to map
     * @throws MappingException if the value cannot be mapped to the item
     */
    protected final void replaceItemValue(final DDocument document, final String itemName, final Calendar value)
            throws MappingException {
        try {
            document.replaceItemValue(itemName, value);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot map Calendar value '" + value + "' to item " + itemName, e);
        }
    }

    /**
     * Replaces the value of an item with an int.
     *
     * @param document document to replace item in
     * @param itemName name of item to replace
     * @param value the value to map
     * @throws MappingException if the value cannot be mapped to the item
     */
    protected final void replaceItemValue(final DDocument document, final String itemName, final int value)
            throws MappingException {
        try {
            document.replaceItemValue(itemName, value);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot map int value '" + value + "' to item " + itemName, e);
        }
    }

    /**
     * Replaces the value of an item with an Integer.
     *
     * @param document document to replace item in
     * @param itemName name of item to replace
     * @param value the value to map
     * @throws MappingException if the value cannot be mapped to the item
     */
    protected final void replaceItemValue(final DDocument document, final String itemName, final Integer value)
            throws MappingException {
        try {
            document.replaceItemValue(itemName, value);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot map Integer value '" + value + "' to item " + itemName, e);
        }
    }

    /**
     * Replaces the value of an item with a double.
     *
     * @param document document to replace item in
     * @param itemName name of item to replace
     * @param value the value to map
     * @throws MappingException if the value cannot be mapped to the item
     */
    protected final void replaceItemValue(final DDocument document, final String itemName, final double value)
            throws MappingException {
        try {
            document.replaceItemValue(itemName, value);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot map double value '" + value + "' to item " + itemName, e);
        }
    }

    /**
     * Replaces the value of an item with a Double.
     *
     * @param document document to replace item in
     * @param itemName name of item to replace
     * @param value the value to map
     * @throws MappingException if the value cannot be mapped to the item
     */
    protected final void replaceItemValue(final DDocument document, final String itemName, final Double value)
            throws MappingException {
        try {
            document.replaceItemValue(itemName, value);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot map Double value '" + value + "' to item " + itemName, e);
        }
    }

    /**
     * Replaces the value of an item with a List.
     *
     * @param document document to replace item in
     * @param itemName name of item to replace
     * @param value the value to map
     * @throws MappingException if the value cannot be mapped to the item
     */
    protected final void replaceItemValue(final DDocument document, final String itemName, final List value)
            throws MappingException {
        try {
            document.replaceItemValue(itemName, cleanList(value));
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot map value '" + value + "' to item " + itemName, e);
        }
    }

    /**
     * Reads the date value of an item.
     *
     * @param document the domingo document to read from
     * @param itemName the name of the item to read from
     * @return value of item as Calendar
     * @throws MappingException if the value cannot be read
     */
    protected final Calendar getValueDate(final DDocument document, final String itemName) throws MappingException {
        try {
            return document.getItemValueDate(itemName);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot read Calendar value from item " + itemName, e);
        }
    }

    /**
     * Reads the DDateRange value of an item.
     *
     * @param document the domingo document to read from
     * @param itemName the name of the item to read from
     * @return value of item as DDateRange
     * @throws MappingException if the value cannot be read
     */
    protected final DDateRange getValueDateRange(final DDocument document, final String itemName) throws MappingException {
        try {
            return document.getItemValueDateRange(itemName);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot read Calendar value from item " + itemName, e);
        }
    }

    /**
     * Reads the String value of an item.
     *
     * @param document the domingo document to read from
     * @param itemName the name of the item to read from
     * @return value of item as String
     * @throws MappingException if the value cannot be read
     */
    protected final String getValueString(final DDocument document, final String itemName) throws MappingException {
        try {
            return document.getItemValueString(itemName);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot read String value from item " + itemName, e);
        }
    }

    /**
     * Reads the Double value of an item.
     *
     * @param document the domingo document to read from
     * @param itemName the name of the item to read from
     * @return value of item as Double
     * @throws MappingException if the value cannot be read
     */
    protected final Double getValueDouble(final DDocument document, final String itemName) throws MappingException {
        try {
            return document.getItemValueDouble(itemName);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot read Double value from item " + itemName, e);
        }
    }

    /**
     * Reads the Integer value of an item.
     *
     * @param document the domingo document to read from
     * @param itemName the name of the item to read from
     * @return value of item as Integer
     * @throws MappingException if the value cannot be read
     */
    protected final Integer getValueInteger(final DDocument document, final String itemName) throws MappingException {
        try {
            return document.getItemValueInteger(itemName);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot read Integer value from item " + itemName, e);
        }
    }

    /**
     * Reads the date value of an item.
     *
     * @param document the domingo document to read from
     * @param itemName the name of the item to read from
     * @return value of item as Calendar
     * @throws MappingException if the value cannot be read
     */
    protected final List getValue(final DDocument document, final String itemName) throws MappingException {
        try {
            return document.getItemValue(itemName);
        } catch (DNotesRuntimeException e) {
            throw new MappingException("Cannot read value list from item " + itemName, e);
        }
    }

    /**
     * Checks and repairs the values in a list. If there are <tt>null</tt>
     * values in the list they are replaced with a default value with the same
     * datatype as the first value that is not <tt>null</tt>. If all values
     * are <tt>null</tt>, all values are replaced with empty strings. The
     * returned list is a new list; the original list is left unchanged.
     *
     * @param value list of values
     */
    private List cleanList(final List list) {
        Object firstValueNotNull = getFirstValueNotNull(list);
        Object defaultValue = getDefaultValue(firstValueNotNull);
        return replaceNull(list, defaultValue);
    }

    /**
     * Returns the default value matching the type of a given value.
     *
     * @param value a value
     * @return default value with same type or best matching type
     */
    private Object getDefaultValue(final Object value) {
        if (value instanceof Number) {
            return new Integer(0);
        } else if (value instanceof Calendar) {
            return NOTES_DEFAULT_DATE_TIME;
        }
        return "";
    }

    /**
     * Returns the first value in a list that is not <tt>null</tt>.
     *
     * @param list list of values
     * @return first value that is not <tt>null</tt>
     */
    private Object getFirstValueNotNull(final List list) {
        Object value = null;
        Iterator iterator = list.iterator();
        while (iterator.hasNext() && value == null) {
            value = iterator.next();
        }
        return value;
    }

    /**
     * Returns a calendar if the given object is a calendar or
     * it is a list and the first element in the list is a calendar.
     *
     * @param calendarOrList a {@link Calendar} or a {@link List}
     * @return {@link Calendar} or <code>null</code>
     */
    protected final Calendar getCalendar(final Object calendarOrList) {
        if (calendarOrList instanceof Calendar) {
            return (Calendar) calendarOrList;
        }
        if (calendarOrList instanceof List && ((List) calendarOrList).size() > 0) {
            return (Calendar) ((List) calendarOrList).get(0);
        }
        return null;
    }

    /**
     * Replaces all <tt>null</tt> values in a list with a given default value.
     *
     * @param list the list to replace in
     * @param defaultValue the default value for <tt>null</tt> values
     */
    private List replaceNull(final List list, final Object defaultValue) {
        List result = new ArrayList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Object value = iterator.next();
            result.add(value == null ? defaultValue : value);
        }
        return result;
    }

    /**
     * Returns the value of an attribute given by its getter method from given
     * object.
     *
     * @param object the object
     * @param getName the name of the getter method
     * @return the value
     * @throws MappingException if the given attribute cannot be accessed
     */
    protected final Object getValue(final Object object, final String getName) throws MappingException {
        Class[] parameterTypes = {};
        try {
            Method method = object.getClass().getMethod(getName, parameterTypes);
            return method.invoke(object, BaseMapper.EMPTY_ARGS);
        } catch (Exception e) {
            throw new MappingException("Cannot access method " + getName, e);
        }
    }

    /**
     * Sets a value of an attribute given by its setter method in a given
     * object.
     *
     * @param object the object
     * @param setName the name of the setter method
     * @param value the value to set
     * @param clazz class name of attribute
     * @throws MappingException if the given attribute cannot be accessed
     */
    protected final void setValue(final Object object, final String setName, final Object value, final Class clazz)
            throws MappingException {
        final Class[] parameterTypes = { clazz };
        final Object arg0;
        if (value == null) {
            return;
        } else if (value instanceof Number && (clazz == Integer.class || clazz == Integer.TYPE)) {
            arg0 = new Integer(((Double) value).intValue());
        } else {
            arg0 = value;
        }

        final Object[] args = { arg0 };
        final Method method;
        try {
            method = object.getClass().getMethod(setName, parameterTypes);
        } catch (Exception e) {
            throw new MappingException("not found: " + object.getClass().getName() + "." + setName + "(" + clazz + ")", e);
        }
        try {
            method.invoke(object, args);
        } catch (Exception e) {
            throw new MappingException("error calling " + object.getClass().getName() + "." + setName + "(" + clazz + ")", e);
        }
    }

    /**
     * Checks if a class contains a method with the specified name and no
     * arguments.
     *
     * @param clazz the class to check
     * @param name a method name
     * @return <code>true</code> if the method exists, else <code>false</code>
     */
    protected static final boolean hasMethod(final Class clazz, final String name) {
        try {
            return clazz.getMethod(name, BaseMapper.EMPTY_PARAMS) != null;
        } catch (SecurityException e) {
            // TODO handle exception
            return false;
        } catch (NoSuchMethodException e) {
            // TODO handle exception
            return false;
        }
    }

}
