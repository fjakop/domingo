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

package de.jakop.lotus.domingo.groupware.map;

import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.groupware.Email;
import de.jakop.lotus.domingo.groupware.Email.Importance;
import de.jakop.lotus.domingo.groupware.Email.Priority;
import de.jakop.lotus.domingo.map.BaseMapperTest;
import de.jakop.lotus.domingo.map.MethodNotFoundException;
import de.jakop.lotus.domingo.mock.MockDocument;

/**
 * Tests for class {@link EmailMapper}.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class EmailMapperTest extends BaseMapperTest {

    /**
     * @throws MethodNotFoundException if the mapper cannot be created
     */
    public void testMapToDocument() throws MethodNotFoundException {
        Email email = new Email();
        DDocument doc = new MockDocument();
        email.setPriority(Priority.HIGH);
        email.setImportance(Importance.HIGH);
        email.addCategories("Test1");
        email.addCategories("Test2");
        EmailMapper mapper = new EmailMapper();
        map(mapper, email, doc);
        assertEquals("1", doc.getItemValueString("Importance"));
        assertEquals("H", doc.getItemValueString("DeliveryPriority"));
        assertEquals(2, doc.getItemValue("Categories").size());
        assertTrue(doc.getItemValue("Categories").contains("Test1"));
        assertTrue(doc.getItemValue("Categories").contains("Test2"));
    }

    /**
     * @throws MethodNotFoundException if the mapper cannot be created
     */
    public void testMapToInstance() throws MethodNotFoundException {
        Email email = new Email();
        DDocument doc = new MockDocument();
        doc.replaceItemValue("DeliveryPriority", "H");
        doc.replaceItemValue("Importance", "1");
        EmailMapper mapper = new EmailMapper();
        map(mapper, doc, email);
        assertEquals(Priority.HIGH, email.getPriority());
        assertEquals(Importance.HIGH, email.getImportance());
    }
}
