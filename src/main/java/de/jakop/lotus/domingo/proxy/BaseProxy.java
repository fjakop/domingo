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

package de.jakop.lotus.domingo.proxy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.exception.DominoException;
import de.jakop.lotus.domingo.monitor.AbstractMonitorEnabled;
import lotus.domino.Base;
import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesIterator;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.i18n.ResourceManager;
import de.jakop.lotus.domingo.i18n.Resources;

/**
 * Abstract base class for all implementations of interfaces derived from
 * <code>DBase</code>.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class BaseProxy extends AbstractMonitorEnabled implements DBase {

    /** Internationalized resources. */
    protected static final Resources RESOURCES = ResourceManager.getPackageResources(NotesProxyFactory.class);

    ////////////////////////////////////////////////
    // constants
    ////////////////////////////////////////////////

    /** Number of characters needed to represent a date/time value. */
    protected static final int DATETIME_STRING_LENGTH = 20;

    /** Maximum number of items supported in method getItemValues(). */
    protected static final int NUM_DATETIME_VALUES = 1000;

    /** maximum number of characters to parse in method getItemValues(). */
    protected static final int MAX_DATETIME_LENGTH = NUM_DATETIME_VALUES * DATETIME_STRING_LENGTH;

    /** The empty string. */
    protected static final String EMPTY_STRING = "";

    ////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////

    /** Reference to the parent object. */
    private final DBase parent;

    /**
     * Reference to the notes object.
     *
     * <p>This reference can be either a notes object directly or a
     * <code>WeakReference</code> to a notes object, if the notes object
     * can be recycled.
     *
     * @see BaseProxy#getNotesObject()
     */
    private Object ref;

    /** Reference to the factory which controls this instance. */
    private final NotesProxyFactory factory;

    ////////////////////////////////////////////////
    // creation
    ////////////////////////////////////////////////

    /**
     * Creates a new DBaseImpl object.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param object the notes object
     * @param monitor the monitor
     */
    protected BaseProxy(final NotesProxyFactory theFactory, final DBase theParent,
                        final Base object, final DNotesMonitor monitor) {
        super(monitor);
        this.factory = theFactory;
        this.parent = theParent;
        this.ref = object;
        //getMonitor().debug("initialize " + super.toString());
    }

    ////////////////////////////////////////////////
    // protected utility methods for sub classes
    ////////////////////////////////////////////////

    /**
     * Returns the Notes object associated with an instance.
     *
     * @return associated Notes object
     */
    protected final Base getNotesObject() {
        if (ref instanceof WeakReference) {
            return (Base) ((WeakReference) ref).get();
        }
        return (Base) ref;
    }

    /**
     * Clears the associated notes object, used during dispose/recycle operations.
     */
    protected final void clearNotesObject() {
        ref = null;
    }

    /**
     * Returns the parent object.
     *
     * @return the parent object or <code>null</code> if no parent available
     */
    protected final DBase getParent() {
        return parent;
    }

    /**
     * Returns the factory corresponding to this instance.
     *
     * @return the corresponding factory
     */
    protected final NotesProxyFactory getFactory() {
        return factory;
    }

    /**
     * Utility method for the toString method of derived classes.
     * Returns a string representation of the object.
     *
     * @return string representation of the object
     *
     * @see java.lang.Object#toString()
     */
    public final String toStringGeneric() {
        final Base base = getNotesObject();
        if (base != null) {
            return base.toString();
        }
        return super.toString();
    }

    /**
     * Convert List to Vector.
     *
     * <p>If the implementation of the <code>List</code> interface is a Vector,
     * this vector is used unchanged, else the references of elements of the
     * <code>List</code> are copied to  a new result <code>Vector</code>.
     *
     * @param list a list
     * @return a Vector containing the elements of the list argument
     */
    protected final Vector convertListToVector(final List list) {
        if (list == null) {
            return null;
        }
        if (list instanceof Vector) {
            return (Vector) list;
        }
        final Vector v = new Vector();
        for (int i = 0; i < list.size(); i++) {
            v.addElement(list.get(i));
        }
        return v;
    }

    /**
     * Convert Vector to List.
     *
     * A Vector does also implement the <code>List</code> interface. But it is
     * synchronized. Therefore this conversion method provides a real List
     * that is not synchronized.
     *
     * @param vector a vector
     * @return a List
     * @deprecated the usage of the method convertVectorToList() is not needed and impacts performance
     */
    protected final List convertVectorToList(final Vector vector) {
        if (vector == null) {
            return null;
        }
        final List list = new ArrayList();
        list.addAll(vector);
        return Collections.unmodifiableList(list);
    }

    /**
     * Converts every occurrence of a <code>DateTime</code> object to a
     * <code>java.util.Calendar</code> instance.
     *
     * <p>The created DateTime objects must get recycled somewhen later, when
     * they are not needed anymore. To ensure this, the calling method must call
     * the method recycleDateTimeList() after usage.</p>
     * <p>Lists are processed recursivly; this is e.g. needed for column values
     * that might contain value lists in the list of column values.</p>
     *
     * @param list the list to convert its content
     * @return List a new List with the same elements in same order,
     *         but converted dates
     */
    protected final List convertNotesDateTimesToCalendar(final List list) {
        if (list == null) {
            return null;
        }
        final List result = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            final Object object = list.get(i);
            if (object instanceof DateTime) {
                final DateTime dateTime = (DateTime) object;
                checkSession(dateTime);
                result.add(createCalendar(dateTime));
            } else if (object instanceof List) {
                result.add(convertNotesDateTimesToCalendar((List) object));
            } else {
                result.add(object);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Converts every occurrence of a <code>java.util.Calendar</code> or
     * <code>java.util.Date</code> in a List to a <code>DateTime</code> instance.
     *
     * <p><b>ATTENTION:</b><br/>
     * The created DateTime objects must get recycled somewhen later, when
     * they are not needed anymore. To ensure this, the calling method must call
     * the method recycleDateTimeList() after usage.</p>
     *
     * <p><b>Example</b><br/>
     * <pre>
     * List keys;
     * // initialize and fill list with Date objects and others
     * List convertedList = convertDatesToNotesDateTime(keys);
     * // now the list contains Notes DateTime objects.
     * // do something with the list
     * recycleDateTimeList(convertedKeys);
     * </pre>
     * </p>
     *
     * @param list the list to convert its content
     * @return List a new List with the same elements in same order,
     *         but converted dates
     */
    protected final List convertCalendarsToNotesDateTime(final List list) {
        if (list == null) {
            return null;
        }
        final List result = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            final Object object = list.get(i);
            if (object instanceof Calendar) {
                final DateTime date = createDateTime((Calendar) object);
                result.add(date);
            } else {
                result.add(object);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Recycles all Notes DateTime objects in a list.
     *
     * <p>The list can have mixed content and must not contain only DateTime
     * objects. But only DateTime objects are recycled.</p>
     *
     * @param list any list implementation
     */
    protected final void recycleDateTimeList(final List list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            final Object object = list.get(i);
            if (object instanceof DateTime) {
                getFactory().recycle(object);
            }
        }
    }

    /**
     * Returns the Domingo session that created the current object.
     *
     * @return Domingo session of current object
     */
    protected final SessionProxy getDSession() {
        BaseProxy proxy = this;
        while (!(proxy instanceof SessionProxy)) {
            proxy = (BaseProxy) proxy.getParent();
        }
        return (SessionProxy) proxy;
    }

    /**
     * Creates a Notes <code>DateTime</code> instance from a
     * <code>java.util.Calendar</code>.
     *
     * Adapter to InternationalProxy.
     * This is a convenience method to allow every object to convert dates.
     *
     * @param calendar the calendar to convert
     * @return DateTime object representing the same point in time
     */
    protected final DateTime createDateTime(final Calendar calendar) {
        return getDSession().getInternational().createDateTime(calendar);
    }

    /**
     * Creates a Notes <code>DateRange</code> instance from two
     * <code>java.util.Calendar</code>s.
     *
     * Adapter to InternationalProxy.
     * This is a convenience method to allow every object to convert dates.
     *
     * @param calendar1 the start calendar to convert
     * @param calendar2 the end calendar to convert
     * @return DateTime object representing the same point in time
     * @see InternationalProxy#createDateRange(Calendar, Calendar)
     */
    protected final DateRange createDateRange(final Calendar calendar1, final Calendar calendar2) {
        return getDSession().getInternational().createDateRange(calendar1, calendar2);
    }

    /**
     * Converts a Notes DateTime object into a Calendar.
     * <p>Milli seconds are cleared in all cases.</p>
     *
     * Adapter to InternationalProxy.
     * This is a convenience method to allow every object to convert dates.
     *
     * @param dateTime a Notes DateTime object
     * @return a Calendar
     * @see InternationalProxy#createCalendar(DateTime)
     */
    protected final Calendar createCalendar(final DateTime dateTime) {
        return getDSession().getInternational().createCalendar(dateTime);
    }

    /**
     * Checks and monitors an error if a <code>DateTime</code> object doesn't
     * have a parent session.
     *
     * @param dateTime the DateTime object to check
     */
    protected final void checkSession(final DateTime dateTime) {
        try {
            if (dateTime.getParent() == null) {
                getMonitor().error("created a DateTime object without parent session");
            }
        } catch (NotesException e) {
            getMonitor().error("created a DateTime object with parent session not available", e);
        }
    }


    ////////////////////////////////////////////////
    //    interface java.lang.Object
    ////////////////////////////////////////////////

    /**
     * @see java.lang.Object#finalize()
     * @throws Throwable the <code>Exception</code> raised by this method
     */
    protected final void finalize() throws Throwable {
        factory.getBaseCache().remove(getNotesObject());
        getFactory().recycleLater(this);
        super.finalize();
    }

    /**
     * Returns the hashCode of the referenced Notes object.
     * @return hashCode of referenced Notes object
     *
     * @see java.lang.Object#hashCode()
     */
    public final int refereceHashCode() {
        if (ref != null) {
            return ref.hashCode();
        } else {
            return 0;
        }
    }

    ////////////////////////////////////////////////
    //    static helper methods
    ////////////////////////////////////////////////

    /**
     * Returns a string representation of the object. This method
     * returns a string that "textually represents" this object.
     * The result is a concise but informative representation that
     * is easy for a person to read.
     *
     * <p>The <code>toStringIntern</code> method
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * object.getClass().getName() + '@' + Integer.toHexString(object.hashCode())
     * </pre></blockquote>
     *
     * @param object the reference object to use.
     * @return a string representation of the object.
     * @see java.lang.Object#toString()
     */
    public static String toStringIntern(final Object object) {
        return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
    }

    /** Counter for number of currently existing DateTime objects. */
    private static int countDateTime = 0;

    /**
     * Returns the global counter for DateTime objects.
     *
     * @return global counter for DateTime objects.
     */
    public static int getCountDateTime() {
        return countDateTime;
    }

    /**
     * Increments the global counter for DateTime objects.
     *
     * @return new value of the counter after incrementation
     */
    public static int  incrementDateTimeCounter() {
        return ++countDateTime;
    }

    /**
     * Decrements the global counter for DateTime objects.
     *
     * @return new value of the counter after decrementation
     */
    public static int decrementDateTimeCounter() {
        return --countDateTime;
    }

    ////////////////////////////////////////////////
    //    inner classes
    ////////////////////////////////////////////////

    /**
     * Iterator for collections of documents.
     *
     * <p>A Mapper instance is used to map Notes document into a business
     * object. If no Mapper is provided, a simple default mapper is used that
     * maps the Notes document into a simple data structure that implements
     * the DDocument interface.</p>
     *
     * @see DDocument
     *
     * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
     */
    public static final class DocumentCollectionIterator extends BaseProxy implements DNotesIterator {

        /** serial version ID for serialization. */
        private static final long serialVersionUID = 3258412845845524528L;

        /** Reference to current document within the view. */
        private BaseDocumentProxy document = null;

        /**
         * Constructor.
         *
         * @param theFactory the controlling factory
         * @param theParent the parent database
         * @param monitor the monitor
         * @param theCollection the Notes document collection
         */
        protected DocumentCollectionIterator(final NotesProxyFactory theFactory,
                                             final DBase theParent,
                                             final DocumentCollection theCollection,
                                             final DNotesMonitor monitor) {
            super(theFactory, theParent, theCollection, monitor);
            initialize(theFactory);
        }

        /**
         * initialization.
         * @param theFactory the controlling factory
         */
        private void initialize(final NotesProxyFactory theFactory) {
            getFactory().preprocessMethod();
            try {
                if (getCollection() != null && getCollection().getCount() > 0) {
                    final Document notesDocument = getCollection().getFirstDocument();
                    document = BaseDocumentProxy.getInstance(theFactory, getParent(), notesDocument, getMonitor());
                }
            } catch (NotesException e) {
                getMonitor().warn("Cannot create Iterator: ", e);
                return;
            }
        }

        /**
         * Returns the associated document collection.
         *
         * @return the associated document collection
         */
        private DocumentCollection getCollection() {
            return (DocumentCollection) getNotesObject();
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return document != null;
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#next()
         */
        public Object next() {
            getFactory().preprocessMethod();
            final Object result = document;
            try {
                final Document nextNotesDoc = getCollection().getNextDocument((Document) document.getNotesObject());
                document = null;
                document = BaseDocumentProxy.getInstance(getFactory(), getParent(), nextNotesDoc, getMonitor());
            } catch (NotesException e) {
                document = null;
            }
            return result;
        }

        /**
         * The <tt>remove</tt> operation is not supported by this Iterator.
         * An <code>UnsupportedOperationException</code> is thrown if this
         * method is called.
         *
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         * @see BaseProxy#toString()
         */
        public String toString() {
            return BaseProxy.toStringIntern(this);
        }

        /**
         * {@inheritDoc}
         * @see DNotesIterator#getSize()
         */
        public int getSize() {
            try {
                return getCollection().getCount();
            } catch (NotesException e) {
                throw newRuntimeException("Cannot get size of iterator", e);
            }
        }
    }

    /**
     * Creates and returns a new exception wrapping a given
     * NotesException.
     * @param e the NotesException, can be null
     * @return new DNotesException
     */
    protected final DNotesException newException(final NotesException e) {
        return newException(null, e);
    }

    /**
     * Creates and returns a new exception with the given message.
     * @param message the message
     * @return new DNotesException
     */
    protected final DNotesException newException(final String message) {
        return newException(message, null);
    }

    /**
     * Creates and returns a new exception wrapping a given NotesException.
     * @param message the message
     * @param e the NotesException, can be null
     * @return new DNotesException
     */
    protected final DNotesException newException(final String message, final NotesException e) {
        final DominoException d = e != null ? new DominoException(e) : null;
        return new NotesProxyException(getFullMessage(message, d), d);
    }

    /**
     * Creates and returns a new runtime exception wrapping a given
     * NotesException.
     * @param e the NotesException, can be null
     * @return new DNotesRuntimeException
     */
    protected final DNotesRuntimeException newRuntimeException(final NotesException e) {
        return newRuntimeException(null, e);
    }

    /**
     * Creates and returns a new runtime exception with the given message.
     * @param message the message
     * @return new DNotesRuntimeException
     */
    protected final DNotesRuntimeException newRuntimeException(final String message) {
        return newRuntimeException(message, null);
    }

    /**
     * Creates and returns a new runtime exception with the given message.
     * @param message the message
     * @param e the NotesException, can be null
     * @return new DNotesRuntimeException
     */
    protected final DNotesRuntimeException newRuntimeException(final String message, final Exception e) {
        final Exception cause = e instanceof NotesException ? new DominoException((NotesException) e) : e;
        return new NotesProxyRuntimeException(getFullMessage(message, cause), cause);
    }

    /**
     * Returns a message for a domino exception.
     * @param message a message string
     * @param a @link{de.jakop.lotus.domingo.proxy.DominoException DominoException}
     * @return combined full message
     */
    private String getFullMessage(final String message, final Exception d) {
        final String msg = message != null ? message : (d != null ? d.getMessage() : "Unknown problem");
        final String fullMessage = this.getClass().getName() + ": " + msg;
        return fullMessage;
    }
}
