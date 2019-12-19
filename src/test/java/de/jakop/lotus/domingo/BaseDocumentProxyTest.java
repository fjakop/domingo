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

package de.jakop.lotus.domingo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class BaseDocumentProxyTest extends BaseProxyTest {

    private String itemName = null;

    /**
     * @param name the name of the test
     */
    protected BaseDocumentProxyTest(String name) {
        super(name);
    }

    /**
     * Tests the getAttachment method.
     */
    public final void testGetAttachment() {
        System.out.println("-> testGetAttachment");
        DBaseDocument doc = createDBaseDocument();
        DRichTextItem rtItem = doc.createRichTextItem(itemName);

        String pre = "testGetAttachment_" + System.currentTimeMillis();
        String suf = ".txt";
        File file = null;
        try {
            file = File.createTempFile(pre, suf);
        } catch (IOException e) {
            assertTrue("Could not create test attachment file.", false);
        }

        String key = file.getAbsolutePath();

        rtItem.embedAttachment(key);

        doc.save();

        DEmbeddedObject embObj = doc.getAttachment(file.getName());

        assertNotNull("The embedded object should not be null.", embObj);
        assertEquals(
            "The name of the embedded object should be the same as the name at embedding.",
            file.getName(),
            embObj.getName());

        file.delete();
        doc.remove(true);
    }

    /**
     * Tests testGetEmbeddedObjects().
     */
    public final void testGetEmbeddedObjects() {
        System.out.println("-> testGetEmbeddedObjects");
        DBaseDocument doc = createDBaseDocument();

        // todo test the getEmbeddedObjects method?

        String pre1 = "testGetEmbeddedObjects1_" + System.currentTimeMillis();
        String pre2 = "testGetEmbeddedObjects2_" + System.currentTimeMillis();
        String suf = ".txt";
        File file1 = null;
        File file2 = null;
        try {
            file1 = File.createTempFile(pre1, suf);
            file2 = File.createTempFile(pre2, suf);
        } catch (IOException e) {
            assertTrue("Could not create test attachment file.", false);
        }

        file1.delete();
        file2.delete();
        doc.remove(true);
    }

    /**
     * Tests the method to create a RichTextItem.
     */
    public final void testCreateRichTextItem() {
        System.out.println("-> testCreateRichTextItem");
        DBaseDocument doc = createDBaseDocument();

        DRichTextItem rtItem = doc.createRichTextItem(itemName);
        assertNotNull("The created RichTextItem should exist.", rtItem);
        assertEquals("The RichTextItem's name should match.", rtItem.getName(), rtItem.getName());
    }

    /**
     * Tests the getAuthors method of a document.
     */
    public final void testGetAuthors() {
        System.out.println("-> testGetAuthors");
        DBaseDocument doc = createDBaseDocument();
        List authors = doc.getAuthors();
        assertNotNull("Document should have a authors list.", authors);
        if (!(doc instanceof DProfileDocument)) {
            assertTrue("The authors list should be empty.", authors.size() == 0);
        }
        doc.save();
        DDatabase database = doc.getParentDatabase();
        if (doc instanceof DDocument) {
            String docId = ((DDocument) doc).getUniversalID();
            doc.recycle();
            doc = database.getDocumentByUNID(docId);
        } else {
            String profileName = ((DProfileDocument) doc).getNameOfProfile();
            String profileKey = ((DProfileDocument) doc).getKey();
            doc.recycle();
            doc = database.getProfileDocument(profileName, profileKey);
        }
        // TODO Also in a profile document, the authors should be available without reloading the document
        if (doc instanceof DProfileDocument) {
            return;
        }
        String updatedBy = doc.getItemValueString("$UpdatedBy");
        assertEquals("The $UpdatedBy item should be the user name of the session.", getSession().getUserName(), updatedBy);
        
        authors = doc.getAuthors();
        assertTrue("The authors list should have at least one entry.", authors.size() > 0);
        assertEquals("The authors name should be the user name of the session.", getSession().getUserName(), authors.get(0));
    }

    /**
     * Tests doc.getLastAccessed().
     */
    public final void testGetLastAccessed() {
        System.out.println("-> testGetLastAccessed");
        DBaseDocument doc = createDBaseDocument();
        assertNotNull("Document now should have a LastAccessed field (also before save).", doc.getLastAccessed());
        doc.save();
        assertNotNull("Document now should have a LastAccessed field.", doc.getLastAccessed());
    }

    /**
     * Tests doc.testGetLastModified().
     */
    public final void testGetLastModified() {
        System.out.println("-> testGetLastModified");
        DBaseDocument doc = createDBaseDocument();
//        if (doc instanceof DDocument) {
//            assertNull("Document should have no LastModified field, because it is new.", doc.getLastModified());
//        } else {
//            assertNotNull("Document now should have a LastModified field (also before save).", doc.getLastModified());
//        }
        doc.save();
        assertNotNull("Document now should have a LastModified field.", doc.getLastModified());
    }

    /**
     * Tests the creation field getter.
     */
    public final void testGetCreated() {
        System.out.println("-> testGetCreated");
        DBaseDocument doc = createDBaseDocument();
        assertNotNull("Document now should have a created field (also before save).", doc.getCreated());
        doc.save();
        assertNotNull("Document now should have a created field.", doc.getCreated());
    }

    /**
     * Tests appending item values for all types.
     */
    public final void testAppendItemValue() {
        System.out.println("-> testAppendItemValue");
        String appendItemName = "" + System.currentTimeMillis();
        DBaseDocument doc = createDBaseDocument();
        DItem nullItem = (DItem) doc.getFirstItem(appendItemName);
        assertTrue("Document had an item named '" + appendItemName + "' but should NOT have one.", nullItem == null);

        {
            doc.appendItemValue(appendItemName);
            DItem emptyItem = (DItem) doc.getFirstItem(appendItemName);
            if (emptyItem.getValues().size() == 0) {
                assertTrue("Item should be empty (item with an empty string), but item IS empty.", false);
            } else {
                boolean emptyOK = "".equals((String) emptyItem.getValues().iterator().next());
                assertTrue("Document should have empty item, but recipient was:" + emptyItem.getValues(), emptyOK);
            }
            removeItem(doc, appendItemName);
        }

        {
            doc.appendItemValue(appendItemName, 4.4);
            DItem doubleItem = (DItem) doc.getFirstItem(appendItemName);
            Object o = doubleItem.getValues().iterator().next();
            boolean doubleOK = (o instanceof Double) ? ((Double) o).doubleValue() == 4.4 : false;
            assertTrue("Received object should be a Double with a value of 4.4, but is not: " + o, doubleOK);
            removeItem(doc, appendItemName);
        }

        {
            String value1 = "value1";
            String value2 = "value2";
            doc.appendItemValue(appendItemName, value1);
            doc.appendItemValue(appendItemName, value2);
            DItem item = (DItem) doc.getFirstItem(appendItemName);
            List values = item.getValues();
            assertTrue("Item should have only one value but has: " + values,
                values.size() == 1 && (value1.equals(values.get(0)) || value2.equals(values.get(0))));
            // We have to remove one of the two items directly, otherwise the removeItem method would complain its test,
            doc.removeItem(appendItemName);
            removeItem(doc, appendItemName);
        }

        {
            doc.appendItemValue(appendItemName, 3);
            DItem intItem = (DItem) doc.getFirstItem(appendItemName);
            Object o = intItem.getValues().iterator().next();
            boolean intOK = (o instanceof Double) ? ((Double) o).intValue() == 3 : false;
            assertTrue("Received object should be Integer 3, but is: " + o.getClass() + ": " + o, intOK);
            removeItem(doc, appendItemName);
        }

        {
            Integer two = new Integer(2);
            Integer five = new Integer(5);
            List manyInts = new ArrayList();
            manyInts.add(two);
            manyInts.add(five);
            doc.appendItemValue(appendItemName, manyInts);
            DItem intListItem = (DItem) doc.getFirstItem(appendItemName);
            List list = intListItem.getValues();
            boolean intListOK =
                list.size() == 2 ? (((Double) list.get(0)).intValue() == 2
                && ((Double) list.get(1)).intValue() == 5) : false;
            assertTrue("object should be Integer list (" + two + ", " + five + "), but is: " + list, intListOK);
            removeItem(doc, appendItemName);
        }

        {
            String someText = "some text";
            doc.appendItemValue(appendItemName, someText);
            DItem stringItem = (DItem) doc.getFirstItem(appendItemName);
            Object o = stringItem.getValues().iterator().next();
            boolean stringOK = someText.equals(o);
            assertTrue("Received object should be String with value '" + someText + "', but is not: " + o, stringOK);
            removeItem(doc, appendItemName);
        }
        doc.remove(true);
    }

    /**
     * Tests is a document has at least one item.
     *
     */
    public final void testHasItem() {
        System.out.println("-> testHasItem");

        DBaseDocument doc = createDBaseDocument();
        assertTrue("Doc should NOT have a item named " + itemName, !doc.hasItem(itemName));

        doc.appendItemValue(itemName);
        assertTrue("Doc should have a item named " + itemName, doc.hasItem(itemName));

        doc.remove(true);
    }

    /**
     * Tests if getItems works properly.
     */
    public final void testGetItems() {
        System.out.println("-> testGetItems");

        DBaseDocument doc = createDBaseDocument();
        Set itemCache = new HashSet();
        for (Iterator it = doc.getItems(); it.hasNext();) {
            DBaseItem dItem = (DBaseItem) it.next();
            itemCache.add(dItem);
        }

        while (doc.hasItem(itemName)) {
            doc.removeItem(itemName);
        }

        String someValue = "some value that's value is really unimportant";
        doc.appendItemValue(itemName, someValue);

        for (Iterator it = doc.getItems(); it.hasNext();) {
            DItem dItem = (DItem) it.next();
            if (!itemCache.contains(dItem)) {
                List values = dItem.getValues();
                if (values != null) {
                    assertEquals(
                        "The only value that was added should be someValue. (" + getClass().getName() + ")",
                        someValue,
                        values.get(values.size() - 1));
                } else {
                    assertTrue("Item should have one value, but item list is NULL.", false);
                }
            }
        }
    }

    /**
     * Tests is getFirstItem supplies a document.
     *
     */
    public final void testGetFirstItem() {
        System.out.println("-> testGetFirstItem");

        DBaseDocument doc = createDBaseDocument();

        Set itemCache = new HashSet();
        for (Iterator it = doc.getItems(); it.hasNext();) {
            DBaseItem dItem = (DBaseItem) it.next();
            itemCache.add(dItem);
        }

        while (doc.hasItem(itemName)) {
            doc.removeItem(itemName);
        }

        String someValue = "some value that's value is really unimportant";
        DItem someItem = (DItem) doc.appendItemValue(itemName, someValue);

        DItem item = (DItem) doc.getFirstItem(itemName);
        assertEquals("Both objects should be equal.", someItem.getValueString(), item.getValueString());
    }

    /**
     * Tests removing of Documents.
     */
    public final void testRemove1() {
        System.out.println("-> BaseDocumentProxy.testRemove1");
        DBaseDocument doc = createDBaseDocument();
        assertTrue("Document should be removable, because it does already exist in DB.", doc.remove(true));
        //System.out.println("Following Exception is OK for testing purposes:");
        try {
            assertTrue("Document should NOT be saved, because it does no longer exist in DB.", !doc.save());
        } catch (DNotesRuntimeException e) {
            // this is ok, object is already removed.
        }
        //doc.remove(true);
    }

    /**
     * Tests removing of Documents.
     */
    public final void testRemove2() {
        System.out.println("-> BaseDocumentProxy.testRemove2");
        DBaseDocument doc = createDBaseDocument();
        doc.replaceItemValue("Test", "Test");
        assertTrue("Document should have been saved. (" + getClass().getName() + ")", doc.save());
        assertTrue("Document should be removable and be removed.", doc.remove(true));
        //System.out.println("Following Exception is OK for testing purposes:");
        try {
            assertTrue("Document should NOT have been saved, because it was previously removed.", !doc.save());
        } catch (DNotesRuntimeException e) {
            // this is ok, object is already removed.
        }
        //System.out.println("Following Exception is OK for testing purposes: ");
        assertTrue("Document should again not be removable, because it does no longer exist.", !doc.remove(true));
        //doc.remove(true);
    }

    /**
     * Tests the method getItemValueString for all previously inserted types.
     *
     */
    public final void testGetItemValueString() {
        System.out.println("-> testGetItemValueString");
        {
            // No item
            DBaseDocument doc = createDBaseDocument();
            String value = doc.getItemValueString(itemName);
            assertEquals("Value of non existent item should be empty string, but was '" + value + "'", "", value);
            doc.remove(true);
        }

        {
            // Empty item
            DBaseDocument doc = createDBaseDocument();
            doc.appendItemValue(itemName);
            String value = doc.getItemValueString(itemName);
            assertEquals("Received Text should be '', but was '" + value + "'", "", value);
            doc.remove(true);
        }

        {
            // String item
            DBaseDocument doc = createDBaseDocument();
            String itemValue = "some_item_value";
            doc.appendItemValue(itemName, itemValue);
            String value = doc.getItemValueString(itemName);
            assertEquals("Received Text should be '" + itemValue + "', but was '" + value + "'", itemValue, value);
            doc.remove(true);
        }

        {
            // int item
            DBaseDocument doc = createDBaseDocument();
            int intValue = 2;
            doc.appendItemValue(itemName, intValue);
            String value = doc.getItemValueString(itemName);
            assertTrue("Received Text should be '', but was '" + value + "'", "".equals(value));
            doc.remove(true);
        }

        {
            // double item
            DBaseDocument doc = createDBaseDocument();
            double doubleValue = 2.6;
            doc.appendItemValue(itemName, doubleValue);
            String value = doc.getItemValueString(itemName);
            assertTrue("Received Text should be '', but was '" + value + "'", "".equals(value));
            doc.remove(true);
        }
    }

    /**
     * Tests the method getItemValueInteger for all previously inserted types.
     */
    public final void testGetItemValueInteger() {
        System.out.println("-> testGetItemValueInteger");

        {
            {
                // No item
                DBaseDocument doc = createDBaseDocument();
                Integer emptyValue = doc.getItemValueInteger(itemName);
                assertNull("Value should be null, but returns: " + emptyValue, emptyValue);
                doc.remove(true);
            }

            {
                // Empty item
                DBaseDocument doc = createDBaseDocument();
                doc.appendItemValue(itemName);
                Integer value = doc.getItemValueInteger(itemName);
                assertNull("Received Text should be 0, but was '" + value + "'", value);
                doc.remove(true);
            }

            {
                // String item
                DBaseDocument doc = createDBaseDocument();
                String stringValue = "someValue";
                doc.appendItemValue(itemName, stringValue);
                Integer intResult = doc.getItemValueInteger(itemName);
                assertNull("Value should be null, but is " + intResult, intResult);
                doc.remove(true);
            }

            {
                // int item
                DBaseDocument doc = createDBaseDocument();
                int itemValue = 2;
                doc.appendItemValue(itemName, itemValue);
                Integer value = doc.getItemValueInteger(itemName);
                assertEquals("value should be " + itemValue + ", but was " + value, itemValue, value.intValue());
                doc.remove(true);
            }

            {
                // double item
                DBaseDocument doc = createDBaseDocument();
                double doubleValue = 3.4;
                doc.appendItemValue(itemName, doubleValue);
                int intResult = doc.getItemValueInteger(itemName).intValue();
                assertEquals("wrong value", 3, intResult);
                doc.remove(true);
            }

            {
                // double item
                DBaseDocument doc = createDBaseDocument();
                double doubleValue = 3.6;
                doc.appendItemValue(itemName, doubleValue);
                int intResult = doc.getItemValueInteger(itemName).intValue();
                assertEquals("wrong value", 4, intResult);
                doc.remove(true);
            }
        }
    }

    /**
     * Tests the method getItemValueDouble for all previously inserted types.
     */
    public final void testGetItemValueDouble() {
        System.out.println("-> testGetItemValueDouble");

        {
            {
                // No item
                DBaseDocument doc = createDBaseDocument();
                Double emptyValue = doc.getItemValueDouble(itemName);
                assertNull("Value from a non existing field should return 0.0, but returns: " + emptyValue, emptyValue);
                doc.remove(true);
            }

            {
                // Empty item
                DBaseDocument doc = createDBaseDocument();
                doc.appendItemValue(itemName);
                Double value = doc.getItemValueDouble(itemName);
                assertNull("Received Double should be 0.0, but was '" + value + "'", value);
                doc.remove(true);
            }

            {
                // String item
                DBaseDocument doc = createDBaseDocument();
                String someValue = "someValue";
                doc.appendItemValue(itemName, someValue);
                Double doubleResult = doc.getItemValueDouble(itemName);
                assertNull("Value of type Double should be null, but is " + doubleResult, doubleResult);
                doc.remove(true);
            }

            {
                // int item
                int itemValue = 2;
                DBaseDocument doc = createDBaseDocument();
                doc.appendItemValue(itemName, itemValue);
                Double doubleValue = doc.getItemValueDouble(itemName);
                assertEquals("int (from double) should be '" + itemValue + "', but was '" + doubleValue + "'",
                    itemValue, doubleValue.doubleValue(), 0.001);
                doc.remove(true);
            }

            {
                // double item
                double itemValue = 2.6;
                DBaseDocument doc = createDBaseDocument();
                doc.appendItemValue(itemName, itemValue);
                Double doubleValue = doc.getItemValueDouble(itemName);
                assertEquals("Received Double should be '" + itemValue + "', but was '" + doubleValue + "'",
                    itemValue, doubleValue.doubleValue(), 0.001);
                doc.remove(true);
            }
        }
    }

    /**
     * Tests the method getItemValueDate for all previously inserted types.
     */
    public final void testGetItemValueDate() {
        System.out.println("-> testGetItemValueDate");

        {
            // No item
            DBaseDocument doc = createDBaseDocument();
            Calendar emptyValue = doc.getItemValueDate(itemName);
            assertNull("Value should be null, but is " + emptyValue, emptyValue);
            doc.remove(true);
        }

        {
            // Empty item
            DBaseDocument doc = createDBaseDocument();
            doc.appendItemValue(itemName);
            Calendar value = doc.getItemValueDate(itemName);
            assertNull("Received Date should be NULL, but was '" + value + "'", value);
            doc.remove(true);
        }

        {
            // String item
            DBaseDocument doc = createDBaseDocument();
            String someValue = "someValue";
            doc.appendItemValue(itemName, someValue);
            Calendar dateResult = doc.getItemValueDate(itemName);
            assertNull("Value of type Date should be null , but is " + dateResult, dateResult);
            doc.remove(true);
        }

        {
            // int item
            int itemValue = 2;
            DBaseDocument doc = createDBaseDocument();
            doc.appendItemValue(itemName, itemValue);
            Calendar dateValue = doc.getItemValueDate(itemName);
            assertNull("Received Date should be '" + itemValue + "', but was '" + dateValue + "'", dateValue);
            doc.remove(true);
        }

        {
            // double item
            double itemValue = 2.6;
            DBaseDocument doc = createDBaseDocument();
            doc.appendItemValue(itemName, itemValue);
            Calendar dateValue = doc.getItemValueDate(itemName);
            assertNull("Received Date should be '" + itemValue + "', but was '" + dateValue + "'", dateValue);
            doc.remove(true);
        }
    }

    /**
     * Tests the method getItemValue for all previously inserted types.
     */
    public final void testGetItemValue() {
        System.out.println("-> testGetItemValue");

        {
            DBaseDocument doc = createDBaseDocument();
            List result = doc.getItemValue(itemName);
            assertTrue("Document should have a list with one entry: '', but has: " + result, result.size() == 0);
            doc.remove(true);
        }

        {
            DBaseDocument doc = createDBaseDocument();
            doc.appendItemValue(itemName);
            List result = doc.getItemValue(itemName);
            assertTrue(
                "Document should have a list with one entry: '', but has: " + result + " with length " + result.size(),
                result.size() == 1);
            doc.remove(true);
        }

        {
            DBaseDocument doc = createDBaseDocument();
            List list = new ArrayList();
            String itemValue1 = "itemValue1";
            String itemValue2 = "itemValue2";
            list.add(itemValue1);
            list.add(itemValue2);
            doc.appendItemValue(itemName, list);
            List value = doc.getItemValue(itemName);
            assertTrue("Received list should be '" + list + "', but was '" + value + "'", list.equals(value));
            doc.remove(true);
        }
    }

    /**
     * Tests replacing of item values in a document.
     */
    public final void testReplaceItemValueDateZone() {
    }

    /**
     * Tests replacing of item values in a document.
     */
    public final void testReplaceItemValueString1() {
        DBaseDocument doc = createDBaseDocument();
        doc.appendItemValue(itemName);
        String value = doc.getItemValueString(itemName);
        assertTrue("Item should have an empty String as value.", "".equals(value));
    }

    /**
     * Tests replacing of item values in a document.
     */
    public final void testReplaceItemValueString2() {
        DBaseDocument doc = createDBaseDocument();
        String expected = "someValue";
        doc.replaceItemValue(itemName, expected);
        String result = doc.getItemValueString(itemName);
        assertTrue("Item value is wrong", expected.equals(result));
    }

    /**
     * Tests replacing of item values in a document.
     */
    public final void testReplaceItemValueDouble() {
        DBaseDocument doc = createDBaseDocument();
        double expected = 1.2;
        doc.replaceItemValue(itemName, expected);
        double result = doc.getItemValueDouble(itemName).doubleValue();
        assertEquals("Item value is wrong", expected, result, 0.001);
    }

    /**
     * Tests replacing of item values in a document.
     */
    public final void testReplaceItemValueInteger() {
        DBaseDocument doc = createDBaseDocument();
        int expected = 3;
        doc.replaceItemValue(itemName, expected);
        double result = doc.getItemValueInteger(itemName).intValue();
        assertTrue("Item value is wrong", expected == result);
    }

    /**
     * Tests replacing of item values in a document.
     */
    public final void testReplaceItemValue() {
        DBaseDocument doc = createDBaseDocument();
        // Test normal List:
        List list = new ArrayList();
        String expected1 = "expected1";
        String expected2 = "expected2";
        list.add(expected1);
        list.add(expected2);
        doc.replaceItemValue(itemName, list);
        List result = doc.getItemValue(itemName);
        assertTrue("result is wrong", list.equals(result));
    }

    /**
     * Removes a specified item out of a document and performs a test if a
     * so named item is still in that document.
     * @param doc this document
     * @param pItemName the item's name
     */
    protected final void removeItem(DBaseDocument doc, String pItemName) {
        doc.removeItem(pItemName);
        DBaseItem nullItem = doc.getFirstItem(pItemName);
        assertTrue(
            "Document had an item named '" + pItemName + "' but that should have been removed.",
            nullItem == null);
    }

    /**
     * @return DBaseDocument that is used for tests in this class, but is of a type of all extending classes.
     */
    protected abstract DBaseDocument createDBaseDocument();

    /**
     * {@inheritDoc}
     * @see BaseProxyTest#setUpTest()
     */
    protected final void setUpTest() {
        itemName = "test";
        setUpConcreteTest();
    }

    /**
     * Can be implemented by sub classes to perform concrete setup tasks.
     */
    protected abstract void setUpConcreteTest();

    /** @return a unique item name. */
    public final String getItemName() {
        return itemName;
    }
}
