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

package de.jakop.lotus.domingo.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.mock.MockDocument;

/**
 * Tests for class {@link DirectMapper}.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class DirectMapperTest extends BaseMapperTest {

    private static final String TEST_ITEM_NAME = "Test";

    private static final String TEST_VALUE = "test-value";

    /**
     * Test mapping a string.
     */
    public void testMapString() {
        DirectMapper mapper = getDirectMapper(TEST_ITEM_NAME, "TestString", String.class);
        DDocument doc = new MockDocument();
        doc.replaceItemValue(TEST_ITEM_NAME, TEST_VALUE);
        TestClass instance = new TestClass();
        map(mapper, doc, instance);
        assertEquals(TEST_VALUE, instance.getTestString());
        instance.setTestString(TEST_VALUE);
        map(mapper, instance, doc);
        assertEquals(TEST_VALUE, doc.getItemValueString(TEST_ITEM_NAME));
    }

    /**
     * Test mapping a list.
     */
    public void testMapList() {
        List values = new ArrayList();
        values.add(TEST_VALUE);
        values.add(TEST_VALUE);
        DirectMapper mapper = getDirectMapper(TEST_ITEM_NAME, "TestList", List.class);
        DDocument doc = new MockDocument();
        doc.replaceItemValue(TEST_ITEM_NAME, values);
        TestClass instance = new TestClass();
        map(mapper, doc, instance);
        assertEquals(2, instance.getTestList().size());
        assertEquals(TEST_VALUE, instance.getTestList().get(0));
        assertEquals(TEST_VALUE, instance.getTestList().get(1));
        instance.setTestList(values);
        map(mapper, instance, doc);
        assertEquals(2, doc.getItemValue(TEST_ITEM_NAME).size());
        assertEquals(TEST_VALUE, doc.getItemValue(TEST_ITEM_NAME).get(0));
        assertEquals(TEST_VALUE, doc.getItemValue(TEST_ITEM_NAME).get(1));
    }

    /**
     * Test mapping a collection.
     */
    public void testMapIntegerType() {
        DirectMapper mapper = getDirectMapper(TEST_ITEM_NAME, "TestIntType", Integer.TYPE);
        DDocument doc = new MockDocument();
        doc.replaceItemValue(TEST_ITEM_NAME, 2);
        TestClass instance = new TestClass();
        map(mapper, doc, instance);
        assertEquals(2, instance.getTestIntType());
        instance.setTestIntType(-2);
        map(mapper, instance, doc);
        assertEquals(new Integer(-2), doc.getItemValueInteger(TEST_ITEM_NAME));
        instance.setTestIntType(2);
        map(mapper, instance, doc);
        assertEquals(new Integer(2), doc.getItemValueInteger(TEST_ITEM_NAME));
    }

    /**
     * Test mapping a collection.
     */
    public void testMapIntegerClass() {
        DirectMapper mapper = getDirectMapper(TEST_ITEM_NAME, "TestIntegerClass", Integer.class);
        DDocument doc = new MockDocument();
        doc.replaceItemValue(TEST_ITEM_NAME, new Integer(2));
        TestClass instance = new TestClass();
        map(mapper, doc, instance);
        assertEquals(new Integer(2), instance.getTestIntegerClass());
        instance.setTestIntegerClass(new Integer(-2));
        map(mapper, instance, doc);
        assertEquals(new Integer(-2), doc.getItemValueInteger(TEST_ITEM_NAME));
        instance.setTestIntegerClass(new Integer(2));
        map(mapper, instance, doc);
        assertEquals(new Integer(2), doc.getItemValueInteger(TEST_ITEM_NAME));
    }

    /**
     * Test mapping a collection.
     */
    public void testMapDoubleType() {
        DirectMapper mapper = getDirectMapper(TEST_ITEM_NAME, "TestDoubleType", Double.TYPE);
        DDocument doc = new MockDocument();
        doc.replaceItemValue(TEST_ITEM_NAME, 2);
        TestClass instance = new TestClass();
        map(mapper, doc, instance);
        assertEquals(2.0, instance.getTestDoubleType(), 0.00001);
        instance.setTestDoubleType(-2.3);
        map(mapper, instance, doc);
        assertEquals(new Double(-2.3), doc.getItemValueDouble(TEST_ITEM_NAME));
        instance.setTestDoubleType(2.1);
        map(mapper, instance, doc);
        assertEquals(new Double(2.1), doc.getItemValueDouble(TEST_ITEM_NAME));
    }

    /**
     * Test mapping a collection.
     */
    public void testMapDoubleClass() {
        DirectMapper mapper = getDirectMapper(TEST_ITEM_NAME, "TestDoubleClass", Double.class);
        DDocument doc = new MockDocument();
        doc.replaceItemValue(TEST_ITEM_NAME, new Double(2));
        TestClass instance = new TestClass();
        map(mapper, doc, instance);
        assertEquals(new Double(2), instance.getTestDoubleClass());
        instance.setTestDoubleClass(new Double(-2.3));
        map(mapper, instance, doc);
        assertEquals(new Double(-2.3), doc.getItemValueDouble(TEST_ITEM_NAME));
        instance.setTestDoubleClass(new Double(2.1));
        map(mapper, instance, doc);
        assertEquals(new Double(2.1), doc.getItemValueDouble(TEST_ITEM_NAME));
    }

    /**
     * Test mapping a collection.
     */
    public void testMapBooleanType() {
        DirectMapper mapper = getDirectMapper(TEST_ITEM_NAME, "TestBooleanType", Boolean.TYPE);
        DDocument doc = new MockDocument();
        doc.replaceItemValue(TEST_ITEM_NAME, "1");
        TestClass instance = new TestClass();
        instance.setTestBooleanType(false);
        map(mapper, instance, doc);
        assertEquals("0", doc.getItemValueString(TEST_ITEM_NAME));
        instance.setTestBooleanType(true);
        map(mapper, instance, doc);
        assertEquals("1", doc.getItemValueString(TEST_ITEM_NAME));
    }

    /**
     * Test mapping a collection.
     */
    public void testMapBooleanClass() {
        DirectMapper mapper = getDirectMapper("Test", "TestBooleanClass", Boolean.class);
        DDocument doc = new MockDocument();
        doc.replaceItemValue("Test", "1");
        TestClass instance = new TestClass();
        map(mapper, doc, instance);
        assertEquals(Boolean.TRUE, instance.getTestBooleanClass());
        instance.setTestBooleanClass(Boolean.FALSE);
        map(mapper, instance, doc);
        assertEquals("0", doc.getItemValueString(TEST_ITEM_NAME));
        instance.setTestBooleanClass(Boolean.TRUE);
        map(mapper, instance, doc);
        assertEquals("1", doc.getItemValueString(TEST_ITEM_NAME));
    }

    /**
     * Creates a {@link DirectMapper} while handling all exceptions.
     * Fails in sense of JUnit if the mapper cannot be created.
     *
     * @param itemName name of Notes item
     * @param attributeName name of Java attribute
     * @param clazz the value type
     * @return new {@link DirectMapper}
     */
    private DirectMapper getDirectMapper(String itemName, String attributeName, Class clazz) {
        try {
            return new DirectMapper(itemName, attributeName, clazz);
        } catch (MethodNotFoundException e) {
            fail("Cannot create DirectMapper: " + e.getMessage());
            return null;
        }
    }

    /**
     * Test class for mapping tests.
     */
    public static final class TestClass {

        private String fTestString;

        private List fTestList;

        private Collection fTestCollection;

        private Boolean fTestBooleanClass;

        private boolean fTestBoolean;

        private int fTestIntType;

        private Integer fTestIntegerClass;

        private double fTestDoubleType;

        private Double fTestDoubleClass;

        /**
         * @return test list
         */
        public List getTestList() {
            return fTestList;
        }

        /**
         * @param testList test list
         */
        public void setTestList(final List testList) {
            fTestList = testList;
        }
        
        /**
         * @return test string
         */
        public String getTestString() {
            return fTestString;
        }

        /**
         * @param testString test string
         */
        public void setTestString(final String testString) {
            fTestString = testString;
        }

        /**
         * @return test collection
         */
        public Collection getTestCollection() {
            return fTestCollection;
        }

        /**
         * @param testCollection test collection
         */
        public void setTestCollection(final Collection testCollection) {
            fTestCollection = testCollection;
        }

        /**
         * @return int
         */
        public int getTestIntType() {
            return fTestIntType;
        }

        /**
         * @param testInt int
         */
        public void setTestIntType(final int testInt) {
            fTestIntType = testInt;
        }

        /**
         * @return Integer
         */
        public Integer getTestIntegerClass() {
            return fTestIntegerClass;
        }

        /**
         * @param testInteger Integer
         */
        public void setTestIntegerClass(final Integer testInteger) {
            fTestIntegerClass = testInteger;
        }
        
        /**
         * @return Double
         */
        public Double getTestDoubleClass() {
            return fTestDoubleClass;
        }

        /**
         * @param testDoubleClass Double
         */
        public void setTestDoubleClass(final Double testDoubleClass) {
            fTestDoubleClass = testDoubleClass;
        }

        /**
         * @return double
         */
        public double getTestDoubleType() {
            return fTestDoubleType;
        }

        /**
         * @param testDoubleType double
         */
        public void setTestDoubleType(final double testDoubleType) {
            fTestDoubleType = testDoubleType;
        }

        /**
         * @return boolean
         */
        public boolean isTestBooleanType() {
            return fTestBoolean;
        }

        /**
         * @param testBoolean boolean
         */
        public void setTestBooleanType(final boolean testBoolean) {
            fTestBoolean = testBoolean;
        }

        /**
         * @return Boolean
         */
        public Boolean getTestBooleanClass() {
            return fTestBooleanClass;
        }

        /**
         * @param testBooleanClass Boolean
         */
        public void setTestBooleanClass(final Boolean testBooleanClass) {
            fTestBooleanClass = testBooleanClass;
        }
    }
}
