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

import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DBaseItem;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * This class represents the Domino-Class <code>Item</code>.
 */
public abstract class BaseItemProxy extends BaseProxy implements DBaseItem {

    /**
     * Constructor for DItemImpl.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param item the Notes item object
     * @param monitor the monitor
     */
    protected BaseItemProxy(final NotesProxyFactory theFactory, final DBase parent,
                            final Item item, final DNotesMonitor monitor) {
        super(theFactory, parent, item, monitor);
    }

    /**
     * Creates or returns a cached implementation of the requested item
     * interface.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param item the associated Notes Item
     * @param monitor the monitor
     *
     * @return implementation of interface DBaseItem or null
     */
    static BaseItemProxy getInstance(final NotesProxyFactory theFactory, final DBase parent,
                                     final Item item, final DNotesMonitor monitor) {
        if (item == null) {
            return null;
        }
        BaseItemProxy itemProxy = (BaseItemProxy) theFactory.getBaseCache().get(item);
        if (itemProxy == null) {
            if (item instanceof RichTextItem) {
                itemProxy = new RichTextItemProxy(theFactory, parent, (RichTextItem) item, monitor);
            } else {
                itemProxy = new ItemProxy(theFactory, parent, item, monitor);
            }
            itemProxy.setMonitor(monitor);
            theFactory.getBaseCache().put(item, itemProxy);
        }
        return itemProxy;
    }

    /**
     * Returns the associated notes item object.
     *
     * @return notes item object
     */
    protected final Item getItem() {
        return (Item) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see DBaseItem#getName()
     */
    public final String getName() {
        getFactory().preprocessMethod();
        try {
            return getItem().getName();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get name", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DBaseItem#remove()
     */
    public final void remove() {
        getFactory().preprocessMethod();
        try {
            getItem().remove();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot remove item", e);
        }
    }

    /**
     * @see BaseProxy#toString()
     * @return the name of the item
     */
    public final String toString() {
        return getName();
    }
}
