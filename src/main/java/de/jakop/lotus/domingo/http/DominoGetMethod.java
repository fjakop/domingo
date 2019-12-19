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

package de.jakop.lotus.domingo.http;

import java.io.IOException;

import de.jakop.lotus.domingo.util.XMLUtil;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * An Http GET method to a Lotus Domino server.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoGetMethod extends GetMethod implements DominoHttpMethod {

    /**
     * Private constructor; use {@ink #GetInstance(String)} to create new
     * instances of this class.
     *
     * @param uri the URI of the method
     */
    private DominoGetMethod(final String uri) {
        super(uri);
    }

    /**
     * Returns a new instance of this class.
     *
     * @param uri the URI of the method
     * @return a new instance
     */
    static DominoGetMethod getInstance(final String uri) {
        return new DominoGetMethod(uri);
    }

    /**
     * {@inheritDoc}
     * @throws IOException if the response cannot be read
     *
     * @see DominoHttpMethod#getResponseBodyString()
     */
    public String getResponseBodyString() throws IOException  {
        Header contentTypeHeader = getResponseHeader("Content-Type");
        String contentType = contentTypeHeader == null ? "text/html charset=UTF-8" : contentTypeHeader.getValue();
        if (contentType.startsWith("text/html")) {
            return getResponseBodyAsString();
        }
        if (contentType.startsWith("text/xml")) {
            return XMLUtil.parse(getResponseBody());
        }
        return getResponseBodyAsString();
    }
}
