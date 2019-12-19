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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.Cookie;

/**
 * Domino regional preferences.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class DominoPreferences {

    /** Number of years that a preferences cookie is valid. */
    public static final int EXPIRY_YEARS = 30;

    /** Default encoding (UTF-8) for cookie values. */
    public static final String ENCODING_UTF_8 = "UTF-8";

    /** Default time zone search string. */
    public static final String DEFAULT_TIME_ZONE = "Pacific Time (US & Canada)";

    /** The locale settings. */
    public static final DominoLocale DEFAULT_LOCALE = DominoLocale.get("en-US");

    /** List of {@link org.apache.commons.httpclient.Header}s. */
    private List fCookies = new ArrayList();

    /** Domain of the generated cookies. */
    private final String fDomain;

    /** path of the generated cookies. */
    private String fPath = "/";

    /** The time zone. */
    private DominoTimeZone fTimeZone = DominoTimeZone.searchTimeZone(DEFAULT_TIME_ZONE);

    /** Indicates whether the time format reflects daylight savings time. */
    private boolean fDst;

    /** locale string. */
    private String fLocaleString;

    /** Indicates whether the order of the date format is day-month-year, month-day-year or year-month-day. */
    private String fDateComponentOrder;

    /** The character used to separate months, days, and years, for example, the slash.. */
    private String fDateSeparator;

    /** The character used to separate hours, minutes, and seconds, for example, the colon.. */
    private String fTimeSeparator;

    /** Indicates whether the time format is 24-hour. */
    private boolean fClock24Hour;

    /** The string that denotes AM time, for example, "AM" in English. */
    private String fAmString;

    /** The string that denotes PM time, for example, "PM" in English. */
    private String fPmString;

    /** Indicates whether the AM/PM symbol follows the time in the time format. */
    private boolean fAmPmSuffix;

    /** The decimal separator for number format, for example, the decimal point. */
    private String fDecimalSeparator;

    /** The thousand separator in number format, for example, the comma. */
    private String fThousandSeparator;

    /** Year format. */
    private String fYearFormat;

    /** Indicates whether fractions have a zero before the decimal point in number format. */
    private boolean fNumberLeadingZero;

    /** The symbol that indicates a number is currency, for example, the dollar sign. */
    private String fCurrencySymbol;

    /** Indicates whether the currency symbol follows the number in the currency format. */
    private boolean fCurrencySuffix;

    /** Indicates whether the currency format has a space between the currency symbol and the number.. */
    private boolean fCurrencySpace;

    /** The date/time when thre preferences will expire. */
    private final Date fExpiryDate;

    /** The cookie that represents the time zone settings. */
    private Cookie fTimeZoneCookie;

    /** The cookie that represents the locale settings. */
    private Cookie fRegionalCookie;

    /**
     * Constructor.
     *
     * @param domain the domain to that this preferences belong to
     */
    public DominoPreferences(final String domain) {
        this(domain, "/");
    }

    /**
     * Constructor.
     *
     * @param domain the domain to that this preferences belong to
     * @param path the path defining the subset of URLs in a domain for which the cookie is valid
     */
    public DominoPreferences(final String domain, final String path) {
        fDomain = domain;
        fPath = path;
        Calendar creationDate = Calendar.getInstance();
        creationDate.add(Calendar.YEAR, EXPIRY_YEARS);
        fExpiryDate = creationDate.getTime();
        setLocale("en-US", true);
        fTimeZoneCookie = newTimeZoneCookie();
        fRegionalCookie = newRegionalCookie();
    }

    /**
     * Sets the time zone.
     *
     * @param timeZone Domino time zone
     */
    public void setTimeZone(final DominoTimeZone timeZone) {
        fTimeZone = timeZone;
    }

    /**
     * Sets whether to observe daylight saving time or not.
     *
     * @param dst observe daylight saving time or not
     */
    public void setObserverDST(final boolean dst) {
        fDst = dst;
    }

    /**
     * @param locale The locale to set.
     */
    public void setLocale(final String locale) {
        setLocale(locale, false);
    }

    /**
     * @param localeString The locale to set as a string.
     * @param loadDefault if <code>true</code>, load all default values for
     *            this locale
     */
    public void setLocale(final String localeString, final boolean loadDefault) {
        fLocaleString = localeString;
        DominoLocale locale = (DominoLocale) DominoLocale.get(localeString);
        if (locale == null) {
            throw new NotesHttpRuntimeException("unsupported locale: " + localeString);
        }
        fLocaleString = locale.getLocale();
        fDateComponentOrder = locale.getDateComponentOrder();
        fDateSeparator = locale.getDateSeparator();
        fTimeSeparator = locale.getTimeSeparator();
        fClock24Hour = "".equals(locale.getClock24Hour());
        fAmString = locale.getAmString();
        fPmString = locale.getPmString();
        fAmPmSuffix = "1".indexOf(locale.getAmPmSuffix()) >= 0;
        fDecimalSeparator = locale.getDecimalSeparator();
        fNumberLeadingZero = "1".equals(locale.getNumberLeadingZero());
        fCurrencySymbol = locale.getCurrencySymbol();
        fCurrencySuffix = "1".equals(locale.getCurrencySuffix());
        fCurrencySpace = "1".equals(locale.getCurrencySpace());
        fThousandSeparator = locale.getThousandSeparator();
        fYearFormat = locale.getYearFormat();
    }

    /**
     * @param amString The amString to set.
     */
    public void setAmString(final String amString) {
        fAmString = amString;
    }

    /**
     * @param currencySpace The currencySpace to set.
     */
    public void setCurrencySpace(final boolean currencySpace) {
        fCurrencySpace = currencySpace;
    }

    /**
     * @param currencySymbol The currencySymbol to set.
     */
    public void setCurrencySymbol(final String currencySymbol) {
        fCurrencySymbol = currencySymbol;
    }

    /**
     * @param dateSeperator The dateSeperator to set.
     */
    public void setDateSeperator(final String dateSeperator) {
        fDateSeparator = dateSeperator;
    }

    /**
     * @param decimalSeperator The decimalSeperator to set.
     */
    public void setDecimalSeperator(final String decimalSeperator) {
        fDecimalSeparator = decimalSeperator;
    }

    /**
     * @param cookies The cookies to set.
     */
    public void setCookies(final List cookies) {
        fCookies = cookies;
    }

    /**
     * @param dst The dst to set.
     */
    public void setDst(final boolean dst) {
        fDst = dst;
    }

    /**
     * @param hour24Format The hourFormat to set.
     */
    public void setHourFormat(final boolean hour24Format) {
        fClock24Hour = hour24Format;
    }

    /**
     * @param leadingDecimalZeros The leadingDecimalZeros to set.
     */
    public void setLeadingDecimalZeros(final boolean leadingDecimalZeros) {
        fNumberLeadingZero = leadingDecimalZeros;
    }

    /**
     * @param pmString The pmString to set.
     */
    public void setPmString(final String pmString) {
        fPmString = pmString;
    }

    /**
     * @param amPmSuffix The amPmSuffix to set.
     */
    public void setAmPmSuffix(final boolean amPmSuffix) {
        fAmPmSuffix = amPmSuffix;
    }

    /**
     * @param currencySuffix The currencySuffix to set.
     */
    public void setPositionCurrencySymbol(final boolean currencySuffix) {
        fCurrencySuffix = currencySuffix;
    }

    /**
     * @param thousandsSeperator The thousandsSeperator to set.
     */
    public void setThousandsSeperator(final String thousandsSeperator) {
        fThousandSeparator = thousandsSeperator;
    }

    /**
     * @param timeSeperator The timeSeperator to set.
     */
    public void setTimeSeperator(final String timeSeperator) {
        fTimeSeparator = timeSeperator;
    }

    /**
     * Returns a list of cookies for the current regional settings.
     *
     * @return cookies for regional settings
     */
    public List getRegionalCookies() {
        return fCookies;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return  a string representation of the object.
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

    /**
     * Creates the time zone cookie of Lotus Domino.
     *
     * <p>Format:</p>
     * <dl>
     * <dt>Name</dt><dd><tt>DomTimeZonePrfM</tt></dd>
     * <dt>Format</dt><dd><tt>flag : version  : zone-name : zone-offset : dst-law : dst</tt></dd>
     * <dt>flag</dt><dd><tt>"+"</tt> for valid cookies, any other char otherwise</dd>
     * <dt>version</dt><dd>version number <tt>"6"</tt></dd>
     * <dt>zone-name</dt><dd>conventional Notes/Domino TimeZone name, ignored</dd>
     * <dt>zone-offset</dt><dd>encoded time zone offset in the form: <tt>[-][mm][h]h</tt>.
     * Offset is negative for TimeZones east to greenwich</dd>
     * <dt>dst-law</dt><dd>Daylight Savings Time turning on/off rule in the form:
     *     <tt>(BMonth,BDay,BWeekDay,EMonth,EDay,EWeekDay) | 0</tt> when does not exist
     *     <ul>
     *     <li>BMonth, EMonth take values 1...12, where 1 is January
     *     <li>BDay, EDay - the date in that month, 1 means first, 2 means second, -1 means last
     *     <li> BWeekDay, EWeekDay - 1-based day of a week, 1 means Sunday
     *     </ul></dd>
     * <dt>dst</dt><dd>Daylight Savings Time offset - either 1 (when used) or 0 otherwise</dd>
     * </dl>
     */
    private Cookie newTimeZoneCookie() {
        Cookie cookie = new Cookie(fDomain, "DomTimeZonePrfM", "", fPath, fExpiryDate, false);
        cookie.setValue("+:6" + ":" + fTimeZone.getValue() + ":" + encode(fDst));
        return cookie;
        // TODO check version number of coockies on different versions of Domino
    }

    /**
     * Creates the regional cookie of Lotus Domino.
     *
     * <dl>
     * <dt>Name</dt><dd><tt>DomRegionalPrfM</tt></dd>
     * <dt>Format</dt><dd><tt>flag : version : encoding : Locale :  DateOrder : DateSeperator : TimeSeperator : Hour24Format :
     * AmString : PmString : AmPmSuffix : DecimalSeperator : LeadingDecimalZeros : CurrencySymbol : CurrencySuffix :
     * CurrencySpace : ThousandsSeperator "1"</tt></dd>
     * <dt>encoding</dt><dd>must be "UTF-8"</dd>
     * <dt>locale</dt><dd>a Lotus Domino locale string, e.g. <tt>"en-US"</tt> or <tt>"de-DE"</tt></dd>
     * <dt>date-order</dt><dd>Order of date components. <tt>"0"</tt> for weekday-month-day-year,
     *   <tt>"1"</tt> for weekday-day-month-year,
     *   <tt>"2"</tt> for year-month-day-weekday</dd>
     * <dt>date-seperator</dt><dd>the seperater character for date components, e.g. <tt>"/"</tt> for locale
     *   <tt>en_US</tt> or <tt>"."</tt> for locale <tt>de_DE</tt></dd>
     * <dt>time-seperator</dt><dd>the seperater character for time components, e.g. <tt>":"</dd>>
     * <dt>24-hour-format</dt><dd>Whether to us 24 hour format or not. <tt>"0"</tt> for 12-hour format with AM/OM strings or
     *   <tt>"1"</tt> for 24-hour format</dd>
     * <dt>AM-string</dt><dd>The string identifying times before noon</dd>
     * <dt>PM-string</dt><dd>The string identifying times after noon</dd>
     * <dt>AM-PM-suffix</dt><dd>Position of AM/PM string. <tt>"0"</tt> for preceeding the time,
     *   <tt>"1"</tt> for a suffix of the time</dd>
     * <dt>decimal-seperator</dt><dd>The decimal seperator for seperating the fractional part of decimal numbers, e.g.
     *   <tt>"."</tt> for locale <tt>en_US</tt> or <tt>","</tt> for locale <tt>de_DE</tt></dd>
     * <dt>leading-decimal-zeros</dt><dd>Whether to use a leading zero in decimal fractions,
     *   <tt>1</tt> for using a leading zero</dd>
     * <dt>currency-symbol</dt><dd>The currency symbol, e.g. <tt>"$"</tt> for locale <tt>en_US</tt> or
     *   <tt>"&#0128;"</tt> for locale <tt>de_DE</tt></dd>
     * <dt>currency-suffix</dt><dd>Whether the currency symbol is displayed before or after the currency value.
     *   <tt>"0"</tt> for before, <tt>"1"</tt> for after</dd>
     * <dt>currency-space</dt><dd>Whether to insert a space between currency value and cuurency symbol.
     *   <tt>"0"</tt> for not using a space, <tt>"1"</tt> for using a space</dd>
     * <dt>thousands-seperator</dt><dd>The thousands seperator in number formats, e.g.
     *   <tt>","</tt> for locale <tt>en_US</tt> or <tt>"."</tt> for locale <tt>de_DE</tt></dd>
     * </dl>
     */
    private Cookie newRegionalCookie() {
        Cookie cookie = new Cookie(fDomain, "DomRegionalPrfM", "", fPath, fExpiryDate, false);
        List list = new ArrayList();
        list.add("+");
        list.add("6");
        list.add("UTF-8");
        list.add(fLocaleString);
        list.add(fDateComponentOrder);
        list.add(encode(fDateSeparator));
        list.add(encode(fTimeSeparator));
        list.add(encode(fClock24Hour));
        list.add(encode(fAmString));
        list.add(encode(fPmString));
        list.add(encode(fAmPmSuffix));
        list.add(encode(fDecimalSeparator));
        list.add(encode(fNumberLeadingZero));
        list.add(encode(fCurrencySymbol));
        list.add(encode(fCurrencySuffix));
        list.add(encode(fCurrencySpace));
        list.add(encode(fThousandSeparator));
        list.add(encode(fYearFormat));
        cookie.setValue(implode(list, ":"));
        return cookie;
    }

    /**
     * Combines the elements of a string array to a single string using a given
     * seperator.
     *
     * @param stringList a list of strings
     * @param delimiter the delimiting string
     * @return combined string
     */
    public static String implode(final List stringList, final String delimiter) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < stringList.size(); i++) {
            buffer.append((i > 0 ? delimiter : "") + stringList.get(i));
        }
        return buffer.toString();
    }

    /**
     * Returns a new Lotus Domino time zone cookie.
     *
     * @return Lotus Domino time zone cookie
     */
    public Cookie getTimeZoneCookie() {
        return fTimeZoneCookie;
    }

    /**
     * Returns a new Lotus Domino regional cookie.
     *
     * @return Lotus Domino regional cookie
     */
    public Cookie getRegionalCookie() {
        return fRegionalCookie;
    }

    /**
     * Encodes a boolean value to <tt>"1"</tt> for <code>true</code> and
     * <tt>"0"</tt> for <code>false</code>.
     *
     * @param booleanValue the boolean value to encode
     * @return encoded boolean value
     */
    static String encode(final boolean booleanValue) {
        return (booleanValue ? "1" : "0");
    }

    /**
     * URL-encodes a string.
     *
     * @param text the test to encode
     * @return URL-encoded text
     */
    static String encode(final String text) {
        try {
            return URLEncoder.encode(text, ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            // cannot happen since UTF-8 must be supported on all platforms
            throw new NotesHttpRuntimeException("Unsupported encoding: " + ENCODING_UTF_8, e);
        }
    }
}
