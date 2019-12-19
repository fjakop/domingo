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

import java.util.List;

import de.jakop.lotus.domingo.groupware.Contact;
import de.jakop.lotus.domingo.groupware.ContactDigest;
import de.jakop.lotus.domingo.map.MethodNotFoundException;

/**
 * Mapper for Person entries in a Notes public address book.
 *
 * @see Contact
 * @see ContactDigest
 * @see BaseContactMapper
 * @see PrivateContactMapper
 * @author <a href=mailto:schwarz_dot_dan_at_gmail_dot_com>Daniel Schwarz</a>
 */
public final class PrivateContactMapper extends BaseContactMapper {

    /** zero-based column number of view column showing the full name of a contact. */
    private static final int FULLNAME_COLUMN = 0;

    /** zero-based column number of view column showing the email of a contact. */
    private static final int EMAIL_COLUMN = 1;

    /**
     * @throws MethodNotFoundException this should never happen
     */
    public PrivateContactMapper() throws MethodNotFoundException {
        super();
    }

    /**
     * {@inheritDoc}
     * @see BaseContactMapper#mapColumnValues(java.util.List, ContactDigest)
     */
    protected void mapColumnValues(final List columnValues, final ContactDigest digest) {
        digest.setFullName((String) columnValues.get(FULLNAME_COLUMN));
        digest.setEmail((String) columnValues.get(EMAIL_COLUMN));
    }
}
