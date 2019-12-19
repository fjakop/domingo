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

import java.util.ArrayList;
import java.util.List;

import de.jakop.lotus.domingo.queue.Queue;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Item;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

/**
 * Queue of Notes objects to be recycled.
 *
 * <p>Notes objects are queued with a priority defined by their class in this
 * decreasing order:<p/>
 * <p><code>Session</code>, <code>Database</code>, <code>View</code>,
 * <code>Collection</code>, <code>Document</code>, <code>Entry</code>,
 * <code>Item</code>, <code><i>others</i></code></p>
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class NotesRecycleQueue implements Queue {

    /** Reference to a session to be recycled. */
    private Session session = null;

    /** List of references to databases to be recycled. */
    private List databaseQueue = new ArrayList();

    /** List of references to views to be recycled. */
    private List viewQueue = new ArrayList();

    /** List of references to collection to be recycled. */
    private List collectionQueue = new ArrayList();

    /** List of references to documents to be recycled. */
    private List documentQueue = new ArrayList();

    /** List of references to view entries to be recycled. */
    private List entryQueue = new ArrayList();

    /** List of references to items to be recycled. */
    private List itemQueue = new ArrayList();

    /** List of references to other objects to be recycled. */
    private List otherQueue = new ArrayList();

    /**
     * Constructor.
     */
    public NotesRecycleQueue() {
        super();
    }

    /**
     * Returns the number of objects in the queue.
     *
     * @return number of objects in the queue
     */
    public synchronized int size() {
        return databaseQueue.size() + viewQueue.size() + collectionQueue.size() + documentQueue.size()
                + entryQueue.size() + itemQueue.size() + otherQueue.size() + (session != null ? 1 : 0);
    }

    /**
     * Checks is the queue is empty or not.
     *
     * @return <code>true</code> if the queue is empty, else <code>false</code>
     */
    public synchronized boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Enqueues an object to the queue.
     *
     *  @param obj the object to enqueue
     */
    public synchronized void enqueue(final Object obj) {
        if (obj instanceof Session) {
            session = (Session) obj;
        } else if (obj instanceof Database) {
            databaseQueue.add(obj);
        } else if (obj instanceof View) {
            viewQueue.add(obj);
        } else if (obj instanceof DocumentCollection || obj instanceof ViewEntryCollection) {
            collectionQueue.add(obj);
        } else if (obj instanceof Document) {
            documentQueue.add(obj);
        } else if (obj instanceof ViewEntry) {
            entryQueue.add(obj);
        } else if (obj instanceof Item) {
            itemQueue.add(obj);
        } else {
            otherQueue.add(obj);
        }
    }

    /**
     * Dequeues an object from the queue.
     *
     * @return next object from the queue
     *         or <code>null</code> if the queue is empty
     */
    public synchronized Object dequeue() {
        if (!otherQueue.isEmpty()) {
            return otherQueue.remove(otherQueue.size() - 1);
        } else if (!itemQueue.isEmpty()) {
            return itemQueue.remove(itemQueue.size() - 1);
        } else if (!entryQueue.isEmpty()) {
            return entryQueue.remove(entryQueue.size() - 1);
        } else if (!documentQueue.isEmpty()) {
            return documentQueue.remove(documentQueue.size() - 1);
        } else if (!itemQueue.isEmpty()) {
            return itemQueue.remove(itemQueue.size() - 1);
        } else if (!collectionQueue.isEmpty()) {
            return collectionQueue.remove(collectionQueue.size() - 1);
        } else if (!viewQueue.isEmpty()) {
            return viewQueue.remove(viewQueue.size() - 1);
        } else if (!databaseQueue.isEmpty()) {
            return databaseQueue.remove(databaseQueue.size() - 1);
        } else if (session != null) {
            final Object obj = session;
            session = null;
            return obj;
        } else {
            return null;
        }
    }
}
