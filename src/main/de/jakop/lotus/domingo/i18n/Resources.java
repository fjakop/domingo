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

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * A class to simplify extracting localized strings, icons
 * and other common resources from a ResourceBundle.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class Resources {

    /** A pseudorandom numbers generator. */
    private static final Random RANDOM = new Random();

    /** Local of Resources. */
    private final Locale locale;

    /** Resource bundle referenced by manager. */
    private ResourceBundle bundle;

    /** Base name of resource bundle. */
    private final String baseName;

    /** ClassLoader from which to load resources. */
    private final ClassLoader classLoader;

    /** Whether an exception should be thrown in case of an unknown resource or not. */
    private boolean fFailOnError;

    /**
     * Constructor that builds a manager in default locale.
     *
     * @param theBaseName the base name of ResourceBundle
     */
    public Resources(final String theBaseName) {
        this(theBaseName, Locale.getDefault(), null);
    }

    /**
     * Constructor that builds a manager in default locale
     * using specified ClassLoader.
     *
     * @param theBaseName the base name of ResourceBundle
     * @param theClassLoader the classLoader to load ResourceBundle from
     */
    public Resources(final String theBaseName, final ClassLoader theClassLoader) {
        this(theBaseName, Locale.getDefault(), theClassLoader);
    }

    /**
     * Constructor that builds a manager in specified locale.
     *
     * @param theBaseName the base name of ResourceBundle
     * @param theLocale the Locale for resource bundle
     */
    public Resources(final String theBaseName, final Locale theLocale) {
        this(theBaseName, theLocale, null);
    }

    /**
     * Constructor that builds a manager in specified locale.
     *
     * @param theBaseName the base name of ResourceBundle
     * @param theLocale the Locale for resource bundle
     * @param theClassLoader the classLoader to load ResourceBundle from
     */
    public Resources(final String theBaseName, final Locale theLocale, final ClassLoader theClassLoader) {
        if (null == theBaseName) {
            throw new NullPointerException("baseName property is null");
        }
        if (null == theLocale) {
            throw new NullPointerException("locale property is null");
        }
        baseName = theBaseName;
        locale = theLocale;
        classLoader = theClassLoader;
    }

    /**
     * Whether an exception should be thrown in case of an unknown resource or
     * not.
     *
     * @param failOnError <code>true</code> if an exception should be thrown
     *            in case of an unknown resource, else <code>false</code>
     * @return the instance itself
     */
    public Resources withFailOnError(final boolean failOnError) {
        this.fFailOnError = failOnError;
        return this;
    }

    /**
     * Retrieve a boolean from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource boolean
     * @throws MissingResourceException if a resource wasn't found
     */
    public boolean getBoolean(final String key, final boolean defaultValue) throws MissingResourceException {
        try {
            return getBoolean(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a boolean from bundle.
     *
     * @param key the key of resource
     * @return the resource boolean
     * @throws MissingResourceException if a resource wasn't found
     */
    public boolean getBoolean(final String key) throws MissingResourceException {
        final ResourceBundle newBundle = getBundle();
        final String value = newBundle.getString(key);
        return value.equalsIgnoreCase("true");
    }

    /**
     * Retrieve a byte from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource byte
     * @throws MissingResourceException if a resource wasn't found
     */
    public byte getByte(final String key, final byte defaultValue) throws MissingResourceException {
        try {
            return getByte(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a byte from bundle.
     *
     * @param key the key of resource
     * @return the resource byte
     * @throws MissingResourceException if a resource wasn't found
     */
    public byte getByte(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            return Byte.parseByte(value);
        } catch (final NumberFormatException nfe) {
            throw new MissingResourceException("Expecting a byte value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a char from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource char
     * @throws MissingResourceException if a resource wasn't found
     */
    public char getChar(final String key, final char defaultValue) throws MissingResourceException {
        try {
            return getChar(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a char from bundle.
     *
     * @param key the key of resource
     * @return the resource char
     * @throws MissingResourceException if a resource wasn't found
     */
    public char getChar(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        if (1 != value.length()) {
            throw new MissingResourceException("Expecting a char value but got " + value, String.class.getName(), key);
        }
        return value.charAt(0);
    }

    /**
     * Retrieve a short from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource short
     * @throws MissingResourceException if a resource wasn't found
     */
    public short getShort(final String key, final short defaultValue) throws MissingResourceException {
        try {
            return getShort(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a short from bundle.
     *
     * @param key the key of resource
     * @return the resource short
     * @throws MissingResourceException if a resource wasn't found
     */
    public short getShort(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            return Short.parseShort(value);
        } catch (final NumberFormatException nfe) {
            throw new MissingResourceException("Expecting a short value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a integer from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource integer
     * @throws MissingResourceException if a resource wasn't found
     */
    public int getInteger(final String key, final int defaultValue) throws MissingResourceException {
        try {
            return getInteger(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a integer from bundle.
     *
     * @param key the key of resource
     * @return the resource integer
     * @throws MissingResourceException if a resource wasn't found
     */
    public int getInteger(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException nfe) {
            throw new MissingResourceException("Expecting a integer value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a long from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource long
     * @throws MissingResourceException if a resource wasn't found
     */
    public long getLong(final String key, final long defaultValue) throws MissingResourceException {
        try {
            return getLong(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a long from bundle.
     *
     * @param key the key of resource
     * @return the resource long
     * @throws MissingResourceException if a resource wasn't found
     */
    public long getLong(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            return Long.parseLong(value);
        } catch (final NumberFormatException nfe) {
            throw new MissingResourceException("Expecting a long value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a float from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource float
     * @throws MissingResourceException if a resource wasn't found
     */
    public float getFloat(final String key, final float defaultValue) throws MissingResourceException {
        try {
            return getFloat(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a float from bundle.
     *
     * @param key the key of resource
     * @return the resource float
     * @throws MissingResourceException if a resource wasn't found
     */
    public float getFloat(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            return Float.parseFloat(value);
        } catch (final NumberFormatException nfe) {
            throw new MissingResourceException("Expecting a float value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a double from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource double
     * @throws MissingResourceException if a resource wasn't found
     */
    public double getDouble(final String key, final double defaultValue) throws MissingResourceException {
        try {
            return getDouble(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a double from bundle.
     *
     * @param key the key of resource
     * @return the resource double
     * @throws MissingResourceException if a resource wasn't found
     */
    public double getDouble(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            return Double.parseDouble(value);
        } catch (final NumberFormatException nfe) {
            throw new MissingResourceException("Expecting a double value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a date from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource date
     * @throws MissingResourceException if a resource wasn't found
     */
    public Date getDate(final String key, final Date defaultValue) throws MissingResourceException {
        try {
            return getDate(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a date from bundle.
     *
     * @param key the key of resource
     * @return the resource date
     * @throws MissingResourceException if a resource wasn't found
     */
    public Date getDate(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            final DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
            return format.parse(value);
        } catch (final ParseException pe) {
            throw new MissingResourceException("Expecting a date value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource time
     * @throws MissingResourceException if a resource wasn't found
     */
    public Date getTime(final String key, final Date defaultValue) throws MissingResourceException {
        try {
            return getTime(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key the key of resource
     * @return the resource time
     * @throws MissingResourceException if a resource wasn't found
     */
    public Date getTime(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            final DateFormat format = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
            return format.parse(value);
        } catch (final ParseException pe) {
            throw new MissingResourceException("Expecting a time value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key the key of resource
     * @param defaultValue the default value if key is missing
     * @return the resource time
     * @throws MissingResourceException if a resource wasn't found
     */
    public Date getDateTime(final String key, final Date defaultValue) throws MissingResourceException {
        try {
            return getDateTime(key);
        } catch (final MissingResourceException mre) {
            return defaultValue;
        }
    }

    /**
     * Retrieve a date + time from bundle.
     *
     * @param key the key of resource
     * @return the resource date + time
     * @throws MissingResourceException if a resource wasn't found
     */
    public Date getDateTime(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final String value = theBundle.getString(key);
        try {
            final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
            return format.parse(value);
        } catch (final ParseException pe) {
            throw new MissingResourceException("Expecting a date/time value but got " + value, String.class.getName(), key);
        }
    }

    /**
     * Retrieve a raw string from bundle.
     *
     * @param key the key of resource
     * @return the resource string
     * @throws MissingResourceException if a resource wasn't found
     */
    public String getString(final String key) throws MissingResourceException {
        final Object[] args = new Object[] {};
        return format(key, args);
    }

    /**
     * Retrieve a string from resource bundle and format it with specified arguments.
     *
     * @param key the key for resource
     * @param arg1 an argument
     * @return the formatted string
     */
    public String getString(final String key, final Object arg1) {
        final Object[] args = new Object[] {arg1 };
        return format(key, args);
    }

    /**
     * Retrieve a string from resource bundle and format it with specified arguments.
     *
     * @param key the key for resource
     * @param arg1 an argument
     * @param arg2 an argument
     * @return the formatted string
     */
    public String getString(final String key, final Object arg1, final Object arg2) {
        final Object[] args = new Object[] {arg1, arg2 };
        return format(key, args);
    }

    /**
     * Retrieve a string from resource bundle and format it with specified arguments.
     *
     * @param key the key for resource
     * @param arg1 an argument
     * @param arg2 an argument
     * @param arg3 an argument
     * @return the formatted string
     */
    public String getString(final String key, final Object arg1, final Object arg2, final Object arg3) {
        final Object[] args = new Object[] {arg1, arg2, arg3 };
        return format(key, args);
    }

    /**
     * Retrieve a string from resource bundle and format it with specified arguments.
     *
     * @param key the key for resource
     * @param arg1 an argument
     * @param arg2 an argument
     * @param arg3 an argument
     * @param arg4 an argument
     * @return the formatted string
     */
    public String getString(final String key, final Object arg1, final Object arg2, final Object arg3, final Object arg4) {
        final Object[] args = new Object[] {arg1, arg2, arg3, arg4 };
        return format(key, args);
    }

    /**
     * Retrieve a string from resource bundle and format it with specified arguments.
     *
     * @param key the key for resource
     * @param args an array of arguments
     * @return the formatted string
     * @throws MissingResourceException if {@link #withFailOnError(boolean)} is true and the requested resource wasn't found
     * @see #withFailOnError(boolean)
     */
    public String format(final String key, final Object[] args) throws MissingResourceException {
        try {
            final String pattern = getPatternString(key);
            return MessageFormat.format(pattern, args);
        } catch (final MissingResourceException mre) {
            if (fFailOnError) {
                throw mre;
            }
            final StringBuffer sb = new StringBuffer();
            sb.append("Unknown resource. Bundle: '");
            sb.append(baseName);
            sb.append("' key: '");
            sb.append(key);
            sb.append("' arguments: '");
            for (int i = 0; i < args.length; i++) {
                if (0 != i) {
                    sb.append("', '");
                }
                sb.append(args[i]);
            }
            sb.append("' Reason: ");
            sb.append(mre);
            return sb.toString();
        }
    }

    /**
     * Retrieve underlying ResourceBundle.
     * If bundle has not been loaded it will be loaded by this method.
     * Access is given in case other resources need to be extracted
     * that this Manager does not provide simplified access to.
     *
     * @return the ResourceBundle
     * @throws MissingResourceException if an error occurs
     */
    public ResourceBundle getBundle() throws MissingResourceException {
        if (null == bundle) {
            // bundle wasn't cached, so load it, cache it, and return it.
            ClassLoader theClassLoader = this.classLoader;
            if (null == theClassLoader) {
                theClassLoader = Thread.currentThread().getContextClassLoader();
            }
            if (null != theClassLoader) {
                bundle = ResourceBundle.getBundle(baseName, locale, theClassLoader);
            } else {
                bundle = ResourceBundle.getBundle(baseName, locale);
            }
        }
        return bundle;
    }

    /**
     * Utility method to retrieve a string from ResourceBundle.
     * If the key is a single string then that will be returned.
     * If key refers to string array then a random string will be chosen.
     * Other types cause an exception.
     *
     * @param key the key to resource
     * @return the string resource
     * @throws MissingResourceException if an error occurs
     */
    private String getPatternString(final String key) throws MissingResourceException {
        final ResourceBundle theBundle = getBundle();
        final Object object = theBundle.getObject(key);
        // is the resource a single string
        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof String[]) {
            //if string array then randomly pick one
            final String[] strings = (String[]) object;
            return strings[RANDOM.nextInt(strings.length)];
        } else {
            throw new MissingResourceException("Unable to find resource of appropriate type.", String.class.getName(), key);
        }
    }
}
