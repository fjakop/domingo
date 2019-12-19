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

import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.groupware.CalendarEntry;
import de.jakop.lotus.domingo.groupware.CalendarEntry.Type;
import de.jakop.lotus.domingo.map.BaseMapperTest;
import de.jakop.lotus.domingo.map.MethodNotFoundException;
import de.jakop.lotus.domingo.mock.MockDocument;

/**
 * Tests for class {@link CalendarEntryMapper}.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class CalendarEntryMapperTest extends BaseMapperTest {

    /**
     * @throws MethodNotFoundException if the mapper cannot be created
     */
    public void testMapToDocument() throws MethodNotFoundException {
        CalendarEntry entry = new CalendarEntry();
        DDocument doc = new MockDocument();
        entry.setSendAttachments(true);
        entry.setType(Type.MEETING);
        entry.addCategory("Test1");
        entry.addCategory("Test2");
        CalendarEntryMapper mapper = new CalendarEntryMapper();
        map(mapper, entry, doc);
        assertEquals("3", doc.getItemValueString("Type"));
        assertEquals(2, doc.getItemValue("Categories").size());
        assertTrue(doc.getItemValue("Categories").contains("Test1"));
        assertTrue(doc.getItemValue("Categories").contains("Test2"));
    }

    /**
     * @throws MethodNotFoundException if the mapper cannot be created
     */
    public void testMapToInstance() throws MethodNotFoundException {
        CalendarEntry entry = new CalendarEntry();
        DDocument doc = new MockDocument();
        doc.replaceItemValue("Type", "4");
        doc.replaceItemValue("Importance", "1");
        CalendarEntryMapper mapper = new CalendarEntryMapper();
        map(mapper, doc, entry);
        assertEquals(Type.REMINDER, entry.getType());
    }
}
