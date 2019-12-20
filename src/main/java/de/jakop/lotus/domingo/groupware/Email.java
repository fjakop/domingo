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

package de.jakop.lotus.domingo.groupware;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jakop.lotus.domingo.map.BaseInstance;

/**
 * Represents a Notes mail document.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class Email extends BaseInstance {

    /**
     * Represents all possible values for the importance.
     */
    public static final class Importance {

        /** Represents a low importance. */
        public static final Importance LOW = new Importance((byte) 3, "low");

        /** Represents a normal importance. */
        public static final Importance NORMAL = new Importance((byte) 2, "normal");

        /** Represents a high importance. */
        public static final Importance HIGH = new Importance((byte) 1, "high");

        private byte fValue;

        private transient final String fName;

        private Importance(final byte i, final String name) {
            fValue = i;
            fName = name;
        }

        /**
         * {@inheritDoc}
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return fName;
        }

        private Object readResolve() throws ObjectStreamException {
            if (fValue == 1) {
                return HIGH;
            }
            if (fValue == 2) {
                return NORMAL;
            }
            if (fValue == 3) {
                return LOW;
            }
            throw new InvalidObjectException("Attempt to resolve unknown importance: " + fValue);
        }
    }

    /**
     * Represents all possible values for the priority.
     */
    public static final class Priority {

        /** Represents a low priority. */
        public static final Priority LOW = new Priority((byte) 3, "low");

        /** Represents a normal priority. */
        public static final Priority NORMAL = new Priority((byte) 2, "normal");

        /** Represents a high priority. */
        public static final Priority HIGH = new Priority((byte) 1, "high");

        private byte fValue;

        private transient final String fName;

        private Priority(final byte i, final String name) {
            fValue = i;
            fName = name;
        }

        /**
         * {@inheritDoc}
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return fName;
        }

        private Object readResolve() throws ObjectStreamException {
            if (fValue == 1) {
                return HIGH;
            }
            if (fValue == 2) {
                return LOW;
            }
            if (fValue == 3) {
                return NORMAL;
            }
            throw new InvalidObjectException("Attempt to resolve unknown priority: " + fValue);
        }
    }

    /** Subject. */
    private String fSubject;

    /** Sender name. */
    private String fFrom;

    /** Reply-to name. */
    private String fReplyTo;

    /** Principal name. */
    private String fPrincipal;

    /** Required invitees (List of Strings). */
    private List fRecipients = new ArrayList();

    /** Optional invitees (List of Strings). */
    private List fCc = new ArrayList();

    /** Optional informed persons (List of Strings). */
    private List fBcc = new ArrayList();

    /** List of other header attributes. */
    private Map fHeaders = new HashMap();

    /** The body of the email as text. */
    private String fBody;

    /** List of category strings. */
    private Set fCategories = new HashSet();

    /** Priority of the email, defaults to {@link Priority#NORMAL}. */
    private Priority fPriority = Priority.NORMAL;

    /** Importance of the message, defaults to {@link Importance#NORMAL}. */
    private Importance fImportance = Importance.NORMAL;

    /** Date e-mail was delivered. */
    private Calendar fDeliveredDate;

    /** If the mail should be saved after send. */
    private boolean fSaveOnSend = true;

    /**
     * Constructor.
     */
    public Email() {
        super();
    }

    /**
     * Constructor.
     *
     * @param memo another memo for copying data from.
     */
    public Email(final Email memo) {
        super(memo);
    }

    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return fSubject;
    }

    /**
     * @param subject The subject to set
     */
    public void setSubject(final String subject) {
        this.fSubject = subject;
    }

    /**
     * Returns the list of blind copy recipients.
     *
     * @return list of blind copy recipients
     */
    public List getBcc() {
        return fBcc;
    }

    /**
     * Sets the list of blind copy recipients to a list of names. All previously
     * existing names are removed.
     *
     * @param bcc list of names
     */
    public void setBcc(final Collection bcc) {
        fBcc.clear();
        fBcc.addAll(bcc);
    }

    /**
     * Sets the list of blind copy recipients to a list of names. All previously
     * existing names are removed.
     *
     * @param bcc list of names
     */
    public void setBcc(final List bcc) {
        fBcc.clear();
        fBcc.addAll(bcc);
    }

    /**
     * Sets the list of blind copy recipients to a single name. All previously
     * existing names are removed.
     *
     * @param bcc The sendTo to set.
     */
    public void setBcc(final String bcc) {
        fBcc.clear();
        fBcc.add(bcc);
    }

    /**
     * Adds a name to the list of the blind copy recipients.
     *
     * @param bcc The sendTo to set.
     */
    public void addBcc(final String bcc) {
        fBcc.add(bcc);
    }

    /**
     * Adds list of names to the list of the blind copy recipients.
     *
     * @param bcc The sendTo to set.
     */
    public void addBcc(final List bcc) {
        fBcc.addAll(bcc);
    }

    /**
     * @return Returns the recipients.
     */
    public List getRecipients() {
        return fRecipients;
    }

    /**
     * @param recipients The recipients to set.
     */
    public void setRecipients(final Collection recipients) {
        fRecipients.clear();
        fRecipients.add(recipients);
    }

    /**
     * @param recipients The recipients to set.
     */
    public void setRecipients(final List recipients) {
        fRecipients.clear();
        fRecipients.add(recipients);
    }

    /**
     * Sets the list of recipients to a single name. All previously
     * existing names are removed.
     *
     * @param recipients The recipients to set.
     */
    public void setRecipient(final String recipients) {
        fRecipients.clear();
        fRecipients.add(recipients);
    }

    /**
     * @param recipient The recipient to set.
     */
    public void addRecipient(final String recipient) {
        fRecipients.add(recipient);
    }

    /**
     * @return Returns the sopyTo.
     */
    public List getCc() {
        return fCc;
    }

    /**
     * @param copyTo The copyTo to set.
     */
    public void setCc(final Collection copyTo) {
        fCc.clear();
        fCc.addAll(copyTo);
    }

    /**
     * @param copyTo The copyTo to set.
     */
    public void setCc(final List copyTo) {
        fCc.clear();
        fCc.addAll(copyTo);
    }

    /**
     * @param sendTo The sendTo to set.
     */
    public void addCc(final String sendTo) {
        fCc.add(sendTo);
    }

    /**
     * @param sendTo The sendTo to set.
     */
    public void addCc(final List sendTo) {
        fCc.addAll(sendTo);
    }

    /**
     * @return Returns the from.
     */
    public String getFrom() {
        return fFrom;
    }

    /**
     * @param from The from to set.
     */
    public void setFrom(final String from) {
        fFrom = from;
    }

    /**
     * @return Returns the principal.
     */
    public String getPrincipal() {
        return fPrincipal;
    }

    /**
     * @param principal The principal to set.
     */
    public void setPrincipal(final String principal) {
        fPrincipal = principal;
    }

    /**
     * @return Returns the replyTo.
     */
    public String getReplyTo() {
        return fReplyTo;
    }

    /**
     * @param replyTo The replyTo to set.
     */
    public void setReplyTo(final String replyTo) {
        fReplyTo = replyTo;
    }

    /**
     * Returns the body of the email as an unformatted string.
     *
     * @return unformatted body
     */
    public String getBody() {
        return fBody;
    }

    /**
     * Sets the body of the email as an unformatted string.
     *
     * @param body unformatted body
     */
    public void setBody(final String body) {
        fBody = body;
    }

    /**
     * Adds a new header attribute.
     *
     * @param name the name of the attribute
     * @param value the value of the attribute
     */
    public void addHeader(final String name, final String value) {
        fHeaders.put(name, value);
    }

    /**
     * Removes a header attribute.
     *
     * @param name the name of the attribute
     */
    public void removeHeader(final String name) {
        fHeaders.remove(name);
    }

    /**
     * Returns the set of all header names.
     *
     * @return set of all header names
     */
    public Set getAllHeaderNames() {
        return fHeaders.keySet();
    }

    /**
     * Returns the value of a given named header attribute.
     *
     * @param name the name of the attribute
     */
    public void getHeader(final String name) {
        fHeaders.get(name);
    }

    /**
     * Returns the map of all header attribute.
     *
     * @return map of header attributes
     */
    public Map getHeaders() {
        return fHeaders;
    }

    /**
     * Clears all other header information.
     */
    public void clearHeaders() {
        fHeaders.clear();
    }

    /**
     * Checks if a header attribute exists.
     *
     * @param name name of the attribute to check
     * @return <code>true</code> if the attribute exists, else
     *         <code>false</code>
     */
    public boolean containsHeader(final Object name) {
        return fHeaders.containsKey(name);
    }

    /**
     * Returns a set of the names of all available header attributes..
     *
     * @return set of header attribute names
     */
    public Set headerNames() {
        return fHeaders.keySet();
    }

    /**
     * Adds all given attributes to the set of headers.
     *
     * @param map of attributes to add
     */
    public void addHeaders(final Map map) {
        fHeaders.putAll(map);
    }

    /**
     * Adds a collection of categories.
     *
     * @param categories the categories to add
     */
    public void addCategories(final Collection categories) {
        fCategories.addAll(categories);
    }

    /**
     * Adds a single category.
     *
     * @param category the category to add
     */
    public void addCategories(final String category) {
        fCategories.add(category);
    }

    /**
     * Sets the set of categories.
     *
     * @param categories the categories to set
     */
    public void setCategories(final Set categories) {
        fCategories = categories;
    }

    /**
     * Sets the set of categories.
     *
     * @param categories the categories to set
     */
    public void setCategories(final Collection categories) {
        fCategories = new HashSet();
        fCategories.addAll(categories);
    }

    /**
     * Returns an iterator over all categories.
     *
     * @return iterator over all categories.
     */
    public Set getCategories() {
        return fCategories;
    }

    /**
     * Returns the importance.
     *
     * @return importance
     */
    public Importance getImportance() {
        return fImportance;
    }

    /**
     * Sets the importance.
     *
     * @param importance importance
     */
    public void setImportance(final Importance importance) {
        fImportance = importance;
    }

    /**
     * Returns the priority.
     *
     * @return priority
     */
    public Priority getPriority() {
        return fPriority;
    }

    /**
     * Sets the priority.
     *
     * @param priority priority
     */
    public void setPriority(final Priority priority) {
        fPriority = priority;
    }

    /**
     * Get the Delivery Date/Time.
     *
     * @return date message was delivered.
     */
    public Calendar getDeliveredDate() {
        return fDeliveredDate;
    }

    /**
     * Set the Delivery Date/Time.
     *
     * @param deliveredDate date email was delivered.
     */
    public void setDeliveredDate(final Calendar deliveredDate) {
        fDeliveredDate = deliveredDate;
    }

    /**
     * Returns if the mail should be saved after send or not.
     *
     * @return <code>true</code> if the mail should be saved after send, else <code>false</code>
     */
    public boolean getSaveOnSend() {
        return fSaveOnSend;
    }

    /**
     * Sets if the mail should be saved after send or not.
     * <p>The default value is <code>true</code>.</p>
     *
     * @param saveOnSend <code>true</code> if the mail should be saved after send, else <code>false</code>
     */
    public void setSaveOnSend(final boolean saveOnSend) {
        fSaveOnSend = saveOnSend;
    }
}
