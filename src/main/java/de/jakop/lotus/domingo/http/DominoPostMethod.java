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

import org.apache.commons.httpclient.methods.PostMethod;

/**
 * An Http POST method to a Lotus Domino server.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoPostMethod extends PostMethod implements DominoHttpMethod {

    /**
     * Private constructor; use {@ink #GetInstance(String)} to create new
     * instances of this class.
     *
     * @param uri the URI of the method
     */
    private DominoPostMethod(final String uri) {
        super(uri);
    }

    /**
     * Returns a new instance of this class.
     *
     * @param uri the URI of the method
     * @return a new instance
     */
    static DominoPostMethod getInstance(final String uri) {
        return new DominoPostMethod(uri);
    }

    /**
     * {@inheritDoc}
     *
     * @see DominoHttpMethod#getResponseBodyString()
     */
    public String getResponseBodyString() {
        // TODO Auto-generated method stub
        return null;
    }
}
