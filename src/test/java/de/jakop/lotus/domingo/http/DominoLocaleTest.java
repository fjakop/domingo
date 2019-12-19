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

package de.jakop.lotus.domingo.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoLocaleTest extends TestCase {

    /**
     * Test .
     */
    public void testGetDateTimeString() {
    }

    /**
     * Creates a dump of all domino locales in UTF-8 encoding.
     *
     * At least one dummy argument must be specified to prevent
     * unintended overwrite of dump.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please add a dummy argument to recreate dump of domino locales.");
            return;
        }
        List locales = DominoLocale.getLocales();
        SortedSet sortedSet = new TreeSet(new DominoLocaleComparator());
        sortedSet.addAll(locales);
        Iterator iterator = sortedSet.iterator();
        File file = new File("src/test/de/bea/domingo/http/domino-locales-dump-utf-8.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintStream printStream = new PrintStream(fileOutputStream, true, "UTF-8");
            while (iterator.hasNext()) {
                Object locale = iterator.next();
                printStream.println(locale.toString());
            }
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compares two DominoLocale instances.
     */
    private static final class DominoLocaleComparator implements Comparator, Serializable {

        /** serial version ID for serialization. */
        private static final long serialVersionUID = 2345889372494399635L;

        /**
         * {@inheritDoc}
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(final Object o1, final Object o2) {
            return (((DominoLocale) o1).getLocale() + ":").compareTo(((DominoLocale) o2).getLocale() + ":");
        }
    }
}
