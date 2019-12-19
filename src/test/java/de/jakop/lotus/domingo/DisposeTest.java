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

package de.jakop.lotus.domingo;

import junit.framework.TestCase;

/**
 * Test disposal.
 * <p>This test should run as the last test in any test suite to ensure
 * proper disposal of all resources allocated by the notes API.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DisposeTest extends TestCase {

    /**
     * @param name the name of the test
     */
    public DisposeTest(String name) {
        super(name);
    }

    /**
     * Test disposal.
     */
    public void testDispose() {
        System.out.println("-> DNotesFactory.testDispose");
        try {
            DNotesFactory.dispose();
        } catch (DNotesRuntimeException e) {
            e.printStackTrace();
            fail("Cannot dispose Domingo: " + e.getMessage());
        }
    }
}
