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

import java.io.File;
import java.io.IOException;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class EmbeddedObjectProxyTest extends BaseProxyTest {

    private String itemName;

    private DDocument doc;
    private File attachFile;
    private DRichTextItem rTitem;

    /**
     * @param name the tests name
     */
    public EmbeddedObjectProxyTest(String name) {
        super(name);
    }

    /**
     * Tests extractFile.
     */
    public void testExtractFile() {
        System.out.println("-> EmbeddedObjectProxy.testExtractFile");
        String path = attachFile.getAbsolutePath();
        rTitem.embedAttachment(path);
        doc.save();
        DEmbeddedObject received = rTitem.getEmbeddedAttachment(attachFile.getName());
        assertNotNull("Embedded object to detach not found", received);

        String extractPath = "c:\\temp\\" + System.currentTimeMillis() + "extractFile.txt";

        received.extractFile(extractPath);

        File extracted = new File(extractPath);
        assertTrue("Extracted File should exist.", extracted.exists());

        extracted.delete();
        attachFile.delete();
    }

    /**
     * Embeds a file, retrieves the Embedded object and equals its name with
     * the placed name.
     */
    public void testGetName() {
        System.out.println("-> EmbeddedObjectProxy.testGetName");
        String path = attachFile.getAbsolutePath();
        DEmbeddedObject embedded = rTitem.embedAttachment(path);
        doc.save();
        DEmbeddedObject received = rTitem.getEmbeddedAttachment(attachFile.getName());
        assertNotNull("Embedded object not found", received);
        assertEquals(
            "The content (name) of the embedded object should be equal to the given name.",
            attachFile.getName(),
            embedded.getName());
        assertEquals(
            "The content (name) of both embedded objects should be equal.",
            embedded.getName(),
            received.getName());

        attachFile.delete();
    }

    /**
     * Creates and removes an attached file.
     */
    public void testRemove() {
        System.out.println("-> EmbeddedObjectProxy.testRemove");

        String path = attachFile.getAbsolutePath();
        rTitem.embedAttachment(path);
        doc.save();
        DEmbeddedObject rec1 = rTitem.getEmbeddedAttachment(attachFile.getName());
        assertNotNull("Embedded object not found", rec1);
        assertEquals("This operation should result in the embedded's name.", attachFile.getName(), rec1.getName());
        rec1.remove();

        attachFile.delete();
    }

    /**
     * Sets up instance variables that are used in most of the tests.
     */
    public void setUpTest() {
        itemName = "some_item_that_does_surely_not_exist";

        String suf = System.currentTimeMillis() + "testEmbedAndGetter";
        String pre = ".txt";
        attachFile = null;
        try {
            attachFile = File.createTempFile(suf, pre);
        } catch (IOException e) {
            assertTrue("Could not create test attachment file.", false);
        }

        doc = getDatabase().createDocument();
        rTitem = doc.createRichTextItem(itemName);
    }
}
