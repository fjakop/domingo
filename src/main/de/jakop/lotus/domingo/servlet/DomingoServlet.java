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

package de.jakop.lotus.domingo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.DNotesFactory;
import de.jakop.lotus.domingo.DSession;
import de.jakop.lotus.domingo.server.DomingoServer;


/**
 * TODO.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DomingoServlet extends HttpServlet {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = -4964947433973323738L;

    /** Reference to a domingo session associated with this servlet. */
    private DSession session;

    /** Reference to the domingo factory. */
    private DNotesFactory fFactory;

    /**
     * {@inheritDoc}
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        fFactory = DNotesFactory.getInstance();
        session = fFactory.getSession();
        // TODO allow config: either use local session or remote session
    }

    /**
     * @see javax.servlet.Servlet#destroy()
     */
    public void destroy() {
        fFactory.disposeInstance();
        super.destroy();
    }

    /**
     * {@inheritDoc}
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        final Map parameters = new HashMap();
        final Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            final String name = (String) enumeration.nextElement();
            parameters.put(name, request.getParameterValues(name));
        }
        final DomingoServer server = new DomingoServer();
        final ServletOutputStream stream = response.getOutputStream();
        final PrintWriter printWriter = new PrintWriter(stream);
        try {
            server.execute(session, parameters, printWriter);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (DNotesException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
        super.doPost(request, response);
    }
}
