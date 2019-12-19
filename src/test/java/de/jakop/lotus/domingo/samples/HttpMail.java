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

import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.monitor.AbstractMonitor;
import de.jakop.lotus.domingo.monitor.ConsoleMonitor;

/**
 * A simple example accessing a Lotus Domino server via XML over Http.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class HttpMail {

    /**
     * Main method of sample.
     *
     * @param args arguments
     * @throws DNotesException if the sample fails
     */
    public static void main(String[] args) throws DNotesException {

        // create a console monitor
        ConsoleMonitor monitor = new ConsoleMonitor();
        monitor.setLevel(AbstractMonitor.DEBUG);

        // get an instance of the domingo factory
        DNotesFactory factory = DNotesFactory.getInstance("de.bea.domingo.http.NotesHttpFactory", monitor);

        // create a Http session to the Lotus Domino server
        DSession session = factory.getSession("http://plato.acme", "kriede", "password");

        // get the mail database
//        DDatabase database = session.getDatabase("", "mail/kriede.nsf");
        DDatabase database = session.getDatabase("", "mail/administ.nsf");

        // create a new memo in the mail database and save it
        DDocument document = database.createDocument();
        document.replaceItemValue("Form", "Memo");
        document.replaceItemValue("From", "kriede@acme");
        document.replaceItemValue("Subject", "Domingo Test with XML over Http");
        document.replaceItemValue("Body", "Domingo Test with XML over Http");
        boolean b = document.save();
        if (b) {
            System.out.println("Mail saved");
        } else {
            System.out.println("error saving mail");
        }
    }
}
