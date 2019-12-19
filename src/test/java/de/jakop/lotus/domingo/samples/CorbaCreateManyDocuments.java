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

import java.util.GregorianCalendar;

import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DRichTextItem;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.monitor.AbstractMonitor;
import de.jakop.lotus.domingo.monitor.ConsoleMonitor;

/**
 * This example creates a new database on a server and creates a huge number of
 * documents to test proper recycling thru a Corba connection.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public class CorbaCreateManyDocuments {

    /**
     * Main method.
     *
     * @param args command line arguments: host username password
     */
    public static final void main(final String[] args) {
        if (args.length != 3) {
            System.out.println("Arguments: host username password");
            return;
        }
        DDatabase database = null;
        try {
            ConsoleMonitor monitor = new ConsoleMonitor();
            monitor.setLevel(AbstractMonitor.DEBUG);
            DNotesFactory factory = DNotesFactory.getInstance(monitor);
            DSession session = factory.getSession(args[0], args[1], args[2]);
            database = session.createDatabase("", "testCorbaCreateManyDocuments.nsf");
            monitor.debug("Database many.nsf created on server "  + args[0]);
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 1000; j++) {
                    DDocument document = database.createDocument();
                    document.replaceItemValue("Form", "Test");
                    document.replaceItemValue("Test", "Test");
                    document.replaceItemValue("Number", 1.1234);
                    document.replaceItemValue("Date", new GregorianCalendar(2007, 01, 01, 17, 30, 00));
                    DRichTextItem item = document.createRichTextItem("Body");
                    item.appendText("some rich text");
                    item.addNewLine(2);
                    item.appendText("some more text");
                    document.save();
                }
                monitor.debug("Created " + ((i + 1) * 1000) + " documents");
            }
            monitor.debug("finished successfully.");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (database != null) {
            database.remove();
        }
    }
}
