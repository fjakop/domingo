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

import de.jakop.lotus.domingo.exception.DominoException;
import de.jakop.lotus.domingo.monitor.AbstractMonitorEnabled;
import de.jakop.lotus.domingo.queue.Queue;
import lotus.domino.Agent;
import lotus.domino.AgentContext;
import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.DbDirectory;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.EmbeddedObject;
import lotus.domino.Form;
import lotus.domino.Item;
import lotus.domino.Log;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * Implementation of interface <code>NotesRecycler</code> that explicitly
 * recycles almost all Notes objects.
 *
 * <p>This strategy should be used with Lotus Notes R5 while missing explicit
 * recycle results in memory leaks.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class RecycleStrategy extends AbstractMonitorEnabled implements NotesRecycler {

    /** Reference to a recycle queue. */
    private Queue recycleQueue = new NotesRecycleQueue();

    /** Indicates whether the DateTime recycle problem is already reported.*/
    private boolean dateTimeHitfixProblemReported = false;

    /**
     * Constructor.
     *
     * @param monitor the monitor
     */
    public RecycleStrategy(final DNotesMonitor monitor) {
        super(monitor);
    }

    /**
     * {@inheritDoc}
     * @see NotesRecycler#recycleLater(java.lang.Object)
     */
    public synchronized void recycleLater(final Object object) {
        final Base notesBase = getNotesObject(object);
        if (notesBase != null) {
            recycleQueue.enqueue(notesBase);
        }
    }

    /**
     * @see NotesRecycler#recycleQueue()
     */
    public synchronized void recycleQueue() {
        if (!recycleQueue.isEmpty()) {
            while (!recycleQueue.isEmpty()) {
                final Object obj = recycleQueue.dequeue();
                if (obj != null) {
                    recycle(obj);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * @see NotesRecycler#recycle(java.lang.Object)
     */
    public synchronized void recycle(final Object object) {
        final Base notesBase = getNotesObject(object);
        try {
            if (notesBase == null) {
                return; // ignore null values
            } else if (notesBase instanceof Session) {
                notesBase.recycle();
            } else if (notesBase instanceof DbDirectory) {
                notesBase.recycle();
            } else if (notesBase instanceof Database) {
                notesBase.recycle();
            } else if (notesBase instanceof Log) {
                return; // TODO Check how a Log must be recycled properly. notesBase.recycle();
            } else if (notesBase instanceof View) {
                notesBase.recycle();
            } else if (notesBase instanceof ViewNavigator) {
                notesBase.recycle();
            } else if (notesBase instanceof ViewEntryCollection) {
                return; // don't recycle entry collections
            } else if (notesBase instanceof ViewEntry) {
                notesBase.recycle();
            } else if (notesBase instanceof DocumentCollection) {
                notesBase.recycle();
            } else if (notesBase instanceof Document) {
                notesBase.recycle();
            } else if (notesBase instanceof Item) {
                return; // don't recycle items
            } else if (notesBase instanceof RichTextItem) {
                return; // don't recycle items
            } else if (notesBase instanceof EmbeddedObject) {
                return; // don't recycle embedded objects
            } else if (notesBase instanceof Form) {
                notesBase.recycle();
            } else if (notesBase instanceof Agent) {
                notesBase.recycle();
            } else if (notesBase instanceof AgentContext) {
                notesBase.recycle();
            } else if (notesBase instanceof DateTime) {
                recycleDateTime((DateTime) notesBase);
            } else if (notesBase instanceof DateRange) {
                notesBase.recycle();
            } else {
                getMonitor().debug("No explicit recycle strategy found for class " + notesBase.getClass().getName());
                notesBase.recycle();
            }
        } catch (NotesException e) {
            getMonitor().warn("Cannot recycle " + notesBase.getClass().getName(), new DominoException(e));
        }
    }

    /**
     * Recycles a Notes date/time object.
     *
     * @param dateTime the date/time object
     */
    private void recycleDateTime(final DateTime dateTime) {
        try {
            if (dateTime.getParent() == null) {
                //
                // DateTime objects that only have a Date component seem
                // not to have a parent session in Release 5.0.8 and 5.0.11
                // without the HotFix of the file notes.jar from Lotus.
                // It is impossible to recycle such objects, so we log an error here.
                //
                // See also Lotus Software Problem report (SPR):
                // http://www.ibm.com/support/docview.wss?uid=sim4ae8f437f0c71ea0b85256c9f00791c48
                // -> SPR# MKIN5CYTH4 - Fixed a null pointer exception when calling
                //                      the recycle method of DateTime class
                //
                if (!dateTimeHitfixProblemReported) {
                    dateTimeHitfixProblemReported = true;
                    getMonitor().fatalError("recycle a DateTime object without parent session.");
                }
            } else {
                dateTime.recycle();
            }
        } catch (NotesException e) {
            getMonitor().warn("recycle DateTime with parent session not available.", new DominoException(e));
        }
    }

    /**
     * Given any object (might be a Domingo object or a Notes object) returns
     * the base notes object if available.
     *
     * @return notes object
     */
    private Base getNotesObject(final Object object) {
        final Base notesBase;
        if (object instanceof BaseProxy) {
            notesBase = ((BaseProxy) object).getNotesObject();
        } else if (object instanceof Base) {
            notesBase = (Base) object;
        } else {
            notesBase = null;
        }
        return notesBase;
    }
}
