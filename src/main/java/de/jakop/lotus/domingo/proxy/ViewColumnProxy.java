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

package de.jakop.lotus.domingo.proxy;

import lotus.domino.NotesException;
import lotus.domino.ViewColumn;
import de.jakop.lotus.domingo.DNotesMonitor;
import de.jakop.lotus.domingo.DView;
import de.jakop.lotus.domingo.DViewColumn;

/**
 * Represents the Domino-Class <code>View</code>.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class ViewColumnProxy extends BaseProxy implements DViewColumn {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3832616283597780788L;

    /**
     * Constructor for DViewImpl.
     *
     * @param theFactory the controlling factory
     * @param database Notes database
     * @param viewColumn the Notes View
     * @param monitor the monitor
     */
    private ViewColumnProxy(final NotesProxyFactory theFactory, final DView view, final ViewColumn viewColumn,
            final DNotesMonitor monitor) {
        super(theFactory, view, viewColumn, monitor);
    }

    /**
     * Returns a view object.
     *
     * @param theFactory the controlling factory
     * @param view the parent view
     * @param viewColumn the notes view object
     * @param monitor the monitor
     * @return a view object
     */
    static ViewColumnProxy getInstance(final NotesProxyFactory theFactory, final DView view, final ViewColumn viewColumn,
            final DNotesMonitor monitor) {
        if (viewColumn == null) {
            return null;
        }
        ViewColumnProxy viewColumnProxy = (ViewColumnProxy) theFactory.getBaseCache().get(viewColumn);
        if (viewColumnProxy == null) {
            viewColumnProxy = new ViewColumnProxy(theFactory, view, viewColumn, monitor);
            viewColumnProxy.setMonitor(monitor);
            theFactory.getBaseCache().put(viewColumn, viewColumnProxy);
        }
        return viewColumnProxy;
    }

    /**
     * Returns the associated Notes view.
     *
     * @return associated Notes view
     */
    private ViewColumn getViewColumn() {
        return (ViewColumn) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getAlignment()
     */
    public int getAlignment() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getAlignment();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getAlignment", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getDateFmt()
     */
    public int getDateFmt() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getDateFmt();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getDateFmt", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getFontColor()
     */
    public int getFontColor() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getFontColor();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getFontColor", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getFontFace()
     */
    public String getFontFace() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getFontFace();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getFontFace", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getFontPointSize()
     */
    public int getFontPointSize() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getFontPointSize();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getFontPointSize", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getFontStyle()
     */
    public int getFontStyle() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getFontStyle();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getFontStyle", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getFormula()
     */
    public String getFormula() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getFormula();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getFormula", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getHeaderAlignment()
     */
    public int getHeaderAlignment() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getHeaderAlignment();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getHeaderAlignment", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getHeaderFontColor()
     */
    public int getHeaderFontColor() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getHeaderFontColor();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getHeaderFontColor", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getHeaderFontFace()
     */
    public String getHeaderFontFace() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getHeaderFontFace();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getHeaderFontFace", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getHeaderFontPointSize()
     */
    public int getHeaderFontPointSize() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getHeaderFontPointSize();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getHeaderFontPointSize", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getHeaderFontStyle()
     */
    public int getHeaderFontStyle() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getHeaderFontStyle();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getHeaderFontStyle", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getItemName()
     */
    public String getItemName() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getItemName();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getItemName", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getListSep()
     */
    public int getListSep() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getListSep();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getListSep", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getNumberAttrib()
     */
    public int getNumberAttrib() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getNumberAttrib();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getNumberAttrib", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getNumberDigits()
     */
    public int getNumberDigits() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getNumberDigits();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getNumberDigits", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getNumberFormat()
     */
    public int getNumberFormat() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getNumberFormat();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getNumberFormat", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getPosition()
     */
    public int getPosition() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getPosition();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getPosition", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getResortToViewName()
     */
    public String getResortToViewName() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getResortToViewName();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getResortToViewName", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getSecondaryResortColumnIndex()
     */
    public int getSecondaryResortColumnIndex() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getSecondaryResortColumnIndex();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getSecondaryResortColumnIndex", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getTimeDateFmt()
     */
    public int getTimeDateFmt() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getTimeDateFmt();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getTimeDateFmt", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getTimeFmt()
     */
    public int getTimeFmt() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getTimeFmt();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getTimeFmt", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getTimeZoneFmt()
     */
    public int getTimeZoneFmt() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getTimeZoneFmt();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getTimeZoneFmt", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getTitle()
     */
    public String getTitle() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getTitle();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getTitle", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#getWidth()
     */
    public int getWidth() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().getWidth();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke getWidth", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isAccentSensitiveSort()
     */
    public boolean isAccentSensitiveSort() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isAccentSensitiveSort();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isAccentSensitiveSort", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isCaseSensitiveSort()
     */
    public boolean isCaseSensitiveSort() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isCaseSensitiveSort();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isCaseSensitiveSort", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isCategory()
     */
    public boolean isCategory() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isCategory();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isCategory", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isField()
     */
    public boolean isField() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isField();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isField", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isFontBold()
     */
    public boolean isFontBold() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isFontBold();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isFontBold", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isFontItalic()
     */
    public boolean isFontItalic() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isFontItalic();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isFontItalic", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isFontStrikethrough()
     */
    public boolean isFontStrikethrough() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isFontStrikethrough();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isFontStrikethrough", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isFontUnderline()
     */
    public boolean isFontUnderline() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isFontUnderline();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isFontUnderline", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isFormula()
     */
    public boolean isFormula() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isFormula();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isFormula", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isHeaderFontBold()
     */
    public boolean isHeaderFontBold() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isHeaderFontBold();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isHeaderFontBold", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isHeaderFontItalic()
     */
    public boolean isHeaderFontItalic() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isHeaderFontItalic();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isHeaderFontItalic", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isHeaderFontStrikethrough()
     */
    public boolean isHeaderFontStrikethrough() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isHeaderFontStrikethrough();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isHeaderFontStrikethrough", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isHeaderFontUnderline()
     */
    public boolean isHeaderFontUnderline() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isHeaderFontUnderline();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isHeaderFontUnderline", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isHidden()
     */
    public boolean isHidden() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isHidden();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isHidden", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isHideDetail()
     */
    public boolean isHideDetail() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isHideDetail();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isHideDetail", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isIcon()
     */
    public boolean isIcon() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isIcon();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isIcon", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isNumberAttribParens()
     */
    public boolean isNumberAttribParens() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isNumberAttribParens();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isNumberAttribParens", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isNumberAttribPercent()
     */
    public boolean isNumberAttribPercent() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isNumberAttribPercent();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isNumberAttribPercent", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isNumberAttribPunctuated()
     */
    public boolean isNumberAttribPunctuated() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isNumberAttribPunctuated();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isNumberAttribPunctuated", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isResize()
     */
    public boolean isResize() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isResize();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isResize", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isResortAscending()
     */
    public boolean isResortAscending() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isResortAscending();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isResortAscending", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isResortDescending()
     */
    public boolean isResortDescending() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isResortDescending();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isResortDescending", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isResortToView()
     */
    public boolean isResortToView() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isResortToView();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isResortToView", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isResponse()
     */
    public boolean isResponse() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isResponse();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isResponse", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isSecondaryResort()
     */
    public boolean isSecondaryResort() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isSecondaryResort();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isSecondaryResort", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isSecondaryResortDescending()
     */
    public boolean isSecondaryResortDescending() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isSecondaryResortDescending();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isSecondaryResortDescending", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isShowTwistie()
     */
    public boolean isShowTwistie() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isShowTwistie();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isShowTwistie", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isSortDescending()
     */
    public boolean isSortDescending() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isSortDescending();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isSortDescending", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#isSorted()
     */
    public boolean isSorted() {
        getFactory().preprocessMethod();
        try {
            return getViewColumn().isSorted();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke isSorted", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setAccentSensitiveSort(boolean)
     */
    public void setAccentSensitiveSort(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setAccentSensitiveSort(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setAccentSensitiveSort", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setAlignment(int)
     */
    public void setAlignment(final int alignment) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setAlignment(alignment);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setAlignment", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setCaseSensitiveSort(boolean)
     */
    public void setCaseSensitiveSort(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setCaseSensitiveSort(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setCaseSensitiveSort", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setDateFmt(int)
     */
    public void setDateFmt(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setDateFmt(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setDateFmt", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFontBold(boolean)
     */
    public void setFontBold(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFontBold(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFontBold", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFontColor(int)
     */
    public void setFontColor(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFontColor(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFontColor", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFontFace(java.lang.String)
     */
    public void setFontFace(final String arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFontFace(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFontFace", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFontItalic(boolean)
     */
    public void setFontItalic(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFontItalic(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFontItalic", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFontPointSize(int)
     */
    public void setFontPointSize(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFontPointSize(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFontPointSize", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFontStrikethrough(boolean)
     */
    public void setFontStrikethrough(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFontStrikethrough(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFontStrikethrough", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFontStyle(int)
     */
    public void setFontStyle(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFontStyle(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFontStyle", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFontUnderline(boolean)
     */
    public void setFontUnderline(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFontUnderline(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFontUnderline", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setFormula(java.lang.String)
     */
    public void setFormula(final String arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setFormula(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setFormula", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderAlignment(int)
     */
    public void setHeaderAlignment(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderAlignment(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderAlignment", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderFontBold(boolean)
     */
    public void setHeaderFontBold(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderFontBold(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderFontBold", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderFontColor(int)
     */
    public void setHeaderFontColor(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderFontColor(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderFontColor", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderFontFace(java.lang.String)
     */
    public void setHeaderFontFace(final String arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderFontFace(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderFontFace", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderFontItalic(boolean)
     */
    public void setHeaderFontItalic(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderFontItalic(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderFontItalic", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderFontPointSize(int)
     */
    public void setHeaderFontPointSize(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderFontPointSize(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderFontPointSize", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderFontStrikethrough(boolean)
     */
    public void setHeaderFontStrikethrough(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderFontStrikethrough(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderFontStrikethrough", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderFontStyle(int)
     */
    public void setHeaderFontStyle(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderFontStyle(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderFontStyle", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHeaderFontUnderline(boolean)
     */
    public void setHeaderFontUnderline(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHeaderFontUnderline(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHeaderFontUnderline", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHidden(boolean)
     */
    public void setHidden(final boolean flag) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHidden(flag);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHidden", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setHideDetail(boolean)
     */
    public void setHideDetail(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setHideDetail(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setHideDetail", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setListSep(int)
     */
    public void setListSep(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setListSep(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setListSep", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setNumberAttrib(int)
     */
    public void setNumberAttrib(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setNumberAttrib(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setNumberAttrib", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setNumberAttribParens(boolean)
     */
    public void setNumberAttribParens(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setNumberAttribParens(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setNumberAttribParens", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setNumberAttribPercent(boolean)
     */
    public void setNumberAttribPercent(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setNumberAttribPercent(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setNumberAttribPercent", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setNumberAttribPunctuated(boolean)
     */
    public void setNumberAttribPunctuated(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setNumberAttribPunctuated(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setNumberAttribPunctuated", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setNumberDigits(int)
     */
    public void setNumberDigits(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setNumberDigits(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setNumberDigits", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setNumberFormat(int)
     */
    public void setNumberFormat(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setNumberFormat(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setNumberFormat", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setResize(boolean)
     */
    public void setResize(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setResize(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setResize", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setResortAscending(boolean)
     */
    public void setResortAscending(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setResortAscending(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setResortAscending", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setResortDescending(boolean)
     */
    public void setResortDescending(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setResortDescending(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setResortDescending", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setResortToView(boolean)
     */
    public void setResortToView(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setResortToView(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setResortToView", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setResortToViewName(java.lang.String)
     */
    public void setResortToViewName(final String arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setResortToViewName(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setResortToViewName", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setSecondaryResort(boolean)
     */
    public void setSecondaryResort(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setSecondaryResort(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setSecondaryResort", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setSecondaryResortColumnIndex(int)
     */
    public void setSecondaryResortColumnIndex(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setSecondaryResortColumnIndex(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setSecondaryResortColumnIndex", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setSecondaryResortDescending(boolean)
     */
    public void setSecondaryResortDescending(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setSecondaryResortDescending(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setSecondaryResortDescending", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setShowTwistie(boolean)
     */
    public void setShowTwistie(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setShowTwistie(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setShowTwistie", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setSortDescending(boolean)
     */
    public void setSortDescending(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setSortDescending(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setSortDescending", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setSorted(boolean)
     */
    public void setSorted(final boolean arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setSorted(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setSorted", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setTimeDateFmt(int)
     */
    public void setTimeDateFmt(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setTimeDateFmt(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setTimeDateFmt", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setTimeFmt(int)
     */
    public void setTimeFmt(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setTimeFmt(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setTimeFmt", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setTimeZoneFmt(int)
     */
    public void setTimeZoneFmt(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setTimeZoneFmt(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setTimeZoneFmt", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setTitle(java.lang.String)
     */
    public void setTitle(final String title) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setTitle(title);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setTitle", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see DViewColumn#setWidth(int)
     */
    public void setWidth(final int arg1) {
        getFactory().preprocessMethod();
        try {
            getViewColumn().setWidth(arg1);
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke setWidth", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see BaseProxy#toString()
     */
    public String toString() {
        getFactory().preprocessMethod();
        try {
            String title = getViewColumn().getTitle();
            if (title != null && title.length() > 0) {
                return title;
            }
            return getViewColumn().getFormula();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot invoke toString", e);
        }
    }
}
