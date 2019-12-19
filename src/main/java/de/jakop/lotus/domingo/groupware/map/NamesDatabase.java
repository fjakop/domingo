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
import java.util.Iterator;

import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.DViewEntry;
import de.jakop.lotus.domingo.groupware.AddressBook;
import de.jakop.lotus.domingo.groupware.ContactDigest;
import de.jakop.lotus.domingo.groupware.GroupwareRuntimeException;
import de.jakop.lotus.domingo.map.BaseDatabase;
import de.jakop.lotus.domingo.map.MapperRegistrationException;
import de.jakop.lotus.domingo.map.MappingException;
import de.jakop.lotus.domingo.map.NotesLocation;

/**
 * Interface to public and private name and address book databases.
 *
 * @author <a href=mailto:schwarz_dot_dan_at_gmail_dot_com>Daniel Schwarz</a>
 */
public final class NamesDatabase extends BaseDatabase implements AddressBook {

    /**
     * Constructor.
     *
     * @param locationUri URI of location of database.
     * @throws IOException if the location is not a valid notes location
     * @throws DNotesException if the database cannot be opened
     */
    public NamesDatabase(final String locationUri) throws IOException, DNotesException {
        super(locationUri);
    }

    /**
     * Constructor.
     *
     * @param session an existing domingo session
     * @param location location of database.
     * @throws DNotesException if the uri is invalid or the database cannot be
     *             opened
     */
    public NamesDatabase(final DSession session, final NotesLocation location) throws DNotesException {
        super(session, location);
    }

    /**
     * {@inheritDoc}
     * @see AddressBook#getContacts()
     */
    public Iterator getContacts() {
        DView view;
        if (this.isPrivate()) {
            view = getDatabase().getView("Contacts");
        } else {
             view = getDatabase().getView("People");
        }
        if (view == null) {
            throw new GroupwareRuntimeException("Cannot get contacts or people view");
        }
        return new ContactIterator(view.getAllEntries());
    }

    /**
     * {@inheritDoc}
     * @see AddressBook#isPrivate()
     */
    public boolean isPrivate() {
        return (getDatabase().isPrivateAddressBook());
    }

    /**
     * {@inheritDoc}
     * @see AddressBook#isPublic()
     */
    public boolean isPublic() {
        return (getDatabase().isPublicAddressBook());
    }

    /**
     * {@inheritDoc}
     * @see BaseDatabase#registerMappers()
     */
    protected void registerMappers() throws MapperRegistrationException {
        if (isPrivate()) {
            register(PrivateContactMapper.class);
        } else {
            register(PublicContactMapper.class);
        }
    }

    /**
     * Iterates thru a collection of view entries and returns for each
     * view-entry the corresponding email digest.
     */
    private class ContactIterator implements Iterator {

        private final Iterator allEntries;

        /**
         * Constructor.
         *
         * @param allEntries collection of view-entries
         */
        public ContactIterator(final Iterator allEntries) {
            this.allEntries = allEntries;
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return allEntries.hasNext();
        }

        /**
         * Returns instances of class {@link ContactDigest}.
         * @see java.util.Iterator#next()
         */
        public Object next() {
            DViewEntry entry = (DViewEntry) allEntries.next();
            ContactDigest memoDigest = new ContactDigest();
            try {
                map(entry, memoDigest);
            } catch (MappingException e) {
                throw new GroupwareRuntimeException("Cannot get next contact", e);
            }
            return memoDigest;
        }

        /**
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            // todo why don't we allow to remove entries? // allEntries.remove();
            throw new UnsupportedOperationException();
        }
    }
}
