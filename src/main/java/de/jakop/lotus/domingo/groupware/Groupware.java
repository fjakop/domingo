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

package de.jakop.lotus.domingo.groupware;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DNotesRuntimeException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.groupware.map.MailDatabase;
import de.jakop.lotus.domingo.groupware.map.NamesDatabase;
import de.jakop.lotus.domingo.map.NotesLocation;

/**
 * Main entry point to the groupware functionality of domingo.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class Groupware {

    /** Reference to the associated mail database. */
    private MailDatabase fMailDatabase;

    /** references to the associated address books */
    private List addressBooks;

    private DNotesFactory fFactory;

    private DSession fSession;

    private final NotesLocation fLocation;

    /**
     * Constructor.
     *
     * The location is only used to create a domingo session; the mail-server
     * and the path to the mail database is examined from the current user.
     *
     * <p>If the location is a IIOP location and the mail-server of the current
     * user is not the server as specified in the location the connection will
     * fail.</p>
     *
     * @param locationUri location URI of notes database
     * @throws GroupwareException if the groupware cannot be initialized
     */
    public Groupware(final String locationUri) throws GroupwareException {
        this(new NotesLocation(locationUri), null);
    }

    /**
     * Constructor.
     *
     * The location is only used to create a domingo session; the mail-server
     * and the path to the mail database is examined from the current user.
     *
     * <p>If the location is a IIOP location and the mail-server of the current
     * user is not the server as specified in the location the connection will
     * fail.</p>
     *
     * @param locationUri location URI of notes database
     * @param monitor a monitor
     * @throws GroupwareException if the groupware cannot be initialized
     */
    public Groupware(final String locationUri, final DNotesMonitor monitor) throws GroupwareException {
        this(new NotesLocation(locationUri), monitor);
    }

    /**
     * Constructor.
     *
     * The location is only used to create a domingo session; the mail-server
     * and the path to the mail database is examined from the current user.
     *
     * <p>If the location is a IIOP location and the mail-server of the current
     * user is not the server as specified in the location the connection will
     * fail.</p>
     *
     * @param location location of notes database
     * @throws GroupwareException if the groupware cannot be initialized
     */
    public Groupware(final NotesLocation location) throws GroupwareException {
        this(location, null);
    }

    /**
     * Constructor.
     *
     * The location is only used to create a domingo session; the mail-server
     * and the path to the mail database is examined from the current user.
     *
     * <p>If the location is a IIOP location and the mail-server of the current
     * user is not the server as specified in the location the connection will
     * fail.</p>
     *
     * @param location location of notes database
     * @param monitor a monitor
     * @throws GroupwareException if the groupware cannot be initialized
     */
    public Groupware(final NotesLocation location, final DNotesMonitor monitor) throws GroupwareException {
        fLocation = location;
        try {
            fSession = getSession(location, monitor);
        } catch (DNotesException e) {
            throw new GroupwareException("Cannot initialize Groupware", e);
        }
    }

    /**
     * Returns an implementation of the mailbox interface for the current user.
     *
     * @return mailbox interface
     * @throws GroupwareException if the mailbox cannot be opened
     */
    public Mailbox getMailbox() throws GroupwareException {
        if (fMailDatabase == null) {
            String username = fSession.getCommonUserName();
            String server = fSession.getMailServer();
            String path = fSession.getMailDatabaseName();
            NotesLocation mailLocation = NotesLocation.getInstance(fLocation, server, path);
            try {
                fMailDatabase = new MailDatabase(fSession, mailLocation, username);
            } catch (DNotesException e) {
                throw new GroupwareException("Cannot open mail database " + server + "!!" + path, e);
            }
        }
        return fMailDatabase;
    }

    /**
     * Returns an implementation of the mailbox interface for a given user.
     *
     * @param username username
     * @return mailbox interface
     * @throws GroupwareException if the mailbox cannot be opened
     */
    public Mailbox getMailbox(final String username) throws GroupwareException {
        if (fMailDatabase == null) {
            try {
                DDatabase database = fSession.getMailDatabase(username);
                fMailDatabase = new MailDatabase(database, username);
            } catch (DNotesException e) {
                throw new GroupwareException("Cannot open mail database for user " + username, e);
            }
        }
        return fMailDatabase;
    }

    /**
     * Returns an implementation of the calendar interface.
     *
     * @return calendar interface
     * @throws GroupwareException if the calendar database cannot be opened.
     */
    public CalendarInterface getCalendar() throws GroupwareException {
        return (CalendarInterface) getMailbox();
    }

    /**
     * Returns an iterator over all available public and private addressbooks.
     * @return Iterator
     * @see AddressBook
     */
    public Iterator getAddressBooks() {
        if (addressBooks == null) {
            addressBooks = fSession.getAddressBooks();
        }
        return new AddressBookIterator(addressBooks.iterator());
    }

    /**
     * Closes the groupware instance, including all internal resources of the
     * Notes connection.
     *
     * @throws DNotesRuntimeException if an error occurs during disposal or
     *              if not all objects can be disposed
     */
    public void close() throws DNotesRuntimeException {
        if (fLocation.isHttp() || fLocation.isIIOP()) {
            fFactory.disposeInstance(true);
        }
    }

    /**
     * Iterates thru a collection of address books
     */
    private static class AddressBookIterator implements Iterator {

       private final Iterator fAllEntries;

       /**
        * Constructor.
        *
        * @param allEntries collection of view-entries
        */
       public AddressBookIterator(final Iterator allEntries) {
           fAllEntries = allEntries;
       }

       /**
        * @see java.util.Iterator#hasNext()
        */
       public boolean hasNext() {
           return fAllEntries.hasNext();
       }

       /**
        * Returns instances of class {@link NamesDatabase}.
        * @see java.util.Iterator#next()
        */
       public Object next() throws NoSuchElementException {
            DDatabase entry = (DDatabase) fAllEntries.next();
            NotesLocation loc = new NotesLocation(entry.getServer(), entry.getFilePath());
            try {
                return new NamesDatabase(entry.getSession(), loc);
            } catch (DNotesException e) {
                throw new NoSuchElementException("Error getting addresss book " + entry.getServer() + " " + entry.getFilePath());
            }
        }

       /**
        * @see java.util.Iterator#remove()
        */
       public void remove() {
           throw new UnsupportedOperationException();
       }
   }

    /**
     * Creates and returns a new domingo session for a given location.
     *
     * @param location location of database.
     * @return domingo session
     * @throws DNotesException if the uri is invalid or the database cannot be
     *             opened
     */
    private DSession getSession(final NotesLocation location, final DNotesMonitor monitor) throws DNotesException {
        if (location.isLocal()) {
            fFactory = DNotesFactory.getInstance(monitor);
            return fFactory.getSession();
        }
        final String host = location.getHost();
        final String user = location.getUsername();
        final String passwd = location.getPassword();
        if (location.isHttp()) {
            final DNotesFactory factory = DNotesFactory.newInstance("de.bea.domingo.http.NotesHttpFactory", monitor);
            return factory.getSession(host, user, passwd);
        } else if (location.isIIOP()) {
            final DNotesFactory factory = DNotesFactory.newInstance(monitor);
            return factory.getSession(host, user, passwd);
        }
        throw new DNotesException("Invalid notes uri: " + location);
    }
}
