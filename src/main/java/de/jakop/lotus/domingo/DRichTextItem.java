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

import java.util.Iterator;

/**
 * Represents an item of type rich text.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DRichTextItem extends DBaseItem {

    /**
     * Embeds an object or an attachment. It can be retrieved later by use
     * of the pure filename (without path) of the attached file.
     *
     * Calls the notes method
     * <code>embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", source, "")</code>
     *
     * @param path source name of the object
     * @return DEmbeddedObject
     */
    DEmbeddedObject embedAttachment(String path);

    /**
     * Given the name of a file attachment in a rich-text item,
     * returns the corresponding EmbeddedObject.
     * <p><b>Note</b>
     *   getEmbeddedObject is not supported under OS/2, under UNIX, and on the
     * Macintosh.</p>
     *
     * @param fileName the embedded attachments filename
     * @return the embedded object
     */
    DEmbeddedObject getEmbeddedAttachment(String fileName);

    /**
     * All the embedded objects, object links, and file attachments contained
     * in a rich text item.
     *
     * If you need access to OLE/2 embedded objects that exist in a document
     * but are not part of a rich text item (for example, because the object
     * was originally created on the document's form), use the EmbeddedObjects
     * property in Document.
     *
     * <p><b>Note</b>
     * Embedded objects and object links are not supported for OS/2, UNIX, and
     * the Macintosh. File attachments are.</p>
     *
     * @return Iterator an iterator supplying all embedded objects
     */
    Iterator getEmbeddedObjects();

    /**
     * Appends text to the end of a rich text item. The text is rendered in the
     * current style of the item.
     *
     * @param text The text to append.
     */
    void appendText(String text);

    /**
     * Appends one or more new lines (carriage returns) to the end of a
     * rich-text item.
     */
    void addNewLine();

    /**
     * Appends one or more new lines (carriage returns) to the end of a
     * rich-text item.
     *
     * @param n The number of new lines to append.
     */
    void addNewLine(int n);

    /**
     * Appends one or more new lines (carriage returns) to the end of a
     * rich-text item.
     *
     * @param n The number of new lines to append.
     * @param newparagraph If true (default), forces the new line to be a
     *                     paragraph separator. If false, the new line is added,
     *                     but does not force a new paragraph.
     */
    void addNewLine(int n, boolean newparagraph);

    /**
     * Appends one or more tabs to the end of a rich-text item.
     */
    void addTab();

    /**
     * Appends one or more tabs to the end of a rich-text item.
     *
     * @param count The number of tabs to append.
     */
    void addTab(int count);

    /**
     * Returns the text of the item without any formatting.
     *
     * @return String
     */
    String getUnformattedText();
}
