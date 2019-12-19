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

package de.jakop.lotus.domingo.proxy;

import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.DxlExporter;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DDocumentCollection;
import de.jakop.lotus.domingo.DDxlExporter;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Represents a view entry. A view entry describes a row in a view.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DxlExporterProxy extends BaseProxy implements DDxlExporter {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3978430204404511538L;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param viewEntry a view entry
     * @param monitor the monitor
     */
    private DxlExporterProxy(final NotesProxyFactory theFactory, final DBase theParent,
                           final DxlExporter exporter, final DNotesMonitor monitor) {
        super(theFactory, theParent, exporter, monitor);
    }

    /**
     * Creates an encapsulated notes session object.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param exporter the Notes ViewEntry
     * @param monitor the monitor
     * @return a session object
     */
    public static DxlExporterProxy getInstance(final NotesProxyFactory theFactory,  final DBase parent,
                                             final DxlExporter exporter, final DNotesMonitor monitor) {
        if (exporter == null) {
            return null;
        }
        return new DxlExporterProxy(theFactory, parent, exporter, monitor);
    }

    /**
     * Returns the associated Notes view entry.
     *
     * @return associated notes view entry
     */
    private DxlExporter getDxlExporter() {
        return (DxlExporter) getNotesObject();
    }

    /**
     * @see java.lang.Object#toString()
     * @return "DxlExporterProxy"
     */
    public String toString() {
        return "DxlExporterProxy";
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#exportDxl(DDatabase)
     */
    public String exportDxl(final DDatabase database) throws DNotesException {
        if (!(database instanceof BaseProxy)) {
            throw newRuntimeException("Cannot export DXL", new ClassCastException(database.getClass().getName()));
        }
        try {
            final Base notesObject = ((BaseProxy) database).getNotesObject();
            return this.getDxlExporter().exportDxl((Database) notesObject);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot export DXL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#exportDxl(DDocument)
     */
    public String exportDxl(final DDocument document) throws DNotesException {
        if (!(document instanceof BaseProxy)) {
            throw newRuntimeException("Cannot rexport DXL", new ClassCastException(document.getClass().getName()));
        }
        try {
            final Base notesObject = ((BaseProxy) document).getNotesObject();
            return this.getDxlExporter().exportDxl((Document) notesObject);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set systemIdexporter", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#exportDxl(DDocumentCollection)
     */
    public String exportDxl(final DDocumentCollection documentCollection) throws DNotesException {
        if (!(documentCollection instanceof BaseProxy)) {
            throw newRuntimeException("Cannot rexport DXL", new ClassCastException(documentCollection.getClass().getName()));
        }
        try {
            final Base notesObject = ((BaseProxy) documentCollection).getNotesObject();
            return this.getDxlExporter().exportDxl((DocumentCollection) notesObject);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot export DXL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#getOutputDOCTYPE()
     */
    public boolean getOutputDOCTYPE() {
        try {
            return this.getDxlExporter().getOutputDOCTYPE();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get output systemId", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#setOutputDoctype(boolean)
     */
    public void setOutputDoctype(final boolean flag) {
        try {
            this.getDxlExporter().setOutputDOCTYPE(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set output systemId", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#getDoctypeSYSTEM()
     */
    public String getDoctypeSYSTEM() {
        try {
            return this.getDxlExporter().getDoctypeSYSTEM();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get systemIdexporter", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#setDoctypeSYSTEM(java.lang.String)
     */
    public void setDoctypeSYSTEM(final String systemId) {
        try {
            this.getDxlExporter().setDoctypeSYSTEM(systemId);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set systemIdexporter", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#setDoctypeSYSTEM(java.lang.String)
     */
    public void setConvertNotesBitmapsToGIF(final boolean flag) {
        try {
            this.getDxlExporter().setConvertNotesBitmapsToGIF(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set systemIdexporter", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DDxlExporter#getConvertNotesBitmapsToGIF()
     */
    public boolean getConvertNotesBitmapsToGIF() {
        try {
            return this.getDxlExporter().getConvertNotesBitmapsToGIF();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set systemIdexporter", e);
        }
    }
}
