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

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Is the root of the Notes Objects containment hierarchy, providing access to
 * the other Domino objects, and represents the Domino environment of the
 * current program.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DSession extends DBase {

    /**
     * Indicates if the session is running on a server.
     *
     * @return <code>true</code> if the program is running on a server, <code>false</code> if the program is running
     *         on a workstation
     *
     * <p>
     * <b>Usage </b>
     * </p>
     * <p>A remote (IIOP) session always runs on a server.</p>
     * <p>A local session is running on a server if it is an agent in a server-
     * based database, and the agent has one of the following triggers:</p>
     * <p><ul>
     * <li>When new mail arrives</li>
     * <li>When documents have been created or modified</li>
     * <li>When documents have been pasted</li>
     * <li>On schedule hourly, daily, weekly, or monthly</li>
     * </ul></p>
     * <p>All other programs run on a workstation.</p>
     */
    boolean isOnServer();

    /**
     * Returns a database interface to a Notes database.
     *
     * @param serverName notes server name
     * @param databaseName notes database filename
     * @return DDatabase
     * @throws DNotesException if the database cannot be opened
     */
    DDatabase getDatabase(String serverName, String databaseName) throws DNotesException;

    /**
     * The full name of the user or server that created the session.
     *
     * @return user name
     */
    String getUserName();

    /**
     * Creates an empty database and opens it. Because the new database is
     * not based on a template, it is blank and does not contain any forms or
     * views.
     *
     * @param serverName notes server name
     * @param databaseName notes database filename
     * @return DDatabase
     */
    DDatabase createDatabase(String serverName, String databaseName);

    /**
     * Returns a String value containing the common name (CN=) component
     * of a hierarchical name.
     *
     * <p>Example:<br/>
     * <pre>Kurt Riede</pre></p>
     *
     * @return common user name
     */
    String getCommonUserName();

    /**
     * returns a String value containing the canonical form of a
     * hierarchical name.
     *
     * <p>Example:<br/>
     * <pre>Kurt Riede/Development/BEAP</pre></p>
     *
     * @return canonical user name
     */
    String getCanonicalUserName();

    /**
     * Evaluates a domino formula.
     *
     * <p>If the formula contains the name of a field, you must use the
     * 2-parameter method. The formula takes the field from the document
     * specified as the <code>doc</code> parameter.</p>
     *
     * <p>0x0040Functions that affect the user interface do not work in evaluate.
     * These include: 0x0040Command, 0x0040DbManager, 0x0040DbName,
     * 0x0040DbTitle, 0x0040DDEExecute, 0x0041DDEInitiate, 0x0040DDEPoke,
     * 0x0040DDETerminate, 0x0040DialogBox, 0x0040PickList, 0x0040PostedCommand,
     * 0x0040Prompt, and 0x0040ViewTitle.</p>
     *
     * <p>You cannot change a document with evaluate; you can only get a
     * result. To change a document, write the result to the document with a
     * method such as Document.replaceItemValue.</p>
     *
     * @param formula The formula
     * @return The result of the evaluation or <code>null</code> if an error
     *         occurred. A scalar result is returned as the first element.
     * @throws DNotesException if an error occurs during evaluation of the formula
     */
    List evaluate(String formula) throws DNotesException;

    /**
     * Evaluates a domino formula.
     *
     * <p>If the formula contains the name of a field, the formula takes the
     * field from the document specified as the <code>doc</code> parameter.</p>
     *
     * <p>0x0040Functions that affect the user interface do not work in evaluate.
     * These include: 0x0040Command, 0x0040DbManager, 0x0040DbName,
     * 0x0040DbTitle, 0x0040DDEExecute, 0x0041DDEInitiate, 0x0040DDEPoke,
     * 0x0040DDETerminate, 0x0040DialogBox, 0x0040PickList, 0x0040PostedCommand,
     * 0x0040Prompt, and 0x0040ViewTitle.</p>
     *
     * <p>You cannot change a document with evaluate; you can only get a
     * result. To change a document, write the result to the document with a
     * method such as Document.replaceItemValue.</p>
     *
     * @param formula The formula
     * @param doc optional document as context for formula
     * @return The result of the evaluation or <code>null</code> if an error
     *         occurred. A scalar result is returned as the first element.
     * @throws DNotesException if an error occurs during evaluation of the formula
     */
    List evaluate(String formula, DBaseDocument doc) throws DNotesException;

    /**
     * Given the name of an environment variable, retrieves its value.
     *
     * <p><b>Usage</b><br/>
     * This method retrieves the environment variable from the NOTES.INI file
     * for the current session.</p>
     * <p>The method prepends "$" to the name before retrieving its value.</p>
     * <p>Do not use this method for string values.</p>
     *
     * @param name the name of the environment variable to get.
     * @return the value of the environment variable.
     */
    Object getEnvironmentValue(String name);

    /**
     * Given the name of an environment variable, retrieves its value.
     *
     * <p><b>Usage</b><br/>
     * This method retrieves the environment variable from the NOTES.INI file
     * for the current session.</p>
     * <p>Do not use this method for string values.</p>
     *
     * @param name the name of the environment variable to get.
     * @param isSystem If <code>true</code>, the method uses the exact name of the
     *                 environment variable. If false or omitted, the method
     *                 prepends "$" to the name before retrieving its value.
     * @return the value of the environment variable.
     */
    Object getEnvironmentValue(String name, boolean isSystem);

    /**
     * Given the name of an environment variable, retrieves its value.
     *
     * <p><b>Usage</b><br/>
     * This method retrieves the environment variable from the NOTES.INI file
     * for the current session.</p>
     * <p>The method prepends "$" to the name before retrieving its value.</p>
     *
     * @param name the name of the environment variable
     * @return the value of the environment variable
     */
    String getEnvironmentString(String name);

    /**
     * Given the name of an environment variable, retrieves its value.
     *
     * <p><b>Usage</b><br/>
     * This method retrieves the environment variable from the NOTES.INI file
     * for the current session.</p>
     *
     * @param name the name of the environment variable
     * @param isSystem If <code>true</code>, the method uses the exact name of the
     *                 environment variable. If false or omitted, the method
     *                 prepends "$" to the name before retrieving its value.
     * @return the value of the environment variable
     */
    String getEnvironmentString(String name, boolean isSystem);

    /**
     * Sets the value of an environment variable.
     * <p>The method prepends "$" to the name before retrieving its value.</p>
     *
     * @param name the name of the environment variable
     * @param value The value of the environment variable.
     */
    void setEnvironmentString(String name, String value);

    /**
     * Sets the value of a string environment variable.
     *
     * @param name the name of the environment variable
     * @param value The value of the environment variable.
     * @param isSystem If <code>true</code>, the method uses the exact name of the
     *                 environment variable. If false or omitted, the method
     *                 prepends "$" to the name before retrieving its value.
     */
    void setEnvironmentString(String name, String value, boolean isSystem);

    /**
     * Creates a new Log object with the name you specify.
     *
     * @param name a name that identifies the log
     * @return the newly created log
     */
    DLog createLog(String name);

    /**
     * The Domino Directories and Personal Address Books that are known to the
     * current session.
     *
     * <p><b>Usage</b></p>
     * <p>To find out if an address book is a Domino Directory or a Personal
     * Address Book, use {@link DDatabase#isPublicAddressBook()}
     * and {@link DDatabase#isPrivateAddressBook()} of
     * {@link DDatabase}.</p>
     * <p>If the program runs on a workstation, both Domino Directories and
     * Personal Address Books are included. If the program runs on a server or
     * through remote (IIOP) calls, only Domino Directories on the server are
     * included.</p>
     * <p>A database retrieved through <tt>getAddressBooks</tt> is closed.
     * The following Database methods are available on the closed database:
     * {@link DDatabase#getFileName()},
     * {@link DDatabase#getFilePath()},
     * {@link DDatabase#isOpen()},
     * {@link DDatabase#isPrivateAddressBook()},
     * {@link DDatabase#isPublicAddressBook()},
     * {@link DDatabase#getSession()}, and
     * {@link DDatabase#getServer()}.
     * To access all other properties and methods, you must explicitly open
     * the database. See {@link DDatabase#open()} in
     * {@link DDatabase}.</p>
     *
     * @return list of DDatabase objects for all known personal address books
     */
    List getAddressBooks();

    /**
     * Converts a canonical name to it's abbreviated form.
     *
     * <p>The canonical format for storing hierarchical names displays the
     * hierarchical attribute of each component of the name.<br/>
     * Example: <tt>CN=Kurt Riede/OU=Development/O=BEAP</tt><br/>
     * where:<br/>
     * <tt>CN</tt> is the common name<br/>
     * <tt>OU</tt> is the organizational unit<br/>
     * <tt>O</tt>  is the organization<br/>
     * <tt>C</tt>  is the country code</p>
     *
     * <p>The abbreviated format of the example above is
     * <tt>Kurt Riede/Development/BEAP</tt>. </p>
     *
     * @param canonicalName a name in canonical form
     * @return abbreviated form of the name
     */
    String getAbbreviatedName(String canonicalName);

    /**
     * Converts a abbreviated name to it's canonical form.
     *
     * <p>The canonical format for storing hierarchical names displays the
     * hierarchical attribute of each component of the name.<br/>
     * Example: <tt>CN=Kurt Riede/OU=Development/O=BEAP</tt><br/>
     * where:<br/>
     * <tt>CN</tt> is the common name<br/>
     * <tt>OU</tt> is the organizational unit<br/>
     * <tt>O</tt>  is the organization<br/>
     * <tt>C</tt>  is the country code</p>
     *
     * <p>The abbreviated format of the example above is
     * <tt>Kurt Riede/Development/BEAP</tt>. </p>
     *
     * @param abbreviatedName a name in abbreviated form
     * @return canonical form of the name
     */
    String getCanonicalName(String abbreviatedName);

    /**
     * Represents the agent environment of the current program, if an agent is
     * running it. If the current program is not running from an agent, this
     * property returns null.
     *
     * @return context of current agent
     */
    DAgentContext getAgentContext();

    /**
     * Returns the current date/time.
     * <p>If using IIOP or running as a server agent, the server date/time is returned.
     * If running on the client, the date/time of the client is returned.
     *
     * @return current date/time
     */
    Calendar getCurrentTime();

    /**
     * Indicates whether an instantiated Session object is still valid.
     * For a remote operation, this method determines if the DIIOP server
     * task considers the session valid.
     * For a remote operation, iterative use of this method impacts performance.
     *
     * @return <tt>true</tt> if the Session object is still valid, else <tt>false</tt>
     */
    boolean isValid();

    /**
     * The release of Domino the session is running on.
     *
     * @return version info
     * @since domingo 1.1
     */
    String getNotesVersion();

    /**
     * The name of the platform the session is running on.
     *
     * <p>possible values:</p>
     * <p><table><tr><th>Value</th><th>Platform</th></tr>
     * <tr><td>"Macintosh"  </td><td>Macintosh</td></tr>
     * <tr><td>"MS-DOS"     </td><td>MS-DOS</td></tr>
     * <tr><td>"Netware"    </td><td>NetWare</td></tr>
     * <tr><td>"OS/2v1"     </td><td>OS/2� 16-bit</td></tr>
     * <tr><td>"OS/2v2"     </td><td>OS/2 32-bit </td></tr>
     * <tr><td>"OS/400"     </td><td>OS/400</td></tr>
     * <tr><td>"Windows/16" </td><td>Windows 16-bit</td></tr>
     * <tr><td>"Windows/32" </td><td>Windows 32-bit</td></tr>
     * <tr><td>"UNIX"       </td><td>UNIX (Sun, SCO, AIX�)</td></tr>
     * </table>
     *
     * @return name of platform
     * @since domingo 1.1
     */
    String getPlatform();

    /**
     * Creates a DxlExporter object.
     *
     * @return The newly created DxlExporter object.
     * @throws DNotesException if the DxlExporter object cannot be created
     */
    DDxlExporter createDxlExporter() throws DNotesException;

    /**
     * Returns the name of the mail server of the current user.
     *
     * @return name of mail server
     */
    String getMailServer();

    /**
     * Returns the file/path of the mail database of the current users.
     *
     * @return file/path of mail database
     */
    String getMailDatabaseName();

    /**
     * Returns the mail database of the current user.
     *
     * @return mail database of current user
     * @throws DNotesException if the mail database cannot be opened or, in case
     *             of a remote session, is not on the current server
     */
    DDatabase getMailDatabase() throws DNotesException;

    /**
     * Returns the mail database of a given user.
     *
     * @param username username
     * @return mail database of a given user
     * @throws DNotesException if the mail database cannot be opened or, in case
     *             of a remote session, is not on the current server
     */
    DDatabase getMailDatabase(String username) throws DNotesException;

    /**
     * Returns the name of the mail-domain of the current user.
     *
     * @return mail domain
     */
    String getMailDomain();

    /**
     * Returns the Domino object that a URL addresses.
     *
     * <p>The general forms of URLs that address Domino objects are as follows:</p>
     *
     * <pre>protocol://host/database?OpenDatabase</pre>
     * <pre>protocol://host/database/view?OpenView</pre>
     * <pre>protocol://host/database/form?OpenForm</pre>
     * <pre>protocol://host/database/document?OpenDocument</pre>
     * <pre>protocol://host/database/agent?OpenAgent</pre>
     *
     * <p>For local calls, the protocol is "notes" and the host is empty so
     * that the URL starts with "notes:///" ("notes," colon, and three slashes).</p>
     *
     * <p>The database can be specified by name or replica ID. The name can
     * include spaces (you can substitute plus signs for the spaces but it is
     * not necessary). The name need not include the type suffix if it is NSF.
     * The replica ID can be specified as the 16-digit ID by itself or the
     * 16-digit ID prefixed by two underscores and suffixed by the file type
     * (for example, NSF).</p>
     *
     * <p>The view, form, document, or agent can be specified by name,
     * universal ID, or note ID (not recommended because note IDs change between
     * replicas).</p>
     *
     * <p>The action (following the question mark) must be specified.</p>
     *
     * <p>You can use getURL to specify the URL.</p>
     *
     * @param url a URL that addresses a Domino object
     * @return a Database, View, Form, Document, or Agent object. You must cast
     *         the return value to the expected type.
     */
    DBase resolve(String url);

    /**
     * Returns the full name of the server that the session is running on.
     * If the server name is hierarchical, this property returns the fully
     * distinguished name. This property is null if the session is not running
     * on a server.
     *
     * @return name of the server that the session is running on
     */
    String getServerName();

    /**
     * Sets the time zone of calendar instances that are created by domingo.
     *
     * @param zone the time zone
     *
     * @see DBaseDocument#getItemValueDate(String)
     * @see DItem#getValueDateTime()
     */
    void setTimeZone(TimeZone zone);

    /**
     * Returns the time zone of calendar instances that are created by domingo.
     *
     * @return zone the time zone
     *
     * @see DBaseDocument#getItemValueDate(String)
     * @see DItem#getValueDateTime()
     */
    TimeZone getTimeZone();
}
