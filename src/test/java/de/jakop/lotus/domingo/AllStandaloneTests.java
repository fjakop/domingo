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

import de.jakop.lotus.domingo.groupware.map.AllTests;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite of  basic tests for the BEA Notes API.
 * This test suite does not need any installed applications.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class AllStandaloneTests {

    /**
     * Private constructor to prevent instantiation.
     */
    private AllStandaloneTests() {
    }

    /**
     * The suite.
     *
     * @return Test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("All stand-alone tests for domingo");
        suite.addTest(de.jakop.lotus.domingo.groupware.AllTests.suite());
        suite.addTest(AllTests.suite());
        suite.addTest(de.jakop.lotus.domingo.http.AllTests.suite());
        suite.addTest(de.jakop.lotus.domingo.map.AllTests.suite());
        suite.addTest(de.jakop.lotus.domingo.service.AllTests.suite());
        suite.addTest(de.jakop.lotus.domingo.util.AllTests.suite());
        //$JUnit-BEGIN$
        //$JUnit-END$
        return suite;
    }
}
