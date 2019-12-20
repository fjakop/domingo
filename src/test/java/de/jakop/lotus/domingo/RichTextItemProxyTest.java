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
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Test for RichText items.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class RichTextItemProxyTest extends BaseItemProxyTest {

    /**
     * @param name the tests name
     */
    public RichTextItemProxyTest(String name) {
        super(name);
    }

    /**
     * Tests embedding an object.
     */
    public void testEmbedAndGetAttachment() {
        System.out.println("-> testEmbedAndGetAttachment");

        String suf = System.currentTimeMillis() + "testEmbedAndGetAttachment";
        String pre = ".txt";
        File file = null;
        try {
            file = File.createTempFile(suf, pre);
        } catch (IOException e) {
            assertTrue("Could not create test attachment file.", false);
        }

        DDocument doc = getDatabase().createDocument();
        DRichTextItem rTitem = doc.createRichTextItem(getItemName());
        DEmbeddedObject embedded = rTitem.embedAttachment(file.getAbsolutePath());
        doc.save();
        DEmbeddedObject received = rTitem.getEmbeddedAttachment(file.getName());
        assertNotNull("No attachment found", received);
        assertEquals(
            "The content (name) of the embedded object should be equal to the given name.",
            file.getName(),
            embedded.getName());
        assertEquals(
            "The content (name) of both embedded objects should be equal.",
            embedded.getName(),
            received.getName());

        file.delete();
        doc.remove(true);
    }

    /**
     * Tests RichTextItem.getEmbeddedObjects().
     */
    public void testGetEmbeddedObjects() {
        System.out.println("-> testGetEmbeddedObjects");

        DBaseDocument doc = getDatabase().createDocument();
        DRichTextItem rtItem1 = doc.createRichTextItem(getItemName());

        String pre1 = System.currentTimeMillis() + "testGetEmbeddedObjects1";
        String pre2 = System.currentTimeMillis() + "testGetEmbeddedObjects2";
        String suf = ".txt";
        File file1 = null;
        File file2 = null;
        try {
            file1 = File.createTempFile(pre1, suf);
            file2 = File.createTempFile(pre2, suf);
        } catch (IOException e) {
            assertTrue("Could not create test attachment file.", false);
        }

        String key1 = file1.getAbsolutePath();
        String key2 = file2.getAbsolutePath();

        rtItem1.embedAttachment(key1);
        rtItem1.embedAttachment(key2);

        doc.save();

        Iterator it = rtItem1.getEmbeddedObjects();

        int eOCounter = 0;
        while (it.hasNext()) {
            DEmbeddedObject eo = (DEmbeddedObject) it.next();
            eOCounter++;
            assertNotNull("The embedded object should not be null.", eo);
            boolean ok1 = file1.getName().equals(eo.getName());
            boolean ok2 = file2.getName().equals(eo.getName());
            assertTrue("The name of the embedded object should be the same as the name at embedding.", ok1 || ok2);
        }
        assertEquals("There should be two embedded objects.", 2, eOCounter);

        file1.delete();
        file2.delete();
        doc.remove(true);

    }

    /**
     * Tests RichTextItem.getUnformattedText().
     */
    public void testGetUnformattedText() {
        System.out.println("-> testGetUnformattedText");

        DBaseDocument doc = getDatabase().createDocument();
        DRichTextItem rtItem = doc.createRichTextItem(getItemName());
        String val = "This text is stored in a RichTextItem to test the method RichTextItem.getUnformattedText()";
        rtItem.appendText(val);
        doc.save();

        DRichTextItem rtItem2 = (DRichTextItem) doc.getFirstItem(getItemName());
        String comp = rtItem2.getUnformattedText();

        assertEquals(val, comp);
        doc.removeItem(getItemName());
        rtItem = doc.createRichTextItem(getItemName());
        rtItem.appendText(getBigText());

        rtItem2 = (DRichTextItem) doc.getFirstItem(getItemName());
        comp = rtItem2.getUnformattedText();
        String compare = replaceAll(replaceAll(comp, "\n", ""), "\r", "");
        assertEquals(getBigText(), compare);
        doc.remove(true);
    }

    /**
     * Replaces all occurances of a substring in a string with another substring.
     */
    private static String replaceAll(final String text, final String repl, final String with) {
        if (text == null || repl == null || with == null || repl.length() == 0) {
            return text;
        }
        StringBuffer buf = new StringBuffer(text.length());
        int start = 0;
        int end = 0;
        do {
            end = text.indexOf(repl, start);
            if (end == -1) {
                break;
            }
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();
        } while (true);
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * Tests formatting methods of RichTextItem.
     */
    public void testFormats() {
        System.out.println("-> testFormats");

        DBaseDocument doc = getDatabase().createDocument();
        DRichTextItem rtItem1 = doc.createRichTextItem(getItemName());

        rtItem1.appendText("some text");
        rtItem1.addNewLine();
        rtItem1.appendText("some text");
        rtItem1.addNewLine(2);
        rtItem1.appendText("some text");
        rtItem1.addNewLine(2, true);
        rtItem1.appendText("some text");
        rtItem1.addNewLine(2, false);
        rtItem1.appendText("some text");
        rtItem1.addTab();
        rtItem1.appendText("some text");
        rtItem1.addTab(2);
        rtItem1.appendText("some text");
        doc.save();
        doc.remove(true);
    }

    /**
     * @see BaseItemProxyTest#createDBaseItem()
     * {@inheritDoc}
     */
    protected DBaseItem createDBaseItem() {
        DDocument doc = getDatabase().createDocument();
        DRichTextItem item = doc.createRichTextItem(getItemName());
        return item;
    }

    /**
     * Returns a very long string.
     * @return a very long string
     */
    private String getBigText() {
        ResourceBundle bundle = ResourceBundle.getBundle("de.jakop.lotus.domingo.test-values");
        return bundle.getString("value.string.long");
    }
}
