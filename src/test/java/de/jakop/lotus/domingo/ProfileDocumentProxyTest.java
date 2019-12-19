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

import java.util.Random;

/**
 * @author MarcusT
 */
public final class ProfileDocumentProxyTest extends BaseDocumentProxyTest {

    private final Random random;

    private String profileName;
    private String profileKey;

    /**
     * @param name the name of the test
     */
    public ProfileDocumentProxyTest(String name) {
        super(name);
        random = new Random();
    }

    /**
     * Tests a profile document to return its name.
     */
    public void testGetNameOfProfile() {
        System.out.println("-> testGetNameOfProfile");
        String profileNameG = "GN" + System.currentTimeMillis();
        String profileKeyG = "GN" + System.currentTimeMillis();
        DProfileDocument doc = getDatabase().getProfileDocument(profileNameG, profileKeyG);

        String key = doc.getNameOfProfile();
        assertEquals("The keys should be equal.", profileKeyG, key);
    }

    /**
     * Tests a profile document to return its key.
     */
    public void testGetKey() {
        System.out.println("-> testGetKey");

        String profileNameG = "G" + System.currentTimeMillis();
        String profileKeyG = "G" + System.currentTimeMillis();
        DProfileDocument doc = getDatabase().getProfileDocument(profileNameG, profileKeyG);

        String key = doc.getKey();
        assertEquals("The keys should be equal.", profileKeyG, key);
    }

    /**
     * Tests a profile document to its ability to save itself.
     *
     */
    public void testSave() {
        System.out.println("-> testSave");

        String profileNameS = "B" + System.currentTimeMillis();
        String profileKeyS = "Key" + System.currentTimeMillis();
        DProfileDocument doc = getDatabase().getProfileDocument(profileNameS, profileKeyS);
        String someValue = "someValueForTestSave";
        doc.appendItemValue(getItemName(), someValue);
        doc.save();

        DProfileDocument received = getDatabase().getProfileDocument(profileNameS, profileKeyS);
        String receivedValue = received.getItemValueString(getItemName());
        assertEquals(
            "The Document received from the database should contain an item, but does not.",
            someValue,
            receivedValue);

        doc.remove(true);
    }

    /**
     * Tests save(boolean).
     */
    public void testSaveWithOneArgument() {
        System.out.println("-> testSaveWithOneArgument");

        String profileNameE = "S_bool_" + System.currentTimeMillis();
        String profileKeyE = "K_S_bool" + System.currentTimeMillis();
        DProfileDocument doc = getDatabase().getProfileDocument(profileNameE, profileKeyE);

        String someValue = "someValueForTestSave";
        doc.appendItemValue(getItemName(), someValue);
        boolean saved = doc.save(true);
        assertTrue("The save action should work properly.", saved);

        doc.appendItemValue(getItemName() + getItemName(), someValue);
        saved = doc.save(false);
        assertTrue("The unforced save action should work properly.", saved);

        DProfileDocument received = getDatabase().getProfileDocument(profileNameE, profileKeyE);
        assertNotNull("The received document should exist.", received);
        String receivedValue = received.getItemValueString(getItemName());
        assertEquals("Document should contain an item, but does not.", someValue, receivedValue);

        String receivedValue1 = received.getItemValueString(getItemName() + getItemName());
        assertEquals("Document should contain a second item, but does not.", someValue, receivedValue1);

        doc.remove(true);
    }

    /**
     * Tests save(boolean, boolean).
     */
    public void testSaveWithTwoArguments() {
        System.out.println("-> testSaveWithTwoArguments");

        String profileNameE = "S_boolbool_" + System.currentTimeMillis();
        String profileKeyE = "K_S_boolbool" + System.currentTimeMillis();
        DProfileDocument doc = getDatabase().getProfileDocument(profileNameE, profileKeyE);

        String someValue = "someValueForTestSave";
        doc.appendItemValue(getItemName(), someValue);
        boolean saved = doc.save(true, false);
        assertTrue("The save action should work properly.", saved);

        doc.appendItemValue(getItemName() + getItemName(), someValue);
        saved = doc.save(true, true);
        assertTrue("The unforced save action should work properly.", saved);

        DProfileDocument received = getDatabase().getProfileDocument(profileNameE, profileKeyE);
        assertNotNull("The received document should exist.", received);
        String receivedValue = received.getItemValueString(getItemName());
        assertEquals("Document should contain an item, but does not.", someValue, receivedValue);

        String receivedValue1 = received.getItemValueString(getItemName() + getItemName());
        assertEquals("Document should contain a second item, but does not.", someValue, receivedValue1);

        doc.remove(true);
    }

    /**
     * Tests a profile document on its equality to others.
     *
     */
    public void testEquals() {
        System.out.println("-> testEquals");

        String profileNameE = "A" + System.currentTimeMillis();
        String profileKeyE = "K" + System.currentTimeMillis();
        DProfileDocument doc = getDatabase().getProfileDocument(profileNameE, profileKeyE);
        doc.replaceItemValue("Test", "abcdefgh");
        doc.save();
        DProfileDocument receivedDoc = getDatabase().getProfileDocument(profileNameE, profileKeyE);
        assertEquals("Documents should be equal but are not.", "abcdefgh", receivedDoc.getItemValueString("Test"));

        DBaseDocument anotherOne = createDBaseDocument();
        assertTrue("Documents should NOT be equal but are.", !doc.equals(anotherOne));

        doc.remove(true);
    }

    /**
     * @see BaseDocumentProxyTest#createDBaseDocument()
     * {@inheritDoc}
     */
    protected DBaseDocument createDBaseDocument() {
        profileName = createStringKey();
        profileKey = createStringKey();
        return getDatabase().getProfileDocument(profileName, profileKey);
    }

    /**
     * Creates a unique key by current time in milliseconds and a concatenated
     * random number string from 0 to 10000.
     * @return A String that should suit as unique key
     */
    private String createStringKey() {
        return "" + System.currentTimeMillis() + random.nextInt(10000);
    }

    /**
     * Sets up the proper test environment for each test again.
     * Sets: profileName, profileKey
     *
     * {@inheritDoc}
     * @see BaseDocumentProxyTest#setUpConcreteTest()
     */
    public void setUpConcreteTest() {
        profileName = "someProfileName";
        profileKey = "someProfileKey";
    }
}
