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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jakop.lotus.domingo.i18n.ResourceManager;
import de.jakop.lotus.domingo.i18n.Resources;

/**
 * Enumeration of all possible locales in Lotus Notes.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoLocale implements Serializable {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 7747855846132804053L;

    /** value of field <tt>rg_date_component_order</tt>. */
    public static final String WEEKDAY_DAY_MONTH_YEAR = "1";

    /** value of field <tt>rg_date_component_order</tt>. */
    public static final String WEEKDAY_MONTH_DAY_YEAR = "0";

    /** value of field <tt>rg_date_component_order</tt>. */
    public static final String YEAR_MONTH_DAY_WEEKDAY = "2";

    /** Map from locale strings to Domino locales. */
    private static final Map LOCALES_MAP;

    /** Maps time zone strings to Lotus Notes format. */
    private static final Resources RESOURCES = ResourceManager.getClassResources(DominoLocale.class).withFailOnError(true);

    private static final List LOCALES;

    private final String fLocaleString;

    private final String fDateComponentOrder;

    private final String fDateSeparator;

    private final String fTimeSeparator;

    private final String fClock24Hour;

    private final String fAmString;

    private final String fPmString;

    private final String fAmPmSuffix;

    private final String fDecimalSeparator;

    private final String fNumberLeadingZero;

    private final String fCurrencySymbol;

    private final String fCurrencySuffix;

    private final String fCurrencySpace;

    private final String fThousandSeparator;

    private final String fYearFormat;

    static {
        LOCALES = new ArrayList();
        LOCALES_MAP = new HashMap();
        Enumeration keys = RESOURCES.getBundle().getKeys();
        while (keys.hasMoreElements()) {
            String locale = (String) keys.nextElement();
            String dominoLocaleString = RESOURCES.getString(locale);
            addLocale(new DominoLocale(locale, dominoLocaleString));
        }
    }

    /**
     * Adds a given locale to the map of all locales.
     *
     * @param locale the locale to add
     */
    private static void addLocale(final DominoLocale locale) {
        LOCALES_MAP.put(locale.getLocale(), locale);
        LOCALES.add(locale);
    }

    /**
     * Constructor.
     * <dl> <dt>Format of parameter <tt>dominoLocaleString</tt></dt><dd>
     * <tt>dateComponentOrder '|' dateSeparator '|' timeSeparator '|'
     * clock24Hour '|' amString '|' pmString '|' amPmSuffix '|'
     * decimalSeparator '|' numberLeadingZero '|' currencySymbol '|'
     * currencySuffix '|' currencySpace '|' thousandSeparator '|'
     * yearFormat</tt></p></dd>
     * <dt>dateComponentOrder</dt><dd>order of date components</dd>
     * <dt>dateSeparator</dt><dd>the date separator</dd>
     * <dt>timeSeparator</dt><dd>the time separator</dd>
     * <dt>clock24Hour</dt><dd>whether 24-hour clock is used</dd>
     * <dt>amString</dt><dd>the AM string</dd>
     * <dt>pmString</dt><dd>the PM string</dd>
     * <dt>amPmSuffix</dt><dd>whether the AM/M string is a suffix to the time or a
     *            prefix</dd>
     * <dt>decimalSeparator</dt><dd>the decimal separator</dd>
     * <dt>numberLeadingZero</dt><dd>whether fractional numbers have a leading number</dd>
     * <dt>currencySymbol</dt><dd>the currency symbol</dd>
     * <dt>currencySuffix</dt><dd>whether the currency symbol is a suffix to the amount
     *            or a prefix</dd>
     * <dt>currencySpace</dt><dd>whether a space is inserted between the amount an
     *            the currency symbol</dd>
     * <dt>thousandSeparator</dt><dd>the thousands separator</dd>
     * <dt>yearFormat</dt><dd>year format, always <tt>"1"</tt></dd>
     * </dl>
     *
     * @param locale the locale string
     * @param dominoLocaleString pipe-seperated list of values
     */
    private DominoLocale(final String locale, final String dominoLocaleString) {
        String[] values = dominoLocaleString.split("\\|");
        int index = 0;
        fLocaleString = locale;
        fDateComponentOrder = values[index++];
        fDateSeparator = values[index++];
        fTimeSeparator = values[index++];
        fClock24Hour = values[index++];
        fAmString = values[index++];
        fPmString = values[index++];
        fAmPmSuffix = values[index++];
        fDecimalSeparator = values[index++];
        fNumberLeadingZero = values[index++];
        fCurrencySymbol = values[index++];
        fCurrencySuffix = values[index++];
        fCurrencySpace = values[index++];
        fThousandSeparator = values[index++];
        fYearFormat = values[index++];
    }

    /**
     * Returns a list of all Domino locales.
     *
     * @return list of locales
     */
    public static List getLocales() {
        return LOCALES;
    }

    /**
     * Returns the Domino locales for a given locale string.
     *
     * @param localeString e.g. <tt>"en-US"</tt> for USA or <tt>de-DE</tt>
     *            for Germany
     * @return Domino locales
     */
    public static DominoLocale get(final String localeString) {
        return (DominoLocale) LOCALES_MAP.get(localeString);
    }

    /**
     * @return Returns the amPmPosition.
     */
    public String getAmPmSuffix() {
        return fAmPmSuffix;
    }

    /**
     * @return Returns the amString.
     */
    public String getAmString() {
        return fAmString;
    }

    /**
     * @return Returns the clock24Hour.
     */
    public String getClock24Hour() {
        return fClock24Hour;
    }

    /**
     * @return Returns the currencyPosition.
     */
    public String getCurrencySuffix() {
        return fCurrencySuffix;
    }

    /**
     * @return Returns the currencySpace.
     */
    public String getCurrencySpace() {
        return fCurrencySpace;
    }

    /**
     * @return Returns the currencyString.
     */
    public String getCurrencySymbol() {
        return fCurrencySymbol;
    }

    /**
     * @return Returns the dateComponentOrder.
     */
    public String getDateComponentOrder() {
        return fDateComponentOrder;
    }

    /**
     * @return Returns the dateString.
     */
    public String getDateSeparator() {
        return fDateSeparator;
    }

    /**
     * @return Returns the decimalString.
     */
    public String getDecimalSeparator() {
        return fDecimalSeparator;
    }

    /**
     * @return Returns the locale.
     */
    public String getLocale() {
        return fLocaleString;
    }

    /**
     * @return Returns the numberLeadingZero.
     */
    public String getNumberLeadingZero() {
        return fNumberLeadingZero;
    }

    /**
     * @return Returns the pmString.
     */
    public String getPmString() {
        return fPmString;
    }

    /**
     * @return Returns the thousandString.
     */
    public String getThousandSeparator() {
        return fThousandSeparator;
    }

    /**
     * @return Returns the timeString.
     */
    public String getTimeSeparator() {
        return fTimeSeparator;
    }

    /**
     * @return Returns the yearFormat.
     */
    public String getYearFormat() {
        return fYearFormat;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object.
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(fLocaleString + ":");
        buffer.append(fDateComponentOrder + ":");
        buffer.append(fDateSeparator + ":");
        buffer.append(fTimeSeparator + ":");
        buffer.append(fClock24Hour + ":");
        buffer.append(fAmString + ":");
        buffer.append(fPmString + ":");
        buffer.append(fAmPmSuffix + ":");
        buffer.append(fDecimalSeparator + ":");
        buffer.append(fNumberLeadingZero + ":");
        buffer.append(fCurrencySymbol + ":");
        buffer.append(fCurrencySuffix + ":");
        buffer.append(fCurrencySpace + ":");
        buffer.append(fThousandSeparator + ":");
        buffer.append(fYearFormat + ":");
        return buffer.toString();
    }
}
