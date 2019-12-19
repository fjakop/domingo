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

/**
 * Origin:
 * From <http://www.adtmag.com/java/article.aspx?id=4276>
 * Original license- public domain? code published in article
 */

package de.jakop.lotus.domingo.map;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility methods for querying Class objects.
 */
final class ClassUtilities {

    /** Mapping from primitive wrapper Classes to their corresponding primitive Classes. */
    private static final Map OBJECT_PRIMITIVE_MAP = new HashMap(13);

    static {
        OBJECT_PRIMITIVE_MAP.put(Boolean.class, Boolean.TYPE);
        OBJECT_PRIMITIVE_MAP.put(Byte.class, Byte.TYPE);
        OBJECT_PRIMITIVE_MAP.put(Character.class, Character.TYPE);
        OBJECT_PRIMITIVE_MAP.put(Double.class, Double.TYPE);
        OBJECT_PRIMITIVE_MAP.put(Float.class, Float.TYPE);
        OBJECT_PRIMITIVE_MAP.put(Integer.class, Integer.TYPE);
        OBJECT_PRIMITIVE_MAP.put(Long.class, Long.TYPE);
        OBJECT_PRIMITIVE_MAP.put(Short.class, Short.TYPE);
    }

    /** Mapping from primitive names to primitive Classes. */
    private static final Map PRIMITIVE_NAME_MAP = new HashMap(13);

    static {
        PRIMITIVE_NAME_MAP.put("boolean", Boolean.TYPE);
        PRIMITIVE_NAME_MAP.put("byte", Byte.TYPE);
        PRIMITIVE_NAME_MAP.put("char", Character.TYPE);
        PRIMITIVE_NAME_MAP.put("double", Double.TYPE);
        PRIMITIVE_NAME_MAP.put("float", Float.TYPE);
        PRIMITIVE_NAME_MAP.put("int", Integer.TYPE);
        PRIMITIVE_NAME_MAP.put("long", Long.TYPE);
        PRIMITIVE_NAME_MAP.put("short", Short.TYPE);
        PRIMITIVE_NAME_MAP.put("null", Void.TYPE);
        PRIMITIVE_NAME_MAP.put("void", Void.TYPE);
        PRIMITIVE_NAME_MAP.put("", Void.TYPE);
    }

    /**
     * Mapping from primitive wrapper Classes to the sets of primitive classes
     * whose instances can be assigned an instance of the first.
     */
    private static final Map PRIMITIVE_WIDENINGS_MAP = new HashMap(11);

    static {
        Set set = new HashSet();
        set.add(Short.TYPE);
        set.add(Integer.TYPE);
        set.add(Long.TYPE);
        set.add(Float.TYPE);
        set.add(Double.TYPE);
        PRIMITIVE_WIDENINGS_MAP.put(Byte.TYPE, set);

        set = new HashSet();
        set.add(Integer.TYPE);
        set.add(Long.TYPE);
        set.add(Float.TYPE);
        set.add(Double.TYPE);
        PRIMITIVE_WIDENINGS_MAP.put(Short.TYPE, set);
        PRIMITIVE_WIDENINGS_MAP.put(Character.TYPE, set);

        set = new HashSet();
        set.add(Long.TYPE);
        set.add(Float.TYPE);
        set.add(Double.TYPE);
        PRIMITIVE_WIDENINGS_MAP.put(Integer.TYPE, set);

        set = new HashSet();
        set.add(Float.TYPE);
        set.add(Double.TYPE);
        PRIMITIVE_WIDENINGS_MAP.put(Long.TYPE, set);

        set = new HashSet();
        set.add(Double.TYPE);
        PRIMITIVE_WIDENINGS_MAP.put(Float.TYPE, set);
    }

    /**
     * Do not instantiate. Static methods only.
     */
    private ClassUtilities() {
    }

    /**
     * @param name FQN of a class, or the name of a primitive type
     * @param loader a ClassLoader
     * @return the Class for the name given. Primitive types are converted to
     *         their particular Class object. null, the empty string, "null",
     *         and "void" yield Void.TYPE. If any classes require loading
     *         because of this operation, the loading is done by the given class
     *         loader. Such classes are not initialized, however.
     * @exception ClassNotFoundException if name names an unknown class or
     *                primitive
     */
    public static Class classForNameOrPrimitive(final String name, final ClassLoader loader) throws ClassNotFoundException {
        Class clazz = (Class) PRIMITIVE_NAME_MAP.get(name == null ? "" : name);
        return clazz != null ? clazz : Class.forName(name, false, loader);
    }

    /**
     * @param aClass a Class
     * @return true if the class is accessible, false otherwise. Presently
     *         returns true if the class is declared public.
     */
    public static boolean classIsAccessible(final Class aClass) {
        return Modifier.isPublic(aClass.getModifiers());
    }

    /**
     * Tells whether instances of the classes in the 'rhs' array could be used
     * as parameters to a reflective method invocation whose parameter list has
     * types denoted by the 'lhs' array.
     *
     * @param lhs Class array representing the types of the formal parameters of
     *            a method
     * @param rhs Class array representing the types of the actual parameters of
     *            a method. A null value or Void.TYPE is considered to match a
     *            corresponding Object or array class in lhs, but not a
     *            primitive.
     * @return true if compatible, false otherwise
     */
    public static boolean compatibleClasses(final Class[] lhs, final Class[] rhs) {
        if (lhs.length != rhs.length) {
            return false;
        }
        for (int i = 0; i < lhs.length; ++i) {
            if (rhs[i] == null || rhs[i].equals(Void.TYPE)) {
                if (lhs[i].isPrimitive()) {
                    return false;
                } else {
                    continue;
                }
            }
            if (!lhs[i].isAssignableFrom(rhs[i])) {
                Class lhsPrimEquiv = primitiveEquivalentOf(lhs[i]);
                Class rhsPrimEquiv = primitiveEquivalentOf(rhs[i]);
                if (!primitiveIsAssignableFrom(lhsPrimEquiv, rhsPrimEquiv)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param aClass a Class
     * @param methodName name of a method
     * @param parameterTypes Class array representing the types of a method's formal
     *            parameters
     * @return the Method with the given name and formal parameter types that is
     *         in the nearest accessible class in the class hierarchy, starting
     *         with aClass's superclass. The superclass and implemented
     *         interfaces of aClass are searched, then their superclasses, etc.
     *         until a method is found. Returns null if there is no such method.
     */
    public static Method getAccessibleMethodFrom(final Class aClass, final String methodName, final Class[] parameterTypes) {
        // Look for overridden method in the superclass.
        Class superclass = aClass.getSuperclass();
        Method overriddenMethod = null;
        if (superclass != null && classIsAccessible(superclass)) {
            overriddenMethod = getMethod(methodName, parameterTypes, superclass);
            if (overriddenMethod != null) {
                return overriddenMethod;
            }
        }
        // If here, then aClass represents Object, or an interface, or
        // the superclass did not have an override. Check implemented interfaces.
        Class[] interfaces = aClass.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            overriddenMethod = null;
            if (classIsAccessible(interfaces[i])) {
                overriddenMethod = getMethod(methodName, parameterTypes, interfaces[i]);
                if (overriddenMethod != null) {
                    return overriddenMethod;
                }
            }
        }
        overriddenMethod = null;
        // Try superclass's superclass and implemented interfaces.
        if (superclass != null) {
            overriddenMethod = getAccessibleMethodFrom(superclass, methodName, parameterTypes);
            if (overriddenMethod != null) {
                return overriddenMethod;
            }
        }
        // Try implemented interfaces' extended interfaces...
        for (int i = 0; i < interfaces.length; ++i) {
            overriddenMethod = getAccessibleMethodFrom(interfaces[i], methodName, parameterTypes);
            if (overriddenMethod != null) {
                return overriddenMethod;
            }
        }
        // Give up.
        return null;
    }

    private static Method getMethod(final String methodName, final Class[] parameterTypes, final Class superclass) {
        try {
            return superclass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
        }
        return null;
    }

    /**
     * @param aClass a Class
     * @return the class's primitive equivalent, if aClass is a primitive
     *         wrapper. If aClass is primitive, returns aClass. Otherwise,
     *         returns null.
     */
    public static Class primitiveEquivalentOf(final Class aClass) {
        return aClass.isPrimitive() ? aClass : (Class) OBJECT_PRIMITIVE_MAP.get(aClass);
    }

    /**
     * Tells whether an instance of the primitive class represented by 'rhs' can
     * be assigned to an instance of the primitive class represented by 'lhs'.
     *
     * @param lhs assignee class
     * @param rhs assigned class
     * @return true if compatible, false otherwise. If either argument is
     *         <code>null</code>, or one of the parameters does not represent
     *         a primitive (e.g. Byte.TYPE), returns false.
     */
    public static boolean primitiveIsAssignableFrom(final Class lhs, final Class rhs) {
        if (lhs == null || rhs == null) {
            return false;
        }
        if (!(lhs.isPrimitive() && rhs.isPrimitive())) {
            return false;
        }
        if (lhs.equals(rhs)) {
            return true;
        }
        Set wideningSet = (Set) PRIMITIVE_WIDENINGS_MAP.get(rhs);
        if (wideningSet == null) {
            return false;
        }
        return wideningSet.contains(lhs);
    }
}
