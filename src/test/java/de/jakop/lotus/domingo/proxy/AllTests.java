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

package de.jakop.lotus.domingo.proxy;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite of all tests of the mapping module.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class AllTests {

    /**
     * Private constructor to prevent instantiation.
     */
    public AllTests() {
    }

    /**
     * The suite.
     *
     * @return Test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("All tests for package de.jakop.lotus.domingo.proxy");
        //$JUnit-BEGIN$
        suite.addTestSuite(SessionProxyTest.class);
        //$JUnit-END$
        return suite;
    }
}
