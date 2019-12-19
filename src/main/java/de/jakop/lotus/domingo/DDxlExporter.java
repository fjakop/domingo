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

/**
 * Represents a Notes database.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DDxlExporter extends DBase {

    /**
     * Converts Domino data to DXL data.
     *
     * @param database The Domino data to be converted, in this case the entire database.
     * @return The exported DXL.
     * @throws DNotesException if the Domino data cannot be exported
     */
    String exportDxl(DDatabase database) throws DNotesException;

    /**
     * Converts Domino data to DXL data.
     *
     * @param document The Domino data to be converted, in this case one document.
     * @return The exported DXL.
     * @throws DNotesException if the Domino data cannot be exported
     */
    String exportDxl(DDocument document) throws DNotesException;

    /**
     * Converts Domino data to DXL data.
     *
     * @param documentCollection document The Domino data to be converted, in this case the documents in a document collection.
     * @return The exported DXL.
     * @throws DNotesException if the Domino data cannot be exported
     */
    String exportDxl(DDocumentCollection documentCollection) throws DNotesException;

    /**
     * Indicates whether a !DOCTYPE statement is exported or not.
     *
     * @return <code>true</code> (default) to export a !DOCTYPE statement
     */
    boolean getOutputDOCTYPE();

    /**
     * Indicates whether a !DOCTYPE statement is exported or not.
     *
     * @param flag <code>true</code> (default) to export a !DOCTYPE statement
     */
    void setOutputDoctype(boolean flag);

    /**
     * The value of SYSTEM in the exported !DOCTYPE statement.
     *
     * @return system doctype
     */
    String getDoctypeSYSTEM();

    /**
     * The value of SYSTEM in the exported !DOCTYPE statement.
     *
     * @param systemId system doctype
     */
    void setDoctypeSYSTEM(String systemId);

    /**
     * @param flag <code>true</code> to convert bit maps <code>false</code>
     *         (default) to leave bit maps in Notes format
     */
    void setConvertNotesBitmapsToGIF(final boolean flag);

    /**
     * Indicates whether bit maps pasted in rich text items should be converted
     * to GIF format.
     *
     * @return <code>true</code> to convert bit maps <code>false</code>
     *         (default) to leave bit maps in Notes format
     */
     boolean getConvertNotesBitmapsToGIF();
}
