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

import lotus.domino.Agent;
import lotus.domino.DateTime;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DAgent;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Represents a notes agent.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class AgentProxy extends BaseProxy implements DAgent {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257562923407520313L;

    /** The name of the agent. */
    private final String name;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent
     * @param agent the notes agent object
     * @param monitor the monitor
     */
    public AgentProxy(final NotesProxyFactory theFactory, final DBase theParent,
                      final Agent agent, final DNotesMonitor monitor) {
        super(theFactory, theParent, agent, monitor);
        try {
            name = agent.getName();
        } catch (Throwable t) {
            throw new NullPointerException(RESOURCES.getString("agent.error"));
        }
    }

    /**
     * Factory method for instances of this class.
     *
     * @param theFactory the controlling factory
     * @param database the database that contains the agent
     * @param theAgent Notes agent object
     * @param monitor the monitor that handles logging
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    public static DAgent getInstance(final NotesProxyFactory theFactory, final DatabaseProxy database,
                                     final Agent theAgent, final DNotesMonitor monitor) {
        if (theAgent == null) {
            return null;
        }
        AgentProxy agent = (AgentProxy) theFactory.getBaseCache().get(theAgent);
        if (agent == null) {
            agent = new AgentProxy(theFactory, database, theAgent, monitor);
            theFactory.getBaseCache().put(theAgent, agent);
        }
        return agent;
    }

    /** Returns the associated notes agent object.
     *
     * @return associated notes agent object
     */
    private Agent getAgent() {
        return (Agent) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getComment()
     */
    public String getComment() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getComment();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString(RESOURCES.getString("agent.cannot.get.comment")), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getCommonOwner()
     */
    public String getCommonOwner() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getCommonOwner();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.common.owner"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#isEnabled()
     */
    public boolean isEnabled() {
        getFactory().preprocessMethod();
        try {
            return getAgent().isEnabled();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.check.enabled"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#setEnabled(boolean)
     */
    public void setEnabled(final boolean enabled) {
        getFactory().preprocessMethod();
        try {
            getAgent().setEnabled(enabled);
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.enable"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#isNotesAgent()
     */
    public boolean isNotesAgent() {
        getFactory().preprocessMethod();
        try {
            return getAgent().isNotesAgent();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.check.notes"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#isPublic()
     */
    public boolean isPublic() {
        getFactory().preprocessMethod();
        try {
            return getAgent().isPublic();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.check.public"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#isWebAgent()
     */
    public boolean isWebAgent() {
        getFactory().preprocessMethod();
        try {
            return getAgent().isWebAgent();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.enabled"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getLastRun()
     */
    public Calendar getLastRun() {
        getFactory().preprocessMethod();
        try {
            final DateTime dateTime = getAgent().getLastRun();
            final Calendar lastRun = createCalendar(dateTime);
            dateTime.recycle();
            return lastRun;
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.lastrun"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getName()
     */
    public String getName() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getName();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.name"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getOwner()
     */
    public String getOwner() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getOwner();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.owner"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getParameterDocID()
     */
    public String getParameterDocID() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getParameterDocID();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.noteid"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getQuery()
     */
    public String getQuery() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getQuery();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.query"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getServerName()
     */
    public String getServerName() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getServerName();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.server"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#setServerName(java.lang.String)
     */
    public void setServerName(final String serverName) {
        getFactory().preprocessMethod();
        try {
            getAgent().setServerName(serverName);
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.set.server"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getTarget()
     */
    public int getTarget() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getTarget();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.target"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#getTrigger()
     */
    public int getTrigger() {
        getFactory().preprocessMethod();
        try {
            return getAgent().getTrigger();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.get.trigger"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#remove()
     */
    public void remove() {
        getFactory().preprocessMethod();
        try {
            getAgent().remove();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.remove.agent"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#run()
     */
    public void run() {
        getFactory().preprocessMethod();
        try {
            getAgent().run();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.run.agent"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#run(java.lang.String)
     */
    public void run(final String noteId) {
        getFactory().preprocessMethod();
        try {
            getAgent().run(noteId);
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.run.agent"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#runOnServer()
     */
    public int runOnServer() {
        getFactory().preprocessMethod();
        try {
            return getAgent().runOnServer();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.run.on.server"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#runOnServer(java.lang.String)
     */
    public int runOnServer(final String noteId) {
        getFactory().preprocessMethod();
        try {
            return getAgent().runOnServer(noteId);
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.run.on.server"), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DAgent#save()
     */
    public void save() {
        getFactory().preprocessMethod();
        try {
            getAgent().save();
        } catch (NotesException e) {
            throw newRuntimeException(RESOURCES.getString("agent.cannot.save"), e);
        }
    }

    /**
     * @see BaseProxy#toString()
     * @return the name of the agent
     */
    public String toString() {
        return name;
    }
}
