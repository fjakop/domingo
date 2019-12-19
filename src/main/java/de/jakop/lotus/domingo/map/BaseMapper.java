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
 * Abstract base class for domingo mappers.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class BaseMapper extends MapperSet implements DMapper {

    /** empty array of objects, used as empty arguments list for reflection. */
    public static final Object[] EMPTY_ARGS = {};

    /** empty array of classes, used as empty class list for reflection. */
    public static final Class[] EMPTY_PARAMS = {};

    /** Class of instances of the business objects. */
    private Class instanceClass;

    /** Class of instances of digests of the business objects. */
    private Class digestClass;

    /**
     * Constructor. The class of instances of digest of the business objects is
     * assumed to be the same as the instance class.
     *
     * @param instanceClass the class of instance of the business objects
     */
    public BaseMapper(final Class instanceClass) {
        this(instanceClass, instanceClass);
    }

    /**
     * Constructor.
     *
     * @param instanceClass the class of instance of the business objects
     * @param digestClass the class of instance of digests of the business
     *            objects
     */
    public BaseMapper(final Class instanceClass, final Class digestClass) {
        super();
        this.instanceClass = instanceClass;
        this.digestClass = digestClass;
    }

    /**
     * {@inheritDoc}
     * @see DMapper#getInstanceClass()
     */
    public final Class getInstanceClass() {
        return instanceClass;
    }

    /**
     * {@inheritDoc}
     * @see DMapper#getDigestClass()
     */
    public final Class getDigestClass() {
        return digestClass;
    }
}
