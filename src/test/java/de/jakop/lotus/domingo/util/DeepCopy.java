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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of objects. Objects are first serialized and then
 * deserialized. Error checking is fairly minimal in this implementation. If an object is encountered that cannot be
 * serialized (or that references an object that cannot be serialized) an error is printed to System.err and null is
 * returned.
 */
public final class DeepCopy {

    /**
     * Private Constructor.
     */
    private DeepCopy() {
    }

    /**
     * @param orig the original object to copy
     * @return a copy of the object, or null if the object cannot be serialized.
     */
    public static Object copy(final Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    /**
     * ByteArrayInputStream implementation that does not synchronize methods.
     */
    private static final class FastByteArrayInputStream extends InputStream {

        /**
         * Our byte buffer
         */
        private byte[] buf = null;

        /**
         * Number of bytes that we can read from the buffer
         */
        private int count = 0;

        /**
         * Number of bytes that have been read from the buffer
         */
        private int pos = 0;

        /**
         * Constructor.
         *
         * @param buf a byte array to read from
         * @param count number of bytes to read
         */
        public FastByteArrayInputStream(byte[] buf, int count) {
            this.buf = buf;
            this.count = count;
        }

        /**
         * {@inheritDoc}
         * @see java.io.InputStream#available()
         */
        public int available() {
            return count - pos;
        }

        /**
         * {@inheritDoc}
         * @see java.io.InputStream#read()
         */
        public int read() {
            return (pos < count) ? (buf[pos++] & 0xff) : -1;
        }

        /**
         * {@inheritDoc}
         * @see java.io.InputStream#read(byte[], int, int)
         */
        public int read(final byte[] b, final int off, int len) {
            if (pos >= count) {
                return -1;
            }
            if ((pos + len) > count) {
                len = (count - pos);
            }
            System.arraycopy(buf, pos, b, off, len);
            pos += len;
            return len;
        }

        /**
         * {@inheritDoc}
         * @see java.io.InputStream#skip(long)
         */
        public long skip(long n) {
            if ((pos + n) > count) {
                n = count - pos;
            }
            if (n < 0) {
                return 0;
            }
            pos += n;
            return n;
        }
    }

    /**
     * ByteArrayOutputStream implementation that doesn't synchronize methods and doesn't copy the data on toByteArray().
     */
    private static final class FastByteArrayOutputStream extends OutputStream {
        /**
         * Buffer and size
         */
        private byte[] buf = null;

        private int size = 0;

        /**
         * Constructs a stream with buffer capacity size 5K
         */
        public FastByteArrayOutputStream() {
            this(5 * 1024);
        }

        /**
         * Constructs a stream with the given initial size
         */
        public FastByteArrayOutputStream(int initSize) {
            this.size = 0;
            this.buf = new byte[initSize];
        }

        /**
         * Ensures that we have a large enough buffer for the given size.
         */
        private void verifyBufferSize(int sz) {
            if (sz > buf.length) {
                byte[] old = buf;
                buf = new byte[Math.max(sz, 2 * buf.length)];
                System.arraycopy(old, 0, buf, 0, old.length);
            }
        }

        public int getSize() {
            return size;
        }

        /**
         * Returns the byte array containing the written data. Note that this array will almost always be larger than
         * the amount of data actually written.
         */
        public byte[] getByteArray() {
            return buf;
        }

        /**
         * {@inheritDoc}
         * @see java.io.OutputStream#write(byte[])
         */
        public void write(final byte[] b) {
            verifyBufferSize(size + b.length);
            System.arraycopy(b, 0, buf, size, b.length);
            size += b.length;
        }

        /**
         * {@inheritDoc}
         * @see java.io.OutputStream#write(byte[], int, int)
         */
        public void write(final byte[] b, final int off, final int len) {
            verifyBufferSize(size + len);
            System.arraycopy(b, off, buf, size, len);
            size += len;
        }

        /**
         * {@inheritDoc}
         * @see java.io.OutputStream#write(int)
         */
        public void write(final int b) {
            verifyBufferSize(size + 1);
            buf[size++] = (byte) b;
        }

        public void reset() {
            size = 0;
        }

        /**
         * Returns a ByteArrayInputStream for reading back the written data
         */
        public InputStream getInputStream() {
            return new FastByteArrayInputStream(buf, size);
        }
    }
}