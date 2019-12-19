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

/**
 * Optional abstract base class for digest classes. Implementations of digest
 * classes that inherit from this base class have access to internal properties
 * of the view column from which they where created. <p>If digest classes that
 * don't inherit from this base class need access to such properties (e.g. the
 * UniversalId of the document), they can also do this in the concrete mapper,
 * but it is recommended to use this base class.</p>
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class BaseDigest {

    /** The universalId of the notes document. */
    private String universalId;

    /**
     * @return Returns the unid.
     */
    public final String getUnid() {
        return universalId;
    }

    /**
     * @param unid The unid to set.
     */
    public final void setUnid(final String unid) {
        this.universalId = unid;
    }
}
