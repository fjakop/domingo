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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.jakop.lotus.domingo.util.GregorianDate;

/**
 * @author MarcusT
 */
public final class DocumentProxyTest extends BaseDocumentProxyTest {

    /**
     * Builds all tests for DocumentProxies.
     * @param name the tests name
     */
    public DocumentProxyTest(String name) {
        super(name);
    }

    /**
     * Tests the equality of two Documents.
     */
    public void testEquals() {
        System.out.println("-> testEquals");

        DDocument doc = getDatabase().createDocument();
        doc.save();
        DDocument receivedDoc = getDatabase().getDocumentByUNID(doc.getUniversalID());
        if (!isDIIOP()) {
            // TODO maybe this should also be true for DIIOP connections
            assertEquals("Documents should be equal but are not.", doc, receivedDoc);
        }
        DBaseDocument anotherOne = createDBaseDocument();
        assertTrue("Documents should NOT be equal but are.", !doc.equals(anotherOne));

        doc.remove(true);
    }

    /**
     * Tests saving of a document.
     *
     */
    public void testSave() {
        System.out.println("-> testSave");

        DDocument doc = getDatabase().createDocument();
        String someValue = "someValueForTestSave";
        doc.appendItemValue(getItemName(), someValue);
        doc.save();

        String uniID = doc.getUniversalID();

        DDocument received = getDatabase().getDocumentByUNID(uniID);
        String receivedValue = received.getItemValueString(getItemName());
        assertEquals(
            "The Document received from the database should contain an item, but does not.",
            someValue, receivedValue);

        if (!isDIIOP()) {
            // TODO maybe this should also be true for DIIOP connections
            assertTrue("The Document received should be equal to the previously saved one.", doc.equals(received));
        }

        doc.remove(true);
    }

    /**
     * Tests save(boolean).
     */
    public void testSaveBoolean() {
        System.out.println("-> testSaveBoolean");
        DDocument doc = getDatabase().createDocument();
        String someValue = "someValueForTestSave";
        doc.appendItemValue(getItemName(), someValue);
        boolean saved = doc.save(true);
        assertTrue("The save action should work properly.", saved);

        doc.appendItemValue(getItemName() + getItemName(), someValue);
        saved = doc.save(false);
        assertTrue("The unforced save action should work properly.", saved);

        String uniID = doc.getUniversalID();

        DDocument received = getDatabase().getDocumentByUNID(uniID);
        assertNotNull("The received document should exist.", received);
        String receivedValue = received.getItemValueString(getItemName());
        assertEquals("Document should contain an item, but does not.", someValue, receivedValue);

        String receivedValue1 = received.getItemValueString(getItemName() + getItemName());
        assertEquals(" Document should contain a second item, but does not.", someValue, receivedValue1);
        if (!isDIIOP()) {
            // TODO maybe this should also be true for DIIOP connections
            assertTrue("Document should be equal to the previously saved one.", doc.equals(received));
        }
        doc.remove(true);
    }

    /**
     * Tests save(boolean, boolean).
     */
    public void testSaveBooleanBoolean() {
        System.out.println("-> testSaveBooleanBoolean");

        // todo test save(boolean, boolean) if another user is editing the document
        // see also testSaveWithOneArgument in DocumentProxyTest

        DDocument doc = getDatabase().createDocument();
        String someValue = "someValueForTestSave";
        doc.appendItemValue(getItemName(), someValue);
        boolean saved = doc.save(true, false);
        assertTrue("The save action should work properly.", saved);

        doc.appendItemValue(getItemName() + getItemName(), someValue);
        saved = doc.save(true, true);
        assertTrue("The unforced save action should work properly.", saved);

        String uniID = doc.getUniversalID();

        DDocument received = getDatabase().getDocumentByUNID(uniID);
        assertNotNull("The received document should exist.", received);
        String receivedValue = received.getItemValueString(getItemName());
        assertEquals("Document should contain an item, but does not.", someValue, receivedValue);

        String receivedValue1 = received.getItemValueString(getItemName() + getItemName());
        assertEquals("Document should contain a second item, but does not.", someValue, receivedValue1);
        if (!isDIIOP()) {
            // TODO maybe this should also be true for DIIOP connections
            assertTrue("Document should be equal to the previously saved one.", doc.equals(received));
        }
        doc.remove(true);
    }

    /**
     * Tests relative general methods as isNewNote and getNoteId.
     *
     */
    public void testGeneralInfo() {
        System.out.println("-> testGeneralInfo");
        DDocument doc = getDatabase().createDocument();
        assertTrue("Document should be new (not saved)", doc.isNewNote());
        doc.save();
        assertTrue("Document should NOT be new (saved) (" + getClass().getName() + ")", !doc.isNewNote());

        String noteId = doc.getNoteID();
        String received = getDatabase().getDocumentByUNID(doc.getUniversalID()).getNoteID();
        assertTrue("The NoteID of a saved one differs from the direct accessible one.", noteId.equals(received));

        doc.remove(true);
    }

    /**
     * Tests the functionality of a document to be a response.
     *
     */
    public void testResponses() {
        System.out.println("-> testResponses");
        DDocument doc = getDatabase().createDocument();
        doc.save();
        assertTrue("Document should be no response.", !doc.isResponse());

        Iterator it = doc.getResponses();
        assertTrue("Document should have no responses.", !it.hasNext());

        DDocument parent = getDatabase().createDocument();
        parent.save();
        doc.makeResponse(parent);
        assertTrue("Document should be a response.", doc.isResponse());

        parent.save();
        doc.save();
        it = parent.getResponses();
        assertTrue("Document should have responses.", it.hasNext());

    }

    /**
     * Tests a document to its ability to have parents.
     *
     */
    public void testParents() {
        System.out.println("-> testParents");

        DDocument doc1 = getDatabase().createDocument();
        DDocument doc2 = getDatabase().createDocument();
        doc1.save();
        doc2.save();
        assertNull("Document has a parent, but should have NONE.", doc1.getParentDocument());

        DDocument parent = getDatabase().createDocument();
        parent.save();
        doc1.makeResponse(parent);
        doc2.makeResponse(parent);
        doc1.save();
        doc2.save();

        DDocument parent1 = doc1.getParentDocument();
        assertTrue("Document has no parent, but should have one.", (parent1 != null && parent1.equals(parent)));

        DDocument parent2 = doc2.getParentDocument();
        assertTrue("Document has no parent, but should have one.", (parent2 != null && parent2.equals(parent)));

        assertTrue("Parent has wrong UniveralID.", parent2.getUniversalID().equals(parent.getUniversalID()));

        doc1.remove(true);
        doc2.remove(true);
        parent.remove(true);
    }

    /**
     * Tests the copyToDatabase method.
     */
    public void testCopyToDatabase() {
        DDocument doc = getDatabase().createDocument();
        String itemValue = "some really unimportant text";
        doc.appendItemValue(getItemName(), itemValue);
        doc.save();

        DDatabase newDB = getSession().createDatabase(getServerName(), "testCopyToDatabase_" + System.currentTimeMillis() + ".nsf");
        doc.copyToDatabase(newDB);

        Iterator it = newDB.getAllDocuments();

        boolean ok = false;
        while (it.hasNext()) {
            DDocument doc1 = (DDocument) it.next();
            if (itemValue.equals(doc1.getItemValueString(getItemName()))) {
                ok = true;
            }
        }
        assertTrue("The document is NOT in the new Database", ok);
        doc.remove(true);
        newDB.remove();
    }

    /**
     * Tests send(String).
     */
    public void testSendString() {
        System.out.println("-> testSendString");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo tests - please delete this mail.");
        doc.save();
        String user = getSession().getUserName();
        doc.send(user);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**
     * Tests setSaveMessageOnSend(boolean).
     */
    public void testSetSaveMessageOnSaveTrue() {
        System.out.println("-> testSaveMessageOnSend");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo tests - please delete this mail.");
        doc.setSaveMessageOnSend(true);
        String user = getSession().getUserName();
        doc.send(user);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**
     * Tests setSaveMessageOnSend(boolean).
     */
    public void testSetSaveMessageOnSaveFalse() {
        System.out.println("-> testSaveMessageOnSend");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo tests - please delete this mail.");
        doc.setSaveMessageOnSend(false);
        String user = getSession().getUserName();
        doc.send(user);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**
     * Tests setEncryptOnSendTrue(boolean).
     */
    public void testSetEncryptOnSendTrue() {
        System.out.println("-> testSetEncryptOnSendTrue");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo tests - please delete this mail.");
        doc.setEncryptOnSend(false);
        doc.save();
        String user = getSession().getUserName();
        doc.send(user);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**
     * Tests setEncryptOnSendTrue(boolean).
     */
    public void testSetEncryptOnSendFalse() {
        System.out.println("-> testSetEncryptOnSendFalse");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo tests - please delete this mail.");
        doc.setEncryptOnSend(false);
        doc.save();
        String user = getSession().getUserName();
        doc.send(user);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**
     * Tests setSignOnSendTrue(boolean).
     */
    public void testSetSignOnSendTrue() {
        System.out.println("-> testSetSignOnSendTrue");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo tests - please delete this mail.");
        doc.setSignOnSend(false);
        doc.save();
        String user = getSession().getUserName();
        doc.send(user);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**
     * Tests setEncryptOnSendTrue(boolean).
     */
    public void testSetSignOnSendFalse() {
        System.out.println("-> testSetSignOnSendFalse");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo tests - please delete this mail.");
        doc.setSignOnSend(false);
        doc.save();
        String user = getSession().getUserName();
        doc.send(user);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**
     * Tests sign(boolean).
     */
    public void testSign() {
        System.out.println("-> testSign");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo - please delete this mail.");
        doc.sign();
        doc.save();
        String user = getSession().getUserName();
        doc.send(user);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**SetSignOnSend
     * Tests send(List).
     */
    public void testSendList() {
        System.out.println("-> testSendString");
        DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("Subject", "# Mailing test from Domingo tests - please delete this mail.");
        doc.save();
        String user = getSession().getUserName();
        List list = new ArrayList();
        list.add(user);
        doc.send(list);
        assertNotNull("Document not sent. Maybe disconnected from mail server?.", doc.getItemValueDate("PostedDate"));
        doc.remove(true);
    }

    /**
     * Tests send(String).
     */
    public void testDateTimeValues1() {
        System.out.println("-> testDateTimeValues");
        DDocument doc = getDatabase().createDocument();
        try {
            Calendar date1 = new GregorianDate(2004, Calendar.FEBRUARY, 3);
            Calendar date2 = new GregorianDate(2007, Calendar.JULY, 5);
            doc.appendItemValue("date1", date1);
            doc.appendItemValue("date2", date2);
            List value1 = doc.getItemValue("date1");
            doc.save();
            List value2 = doc.getItemValue("date2");
            Calendar result1 = (Calendar) value1.get(0);
            Calendar result2 = (Calendar) value2.get(0);
            result1.getTime();
            result2.getTime();
            System.out.println(date1);
            System.out.println(result1);
            System.out.println(date2);
            System.out.println(result2);
            assertEquals("Got wrong date from document.", date2, result2);
            assertEquals("Got wrong date from document.", date1, result1);
        } catch (Exception e) {
            fail("Unexpected Exception. Maybe a recycle problem?");
            e.printStackTrace();
        }
        doc.remove(true);
    }

    /**
     * Tests send(String).
     */
    public void testDateTimeValues2() {
        System.out.println("-> testDateTimeValues");
        DDocument doc = getDatabase().createDocument();
        try {
            Calendar date1 = new GregorianDate(2004, 02, 11);
            Calendar date2 = new GregorianDate(2007, 07, 21);
            doc.appendItemValue("date1", date1);
            doc.appendItemValue("date2", date2);
            List value1 = doc.getItemValue("date1");
            doc.save();
            List value2 = doc.getItemValue("date2");
            Calendar result1 = (Calendar) value1.get(0);
            Calendar result2 = (Calendar) value2.get(0);
            result1.getTime();
            result2.getTime();
            System.out.println(date1);
            System.out.println(result1);
            System.out.println(date2);
            System.out.println(result2);
            assertEquals("Got wrong date from document.", date2, result2);
            assertEquals("Got wrong date from document.", date1, result1);
        } catch (Exception e) {
            fail("Unexpected Exception. Maybe a recycle problem?");
            e.printStackTrace();
        }
        doc.remove(true);
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
     * @see BaseDocumentProxyTest#setUpConcreteTest()
     */
    protected void setUpConcreteTest() {
    }
}
