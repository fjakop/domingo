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

import java.util.List;

/**
 * Represents a form in a database.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public interface DForm extends DBase {

    /**
     * Returns the aliases of a form. This property returns all but the first in
     * the list of all the form's names. The Name property returns the first
     * name in the list.
     *
     * @return list of aliases
     */
    List getAliases();

    /**
     * Returns the names of all the fields of a form.
     *
     * @return list of field names
     */
    List getFields();

    /**
     * Returns the contents of the $FormUsers field.
     *
     * @return list of names
     */
    List getFormUsers();

    /**
     * Sets the contents of the $FormUsers field.
     *
     * @param users list of user names
     */
    void setFormUsers(List users);

    /**
     * Return the name of a form.
     *
     * @return name of the form
     */
    String getName();

    /**
     * Returns the contents of the $Readers field.
     *
     * @return list of reader names
     */
    List getReaders();

    /**
     * Sets the contents of the $Readers field.
     *
     * @param readers list of reader names
     */
    void setReaders(List readers);

    /**
     * Indicates whether a form is a subform.
     *
     * @return <code>true</code> if the form is a subform, else
     *         <code>false</code>
     */
    boolean isSubForm();

    /**
     * Returns if the $Readers items is protected from being overwritten by
     * replication.
     *
     * @return <code>true</code> to protect $Readers, else <code>false</code>
     */
    boolean isProtectReaders();

    /**
     * Sets if the $Readers items is protected from being overwritten by
     * replication.
     *
     * @param flag <code>true</code> to protect $Readers, else
     *            <code>false</code>
     */
    void setProtectReaders(boolean flag);

    /**
     * Returns if the $FormUsers items is protected from being overwritten by
     * replication.
     *
     * @return <code>true</code> to protect $FormUsers, else
     *         <code>false</code>
     */
    boolean isProtectUsers();

    /**
     * Sets if the $FormUsers items is protected from being overwritten by
     * replication.
     *
     * @param flag <code>true</code> to protect $FormUsers, else
     *            <code>false</code>
     */
    void setProtectUsers(boolean flag);

    /**
     * Permanently deletes a form from a database.
     */
    void remove();

    /**
     * Returns the Domino URL for the form.
     *
     * @return URL
     */
    String getURL();

    /**
     * Returns the Domino URL of a form when Notes protocols are in effect. If
     * Notes protocols are not available, this property returns an empty string.
     *
     * @return Domino URL of a form
     * @since Lotus Notes Release 6.5.
     */
    String getNotesURL();

    /**
     * Returns the Domino URL of a form when HTTP protocols are in effect.
     *
     * @return Http URL of a form
     * @since Lotus Notes Release 6.5.
     */
    String getHttpURL();

    /**
     * Gets the type of a field on the form.
     *
     * @see DItem#getType()
     * @param fieldName field name
     * @return type of the field
     */
    int getFieldType(String fieldName);

    /**
     * Returns the names of the holders of a lock.
     *
     * @return list of name strings
     * @since Lotus Notes Release 6.5.
     */
    List getLockHolders();

    /**
     * Locks a form.
     *
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lock();

    /**
     * Locks a form. The lock holder defaults to the effective user.
     *
     * @param provisionalok <code>true</code> to permit the placement of a
     *            provisional lock
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lock(boolean provisionalok);

    /**
     * Locks a form. The holder must be a user or group. Defaults to the
     * effective user. The empty string ("") is not permitted.
     *
     * @param name name of the lock holder.
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lock(String name);

    /**
     * Locks a form. The lock holder must be a user or group. Defaults to the
     * effective user. The empty string ("") is not permitted.
     *
     * @param name name of the lock holder.
     * @param provisionalok <code>true</code> to permit the placement of a
     *            provisional lock
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lock(String name, boolean provisionalok);

    /**
     * Locks a form.
     *
     * @param names list of name strings
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lock(List names);

    /**
     * Locks a form. The lock holder must be a user or group. Defaults to one
     * lock holder: the effective user. The empty string ("") is not permitted.
     *
     * @param names names of the lock holders
     * @param provisionalok <code>true</code> to permit the placement of a
     *            provisional lock
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lock(List names, boolean provisionalok);

    /**
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lockProvisional();

    /**
     * @param name name of the lock holder.
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lockProvisional(String name);

    /**
     * Locks a form provisionally.
     *
     * @since Lotus Notes Release 6.5.
     * @param names names list of name strings
     * @return <code>true</code> if the lock is placed, else
     *         <code>false</code>
     * @since Lotus Notes Release 6.5.
     */
    boolean lockProvisional(List names);

    /**
     * Unlocks a form.
     *
     * @since Lotus Notes Release 6.5.
     */
    void unlock();
}
