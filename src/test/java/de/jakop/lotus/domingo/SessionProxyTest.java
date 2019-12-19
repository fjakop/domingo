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

import java.util.List;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class SessionProxyTest extends BaseProxyTest {

        /**
     * @param name the name of the test
     */
    public SessionProxyTest(String name) {
        super(name);
    }


    /**
     * {@inheritDoc}
     * @see BaseProxyTest#setUpTest()
     */
    public void setUpTest() {
    }

    /**
     * Tests the ability to create a database.
     */
    public void testCreateDatabase() {
        System.out.println("-> testCreateDatabase");

        String dbName = "testCreateDatabase_" + System.currentTimeMillis() + ".nsf";
        DDatabase database = getSession().createDatabase(getServerName(), dbName);
        assertNotNull("Database file should exist.", database);

        DDocument doc = database.createDocument();
        assertTrue("Document should have been saved.", doc.save());

        database.remove();
    }

    /**
     * Tests a session to provide existing databases.
     */
    public void testGetDatabase() {
        String databaseName = "log.nsf";
        DDatabase database = null;
        try {
            database = getSession().getDatabase(getServerName(), databaseName);
        } catch (DNotesException e) {
            fail("Cannot open database " + getServerName() + "!!" + databaseName, e);
        }
        assertNotNull("database not found", database);
        assertTrue("Wrong path/filename", database.getFilePath().endsWith(databaseName));
    }

    /**
     * Tests a session to return the user's name.
     */
    public void testGetUserName() {
        System.out.println("-> testGetUserName");
        String userName = getSession().getUserName();
        assertTrue("At least a string longer than 0 should be returned.", userName != null && userName.length() > 0);
    }

    /**
     * Tests a session to return the common user's name.
     */
    public void testGetCommonUserName() {
        System.out.println("-> testGetCommonUserName");
        String userName = getSession().getCommonUserName();
        assertTrue("At least a string longer than 0 should be returned.", userName != null && userName.length() > 0);
    }

    /**
     * Tests a session to return the common user's name.
     */
    public void testGetCanonicalUserName() {
        System.out.println("-> testGetCanonicalUserName");
        String userName = getSession().getCanonicalUserName();
        assertTrue("At least a string longer than 0 should be returned.", userName != null && userName.length() > 0);
    }

    /**
     * Tests a session to return the common user's name.
     */
    public void testIsOnServer() {
        System.out.println("-> testIsOnServer");
        boolean isOnServer = getSession().isOnServer();
        if (isDIIOP()) {
            assertTrue("Method isOnServer Should return true.", isOnServer);
        } else {
            assertTrue("Method isOnServer Should return false.", !isOnServer);
        }
    }

    /**
     * Tests getEnvironmentString(String).
     */
    public void testGetEnvironmentString() {
        System.out.println("-> testGetEnvironmentString");
        String env = "Directory";
        getSession().setEnvironmentString(env, "XXX", false);
        String value = getSession().getEnvironmentString(env);
        assertTrue("Environment not found: " + env, value != null && ((String) value).length() > 0);
    }

    /**
     * Tests getEnvironmentValue(String).
     */
    public void testGetEnvironmentValue() {
        System.out.println("-> testGetEnvironmentString");
        String env = "Directory";
        getSession().setEnvironmentString(env, "XXX", false);
        Object value = getSession().getEnvironmentValue(env);
        assertTrue(value instanceof String);
        assertTrue("Environment not found: " + env, value != null && ((String) value).length() > 0);
    }

    /**
     * Tests getEnvironmentString(String, boolean).
     */
    public void testGetEnvironmentStringFalse() {
        System.out.println("-> testGetEnvironmentString");
        String env = "Directory";
        String value = getSession().getEnvironmentString(env, false);
        assertTrue("Environment not found: " + env, value != null && ((String) value).length() > 0);
    }

    /**
     * Tests getEnvironmentValue(String, boolean).
     */
    public void testGetEnvironmentValueFalse() {
        System.out.println("-> testGetEnvironmentString");
        String env = "Directory";
        Object value = getSession().getEnvironmentValue(env, false);
        assertTrue(value instanceof String);
        assertTrue("Environment not found: " + env, value != null && ((String) value).length() > 0);
    }

    /**
     * Tests evaluate method.
     */
    public void testEvaluate() {
        System.out.println("-> testEvaluate");
        Object obj = null;
        try {
            obj = getSession().evaluate("\"A\": \"B\": \"C\"");
        } catch (DNotesException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
        assertTrue(obj instanceof List);
        List list = (List) obj;
        assertTrue(list.size() == 3);
        assertTrue("A".equals(list.get(0)));
        assertTrue("B".equals(list.get(1)));
        assertTrue("C".equals(list.get(2)));

        DDatabase db = null;
        DDocument doc = null;

        doc = (DDocument) getDatabase().getAllDocuments().next();
        try {
            obj = getSession().evaluate("@UserNamesList", doc);
            System.out.println("result: " + obj);
        } catch (DNotesException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }

        String databaseName = "log.nsf";
        try {
            db = getSession().getDatabase("", databaseName);
        } catch (DNotesException e1) {
            e1.printStackTrace();
            fail("Cannot open local database " + databaseName);
        }
        doc = (DDocument) db.getAllDocuments().next();
        try {
            obj = getSession().evaluate("@UserNamesList", doc);
            System.out.println("result: " + obj);
        } catch (DNotesException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
        assertTrue(obj instanceof List);

        assertTrue(obj instanceof List);
    }

    /**
     * Tests createLog(String).
     */
    public void testCreateLog() {
        System.out.println("-> testCreateLog");
        DLog log = getSession().createLog("Domingo_Log");
        log.openNotesLog(getServerName(), "log.nsf");
        log.logAction("log something");
    }
}
