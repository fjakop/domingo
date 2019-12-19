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

package de.jakop.lotus.domingo.map;


/**
 * Tests for class {@link NotesLocation}.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class NotesLocationTest extends BaseMapperTest {

    private Object password = "pwd";

    /**
     * Test notes locations.
     */
    public void testInvalidLocation() {
        assertInvalidLocation("not:///");
        assertInvalidLocation("note://local!!names.nsf");
        assertInvalidLocation("notes://local!!names.nsf");
        assertInvalidLocation("notes:///user:pwd");
        assertInvalidLocation("local!!names.nsf");
        assertInvalidLocation("local!names.nsf");
        assertInvalidLocation("local!");
        assertInvalidLocation("notes:///user:@!!");
        assertInvalidLocation("notes:///user:pwd@!!");
        assertInvalidLocation("notes:///:pwd@sf.net!!names.nsf");
        assertInvalidLocation("http://");
    }

    /**
     * Test notes locations.
     */
    public void testLocalLocation() {
        assertValidLocalLocation("notes:///", "", "");
        assertValidLocalLocation("notes:///!!names.nsf", "", "names.nsf");
        assertValidLocalLocation("notes:///local/", "", "");
        assertValidLocalLocation("notes:///local!!", "", "");
        assertValidLocalLocation("notes:///local", "", "");
        assertValidLocalLocation("notes:///lokal", "", "");
        assertValidLocalLocation("notes:///!!", "", "");
        assertValidLocalLocation("notes:///!!names.nsf", "", "names.nsf");
        assertValidLocalLocation("notes:///local!!names.nsf", "", "names.nsf");
        assertValidLocalLocation("notes:///lokal!!names.nsf", "", "names.nsf");
        assertValidLocalLocation("notes:///lokal!!test/test.nsf", "", "test/test.nsf");
        assertFalse("should not be a local location", (newLocation("notes:///lockal!!names.nsf")).isLocal());
    }

    /**
     * Test notes locations.
     */
    public void testServerLocation() {
        assertValidServerLocation("notes:///domingo.sf.net!!names.nsf", "domingo.sf.net", "names.nsf");
        assertValidServerLocation("notes:///domingo.sf.net!!test/test.nsf", "domingo.sf.net", "test/test.nsf");
        assertValidServerLocation("notes:///domingo.sf.net!!8525690D006AC34D", "domingo.sf.net", "8525690D006AC34D");
        assertValidServerLocation("notes:///domingo.sf.net!!__8525690D006AC34D.nsf", "domingo.sf.net", "__8525690D006AC34D.nsf");
        assertValidServerLocation("notes:///domingo.sf.net/names.nsf", "domingo.sf.net", "names.nsf");
        assertValidServerLocation("notes:///domingo.sf.net/test/test.nsf", "domingo.sf.net", "test/test.nsf");
        assertValidServerLocation("notes:///domingo.sf.net/test/test.nsf", "domingo.sf.net", "test/test.nsf");
        assertValidServerLocation("notes:///domingo.sf.net/test//test.nsf", "domingo.sf.net", "test//test.nsf");
        assertValidServerLocation("notes:///domingo.sf.net///test/test.nsf", "domingo.sf.net", "//test/test.nsf");
        assertValidServerLocation("notes:///domingo.sf.net/8525690D006AC34D", "domingo.sf.net", "8525690D006AC34D");
        assertValidServerLocation("notes:///domingo.sf.net/__8525690D006AC34D.nsf", "domingo.sf.net", "__8525690D006AC34D.nsf");
        assertValidServerLocation("notes:///domingo.sf.net", "domingo.sf.net", "");
        assertValidServerLocation("notes:///CN=PLATO/O=ACME!!names.nsf", "CN=PLATO/O=ACME", "names.nsf");
    }

    /**
     * Test IIOP locations.
     */
    public void testIIOPLocation() {
        assertValidIIOPLocation("notes:///user:pwd@domingo.sf.net!!names.nsf", "domingo.sf.net", "names.nsf");
        assertValidIIOPLocation("notes:///user:pwd@domingo.sf.net!!8525690D006AC34D", "domingo.sf.net", "8525690D006AC34D");
        assertValidIIOPLocation("notes:///user:pwd@domingo.sf.net!!__8525690D006AC34D.nsf", "domingo.sf.net", "__8525690D006AC34D.nsf");
        assertValidIIOPLocation("notes:///user:pwd@domingo.sf.net/names.nsf", "domingo.sf.net", "names.nsf");
        assertValidIIOPLocation("notes:///user:pwd@domingo.sf.net/8525690D006AC34D", "domingo.sf.net", "8525690D006AC34D");
        assertValidIIOPLocation("notes:///user:pwd@domingo.sf.net/__8525690D006AC34D.nsf", "domingo.sf.net", "__8525690D006AC34D.nsf");
        assertValidIIOPLocation("notes:///user:pwd@127.0.0.1!!names.nsf", "127.0.0.1", "names.nsf");
        password = null;
        assertValidIIOPLocation("notes:///user:@127.0.0.1!!names.nsf", "127.0.0.1", "names.nsf");
        assertValidIIOPLocation("notes:///user:@127.0.0.1!!names.nsf", "127.0.0.1", "names.nsf");
        password = "pwd";
    }

    /**
     * Test IIOP locations.
     */
    public void testHttpLocation() {
        assertValidIIOPLocation("http://user:pwd@sf.net", "sf.net", "");
    }

    /**
     * Test IIOP locations.
     */
    public void testGetInstanceContext() {
        NotesLocation context = newLocation("notes:///!!names.nsf");
        assertValidLocalLocation(NotesLocation.getInstance(context, "", "log.nsf"), "", "log.nsf");
        assertValidServerLocation(NotesLocation.getInstance(context, "sf.net", "log.nsf"), "sf.net", "log.nsf");
        assertValidServerLocation(NotesLocation.getInstance(context, "sf.net", "//log.nsf"), "sf.net", "//log.nsf");
    }

    /**
     * Test method for {@link NotesLocation#NotesLocation(java.lang.String)}.
     */
    public void testNotesLocationHierarchicalServerName() {

        // check for proper handling Notes URIs with hierarchically named servers in canonical format
        NotesLocation nl = new NotesLocation("notes:///CN=SomeServer/OU=SVR/O=SomeOrg/C=US!!directory\\database.nsf");
        assertEquals("CN=SomeServer/OU=SVR/O=SomeOrg/C=US", nl.getServer());
        assertEquals("directory\\database.nsf", nl.getPath());
    }

    private void assertValidLocalLocation(final String locationString, final String server, final String path) {
        assertValidLocalLocation(newLocation(locationString), server, path);
    }

    private void assertValidLocalLocation(final NotesLocation location, final String server, final String path) {
        assertTrue("Should be a local location", location.isLocal());
        assertFalse("Should be a local location", location.isIIOP());
        assertEquals("wrong server", server, location.getServer());
        assertEquals("wrong path", path, location.getPath());
        assertNull("username should be null", location.getUsername());
        assertNull("passwordshould be null", location.getPassword());
    }
    
    private void assertValidServerLocation(String locationString, final String server, final String path) {
        assertValidServerLocation(newLocation(locationString), server, path);
    }

    private void assertValidServerLocation(NotesLocation location, final String server, final String path) {
        assertFalse("Should be a local location", location.isLocal());
        assertFalse("Should be a local location", location.isIIOP());
        assertEquals("wrong server", server, location.getServer());
        assertEquals("wrong path", path, location.getPath());
    }

    private void assertValidIIOPLocation(String locationString, final String host, final String path) {
        NotesLocation location = newLocation(locationString);
        assertFalse("Should be an IIOP location", location.isLocal());
        assertTrue("Should be an IIOP location", location.isIIOP());
        assertEquals("wrong host", host, location.getHost());
        assertEquals("wrong path", path, location.getPath());
        assertEquals("wrong username", "user", location.getUsername());
        assertEquals("wrong password", this.password, location.getPassword());
    }

    private void assertInvalidLocation(String locationString) {
        try {
            new NotesLocation(locationString);
        } catch (RuntimeException e) {
            return;
        }
        fail("location is unexpectedly valid");
    }

    private NotesLocation newLocation(String locationString) {
        try {
            return new NotesLocation(locationString);
        } catch (RuntimeException e) {
            fail("Location should be valid", e);
            return null;
        }
    }
}
