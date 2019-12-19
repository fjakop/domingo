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

package de.jakop.lotus.domingo.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Utilities for manipulating and examining <code>Throwable</code>s.
 */
public final class ExceptionUtil {

    /** Platform specific line separator. */
    private static final String LINE_SEPARATOR;

    /** An empty immutable <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** An empty immutable <code>Object</code> array. */
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /** Names of methods commonly used to access a wrapped exception. */
    private static final String[] CAUSE_METHOD_NAMES = { "getCause", "getNextException", "getTargetException", "getException",
            "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException",
            "getNestedException", "getLinkedCause", "getThrowable", };

    /** Method object for Java 1.4 getCause. */
    private static final Method THROWABLE_CAUSE_METHOD;

    /** Package separator character: <code>'&#x2e;' == {@value}</code>. */
    public static final char PACKAGE_SEPARATOR_CHAR = '.';

    /** The package separator String: <code>"&#x2e;"</code>. */
    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

    /** Inner class separator character: <code>'$' == {@value}</code>. */
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

    static {
        Method causeMethod;
        try {
            causeMethod = Throwable.class.getMethod("getCause", null);
        } catch (Exception e) {
            causeMethod = null;
        }
        THROWABLE_CAUSE_METHOD = causeMethod;
        String lineSeperator = null;
        try {
            lineSeperator = System.getProperty("line.separator");
        } catch (SecurityException ex) {
        }
        LINE_SEPARATOR = lineSeperator == null ? "\n" : lineSeperator;
    }

    /**
     * Prevents instantiation.
     */
    private ExceptionUtil() {
        super();
    }

    /**
     * Returns the given list as a <code>String[]</code>.
     *
     * @param list a list to transform.
     * @return the given list as a <code>String[]</code>.
     */
    private static String[] toArray(final List list) {
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * <p>Introspects the <code>Throwable</code> to obtain the cause.</p>
     *
     * <p>The method searches for methods with specific names that return a
     * <code>Throwable</code> object. This will pick up most wrapping
     * exceptions, including those from JDK 1.4, and the domingo exceptions.</p>
     *
     * <p>The default list searched for are:</p> <ul> <li><code>getCause()</code></li>
     * <li><code>getNextException()</code></li> <li><code>getTargetException()</code></li>
     * <li><code>getException()</code></li> <li><code>getSourceException()</code></li>
     * <li><code>getRootCause()</code></li> <li><code>getCausedByException()</code></li>
     * <li><code>getNested()</code></li> </ul>
     *
     * <p>In the absence of any such method, the object is inspected for a
     * <code>detail</code> field assignable to a <code>Throwable</code>.</p>
     *
     * <p>If none of the above is found, returns <code>null</code>.</p>
     *
     * @param throwable the throwable to introspect for a cause, may be null
     * @return the cause of the <code>Throwable</code>, <code>null</code>
     *         if none found or null throwable input
     */
    public static Throwable getCause(final Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        Throwable cause = getCauseUsingWellKnownTypes(throwable);
        if (cause == null) {
            for (int i = 0; i < CAUSE_METHOD_NAMES.length; i++) {
                String methodName = CAUSE_METHOD_NAMES[i];
                if (methodName != null) {
                    cause = getCauseUsingMethodName(throwable, methodName);
                    if (cause != null) {
                        break;
                    }
                }
            }

            if (cause == null) {
                cause = getCauseUsingFieldName(throwable, "detail");
            }
        }
        return cause;
    }

    /**
     * <p>Finds a <code>Throwable</code> for known types.</p>
     *
     * <p>Uses <code>instanceof</code> checks to examine the exception,
     * looking for well known types which could contain chained or wrapped
     * exceptions.</p>
     *
     * @param throwable the exception to examine
     * @return the wrapped exception, or <code>null</code> if not found
     */
    private static Throwable getCauseUsingWellKnownTypes(final Throwable throwable) {
        if (throwable instanceof CascadingThrowable) {
            return ((CascadingThrowable) throwable).getCause();
        } else if (throwable instanceof SQLException) {
            return ((SQLException) throwable).getNextException();
        } else if (throwable instanceof InvocationTargetException) {
            return ((InvocationTargetException) throwable).getTargetException();
        } else {
            return null;
        }
    }

    /**
     * <p>Finds a <code>Throwable</code> by method name.</p>
     *
     * @param throwable the exception to examine
     * @param methodName the name of the method to find and invoke
     * @return the wrapped exception, or <code>null</code> if not found
     */
    private static Throwable getCauseUsingMethodName(final Throwable throwable, final String methodName) {
        Method method = null;
        try {
            method = throwable.getClass().getMethod(methodName, null);
        } catch (NoSuchMethodException ignored) {
            // exception ignored
        } catch (SecurityException ignored) {
            // exception ignored
        }

        if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
            try {
                return (Throwable) method.invoke(throwable, EMPTY_OBJECT_ARRAY);
            } catch (IllegalAccessException ignored) {
                // exception ignored
            } catch (IllegalArgumentException ignored) {
                // exception ignored
            } catch (InvocationTargetException ignored) {
                // exception ignored
            }
        }
        return null;
    }

    /**
     * <p>Finds a <code>Throwable</code> by field name.</p>
     *
     * @param throwable the exception to examine
     * @param fieldName the name of the attribute to examine
     * @return the wrapped exception, or <code>null</code> if not found
     */
    private static Throwable getCauseUsingFieldName(final Throwable throwable, final String fieldName) {
        Field field = null;
        try {
            field = throwable.getClass().getField(fieldName);
        } catch (NoSuchFieldException ignored) {
            // exception ignored
        } catch (SecurityException ignored) {
            // exception ignored
        }

        if (field != null && Throwable.class.isAssignableFrom(field.getType())) {
            try {
                return (Throwable) field.get(throwable);
            } catch (IllegalAccessException ignored) {
                // exception ignored
            } catch (IllegalArgumentException ignored) {
                // exception ignored
            }
        }
        return null;
    }

    /**
     * <p>Checks if the Throwable class has a <code>getCause</code> method.</p>
     *
     * <p>This is true for JDK 1.4 and above.</p>
     *
     * @return true if Throwable is cascading
     */
    public static boolean isCascadingThrowable() {
        return THROWABLE_CAUSE_METHOD != null;
    }

    /**
     * <p>Checks whether this <code>Throwable</code> class can store a cause.</p>
     *
     * <p>This method does <b>not</b> check whether it actually does store a
     * cause.<p>
     *
     * @param throwable the <code>Throwable</code> to examine, may be null
     * @return boolean <code>true</code> if cascading, otherwise <code>false</code>
     */
    public static boolean isCascadingThrowable(final Throwable throwable) {
        if (throwable == null) {
            return false;
        }

        if (throwable instanceof CascadingThrowable) {
            return true;
        } else if (throwable instanceof SQLException) {
            return true;
        } else if (throwable instanceof InvocationTargetException) {
            return true;
        } else if (isCascadingThrowable()) {
            return true;
        }

        Class cls = throwable.getClass();
        int isize = CAUSE_METHOD_NAMES.length;
        for (int i = 0; i < isize; i++) {
            try {
                Method method = cls.getMethod(CAUSE_METHOD_NAMES[i], null);
                if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
                    return true;
                }
            } catch (NoSuchMethodException ignored) {
                // exception ignored
            } catch (SecurityException ignored) {
                // exception ignored
            }
        }

        try {
            Field field = cls.getField("detail");
            if (field != null) {
                return true;
            }
        } catch (NoSuchFieldException ignored) {
            // exception ignored
        } catch (SecurityException ignored) {
            // exception ignored
        }

        return false;
    }

    /**
     * <p>Removes common frames from the cause trace given the two stack
     * traces.</p>
     *
     * @param causeFrames stack trace of a cause throwable
     * @param wrapperFrames stack trace of a wrapper throwable
     * @throws IllegalArgumentException if either argument is null
     */
    public static void removeCommonFrames(final List causeFrames, final List wrapperFrames) throws IllegalArgumentException {
        if (causeFrames == null || wrapperFrames == null) {
            throw new IllegalArgumentException("The List must not be null");
        }
        int causeFrameIndex = causeFrames.size() - 1;
        int wrapperFrameIndex = wrapperFrames.size() - 1;
        while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
            // Remove the frame from the cause trace if it is the same
            // as in the wrapper trace
            String causeFrame = (String) causeFrames.get(causeFrameIndex);
            String wrapperFrame = (String) wrapperFrames.get(wrapperFrameIndex);
            if (causeFrame.equals(wrapperFrame)) {
                causeFrames.remove(causeFrameIndex);
            }
            causeFrameIndex--;
            wrapperFrameIndex--;
        }
    }

    /**
     * <p>Gets the stack trace from a Throwable as a String.</p>
     *
     * <p>The result of this method vary by JDK version as this method uses
     * {@link Throwable#printStackTrace(java.io.PrintWriter)}. On JDK1.3 and
     * earlier, the cause exception will not be shown unless the specified
     * throwable alters printStackTrace.</p>
     *
     * @param throwable the <code>Throwable</code> to be examined
     * @return the stack trace as generated by the exception's
     *         <code>printStackTrace(PrintWriter)</code> method
     */
    public static String getStackTrace(final Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    /**
     * <p>Returns an array where each element is a line from the argument.</p>
     *
     * <p>The end of line is determined by the value of
     * <tt>line.separator</tt> system property.</p>
     *
     * @param stackTrace a stack trace String
     * @return an array where each element is a line from the argument
     */
    static String[] getStackFrames(final String stackTrace) {
        String linebreak = LINE_SEPARATOR;
        StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        List list = new ArrayList();
        while (frames.hasMoreTokens()) {
            list.add(frames.nextToken());
        }
        return toArray(list);
    }

    /**
     * <p>Produces a <code>List</code> of stack frames - the message is not
     * included. Only the trace of the specified exception is returned, any
     * caused by trace is stripped.</p>
     *
     * <p>This works in most cases - it will only fail if the exception message
     * contains a line that starts with:
     * <code>&quot;&nbsp;&nbsp;&nbsp;at&quot;.</code></p>
     *
     * @param t is any throwable
     * @return List of stack frames
     */
    static List getStackFrameList(final Throwable t) {
        String stackTrace = getStackTrace(t);
        String linebreak = LINE_SEPARATOR;
        StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
        List list = new ArrayList();
        boolean traceStarted = false;
        while (frames.hasMoreTokens()) {
            String token = frames.nextToken();
            // Determine if the line starts with <whitespace>at
            int at = token.indexOf("at");
            if (at != -1 && token.substring(0, at).trim().length() == 0) {
                traceStarted = true;
                list.add(token);
            } else if (traceStarted) {
                break;
            }
        }
        return list;
    }

    /**
     * <p>Gets the class name minus the package name for an <code>Object</code>.</p>
     *
     * @param object  the class to get the short name for, may be null
     * @param valueIfNull  the value to return if null
     * @return the class name of the object without the package name, or the null value
     */
    public static String getShortClassName(final Object object, final String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortClassName(object.getClass().getName());
    }

    /**
     * <p>Gets the class name minus the package name from a <code>Class</code>.</p>
     *
     * @param clazz  the class to get the short name for.
     * @return the class name without the package name or an empty string
     */
    public static String getShortClassName(final Class clazz) {
        if (clazz == null) {
            return "";
        }
        return getShortClassName(clazz.getName());
    }

    /**
     * <p>Gets the class name minus the package name from a String.</p>
     *
     * <p>The string passed in is assumed to be a class name - it is not checked.</p>
     *
     * @param className  the className to get the short name for
     * @return the class name of the class without the package name or an empty string
     */
    public static String getShortClassName(final String className) {
        if (className == null) {
            return "";
        }
        if (className.length() == 0) {
            return "";
        }
        char[] chars = className.toCharArray();
        int lastDot = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == PACKAGE_SEPARATOR_CHAR) {
                lastDot = i + 1;
            } else if (chars[i] == INNER_CLASS_SEPARATOR_CHAR) {  // handle inner classes
                chars[i] = PACKAGE_SEPARATOR_CHAR;
            }
        }
        return new String(chars, lastDot, chars.length - lastDot);
    }

    /**
     * Prints the stack trace of a throwable to the standard error stream.
     *
     * @param throwable a cascading throwable
     */
    public static void printStackTrace(final Throwable throwable) {
        printStackTrace(throwable, System.err);
    }

    /**
     * Prints the stack trace of a throwable to the specified stream.
     *
     * @param throwable a cascading throwable
     * @param out <code>PrintStream</code> to use for output.
     * @see #printStackTrace(Throwable, PrintWriter)
     */
    public static void printStackTrace(final Throwable throwable, final PrintStream out) {
        synchronized (out) {
            PrintWriter pw = new PrintWriter(out, false);
            printStackTrace(throwable, pw);
            pw.flush();
        }
    }

    /**
     * Prints the stack trace of a throwable to the specified writer.
     *
     * @param throwable a cascading throwable
     * @param out <code>PrintWriter</code> to use for output.
     */
    public static void printStackTrace(final Throwable throwable, final PrintWriter out) {
        Throwable currentThrowable = throwable;
        // if running on jre1.4 or higher, use default printStackTrace
        if (ExceptionUtil.isCascadingThrowable()) {
            if (currentThrowable instanceof CascadingThrowable) {
                ((CascadingThrowable) currentThrowable).printPartialStackTrace(out);
            } else {
                currentThrowable.printStackTrace(out);
            }
            return;
        }

        // generating the cascading stack trace
        List stacks = new ArrayList();
        while (currentThrowable != null) {
            String[] st = getStackFrames(currentThrowable);
            stacks.add(st);
            currentThrowable = ExceptionUtil.getCause(currentThrowable);
        }

        String separatorLine = "Caused by: ";
        trimStackFrames(stacks);

        synchronized (out) {
            for (Iterator iter = stacks.iterator(); iter.hasNext();) {
                String[] st = (String[]) iter.next();
                int len = st.length;
                for (int i = 0; i < len; i++) {
                    out.println(st[i]);
                }
                if (iter.hasNext()) {
                    out.print(separatorLine);
                }
            }
        }
    }

    /**
     * Captures the stack trace associated with the specified
     * <code>Throwable</code> object, decomposing it into a list of stack
     * frames.
     *
     * @param throwable The <code>Throwable</code>.
     * @return An array of strings describing each stack frame.
     */
    protected static String[] getStackFrames(final Throwable throwable) {
        if (throwable == null) {
            return EMPTY_STRING_ARRAY;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);

        // Avoid infinite loop between decompose() and printStackTrace().
        if (throwable instanceof CascadingThrowable) {
            ((CascadingThrowable) throwable).printPartialStackTrace(pw);
        } else {
            throwable.printStackTrace(pw);
        }
        return ExceptionUtil.getStackFrames(sw.getBuffer().toString());
    }

    /**
     * Trims the stack frames. The first set is left untouched. The rest of the
     * frames are truncated from the bottom by comparing with one just on top.
     *
     * @param stacks The list containing String[] elements
     */
    protected static void trimStackFrames(final List stacks) {
        int size = stacks.size();
        for (int i = size - 1; i > 0; i--) {
            String[] curr = (String[]) stacks.get(i);
            String[] next = (String[]) stacks.get(i - 1);

            List currList = new ArrayList(Arrays.asList(curr));
            List nextList = new ArrayList(Arrays.asList(next));
            ExceptionUtil.removeCommonFrames(currList, nextList);

            int trimmed = curr.length - currList.size();
            if (trimmed > 0) {
                currList.add("\t... " + trimmed + " more");
                stacks.set(i, currList.toArray(new String[currList.size()]));
            }
        }
    }
}
