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

import java.io.ObjectStreamException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Constants of Notes data types.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class NotesType {

    /** Integer constant for data type TEXT. */
    private static final Integer TEXT_TYPE = new Integer(1);

    /** Integer constant for data type NUMBER. */
    private static final Integer NUMBER_TYPE = new Integer(2);

    /** Integer constant for data type DATE. */
    private static final Integer DATE_TYPE = new Integer(3);

    /** Data type TEXT. */
    public static final NotesType TEXT = new NotesType(TEXT_TYPE, "TEXT".intern(), String.class);

    /** Data type NUMBER. */
    public static final NotesType NUMBER = new NotesType(NUMBER_TYPE, "NUMBER".intern(), Number.class);

    /** Data type DATE. */
    public static final NotesType DATE = new NotesType(DATE_TYPE, "DATE".intern(), Calendar.class);

    /** Integer constant identifying a type. */
    private final Integer type;

    /** Textual representation of a type. */
    private final String name;

    /** Corresponding Java type. */
    private final Class clazz;

    /** Map of all values. */
    private static Map values = new HashMap();
    static {
        values.put(TEXT_TYPE, TEXT);
        values.put(NUMBER_TYPE, NUMBER);
        values.put(DATE_TYPE, DATE);
    }

    /**
     * Constructor.
     *
     * @param type type constant
     */
    private NotesType(final Integer type, final String name, final Class clazz) {
        this.type = type;
        this.name = name;
        this.clazz = clazz;
    }

    /**
     * Returns a collection of all possible values.
     *
     * @return collection of possible values
     */
    public static Collection getValues() {
        return values.values();
    }

    /**
     * Returns the name of a Notes type.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Java class used to represent values of a Notes type.
     *
     * @return Java class
     */
    public Class getJavaClass() {
        return clazz;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        return (obj instanceof NotesType) && (type.intValue() == ((NotesType) obj).type.intValue());
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return type.intValue();
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

    /**
     * Serialization helper used to resolve the enumeration instances.
     */
    private Object readResolve() throws ObjectStreamException {
        return values.get(this.type);
    }
}
