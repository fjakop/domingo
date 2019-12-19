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

/**
 * Represents a column in a view or folder.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DViewColumn {

    /** Constant for alignments. */
    int ALIGN_LEFT = 0;

    /** Constant for alignments. */
    int ALIGN_RIGHT = 1;

    /** Constant for alignments. */
    int ALIGN_CENTER = 2;

    /** Constant for seperators. */
    int SEP_SPACE = 1;

    /** Constant for seperators. */
    int SEP_COMMA = 2;

    /** Constant for seperators. */
    int SEP_SEMICOLON = 3;

    /** Constant for seperators. */
    int SEP_NEWLINE = 4;

    /** Constant for seperators. */
    int SEP_NONE = 0;

    /** Constant for number formatting. */
    int FMT_H = 2;

    /** Constant for number formatting. */
    int FMT_ALL = 3;

    /** Constant for number formatting. */
    int FMT_GENERAL = 0;

    /** Constant for number formatting. */
    int FMT_FIXED = 1;

    /** Constant for number formatting. */
    int FMT_SCIENTIFIC = 2;

    /** Constant for number formatting. */
    int FMT_CURRENCY = 3;

    /** Constant for number attributes. */
    int ATTR_PUNCTUATED = 1;

    /** Constant for number attributes. */
    int ATTR_PARENS = 2;

    /** Constant for number attributes. */
    int ATTR_PERCENT = 4;

    /** Constant for date formatting. */
    int FMT_YMD = 0;

    /** Constant for date formatting. */
    int FMT_MD = 2;

    /** Constant for date formatting. */
    int FMT_YM = 3;

    /** Constant for date formatting. */
    int FMT_Y4M = 6;

    /** Constant for date formatting. */
    int FMT_HMS = 0;

    /** Constant for date formatting. */
    int FMT_HM = 1;

    /** Constant for date formatting. */
    int FMT_NEVER = 0;

    /** Constant for date formatting. */
    int FMT_SOMETIMES = 1;

    /** Constant for date formatting. */
    int FMT_ALWAYS = 2;

    /** Constant for date formatting. */
    int FMT_DATE = 0;

    /** Constant for date formatting. */
    int FMT_TIME = 1;

    /** Constant for date formatting. */
    int FMT_DATETIME = 2;

    /** Constant for date formatting. */
    int FMT_TODAYTIME = 3;

    /** Constant for text formatting. */
    int FONT_BOLD = 1;

    /** Constant for text formatting. */
    int FONT_ITALIC = 2;

    /** Constant for text formatting. */
    int FONT_UNDERLINE = 4;

    /** Constant for text formatting. */
    int FONT_STRIKEOUT = 8;

    /** Constant for text formatting. */
    int FONT_STRIKETHROUGH = 8;

    /** Constant for text formatting. */
    int FONT_PLAIN = 0;

    /**
     * Indicates whether a column is hidden.
     *
     * <h3>Legal values</h3>
     *
     * @return <code>true</code> if the column is hidden, <code>false</code> if the column is
     * not hidden
     */
    boolean isHidden();

    /**
     * The width of a column.
     *
     * @return width of a column
     */
    int getWidth();

    /**
     * The title of a column or an empty string if the column does not have a
     * title.
     *
     * @return the view title
     */
    String getTitle();

    /**
     * The title of a column or an empty string if the column does not have a
     * title.
     *
     * @param title the new title
     */
    void setTitle(String title);

    /**
     * The alignment of data in a column.
     *
     * @return alignment of a column
     */
    int getAlignment();

    /**
     * The alignment of data in a column.
     *
     * @param alignment the new alignment
     */
    void setAlignment(int alignment);

    /**
     * Indicates whether a column contains only response documents.
     *
     * @return whether a column contains only response documents
     */
    boolean isResponse();

    /**
     * Indicates whether a column is an auto-sorted column.
     *
     * <p>This property is false if the column is user-sorted but not
     * auto-sorted.</p>
     *
     * @return <code>true</code> if the column is auto-sorted, <code>false</code> if the
     * column is not auto-sorted
     */
    boolean isSorted();

    /**
     * The formula for a column that is based on a simple function or a formula.
     *
     * <ul>
     * <li>If the column is based on a formula, this property returns the
     * formula.</li>
     * <li>If the column is based on a simple function, this property returns
     * the formula equivalent. For example, if the column is the simple function
     * "Creation Date," this property returns "@Created."</li>
     * <li>If the column is based on a field, this property returns an empty
     * string.</li>
     * </ul>
     *
     * @return formula for a column
     */
    String getFormula();

    /**
     * Indicates whether a column is hidden.
     *
     * @param flag <code>true</code> if the column is hidden or <code>false</code> if the column is
     * not hidden
     */
    void setHidden(boolean flag);

    /**
     * The formula for a column that is based on a simple function or a formula.
     *
     * <ul>
     * <li>This property replaces the existing formula. The replacement formula
     * must be valid and cannot be an empty string.</li>
     * <li>If the replacement formula is equivalent to a simple function, the
     * column basis changes to the simple function.</li>
     * <li>If the formula is simply the name of a field, the column basis
     * changes to the value of that field, IsField becomes true, and IsFormula
     * becomes false.</li>
     * <li>If a formula (that is not simply the name of a field) is written to
     * a column based on a field, the column basis changes to a simple function
     * or a formula, IsFormula becomes true, and IsField becomes false.</li>
     * </ul>
     *
     * @param arg1 formula for a column
     */
    void setFormula(String arg1);

    /**
     * The position of a column in its view. Columns are numbered from left to
     * right, starting with 1.
     *
     * <p>The Columns property in View returns a vector of DDViewColumn objects.
     * The vector is zero-based, so the first element in the vector is element 0
     * and contains the DViewColumn at position 1. The second element in the
     * vector is element 1 and contains the DViewColumn at position 2, and so on.
     * Remember to add one to the vector index to get the correct position
     * value.</p>
     *
     * @return position of column
     */
    int getPosition();

    /**
     * Indicates whether a column value is based on a field value.
     *
     * @return <code>true</code> if the column is based on a field value, <code>false</code>
     * if the column is not based on a field value
     *
     * <p>If you specify a column as a formula whose value is the name of a
     * field, the column is considered a field. The formula must contain only
     * the name of the field. For example, "Subject" is a field but
     * "@Trim(Subject)" is a formula.</p>
     *
     * <p>This property returns false if the column is based on a simple
     * function or formula.</p>
     */
    boolean isField();

    /**
     * Indicates whether a column value is based on a simple function or a
     * formula.
     *
     * @return <code>true</code> if the column is based on a simple function or formula,
     * <code>false</code> if the column is not based on a simple function or formula
     *
     * <p>If you specify a column as a formula whose value is the name of a
     * field, the column is considered a field. The formula must contain only
     * the name of the field. For example, "Subject" is a field but
     * "@Trim(Subject)" is a formula.</p>
     *
     * <p>This property returns false if the column is based on a field value.</p>
     */
    boolean isFormula();

    /**
     * The programmatic name of a column, which is usually the field (item) name
     * for a column based on a field.
     *
     * <p>The programmatic name is specified in the Advanced tab of the Column
     * properties box. By default, the programmatic name for a column based on a
     * field is the name of the field. The programmatic name for a column based
     * on a simple action or a formula is a dollar sign followed by a digit or
     * digits -- "$1," "$2," and so on.
     *
     * @return programmatic name of column
     */
    String getItemName();

    /**
     * List (multi-value) separator for values in a column.
     *
     * @return {@link #SEP_COMMA}, {@link #SEP_NEWLINE}, {@link #SEP_NONE}, {@link #SEP_SEMICOLON} or {@link #SEP_SPACE}
     *
     * @since Lotus Notes 6.5
     */
    int getListSep();

    /**
     * The alignment of the header in a column.
     *
     * @return {@link #ALIGN_CENTER}, {@link #ALIGN_LEFT} or {@link #ALIGN_RIGHT}
     *
     * @since Lotus Notes 6.5
     */
    int getHeaderAlignment();

    /**
     * The font face of data in a column.
     *
     * <p>Typical font faces include Ariel, Courier, Default Sans Serif,
     * Helvetica, Palatino, Symbol, Times New Roman, and many more.
     *
     * @return font face of data
     *
     * @since Lotus Notes 6.5
     */
    String getFontFace();

    /**
     * The font style of data in a column.
     *
     * @return {@link #FONT_PLAIN}, {@link #FONT_BOLD}, {@link #FONT_ITALIC},
     *         {@link #FONT_UNDERLINE}, {@link #FONT_STRIKEOUT} or {@link #FONT_STRIKETHROUGH}
     *
     * <p>The font style can have multiple values. For example, the style might
     * be {@link #FONT_BOLD}, {@link #FONT_ITALIC}</p>
     *
     * <p>You can use {@link #isFontBold}, {@link #isFontItalic},
     * {@link #isFontStrikethrough} or {@link #isFontUnderline} to query and set
     * the font styles.
     *
     * @since Lotus Notes 6.5
     */
    int getFontStyle();

    /**
     * The font color of data in a column.
     *
     * @return A Domino color index in the range 0-240
     *
     * <p>The first 16 color indices are:</p>
     *
     * <p>{@link DRichTextStyle#COLOR_BLACK}, {@link DRichTextStyle#COLOR_BLUE},
     * {@link DRichTextStyle#COLOR_CYAN}, {@link DRichTextStyle#COLOR_DARK_BLUE},
     * {@link DRichTextStyle#COLOR_DARK_CYAN}, {@link DRichTextStyle#COLOR_DARK_GREEN},
     * {@link DRichTextStyle#COLOR_DARK_MAGENTA}, {@link DRichTextStyle#COLOR_DARK_RED},
     * {@link DRichTextStyle#COLOR_DARK_YELLOW}, {@link DRichTextStyle#COLOR_GRAY},
     * {@link DRichTextStyle#COLOR_GREEN}, {@link DRichTextStyle#COLOR_LIGHT_GRAY},
     * {@link DRichTextStyle#COLOR_MAGENTA}, {@link DRichTextStyle#COLOR_RED},
     * {@link DRichTextStyle#COLOR_WHITE}, {@link DRichTextStyle#COLOR_YELLOW}
     * </p>
     *
     * @since Lotus Notes 6.5
     */
    int getFontColor();

    /**
     * The font point size of data in a column.
     *
     * @return font point size of data
     * @since Lotus Notes 6.5
     */
    int getFontPointSize();

    /**
     * Number of decimal places for numeric values in a column.
     *
     * @return Number of decimal places for numeric values
     * @since Lotus Notes 6.5
     */
    int getNumberDigits();

    /**
     * Format for numeric values in a column.
     *
     * @return {@link #FMT_CURRENCY}, {@link #FMT_FIXED}, {@link #FMT_GENERAL} or {@link #FMT_SCIENTIFIC}
     *
     * @since Lotus Notes 6.5
     */
    int getNumberFormat();

    /**
     * Attributes for numeric values in a column.
     *
     * @return {@link #ATTR_PARENS} (parentheses on negative numbers), {@link #ATTR_PERCENT} (percent sign),
     *  or {@link #ATTR_PUNCTUATED} (punctuated at thousandths)
     *
     * <p>The number attribute can have multiple values. For example, the
     * attribute might be {@link #ATTR_PARENS} and {@link #ATTR_PUNCTUATED}.</p>
     *
     * @since Lotus Notes 6.5
     */
    int getNumberAttrib();

    /**
     * The format of date data in a column.
     *
     * @return {@link #FMT_MD} (month and day), {@link #FMT_YM}
     * (year and month), {@link #FMT_Y4M} (4-digit year and month) or
     * {@link #FMT_YMD} (year, month, and day)
     *
     * @since Lotus Notes 6.5
     */
    int getDateFmt();

    /**
     * The format of time data in a column.
     *
     * @return {@link #FMT_HM} (hour and minute), {@link #FMT_HMS} (hour,
     *         minute, and second), {@link #FMT_H} (hour only) or
     *         {@link #FMT_ALL} (hour, minute, second, and hundredths of a
     *         second)
     *
     * @since Lotus Notes 6.5
     */
    int getTimeFmt();

    /**
     * The format of the zone in a time-date column value.
     *
     * @return {@link #FMT_ALWAYS} (always show time zone), {@link #FMT_NEVER}
     * (never show time zone), {@link #FMT_SOMETIMES} (show time
     * zone if the time is not local)
     *
     * @since Lotus Notes 6.5
     */
    int getTimeZoneFmt();

    /**
     * The format of time-date data in a column.
     *
     * @return {@link #FMT_DATE} (Date only), {@link #FMT_DATETIME}
     * (Date and time), {@link #FMT_TIME} (Time only) or {@link #FMT_TODAYTIME}
     * (Date or 'Today' or 'Yesterday' -- and time)
     *
     * @since Lotus Notes 6.5
     */
    int getTimeDateFmt();

    /**
     * Indicates whether a column is categorized.
     *
     * @return <code>true</code> if the column is categorized,
     *         <code>false</code> if the column is not categorized
     *
     * <p>If isCategory returns <code>true</code>, isSorted also returns
     * <code>true</code>, since columns that are categorized must also be
     * sorted.</p>
     */
    boolean isCategory();

    /**
     * Indicates whether an auto-sorted column is sorted in descending order.
     *
     * @return <code>true</code> if a sorted column is descending, <code>false</code> if a
     * sorted column is ascending
     *
     * <p>In the sort tab for column properties, the"Descending" button
     * indicates a descending auto-sorted column.</p>
     *
     * <p>{@link #isSorted()} must be true for this property to be effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    boolean isSortDescending();

    /**
     * Indicates whether the details for total columns are hidden.
     *
     * @return <code>true</code> if the details for totals are hidden, <code>false</code> if
     * the details for totals are displayed
     *
     * @since Lotus Notes 6.5
     */
    boolean isHideDetail();

    /**
     * Indicates whether column values are displayed as icons.
     *
     * @return <code>true</code> if column values are displayed as icons, <code>false</code>
     * if column values are not displayed as icons
     *
     * @since Lotus Notes 6.5
     */
    boolean isIcon();

    /**
     * Indicates whether a column is resizable.
     *
     * <h3>Legal values</h3>
     *
     * @return <code>true</code> if a column is resizable, <code>false</code> if a column is
     * not resizable
     *
     * @since Lotus Notes 6.5
     */
    boolean isResize();

    /**
     * Indicates whether a column is a user-sorted column that can be sorted in
     * ascending order.
     *
     * <h3>Legal values</h3>
     *
     * @return <code>true</code> if the column is ascending user-sorted, <code>false</code> if
     * the column is not ascending user-sorted
     *
     * <h3>Usage</h3>
     *
     * <p>In the sort tab for column properties, "Click on column head to sort"
     * indicates a user-sorted column. "Ascending" or "Both" indicates
     * ascending.</p>
     *
     * @since Lotus Notes 6.5
     */
    boolean isResortAscending();

    /**
     * Indicates whether a column is a user-sorted column that can be sorted in
     * descending order.
     *
     * <h3>Legal values</h3>
     *
     * @return <code>true</code> if the column is descending user-sorted, <code>false</code>
     * if the column is not descending user-sorted
     *
     * <h3>Usage</h3>
     *
     * <p>In the sort tab for column properties, "Click on column head to sort"
     * indicates a user-sorted column. "Descending" or "Both" indicates
     * ascending.</p>
     *
     * @since Lotus Notes 6.5
     */
    boolean isResortDescending();

    /**
     * Indicates whether an expandable column displays a twistie.
     *
     * <h3>Legal values</h3>
     *
     * @return <code>true</code> if an expandable column displays a twistie, <code>false</code>
     * if an expandable column does not display a twistie
     *
     * @since Lotus Notes 6.5
     */
    boolean isShowTwistie();

    /**
     * Indicates whether a column is a user-sorted column that allows the user
     * to change to another view.
     *
     * @return <code>true</code> if the column is user-sorted and allows changing to
     * another view, <code>false</code> if the column is not user-sorted and allows
     * changing to another view
     *
     * <p>In the sort tab for column properties, "Click on column head to sort"
     * indicates a user-sorted column. "Change To View" indicates allowing the
     * user to change to another view.</p>
     *
     * <p>If a user-sorted column is ascending or descending, this property is
     * false.</p>
     *
     * @since Lotus Notes 6.5
     */
    boolean isResortToView();

    /**
     * Indicates whether an ascending or descending user-sorted column has a
     * secondary sorting column.
     *
     * @return <code>true</code> if a column is a secondary sorting column,
     * <code>false</code> if a column is a secondary sorting column
     *
     * <p>In the sort tab for column properties, "Click on column head to sort"
     * indicates a user-sorted column. For ascending and descending sorts, you
     * can choose to have a secondary sort column.</p>
     *
     * <p>If a user-sorted column is "Change to view," this property is false.</p>
     *
     * <p>The column must be selected through the Lotus Domino Designer UI.</p>
     *
     * <p>The sort is ascending unless {@link #isSecondaryResortDescending} is
     * true.</p>
     *
     * <p>{@link #isResortAscending} or {@link #isResortDescending} must be
     * true for this property to be effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    boolean isSecondaryResort();

    /**
     * Indicates whether a secondary sorting column for a user-sorted column is
     * sorted in descending order.
     *
     * @return <code>true</code> if a secondary sorting column is descending, <code>false</code>
     * if a secondary sorting column is ascending
     *
     * <p>{@link #isSecondaryResort} must be true for this property to be effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    boolean isSecondaryResortDescending();

    /**
     * Indicates whether an auto-sorted column is sorted with regard to case.
     *
     * @return <code>true</code> if the column is sorted with regard to case, <code>false</code>
     * if the column is sorted without regard to case
     *
     * <p>{@link #isSorted} must be true for this property to be effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    boolean isCaseSensitiveSort();

    /**
     * Indicates whether an auto-sorted column is sorted with regard to accent.
     *
     * <h3>Legal values</h3>
     *
     * @return <code>true</code> if the column is sorted with regard to accent, <code>false</code>
     * if the column is sorted without regard to accent
     *
     * <h3>Usage</h3>
     *
     * <p>{@link #isSorted()} must be true for this property to be effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    boolean isAccentSensitiveSort();

    /**
     * The font color of the header in a column.
     *
     * @return A Domino color index in the range 0-240.
     *
     * <p>The first 16 color indices are:</p>
     *
     * <p>{@link DRichTextStyle#COLOR_BLACK}, {@link DRichTextStyle#COLOR_BLUE},
     * {@link DRichTextStyle#COLOR_CYAN}, {@link DRichTextStyle#COLOR_DARK_BLUE},
     * {@link DRichTextStyle#COLOR_DARK_CYAN}, {@link DRichTextStyle#COLOR_DARK_GREEN},
     * {@link DRichTextStyle#COLOR_DARK_MAGENTA}, {@link DRichTextStyle#COLOR_DARK_RED},
     * {@link DRichTextStyle#COLOR_DARK_YELLOW}, {@link DRichTextStyle#COLOR_GRAY},
     * {@link DRichTextStyle#COLOR_GREEN}, {@link DRichTextStyle#COLOR_LIGHT_GRAY},
     * {@link DRichTextStyle#COLOR_MAGENTA}, {@link DRichTextStyle#COLOR_RED},
     * {@link DRichTextStyle#COLOR_WHITE}, {@link DRichTextStyle#COLOR_YELLOW}
     * </p>
     *
     * @since Lotus Notes 6.5
     */
    int getHeaderFontColor();

    /**
     * The font face of the header in a column.
     *
     * <p>Typical font faces include Ariel, Courier, Default Sans Serif,
     * Helvetica, Palatino, Symbol, Times New Roman, and many more.</p>
     *
     * @return font face of the header
     * @since Lotus Notes 6.5
     */
    String getHeaderFontFace();

    /**
     * The font point size of the header in a column.
     *
     * @return font point size
     * @since Lotus Notes 6.5
     */
    int getHeaderFontPointSize();

    /**
     * The font style of the header in a column.
     *
     * @return {@link #FONT_PLAIN}, {@link #FONT_BOLD},
     *         {@link #FONT_ITALIC}, {@link #FONT_UNDERLINE},
     *         {@link #FONT_STRIKEOUT}, {@link #FONT_STRIKETHROUGH}
     *
     * <p>The font style can have multiple values. For example, the style might
     * be {@link #FONT_BOLD} and {@link #FONT_ITALIC}.</p>
     *
     * @since Lotus Notes 6.5
     */
    int getHeaderFontStyle();

    /**
     * The index of the secondary sorting column of a user-sorted column that
     * allows a secondary sorting column.
     *
     * <p>The index of the first column is 0, the second 1, and so on.</p>
     *
     * @return index of the secondary sorting column
     * @since Lotus Notes 6.5
     */
    int getSecondaryResortColumnIndex();

    /**
     * Indicates whether the font style for a column includes bold.
     *
     * @return <code>true</code> if the font style includes bold, <code>false</code> if the
     * font style does not include bold
     *
     * @since Lotus Notes 6.5
     */
    boolean isFontBold();

    /**
     * Indicates whether the font style for a column includes italic.
     *
     * @return <code>true</code> if the font style includes italic, <code>false</code> if the
     * font style does not include italic
     *
     * @since Lotus Notes 6.5
     */
    boolean isFontItalic();

    /**
     * Indicates whether the font style for a column includes underline.
     *
     * @return <code>true</code> if the font style includes underline, <code>false</code> if
     * the font style does not include underline
     *
     * @since Lotus Notes 6.5
     */
    boolean isFontUnderline();

    /**
     * Indicates whether the font style for a column includes strikethrough.
     *
     * @return <code>true</code> if the font style includes strikethrough, <code>false</code>
     * if the font style does not include strikethrough
     *
     * @since Lotus Notes 6.5
     */
    boolean isFontStrikethrough();

    /**
     * Indicates whether the header font style for a column includes bold.
     *
     * @return <code>true</code> if the header font style includes bold, <code>false</code> if
     * the header font style does not include bold
     *
     * @since Lotus Notes 6.5
     */
    boolean isHeaderFontBold();

    /**
     * Indicates whether the header font style for a column includes italic.
     *
     * @return <code>true</code> if the header font style includes italic, <code>false</code>
     * if the header font style does not include italic
     *
     * @since Lotus Notes 6.5
     */
    boolean isHeaderFontItalic();

    /**
     * Indicates whether the header font style for a column includes underline.
     *
     * @return <code>true</code> if the header font style includes underline, <code>false</code>
     * if the header font style does not include underline
     *
     * @since Lotus Notes 6.5
     */
    boolean isHeaderFontUnderline();

    /**
     * Indicates whether the header font style for a column includes
     * strikethrough.
     *
     * @return <code>true</code> if the header font style includes strikethrough, <code>false</code>
     * if the header font style does not include strikethrough
     *
     * @since Lotus Notes 6.5
     */
    boolean isHeaderFontStrikethrough();

    /**
     * Indicates whether the number attributes for a column include parentheses
     * for negative numbers.
     *
     * @return <code>true</code> if the number attributes include parentheses, <code>false</code>
     * if the number attributes do not include parentheses
     *
     * @since Lotus Notes 6.5
     */
    boolean isNumberAttribParens();

    /**
     * Indicates whether the number attributes for a column include punctuation
     * at thousandths.
     *
     * @return <code>true</code> if the number attributes include punctuation, <code>false</code>
     * if the number attributes do not include punctuation
     *
     * @since Lotus Notes 6.5
     */
    boolean isNumberAttribPunctuated();

    /**
     * Indicates whether the number attributes for a column include displaying
     * the number as a percentage.
     *
     * @return <code>true</code> if the number attributes include percent, <code>false</code>
     * if the number attributes do not include percent
     *
     * @since Lotus Notes 6.5
     */
    boolean isNumberAttribPercent();

    /**
     * The name of the target view for a user-sorted column that allows the user
     * to change to another view.
     *
     * @return name of the target view
     * @since Lotus Notes 6.5
     */
    String getResortToViewName();

    /**
     * The format of date data in a column.
     *
     * @param arg1 {@link #FMT_MD} (month and day), {@link #FMT_YM}
     * (year and month), {@link #FMT_Y4M} (4-digit year and month) or
     * {@link #FMT_YMD} (year, month, and day)
     *
     * @since Lotus Notes 6.5
     */
    void setDateFmt(int arg1);

    /**
     * The font color of data in a column.
     *
     * @param arg1 a Domino color index in the range 0-240.
     *
     * <p>The first 16 color indices are:</p>
     *
     * <p>{@link DRichTextStyle#COLOR_BLACK}, {@link DRichTextStyle#COLOR_BLUE},
     * {@link DRichTextStyle#COLOR_CYAN}, {@link DRichTextStyle#COLOR_DARK_BLUE},
     * {@link DRichTextStyle#COLOR_DARK_CYAN}, {@link DRichTextStyle#COLOR_DARK_GREEN},
     * {@link DRichTextStyle#COLOR_DARK_MAGENTA}, {@link DRichTextStyle#COLOR_DARK_RED},
     * {@link DRichTextStyle#COLOR_DARK_YELLOW}, {@link DRichTextStyle#COLOR_GRAY},
     * {@link DRichTextStyle#COLOR_GREEN}, {@link DRichTextStyle#COLOR_LIGHT_GRAY},
     * {@link DRichTextStyle#COLOR_MAGENTA}, {@link DRichTextStyle#COLOR_RED},
     * {@link DRichTextStyle#COLOR_WHITE}, {@link DRichTextStyle#COLOR_YELLOW}
     * </p>
     *
     * @since Lotus Notes 6.5
     */
    void setFontColor(int arg1);

    /**
     * The font face of data in a column.
     *
     * <p>Typical font faces include Ariel, Courier, Default Sans Serif,
     * Helvetica, Palatino, Symbol, Times New Roman, and many more.</p>
     *
     * @param arg1 font face of data
     * @since Lotus Notes 6.5
     */
    void setFontFace(String arg1);

    /**
     * The font point size of data in a column.
     *
     * @param arg1 font point size
     * @since Lotus Notes 6.5
     */
    void setFontPointSize(int arg1);

    /**
     * The font style of data in a column.
     *
     * @param arg1 {@link #FONT_PLAIN}, {@link #FONT_BOLD}, {@link #FONT_ITALIC}
     * {@link #FONT_UNDERLINE}, {@link #FONT_STRIKEOUT} or {@link #FONT_STRIKETHROUGH}
     *
     * <p>The font style can have multiple values. For example, the style might
     * be {@link #FONT_BOLD} and {@link #FONT_ITALIC}.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setFontStyle(int arg1);

    /**
     * The alignment of the header in a column.
     *
     * @param arg1 {@link #ALIGN_CENTER}, {@link #ALIGN_LEFT}
     * {@link #ALIGN_RIGHT}
     *
     * @since Lotus Notes 6.5
     */
    void setHeaderAlignment(int arg1);

    /**
     * Indicates whether an auto-sorted column is sorted with regard to accent.
     *
     * @param arg1 <code>true</code> if the column is sorted with regard to accent, <code>false</code>
     * if the column is sorted without regard to accent
     *
     * <h3>Usage</h3>
     *
     * <p>{@link #isSorted} must be true for this property to be effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setAccentSensitiveSort(boolean arg1);

    /**
     * Indicates whether an auto-sorted column is sorted with regard to case.
     *
     * <h3>Legal values</h3>
     *
     * @param arg1 <code>true</code> if the column is sorted with regard to case, <code>false</code>
     * if the column is sorted without regard to case
     *
     * <p>{@link #isSorted}  must be true for this property to be effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setCaseSensitiveSort(boolean arg1);

    /**
     * Indicates whether the details for total columns are hidden.
     *
     * @param arg1 <code>true</code> if the details for totals are hidden, <code>false</code> if
     * the details for totals are displayed
     *
     * @since Lotus Notes 6.5
     */
    void setHideDetail(boolean arg1);

    /**
     * Indicates whether a column is resizable.
     *
     * @param arg1 <code>true</code> if a column is resizable, <code>false</code> if a column is
     * not resizable
     *
     * @since Lotus Notes 6.5
     */
    void setResize(boolean arg1);

    /**
     * Indicates whether a column is a user-sorted column that can be sorted in
     * ascending order.
     *
     * @param arg1 <code>true</code> if the column is ascending user-sorted, <code>false</code> if
     * the column is not ascending user-sorted
     *
     * <p>In the sort tab for column properties, "Click on column head to sort"
     * indicates a user-sorted column. "Ascending" or "Both" indicates
     * ascending.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setResortAscending(boolean arg1);

    /**
     * Indicates whether a column is a user-sorted column that can be sorted in
     * descending order.
     *
     * @param arg1 <code>true</code> if the column is descending user-sorted, <code>false</code>
     * if the column is not descending user-sorted
     *
     * <p>In the sort tab for column properties, "Click on column head to sort"
     * indicates a user-sorted column. "Descending" or "Both" indicates
     * ascending.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setResortDescending(boolean arg1);

    /**
     * Indicates whether a column is a user-sorted column that allows the user
     * to change to another view.
     *
     * @param arg1 <code>true</code> if the column is user-sorted and allows changing to
     * another view, <code>false</code> if the column is not user-sorted and allows
     * changing to another view
     *
     * <p>In the sort tab for column properties, "Click on column head to sort"
     * indicates a user-sorted column. "Change To View" indicates allowing the
     * user to change to another view.</p>
     *
     * <p>If a user-sorted column is ascending or descending, this property is
     * false.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setResortToView(boolean arg1);

    /**
     * Indicates whether an ascending or descending user-sorted column has a
     * secondary sorting column.
     *
     * @param arg1 <code>true</code> if a column is a secondary sorting column, <code>false</code>
     * if a column is a secondary sorting column
     *
     * <p>In the sort tab for column properties, "Click on column head to sort"
     * indicates a user-sorted column. For ascending and descending sorts, you
     * can choose to have a secondary sort column.</p>
     *
     * <p>If a user-sorted column is "Change to view," this property is false.</p>
     *
     * <p>The column must be selected through the Lotus Domino Designer UI.</p>
     *
     * <p>The sort is ascending unless {@link #isSecondaryResortDescending} is
     * true.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setSecondaryResort(boolean arg1);

    /**
     * Indicates whether a secondary sorting column for a user-sorted column is
     * sorted in descending order.
     *
     * @param arg1 <code>true</code> if a secondary sorting column is descending, <code>false</code>
     * if a secondary sorting column is ascending
     *
     * <p>{@link #isSecondaryResort()} must be true for this
     * property to be effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setSecondaryResortDescending(boolean arg1);

    /**
     * Indicates whether an expandable column displays a twistie.
     *
     * @param arg1 <code>true</code> if an expandable column displays a twistie, <code>false</code>
     * if an expandable column does not display a twistie
     *
     * @since Lotus Notes 6.5
     */
    void setShowTwistie(boolean arg1);

    /**
     * Indicates whether an auto-sorted column is sorted in descending order.
     *
     * @param arg1 <code>true</code> if a sorted column is descending, <code>false</code> if a
     * sorted column is ascending
     *
     * <p>In the sort tab for column properties, the"Descending" button
     * indicates a descending auto-sorted column.</p>
     *
     * <p>{@link #isSorted()} must be true for this property to be
     * effective.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setSortDescending(boolean arg1);

    /**
     * Indicates whether a column is an auto-sorted column.
     *
     * <h3>Legal values</h3>
     *
     * @param arg1 <code>true</code> if the column is auto-sorted, <code>false</code> if the
     * column is not auto-sorted
     *
     * <h3>Usage</h3>
     *
     * <p>In the sort tab for column properties, the "Ascending" or
     * "Descending" button indicates an auto-sorted column.</p>
     *
     * <p>This property is false if the column is user-sorted but not
     * auto-sorted.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setSorted(boolean arg1);

    /**
     * List (multi-value) separator for values in a column.
     *
     * @param arg1 {@link #SEP_COMMA}, {@link #SEP_NEWLINE},
     * {@link #SEP_NONE}, {@link #SEP_SEMICOLON} or {@link #SEP_SPACE}
     *
     * @since Lotus Notes 6.5
     */
    void setListSep(int arg1);

    /**
     * Attributes for numeric values in a column.
     *
     * @param arg1 {@link #ATTR_PARENS} (parentheses on negative numbers),
     * {@link #ATTR_PERCENT} (percent sign) or {@link #ATTR_PUNCTUATED}
     * (punctuated at thousandths)
     *
     * <p>The number attribute can have multiple values. For example, the
     * attribute might be {@link #ATTR_PARENS} and {@link #ATTR_PUNCTUATED}.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setNumberAttrib(int arg1);

    /**
     * Number of decimal places for numeric values in a column.
     *
     * @param arg1 number of decimal places
     * @since Lotus Notes 6.5
     */
    void setNumberDigits(int arg1);

    /**
     * Format for numeric values in a column.
     *
     * @param arg1 {@link #FMT_CURRENCY}, {@link #FMT_FIXED}
     * {@link #FMT_GENERAL} or {@link #FMT_SCIENTIFIC}
     *
     * @since Lotus Notes 6.5
     */
    void setNumberFormat(int arg1);

    /**
     * The format of time-date data in a column.
     *
     * @param arg1 {@link #FMT_DATE} (Date only), {@link #FMT_DATETIME}
     * (Date and time), {@link #FMT_TIME} (Time only), {@link #FMT_TODAYTIME}
     * (Date or 'Today' or 'Yesterday' -- and time)
     *
     * @since Lotus Notes 6.5
     */
    void setTimeDateFmt(int arg1);

    /**
     * The format of time data in a column.
     *
     * @param arg1 {@link #FMT_HM} (hour and minute), {@link #FMT_HMS}
     * (hour, minute, and second), {@link #FMT_H} (hour only)
     * {@link #FMT_ALL} (hour, minute, second, and hundredths of a
     * second)
     *
     * @since Lotus Notes 6.5
     */
    void setTimeFmt(int arg1);

    /**
     * The format of the zone in a time-date column value.
     *
     * @param arg1 {@link #FMT_ALWAYS} (always show time zone), {@link #FMT_NEVER}
     * (never show time zone) or {@link #FMT_SOMETIMES} (show time
     * zone if the time is not local)
     *
     * @since Lotus Notes 6.5
     */
    void setTimeZoneFmt(int arg1);

    /**
     * The width of a column.
     *
     * @param arg1 width of column
     * @since Lotus Notes 6.5
     */
    void setWidth(int arg1);

    /**
     * The font color of the header in a column.
     *
     * @param arg1 a Domino color index in the range 0-240.
     *
     * <p>The first 16 color indices are:</p>
     *
     * <p>{@link DRichTextStyle#COLOR_BLACK}, {@link DRichTextStyle#COLOR_BLUE},
     * {@link DRichTextStyle#COLOR_CYAN}, {@link DRichTextStyle#COLOR_DARK_BLUE},
     * {@link DRichTextStyle#COLOR_DARK_CYAN}, {@link DRichTextStyle#COLOR_DARK_GREEN},
     * {@link DRichTextStyle#COLOR_DARK_MAGENTA}, {@link DRichTextStyle#COLOR_DARK_RED},
     * {@link DRichTextStyle#COLOR_DARK_YELLOW}, {@link DRichTextStyle#COLOR_GRAY},
     * {@link DRichTextStyle#COLOR_GREEN}, {@link DRichTextStyle#COLOR_LIGHT_GRAY},
     * {@link DRichTextStyle#COLOR_MAGENTA}, {@link DRichTextStyle#COLOR_RED},
     * {@link DRichTextStyle#COLOR_WHITE}, {@link DRichTextStyle#COLOR_YELLOW}
     * </p>
     *
     * @since Lotus Notes 6.5
     */
    void setHeaderFontColor(int arg1);

    /**
     * The font face of the header in a column.
     *
     * <p>Typical font faces include Ariel, Courier, Default Sans Serif,
     * Helvetica, Palatino, Symbol, Times New Roman, and many more.</p>
     *
     * @param arg1 font face of header
     * @since Lotus Notes 6.5
     */
    void setHeaderFontFace(String arg1);

    /**
     * The font point size of the header in a column.
     *
     * @param arg1 font point size of the header
     * @since Lotus Notes 6.5
     */
    void setHeaderFontPointSize(int arg1);

    /**
     * The font style of the header in a column.
     *
     * @param arg1 {@link #FONT_PLAIN}, {@link #FONT_BOLD}
     * <li>#FONT_ITALIC}, {@link #FONT_UNDERLINE}
     * <li>#FONT_STRIKEOUT} or {@link #FONT_STRIKETHROUGH}
     *
     * <p>The font style can have multiple values. For example, the style might
     * be {@link #FONT_BOLD} and {@link #FONT_ITALIC}.</p>
     *
     * @since Lotus Notes 6.5
     */
    void setHeaderFontStyle(int arg1);

    /**
     * The index of the secondary sorting column of a user-sorted column that
     * allows a secondary sorting column.
     *
     * <p>The index of the first column is 0, the second 1, and so on.</p>
     *
     * @param arg1 index of the secondary sorting column
     * @since Lotus Notes 6.5
     */
    void setSecondaryResortColumnIndex(int arg1);

    /**
     * Indicates whether the font style for a column includes bold.
     *
     * @param arg1 <code>true</code> if the font style includes bold, <code>false</code> if the
     * font style does not include bold
     *
     * @since Lotus Notes 6.5
     */
    void setFontBold(boolean arg1);

    /**
     * Indicates whether the font style for a column includes italic.
     *
     * @param arg1 <code>true</code> if the font style includes italic, <code>false</code> if the
     * font style does not include italic
     *
     * @since Lotus Notes 6.5
     */
    void setFontItalic(boolean arg1);

    /**
     * Indicates whether the font style for a column includes underline.
     *
     * @param arg1 <code>true</code> if the font style includes underline, <code>false</code> if
     * the font style does not include underline
     *
     * @since Lotus Notes 6.5
     */
    void setFontUnderline(boolean arg1);

    /**
     * Indicates whether the font style for a column includes strikethrough.
     *
     * @param arg1 <code>true</code> if the font style includes strikethrough, <code>false</code>
     * if the font style does not include strikethrough
     *
     * @since Lotus Notes 6.5
     */
    void setFontStrikethrough(boolean arg1);

    /**
     * Indicates whether the header font style for a column includes bold.
     *
     * @param arg1 <code>true</code> if the header font style includes bold, <code>false</code> if
     * the header font style does not include bold
     *
     * @since Lotus Notes 6.5
     */
    void setHeaderFontBold(boolean arg1);

    /**
     * Indicates whether the header font style for a column includes italic.
     *
     * @param arg1 <code>true</code> if the header font style includes italic, <code>false</code>
     * if the header font style does not include italic
     *
     * @since Lotus Notes 6.5
     */
    void setHeaderFontItalic(boolean arg1);

    /**
     * Indicates whether the header font style for a column includes underline.
     *
     * @param arg1 <code>true</code> if the header font style includes underline, <code>false</code>
     * if the header font style does not include underline
     *
     * @since Lotus Notes 6.5
     */
    void setHeaderFontUnderline(boolean arg1);

    /**
     * Indicates whether the header font style for a column includes
     * strikethrough.
     *
     * @param arg1 <code>true</code> if the header font style includes strikethrough, <code>false</code>
     * if the header font style does not include strikethrough
     *
     * @since Lotus Notes 6.5
     */
    void setHeaderFontStrikethrough(boolean arg1);

    /**
     * Indicates whether the number attributes for a column include parentheses
     * for negative numbers.
     *
     * @param arg1 <code>true</code> if the number attributes include parentheses, <code>false</code>
     * if the number attributes do not include parentheses
     *
     * @since Lotus Notes 6.5
     */
    void setNumberAttribParens(boolean arg1);

    /**
     * Indicates whether the number attributes for a column include punctuation
     * at thousandths.
     *
     * @param arg1 <code>true</code> if the number attributes include punctuation, <code>false</code>
     * if the number attributes do not include punctuation
     *
     * @since Lotus Notes 6.5
     */
    void setNumberAttribPunctuated(boolean arg1);

    /**
     * Indicates whether the number attributes for a column include displaying
     * the number as a percentage.
     *
     * @param arg1 <code>true</code> if the number attributes include percent, <code>false</code>
     * if the number attributes do not include percent
     *
     * @since Lotus Notes 6.5
     */
    void setNumberAttribPercent(boolean arg1);

    /**
     * The name of the target view for a user-sorted column that allows the user
     * to change to another view.
     *
     * @param arg1 name of target view
     * @since Lotus Notes 6.5
     */
    void setResortToViewName(String arg1);
}
