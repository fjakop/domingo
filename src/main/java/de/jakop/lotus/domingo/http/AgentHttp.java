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

import java.util.Calendar;

import de.jakop.lotus.domingo.DAgent;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class AgentHttp extends BaseHttp implements DAgent {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 6380752153299895522L;

    /** The name of the agent. */
    private String fName;

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param monitor the monitor
     * @param agentName the name of the agent
     */
    public AgentHttp(final NotesHttpFactory theFactory, final DBase theParent, final DNotesMonitor monitor,
            final String agentName) {
        super(theFactory, theParent, monitor);
        this.fName = agentName;
    }

    /**
     * Factory method for instances of this class.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent the session that produced the database
     * @param monitor the monitor that handles logging
     * @param agentName name of a Notes agent
     *
     * @return Returns a DDatabase instance of type DatabaseProxy
     */
    static AgentHttp getInstance(final NotesHttpFactory theFactory, final DBase theParent, final DNotesMonitor monitor,
            final String agentName) {
        return new AgentHttp(theFactory, theParent, monitor, agentName);
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getComment()
     */
    public String getComment() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getCommonOwner()
     */
    public String getCommonOwner() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#isEnabled()
     */
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#setEnabled(boolean)
     */
    public void setEnabled(final boolean enabled) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#isNotesAgent()
     */
    public boolean isNotesAgent() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#isPublic()
     */
    public boolean isPublic() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#isWebAgent()
     */
    public boolean isWebAgent() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getLastRun()
     */
    public Calendar getLastRun() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getName()
     */
    public String getName() {
        return fName;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getOwner()
     */
    public String getOwner() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getParameterDocID()
     */
    public String getParameterDocID() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getQuery()
     */
    public String getQuery() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getServerName()
     */
    public String getServerName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#setServerName(java.lang.String)
     */
    public void setServerName(final String serverName) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getTarget()
     */
    public int getTarget() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#getTrigger()
     */
    public int getTrigger() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#remove()
     */
    public void remove() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#run()
     */
    public void run() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#run(java.lang.String)
     */
    public void run(final String noteId) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#runOnServer()
     */
    public int runOnServer() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#runOnServer(java.lang.String)
     */
    public int runOnServer(final String noteId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see DAgent#save()
     */
    public void save() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        // TODO Auto-generated method stub
        return fName;
    }
}
