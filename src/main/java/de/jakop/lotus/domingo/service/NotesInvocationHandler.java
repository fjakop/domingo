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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.jakop.lotus.domingo.DDateRange;
import de.jakop.lotus.domingo.DNotesFactory;

/**
 * Invocation handler for all dynamic proxies of interfaces of the
 * domingo API.
 *
 * <p>This class is responsible to separate internal objects from external
 * proxies, where especially arguments and return values must be extracted
 * and wrapped thru each method invocation</p>.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class NotesInvocationHandler implements InvocationHandler {

    /** Reference to Object to invoke on. */
    private final Object object;

    /**
     * Constructor.
     * @param theObject the wrapped object
     */
    public NotesInvocationHandler(final Object theObject) {
        super();
        object = theObject;
    }

    /**
     * Getter method for object attribute.
     *
     * @return associated object.
     */
    Object getObject() {
        return object;
    }

    /**
     * Processes a method invocation on a proxy instance and returns the result.
     * <p>All arguments that are proxies to a NotesInvocationHandler are
     * extracted from the proxy to the original objects. If the resulting object
     * is a Notes object, a new proxy will be created.</p>
     *
     * <p><b>Further Details from
     * {@link java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method,
     *        java.lang.Object[])}:</b></p>
     * <p>{@inheritDoc}</p>
     *
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final NotesServiceFactory factory = (NotesServiceFactory) DNotesFactory.getInstance();
        final Object result;
        final Object[] extractedArgs = extractArguments(args);
        try {
            result = factory.invoke(object, method, extractedArgs);
        } catch (Throwable t) {
            NotesJavaWriter.getInstance().logInvocation(null, object, method, extractedArgs, t);
            throw t;
        }
        NotesJavaWriter.getInstance().logInvocation(result, object, method, extractedArgs, null);
        return packObject(result);
    }

    /**
     * Extracts all arguments that are dynamic proxies.
     *
     * @param args array of objects
     * @return array of extracted objects
     */
    private Object[] extractArguments(final Object[] args) {
        Object[] extractedArgs;
        if (args == null) {
            extractedArgs = null;
        } else {
            extractedArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    extractedArgs[i] = null;
                } else if (Proxy.isProxyClass(args[i].getClass())) {
                    extractedArgs[i] = extractObject(args[i]);
                } else {
                    extractedArgs[i] = args[i];
                }
            }
        }
        return extractedArgs;
    }

    /**
     * Extracts an object if it is a dynamic proxy.
     *
     * @param obj an object
     * @return extracted object
     */
    private Object extractObject(final Object obj) {
        final InvocationHandler invocationHandler = Proxy.getInvocationHandler(obj);
        if (invocationHandler instanceof NotesInvocationHandler) {
            return ((NotesInvocationHandler) invocationHandler).getObject();
        } else {
            return obj;
        }
    }

    /**
     * Packs an object into a dynamic proxy if it is a Domingo object.
     *
     * @param obj an object
     * @return packed object
     */
    private Object packObject(final Object obj) {
        if (obj == null) {
            return obj;
        } else if (obj instanceof Calendar) {
            return obj;
        } else if (obj instanceof DDateRange) {
            return obj;
        } else if (obj.getClass().getPackage().getName().startsWith("de.bea.domingo")) {
            return wrapObject(obj);
        } else if (obj instanceof List) {
            final List list = new ArrayList((List) obj);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null) {
                    if (list.get(i).getClass().getPackage().getName().startsWith("de.bea.domingo")) {
                        set(list, i);
                    }
                }
            }
        }
        return obj;
    }

    private void set(final List list, final int i) {
        Object obj = list.get(i);
        Object pack = packObject(obj);
        if (pack != obj) {
            list.set(i, pack);
        }
    }

    /**
     * Wraps an object into a dynamic proxy.
     *
     * @param obj the object to wrap
     * @return the wrapped object
     */
    private Object wrapObject(final Object obj) {
        return getNotesProxy(obj.getClass().getInterfaces(), obj);
    }

    /**
     * Creates a Proxy for an Interface to an Object.
     *
     * @param theInterfaces array of interface of the proxy
     * @param theObject th object to be wrapped
     * @return proxy object
     */
    public static Object getNotesProxy(final Class[] theInterfaces, final Object theObject) {
        return Proxy.newProxyInstance(
            theObject.getClass().getClassLoader(),
            theInterfaces,
            new NotesInvocationHandler(theObject));
    }
}
