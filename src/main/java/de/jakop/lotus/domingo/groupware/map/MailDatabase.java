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

package de.jakop.lotus.domingo.groupware.map;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.DViewEntry;
import de.jakop.lotus.domingo.groupware.CalendarEntry;
import de.jakop.lotus.domingo.groupware.CalendarEntryDigest;
import de.jakop.lotus.domingo.groupware.CalendarInterface;
import de.jakop.lotus.domingo.groupware.Email;
import de.jakop.lotus.domingo.groupware.EmailDigest;
import de.jakop.lotus.domingo.groupware.GroupwareRuntimeException;
import de.jakop.lotus.domingo.groupware.Mailbox;
import de.jakop.lotus.domingo.map.BaseDatabase;
import de.jakop.lotus.domingo.map.MapperRegistrationException;
import de.jakop.lotus.domingo.map.MappingException;
import de.jakop.lotus.domingo.map.NotesLocation;

/**
 * Interface to mail databases.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class MailDatabase extends BaseDatabase implements Mailbox, CalendarInterface {

    private static final String CALENDAR_VIEW = "($Calendar)";

    /** Owner name. */
    private final String fOwner;

    /**
     * Constructor.
     *
     * @param locationUri URI of location of database.
     * @param owner name of mailbox owner
     * @throws IOException if the location is not a valid notes location
     * @throws DNotesException if the database cannot be opened
     */
    public MailDatabase(final String locationUri, final String owner) throws IOException, DNotesException {
        super(locationUri);
        fOwner = owner;
    }

    /**
     * Constructor.
     *
     * @param session an existing domingo session
     * @param location location of database.
     * @param owner name of mailbox owner
     * @throws DNotesException if the uri is invalid or the database cannot be
     *             opened
     */
    public MailDatabase(final DSession session, final NotesLocation location, final String owner) throws DNotesException {
        super(session, location);
        fOwner = owner;
    }

    /**
     * Constructor.
     *
     * @param database a mail database
     * @param owner name of mailbox owner
     * @throws DNotesException if the database is invalid or cannot be opened
     */
    public MailDatabase(final DDatabase database, final String owner) throws DNotesException {
        super(database);
        fOwner = owner;
    }

    /**
     * Registers all mappers needed for a mail database.
     *
     * @throws MapperRegistrationException if an error occurred during registering a mapper
     * @see BaseDatabase#registerMappers()
     */
    protected void registerMappers() throws MapperRegistrationException {
        register(EmailMapper.class);
        register(CalendarEntryMapper.class);
    }

    /**
     * {@inheritDoc}
     * @see Mailbox#getOwner()
     */
    public String getOwner() {
        // TODO read owner from calendar profile
        return fOwner;
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#reply(Email, boolean, boolean)
     */
    public Email reply(final Email memo, final boolean withHistory, final boolean withAttachments) {
        Email reply = new Email(memo);
        reply.setSubject("Re: " + memo.getSubject());
        String sender = getDatabase().getSession().getCommonUserName();
        reply.setFrom(sender);
        reply.setPrincipal(sender);
        reply.setReplyTo(sender);
        reply.setRecipient(memo.getFrom());
        reply.setBcc("");
        // TODO handle withHistory
        // TODO handle withAttachments
        return reply;
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#replyToAll(Email, boolean, boolean)
     */
    public Email replyToAll(final Email memo, final boolean withHistory, final boolean withAttachments) {
        Email reply = new Email(memo);
        reply.setSubject("Re: " + memo.getSubject());
        String sender = getDatabase().getSession().getCommonUserName();
        reply.setFrom(sender);
        reply.setPrincipal(sender);
        reply.setReplyTo(sender);
        reply.setRecipient(memo.getFrom());
        reply.setCc(memo.getCc());
        reply.setBcc(memo.getBcc());
        // todo handle parameter withHistory
        // todo handle parameter withAttachments
        return reply;
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#remove(Email)
     */
    public void remove(final Email memo) {
        final String unid = memo.getUnid();
        final DDocument document = getDatabase().getDocumentByUNID(unid);
        document.remove(true);
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#remove(EmailDigest)
     */
    public void remove(final EmailDigest memo) {
        final String unid = memo.getUnid();
        final DDocument document = getDatabase().getDocumentByUNID(unid);
        document.remove(true);
    }

//    /**
//     * {@inheritDoc}
//     *
//     * @see de.bea.domingo.groupware.MailboxInterface#getNewMails(java.util.Calendar)
//     */
//    public List getNewMails(final Calendar since) {
//        return null; // TODO
//    }

    /**
     * {@inheritDoc}
     *
     * @see CalendarInterface#getObjects(java.util.Calendar,
     *      java.util.Calendar)
     */
    public List getObjects(final Calendar from, final Calendar to) {

        SortedSet calendarEntrySet = new TreeSet(new CalendarEntryComparator());
        DView view = getDatabase().getView(CALENDAR_VIEW);
        Iterator it = view.getAllEntriesByKey(from, to, true);
        while (it.hasNext()) {
            DViewEntry entry = (DViewEntry) it.next();
            CalendarEntryDigest entryDigest = new CalendarEntryDigest();

            try {
                map(entry, entryDigest);
            } catch (MappingException e) {
                throw new GroupwareRuntimeException("Cannot get next calendar entry", e);
            }

            // This check filters out TO-DO items appearing in the calendar view
            // (they appear unless the user has checked "do not display to-do
            // entries in the calendar") in the Calendar Preferences.
            if (null != entryDigest.getStartDateTime()) {
                calendarEntrySet.add(entryDigest);
            }
        }
        return new ArrayList(calendarEntrySet);
    }

    /**
     * {@inheritDoc}
     *
     * @see CalendarInterface#getCalendar()
     */
    public Iterator getCalendar() {
        return getCalendar(false);
    }

    /**
     * {@inheritDoc}
     *
     * @see CalendarInterface#getCalendar(boolean
     *      reverseOrder)
     */
    public Iterator getCalendar(final boolean reverseOrder) {
        DView view = getDatabase().getView(CALENDAR_VIEW);
        Iterator allEntries;
        if (reverseOrder) {
            allEntries = view.getAllEntriesReverse();
        } else {
            allEntries = view.getAllEntries();
        }
        return new CalendarIterator(allEntries);
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#getInbox()
     */
    public Iterator getInbox() {
        return getInbox(false);
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#getInbox(boolean)
     */
    public Iterator getInbox(final boolean reverseOrder) {
        DView view = getDatabase().getView("($Inbox)");
        Iterator allEntries;
        if (reverseOrder) {
            allEntries = view.getAllEntriesReverse();
        } else {
            allEntries = view.getAllEntries();
        }
        return new MailIterator(allEntries);
    }

    /**
     * Compares calendar entries by start date.
     */
    private static final class CalendarEntryComparator implements Comparator, Serializable {

        private static final long serialVersionUID = 8982105227393259520L;

        public int compare(final Object o1, final Object o2) {
            CalendarEntryDigest ced1 = (CalendarEntryDigest) o1;
            CalendarEntryDigest ced2 = (CalendarEntryDigest) o2;
            if (null == ced1 && null == ced2) { return 0; }
            if (null == ced1) { return -1; }
            if (null == ced2) { return 1; }
            if (null == ced1.getStartDateTime()) { return -1; }
            if (null == ced2.getStartDateTime()) { return 1; }
            return (ced1.getStartDateTime().getTime().compareTo(ced2.getStartDateTime().getTime()));
        }
    }

    /**
     * Iterates thru a collection of view entries and returns for each
     * view-entry the corresponding email digest.
     */
    private class MailIterator implements Iterator {

        private final Iterator allEntries;

        /**
         * Constructor.
         *
         * @param allEntries collection of view-entries
         */
        public MailIterator(final Iterator allEntries) {
            this.allEntries = allEntries;
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return allEntries.hasNext();
        }

        /**
         * @see java.util.Iterator#next()
         */
        public Object next() {
            DViewEntry entry = (DViewEntry) allEntries.next();
            EmailDigest memoDigest = new EmailDigest();
            try {
                map(entry, memoDigest);
                entry.getDocument().recycle();
            } catch (MappingException e) {
                throw new GroupwareRuntimeException("Cannot get next email", e);
            }
            return memoDigest;
        }

        /**
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * Iterates thru a collection of view entries and returns for each
     * view-entry the corresponding calendar digest.
     */
    private class CalendarIterator implements Iterator {

        private final Iterator allEntries;

        /**
         * Constructor.
         *
         * @param allEntries
         */
        public CalendarIterator(final Iterator allEntries) {
            this.allEntries = allEntries;
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return this.allEntries.hasNext();
        }

        /**
         * @see java.util.Iterator#next()
         */
        public Object next() {
            DViewEntry entry = (DViewEntry) allEntries.next();
            CalendarEntryDigest entryDigest = new CalendarEntryDigest();
            try {
                map(entry, entryDigest);
                entry.getDocument().recycle();
            } catch (MappingException e) {
                throw new GroupwareRuntimeException("Cannot get next calendar entry", e);
            }
            return entryDigest;
        }

        /**
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the email represented by the given email-digest.
     *
     * @param emailDigest an email-digest
     * @return the corresponding email
     */
    public Email getEmail(final EmailDigest emailDigest) {
        DDocument memoDocument = getDatabase().getDocumentByUNID(emailDigest.getUnid());
        Email email = new Email();
        try {
            map(memoDocument, email);
            memoDocument.recycle();
        } catch (MappingException e) {
            throw new GroupwareRuntimeException("Cannot get email", e);
        }
        return email;
    }

    /**
     * Returns the full calendar entry for a given calendar entry digest.
     *
     * @param entryDigest a calendar digest
     * @return the full calendar entry
     */
    public Object getCalendarEntry(final CalendarEntryDigest entryDigest) {
        return this.getCalendarEntry(entryDigest.getUnid());
    }

    /**
     * Returns a calendar entry for a given unid.
     *
     * @see CalendarEntry#getAppointmentUnid()
     *
     * @param unid the Notes document unique id of the calendar entry.
     * @return the calendar entry
     */
    public Object getCalendarEntry(final String unid) {
        DDocument calDocument = getDatabase().getDocumentByUNID(unid);
        CalendarEntry entry = new CalendarEntry();
        try {
            map(calDocument, entry);
            calDocument.recycle();
        } catch (MappingException e) {
            throw new GroupwareRuntimeException("Cannot get calendar entry", e);
        }
        return entry;
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#newEmail()
     */
    public Email newEmail() {
        return new Email();
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#forward(Email)
     */
    public Email forward(final Email memo) {
        return forward(memo, true);
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#forward(Email,
     *      boolean)
     */
    public Email forward(final Email memo, final boolean withAttachments) {
        // TODO forward a memo
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#saveAsDraft(Email)
     */
    public void saveAsDraft(final Email memo) {
        // TODO save a new memo as draft
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#send(Email)
     */
    public void send(final Email memo) {
        DDocument document = getDatabase().createDocument();
        try {
            map(memo, document);
        } catch (MappingException e) {
            throw new GroupwareRuntimeException("Cannot send email", e);
        }
        document.send("");
    }

    /**
     * {@inheritDoc}
     *
     * @see CalendarInterface#save(CalendarEntry)
     */
    public void save(final CalendarEntry entry) {
        DDocument document = getDatabase().createDocument();
        try {
            map(entry, document);
        } catch (MappingException e) {
            throw new GroupwareRuntimeException("Cannot create calendar entry", e);
        }
        document.save();
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#remove(Email)
     */
    public void remove(final CalendarEntry entry) {
        final String unid = entry.getUnid();
        final DDocument document = getDatabase().getDocumentByUNID(unid);
        document.remove(true);
    }

    /**
     * {@inheritDoc}
     *
     * @see Mailbox#remove(EmailDigest)
     */
    public void remove(final CalendarEntryDigest entryDigest) {
        final String unid = entryDigest.getUnid();
        final DDocument document = getDatabase().getDocumentByUNID(unid);
        document.remove(true);
    }
}
