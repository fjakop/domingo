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

package de.jakop.lotus.domingo.exception;

import junit.framework.TestCase;
import de.jakop.lotus.domingo.DNotesRuntimeException;

/**
 * Test nested exceptions.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ExceptionTest extends TestCase {

    /**
     * Test nested exceptions.
     */
    public void testException() {
        try {
            doTest1();
        } catch (DNotesRuntimeException e) {
            e.printStackTrace();
        }
    }

    private void doTest1() {
        doTest2();
    }

    private void doTest2() {
        doTest3();
    }

    private void doTest3() {
        doTest4();
    }

    private void doTest4() {
        try {
            doTest5();
        } catch (RuntimeException e) {
            throw new DNotesRuntimeException("exception in method test4()", e);
        }
    }

    private void doTest5() {
        doTest6();
    }

    private void doTest6() {
        doTest7();
    }

    private void doTest7() {
        doTest8();
    }

    private void doTest8() {
        throw new DNotesRuntimeException("exception in method test8()");
    }
}
