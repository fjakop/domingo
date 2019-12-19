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


/**
 * @author MarcusT
 */
public abstract class BaseItemProxyTest extends BaseProxyTest {

    private String itemName = "TX_Test_for_domingo";

    /**
     * @param name the name of this test.
     */
    protected BaseItemProxyTest(String name) {
        super(name); 
    }

    /**
     * Tests getName.
     */
    public final void testGetName() {
        System.out.println("-> BaseItemProxy.testGetName");

        DBaseItem item = createDBaseItem();
        assertEquals("Received and expected name do not match.", itemName, item.getName());
    }

    /** This method shall create a valid DBaseItem to test in this superior class.
     * @return DBaseItem a valid instance.
     */
    protected abstract DBaseItem createDBaseItem();

    /**
     * {@inheritDoc}
     * @see BaseProxyTest#setUpTest()
     */
    public final void setUpTest() {
    }

    /** @return a unique item name. */
    public final String getItemName() {
        return itemName;
    }
}
