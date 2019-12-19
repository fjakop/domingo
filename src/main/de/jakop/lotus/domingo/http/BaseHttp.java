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

package de.jakop.lotus.domingo.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import de.jakop.lotus.domingo.monitor.AbstractMonitorEnabled;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Abstract base class for all implementations of interfaces derived from
 * <code>DBase</code>.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class BaseHttp extends AbstractMonitorEnabled implements DBase {

    ////////////////////////////////////////////////
    // constants
    ////////////////////////////////////////////////

    private static final int VIEW_ENTRY_TIME_LENGTH = "ThhMMss".length();

    private static final int VIEW_ENTRY_DATE_LENGTH = "yyyymmdd".length();

    private static final String LINE_TERM = System.getProperty("line.separator");

    /** Number of characters needed to represent a date/time value. */
    protected static final int DATETIME_STRING_LENGTH = 20;

    /** Maximum number of items supported in method getItemValues(). */
    protected static final int NUM_DATETIME_VALUES = 1000;

    /** maximum number of characters to parse in method getItemValues(). */
    protected static final int MAX_DATETIME_LENGTH = NUM_DATETIME_VALUES * DATETIME_STRING_LENGTH;

    ////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////

    /** Reference to the parent object. */
    private final DBase fParent;

    /** Reference to the factory which controls this instance. */
    private final NotesHttpFactory fFactory;

    private static final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss,SS");
    // TODO add time zone offset in hours (e.g. "+01")

    ////////////////////////////////////////////////
    // creation
    ////////////////////////////////////////////////

    /**
     * Creates a new DBaseImpl object.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param monitor the monitor
     */
    protected BaseHttp(final NotesHttpFactory theFactory, final DBase theParent,
                        final DNotesMonitor monitor) {
        super(monitor);
        this.fFactory = theFactory;
        this.fParent = theParent;
    }

    ////////////////////////////////////////////////
    // protected utility methods for sub classes
    ////////////////////////////////////////////////

    /**
     * Returns the parent object.
     *
     * @return the parent object or <code>null</code> if no parent available
     */
    protected final BaseHttp getParent() {
        return (BaseHttp) fParent;
    }

    /**
     * Returns the parent object.
     *
     * @return the parent object or <code>null</code> if no parent available
     */
    /**
     * Returns the Domingo session that created the current object.
     *
     * @return Domingo session of current object
     */
    protected final SessionHttp getDSession() {
        BaseHttp base = this;
        while (!(base instanceof SessionHttp)) {
            base = (BaseHttp) base.getParent();
        }
        return (SessionHttp) base;
    }

    /**
     * Returns the factory corresponding to this instance.
     *
     * @return the corresponding factory
     */
    protected final NotesHttpFactory getFactory() {
        return fFactory;
    }

    /**
     * Executes a HTTP get request and returns the response body.
     *
     * @param pathInfo path of database and optional e.g. a view name
     * @param query the query string
     * @return response body
     * @throws IOException if the request cannot be executed
     */
    protected final String execute(final String pathInfo, final String query) throws IOException {
        return executeUrl(pathInfo + "?" + query);
    }

    /**
     * Executes a HTTP get request and returns the response body.
     *
     * @param query the query string
     * @return response body
     * @throws IOException if the request cannot be executed
     *
     * @deprecated everything should be rewritten without the domingo database
     */
    protected final String execute(final String query) throws IOException {
        return executeUrl(getDomingoDatabase() + "/Domingo?OpenAgent&" + query);
    }

    /**
     * Returns information from the domingo database on the server.
     *
     * @param infoname name of the information resource
     * @return content of the information resource
     * @throws IOException if the request cannot be executed
     *
     * @deprecated everything should be rewritten without the domingo database
     */
    protected final String executeDomingoDatabaseUrl(final String infoname) throws IOException {
        return executeUrl(getDomingoDatabase() + "/" + infoname);
    }

    /**
     * Executes a given URL and returns the answer from the server.
     *
     * @param pathInfo the path_info to execute
     * @return array of bytes with result from server
     * @throws IOException if the URL cannot be executed
     *
     * TODO move this to the session
     */
    protected final String executeUrl(final String pathInfo) throws IOException {
        final DominoHttpMethod method = getDSession().createGetMethod(pathInfo);
        try {
            final int statusCode = getDSession().executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                getMonitor().error("Http request failed: " + method.getStatusText());
                throw new IOException("Error " + method.getStatusCode() + ": " + method.getStatusText() + ": " + pathInfo);
            }
            final byte[] responseBody = method.getResponseBody();
            Header contentType = method.getRequestHeader("Content-Type");
            String value = contentType.getValue();
            String charset = value.substring(value.lastIndexOf('='));
            if (!Charset.availableCharsets().containsKey(charset)) {
                throw new IOException("unsupported charset: " + charset);
            }
            ByteArrayInputStream is = new ByteArrayInputStream(responseBody);
            InputStreamReader isReader = new InputStreamReader(is, charset);
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer response = new StringBuffer(responseBody.length);
            String line = null;
            do {
                line = reader.readLine();
                if (line != null) {
                    response.append(line);
                    response.append(LINE_TERM);
                }
            } while (line != null);
            return response.toString();
        } catch (IOException e) {
            getMonitor().error(e.getLocalizedMessage(), e);
            throw e;
        } finally {
            method.releaseConnection();
        }
    }

    /**
     * Executes a HTTP get request and returns the response body.
     *
     * @param query the query string
     * @param document the document
     * @return response body
     * @throws IOException if the request cannot be executed
     *
     * @deprecated everything should be rewritten without the domingo database
     */
    protected final byte[] postDXL(final String query, final DocumentHttp document)
            throws IOException {
        final String pathInfo = "/" + getDomingoDatabase() + "/Domingo?OpenAgent&" + query;
        final DominoPostMethod method = getDSession().createPostMethod(pathInfo);
        final Iterator iterator = document.getItems();
        final StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version='1.0'?>");
        buffer.append("<!DOCTYPE document>");
        buffer.append("<document xmlns='http://www.lotus.com/dxl' version='7.0' form='" + document.getItemValue("Form") + "'>");
        buffer.append("<noteinfo noteid='" + document.getNoteID() + "' unid='" + document.getUniversalID() + "' sequence='2'>");
        buffer.append("<created><datetime>" + formatDateTime(document.getCreated()) + "</datetime></created>");
        buffer.append("<modified><datetime>" + formatDateTime(document.getLastModified()) + "</datetime></modified>");
        // TODO check this:
        buffer.append("<revised><datetime>" + formatDateTime(document.getLastModified()) + "</datetime></revised>");
        // TODO check this:
        buffer.append("<lastaccessed><datetime>" + formatDateTime(document.getLastAccessed()) + "</datetime></lastaccessed>");
        // TODO check this:
        buffer.append("<addedtofile><datetime>" + formatDateTime(document.getLastModified()) + "</datetime></addedtofile>");
        buffer.append("</noteinfo>");
        while (iterator.hasNext()) {
            final ItemHttp item = (ItemHttp) iterator.next();
            if (!ignoreItem(item.getName())) {
                buffer.append("<item name='" + item.getName() + "' names='" + item.isNames() + "' readers='" + item.isReaders()
                        + "' authors='" + item.isAuthors() + "' protected='" + item.isProtected() + "'>");
                appendValuesListStartTag(buffer, item);
                Iterator iter = item.getValues().iterator();
                while (iter.hasNext()) {
                    Object object = iter.next();
                    appendValue(buffer, object);
                }
                appendValuesListEndTag(buffer, item);
                buffer.append("</item>");
            }
        }
        buffer.append("</document>");
        method.setRequestBody(buffer.toString());
        try {
            final int statusCode = getDSession().executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                getMonitor().error("Http request failed: " + method.getStatusLine());
            }
            return method.getResponseBody();
        } catch (IOException e) {
            getMonitor().error(e.getLocalizedMessage(), e);
            throw e;
        } finally {
            method.releaseConnection();
        }
    }

    private String getDomingoDatabase() {
        return getFactory().getDomingoDatabase();
    }

    private void appendValuesListStartTag(final StringBuffer buffer, final ItemHttp item) {
        if (item.getValues() == null) {
            return;
        } else if (item.getValues().size() <= 1) {
            return;
        } else if (item.getValues().get(0) instanceof String) {
            buffer.append("<textlist>");
        } else if (item.getValues().get(0) instanceof Number) {
            buffer.append("<numberlist>");
        } else if (item.getValues().get(0) instanceof Calendar) {
            buffer.append("<datedtimelist>");
        }
    }

    private void appendValuesListEndTag(final StringBuffer buffer, final ItemHttp item) {
        if (item.getValues() == null) {
            return;
        } else if (item.getValues().size() <= 1) {
            return;
        } else if (item.getValues().get(0) instanceof String) {
            buffer.append("</textlist>");
        } else if (item.getValues().get(0) instanceof Number) {
            buffer.append("</numberlist>");
        } else if (item.getValues().get(0) instanceof Calendar) {
            buffer.append("</datedtimelist>");
        }
    }

    private void appendValue(final StringBuffer buffer, final Object object) {
        if (object == null) {
            buffer.append("<text/>");
        } else if ("".equals(object)) {
            buffer.append("<text/>");
        } else if (object instanceof String) {
            buffer.append("<text>" + formatString((String) object) + "</text>");
        } else if (object instanceof Number) {
            buffer.append("<number>" + formatNumber((Number) object) + "</number>");
        } else if (object instanceof Calendar) {
            buffer.append("<datetime>" + formatDateTime((Calendar) object) + "</datetime>");
        }
    }

    private static String formatString(final String value) {
        return value.replaceAll("\n", "<break/>");
    }

    private String formatNumber(final Number value) {
        return value.toString();
    }

    private String formatDateTime(final Calendar value) {
        if (value == null) {
            return "";
        }
        return XML_DATE_FORMAT.format(value.getTime());
    }

    private static final String[] IGNORE_ITEMS = {"HTTP_User_Agent", "HTTP_HOST", "HTTP_CONTENT_LENGTH",
            "HTTP_CONTENT_TYPE", "HTTP_AUTHORIZATION", "HTTPS", "CONTENT_LENGTH", "CONTENT_TYPE", "PATH_INFO",
            "CGI_PATH_INFO", "PATH_TRANSLATED", "QUERY_STRING", "Query_String_Decoded", "REMOTE_HOST", "REMOTE_ADDR",
            "REMOTE_IDENT", "REQUEST_METHOD", "SERVER_NAME", "SERVER_PORT", "SERVER_PROTOCOL", "SERVER_SOFTWARE",
            "SERVER_ADDR", "AUTH_TYPE", "REMOTE_USER", "GATEWAY_INTERFACE", "SCRIPT_NAME", "PATH_INFO_DECODED",
            "REQUEST_CONTENT" };

    private boolean ignoreItem(final String name) {
        for (int i = 0; i < IGNORE_ITEMS.length; i++) {
            if (IGNORE_ITEMS[i].equals(name)) {
                return true;
            }
        }
        return false;
    }

    // //////////////////////////////////////////////
    // interface java.lang.Object
    // //////////////////////////////////////////////

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public abstract String toString();

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

    ////////////////////////////////////////////////
    //    inner classes
    ////////////////////////////////////////////////

    /**
     * Base SAX parser for DXL.
     * Handles all kinds of item values.
     */
    class BaseHandler extends DefaultHandler {

        private List fValues = new ArrayList();

        private StringBuffer fValueBuffer = new StringBuffer();

        /**
         * Resets the parser to start a new parsing.
         */
        protected void reset() {
            fValues = new ArrayList();
            fValueBuffer = new StringBuffer();
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
         *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(final String namespaceURI, final String localName, final String qName,
                final Attributes atts) throws SAXException {
            if ("textlist".equals(qName)) {
                return;
            } else if ("numberlist".equals(qName)) {
                return;
            } else if ("datetimelist".equals(qName)) {
                return;
            } else if ("text".equals(qName)) {
                fValueBuffer = new StringBuffer();
            } else if ("number".equals(qName)) {
                fValueBuffer = new StringBuffer();
            } else if ("datetime".equals(qName)) {
                fValueBuffer = new StringBuffer();
            } else if ("break".equals(qName)) {
                fValueBuffer.append("\n");
//            } else if ("datetimepair".equals(qName)) {
//                // n/a
            } else {
                super.startElement(namespaceURI, localName, qName, atts);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
         */
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            if ("textlist".equals(qName)) {
                return;
            } else if ("numberlist".equals(qName)) {
                return;
            } else if ("datetimelist".equals(qName)) {
                return;
            } else if ("text".equals(qName)) {
                fValues.add(fValueBuffer.toString());
            } else if ("number".equals(qName)) {
                fValues.add(new Double(fValueBuffer.toString()));
            } else if ("datetime".equals(qName)) {
                fValues.add(parseViewEntryDateTime(fValueBuffer.toString())); // TODO parse date
//            } else if ("datetimepair".equals(localName)) {
//                // n/a
            } else {
                super.endElement(uri, localName, qName);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#characters(char[], int, int)
         */
        public void characters(final char[] ch, final int start, final int length) throws SAXException {
            fValueBuffer.append(ch, start, length);
        }

        /**
         * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
         */
        public void warning(final SAXParseException e) throws SAXException {
            getMonitor().warn("Parser warning", e);
        }

        /**
         * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
         */
        public void error(final SAXParseException e) throws SAXException {
            getMonitor().error("Parser error", e);
        }

        /**
         * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
         */
        public void fatalError(final SAXParseException e) throws SAXException {
            getMonitor().fatalError("Fatal parser error", e);
        }

        /**
         * Returns the parsed values list.
         *
         * @return list of parsed values
         */
        public List getValues() {
            return fValues;
        }
    }

    /**
     * Parses a date in the format as used in view entries.
     *
     * <p>Format: <tt>yyyyMMddThhmmss,nn+zz</tt></p>
     *
     * <p>Example: <tt>20070119T155258,93+01</tt></p>
     *
     * @param date a date/time value from a view entry
     * @return parsed calendar
     */
    public static Calendar parseViewEntryDateTime(final String date) {
        if (date == null || "".equals(date)) {
            return null;
        }
        final int year = Integer.parseInt(date.substring(0, 4));
        final int month = Integer.parseInt(date.substring(4, 6));
        final int day = Integer.parseInt(date.substring(6, 8));
        final int hour;
        final int minute;
        final int second;
        if (date.length() > VIEW_ENTRY_DATE_LENGTH + 1) {
            // TODO improve parsing, avoid magic naumbers
            hour = Integer.parseInt(date.substring(VIEW_ENTRY_DATE_LENGTH + 1, VIEW_ENTRY_DATE_LENGTH + 3));
            minute = Integer.parseInt(date.substring(VIEW_ENTRY_DATE_LENGTH + 2 + 1, VIEW_ENTRY_DATE_LENGTH + 2 + 2 + 1));
            second = Integer.parseInt(date.substring(VIEW_ENTRY_DATE_LENGTH + 2 + 3, VIEW_ENTRY_DATE_LENGTH + 2 + 2 + 3));
        } else {
            hour = 0;
            minute = 0;
            second = 0;
        }
        int millis = 0;
        if (date.length() > VIEW_ENTRY_DATE_LENGTH + VIEW_ENTRY_TIME_LENGTH) {
            millis = Integer.parseInt(date.substring(VIEW_ENTRY_DATE_LENGTH + VIEW_ENTRY_TIME_LENGTH, VIEW_ENTRY_DATE_LENGTH
                    + VIEW_ENTRY_TIME_LENGTH + 2));
        }
        final Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millis);
        return calendar;

    }
}
