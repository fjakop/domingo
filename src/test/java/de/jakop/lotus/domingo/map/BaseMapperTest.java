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

import junit.framework.TestCase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.exception.ExceptionUtil;

/**
 * Base class for mapping tests.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class BaseMapperTest extends TestCase {

    /**
     * Maps a document to a business object with a given mapper.
     * Fails in sense of JUnit if the mapping cannot be performed.
     *
     * @param mapper the mapper
     * @param doc the document
     * @param instance the business object
     */
    protected final void map(final Mapper mapper, final DDocument doc, final Object instance) {
        try {
            mapper.map(doc, instance);
        } catch (MappingException e) {
            e.printStackTrace();
            fail("Cannot map document to instance: " + e.getMessage());
        }
    }

    /**
     * Maps a document to a business object with a given mapper.
     * Fails in sense of JUnit if the mapping cannot be performed.
     *
     * @param mapper the mapper
     * @param doc the document
     * @param instance the business object
     */
    protected final void map(final Mapper mapper, final Object instance, final DDocument doc) {
        try {
            mapper.map(instance, doc);
        } catch (MappingException e) {
            e.printStackTrace();
            fail("Cannot map instance to document: " + e.getMessage());
        }
    }

    /**
     * Indicates failure with a specific exception.
     * The stack trace of the exception is part of the message.
     *
     * @param message failure message
     * @param t throwable causing the failure
     */
    protected final void fail(final String message, final Throwable t) {
        fail(message + ": " + ExceptionUtil.getStackTrace(t));
    }
}
