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

import java.util.Calendar;
import java.util.Iterator;

import lotus.domino.Agent;
import lotus.domino.AgentContext;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DAgent;
import de.jakop.lotus.domingo.DAgentContext;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DSession;

/**
 * Represents the agent environment of the current program, if an agent is running it.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class AgentContextProxy extends BaseProxy implements DAgentContext {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257288032582580016L;

    /**
     * Creates a new instance of this class.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param object the notes object
     * @param monitor the monitor
     */
    private AgentContextProxy(final NotesProxyFactory theFactory, final DBase theParent, final AgentContext object,
            final DNotesMonitor monitor) {
        super(theFactory, theParent, object, monitor);
    }

    /**
     * Factory method for instances of this class.
     *
     * @param theFactory the controlling factory
     * @param session the session that produced the database
     * @param theAgentContext Notes agent context object
     * @param monitor the monitor that handles logging
     * @return Returns instance of DAgentContext
     */
    public static DAgentContext getInstance(final NotesProxyFactory theFactory, final SessionProxy session,
            final AgentContext theAgentContext, final DNotesMonitor monitor) {
        if (theAgentContext == null) {
            return null;
        }
        final AgentContextProxy agentContextProxy = new AgentContextProxy(theFactory, session, theAgentContext, monitor);
        return agentContextProxy;
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#updateProcessedDoc(DDocument)
     */
    public void updateProcessedDoc(final DDocument document) {
        Document notesDocument = (Document) ((DocumentProxy) document).getNotesObject();
        try {
            getAgentContext().updateProcessedDoc(notesDocument);
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.mark.processed"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#unprocessedFTSearch(java.lang.String, int)
     */
    public Iterator unprocessedFTSearch(final String query, final int maxDocs) {
        try {
            final DocumentCollection collection = getAgentContext().unprocessedFTSearch(query, maxDocs);
            return new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.ftsearchunprocessed"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#unprocessedFTSearch(java.lang.String, int, int, int)
     */
    public Iterator unprocessedFTSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt) {
        try {
            final DocumentCollection collection = getAgentContext().unprocessedFTSearch(query, maxDocs, sortOpt, otherOpt);
            return new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.ftsearchunprocessed"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#unprocessedSearch(java.lang.String, java.util.Calendar, int)
     */
    public Iterator unprocessedSearch(final String query, final Calendar dateTime, final int maxDocs) {
        try {
            final DateTime notesDateTime = createDateTime(dateTime);
            final DocumentCollection collection = getAgentContext().unprocessedSearch(query, notesDateTime, maxDocs);
            getFactory().recycle(dateTime);
            return new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.searchunprocessed"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#getEffectiveUserName()
     */
    public String getEffectiveUserName() {
        try {
            return getAgentContext().getEffectiveUserName();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.get.effectiveusername"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#getCurrentAgent()
     */
    public DAgent getCurrentAgent() {
        try {
            final DatabaseProxy database = (DatabaseProxy) getCurrentDatabase();
            final Agent currentAgent = getAgentContext().getCurrentAgent();
            return AgentProxy.getInstance(getFactory(), database, currentAgent, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.get.currentagent"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#getCurrentDatabase()
     */
    public DDatabase getCurrentDatabase() {
        try {
            final DSession session = (DSession) getParent();
            final Database currentDatabase = getAgentContext().getCurrentDatabase();
            return DatabaseProxy.getInstance(getFactory(), session, currentDatabase, getMonitor(), true);
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.get.currentdatabase"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#getDocumentContext()
     */
    public DDocument getDocumentContext() {
        try {
            final DatabaseProxy database = (DatabaseProxy) getCurrentDatabase();
            final Document documentContext = getAgentContext().getDocumentContext();
            return (DDocument) BaseDocumentProxy.getInstance(getFactory(), database, documentContext, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.get.documentcontext"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#getLastExitStatus()
     */
    public int getLastExitStatus() {
        try {
            return getAgentContext().getLastExitStatus();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.get.last.exitstatus"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#getLastRun()
     */
    public Calendar getLastRun() {
        try {
            final DateTime dateTime = getAgentContext().getLastRun();
            if (dateTime != null) {
                final Calendar calendar = createCalendar(dateTime);
                getFactory().recycle(dateTime);
                dateTime.recycle();
                return calendar;
            }
            return null;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.get.lastrun"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#getSavedData()
     */
    public DDocument getSavedData() {
        try {
            final Document savedData = getAgentContext().getSavedData();
            return (DDocument) BaseDocumentProxy.getInstance(getFactory(), getCurrentDatabase(), savedData, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.get.saveddata"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgentContext#getUnprocessedDocuments()
     */
    public Iterator getUnprocessedDocuments() {
        try {
            final DocumentCollection collection = getAgentContext().getUnprocessedDocuments();
            return new DocumentCollectionIterator(getFactory(), this, collection, getMonitor());
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.context.cannot.searchunprocessed"), e);
        }
    }

    /**
     * @see java.lang.Object#toString()
     * @return "[DAgentContext]"
     */
    public String toString() {
        return "[DAgentContext]";
    }

    /**
     * Returns the associated Notes agent context.
     *
     * @return associated Notes agent context
     */
    private AgentContext getAgentContext() {
        getFactory().preprocessMethod();
        return (AgentContext) this.getNotesObject();
    }
}
