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

/**
 * Represents an embedded object.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DEmbeddedObject extends DBase {

    /** Type of embedded object: object link. */
    int EMBED_OBJECTLINK = 1452;

    /** Type of embedded object: object. */
    int EMBED_OBJECT = 1453;

    /** Type of embedded object: attachment. */
    int EMBED_ATTACHMENT = 1454;

    /**
     * Writes a file attachment to storage. For embedded objects and object
     * links, this method logs a warning.
     *
     * @param path The path and file name where you want to store the extracted
     *            file.
     */
    void extractFile(String path);

    /**
     * Removes an object, object link, or file attachment.
     */
    void remove();

    /**
     * The name used to reference an file attachment (or object, object link).
     *
     * @return name of embedded object
     */
    String getName();

    /**
     * For an object or object link, returns the internal name for the source
     * document. For a file attachment, returns the file name of the original
     * file.
     *
     * @return  internal name for the source document (objects or object links)
     *          or file name of the original file (file attachments).
     */
    String getSource();

    /**
     * Read-only. Indicates whether an embedded object is an object, an object
     * link, or a file attachment.
     *
     * @return type of the embedded object (one of
     *         <tt>{@link DEmbeddedObject#EMBED_OBJECT}</tt>,
     *         <tt>{@link DEmbeddedObject#EMBED_OBJECTLINK}</tt> or
     *         <tt>{@link DEmbeddedObject#EMBED_ATTACHMENT}</tt>)
     */
    int getType();
}
