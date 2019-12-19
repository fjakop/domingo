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

import java.util.Iterator;
import java.util.List;

/**
 * @author MarcusT
 */
public final class DatabaseProxyTest extends BaseProxyTest {

    /**
     * @param name the name of the test
     */
    public DatabaseProxyTest(String name) {
        super(name);
    }

    /**
     * Setup test case.
     */
    public void setUpTest() {
    }

    /**
     * test replicate.
     */
    public void testReplicate() {
        // todo test replicate
    }

    /**
     * test createReplica.
     */
    public void testCreateReplica() {
        System.out.println("-> testCreateReplica");
        String replicaName = "testLog_" + System.currentTimeMillis() + ".nsf";
        DDatabase replica = null;
        try {
            replica = getDatabase().createReplica(getServerName(), replicaName);
        } catch (DNotesException e1) {
            e1.printStackTrace();
            fail("Cannot create replica: " + replicaName);
        }
        replica.remove();
    }

    /**
     * Tests retrieval of Documents in a newly created database that is removed
     * after tests.
     */
    public void testGetAllDocuments() {
        System.out.println("-> testGetAllDocuments");

        String dbName = "testGetAllDocuments_" + System.currentTimeMillis() + ".nsf";
        DDatabase newDB = getSession().createDatabase(getServerName(), dbName);

        DDocument doc1 = newDB.createDocument();
        doc1.replaceItemValue("item1", "value1");
        doc1.replaceItemValue("item2", "value2");
        doc1.save();
        DDocument doc2 = newDB.createDocument();
        doc2.replaceItemValue("item3", "value3");
        doc2.replaceItemValue("item4", "value4");
        doc2.save();

        int docCounter = 0;
        Iterator it = getDatabase().getAllDocuments();
        for (int i = 0; i < 2; i++) {
            DDocument doc = (DDocument) it.next();
            assertNotNull("At least two documents should be in database, but no " + (2 - docCounter) + " is missing.", doc);
            if (doc != null) {
                docCounter++;
            }
        }

        assertTrue("Database should hold at least 2 documents.", 2 == docCounter);

        newDB.remove();
    }

    /**
     * Tests creation of a database from the Notes log database template.
     */
    public void testCreateDatabaseFromTemplate() {
        System.out.println("-> testCreateDatabaseFromTemplate");

        DDatabase template = null;
        try {
            template = getSession().getDatabase(getServerName(), "log.ntf");
        } catch (DNotesException e) {
            e.printStackTrace();
            fail("Cannot open local database log.ntf");
        }
        String newName = "testCreateDatabaseFromTemplate_" + System.currentTimeMillis() + ".nsf";
        DDatabase newDB = null;
        try {
            System.out.println("   template.createDatabaseFromTemplate");
            newDB = template.createDatabaseFromTemplate(getServerName(), newName, true);
        } catch (DNotesException e) {
            assertTrue("Database could not be created: " + e, false);
        }
        assertNotNull("Database not created.", newDB);
        System.out.println("   db created");

        assertTrue("Databases name does not match.", newDB.getFilePath().endsWith(newName));

        String viewName = "Database\\Sizes";
        DView view = newDB.getView(viewName);
        assertNotNull("The received view should not be NULL.", view);
        assertEquals("The views name does not match.", viewName, view.getName());

        DDatabase newDB2 = null;
        try {
            newDB2 = template.createDatabaseFromTemplate(getServerName(), newName, true);
        } catch (DNotesException e) {
            System.out.println("As expected: " + e.getMessage());
        }
        assertNull("Database should NOT have been created: ", newDB2);

        newDB.remove();
    }

    /**
     * Tests removing of a database.
     */
    public void testRemove() {
        System.out.println("-> DatabaseProxy.testRemove");

        String dbName = "testRemove_" + System.currentTimeMillis() + ".nsf";
        DDatabase lDatabase = getSession().createDatabase(getServerName(), dbName);

        lDatabase.remove();

        //        DDocument doc = lDatabase.createDocument();
        //        assertNull("Document should not have been created.", doc);
    }

    /**
     * Tests the creation of documents.
     */
    public void testCreateDocument() {
        System.out.println("-> testCreateDocument");
        DDocument doc = getDatabase().createDocument();
        assertTrue("Document should be new.", doc.isNewNote());
    }

    /**
     * Tests the retrieval of documents by their UnId.
     */
    public void testGetDocumentByUNID() {
        System.out.println("-> testGetDocumentByUNID");
        DDocument doc = getDatabase().createDocument();
        doc.save();
        DDocument newDoc = getDatabase().getDocumentByUNID(doc.getUniversalID());
        assertEquals("UniversalIds must be equal", doc.getUniversalID(), newDoc.getUniversalID());
        newDoc.remove(true);
    }

    /**
     * Tests the retrieval of documents by their NoteId.
     */
    public void testGetDocumentByID() {
        System.out.println("-> testGetDocumentByID");
        DDocument doc = getDatabase().createDocument();
        doc.save();
        DDocument newDoc = getDatabase().getDocumentByID(doc.getNoteID());
        assertEquals("NoteIds must be equal", doc.getNoteID(), newDoc.getNoteID());
        newDoc.remove(true);
    }

    /**
     * Tests the retrieval of the databases filename.
     */
    public void testGetFileName() {
        System.out.println("-> testGetFileName");
        String fileName = getDatabase().getFileName();
        String path = getDatabase().getFilePath();
        assertTrue("The fileName should be equals to the end of the file path", path.endsWith(fileName));
    }

    /**
     * Tests the retrieval of the databases file path.
     */
    public void testGetFilePath() {
        System.out.println("-> testGetFilePath");
        String path = getDatabase().getFilePath();
        assertTrue("At least the database file should exist.", path != null && path.length() > 0);
    }

    /**
     * Tests the retrieval of the databases filename.
     */
    public void testGetCurrentAccessLevel() {
        System.out.println("-> testGetCurrentAccessLevel");
        int level = getDatabase().getCurrentAccessLevel();
        assertTrue("Illegal access level", DACL.LEVEL_NOACCESS <= level && level <= DACL.LEVEL_MANAGER);
    }

    /**
     * Tests creation or retrieval of profile documents.
     */
    public void testGetProfileDocument() {
        System.out.println("-> testGetProfileDocument");
        String profile = "some_profile_name";
        String profileKey = "some_Profile_Key";
        DProfileDocument pDoc = getDatabase().getProfileDocument(profile, profileKey);
        assertNotNull("Document should note be null.", pDoc);
    }

    /**
     * Tests accessing views.
     */
    public void testGetView() {
        System.out.println("-> testGetView");
        String viewName = "Database\\Sizes";
        DView view = getDatabase().getView(viewName);
        assertTrue(
            "Each Notes log DB (log.nsf) should have a view '" + viewName + ", but this DB has not: " + view,
            view != null && viewName.equals(view.getName()));
    }

    /**
     * Tests accessing agents.
     */
    public void testGetAgent() {
        System.out.println("-> testGetAgent");
        String agentName = "Copy to Personal Address Book";
        DDatabase nab = null;
        try {
            nab = getSession().getDatabase(getServerName(), "names.nsf");
        } catch (DNotesException e) {
            e.printStackTrace();
            fail("Cannot open local names.nsf");
        }
        DAgent agent = nab.getAgent(agentName);
        assertTrue(
            "Each Notes NAB (names.nsf) should have an agent '" + agentName + ", but this DB has not.",
            agent != null && agentName.equals(agent.getName()));
    }

    /**
     * Tests getting calendar values from a document that is taken via view.getAllDocuments().
     */
    public void testAllDocs() {
        DDocument doc = null;
        try {
            DDatabase nab = getSession().getDatabase("", "names.nsf");
            DView users = nab.getView("($Users)");
            Iterator allDocs = users.getAllDocuments();
            doc = (DDocument) allDocs.next();
        } catch (DNotesException e) {
            e.printStackTrace();
            fail("Cannot open local names.nsf");
        }
        try {
            List itemValue = doc.getItemValue("$Revisions");
            System.out.println(itemValue);
        } catch (DNotesRuntimeException e) {
            e.printStackTrace();
            fail("Cannot get date/time.");
        }
    }
}
