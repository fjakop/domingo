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

package de.jakop.lotus.domingo.samples;

import java.util.Iterator;

import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.DViewEntry;

/**
 * A simple example accessing the NAB on a server using Http/XML.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class HttpNAB {

    /**
     * Main method of sample.
     *
     * @param args arguments
     * @throws DNotesException if the sample fails
     */
    public static void main(String[] args) throws DNotesException {

        // get an instance of the domingo factory
        DNotesFactory factory = DNotesFactory.getInstance("de.bea.domingo.http.NotesHttpFactory");

        // create a Http session to a Lotus Domino server
        DSession session = factory.getSession("http://plato.acme", "Administrator", "password");

        // get the local database names.nsf
        DDatabase database = session.getDatabase("", "names.nsf");

        // print the view ($Users) to the console
        DView view = database.getView("($Users)");
        Iterator allEntries = view.getAllEntries();
        System.out.println("Content of view ($Users)");
        while (allEntries.hasNext()) {
            DViewEntry entry = (DViewEntry) allEntries.next();
            for (int i = 0; i < entry.getColumnValues().size(); i++) {
                System.out.print(entry.getColumnValues().get(i) + ", ");
            }
            System.out.println();
        }
    }
}
