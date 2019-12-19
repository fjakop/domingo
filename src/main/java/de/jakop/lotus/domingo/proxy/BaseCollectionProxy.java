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

package de.jakop.lotus.domingo.proxy;

import java.util.Iterator;

import de.jakop.lotus.domingo.DDocument;
import lotus.domino.Base;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;
import de.jakop.lotus.domingo.DBase;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DView;

/**
 * Abstract base class for all implementations of interfaces derived from
 * <code>DBase</code>.
 */
public abstract class BaseCollectionProxy extends BaseProxy {

    /**
     * Constructor.
     *
     * @param theFactory the controlling factory
     * @param theParent the parent object
     * @param object the notes object
     * @param monitor the monitor
     */
    protected BaseCollectionProxy(final NotesProxyFactory theFactory, final DBase theParent,
                                  final Base object, final DNotesMonitor monitor) {
        super(theFactory, theParent, object, monitor);
    }


    ////////////////////////////////////////////////
    //    Iterator classes
    ////////////////////////////////////////////////

    /**
     * Iterator for all documents in a view.
     *
     * <p>A Mapper instance is used to map Notes document into a business
     * object. If no Mapper is provided, a simple default mapper is used that
     * maps the Notes document into a simple data structure that implements
     * the DDocument interface.</p>
     *
     * @see DDocument
     *
     * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
     */
    public final class ViewIterator implements Iterator {

        /** Reference to the view to iterate over. */
        private ViewProxy view = null;

        /** Reference to current document within the view. */
        private BaseDocumentProxy document = null;

        /**
         * Constructor.
         *
         * @param theView the View
         */
        protected ViewIterator(final DView theView) {
            view = (ViewProxy) theView;
            initialize();
        }

        /**
         * initialization.
         */
        private void initialize() {
            getFactory().preprocessMethod();
            try {
                final Document notesDocument = ((View) view.getNotesObject()).getFirstDocument();
                document = BaseDocumentProxy.getInstance(getFactory(), view.getParent(), notesDocument, getMonitor());
            } catch (NotesException e) {
                throw newRuntimeException("Cannot initialize ViewIterator", e);
            }
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return document != null;
        }

        /**
         * {@inheritDoc}
         * @see java.util.Iterator#next()
         */
        public Object next() {
            getFactory().preprocessMethod();
            final Object result = document;
            try {
                final Document nextNotesDocument =
                    ((View) view.getNotesObject()).getNextDocument((Document) document.getNotesObject());
                document = BaseDocumentProxy.getInstance(
                        getFactory(), view.getParent(), nextNotesDocument, getMonitor());
            } catch (NotesException e) {
                return null;
            }
            return result;
        }

        /**
         * The <tt>remove</tt> operation is not supported by this Iterator.
         * An <code>UnsupportedOperationException</code> is thrown if this
         * method is called.
         *
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
