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

import java.io.Serializable;

/**
 * Base interface for all concrete notes interfaces.
 *
 * <p>The Base class defines methods that are common to all the classes.
 * User code should not directly access the Base class.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DBase extends Serializable {

    /**
     * Returns a short description of an instance.
     *
     * @return short description of an instance
     */
    String toString();

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param   object the reference object with which to compare.
     * @return  <code>true</code> if this object is the same as the object
     *          argument; <code>false</code> otherwise.
     * @see     #hashCode()
     * @see     java.lang.Object#hashCode()
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable
     */
    boolean equals(Object object);

    /**
     * Returns a hash code value for the object.
     *
     * @return  a hash code value for this object.
     * @see     #equals(java.lang.Object)
     * @see     java.lang.Object#hashCode()
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable
     */
    int hashCode();
}
