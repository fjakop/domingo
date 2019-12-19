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

import java.util.Iterator;

/**
 * Interface to the mail functionality of a Notes address book.
 *
 * @author <a href=mailto:schwarz_dot_dan_at_gmail_dot_com>Daniel Schwarz</a>
 */
public interface AddressBook {

    /**
     * Returns an iterator over all contacts in the Address Book.
     *
     * <p>
     * Should return in alphabetical order
     * </p>
     *
     * @return iterator over all contacts in the Address Book
     */

    Iterator getContacts();

    /**
     * Indicates if a database is a Personal Address Book.
     *
     * @return <code>true</code> if the database is a Personal Address Book,
     *         <code>false</code> if the database is not Personal Address Book
     */
    boolean isPrivate();

    /**
     * Indicates if a database is a Public Address Book.
     *
     * @return <code>true</code> if the database is a Public Address Book,
     *         <code>false</code> if the database is not Public Address Book
     */
    boolean isPublic();

    /**
     * Returns the title of the Address Book.
     *
     * @return title
     */
    String getTitle();

}
