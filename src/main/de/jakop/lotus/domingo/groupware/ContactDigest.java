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

import de.jakop.lotus.domingo.map.BaseDigest;

/**
 * Represents a digest of a Notes Contact (from private address book) or Person
 * (from public address book).
 *
 * @author <a href=mailto:schwarz_dot_dan_at_gmail_dot_com>Daniel Schwarz</a>
 */
public final class ContactDigest extends BaseDigest {
    private String fFullName;

    private String fEmail; // probably wants to be an EmailAddress object with
                            // type included

    /**
     * @return the fEmail
     */
    public String getEmail() {
        return fEmail;
    }

    /**
     * @param email
     *            the Email to set
     */
    public void setEmail(final String email) {
        fEmail = email;
    }

    /**
     * @return the FullName
     */
    public String getFullName() {
        return fFullName;
    }

    /**
     * @param fullName
     *            the FullName to set
     */
    public void setFullName(final String fullName) {
        fFullName = fullName;
    }
}
