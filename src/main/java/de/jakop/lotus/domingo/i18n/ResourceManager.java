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

package de.jakop.lotus.domingo.i18n;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager for internatialized resources.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ResourceManager {

    /** Permission needed to clear complete cache. */
    private static final RuntimePermission CLEAR_CACHE_PERMISSION = new RuntimePermission("i18n.clearCompleteCache");

    /** Static cache for resources. */
    private static final Map RESOURCES = new HashMap();

    /**
     * Private Constructor to block instantiation.
     */
    private ResourceManager() {
    }

    /**
     * Retrieve resource with specified base name.
     *
     * @param baseName the base name
     * @return the Resources
     */
    public static Resources getBaseResources(final String baseName) {
        return getBaseResources(baseName, null);
    }

    /**
     * Retrieve resource with specified base name.
     *
     * @param baseName the base name
     * @param classLoader the classLoader to load resources from
     * @return the Resources
     */
    public static synchronized Resources getBaseResources(final String baseName, final ClassLoader classLoader) {
        Resources theResources = getCachedResource(baseName);
        if (null == theResources) {
            theResources = new Resources(baseName, classLoader);
            putCachedResource(baseName, theResources);
        }
        return theResources;
    }

    /**
     * Clear the cache of all resources currently loaded into the
     * system. This method is useful if you need to dump the complete
     * cache and because part of the application is reloading and
     * thus the resources may need to be reloaded.
     *
     * <p>Note that the caller must have been granted the
     * "i18n.clearCompleteCache" {@link RuntimePermission} or
     * else a security exception will be thrown.</p>
     *
     * @throws SecurityException if the caller does not have
     *                           permission to clear cache
     */
    public static synchronized void clearResourceCache() throws SecurityException {
        final SecurityManager sm = System.getSecurityManager();
        if (null != sm) {
            sm.checkPermission(CLEAR_CACHE_PERMISSION);
        }
        RESOURCES.clear();
    }

    /**
     * Cache specified resource in weak reference.
     *
     * @param baseName the resource key
     * @param theResources the resources object
     */
    private static synchronized void putCachedResource(final String baseName, final Resources theResources) {
        RESOURCES.put(baseName, new WeakReference(theResources));
    }

    /**
     * Retrieve cached resource.
     *
     * @param baseName the resource key
     * @return resources the resources object
     */
    private static synchronized Resources getCachedResource(final String baseName) {
        final WeakReference weakReference = (WeakReference) RESOURCES.get(baseName);
        if (null != weakReference) {
            return (Resources) weakReference.get();
        }
        return null;
    }

    /**
     * Retrieve resource for specified name.
     * The base name is determined by name postfixed with ".Resources".
     *
     * @param name the name to use when looking up resources
     * @return the Resources
     */
    public static Resources getResources(final String name) {
        return getBaseResources(name + ".Resources");
    }

    /**
     * Retrieve resource for specified Classes package.
     * The base name is determined by name of classes package
     * postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public static Resources getPackageResources(final Class clazz) {
        return getBaseResources(getPackageResourcesBaseName(clazz), clazz.getClassLoader());
    }

    /**
     * Retrieve resource for specified Class.
     * The base name is determined by name of Class
     * postfixed with "Resources".
     *
     * @param clazz the Class
     * @return the Resources
     */
    public static Resources getClassResources(final Class clazz) {
        return getBaseResources(getClassResourcesBaseName(clazz), clazz.getClassLoader());
    }

    /**
     * Retrieve resource base name for specified Classes package.
     * The base name is determined by name of classes package
     * postfixed with ".Resources".
     *
     * @param clazz the Class
     * @return the resource base name
     */
    public static String getPackageResourcesBaseName(final Class clazz) {
        final Package pkg = clazz.getPackage();
        String baseName;
        if (null == pkg) {
            final String name = clazz.getName();
            if (-1 == name.lastIndexOf(".")) {
                baseName = "Resources";
            } else {
                baseName = name.substring(0, name.lastIndexOf(".")) + ".Resources";
            }
        } else {
            baseName = pkg.getName() + ".Resources";
        }
        return baseName;
    }

    /**
     * Retrieve resource base name for specified Class.
     * The base name is determined by name of Class
     * postfixed with "Resources".
     *
     * @param clazz the Class
     * @return the resource base name
     */
    public static String getClassResourcesBaseName(final Class clazz) {
        return clazz.getName() + "Resources";
    }
}
