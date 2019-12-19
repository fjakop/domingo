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

package de.jakop.lotus.domingo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Implementation finder for the abstract factory DNotesFactory.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
final class DNotesFactoryFinder {

    /** name of factory property, used with system properties and resource. */
    private static final String FACTORY_PROPERTY = "de.bea.domingo.factory";

    /** Class name of default implementation of this abstract factory. */
    private static final String DEFAULT_IMPL = "de.bea.domingo.service.NotesServiceFactory";

    /** name of resource file defining implementation. */
    private static final String PROPERTIES_FILE = "de/bea/domingo/domingo.properties";

    /** alternative name of resource file defining implementation. */
    private static final String PROPERTIES_FILE2 = "domingo.properties";

    /** Configuration properties. */
    private static Configuration fConfiguration = new Configuration();

    /**
     * Private constructor to prevent creation.
     */
    private DNotesFactoryFinder() {
    }

    /**
     * Find the class name of the implementation of an abstract factory.
     *
     * @return instance the new instance
     * @throws  IllegalAccessException  if the class or initializer is
     *               not accessible.
     * @throws  InstantiationException
     *               if this <code>Class</code> represents an abstract class,
     *               an interface, an array class,
     *               a primitive type, or void;
     *               or if the instantiation fails for some other reason.
     */
    public static Object find() throws InstantiationException, IllegalAccessException {
        ClassLoader classLoader = findClassLoader();
        String implementingClassName = getProperty(FACTORY_PROPERTY, DEFAULT_IMPL);
        return newInstance(implementingClassName, classLoader);
    }

    /**
     * Find the class name of the implementation of an abstract factory.
     *
     * @return instance the new instance
     * @throws  IllegalAccessException  if the class or initializer is
     *               not accessible.
     * @throws  InstantiationException
     *               if this <code>Class</code> represents an abstract class,
     *               an interface, an array class,
     *               a primitive type, or void;
     *               or if the instantiation fails for some other reason.
     */
    public static Object find(final String implementingClassName) throws InstantiationException, IllegalAccessException {
        ClassLoader classLoader = findClassLoader();
        return newInstance(implementingClassName, classLoader);
    }

    /**
     * Create an instance of a class using the specified ClassLoader.
     *
     * @param className the fully qualified name of the desired class.
     * @param classLoader the classloader to use
     * @return instance the new instance
     * @throws  IllegalAccessException  if the class or initializer is
     *               not accessible.
     * @throws  InstantiationException
     *               if this <code>Class</code> represents an abstract class,
     *               an interface, an array class,
     *               a primitive type, or void;
     *               or if the instantiation fails for some other reason.
     */
    private static Object newInstance(final String className, final ClassLoader classLoader)
            throws InstantiationException, IllegalAccessException {
        try {
            Class clazz;
            if (classLoader == null) {
                clazz = Class.forName(className);
            } else {
                clazz = classLoader.loadClass(className);
            }
            return clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new ConfigurationError("Factory not found: " + e.getLocalizedMessage());
        }
    }

    /**
     * Figure out which ClassLoader to use.  For JDK 1.2 and later use the
     * context ClassLoader if possible.  Note: we defer linking the class
     * that calls an API only in JDK 1.2 until runtime so that we can catch
     * LinkageError so that this code will run in older non-Sun JVMs such
     * as the Microsoft JVM in IE.
     * @return Classloader
     */
    public static ClassLoader findClassLoader() {
        ClassLoader classLoader;
        try {
            // Construct the name of the concrete class to instantiate
            Class clazz = Class.forName(DNotesFactoryFinder.class.getName() + "$ClassLoaderFinderConcrete");
            ClassLoaderFinder clf = (ClassLoaderFinder) clazz.newInstance();
            classLoader = clf.getContextClassLoader();
        } catch (LinkageError le) {
            // Assume that we are running JDK 1.1, use the current ClassLoader
            classLoader = DNotesFactoryFinder.class.getClassLoader();
        } catch (ClassNotFoundException x) {
            // This case should not normally happen.  MS IE can throw this
            // instead of a LinkageError the second time Class.forName() is
            // called so assume that we are running JDK 1.1 and use the
            // current ClassLoader
            classLoader = DNotesFactoryFinder.class.getClassLoader();
        } catch (Exception x) {
            // Something abnormal happened so return null
            return null;
        }
        return classLoader;
    }

    /**
     * Returns the value of a property or returns the given default value if no
     * configuration found.
     *
     * @param key the key of the property
     * @param defaultValue default value, if no configuration found
     * @return the value of the property
     */
    public static String getProperty(final String key, final String defaultValue) {
        return fConfiguration.getProperty(key, defaultValue);
    }

    /**
     * Sets a configuration value and overwrites any default or configuration settings.
     *
     * @param key the key of the property
     * @param value the new value
     */
    public static void setProperty(final String key, final String value) {
        fConfiguration.setProperty(key, value);
    }

    /**
     * Error class to indicate factory configuration errors.
     */
    static class ConfigurationError extends Error {

        /** serial version ID for serialization. */
        private static final long serialVersionUID = 3257849874517866546L;

        /**
         * Construct a new instance with the specified detail string and exception.
         * @param msg the error message
         */
        ConfigurationError(final String msg) {
            super(msg);
        }
    }

    /**
     * Nested class that allows getContextClassLoader() to be
     * called only on JDK 1.2 and yet run in older JDK 1.1 JVMs.
     */
    private abstract static class ClassLoaderFinder {
        abstract ClassLoader getContextClassLoader();
    }

    /**
     * Nested class that allows getContextClassLoader() to be
     * called only on JDK 1.2 and yet run in older JDK 1.1 JVMs.
     */
    static class ClassLoaderFinderConcrete extends ClassLoaderFinder {
        /** @return Classloader */
        final ClassLoader getContextClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    /**
     * Interface to the configuration of domingo.
     */
    private static final class Configuration {

        private Properties fProperties;

        /**
         * Loads the configuration.
         */
        private Configuration() {
            ClassLoader classLoader = findClassLoader();
            try {
                fProperties = loadProperties(classLoader, PROPERTIES_FILE);
            } catch (RuntimeException e) {
            }
            if (fProperties == null) {
                try {
                    fProperties = loadProperties(classLoader, PROPERTIES_FILE2);
                } catch (RuntimeException e) {
                }
            }
            if (fProperties == null) {
                fProperties = System.getProperties();
            }
            if (fProperties == null) {
                fProperties = new Properties();
            }
        }

        /**
         * Returns the value of a property or returns the given default value if no
         * configuration found.
         *
         * @param key the key of the property
         * @param defaultValue default value, if no configuration found
         * @return the value of the property
         */
        private String getProperty(final String key, final String defaultValue) {
            return fProperties.getProperty(key, defaultValue);
        }

        /**
         * Sets a configuration value and overwrites any default or configuration settings.
         *
         * @param key the key of the property
         * @param value the new value
         */
        public void setProperty(final String key, final String value) {
            fProperties.setProperty(key, value);
        }

        /**
         * Load a properties resource.
         * The path must include the package name(s), if any, in system directory
         * format and include the properties extension.
         *
         * @param classLoader The class loader to use to find resources
         * @param path The path to the resource
         * @return The initialized Properties object or <code>null</code> if the
         *         resource is not available to the given classloader
         */
        private static Properties loadProperties(final ClassLoader classLoader, final String path) {
            if (classLoader == null) {
                return null;
            }
            InputStream inputStream = classLoader.getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            }
            try {
                return loadProperties(inputStream);
            } catch (IOException e) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                }
            }
            return null;
        }

        /**
         * Loads properties from a given input stream.
         *
         * @param inputStream input stream
         * @return properties
         * @throws IOException if an error occurred when reading from the input stream.
         */
        private static Properties loadProperties(final InputStream inputStream) throws IOException {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Properties properties;
            try {
                properties = new Properties();
                properties.load(bufferedInputStream);
                bufferedInputStream.close();
                inputStream.close();
            } catch (IOException e) {
                properties = null;
                throw e;
            } finally {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                }
            }
            return properties;
        }
    }
//  inputStream.close();
}
