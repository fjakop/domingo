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

package de.jakop.lotus.domingo.groupware;

import junit.framework.TestCase;
import de.jakop.lotus.domingo.groupware.Email.Importance;
import de.jakop.lotus.domingo.groupware.Email.Priority;

/**
 * Tests for class {@link Email}.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class EmailTest extends TestCase {
    
    /**
     * Test {@link Email#setPriority(String)} with low priority.
     */
    public void testSetPriorityLowByString() {
        Email email = new Email();
        email.setPriority(Priority.LOW);
        assertEquals(email.getPriority(), Priority.LOW);
    }

    /**
     * Test {@link Email#setPriority(String)} with normal priority.
     */
    public void testSetPriorityMediumByString() {
        Email email = new Email();
        email.setPriority(Priority.NORMAL);
        assertEquals(email.getPriority(), Priority.NORMAL);
    }

    /**
     * Test {@link Email#setPriority(String)} with high priority.
     */
    public void testSetPriorityHighByString() {
        Email email = new Email();
        email.setPriority(Priority.HIGH);
        assertEquals(email.getPriority(), Priority.HIGH);
    }

    /**
     * Test {@link Email#setImportance(String)} with high importance.
     */
    public void testSetImportanceHighByString() {
        Email email = new Email();
        email.setImportance(Importance.HIGH);
        assertEquals(email.getImportance(), Importance.HIGH);
    }

    /**
     * Test {@link Email#setImportance(String)} with normal importance.
     */
    public void testSetImportanceNormalByString() {
        Email email = new Email();
        email.setImportance(Importance.NORMAL);
        assertEquals(email.getImportance(), Importance.NORMAL);
    }

    /**
     * Test {@link Email#setImportance(String)} with low importance.
     */
    public void testSetImportanceLowByString() {
        Email email = new Email();
        email.setImportance(Importance.LOW);
        assertEquals(email.getImportance(), Importance.LOW);
    }
}
