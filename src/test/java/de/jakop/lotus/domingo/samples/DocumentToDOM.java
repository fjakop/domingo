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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DDxlExporter;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.DView;

/**
 * A simple example accessing the local NAB.
 *
 * <p>The file NCSO.jar from the Lotus Domino server must be in the classpath to
 * run this test.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DocumentToDOM {

    /**
     * Main method of sample.
     * 
     * @param args arguments
     * @throws Exception If any error occurs
     */
    public static void main(final String[] args) throws Exception {

        // get an instance of the domingo factory
        DNotesFactory factory = DNotesFactory.getInstance();

        // create a session to the local Lotus Notes client
        DSession session = factory.getSession();

        // get the mail database
        DDatabase database = session.getMailDatabase();

        // get the first email
        DView view = database.getView("$Inbox");
        DDocument document = (DDocument) view.getAllDocuments().next();

        // export the email as XML and extract the body as HTML
        DDxlExporter exporter = session.createDxlExporter();
        exporter.setOutputDoctype(false);
        String xmlString = exporter.exportDxl(document);
        
        // parse document XML-string into a DOM document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        Document dom = builder.parse(inputStream);

        // do something with the document
        System.out.println("Document created: " + dom.getDocumentElement().getNodeName());
    }
}
