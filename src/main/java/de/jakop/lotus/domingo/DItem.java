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

import java.io.InputStream;
import java.io.Reader;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Represents a discrete value or set of values in a document.
 *
 * <p>The client interface displays items in a document through fields on a
 * form. When a field on a form and an item in a document have the same name,
 * the field displays the item (for example, the Subject field displays the
 * Subject item).</p> <p>All items in a document are accessible
 * programmatically, regardless of what form is used to display the document in
 * the user interface.</p>
 *
 * @author <a href=mailto:markus.toennis@bea.de>Marcus Toennis</a>
 */
public interface DItem extends DBaseItem {

    /** item type. */
    int UNKNOWN = 0;

    /** item type. */
    int RICHTEXT = 1;

    /** item type. */
    int COLLATION = 2;

    /** item type. */
    int NOTEREFS = 4;

    /** item type. */
    int ICON = 6;

    /** item type. */
    int NOTELINKS = 7;

    /** item type. */
    int SIGNATURE = 8;

    /** item type. */
    int USERDATA = 14;

    /** item type. */
    int QUERYCD = 15;

    /** item type. */
    int ACTIONCD = 16;

    /** item type. */
    int ASSISTANTINFO = 17;

    /** item type. */
    int VIEWMAPDATA = 18;

    /** item type. */
    int VIEWMAPLAYOUT = 19;

    /** item type. */
    int LSOBJECT = 20;

    /** item type. */
    int HTML = 21;

    /** item type. */
    int MIME_PART = 25;

    /** item type. */
    int ERRORITEM = 256;

    /** item type. */
    int UNAVAILABLE = 512;

    /** item type. */
    int NUMBERS = 768;

    /** item type. */
    int DATETIMES = 1024;

    /** item type. */
    int NAMES = 1074;

    /** item type. */
    int READERS = 1075;

    /** item type. */
    int AUTHORS = 1076;

    /** item type. */
    int ATTACHMENT = 1084;

    /** item type. */
    int OTHEROBJECT = 1085;

    /** item type. */
    int EMBEDDEDOBJECT = 1090;

    /** item type. */
    int TEXT = 1280;

    /** item type. */
    int FORMULA = 1536;

    /** item type. */
    int USERID = 1792;

    /**
     * Returns the value of an item.
     *
     * <p>If multiple items have the same name, this method returns the value
     * of the first item. Use the Items property to get all the items.</p> <p>If
     * the item has no value, this method returns an empty vector.</p> <p>If
     * no item with the specified name exists, this method returns an empty
     * vector. It does not throw an exception. Use hasItem to verify the
     * existence of an item.</p> <p>This property returns the same value(s)
     * for an item as getValues in Item.</p>
     *
     * @return The value or values contained in the item. The data type of the
     *         value depends on the data type of the item.
     */
    List getValues();

    /**
     * Sets the value of an item. <p>The value of an item in general is a list
     * of values. <p>Legal types for the list elements are:<br/> <ul> <li><code>java.util.Calendar</code></li>
     * <li><code>java.util.Integer</code></li> <li><code>java.util.Double</code></li>
     * <li><code>java.util.String</code></li> </ul></p>
     *
     * @param values the values
     */
    void setValues(List values);

    /**
     * Returns the value of an item with a single text value.
     *
     * <p>If multiple items have the same name, this method returns the value
     * of the first item. Use the Items property to get all the items.</p> <p>
     * The value of the item cannot be null, but if the value is numeric or
     * Calendar, this method returns an empty String.</p> <p>If no item with
     * the specified name exists, this method returns null. It does not throw an
     * exception. Use hasItem to verify the existence of an item.</p> <p>If
     * the item has multiple values, this method returns the first value.</p>
     *
     * @return The value of the item as a String.
     */
    String getValueString();

    /**
     * Returns the value of an item with a single int value.
     *
     * <p>If multiple items have the same name, this method returns the value
     * of the first item. Use the Items property to get all the items.</p> <p>If
     * the value is text and is empty, this method returns <code>null</code>.</p>
     * <p>If the value is text and cannot be converted to an integer, this
     * method returns <code>null</code>.</p> <p>If no item with the
     * specified name exists, this method returns <code>null</code>. It does
     * not throw an exception. Use hasItem to verify the existence of an item.</p>
     * <p>If the item has multiple values, this method returns the first value.</p>
     * <p>A decimal number is rounded down if the fraction is less than 0.5 and
     * up if the fraction is 0.5 or greater.</p>
     *
     * @return The value of the item, rounded to the nearest integer or
     *         <code>null</code>
     */
    Integer getValueInteger();

    /**
     * Returns the value of an item with a single double value.
     *
     * <p>If multiple items have the same name, this method returns the value
     * of the first item. Use the Items property to get all the items.</p> <p>If
     * the value is text and is empty, this method returns <code>null</code>.</p>
     * <p>If the value is text and cannot be converted to a double, this method
     * returns <code>null</code>.</p> <p>If no item with the specified name
     * exists, this method returns <code>null</code>. It does not throw an
     * exception. Use hasItem to verify the existence of an item.</p> <p>If
     * the item has multiple values, this method returns the first value.</p>
     * <p>A decimal number is rounded down if the fraction is less than 0.5 and
     * up if the fraction is 0.5 or greater.</p>
     *
     * @return The value of the item, rounded to the nearest integer or
     *         <code>null</code>
     */
    Double getValueDouble();

    /**
     * Returns the value of an item with a single Calendar value.
     * The date/time value of the item is stored in the calendar in the default
     * time zone of the client.
     *
     * <p>If multiple items have the same name, this method returns the value
     * of the first item. Use the Items property to get all the items.</p> <p>If
     * value is numeric or text, this method returns <code>null</code>.</p>
     * <p>If no item with the specified name exists, this method returns
     * <code>null</code>. It does not throw an exception. Use hasItem to
     * verify the existence of an item.</p> <p>If the item has multiple
     * values, this method returns the first value.</p>
     *
     * @return The value of the item as a Calendar or <code>null</code> if the
     *         item doesn't contain a date/time value
     */
    Calendar getValueDateTime();

    /**
     * Returns the value of an item with two Calendar value, a date range.
     *
     * <p>If multiple items have the same name, this method returns the value
     * of the first item. Use the Items property to get all the items.</p> <p>If
     * value is numeric or text, this method returns <code>null</code>.</p>
     * <p>If no item with the specified name exists, this method returns
     * <code>null</code>. It does not throw an exception. Use hasItem to
     * verify the existence of an item.</p>
     *
     * @return The value of the item as a DDateRange or <code>null</code> if
     *         the item doesn't contain any date/time value. The second value
     *         may be <code>null</code> if only one date/time was specified.
     */
    DDateRange getValueDateRange();

    /**
     * Sets the value of an item to a single string value.
     *
     * @param value the value to set
     */
    void setValueString(String value);

    /**
     * Sets the value of an item to a single numeric value.
     *
     * <p>If you want to simulate an empty field in the Lotus Notes Client, you
     * should use this method with an object argument.
     *
     * @param i the value
     * @see DItem#setValueDouble(java.lang.Double)
     */
    void setValueInteger(int i);

    /**
     * Sets the value of an item to a single numeric value.
     *
     * <p>If the value is <code>null</code>, a <code>TEXT</code> item with
     * an empty string value is set. This behavior is the same as the Lotus
     * Notes Client does with empty fields.</p>
     *
     * @param i the value
     */
    void setValueInteger(Integer i);

    /**
     * Sets the value of an item to a single numeric value.
     *
     * <p>If you want to simulate an empty field in the Lotus Notes Client, you
     * should use this method with an object argument.
     *
     * @param d the value
     * @see DItem#setValueDouble(java.lang.Double)
     */
    void setValueDouble(double d);

    /**
     * Sets the value of an item to a single numeric value.
     *
     * <p>If the value is <code>null</code>, a <code>TEXT</code> item with
     * an empty string value is set. This behavior is the same as the Lotus
     * Notes Client does with empty fields.</p>
     *
     * @param d the value
     */
    void setValueDouble(Double d);

    /**
     * Sets the value of an item to a single datetime value. The milliseconds
     * are cut off.
     *
     * @param calendar the date
     */
    void setValueDateTime(Calendar calendar);

    /**
     * Sets the value of an item to a single time zone value.
     *
     * @param timezone the time zone
     */
    void setValueDateTime(TimeZone timezone);

    /**
     * Sets the value of an item to a date range. <p>If the value is
     * <code>null</code>, a <code>TEXT</code> item with an empty string
     * value is set. This behavior is the same as the Lotus Notes Client does
     * with empty fields.</p> <p>If both calendars are null, an item with an
     * empty string is created. If only one calendar is not null, this calendar
     * is stored as a single date value.</p> The milliseconds are cut off.
     *
     * @param dateRange the date range
     */
    void setValueDateRange(DDateRange dateRange);

    /**
     * Sets the value of an item to a date range. <p>If both calendars are
     * null, an item with an empty string is created. If only one calendar is
     * not null, this calendar is stored as a single date value.</p> The
     * milliseconds are cut off.
     *
     * @param calendar1 start date/time of range
     * @param calendar2 end date/time of range
     */
    void setValueDateRange(Calendar calendar1, Calendar calendar2);

    /**
     * For an item that's a text list, adds a new value to the item without
     * erasing any existing values.
     *
     * @param value The string you want to add to the item.
     */
    void appendToTextList(String value);

    /**
     * For an item that's a text list, adds a new value to the item without
     * erasing any existing values.
     *
     * @param values The string(s) you want to add to the item. Each list
     *            element is an object of type String.
     * @see #appendToTextList(java.lang.String)
     */
    void appendToTextList(List values);

    /**
     * Checks whether a value matches at least one of an item's values.
     *
     * @param value String value to check for
     * @return <tt>true</tt> if the value matches one of the values in the
     *         item
     */
    boolean containsValue(String value);

    /**
     * Checks whether a value matches at least one of an item's values.
     *
     * @param value integer value to check for
     * @return <tt>true</tt> if the value matches one of the values in the
     *         item
     */
    boolean containsValue(Integer value);

    /**
     * Checks whether a value matches at least one of an item's values.
     *
     * @param value integer value to check for
     * @return <tt>true</tt> if the value matches one of the values in the
     *         item
     */
    boolean containsValue(int value);

    /**
     * Checks whether a value matches at least one of an item's values.
     *
     * @param value integer value to check for
     * @return <tt>true</tt> if the value matches one of the values in the
     *         item
     */
    boolean containsValue(Double value);

    /**
     * Checks whether a value matches at least one of an item's values.
     *
     * @param value integer value to check for
     * @return <tt>true</tt> if the value matches one of the values in the
     *         item
     */
    boolean containsValue(double value);

    /**
     * Checks whether a value matches at least one of an item's values.
     *
     * @param value Calendar value to check for
     * @return <tt>true</tt> if the value matches one of the values in the
     *         item
     */
    boolean containsValue(Calendar value);

    /**
     * Indicates whether an item contains summary or non-summary data.
     *
     * <p>Items are flagged as containing summary or non-summary data. Summary
     * data can appear in views and folders; non-summary data cannot. In
     * general, items created through the UI are tagged as non-summary if they
     * contain rich text or are very long.</p>
     *
     * <p>When you create a new item using appendItemValue or replaceItemValue
     * in NotesDocument, the isSummary property for the item is true. If you
     * don't want the item to appear in views and folders, you must change its
     * IsSummary property to false.</p>
     *
     * <p>You can enable or disable the appearance of an existing item in views
     * and folders by changing its IsSummary property.</p>
     *
     * <p>An item whose IsSummary property is true may not appear as expected
     * in views and folders if the data is not suitable. For example, a rich
     * text item whose IsSummary property is true generally appears as a
     * question mark.</p>
     *
     * @return <code>true</code> if the item contains summary data
     *         <code>false</code> if the item contains non-summary data
     */
    boolean isSummary();

    /**
     * Indicates whether an item contains summary or non-summary data.
     *
     * <p>Items are flagged as containing summary or non-summary data. Summary
     * data can appear in views and folders; non-summary data cannot. In
     * general, items created through the UI are tagged as non-summary if they
     * contain rich text or are very long.</p>
     *
     * <p>When you create a new item using appendItemValue or replaceItemValue
     * in NotesDocument, the isSummary property for the item is true. If you
     * don't want the item to appear in views and folders, you must change its
     * IsSummary property to false.</p>
     *
     * <p>You can enable or disable the appearance of an existing item in views
     * and folders by changing its IsSummary property.</p>
     *
     * <p>Example:<br/>
     *
     * <pre>
     * DDocument doc;
     * //...set value of doc...
     * doc.replaceItemValue(&quot;MyNoSummaryItem&quot;, &quot;myValue&quot;).setSummary(false);
     * </pre>
     *
     * </p>
     *
     * @param flag <code>true</code> if the item contains summary data
     *            <code>false</code> if the item contains non-summary data
     */
    void setSummary(boolean flag);

    /**
     * Indicates whether an item is of type Readers.
     *
     * <p><b>Usage</b><br/> A Readers item contains a list of user names
     * indicating people who have Reader access to a document. A Readers item
     * returns <code>Item.TEXT</code> for getType.</p>
     *
     * @return <code>true</code> if the item is of type Readers, else
     *         <code>false</code>.
     */
    boolean isReaders();

    /**
     * Indicates whether an item is of type Names.
     *
     * <p><b>Usage</b><br/> A Names item contains a list of user names. A
     * Names item returns <code>Item.TEXT</code> for getType.</p>
     *
     * @return <code>true</code> if the item is of type Names, else
     *         <code>false</code>.
     */
    boolean isNames();

    /**
     * Indicates whether an item is of type Names.
     *
     * <p><b>Usage</b><br/> A Names item contains a list of user names. A
     * Readers item returns <code>Item.TEXT</code> for getType.
     *
     * <p>Example:<br/>
     *
     * <pre>
     * DDocument doc;
     * //...set value of doc...
     * doc.replaceItemValue(&quot;MyNames&quot;, &quot;[Admin]&quot;).setNames(true);
     * </pre>
     *
     * </p>
     *
     * @param flag <code>true</code> if the item should be of type Names, else<code>false</code>.
     */
    void setNames(boolean flag);

    /**
     * Indicates whether an item is of type Readers.
     *
     * <p><b>Usage</b><br/> A Readers item contains a list of user names
     * indicating people who have Reader access to a document. A Readers item
     * returns <code>Item.TEXT</code> for getType.
     *
     * <p>Example:<br/>
     *
     * <pre>
     * DDocument doc;
     * //...set value of doc...
     * doc.replaceItemValue(&quot;MyReaders&quot;, &quot;[Admin]&quot;).setReaders(true);
     * </pre>
     *
     * </p>
     *
     * @param flag <code>true</code> if the item should be of type Readers,
     *            else<code>false</code>.
     */
    void setReaders(boolean flag);

    /**
     * Indicates whether an item is of type Authors.
     *
     * <p><b>Usage</b><br/> An Authors item contains a list of user names
     * indicating people who have Author access to a document. An Authors item
     * returns <code>Item.TEXT</code> for getType.
     *
     * @return <code>true</code> if the item is of type Authors, else
     *         <code>false</code>.
     */
    boolean isAuthors();

    /**
     * Indicates whether a user needs at least Editor access to modify an item.
     *
     * @return <code>true</code> if you need at least Editor access to modify
     *         the item, <code>false</code> if you do not need Editor access
     *         to modify the item; you can modify it as long as you have Author
     *         access or better
     */
    boolean isProtected();

    /**
     * Indicates whether an item is of type Authors.
     *
     * <p><b>Usage</b><br/> An Authors item contains a list of user names
     * indicating people who have Author access to a document. An Authors item
     * returns <code>Item.TEXT</code> for getType.
     *
     * <p>Example:<br/>
     *
     * <pre>
     * DDocument doc;
     * //...set value of doc...
     * doc.replaceItemValue(&quot;MyAuthors&quot;, &quot;[Admin]&quot;).setAuthors(true);
     * </pre>
     *
     * </p>
     *
     * @param flag <code>true</code> if the item should be of type Authors,
     *            else<code>false</code>.
     */
    void setAuthors(boolean flag);

    /**
     * Indicates whether a user needs at least Editor access to modify an item.
     *
     * @param flag <code>true</code> if you need at least Editor access to
     *            modify the item, <code>false</code> if you do not need
     *            Editor access to modify the item; you can modify it as long as
     *            you have Author access or better
     */
    void setProtected(boolean flag);

    /**
     * Returns the number of values in a item.
     *
     * @return number of values in an item.
     */
    int getSize();

    /**
     * Abbreviates the contents of a text item.
     *
     * @param maxlen The maximum length of the abbreviation.
     * @param dropVowels Specify true if you want to drop vowels from the words
     *            in the item. Otherwise, specify false.
     * @param userDict Specify true if you want to use the table of
     *            abbreviations in NOTEABBR.TXT. Otherwise, specify false.
     * @return The contents of the item, with vowels dropped and abbreviations
     *         substituted (if specified), then truncated to fit to maximum length.
     */
    String abstractText(int maxlen, boolean dropVowels, boolean userDict);

    /**
     * Copies an item to a specified document. <p>When you call this method
     * using a RichTextItem object, file attachments, embedded objects, and
     * object links that are contained within the rich-text item are not copied
     * to the destination document.</p>
     *
     * @param document The document on which to create the item. Cannot be null.
     * @return The new item.
     */
    DItem copyItemToDocument(DDocument document);

    /**
     * Copies an item to a specified document. <p>When you call this method
     * using a RichTextItem object, file attachments, embedded objects, and
     * object links that are contained within the rich-text item are not copied
     * to the destination document.</p>
     *
     * @param document The document on which to create the item. Cannot be null.
     * @param newName The name of the new item. Specify an empty string ("") if
     *            you want to keep the name of the original item.
     * @return The new item.
     */
    DItem copyItemToDocument(DDocument document, String newName);

    /**
     * The date that an item was last modified.
     *
     * @return The date that an item was last modified
     */
    Calendar getLastModified();

    /**
     * A plain text representation of an item's value.
     *
     * <p>Multiple values in a list are separated by semicolons in the returned
     * string. If an item's value is large, the returned string may be
     * truncated.</p> <p>For rich-text items, this property skips non-text
     * data such as bitmaps and file attachments.</p> <p>For HTML items, this
     * property returns null.</p>
     *
     * @return plain text representation of an item's value
     */
    String getText();

    /**
     * A plain text representation of an item's value.
     *
     * <p>Multiple values in a list are separated by semicolons in the returned
     * string. If an item's value is large, the returned string may be
     * truncated.</p> <p>For rich-text items, this property skips non-text
     * data such as bitmaps and file attachments.</p> <p>For HTML items, this
     * property returns null.</p>
     *
     * @param maxLen Maximum length of returned text
     * @return plain text representation of an item's value
     */
    String getText(int maxLen);

    /**
     * The data type of an item.
     *
     * @return type of an item
     */
    int getType();

    /**
     * The bytes of internal storage, including overhead, required to store an item.
     *
     * @return length of value
     */
    int getValueLength();

    /**
     * Sets the value of an item to custom data from an object.
     *
     * <p>The new value replaces the existing value.</p>
     *
     * <p>To keep the changes, you must call Document.save after calling
     * setValueCustomData.</p>
     *
     * <p>The custom data cannot exceed 64K.</p>
     *
     * <p>If you intend to get the custom data through a language binding other
     * than Java, use a "Bytes" method.</p>
     *
     * @param type A name for the data type. When getting custom data, use this
     *            name for verification.
     * @param obj An object that contains the custom data. The class that
     *            defines this object must implement Serializable. If desired,
     *            you can override readObject and writeObject.
     *
     * @since Lotus Notes/Domino Release 6
     */
    void setValueCustomData(String type, Object obj);

    /**
     * Sets the value of an item to custom data from an object.
     *
     * <p>The new value replaces the existing value.</p>
     *
     * <p>To keep the changes, you must call Document.save after calling
     * setValueCustomData.</p>
     *
     * <p>The custom data cannot exceed 64K.</p>
     *
     * <p>If you intend to get the custom data through a language binding other
     * than Java, use a "Bytes" method.</p>
     *
     * @param obj An object that contains the custom data. The class that
     *            defines this object must implement Serializable. If desired,
     *            you can override readObject and writeObject.
     *
     * @since Lotus Notes/Domino Release 6
     */
    void setValueCustomData(Object obj);

    /**
     * Sets the value of an item to custom data from a byte array.
     *
     * <p>The new value replaces the existing value.</p>
     *
     * <p>To keep the changes, you must call Document.save after calling
     * setValueCustomData.</p>
     *
     * <p>The custom data cannot exceed 64K.</p>
     *
     * <p>If you intend to get the custom data through a language binding other
     * than Java, use a "Bytes" method.</p>
     *
     * @param type A name for the data type. When getting custom data, use this
     *            name for verification.
     * @param bytes A byte array that contains the custom data.
     *
     * @since Lotus Notes/Domino Release 6
     */
    void setValueCustomDataBytes(String type, byte[] bytes);

    /**
     * Returns as an object the value of an item containing custom data.
     *
     * @param type The name of the data type. If specified, this name must match
     *            the data type name specified when the item was written. If
     *            omitted, no name checking occurs.
     * @return An object that receives the value of the item. Must have the same
     *         class definition as the object written to the item.
     *
     * @see #setValueCustomData(String, Object)
     */
    Object getValueCustomData(String type);

    /**
     * Returns as an object the value of an item containing custom data.
     *
     * @return An object that receives the value of the item. Must have the same
     *         class definition as the object written to the item.
     *
     * @see #setValueCustomData(String, Object)
     */
    Object getValueCustomData();

    /**
     * Returns as a byte array the value of an item containing custom data.
     *
     * @param type The name of the data type. This name must match the data type
     *            name specified when the item was written.
     * @return A byte array that receives the value of the item.
     *
     * @see #setValueCustomDataBytes(String, byte[])
     */
    byte[] getValueCustomDataBytes(String type);

    /**
     * Indicates whether an item is encrypted.
     *
     * <p>If you set this property to true, the item is not actually encrypted
     * until you call {@link DDocument#encrypt()} on the parent Document.</p>
     *
     * @return <code>true</code> if the item is encrypted, else
     *         <code>false</code>
     */
    boolean isEncrypted();

    /**
     * Indicates whether an item is encrypted.
     *
     * @param flag <code>true</code> if the item is encrypted, else
     *            <code>false</code>
     */
    void setEncrypted(boolean flag);

    /**
     * Indicates whether an item is saved when the document is saved.
     *
     * @return flag <code>true</code> (default) if the item is saved when the
     *         document is saved, else <code>false</code>
     */
    boolean isSaveToDisk();

    /**
     * Indicates whether an item is saved when the document is saved.
     *
     * @param flag <code>true</code> (default) if the item is saved when the
     *            document is saved, else <code>false</code>
     */
    void setSaveToDisk(boolean flag);

    /**
     * Indicates whether a document contains a signature.
     *
     * <p>use {@link DDocument#getSigner()} and {@link DDocument#getVerifier()}
     * to get the Signer and Verifier for a signed document. To access the
     * signature itself, you must find the item of type SIGNATURE in the
     * document.</p>
     *
     * @return <code>true</code> if the document contains one or more
     *         signatures, else <code>false</code>
     */
    boolean isSigned();

    /**
     * Indicates whether an item contains a signature the next time the document
     * is signed.
     *
     * @param flag <code>true</code> if the item is signed when the document
     *            is next signed
     */
    void setSigned(boolean flag);

    /**
     * Contents of an EmbeddedObject, Item, or MIMEEntity object in the form of
     * a java.io.Reader object.
     *
     * @return Contents of an EmbeddedObject, Item, or MIMEEntity object in the
     *         form of a java.io.Reader object
     *
     * <p>This method creates a temporary file. The file is deleted when
     * EmbeddedObject is recycled.</p>
     */
    Reader getReader();

    /**
     * InputStream representation of the contents of an EmbeddedObject, Item, or
     * MIMEEntity object.
     *
     * <p>This method creates a temporary file. The file is deleted when
     * EmbeddedObject is recycled.</p>
     *
     * @return InputStream representation of the contents of an EmbeddedObject,
     *         Item, or MIMEEntity object
     */
    InputStream getInputStream();
}
