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

package de.jakop.lotus.domingo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lotus.domino.Session;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DItem;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.DViewEntry;
import de.jakop.lotus.domingo.exception.ExceptionUtil;
import de.jakop.lotus.domingo.proxy.BaseProxy;
import de.jakop.lotus.domingo.proxy.ViewProxy.ViewEntriesIterator;


/**
 * Creates Java source code of all Notes Java-API calls.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class NotesJavaWriter {

    // todo getFirstEntry() is not called after creating an Iterator of a ViewEntryCollection
    // todo code for list arguments is not correct Java code

    /** Default file name to log java calls. */
    private static final String DEFAULT_JAVA_LOGFILE = ""; // "notes-java-calls.log";

    /** Name of property of java log file. */
    public static final String JAVA_LOGFILE = "de.bea.domingo.java.logfile";

    /** Map from domingo types to corresponding Lotus types. */
    private static final Map TYPES = new HashMap();

    /** Singleton instance. */
    private static NotesJavaWriter instance = null;

    /** Reference to the output stream. */
    private PrintStream stream = null;

    static {
        // Initializing the TYPES map
        TYPES.put(DDatabase.class, "Database");
        TYPES.put(DView.class, "View");
        TYPES.put(DViewEntry.class, "ViewEntry");
        TYPES.put(DDocument.class, "Document");
        TYPES.put(DItem.class, "Item");
        TYPES.put(ViewEntriesIterator.class, "ViewEntriesIterator");
        TYPES.put(Iterator.class, "Iterator");
        TYPES.put(String.class, "String");
        TYPES.put(Integer.class, "Integer");
        TYPES.put(Double.class, "Double");
        TYPES.put(List.class, "List");
    }

    /**
     * Constructor.
     *
     * <p>The <code>de.bea.domingo.java.logfile</code> property from the
     * resource file <code>de/bea/domingo/domingo.properties</code>
     * is used to determine the output file for java logging. If this
     * property is empty or not set, no output is generated.</p>
     */
    private NotesJavaWriter() {
        final String javaLogfile = DNotesFactory.getProperty(JAVA_LOGFILE, DEFAULT_JAVA_LOGFILE);
        if (javaLogfile != null && !"".equals(javaLogfile)) {
            final File file = new File(javaLogfile);
            try {
                stream = new PrintStream(new FileOutputStream(file));
                stream.println("NotesThread.sinitThread();");
                stream.println("Session session = NotesFactory.createSession();");
                stream.println("Map map = new HashMap();");
            } catch (FileNotFoundException e) {
                System.out.println("Cannot open java-logfile: " + javaLogfile);
            }
        }
    }

    /**
     * Returns the singleton instance of this class.
     * @return singleton instance of this class
     */
    public static NotesJavaWriter getInstance() {
        if (instance == null) {
            instance = new NotesJavaWriter();
        }
        return instance;
    }

    /**
     * Logs an invocation to Java-like line.
     *
     * @param result the return value of the method call
     * @param object the proxy instance that the method was invoked on
     * @param method the <code>Method</code> instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the <code>Method</code> object will be the interface that
     * the method was declared in, which may be a super-interface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param args an array of objects containing the values of the
     * @param throwable an optional throwable that occurred during method call
     * <p>arguments passed in the method invocation on the proxy instance,
     * or <code>null</code> if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * <code>java.lang.Integer</code> or <code>java.lang.Boolean</code>.</p>
     */
    public void logInvocation(final Object result, final Object object, final Method method, final Object[] args,
            final Throwable throwable) {
        logInvocation(result, object, method.getName(), args, throwable);
    }

    /**
     * Logs a method invocation as Java code.
     *
     * @param result the return value of the method call
     * @param object the proxy instance that the method was invoked on
     * @param method the name of the method.
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or <code>null</code>
     * @param throwable an optional throwable that occurred during method call
     * <p>if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * <code>java.lang.Integer</code> or <code>java.lang.Boolean</code>.</p>
     */
    public void logInvocation(final Object result, final Object object, final String method, final Object[] args,
            final Throwable throwable) {
        if (stream ==  null) {
            return;
        }
        final StringBuffer arguments = new StringBuffer();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                final Object arg = args[i];
                if (i > 0) {
                    arguments.append(", ");
                }
                arguments.append(objectToString(arg));
            }
        }
        final String methodName;
        if (method.equals("hasNext")) {
            methodName = null;
        } else if (method.equals("next")) {
            methodName = "getNextEntry";
        } else {
            methodName = method;
        }
        if (methodName != null) {
            stream.println(setObjectString(result, objectToString(object) + "." + methodName + "("
                    + arguments.toString() + ")") + ";" + commentObject(result, throwable));
        }
        if (object instanceof Session && method.equals("recycle")) {
            stream.println("NotesThread.stermThread();");
        }
    }

    /**
     * Creates a Java comment about an object.
     * @param obj the object
     * @param throwable an optional throwable that occurred during method call
     * @return Java comment for the object
     */
    private String commentObject(final Object obj, final Throwable throwable) {
        if (throwable != null) {
            return " //* throws " + throwable.getMessage() + "\n" + ExceptionUtil.getStackTrace(throwable) + "\n */";
        } else if (obj == null) {
            return " // returns null";
        } else if (obj instanceof String && "".equals(obj)) {
            return " // returns empty string";
        } else if (obj instanceof String) {
            return " // returns \"" + obj + "\"";
        } else if (obj instanceof Number) {
            return " // returns " + obj;
        } else if (obj instanceof Calendar) {
            return " // returns " + obj;
        } else if (obj instanceof BaseProxy) {
            return " // return " + ((BaseProxy) obj).refereceHashCode();
        } else {
            return " // returns " + obj.hashCode();
        }
    }

    /**
     * Returns a Java statement that sets the value of an expression
     * into a map where the key is the hashcode of the object.
     *
     * @param obj the object
     * @param expression the expression
     * @return statement to set expression in a map
     */
    private String setObjectString(final Object obj, final String expression) {
        if (obj == null) {
            return expression;
        } else if (obj instanceof String) {
            return expression;
        } else if (obj instanceof Double) {
            return expression;
        } else if (obj instanceof Long) {
            return expression;
        } else if (obj instanceof Integer) {
            return expression;
        } else if (obj instanceof Calendar) {
            return expression;
        } else if (obj instanceof DSession) {
            return "session";
        } else if (obj instanceof BaseProxy) {
            return "map.put(\"" + ((BaseProxy) obj).refereceHashCode() + "\", " + expression + ")";
        } else {
            return "map.put(\"" + obj.hashCode() + "\", " + expression + ")";
        }
    }

    /**
     * Converts an object to a string that can be used in Java code
     * as an expression for that object.
     * @param obj the object
     * @return a Java expression for that object
     */
    private String objectToString(final Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return "\"" + obj.toString().replace('\\', '/') + "\"";
        } else if (obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof Boolean) {
            return obj.toString();
        } else if (obj instanceof Session) {
            return "session";
        } else if (Proxy.isProxyClass(obj.getClass())) {
            return "error(" + obj.getClass().getName() + ")";
        } else if (obj instanceof List) {
            return obj.toString();
        } else if (obj instanceof BaseProxy) {
            final BaseProxy proxy = (BaseProxy) obj;
            return "((" + getType(obj) + ") map.get(\"" + proxy.refereceHashCode() + "\"))";
        } else {
            return "((" + getType(obj) + ") map.get(\"" + obj.hashCode() + "\"))";
        }
    }

    /**
     * Returns the type of an object as to be used e.g. in a cast operator.
     * @param obj the object
     * @return type of the object
     */
    static String getType(final Object obj) {
        Iterator iterator = TYPES.keySet().iterator();
        while (iterator.hasNext()) {
            Class clazz = (Class) iterator.next();
            if (clazz.isAssignableFrom(obj.getClass())) {
                return (String) TYPES.get(clazz);
            }
        }
        if (obj.getClass().getName().startsWith("lotus.")) {
            final String className = obj.getClass().getName();
            return className.substring(className.lastIndexOf('.') + 1);
        } else {
            return "unknown." + obj.getClass().getName();
        }
    }
}
