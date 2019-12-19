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
package de.jakop.lotus.domingo.proxy;

import java.util.List;

import de.jakop.lotus.domingo.DView;
import lotus.domino.Form;
import lotus.domino.NotesException;
import de.jakop.lotus.domingo.DDatabase;
import de.jakop.lotus.domingo.DForm;
import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * A notes Form.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class FormProxy extends BaseProxy implements DForm {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 6895768801526231827L;

    /** The name of the view for fast access. */
    private String fName = null;

    /**
     * Constructor for DFormProxy.
     *
     * @param theFactory the controlling factory
     * @param database Notes database
     * @param form the Notes Form
     * @param monitor the monitor
     */
    private FormProxy(final NotesProxyFactory theFactory, final DDatabase database,
                      final Form form, final DNotesMonitor monitor) {
        super(theFactory, database, form, monitor);
        fName = getNameIntern();
    }

    /**
     * Returns a form object.
     *
     * @param theFactory the controlling factory
     * @param database Notes database
     * @param form the notes form object
     * @param monitor the monitor
     * @return a view object
     */
    static FormProxy getInstance(final NotesProxyFactory theFactory, final DDatabase database,
                                 final Form form, final DNotesMonitor monitor) {
        if (form == null) {
            return null;
        }
        FormProxy formProxy = (FormProxy) theFactory.getBaseCache().get(form);
        if (formProxy == null) {
            formProxy = new FormProxy(theFactory, database, form, monitor);
            formProxy.setMonitor(monitor);
            theFactory.getBaseCache().put(form, formProxy);
        }
        return formProxy;
    }

    /**
     * Returns the associated Notes form.
     *
     * @return associated Notes form
     */
    private Form getForm() {
        return (Form) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see BaseProxy#toString()
     */
    public String toString() {
        return fName;
    }

    /**
     * {@inheritDoc}
     * @see DForm#getAliases()
     */
    public List getAliases() {
        getFactory().preprocessMethod();
        try {
            return getForm().getAliases();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get aliases", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#getFieldType(java.lang.String)
     */
    public int getFieldType(final String fieldName) {
        getFactory().preprocessMethod();
        try {
            return getForm().getFieldType(fieldName);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get field name", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#getFields()
     */
    public List getFields() {
        getFactory().preprocessMethod();
        try {
            return getForm().getFields();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get fields", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#getFormUsers()
     */
    public List getFormUsers() {
        getFactory().preprocessMethod();
        try {
            return getForm().getFormUsers();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get form users", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#getHttpURL()
     */
    public String getHttpURL() {
        getFactory().preprocessMethod();
        try {
            return getForm().getHttpURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get Http URL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#getLockHolders()
     */
    public List getLockHolders() {
        getFactory().preprocessMethod();
        try {
            return getForm().getLockHolders();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get lock holders", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#getName()
     */
    public String getName() {
        return fName;
    }

    /**
     * {@inheritDoc}
     * @see DForm#getNotesURL()
     */
    public String getNotesURL() {
        getFactory().preprocessMethod();
        try {
            return getForm().getNotesURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get notes URL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#getReaders()
     */
    public List getReaders() {
        getFactory().preprocessMethod();
        try {
            return getForm().getReaders();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get readers", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#getURL()
     */
    public String getURL() {
        getFactory().preprocessMethod();
        try {
            return getForm().getURL();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get URL", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#isProtectReaders()
     */
    public boolean isProtectReaders() {
        getFactory().preprocessMethod();
        try {
            return getForm().isProtectReaders();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get protect readers", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#isProtectUsers()
     */
    public boolean isProtectUsers() {
        getFactory().preprocessMethod();
        try {
            return getForm().isProtectUsers();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get protect users", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#isSubForm()
     */
    public boolean isSubForm() {
        getFactory().preprocessMethod();
        try {
            return getForm().isSubForm();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get is subform", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lock()
     */
    public boolean lock() {
        getFactory().preprocessMethod();
        try {
            return getForm().lock();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot .lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lock(boolean)
     */
    public boolean lock(final boolean provisionalok) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(provisionalok);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lock(java.lang.String)
     */
    public boolean lock(final String name) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(name);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lock(java.lang.String, boolean)
     */
    public boolean lock(final String name, final boolean provisionalok) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(name, provisionalok);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lock(java.util.List)
     */
    public boolean lock(final List names) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(convertListToVector(names));
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lock(java.util.List, boolean)
     */
    public boolean lock(final List names, final boolean provisionalok) {
        getFactory().preprocessMethod();
        try {
            return getForm().lock(convertListToVector(names), provisionalok);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lockProvisional()
     */
    public boolean lockProvisional() {
        getFactory().preprocessMethod();
        try {
            return getForm().lockProvisional();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lockProvisional(java.lang.String)
     */
    public boolean lockProvisional(final String name) {
        getFactory().preprocessMethod();
        try {
            return getForm().lockProvisional(name);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock provisional", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#lockProvisional(java.util.List)
     */
    public boolean lockProvisional(final List names) {
        getFactory().preprocessMethod();
        try {
            return getForm().lockProvisional(convertListToVector(names));
        } catch (NotesException e) {
            throw newRuntimeException("Cannot lock provisional", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#remove()
     */
    public void remove() {
        getFactory().preprocessMethod();
        try {
            getForm().remove();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot remove form", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#setFormUsers(java.util.List)
     */
    public void setFormUsers(final List users) {
        getFactory().preprocessMethod();
        try {
            getForm().setFormUsers(convertListToVector(users));
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set form users", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#setProtectReaders(boolean)
     */
    public void setProtectReaders(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getForm().setProtectReaders(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set protect readers", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#setProtectUsers(boolean)
     */
    public void setProtectUsers(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getForm().setProtectUsers(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set protect users", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#setReaders(java.util.List)
     */
    public void setReaders(final List readers) {
        getFactory().preprocessMethod();
        try {
            getForm().setReaders(convertListToVector(readers));
        } catch (NotesException e) {
            throw newRuntimeException("Cannot set readers", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DForm#unlock()
     */
    public void unlock() {
        getFactory().preprocessMethod();
        try {
            getForm().unlock();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot unlock", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DView#getName()
     */
    private String getNameIntern() {
        getFactory().preprocessMethod();
        try {
            return getForm().getName();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get name", e);
        }
    }
}
