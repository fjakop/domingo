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

package de.jakop.lotus.domingo.groupware.map;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DViewEntry;
import de.jakop.lotus.domingo.groupware.CalendarEntry;
import de.jakop.lotus.domingo.groupware.CalendarEntryDigest;
import de.jakop.lotus.domingo.groupware.CalendarEntry.Type;
import de.jakop.lotus.domingo.map.*;
import de.jakop.lotus.domingo.map.BaseMapper;
import de.jakop.lotus.domingo.map.DirectMapper;
import de.jakop.lotus.domingo.map.MappingException;
import de.jakop.lotus.domingo.util.GregorianDateTime;

/**
 * Mapper for calendar entries, e.g. in a Notes mail database.
 *
 * @see CalendarEntry
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 * @author Thanx to <a href=mailto:d-rizzle at users.sourceforge.net>Dave Rowe</a> for some patches
 */
public final class CalendarEntryMapper extends BaseMapper {

    private static final int TYPE_COLUMN = 14;

    private static final int CHAIR_COLUMN = 11;

    private static final int INFO_COLUMN = 12;

    private static final int START_DATE_TIME_COLUMN = 8;

    private static final int END_DATE_COLUMN = 10;

    private static final Class INSTANCE_CLASS = CalendarEntry.class;

    private static final Class DIGEST_CLASS = CalendarEntryDigest.class;

    /**
     * Constructor.
     *
     * @throws MethodNotFoundException if the getter or setter method was not found for the attribute name
     */
    public CalendarEntryMapper() throws MethodNotFoundException {
        super(INSTANCE_CLASS, DIGEST_CLASS);
        add(new DirectMapper("StartDate", Calendar.class));
        add(new DirectMapper("EndDate", Calendar.class));
        add(new DirectMapper("StartTime", Calendar.class));
        add(new DirectMapper("EndTime", Calendar.class));
        // TODO add timezone handling
//        add(new DirectMapper("StartTimezone", Calendar.class));
//        add(new DirectMapper("EndTimezone", Calendar.class));
        add(new DirectMapper("StartDateTime", List.class));
        add(new ConstantMapper("Form", "Appointment"));
        add(new DirectMapper("Subject", "Title", String.class));
        // TODO fix the mapping of Chair. It can be a List or a String depending on the calendar entry.
        //        add(new DirectMapper("Chair", List.class));
        add(new DirectMapper("Location", String.class));
        add(new DirectMapper("Categories", List.class));
//        add(new DirectMapper("SendAttachments", Boolean.class));
        add(new DirectMapper("SendTo", "RequiredInvitees", List.class));
        add(new DirectMapper("CopyTo", "OptionalInvitees", List.class));
        add(new DirectMapper("BlindCopyTo", "InformedInvitees", List.class));
        add(new DirectMapper("Room", String.class));
        add(new DirectMapper("RoomToReserve", "Rooms", List.class));
        add(new DirectMapper("Resources", "Resources", List.class));
        add(new DirectMapper("APPTUNID", "AppointmentUnid", String.class));
        add(new TypeMapper());
        add(new ComputeCalendarEntry());
        // TODO add missing fields for calendar entry
    }

    /**
     * {@inheritDoc}
     *
     * @see DMapper#newInstance()
     */
    public Object newInstance() {
        return new CalendarEntry();
    }

    /**
     * {@inheritDoc}
     *
     * @see DMapper#newDigest()
     */
    public Object newDigest() {
        return new CalendarEntry();
    }

    /**
     * {@inheritDoc}
     *
     * @see DMapper#map(DViewEntry,
     *      java.lang.Object)
     */
    public void map(final DViewEntry viewEntry, final Object object) throws MappingException {
        CalendarEntryDigest digest = (CalendarEntryDigest) object;
        digest.setUnid(viewEntry.getUniversalID());
        // TODO the following situation is coming up fairly frequently. Is there a way to make this simpler?
        // CHAIR_COLUMN will be zero, one or more strings. Handle list vs. string case.
        List columnValues = viewEntry.getColumnValues();
        Object chairColumnValues = columnValues.get(CHAIR_COLUMN);
        if (chairColumnValues instanceof List) {
            digest.setChairs((List) chairColumnValues);
        } else {
            List chairList = new ArrayList(1);
            chairList.add((String) chairColumnValues);
            digest.setChairs(chairList);
        }
        // digest.setType(getType(Integer.parseInt(((String)
        // columnValues.get(TYPE_COLUMN)))));
        digest.setStartDateTime(getCalendar(columnValues.get(START_DATE_TIME_COLUMN)));
        if (digest.getType() != Type.REMINDER && digest.getType() != Type.ANNIVERSARY) {
            digest.setEndDateTime(getCalendar(columnValues.get(END_DATE_COLUMN)));
        }
        Object infoColumnValues = columnValues.get(INFO_COLUMN);
        if (infoColumnValues instanceof List) {
            List infoValues = (List) infoColumnValues;
            digest.setSubject((String) infoValues.get(0));
            digest.setLocation((String) infoValues.get(1));
        } else {
            digest.setSubject((String) infoColumnValues);
        }
    }

    private static class ComputeCalendarEntry extends CustomMapper {

        /**
         * {@inheritDoc}
         *
         * @see Mapper#map(DDocument,
         *      java.lang.Object)
         */
        public void map(final DDocument document, final Object object) throws MappingException {
            // TODO Auto-generated method stub

        }

        /**
         * {@inheritDoc}
         *
         * @see Mapper#map(java.lang.Object,
         *      DDocument)
         */
        public void map(final Object object, final DDocument document) throws MappingException {
            CalendarEntry entry = (CalendarEntry) object;
            Calendar date = entry.getStartDate();
            Calendar time = entry.getStartTime();
            final Calendar dateTime = new GregorianDateTime();
            if (date != null) {
                dateTime.set(Calendar.YEAR, date.get(Calendar.YEAR));
                dateTime.set(Calendar.MONTH, date.get(Calendar.MONTH));
                dateTime.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
            }
            if (time != null) {
                dateTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
                dateTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
                dateTime.set(Calendar.SECOND, time.get(Calendar.SECOND));
            }
            // TODO add time zone handling
            document.replaceItemValue("CalendarDateTime", dateTime);
        }
    }

    /**
     * Maps the type.
     */
    private static class TypeMapper extends BaseDMapper {

        /**
         * {@inheritDoc}
         * @see Mapper#map(DDocument, java.lang.Object)
         */
        public void map(final DDocument document, final Object object) throws MappingException {
            ((CalendarEntry) object).setUnid(document.getUniversalID());
            if (!"".equals(document.getItemValueString("Type"))) {
                int type = Integer.parseInt(document.getItemValueString("Type"));
                ((CalendarEntry) object).setType(getType((byte) type));
            } else if (!"".equals(document.getItemValueString("AppointmentType"))) {
                int type = Integer.parseInt(document.getItemValueString("AppointmentType"));
                ((CalendarEntry) object).setType(getType((byte) type));
            }
        }

        /**
         * {@inheritDoc}
         *
         * @see Mapper#map(java.lang.Object,
         *      DDocument)
         */
        public void map(final Object object, final DDocument document) throws MappingException {
            document.replaceItemValue("Type", getType(((CalendarEntry) object).getType()));
            document.replaceItemValue("AppointmentType", getType(((CalendarEntry) object).getType()));
        }
    }

    private static int getType(final Type value) {
        if (value == Type.APPOINTMENT) {
            return Type.APPOINTMENT_VALUE;
        } else if (value == Type.ANNIVERSARY) {
            return Type.ANNIVERSARY_VALUE;
        } else if (value == Type.ALLDAYEVENT) {
            return Type.ALLDAYEVENT_VALUE;
        } else if (value == Type.MEETING) {
            return Type.MEETING_VALUE;
        } else if (value == Type.REMINDER) {
            return Type.REMINDER_VALUE;
        }
        throw new IllegalArgumentException("Unknown type: " + value);
    }

    private static Type getType(final int value) {
        if (value == Type.APPOINTMENT_VALUE) {
            return Type.APPOINTMENT;
        } else if (value == Type.ANNIVERSARY_VALUE) {
            return Type.ANNIVERSARY;
        } else if (value == Type.ALLDAYEVENT_VALUE) {
            return Type.ALLDAYEVENT;
        } else if (value == Type.MEETING_VALUE) {
            return Type.MEETING;
        } else if (value == Type.REMINDER_VALUE) {
            return Type.REMINDER;
        }
        throw new IllegalArgumentException("Unknown type: " + value);
    }
}
