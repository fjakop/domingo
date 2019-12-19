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
 * Represents a Notes database.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DRichTextStyle extends DBase {

    /** Text effect constant. */
    int EFFECTS_NONE = 0;

    /** Text effect constant. */
    int EFFECTS_SUPERSCRIPT = 1;

    /** Text effect constant. */
    int EFFECTS_SUBSCRIPT = 2;

    /** Text effect constant. */
    int EFFECTS_SHADOW = 3;

    /** Text effect constant. */
    int EFFECTS_EMBOSS = 4;

    /** Text effect constant. */
    int EFFECTS_EXTRUDE = 5;

    /** Font face constant. */
    int FONT_ROMAN = 0;

    /** Font face constant. */
    int FONT_HELV = 1;

    /** Font face constant. */
    int FONT_COURIER = 4;

    /** Font face constant. */
    int STYLE_NO_CHANGE = 255;

    /** Constant: Yes. */
    int YES = 1;

    /** Constant: No. */
    int NO = 0;

    /** Constant: Maybe. */
    int MAYBE = 255;

    /** Color constant. */
    int COLOR_BLACK = 0;

    /** Color constant. */
    int COLOR_WHITE = 1;

    /** Color constant. */
    int COLOR_RED = 2;

    /** Color constant. */
    int COLOR_GREEN = 3;

    /** Color constant. */
    int COLOR_BLUE = 4;

    /** Color constant. */
    int COLOR_MAGENTA = 5;

    /** Color constant. */
    int COLOR_YELLOW = 6;

    /** Color constant. */
    int COLOR_CYAN = 7;

    /** Color constant. */
    int COLOR_DARK_RED = 8;

    /** Color constant. */
    int COLOR_DARK_GREEN = 9;

    /** Color constant. */
    int COLOR_DARK_BLUE = 10;

    /** Color constant. */
    int COLOR_DARK_MAGENTA = 11;

    /** Color constant. */
    int COLOR_DARK_YELLOW = 12;

    /** Color constant. */
    int COLOR_DARK_CYAN = 13;

    /** Color constant. */
    int COLOR_GRAY = 14;

    /** Color constant. */
    int COLOR_LIGHT_GRAY = 15;
}
