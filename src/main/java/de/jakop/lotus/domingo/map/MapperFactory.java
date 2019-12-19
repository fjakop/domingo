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

package de.jakop.lotus.domingo.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for mapper objects.
 *
 * @see DMapper
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class MapperFactory {

    /** Maps instance classes to their mappers. */
    private Map instanceMappers = new HashMap();

    /** Maps digest classes to their mappers. */
    private Map digestMappers = new HashMap();

    /**
     * Creates and returns a new instance of this mapper factory.
     *
     * @return a new instance of this class
     */
    public static MapperFactory newInstance() {
        return new MapperFactory();
    }

    /**
     * private Constructor; use {@link #newInstance()} to get an instance of
     * this class.
     */
    private MapperFactory() {
    }

    /**
     * Registers a new mapper.
     *
     * @param mapperClass the class of the new mapper
     * @throws MapperRegistrationException if the mapper cannot get registered
     */
    protected void registerMapper(final Class mapperClass) throws MapperRegistrationException {
        if (mapperClass == null) {
            throw new MapperRegistrationException("mapper class is null");
        }
        DMapper mapper = null;
        try {
            mapper = (DMapper) mapperClass.newInstance();
        } catch (Exception e) {
            throw new MapperRegistrationException("Cannot instantiate mapper of class " + mapperClass.getName(), e);
        }
        instanceMappers.put(mapper.getInstanceClass(), mapper);
        digestMappers.put(mapper.getDigestClass(), mapper);
    }

    /**
     * Returns the mapper that is registered for a given instance class.
     *
     * @param clazz the instance class
     * @return mapper for that class
     */
    public DMapper getInstanceMapper(final Class clazz) {
        DMapper mapper = (DMapper) instanceMappers.get(clazz);
        if (mapper == null) {
            Class[] interfaces = clazz.getInterfaces();
            for (int i = 0; mapper == null && i < interfaces.length; i++) {
                mapper = (DMapper) instanceMappers.get(interfaces[i]);
            }
        }
        return mapper;
    }

    /**
     * Returns the mapper that is registered for a given digest class.
     *
     * @param clazz the instance class
     * @return mapper for that class
     */
    public DMapper getDigestMapper(final Class clazz) {
        return (DMapper) digestMappers.get(clazz);
    }
}
