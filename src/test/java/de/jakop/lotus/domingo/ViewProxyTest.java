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
import de.jakop.lotus.domingo.util.GregorianDateTime;

/**
 * @author MarcusT
 */
public final class ViewProxyTest extends BaseProxyTest {

    private DDatabase newDB = null;
    private DView view = null;
    private DView newView = null;

    private Calendar correctNow = null;
    private String viewName = null;

    /**
     * @param name the name of the test
     */
    public ViewProxyTest(String name) {
        super(name);
    }

    /**
     * todo testRefresh (But how?). Just call the method and check for no exceptions.
     */
    public void testRefresh() {

    }

    /**
     * Tests the method getName().
     */
    public void testGetName() {
        System.out.println("-> ViewProxy.testGetName");
        String result = view.getName();
        assertTrue("View name should be '" + viewName + "' not '" + result + "'", viewName.equals(result));
    }

    /**
     * Tests <code>getAllDocumentsByKey</code> for Lists. Tests both methods
     * with and without boolean parameter.
     *
     */
    public void testGetAllDocumentsByKeyList() {
        System.out.println("-> testGetAllDocumentsByKeyList");

        List keys = new ArrayList();
        keys.add("somePath");
        keys.add(correctNow);
        keys.add("ObjStoreName");
        keys.add("DbName1");

        int docCounter = 0;
        Iterator it = newView.getAllDocumentsByKey(keys, true);
        while (it.hasNext()) {
            DDocument doc = (DDocument) it.next();
            doc.getNoteID();
            docCounter++;
        }
        assertEquals("View should have 2 documents.", 2, docCounter);

        keys = new ArrayList();
        keys.add("somePath");
        keys.add(correctNow);
        keys.add("ObjStoreName");
        // partial key should be in non-categorised column:
        keys.add("DbName");

        docCounter = 0;
        it = newView.getAllDocumentsByKey(keys, false);
        while (it.hasNext()) {
            DDocument doc = (DDocument) it.next();
            doc.getNoteID();
            docCounter++;
        }
        assertTrue("Found " + docCounter + " documents, but 4 expected.", 4 == docCounter);
    }

    /**
     * Tests a method to retrieve all documents that match a string in the
     * first column.
     */
    public void testGetAllDocumentsByKeyString() {
        System.out.println("-> testGetAllDocumentsByKeyString");

        String key = "somePath";

        int docCounter = 0;
        Iterator it = newView.getAllDocumentsByKey(key, true);
        while (it.hasNext()) {
            DDocument doc = (DDocument) it.next();
            doc.getNoteID();
            docCounter++;
        }
        assertEquals("View should have 4 documents.", 4, docCounter);

        // todo a test for a partial key is still required here
        // but it makes no sense in a categorized view, but that are the only
        // ones which are available in a database derived from log.ntf...
    }

    /**
     * Tests the retrieval of one suitable document by specifying a key list.
     */
    public void testGetDocumentByKeyList() {
        System.out.println("-> testGetDocumentByKeyList");

        List keys = new ArrayList();
        keys.add("somePath");
        keys.add(correctNow);
        keys.add("ObjStoreName");
        keys.add("DbName1");

        DDocument doc = newView.getDocumentByKey(keys, true);
        assertNotNull("The view should have a document with this key list.", doc);
        assertEquals("The Document should have a field 'DbName'.", "DbName1", doc.getItemValueString("DbName"));

        keys = new ArrayList();
        keys.add("somePath");
        keys.add(correctNow);
        keys.add("ObjStoreName");
        // partial key should be in non-categorised column:
        keys.add("DbName");

        DDocument doc1 = newView.getDocumentByKey(keys, false);
        assertNotNull("The view should have a document with this key list.", doc1);
        String fieldDbName = doc1.getItemValueString("DbName");
        assertTrue(
            "The Document should have a field 'DbName'.",
            ("DbName1".equals(fieldDbName) || "DbName2".equals(fieldDbName)));
    }

    /**
     * Tests the retrieval of one suitable document by specifying a key list.
     */
    public void testGetDocumentByKeyString() {
        System.out.println("-> testGetDocumentByKeyString");
        String key = "somePath";

        DDocument doc = newView.getDocumentByKey(key, true);
        assertNotNull("The view should contain a Document with that key.", doc);
        String fieldDBName = doc.getItemValueString("DbName");
        boolean match = "DbName1".equals(fieldDBName) || "DbName2".equals(fieldDBName);
        assertTrue("Document should have a field 'DbName'.", match);

        // todo a test for a partial key is still required here
        // but it makes no sense in a categorized view, but that are the only
        // ones which are available in a database derived from log.ntf...
    }

    /**
     * Tests the method getAllEntries.
     */
    public void testGetAllEntries() {
        System.out.println("-> testGetAllEntries");

        Iterator it = newView.getAllEntries();
        int itCounter = 0;
        while (it.hasNext()) {
            DViewEntry entry = (DViewEntry) it.next();
            assertNotNull("ViewEntry " + itCounter + " should not be null.", entry);
            itCounter++;
        }
        int expected = 4;
        assertEquals("The view should have " + expected + " entries.", expected, itCounter);
    }

    /**
     * Tests the method getAllEntries.
     */
    public void testGetAllEntriesFrom() {
        System.out.println("-> testGetAllEntriesFrom");

        Iterator it = newView.getAllEntries();
        int itCounter = 0;
        if (it.hasNext()) {
            DViewEntry entry = (DViewEntry) it.next();
            assertNotNull("ViewEntry " + itCounter + " should not be null.", entry);
            Iterator iter = newView.getAllEntries(entry);
            while (iter.hasNext()) {
                DViewEntry entry1 = (DViewEntry) iter.next();
                assertNotNull("ViewEntry " + itCounter + " should not be null.", entry1);
                if (entry1.isDocument()) {
                    itCounter++;
                }
            }
        }
        int expected = 4;
        assertEquals("The view should have " + expected + " entries.", expected, itCounter);
    }

    /**
     * Tests the method getAllEntriesByKey(String, boolean).
     */
    public void testGetAllEntriesByKeyString() {
        System.out.println("-> getAllEntriesByKeyString");

        String key = "somePath";
        Iterator it = newView.getAllEntriesByKey(key, true);
        int itCounter = 0;
        while (it.hasNext()) {
            DViewEntry entry = (DViewEntry) it.next();
            assertNotNull("ViewEntry " + itCounter + " should not be null.", entry);
            itCounter++;
        }
        int expected = 4;
        assertEquals("The view should have " + expected + " entries.", expected, itCounter);

        // todo some test for a partial key would be nice here
    }

    /**
     * Tests the method getAllEntriesByKey(List, boolean).
     */
    public void testGetAllCategories() {
        System.out.println("-> testGetAllCategories");
        DView categorisedView = getDatabase().getView("MiscEvents");
        Iterator iterator = categorisedView.getAllCategories(1);
        while (iterator.hasNext()) {
            DViewEntry entry = (DViewEntry) iterator.next();
            if (entry.isCategory()) {
                System.out.println("Category: " + entry.getColumnValues().toString());
            } else {
                System.out.println("    not a category");
            }
        }
    }

    /**
     * Tests the method getAllEntriesByKey(List, boolean).
     */
    public void testGetAllEntriesByKeyList() {
        System.out.println("-> getAllEntriesByKeyList");

        List list = new ArrayList();
        list.add("somePath");
        list.add(correctNow);
        list.add("ObjStoreName");
        list.add("DbName1");

        Iterator it = newView.getAllEntriesByKey(list, true);
        int itCounter = 0;
        while (it.hasNext()) {
            DViewEntry entry = (DViewEntry) it.next();
            assertNotNull("ViewEntry " + itCounter + " should not be null.", entry);
            itCounter++;
        }
        int expected = 2;
        assertEquals("The view should have " + expected + " entries.", expected, itCounter);

        list = new ArrayList();
        list.add("somePath");
        list.add(correctNow);
        list.add("ObjStoreName");
        list.add("DbName");

        it = newView.getAllEntriesByKey(list, false);
        itCounter = 0;
        while (it.hasNext()) {
            DViewEntry entry = (DViewEntry) it.next();
            assertNotNull("ViewEntry " + itCounter + " should not be null.", entry);
            itCounter++;
        }
        expected = 4;
        assertEquals("The view should have " + expected + " entries.", expected, itCounter);
    }

    /**
     * Excluded part of setUp, because not in every test, a new DB is necessary.
     * If this method were executed before every testMethod, each such
     * testMethod would have to call <code>newDB.remove()</code>, but this way,
     * only callers of this method need to call it.
     *
     * @param name the databases localhost filePath
     */
    private void createNewDBAndView(String name) {
        correctNow = new GregorianDate();

        DDatabase db = null;
        try {
            db = getSession().getDatabase("", "log.ntf");
        } catch (DNotesException e1) {
            e1.printStackTrace();
            fail("Cannot open local database log.ntf");
        }
        try {
            newDB = db.createDatabaseFromTemplate(getServerName(), name, true);
        } catch (DNotesException e) {
            assertTrue("Could not create DB from template.", false);
        }

        DDocument doc1 = newDB.createDocument();
        doc1.replaceItemValue("Form", "ObjStoreUsageForm");
        doc1.replaceItemValue("ObjStoreName", "ObjStoreName");
        doc1.replaceItemValue("DbName", "DbName1");
        doc1.replaceItemValue("DbTitle", "DbTitle");
        doc1.replaceItemValue("DocCount", 2);
        doc1.replaceItemValue("Server", "somePath");
        doc1.replaceItemValue("StartTime", correctNow);
        doc1.save();
        DDocument doc2 = newDB.createDocument();
        doc2.replaceItemValue("Form", "ObjStoreUsageForm");
        doc2.replaceItemValue("ObjStoreName", "ObjStoreName");
        doc2.replaceItemValue("DbName", "DbName1");
        doc2.replaceItemValue("DbTitle", "DbTitle");
        doc2.replaceItemValue("DocCount", 2);
        doc2.replaceItemValue("Server", "somePath");
        doc2.replaceItemValue("StartTime", correctNow);
        doc2.save();
        DDocument doc3 = newDB.createDocument();
        doc3.replaceItemValue("Form", "ObjStoreUsageForm");
        doc3.replaceItemValue("ObjStoreName", "ObjStoreName");
        doc3.replaceItemValue("DbName", "DbName2");
        doc3.replaceItemValue("DbTitle", "DbTitle");
        doc3.replaceItemValue("DocCount", 2);
        doc3.replaceItemValue("Server", "somePath");
        doc3.replaceItemValue("StartTime", correctNow);
        doc3.save();
        DDocument doc4 = newDB.createDocument();
        doc4.replaceItemValue("Form", "ObjStoreUsageForm");
        doc4.replaceItemValue("ObjStoreName", "ObjStoreName");
        doc4.replaceItemValue("DbName", "DbName2");
        doc4.replaceItemValue("DbTitle", "DbTitle");
        doc4.replaceItemValue("DocCount", 2);
        doc4.replaceItemValue("Server", "somePath");
        doc4.replaceItemValue("StartTime", correctNow);
        doc4.save();
        newView = newDB.getView("Object Store Usage");
    }

    /**
     * {@inheritDoc}
     * @see BaseProxyTest#setUpTest()
     */
    protected void setUpTest() {
        correctNow = new GregorianDateTime();
        viewName = "Miscellaneous Events";
        view = getDatabase().getView(viewName);
        createNewDBAndView("testViewProxy" + "_" + System.currentTimeMillis() + ".nsf");
    }

    /**
     * {@inheritDoc}
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        newDB.remove();
    }
}
