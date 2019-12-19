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
package de.jakop.lotus.domingo.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class XMLUtil {

    private static final String XML_HEADER_SAMPLE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"      ";

    private static final String XML_START = "<?xml";

    private static final String XML_END = "?>";

    private XMLUtil() {
    }

    /**
     * Parses a byte array that represents an XML instance document to a String
     * with respect to encoding.
     *
     * @param bytes the bytes to parse
     * @return parsed string
     */
    public static String parse(final byte[] bytes) {
        final byte[] b4 = new byte[XML_START.length() - 1];
        int count = 0;
        for (; count < b4.length; count++) {
            b4[count] = (byte) bytes[count];
        }
        String encoding = (String) IANAEncoding.getEncodingName(b4, count)[0];
        String preparsedResult = null;
        try {
            int min = Math.min(bytes.length, 2 * XML_HEADER_SAMPLE.length());
            preparsedResult = new String(bytes, 0, min, encoding);
        } catch (UnsupportedEncodingException e) {
            try {
                preparsedResult = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                preparsedResult = new String(bytes);
            }
        }
        XMLHeaderParser xmlHeaderParser = new XMLHeaderParser(preparsedResult);
        try {
            String xmlEncoding = xmlHeaderParser.getEncoding();
            return new String(bytes, xmlEncoding);
        } catch (UnsupportedEncodingException e) {
        }
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    private static final class XMLHeaderParser {

        /** BOM for encoding in big endian. */
        private static final int BOM_BIG_ENDIAN = 0xfffe;

        /** BOM for encoding in little endian. */
        private static final int BOM_LITTLE_ENDIAN = 0xfeff;

        private Properties fPseudoAttributes = new Properties();

        /** Position in preparsed string. */
        private int fPosition = 0;

        /** byte array of input to parse. */
        private final String fPreparsedString;

        /**
         * Constructor.
         */
        public XMLHeaderParser(final String preparsedString) {
            fPreparsedString = preparsedString;
        }

        /**
         * @param preparsedResult
         * @return
         * @throws XmlHeaderParserException if the XML header is inavlid
         */
        private String getEncoding() throws UnsupportedEncodingException {
            skipBOM();
            skip(XML_START);
            parsePseudoAttribute();
            parsePseudoAttribute();
            parsePseudoAttribute();
            skip(XML_END);
            return (String) fPseudoAttributes.get("encoding");
        }

        /**
         * Parses and skips the optional byte order mask (BOM).
         */
        private void skipBOM() {
             char c = fPreparsedString.charAt(0);
             if (c == BOM_LITTLE_ENDIAN || c == BOM_BIG_ENDIAN) {
                 fPosition++;
             }
        }

        /**
         * Parses a pseudo attribute as used in the XML declaration.
         *
         * @return String representing the pseudo attribute
         * @throws XmlHeaderParserException if the XML header is inavlid
         */
        private void parsePseudoAttribute() throws UnsupportedEncodingException {
            skipWhiteSpace();
            if (fPreparsedString.charAt(fPosition) == '?') {
                return;
            }
            String name = fPreparsedString.substring(fPosition, fPreparsedString.indexOf('=', fPosition));
            fPosition += name.length() + 1;
            skipQuote();
            String value = fPreparsedString.substring(fPosition, fPreparsedString.indexOf('\"', fPosition));
            fPosition += value.length();
            skipQuote();
            fPseudoAttributes.put(name, value);
        }

        /**
         * @throws XmlHeaderParserException if there is no quote at the current position
         */
        private void skipQuote() throws UnsupportedEncodingException {
            if (fPreparsedString.charAt(fPosition) == '\"') {
                fPosition++;
                return;
            }
            throw new UnsupportedEncodingException("expected quote at character " + fPosition);
        }

        /**
         *
         */
        private void skipWhiteSpace() {
            while (isWhitespace(fPreparsedString.charAt(fPosition))) {
                fPosition++;
            }
        }

        /**
         * Checks if a given character is a white-space character.
         *
         * @param c the charcter to check
         * @return <code>true</code> if the character is a white-space, else <code>false</code>
         */
        private boolean isWhitespace(final char c) {
            return c == ' ' || c == '\t';
        }

        /**
         * Parses a constant string.
         *
         * @param s the string to parse
         * @throws XmlHeaderParserException if the expected string wasn't found
         */
        private void skip(final String s) throws UnsupportedEncodingException {
            if (fPreparsedString.length() - fPosition < s.length()) {
                throw new UnsupportedEncodingException("expected " + s + " at character " + fPosition);
            }
            String substring = fPreparsedString.substring(fPosition, fPosition + s.length());
            if (substring.equals(s)) {
                fPosition += s.length();
                return;
            }
            throw new UnsupportedEncodingException("expected " + s + " at character " + fPosition);
        }
    }

    private static final class Encoding {

        /** Encoding name: UTF-8. */
        private static final String UTF_8 = "UTF-8";

        /** Encoding name: UTF-16 with big endian. */
        private static final String UTF_16_BIG_ENDIAN = "UTF-16BE";

        /** Encoding name: UTF-16 with little endian. */
        private static final String UTF_16_LITTLE_ENDIAN = "UTF-16LE";

        /** Encoding name: ISO-10646-UCS-4. */
        private static final String ISO_10646_UCS_4 = "ISO-10646-UCS-4";

        /** Encoding name: CP037 (EBCDIC). */
        private static final String CP037 = "CP037";
    }

    private static final class EBCDIC {

        /** EBCDIC character less-than ("<"). */
        private static final int LESS_THAN = 0x4C;

        /** EBCDIC character questionmark ("?"). */
        private static final int QUESTIONMARK = 0x6F;

        /** EBCDIC lower case character x. */
        private static final int LOWER_X = 0xA7;

        /** EBCDIC lower case character m. */
        private static final int LOWER_M = 0x94;
    }

    private static final class ASCII {

        /** ASCII character less-than ("<"). */
        private static final int LESS_THAN = 0x3C;

        /** ASCII character questionmark ("?"). */
        private static final int QUESTIONMARK = 0x3F;
    }

    private static final class IANAEncoding {

        /** Bit mask: <tt>10111011</tt>, character.*/
        private static final int MASK_10111011 = 0xBB;

        /** Bit mask: <tt>10111111</tt>, character.*/
        private static final int MASK_10111111 = 0xBF;

        /** Bit mask: <tt>11111111</tt>.*/
        private static final int MASK_11111111 = 0xFF;

        /** Bit mask: <tt>11111110</tt>.*/
        private static final int MASK_11111110 = 0xFE;

        /** Bit mask: <tt>11101111</tt>.*/
        private static final int MASK_11101111 = 0xEF;

        private IANAEncoding() {
        }

        /**
         * Returns the IANA encoding name that is auto-detected from the bytes
         * specified, with the endian-ness of that encoding where appropriate.
         *
         * @param b4 The first four bytes of the input.
         * @param count The number of bytes actually read.
         * @return a 2-element array: the first element, an IANA-encoding string,
         *         the second element a Boolean which is true iff the document is
         *         big endian, false if it's little-endian, and null if the
         *         distinction isn't relevant.
         */
        private static Object[] getEncodingName(final byte[] b4, final int count) {
            if (count < 2) {
                return new Object[] { Encoding.UTF_8, null };
            }
            // UTF-16, with BOM
            int b0 = b4[0] & MASK_11111111;
            int b1 = b4[1] & MASK_11111111;
            if (b0 == MASK_11111110 && b1 == MASK_11111111) {
                return new Object[] { Encoding.UTF_16_BIG_ENDIAN, Boolean.TRUE };
            }
            if (b0 == MASK_11111111 && b1 == MASK_11111110) {
                return new Object[] { Encoding.UTF_16_LITTLE_ENDIAN, Boolean.FALSE };
            }
            // default to UTF-8 if we don't have enough bytes to make a good determination of the encoding
            if (count < 3) {
                return new Object[] { Encoding.UTF_8, null };
            }
            // UTF-8 with a BOM
            int b2 = b4[2] & MASK_11111111;
            if (b0 == MASK_11101111 && b1 == MASK_10111011 && b2 == MASK_10111111) {
                return new Object[] { Encoding.UTF_8, null };
            }
            // default to UTF-8 if we don't have enough bytes to make a good determination of the encoding
            if (count < 2 + 2) {
                return new Object[] { Encoding.UTF_8, null };
            }
            // other encodings
            int b3 = b4[3] & MASK_11111111;
            if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == ASCII.LESS_THAN) {
                // UCS-4, big endian (1234)
                return new Object[] { Encoding.ISO_10646_UCS_4, Boolean.TRUE };
            }
            if (b0 == ASCII.LESS_THAN && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {
                // UCS-4, little endian (4321)
                return new Object[] { Encoding.ISO_10646_UCS_4, Boolean.FALSE };
            }
            if (b0 == 0x00 && b1 == 0x00 && b2 == ASCII.LESS_THAN && b3 == 0x00) {
                // UCS-4, unusual octet order (2143)
                // REVISIT: What should this be?
                return new Object[] { Encoding.ISO_10646_UCS_4, null };
            }
            if (b0 == 0x00 && b1 == ASCII.LESS_THAN && b2 == 0x00 && b3 == 0x00) {
                // UCS-4, unusual octect order (3412)
                // REVISIT: What should this be?
                return new Object[] { Encoding.ISO_10646_UCS_4, null };
            }
            if (b0 == 0x00 && b1 == ASCII.LESS_THAN && b2 == 0x00 && b3 == ASCII.QUESTIONMARK) {
                // UTF-16, big-endian, no BOM
                // (or could turn out to be UCS-2...
                // REVISIT: What should this be?
                return new Object[] { Encoding.UTF_16_BIG_ENDIAN, Boolean.TRUE };
            }
            if (b0 == ASCII.LESS_THAN && b1 == 0x00 && b2 == ASCII.QUESTIONMARK && b3 == 0x00) {
                // UTF-16, little-endian, no BOM
                // (or could turn out to be UCS-2...
                return new Object[] { Encoding.UTF_16_LITTLE_ENDIAN, Boolean.FALSE };
            }
            if (b0 == EBCDIC.LESS_THAN && b1 == EBCDIC.QUESTIONMARK && b2 == EBCDIC.LOWER_X && b3 == EBCDIC.LOWER_M) {
                // EBCDIC
                // a la xerces, return CP037 instead of EBCDIC here
                return new Object[] { Encoding.CP037, null };
            }
            // default encoding
            return new Object[] { Encoding.UTF_8, null };
        }
    }
}
