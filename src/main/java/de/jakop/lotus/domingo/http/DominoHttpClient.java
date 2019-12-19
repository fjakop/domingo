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

package de.jakop.lotus.domingo.http;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import de.jakop.lotus.domingo.monitor.AbstractMonitorEnabled;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import de.jakop.lotus.domingo.DNotesMonitor;

/**
 * An Http client for communication with Lotus Domino.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoHttpClient extends AbstractMonitorEnabled implements Serializable {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1071222474842080977L;

    /** Default port of Http protocol. */
    private static final int DEFAULT_HTTP_PORT = 80;

    private Credentials fCredentials;

    private String fProtocol = "http:";

    private String fHost;

    private int fPort = DEFAULT_HTTP_PORT;

    private String fUsername;

    private String fPassword;

    /** List of {@link org.apache.commons.httpclient.Cookie}s. */
//    private List fCookies = new ArrayList();

    private HttpClient fHttpClient;

    /**
     * Constructor.
     *
     * @param monitor the monitor
     * @param host the host for the session to connect
     * @param username the username for login
     * @param password the password for login
     * @throws MalformedURLException if the host is not valid
     */
    public DominoHttpClient(final DNotesMonitor monitor, final String host, final String username, final String password)
            throws MalformedURLException {
        super(monitor);
        final String urlStr = (host.indexOf(':') < 0) ? "http://" + host : host;
        final URL url = new URL(urlStr);
        fProtocol = url.getProtocol();
        if (!"http".equals(fProtocol) && !"https".equals(fProtocol)) {
            throw new NotesHttpRuntimeException("protocol not supported: " + fProtocol);
        }
        fHost = url.getHost();
        fPort = url.getPort();
        fPort = fPort == -1 ? DEFAULT_HTTP_PORT : fPort;
        fUsername = username;
        fPassword = password;
        fHttpClient = new HttpClient();
        DominoPreferences prefs = new DominoPreferences(fHost);
        fHttpClient.getState().addCookie(prefs.getTimeZoneCookie());
        fHttpClient.getState().addCookie(prefs.getRegionalCookie());
    }

    /**
     * Login to the Lotus Domino server.
     *
     * @throws IOException if the login fails
     */
    public void login() throws IOException {
        loginBasicAuthentication();
        loginSessionAuthentication();
    }

    private void loginBasicAuthentication() throws IOException {
        fCredentials = new UsernamePasswordCredentials(fUsername, fPassword);
        fHttpClient.getState().setCredentials(new AuthScope(fHost, fPort, AuthScope.ANY_REALM), fCredentials);

        // TODO test this: Http request to ensure server is available
//        DominoGetMethod method = createGetMethod(fProtocol + "://" + fHost + ":" + fPort + "/" + "names.nsf");
//        final int statusCode = executeMethod(method);
//        if (statusCode != HttpStatus.SC_OK) {
//            getMonitor().error("Http request failed: " + method.getStatusText());
//            throw new NotesHttpRuntimeException("Error " + method.getStatusCode() + ": " + method.getStatusText());
//        }
//        final byte[] responseBody = method.getResponseBody();
//        if (new String(responseBody).indexOf("Session valid") < 0) {
//            throw new NotesHttpRuntimeException("Invalid Session");
//        }
    }

    private void loginSessionAuthentication() throws IOException {
        final String url = fProtocol + "://" + fHost + ":" + fPort + "/" + "names.nsf?Login";
        final PostMethod method = new PostMethod(url);
        method.addParameter("%%ModDate", "0000000000000000");
        method.addParameter("Username", fUsername);
        method.addParameter("Password", fPassword);
        method.addParameter("RedirectTo", "/names.nsf");
        try {
            getMonitor().debug("Session authentication with " + url);
            final int statusCode = fHttpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
                getMonitor().error("Http request failed: " + method.getStatusLine());
            }
            // TODO check if a valid session cookie is now available
//            setCookies(method.getResponseHeaders("Set-Cookie"));
            logCookies();
        } catch (IOException e) {
            getMonitor().error(e.getLocalizedMessage(), e);
            throw e;
        } finally {
            method.releaseConnection();
        }
    }

    /**
     * Returns the protocol for the connection, either <tt>http</tt> or
     * <tt>https</tt>.
     *
     * @return protocol
     */
    public String getProtocol() {
        return fProtocol;
    }

    /**
     * Returns the host name of the server.
     *
     * @return host name
     */
    public String getHost() {
        return fHost;
    }

    /**
     * Returns the port of the server.
     *
     * @return port
     */
    public int getPort() {
        return fPort;
    }

    /**
     * Returns the current user name.
     *
     * @return username
     */
    public String getUserName() {
        return fUsername;
    }

    /**
     * Returns the name of the current user in the common format.
     *
     * @return username in common format
     */
    public String getCommonUserName() {
        // TODO convert user name format
        return fUsername;
    }

    /**
     * Returns the name of the current user in the canonical format.
     *
     * @return username in canonical format
     */
    public String getCanonicalUserName() {
        // TODO convert user name format
        return fUsername;
    }

    /**
     * Executes the given {@link HttpMethod HTTP method} using the given custom
     * {@link HostConfiguration host configuration} with the given custom
     * {@link HttpState HTTP state}.
     *
     * @param hostConfiguration The {@link HostConfiguration host configuration}
     *            to use. If <code>null</code>, the host configuration
     *            returned by {@link HttpClient#getHostConfiguration()} will be used
     * @param method the {@link HttpMethod HTTP method} to execute.
     * @param state the {@link HttpState HTTP state} to use when executing the
     *            method. If <code>null</code>, the state returned by
     *            {@link HttpClient#getState()} will be used.
     * @return the method's response code
     *
     * @throws IOException If an I/O (transport) error occurs. Some transport
     *             exceptions can be recovered from.
     */
    public int executeMethod(final HostConfiguration hostConfiguration, final HttpMethod method, final HttpState state)
            throws IOException {
        logMethod(method);
        return fHttpClient.executeMethod(hostConfiguration, method, state);
    }

    /**
     * Executes the given {@link HttpMethod HTTP method} using custom
     * {@link HostConfiguration host configuration}.
     *
     * @param hostConfiguration The {@link HostConfiguration host configuration}
     *            to use. If <code>null</code>, the host configuration
     *            returned by {@link HttpClient#getHostConfiguration()} will be used
     * @param method the {@link HttpMethod HTTP method} to execute
     * @return the method's response code
     *
     * @throws IOException If an I/O (transport) error occurs. Some transport
     *             exceptions can be recovered from.
     */
    public int executeMethod(final HostConfiguration hostConfiguration, final HttpMethod method) throws IOException {
        logMethod(method);
        return fHttpClient.executeMethod(hostConfiguration, method);
    }

    /**
     * Executes the given {@link HttpMethod HTTP method}.
     *
     * @param method the {@link HttpMethod HTTP method} to execute
     * @return the method's response code
     *
     * @throws IOException If an I/O (transport) error occurs. Some transport
     *             exceptions can be recovered from.
     */
    public int executeMethod(final DominoHttpMethod method) throws IOException {
        logMethod(method);
        return fHttpClient.executeMethod(method);
    }

//    private void setCookies(final HttpMethod method) {
//        for (int i = 0; i < fCookies.size(); i++) {
//            method.addRequestHeader((Header) fCookies.get(i));
//        }
//    }
//
//    private void setCookies(final Header[] cookies) {
//        for (int i = 0; i < cookies.length; i++) {
//            fCookies.add(cookies[i]);
//        }
//    }

    private void logMethod(final HttpMethod method) throws URIException {
        if (method instanceof GetMethod) {
            getMonitor().debug("HTTP GET " + method.getURI());
        } else if (method instanceof GetMethod) {
            getMonitor().debug("HTTP POST " + method.getURI());
        }
        logCookies();
    }

    private void logCookies() {
        if (getMonitor().isDebugEnabled()) {
            Cookie[] cookies = fHttpClient.getState().getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = (Cookie) cookies[i];
                getMonitor().debug(cookie.getName() + ": " + cookie.getValue());
            }
        }
    }

    /**
     * Creates and returns a new Http POST method.
     *
     * @param pathInfo the path on the server
     * @return new POST method
     */
    public DominoPostMethod createPost(final String pathInfo) {
        final String url = fProtocol + "://" + fHost + ":" + fPort + "/" + pathInfo;
        return DominoPostMethod.getInstance(url);
    }

    /**
     * Creates and returns a new Http GET method.
     *
     * @param pathInfo the path on the server
     * @return new GET method
     */
    public DominoGetMethod createGetMethod(final String pathInfo) {
        final String url = fProtocol + "://" + fHost + ":" + fPort + "/" + pathInfo;
        return DominoGetMethod.getInstance(url);
    }
}
