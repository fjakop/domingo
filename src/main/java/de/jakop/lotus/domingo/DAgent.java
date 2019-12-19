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

package de.jakop.lotus.domingo;

import java.util.Calendar;

/**
 * Represents notes agents.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DAgent extends DBase {

    /** Trigger constant: NONE. */
    int TRIGGER_NONE = 0;

    /** Trigger constant: TRIGGER_SCHEDULED. */
    int TRIGGER_SCHEDULED = 1;

    /** Trigger constant: TRIGGER_AFTER_MAIL_DELIVERY. */
    int TRIGGER_AFTER_MAIL_DELIVERY = 2;

    /** Trigger constant: TRIGGER_DOC_PASTED. */
    int TRIGGER_DOC_PASTED = 3;

    /** Trigger constant: TRIGGER_MANUAL. */
    int TRIGGER_MANUAL = 4;

    /** Trigger constant: TRIGGER_DOC_UPDATE. */
    int TRIGGER_DOC_UPDATE = 5;

    /** Trigger constant: TRIGGER_BEFORE_MAIL_DELIVERY. */
    int TRIGGER_BEFORE_MAIL_DELIVERY = 6;

    /** Target constant: TARGET_NONE. */
    int TARGET_NONE = 0;

    /** Target constant: TARGET_ALL_DOCS. */
    int TARGET_ALL_DOCS = 1;

    /** Target constant: TARGET_NEW_DOCS. */
    int TARGET_NEW_DOCS = 2;

    /** Target constant: TARGET_NEW_OR_MODIFIED_DOCS. */
    int TARGET_NEW_OR_MODIFIED_DOCS = 3;

    /** Target constant: TARGET_SELECTED_DOCS. */
    int TARGET_SELECTED_DOCS = 4;

    /** Target constant: TARGET_ALL_DOCS_IN_VIEW. */
    int TARGET_ALL_DOCS_IN_VIEW = 5;

    /** Target constant: TARGET_UNREAD_DOCS_IN_VIEW. */
    int TARGET_UNREAD_DOCS_IN_VIEW = 6;

    /** Target constant: TARGET_RUN_ONCE. */
    int TARGET_RUN_ONCE = 8;

    /** Return value of method runOnServer if an exception occurred. */
    int ERROR_RUN = 1000;

    /**
     * The comment that describes an agent, as entered by the agent's designer.
     *
     * @return the comment that describes an agent, as entered by the agent's
     *         designer
     */
    String getComment();

    /**
     * The common name of the person who last modified and saved an agent.
     *
     * @return the common name of the person who last modified and saved an agent
     */
    String getCommonOwner();

    /**
     * Indicates if an agent is able to run or not.
     *
     * <p><b>Usage</b></p>
     * <p>This property is intended for use with scheduled agents, which can
     * be enabled and disabled. This property always returns true for hidden
     * agents and agents that are run from a menu.</p>
     * <p>You must call Save to make any change effective.</p>
     * <p>If the agent is open in the UI, a change is not immediately
     * reflected. The agent must be closed and reopened.</p>
     *
     * @return <code>true</code> if the agent can be run, else <code>false</code>
     */
    boolean isEnabled();

    /**
     * Indicates if an agent is able to run or not.
     *
     * <p><b>Usage</b></p>
     * <p>This property is intended for use with scheduled agents, which can
     * be enabled and disabled. This property always returns true for hidden
     * agents and agents that are run from a menu.</p>
     * <p>You must call Save to make any change effective.</p>
     * <p>If the agent is open in the UI, a change is not immediately
     * reflected. The agent must be closed and reopened.</p>
     *
     * @param enabled <code>true</code> if the agent can be run, else <code>false</code>
     */
    void setEnabled(boolean enabled);

    /**
     * Indicates if an agent can run in the Notes client environment.
     *
     * @return <code>true</code> if the agent can run in the Notes client environment, else <code>false</code>
     */
    boolean isNotesAgent();

    /**
     * Indicates if an agent is public or personal.
     *
     * @return <code>true</code> if an agent is public, else <code>false</code>.
     */
    boolean isPublic();

    /**
     * Indicates if an agent can run in a Web browser environment.
     *
     * @return <code>true</code> if an agent can run in a Web browser environment, else <code>false</code>.
     */
    boolean isWebAgent();

    /**
     * The date that an agent last ran.
     *
     * @return the date that an agent last ran
     */
    Calendar getLastRun();

    /**
     * The name of an agent. Within a database, the name of an agent may not be unique.
     *
     * @return the name of an agent
     */
    String getName();

    /**
     * The name of the person who last modified and saved an agent.
     *
     * <p><b>Usage</b></p>
     * <p>If the owner's name is hierarchical, this property returns the fully
     * distinguished name.</p>
     * <p>Saving the agent changes the owner immediately. However, if you
     * subsequently call Agent.owner within the same Session, the previous
     * owner's name will be returned. The ownership change is not reflected
     * in properties until the next time a Session is obtained.</p>
     *
     * @return the name of the person who last modified and saved an agent
     */
    String getOwner();

    /**
     * Returns the NoteID of a document passed in by Run or RunOnServer.
     *
     * <p><b>Usage</b></p>
     * <p>Use getDocumentByID in Database to get a document through its NoteID.</p>
     *
     * @return the NoteID of a document passed in by Run or RunOnServer
     */
    String getParameterDocID();

    /**
     * The text of the query used by an agent to select documents.
     * <p>In the Agent Builder, a query is defined by the searches
     * added to the agent using the Add Search button.</p>
     * <p>If no query is defined with the Add Search button, the Query
     * property returns an empty string, even if the agent runs a formula
     * that has its own SELECT statement or a script that selects specific
     * documents.</p>
     *
     * @return the text of the query used by an agent to select documents
     */
    String getQuery();

    /**
     * The name of the server on which an agent runs.
     *
     * @return the name of the server on which an agent runs
     */
    String getServerName();

    /**
     * The name of the server on which an agent runs.
     * <p><b>Usage</b></p>
     * <p>The value returned by getServerName depends upon whether the agent is scheduled:
     * <ul>
     * <li>If the agent is scheduled, the property returns the name of the
     * server that the scheduled agent runs on. Since scheduled agents can
     * only run on a single replica of a database, you designate a server
     * name for the agent under Schedule in the Agent Builder. Therefore,
     * the ServerName property may represent the parent database's server,
     * or it may represent a replica's server.</li>
     * <li>If the agent is not scheduled, this property returns an empty
     * string.</li>
     * <li>You can set ServerName to the asterisk (*) to indicate that the
     * agent can run on any server.</li>
     * <li>A null ServerName means the local workstation.</li>
     * <li>You must call Save to make any change effective.</li></ul></p>
     *
     * @param serverName the name of the server on which the agent should runs
     */
    void setServerName(String serverName);

    /**
     * Indicates which documents this agent acts on.
     *
     * <p><b>Usage</b></p>
     * <p>This property corresponds to "Which document(s) should it act on?"
     * in the Designer UI for agents. The trigger limits the target
     * possibilities. The TARGET_NONE targets are the only possibilities for
     * their corresponding triggers.</p>
     *
     * <p><b>Legal values</b>
     * <ul>
     * <li>Agent.TARGET_ALL_DOCS ("All documents in database")</li>
     * <li>Agent.TARGET_ALL_DOCS_IN_VIEW ("All documents in view")</li>
     * <li>Agent.TARGET_NEW_DOCS (Not used)</li>
     * <li>Agent.TARGET_NEW_OR_MODIFIED_DOCS ("All new and modified documents
     * since last run")</li>
     * <li>Agent.TARGET_NONE ("Each incoming mail document," "Newly received
     * mail documents," "Newly modified documents," "Pasted documents")</li>
     * <li>Agent.TARGET_SELECTED_DOCS ("Selected documents")</li>
     * <li>Agent.TARGET_UNREAD_DOCS_IN_VIEW ("All unread documents in view")</li>
     * <li>Agent.TARGET_RUN_ONCE ("Run once")</li></ul></p>
     *
     * @return indicates which documents this agent acts on
     */
    int getTarget();

    /**
     * Indicates when this agent runs.
     *
     * @return indicates when this agent runs
     */
    int getTrigger();

    /**
     * Permanently deletes an agent from a database.
     */
    void remove();

    /**
     * Runs the agent.
     *
     * <p><b>Usage</b></p>
     * <p>You cannot run an agent recursively (cannot call it from itself).</p>
     * <p>The user cannot interact directly with a called agent. User output
     * goes to the Domino log.</p>
     * <p>You cannot debug a called agent.</p>
     * <p>For local operations, the agent runs on the computer running the
     * current program. See runOnServer to do otherwise.</p>
     * <p>For remote (IIOP) operations, the agent runs on the server handling
     * the remote calls.</p>
     */
    void run();

    /**
     * Runs the agent.
     *
     * <p><b>Usage</b></p>
     * <p>You cannot run an agent recursively (cannot call it from itself).</p>
     * <p>The user cannot interact directly with a called agent. User output
     * goes to the Domino log.</p>
     * <p>You cannot debug a called agent.</p>
     * <p>For local operations, the agent runs on the computer running the
     * current program. See runOnServer to do otherwise.</p>
     * <p>For remote (IIOP) operations, the agent runs on the server handling
     * the remote calls.</p>
     *
     * @param noteId the NoteID of a document. The value is passed to the ParameterDocID property of the called agent.
     */
    void run(String noteId);

    /**
     * Runs the agent on the computer containing the database.
     *
     * <p><b>Usage</b></p>
     * <p>You cannot run an agent recursively (cannot call it from itself)..</p>
     * <p>The rules governing the access level required to run an agent using
     * the runOnServer method are the same as for any other server-based agent.
     * For information regarding agent security, see "Setting up agent security"
     * in Application Development with Domino Designer..</p>
     * <p>The user cannot interact directly with a called agent. User output
     * goes to the Domino log..</p>
     * <p>You cannot debug a called agent..</p>
     * <p>On a local database, the runOnServer method works like the run method,
     * that is, runs the agent on the local computer or the server handling the
     * remote (IIOP) calls..</p>
     * <p>Note  This behavior is new with Release 5.0.2. The former behavior
     * was to issue the error message "runOnServer must be used with a remote
     * database.".</p>
     * <p>If a Notes client invokes runOnServer, security is through the signer
     * of the agent.</p>
     *
     * @return status of the operation where 0 indicates success.
     */
    int runOnServer();

    /**
     * Runs the agent on the computer containing the database.
     *
     * <p><b>Usage</b></p>
     * <p>You cannot run an agent recursively (cannot call it from itself)..</p>
     * <p>The rules governing the access level required to run an agent using
     * the runOnServer method are the same as for any other server-based agent.
     * For information regarding agent security, see "Setting up agent security"
     * in Application Development with Domino Designer..</p>
     * <p>The user cannot interact directly with a called agent. User output
     * goes to the Domino log..</p>
     * <p>You cannot debug a called agent..</p>
     * <p>On a local database, the runOnServer method works like the run method,
     * that is, runs the agent on the local computer or the server handling the
     * remote (IIOP) calls..</p>
     * <p>Note  This behavior is new with Release 5.0.2. The former behavior
     * was to issue the error message "runOnServer must be used with a remote
     * database.".</p>
     * <p>If a Notes client invokes runOnServer, security is through the signer
     * of the agent.</p>
     *
     * @param noteId the NoteID of a document. The value is passed to the ParameterDocID property of the called agent.
     * @return status of the operation where 0 indicates success.
     */
    int runOnServer(String noteId);

    /**
     * Saves changes made to the agent.
     *
     * <p><b>Usage</b></p>
     * <p>Saving the agent changes the owner immediately. However, if you
     * subsequently call Agent.owner within the same Session, the previous
     * owner's name will be returned. The ownership change is not reflected
     * in properties until the next time a Session is obtained.</p>
     * <p>You must call Save after setServerName and setEnabled, or the new
     * value is lost.</p>
     */
    void save();
}
