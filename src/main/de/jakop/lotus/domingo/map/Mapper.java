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

package de.jakop.lotus.domingo.map;

import de.jakop.lotus.domingo.DDocument;

/**
 * Mapper interface for mappers between business objects and Notes documents.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public interface Mapper {

    /**
     * Maps a domingo document to a business object.
     *
     * @param document the domingo document to map
     * @param object the business object
     * @throws MappingException if an error occurred during mapping
     */
    void map(DDocument document, Object object) throws MappingException;

    /**
     * Maps a business object to a domingo document.
     *
     * @param object the business object
     * @param document the domingo document to map to
     * @throws MappingException if an error occurred during mapping
     */
    void map(Object object, DDocument document) throws MappingException;
}
