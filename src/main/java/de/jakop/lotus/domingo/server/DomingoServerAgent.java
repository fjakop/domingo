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

package de.jakop.lotus.domingo.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DNotesException;
import de.jakop.lotus.domingo.notes.DAgentBase;

/**
 * Adapter from a domingo agent to the domingo server.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DomingoServerAgent extends DAgentBase {

    /** ASCII code of the plus sign (+). */
    private static final int CHAR_PLUS = 43;

    /** ASCII code of the percentage sign (%). */
    private static final int CHAR_PERCENT = 37;

    /** Radix for hexa-decimal representations of numbers (16). */
    private static final int RADIX_16 = 16;

    /**
     * @see DAgentBase#main()
     */
    public void main() {
        final DDocument doc = getDSession().getAgentContext().getDocumentContext();
        final Map parameters = parseQueryString(doc.getItemValueString("QUERY_STRING"));
        final DomingoServer server = new DomingoServer();
        final PrintWriter agentOutput = getAgentOutput();
        try {
            server.execute(getDSession(), parameters, agentOutput);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (DNotesException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map parseQueryString(final String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        Map map = new HashMap();
        StringBuffer stringbuffer = new StringBuffer();
        String name;
        String[] values;
        StringTokenizer stringtokenizer = new StringTokenizer(s, "&");
        while (stringtokenizer.hasMoreTokens()) {
            String token = stringtokenizer.nextToken();
            int i = token.indexOf('=');
            String value;
            if (i == -1) {
                name = parseName(token, stringbuffer);
                value = null;
            } else {
                name = parseName(token.substring(0, i), stringbuffer);
                value = parseName(token.substring(i + 1, token.length()), stringbuffer);
            }
            if (map.containsKey(name)) {
                String[] as1 = (String[]) map.get(name);
                values = new String[as1.length + 1];
                for (int j = 0; j < as1.length; j++) {
                    values[j] = as1[j];
                }
                values[as1.length] = value;
            } else {
                values = new String[1];
                values[0] = value;
            }
            map.put(name, values);
        }
        return map;
    }

    private String parseName(final String s, final StringBuffer stringbuffer) {
        stringbuffer.setLength(0);
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            switch (c) {
            case CHAR_PLUS:
                stringbuffer.append(' ');
                break;
            case CHAR_PERCENT:
                try {
                    stringbuffer.append((char) Integer.parseInt(s.substring(i + 1, i + 3), RADIX_16));
                    i += 2;
                    break;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException();
                } catch (StringIndexOutOfBoundsException e) {
                    String s1 = s.substring(i);
                    stringbuffer.append(s1);
                    if (s1.length() == 2) {
                        i++;
                    }
                }
                break;
            default:
                stringbuffer.append(c);
                break;
            }
            i++;
        }
        return stringbuffer.toString();
    }
}
