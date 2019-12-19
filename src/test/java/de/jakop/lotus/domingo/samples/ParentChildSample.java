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

package de.jakop.lotus.domingo.samples;

import java.util.Iterator;

import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DSession;

/**
 * Creating parent and child documents.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede </a>
 */
public final class ParentChildSample {

    /**
     * Main method of sample.
     *
     * @param args arguments
     * @throws DNotesException if the sample fails
     */
    public static void main(String[] args) throws DNotesException {
        DNotesFactory factory = DNotesFactory.getInstance();
        DSession session = factory.getSession("plato.acme", "Administrator", "password");
        DDatabase database = session.getDatabase("", "test.nsf");

        // ceate the parent document
        DDocument parent = database.createDocument();
        // set other fields here on the parent
        parent.save();

        // ceate the child document
        DDocument child = database.createDocument();
        // set other fields here on the child
        child.makeResponse(parent);
        child.save();

        // list all parent documents that donot have any children
        System.out.println("parent documents:");
        Iterator iterator = database.search("ParentUNID = \"\"");
        while (iterator.hasNext()) {
            DDocument document = (DDocument) iterator.next();
            System.out.println(document.getUniversalID());
        }
    }
}
