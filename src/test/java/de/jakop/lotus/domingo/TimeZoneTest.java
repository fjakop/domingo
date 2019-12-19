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

package de.jakop.lotus.domingo;

import java.util.Calendar;
import java.util.TimeZone;

import de.jakop.lotus.domingo.util.GregorianDateTime;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class TimeZoneTest extends BaseProxyTest {

    private String itemName;

    /**
     * @param name the name of the test
     */
    public TimeZoneTest(String name) {
        super(name);
    }

    /**
     * Tests time zone handling in documents.
     */
    public void testTimezones() {
        System.out.println("-> testTimezones");

        DBaseDocument doc = createDBaseDocument();
        {
            Calendar calendar = new GregorianDateTime(2007, 07, 01, 17, 59, 59);
            calendar.set(2007, 07, 01, 17, 59, 59);
            calendar.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            doc.replaceItemValue(itemName, calendar);
            doc.save();
            Calendar result = doc.getItemValueDate(itemName);
            System.out.println(calendar.toString());
            System.out.println(result.toString());
            System.out.println(calendar.getTimeZone());
            System.out.println(result.getTimeZone());
            assertEquals("wrong item value", calendar.getTime().getTime() / 1000, result.getTime().getTime() / 1000);
            assertEquals("wrong item value", calendar.getTime(), result.getTime());
            assertEquals(calendar.getTimeZone(), result.getTimeZone());
        }
    }

    /**
     * Removes a specified item out of a document and performs a test if a
     * so named item is still in that document.
     * @param doc this document
     * @param pItemName the item's name
     */
    protected void removeItem(DBaseDocument doc, String pItemName) {
        doc.removeItem(pItemName);
        DBaseItem nullItem = doc.getFirstItem(pItemName);
        assertTrue(
            "Document had an item named '" + pItemName + "' but that should have been removed.",
            nullItem == null);
    }

    /**
     * Returns a normal Document.
     * @return DBaseDocument a normal Document
     */
    protected DBaseDocument createDBaseDocument() {
        return getDatabase().createDocument();
    }

    /**
     * {@inheritDoc}
     * @see BaseProxyTest#setUpTest()
     */
    public void setUpTest() {
        itemName = "some_item_that_does_surely_not_exist";
    }

    /** @return a unique item name. */
    public String getItemName() {
        return itemName;
    }
}
