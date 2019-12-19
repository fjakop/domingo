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

import java.io.IOException;
import java.util.Date;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DSession;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public class CorbaCreateManyDocuments2 {

    private static final int MAX_DOCS = 30000;

    /**
     * @param args arguments
     * @throws DNotesException in case of a Notes error
     * @throws IOException in case of an IO error
     */
    public static void main(String[] args) throws DNotesException, IOException {

        // Test Domingo Java API - create 10.000 Documents
        DNotesFactory factory = DNotesFactory.getInstance();
        DSession session = factory.getSession("PLATO.Acme", "Administrator", "password");
        System.out.println("Platform --> " + session.getPlatform());
        System.out.println("Username --> " + session.getUserName());
        DDatabase database = session.getDatabase("", "test.nsf");
        System.out.println("DB-Title --> " + database.getTitle());

        DDocument doc;
        Date dateStart = new Date();

        System.out.println("Creating " + MAX_DOCS + " Notes Documents...");

        for (int idx = 0; idx < MAX_DOCS; idx++) {
            doc = database.createDocument();
            doc.replaceItemValue("Form", "Memo");
            doc.replaceItemValue("Body", "Test");
            doc.save();
        }
        Date dateFinish = new Date();
        long duration = dateFinish.getTime() - dateStart.getTime();

        System.out.println("Duration: " + duration / 3600 + " seconds");

    }
}
