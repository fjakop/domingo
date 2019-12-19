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

package de.jakop.lotus.domingo.http;

import java.io.IOException;

import org.apache.commons.httpclient.HttpMethod;

/**
 * An Http method to a Lotus Domino server.
 * This is only a markup interface to prevent from using unsupported methods.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DominoHttpMethod extends HttpMethod {

    /**
     * Returns the response body as a string.
     *
     * This method respects the encoding of the response body of the content
     * types <tt>text/html</tt> and <tt>text/xml</tt>:
     *
     * <dl>
     * <dt><tt>text/html</tt></dt><dd>name of charset (encoding) is read from the <tt>Content-Type</tt> header</dd>
     * <dt><tt>text/xml</tt></dt><dd>name of charset (encoding) is read from the XML tag in the response body</dd>
     * </dl>
     *
     * @return response as String with proper encoding
     * @throws IOException if the response cannot be read
     */
    String getResponseBodyString() throws IOException;
}
