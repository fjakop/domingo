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

package de.jakop.lotus.domingo.monitor;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract stream monitor, logs everything to a <code>PrintStream</code>.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public abstract class AbstractStreamMonitor extends AbstractDefaultMonitor {

    /** Date format used to format dates in log output. */
    private final SimpleDateFormat dateFormat;

    /** Reference to the output stream for monitoring. */
    private PrintStream stream;

    /**
     * Constructor.
     *
     * @param theStream the output stream for monitoring
     */
    public AbstractStreamMonitor(final PrintStream theStream) {
        stream = theStream;
        dateFormat = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
    }

    /**
     * Monitor a message.
     *
     * @param message the message
     */
    protected final synchronized void monitor(final String message) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("[" + dateFormat.format(new Date()) + "] ");
        buffer.append(Thread.currentThread().getName() + ": " + message);
        stream.println(buffer.toString());
    }

    /**
     * Monitor a throwable.
     *
     * @param throwable the throwable
     */
    protected final synchronized void monitor(final Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace(stream);
        }
    }
}
