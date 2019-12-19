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

import de.jakop.lotus.domingo.DViewEntry;
import de.jakop.lotus.domingo.groupware.Contact;
import de.jakop.lotus.domingo.groupware.ContactDigest;
import de.jakop.lotus.domingo.map.BaseMapper;
import de.jakop.lotus.domingo.map.DirectMapper;
import de.jakop.lotus.domingo.map.MappingException;
import de.jakop.lotus.domingo.map.DMapper;
import de.jakop.lotus.domingo.map.MethodNotFoundException;

/**
 * Mapper for Contact entries - common functonality between Public and Private
 * address books.
 *
 * @see Contact
 * @see ContactDigest
 * @see PublicContactMapper
 * @see PrivateContactMapper
 * @author <a href=mailto:schwarz_dot_dan_at_gmail_dot_com>Daniel Schwarz</a>
 */
public abstract class BaseContactMapper extends BaseMapper {

    private static final Class INSTANCE_CLASS = Contact.class;

    private static final Class DIGEST_CLASS = ContactDigest.class;

    /**
     * Constructor.
     *
     * @throws MethodNotFoundException if a getter or setter method was not
     *             found in the instance or digest class
     */
    public BaseContactMapper() throws MethodNotFoundException {
        super(INSTANCE_CLASS, DIGEST_CLASS);
        add(new DirectMapper("FullName", String.class));
        add(new DirectMapper("MailAddress", "Email", String.class));
    }

    /**
     * {@inheritDoc}
     * @see BaseContactMapper#map(DViewEntry, java.lang.Object)
     */
    public final void map(final DViewEntry viewEntry, final Object object) throws MappingException {
         ContactDigest digest = (ContactDigest) object;
         List columnValues = viewEntry.getColumnValues();
         digest.setUnid(viewEntry.getUniversalID());
         mapColumnValues(columnValues, digest);
    }

    /**
     * Maps the column values a contact digest.
     * Implemented by database/view-specific subclasses.
     *
     * @param columnValues column values of a view entry.
     * @param digest the digest to map the column values into
     */
    protected abstract void mapColumnValues(final List columnValues, final ContactDigest digest);

    /**
     * {@inheritDoc}
     *
     * @see DMapper#newDigest()
     */
    public final Object newDigest() {
        return new ContactDigest();
    }

    /**
     * {@inheritDoc}
     * @see DMapper#newInstance()
     */
    public final Object newInstance() {
        return new Contact();
    }
}
