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

import java.util.HashMap;
import java.util.Map;

import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DViewEntry;

/**
 * Abstract base class for concrete databases.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public abstract class BaseDatabase {

    /** Reference to the notes database. */
    private DDatabase mDatabase;

    /** Reference to the mapping factory. */
    private MapperFactory mapperFactory = MapperFactory.newInstance();

    private Map persistence = new HashMap();

    /**
     * Constructor.
     *
     * @param session an existing domingo session
     * @param location location of database.
     * @throws DNotesException if the uri is invalid or the database cannot be
     *             opened
     */
    public BaseDatabase(final DSession session, final NotesLocation location) throws DNotesException {
        initDatabase(session, location);
        registerMappers();
    }

    /**
     * Constructor.
     *
     * @param locationUri URI of location of database.
     * @throws DNotesException if the uri is invalid or the database cannot be
     *             opened
     */
    public BaseDatabase(final String locationUri) throws DNotesException {
        this(NotesLocation.getInstance(locationUri));
    }

    /**
     * Constructor.
     *
     * @param location location of database.
     * @throws DNotesException if the uri is invalid or the database cannot be
     *             opened
     */
    public BaseDatabase(final NotesLocation location) throws DNotesException {
        final DSession session = getSession(location);
        initDatabase(session, location);
        registerMappers();
    }

    /**
     * Constructor.
     *
     * @param database a notes database
     * @throws DNotesException if the database is invalid or cannot be opened
     */
    public BaseDatabase(final DDatabase database) throws DNotesException {
        mDatabase = database;
        registerMappers();
    }

    /**
     * Creates and returns a new domingo session for a given location.
     *
     * @param location location of database.
     * @return domingo session
     * @throws DNotesException if the uri is invalid or the database cannot be
     *             opened
     */
    protected final DSession getSession(final NotesLocation location) throws DNotesException {
        final DNotesFactory factory = DNotesFactory.getInstance();
        if (location.isIIOP()) {
            final String host = location.getHost();
            final String user = location.getUsername();
            final String passwd = location.getPassword();
            return factory.getSession(host, user, passwd);
        } else if (location.isLocal()) {
            return factory.getSession();
        } else {
            throw new DNotesException("Invalid notes uri: " + location);
        }
    }

    /**
     * Initializes the database.
     */
    private void initDatabase(final DSession session, final NotesLocation location) throws DNotesException {
        final String path = location.getPath();
        if (location.isIIOP()) {
            mDatabase = session.getDatabase("", path);
        } else {
            final String server = location.getServer();
             mDatabase = session.getDatabase(server, path);
        }
    }

    /**
     * Implemented by derived class; must register all mappers.
     *
     * @throws MapperRegistrationException if an error occurred during
     *             registering a mapper
     */
    protected abstract void registerMappers() throws MapperRegistrationException;

    /**
     * Registers a mapper in the mapper factory.
     *
     * @param mapperClass the class of the new mapper
     * @throws MapperRegistrationException if an error occurred during
     *             registering a mapper
     */
    protected final void register(final Class mapperClass) throws MapperRegistrationException {
        mapperFactory.registerMapper(mapperClass);
    }

    /**
     * Returns the database specified by the current location.
     *
     * @return Notes database at current location
     */
    protected final DDatabase getDatabase() {
        return mDatabase;
    }

    /**
     * Creates a new instance of a business object with a given class.
     *
     * @param clazz class of business object
     * @return new business object
     */
    public final Object create(final Class clazz) {
        DMapper instanceMapper = mapperFactory.getInstanceMapper(clazz);
        return instanceMapper.newInstance();
    }

    /**
     * Saves a business object.
     *
     * @param object business object to save
     * @throws MappingException if an error occurred during mapping
     */
    public final void save(final Object object) throws MappingException {
        String universalId = (String) persistence.get(object);
        DDocument document = null;
        if (universalId != null) {
            document = mDatabase.getDocumentByUNID(universalId);
        }
        if (document == null) {
            document = mDatabase.createDocument();
        }
        DMapper mapper = mapperFactory.getInstanceMapper(object.getClass());
        mapper.map(object, document);
        document.save(true, false);
    }

    /**
     * Maps a business object to a domingo document.
     *
     * @param object the business object
     * @param document the domingo document to map to
     * @throws MappingException if an error occurred during mapping
     *
     * @see Mapper#map(java.lang.Object,
     *      DDocument)
     */
    public final void map(final Object object, final DDocument document) throws MappingException {
        DMapper mapper = mapperFactory.getInstanceMapper(object.getClass());
        mapper.map(object, document);
    }

    /**
     * Maps a domingo document to a business object.
     *
     * @param document the domingo document to map
     * @param object the business object
     * @throws MappingException if an error occurred during mapping
     *
     * @see Mapper#map(DDocument,
     *      java.lang.Object)
     */
    public final void map(final DDocument document, final Object object) throws MappingException {
        DMapper mapper = mapperFactory.getInstanceMapper(object.getClass());
        mapper.map(document, object);
    }

    /**
     * Maps a Domingo ViewEntry to a business object.
     *
     * @param viewEntry the domingo ViewEntry
     * @param object the business object
     * @throws MappingException if an error occurred during mapping
     *
     * @see DMapper#map(DViewEntry, Object)
     */
    public final void map(final DViewEntry viewEntry, final Object object) throws MappingException {
        DMapper mapper = mapperFactory.getDigestMapper(object.getClass());
        mapper.map(viewEntry, object);
    }

    /**
     * The title of a database.
     *
     * @return database title.
     *
     * @see DDatabase#getTitle()
     */
    public final String getTitle() {
        return mDatabase.getTitle();
    }
}
