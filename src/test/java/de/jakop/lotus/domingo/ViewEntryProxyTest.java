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
public final class ViewEntryProxyTest extends BaseProxyTest {

    private DDatabase newDB = null;

    private DView newView = null;

    private Calendar correctNow = null;

    /**
     * Creates a instance for all ViewEntryProxyTests.
     *
     * @param name the tests name
     */
    public ViewEntryProxyTest(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * @see BaseProxyTest#setUpTest()
     */
    protected void setUpTest() {
        createNewDBAndView("testViewEntryProxy" + "_" + System.currentTimeMillis() + ".nsf");
    }

    /**
     * {@inheritDoc}
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (newDB != null) {
            newDB.remove();
            newDB = null;
        }
    }

    /**
     * Tests the method getColumnValues.
     */
    public void testGetColumnValues() {
        System.out.println("-> testGetColumnValues");

        List list = new ArrayList();
        list.add("somePath");
        list.add(correctNow);
        list.add("ObjStoreName");
        list.add("DbName1");

        Iterator it = newView.getAllEntriesByKey(list, true);
        DViewEntry entry = (DViewEntry) it.next();
        List colVals = entry.getColumnValues();
        assertEquals("First ColumnValue does not match.", "somePath", colVals.get(0));
        assertEquals("Second ColumnValue does not match.", correctNow, colVals.get(1));
        assertEquals("Third ColumnValue does not match.", "ObjStoreName", colVals.get(2));
        assertEquals("Fourth ColumnValue does not match.", "DbName1", colVals.get(3));
        assertEquals("Fifth ColumnValue does not match.", "DbTitle", colVals.get(4));
    }

    /**
     * Tests the method getDocument.
     */
    public void testGetDocument() {
        List list = new ArrayList();
        list.add("somePath");
        list.add(correctNow);
        list.add("ObjStoreName");
        list.add("DbName1");

        Iterator it = newView.getAllEntriesByKey(list, true);
        DViewEntry entry = (DViewEntry) it.next();
        DDocument doc = entry.getDocument();
        assertNotNull("There should be a document.", doc);
        assertNotNull("The document also should have a UniId.", doc.getUniversalID());
    }

    /**
     * Tests the method isDocument.
     */
    public void testIsDocument() {
        List list = new ArrayList();
        list.add("somePath");
        list.add(correctNow);
        list.add("ObjStoreName");
        list.add("DbName1");

        Iterator it = newView.getAllEntriesByKey(list, true);
        DViewEntry entry = (DViewEntry) it.next();
        assertTrue("The ViewEntry should represent a document.", entry.isDocument());
    }

    /**
     * todo isCategory (but wait for ViewNavigator, or something alike).
     */
    public void testIsCategory() {

    }

    /**
     * todo isTotal (but wait for ViewNavigator, or something alike).
     */
    public void testIsTotal() {

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
        String dbName = name.replaceAll(".nsf", "_" + System.currentTimeMillis() + ".nsf");

        correctNow = new GregorianDate();

        DDatabase db = null;
        try {
            db = getSession().getDatabase(getServerName(), "log.ntf");
        } catch (DNotesException e1) {
            e1.printStackTrace();
            fail("Cannot open database log.ntf");
        }
        try {
            newDB = db.createDatabaseFromTemplate(getServerName(), dbName, true);
        } catch (DNotesException e) {
            assertTrue("Could not create DB from template.", false);
        }
        assertNotNull("bd is null", newDB);
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
}
