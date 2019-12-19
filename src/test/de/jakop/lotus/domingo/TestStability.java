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

package de.jakop.lotus.domingo;

import java.util.Calendar;
import java.util.Iterator;

/**
 * Test stability of Domingo.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class TestStability {

    /**
     * Constructor.
     */
    public TestStability() {
    }

    /**
     * Main method of the test.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        log("Start");
        logMem();
        TestStability test = new TestStability();
        test.run();
        logMem();
        log("Finished");
    }

    private void run() {
        log("run repeated tests");
        for (int i = 0; i < 10; i++) {
            logMem();
            test();
            sleep(10);
        }
        log("Wait, run gc, run finalizers");
        for (int i = 0; i < 10; i++) {
            System.gc();
            sleep(50);
            logMem();
        }
        logMem();
    }

    private void test() {
        try {
            final DNotesFactory factory = DNotesFactory.getInstance();
            final DSession session = factory.getSession();
            final DDatabase database = session.getDatabase("", "log.nsf");
            final Iterator iterator = database.getAllDocuments();
            while (iterator.hasNext()) {
                DDocument doc = (DDocument) iterator.next();
                doc.replaceItemValue("Form", "Test");
                doc.getLastModified();
                doc.replaceItemValue("Calendar", Calendar.getInstance());
            }
        } catch (DNotesException e) {
            e.printStackTrace();
        } finally {
            try {
                DNotesFactory.dispose();
            } catch (DNotesRuntimeException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    private static void logMem() {
        System.out.println("Free Memory: " + Runtime.getRuntime().freeMemory());
    }

    private void sleep(int millis) {
        try {
            synchronized (this) {
                wait(millis);
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
