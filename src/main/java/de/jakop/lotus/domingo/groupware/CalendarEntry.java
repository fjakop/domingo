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

package de.jakop.lotus.domingo.groupware;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import de.jakop.lotus.domingo.map.BaseInstance;
import de.jakop.lotus.domingo.util.DateUtil;
import de.jakop.lotus.domingo.util.GregorianDate;
import de.jakop.lotus.domingo.util.GregorianTime;

/**
 * Calendar entry, e.g. in Notes mail databases.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 * @author Thanx to <a href=mailto:d-rizzle at users.sourceforge.net>Dave Rowe</a> for some patches
 */
public class CalendarEntry extends BaseInstance {

    /**
     * Represents all possible values for the importance.
     */
    public static final class Type {

        /** calendar entry type: Appointment. */
        public static final Type APPOINTMENT = new Type((byte) 0, "appointment");

        /** calendar entry type: Anniversary. */
        public static final Type ANNIVERSARY = new Type((byte) 1, "anniversary");

        /** calendar entry type: All Day Event. */
        public static final Type ALLDAYEVENT = new Type((byte) 2, "all-day-event");

        /** calendar entry type: Appointment. */
        public static final Type MEETING = new Type((byte) 3, "meeting");

        /** calendar entry type: Reminder. */
        public static final Type REMINDER = new Type((byte) 4, "reminder");

        /** calendar entry type value: Appointment. */
        public static final int APPOINTMENT_VALUE = 0;

        /** calendar entry type value: Anniversary. */
        public static final int ANNIVERSARY_VALUE = 1;

        /** calendar entry type value: All Day Event. */
        public static final int ALLDAYEVENT_VALUE = 2;

        /** calendar entry type value: Appointment. */
        public static final int MEETING_VALUE = 3;

        /** calendar entry type value: Reminder. */
        public static final int REMINDER_VALUE = 4;

        private static final Type[] TYPES;

        static {
            TYPES = new Type[REMINDER.fValue + 1];
            int i = 0;
            TYPES[i++] = APPOINTMENT;
            TYPES[i++] = ANNIVERSARY;
            TYPES[i++] = ALLDAYEVENT;
            TYPES[i++] = MEETING;
            TYPES[i++] = REMINDER;
        }

        private byte fValue;

        private transient final String fName;

        private Type(final byte i, final String name) {
            fValue = i;
            fName = name;
        }

        /**
         * {@inheritDoc}
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return fName;
        }

        private Object readResolve() throws ObjectStreamException {
            if (fValue >= 0 && fValue <= TYPES.length) {
                return TYPES[fValue];
            }
            throw new InvalidObjectException("Attempt to resolve unknown type: " + fValue);
        }
    }

    /** Subject. */
    private String fTitle;

    /** The person who has the chair. */
    private String fChair;

    /**
     * Type of a calendar entry. Can be one of {@link #APPOINTMENT},
     * {@link #ANNIVERSARY}, {@link #ALLDAYEVENT}, {@link #MEETING} or
     * {@link #REMINDER}, defaults to {@link #MEETING}.
     */
    private Type fType = Type.MEETING;

    /** Location. */
    private String fLocation;

    /** Categories (List of Strings). */
    private List fCategories = new ArrayList();

    /** Sent attachments with the meeting invitation. */
    private boolean fSendAttachments;

    /** The date when the Calendar Entry starts (in local time). */
    private Calendar fStartDate;

    /** The date when the Calendar Entry ends (in local time). */
    private Calendar fEndDate;

    /** The time when the Calendar Entry starts (in local time). */
    private Calendar fStartTime;

    /** The time when the Calendar Entry ends (in local time). */
    private Calendar fEndTime;

    /** The list of Start/End Times for a repeating event. */
    private List fStartDateTime;

    /** Required invitees (List of Strings). */
    private List fRequiredInvitees;

    /** Optional invitees (List of Strings). */
    private List fOptionalInvitees;

    /** Optional informed persons (List of Strings). */
    private List fInformedInvitees;

    /** List of allocated rooms. */
    private List fRooms;

    /** Single allocated room. */
    private String fRoom;

    /** List of allocated resources. */
    private List fResources;

    /** Appointment Unique ID - this is UNID of the event's parent document, or an imported iCalendar event UID */
    private String fAppointmentUnid;

    // TODO add missing fields for calendar entry

    /**
     * Default Constructor.
     */
    public CalendarEntry () {

    }

    /**
     * Copy constructor.
     *
     * @param copyFrom CalendarEntry to copy from.
     */
    public CalendarEntry (final CalendarEntry copyFrom) {
        fTitle = copyFrom.getTitle();
        fChair = copyFrom.getChair();
        fType = copyFrom.getType();
        fLocation = copyFrom.getLocation();
        fCategories = copyFrom.getCategories();
        fSendAttachments = copyFrom.isSendAttachments();
        fStartDate = copyFrom.getStartDate();
        fEndDate = copyFrom.getEndDate();
        fStartTime = copyFrom.getStartTime();
        fEndTime = copyFrom.getEndTime();
        fStartDateTime = copyFrom.getStartDateTime();
        fRequiredInvitees = copyFrom.getRequiredInvitees();
        fOptionalInvitees = copyFrom.getOptionalInvitees();
        fInformedInvitees = copyFrom.getInformedInvitees();
        fRooms = copyFrom.getRooms();
        fResources = copyFrom.getResources();
        fAppointmentUnid = copyFrom.getAppointmentUnid();
    }

    /**
     * Returns the end date of a calendar entry.
     *
     * @return Returns the endDate.
     */
    public final Calendar getEndDate() {
        return fEndDate;
    }

    /**
     * @param date The endDate to set.
     */
    public final void setEndDate(final Calendar date) {
        this.fEndDate = date instanceof GregorianDate ? date : new GregorianDate(date);
    }

    /**
     * Sets the start date of a calendar entry.
     *
     * <p>The first month of the year is <code>JANUARY</code> which is 0; the
     * last month is <code>DEDCEMBER</code> which is 11.</p>
     *
     * @param year the year of the endDate to set.
     * @param month the month of the endDate to set.
     * @param day the day of the endDate to set.
     */
    public final void setEndDate(final int year, final int month, final int day) {
        fEndDate = new GregorianDate(year, month, day);
    }

    /**
     * Returns the end time of a calendar entry.
     *
     * @return Returns the endTime.
     */
    public final Calendar getEndTime() {
        return fEndTime;
    }

    /**
     * Sets the end time of a calendar entry.
     *
     * @param time The endTime to set.
     */
    public final void setEndTime(final Calendar time) {
        this.fEndTime = time instanceof GregorianTime ? time : new GregorianTime(time);
    }

    /**
     * Sets the end time of a calendar entry.
     *
     * @param hours the hours of the endDate to set.
     * @param minutes the minutes of the endDate to set.
     * @param seconds the seconds of the endDate to set.
     */
    public final void setEndTime(final int hours, final int minutes, final int seconds) {
        this.fEndTime = new GregorianTime(hours, minutes, seconds);
    }

    /**
     * Returns the start date of a calendar entry.
     *
     * @return Returns the startDate.
     */
    public final Calendar getStartDate() {
        return fStartDate;
    }

    /**
     * Sets the start date of a calendar entry.
     *
     * @param date The startDate to set.
     */
    public final void setStartDate(final Calendar date) {
        this.fStartDate = date instanceof GregorianDate ? date : new GregorianDate(date);
    }

    /**
     * Sets the start date of a calendar entry.
     *
     * <p>The first month of the year is <code>JANUARY</code> which is 0; the
     * last month is <code>DEDCEMBER</code> which is 11.</p>
     *
     * @param year the year of the endDate to set.
     * @param month the month of the endDate to set.
     * @param day the day of the endDate to set.
     */
    public final void setStartDate(final int year, final int month, final int day) {
        fStartDate = new GregorianDate(year, month, day);
    }

    /**
     * Returns the start time.
     *
     * @return Returns the startTime.
     */
    public final Calendar getStartTime() {
        return fStartTime;
    }

    /**
     * Sets the start time.
     *
     * @param time The startTime to set.
     */
    public final void setStartTime(final Calendar time) {
        this.fStartTime = time instanceof GregorianTime ? time : new GregorianTime(time);
    }

    /**
     * Sets the start time.
     *
     * @param hours the hours of the endDate to set.
     * @param minutes the minutes of the endDate to set.
     * @param seconds the seconds of the endDate to set.
     */
    public final void setStartTime(final int hours, final int minutes, final int seconds) {
        this.fStartTime = new GregorianTime(hours, minutes, seconds);
    }

    /**
     * Sets the title.
     *
     * @param title the title
     */
    public final void setTitle(final String title) {
        fTitle = title;
    }

    /**
     * Returns the chair of a calendar entry.
     *
     * @return chair the chair
     */
    public final String getChair() {
        return fChair;
    }

    /**
     * Sets the chair of a calendar entry.
     *
     * @param chair the chair
     */
    public final void setChair(final String chair) {
        fChair = chair;
    }

    /**
     * Returns the informed invitees of a calendar entry.
     *
     * @return chair the informed invitees
     */
    public final List getInformedInvitees() {
        return fInformedInvitees;
    }

    /**
     * Sets the informed invitees of a calendar entry.
     *
     * @param informedInvitees the informed invitees
     */
    public final void setInformedInvitees(final List informedInvitees) {
        fInformedInvitees = informedInvitees;
    }

    /**
     * Returns the location of a calendar entry.
     *
     * @return chair the location
     */
    public final String getLocation() {
        if (fLocation == null) {
            return fRoom;
        }
        return fLocation;
    }

    /**
     * Sets the location a calendar entry.
     *
     * @param location the location
     */
    public final void setLocation(final String location) {
        fLocation = location;
    }

    /**
     * Returns the optional invitees of a calendar entry.
     *
     * @return chair the optional invitees
     */
    public final List getOptionalInvitees() {
        return fOptionalInvitees;
    }

    /**
     * Sets the optional invitees of a calendar entry.
     *
     * @param optionalInvitees the optional invitees
     */
    public final void setOptionalInvitees(final List optionalInvitees) {
        fOptionalInvitees = optionalInvitees;
    }

    /**
     * Returns the required invitees of a calendar entry.
     *
     * @return chair the required invitees
     */
    public final List getRequiredInvitees() {
        return fRequiredInvitees;
    }

    /**
     * Sets the required invitees of a calendar entry.
     *
     * @param requiredInvitees the required invitees
     */
    public final void setRequiredInvitees(final List requiredInvitees) {
        fRequiredInvitees = requiredInvitees;
    }

    /**
     * Returns the resources of a calendar entry.
     *
     * @return chair the resources
     */
    public final List getResources() {
        return fResources;
    }

    /**
     * Sets the resources of a calendar entry.
     *
     * @param resources the resources
     */
    public final void setResources(final List resources) {
        fResources = resources;
    }

    /**
     * Returns the rooms of a calendar entry.
     *
     * @return chair the rooms
     */
    public final List getRooms() {
        return fRooms;
    }

    /**
     * Sets the rooms of a calendar entry.
     *
     * @param rooms the rooms
     */
    public final void setRooms(final List rooms) {
        fRooms = rooms;
    }

    /**
     *
     * @return room
     */
    public final String getRoom() {
        if (fRoom == null) {
            return fLocation;
        }
        return fRoom;
    }

    /**
     *
     * @param room room to set.
     */
    public final void setRoom(final String room) {
        fRoom = room;
    }

    /**
     * Checks if attachments should be sent.
     *
     * @return <code>true</code> if attachments should be sent else <code>false</code>
     */
    public final boolean isSendAttachments() {
        return fSendAttachments;
    }

    /**
     * Sets if attachments should be sent.
     *
     * @param sendAttachments <code>true</code> if attachments should be sent else <code>false</code>
     */
    public final void setSendAttachments(final boolean sendAttachments) {
        fSendAttachments = sendAttachments;
    }

    /**
     * Returns the type of a calendar entry.
     *
     * @return type of calendar entry
     */
    public final Type getType() {
        return fType;
    }

    /**
     * Sets the type of a calendar entry.
     *
     * @param type of type
     */
    public final void setType(final Type type) {
        fType = type;
    }

    /**
     * Returns the title of a calendar entry.
     *
     * @return title of calendar entry
     */
    public final String getTitle() {
        return fTitle;
    }

    /**
     * Returns the categories of a calendar entry.
     *
     * @return list of categories of calendar entry
     */
    public final List getCategories() {
        return fCategories;
    }

    /**
     * Adds a category to a calendar entry.
     *
     * @param category the category
     */
    public final void addCategory(final String category) {
        fCategories.add(category);
    }

    /**
     * Adds categories to a calendar entry.
     *
     * @param categories the categories
     */
    public final void addCategories(final Collection categories) {
        fCategories.addAll(categories);
    }

    /**
     * Sets the categories of a calendar entry.
     *
     * @param categories the categories
     */
    public final void setCategories(final List categories) {
        fCategories = categories;
    }

    /**
     * Returns the list of occurences for a repeating event.
     *
     * @return list of start/end times for a repeating event.
     */
    public final List getStartDateTime() {
        return fStartDateTime;
    }

    /**
     * Set list of occurences for repeating event.
     *
     * @param startDateTime list of occurences.
     */
    public final void setStartDateTime(final List startDateTime) {
        fStartDateTime = startDateTime;
    }

    /**
     * Returns the Appointment UNID of the calendar document.
     * @return Appointment UNID
     */
    public final String getAppointmentUnid() {
        return fAppointmentUnid;

    }

    /**
     * @param appointmentUnid the appointmentUnid to set
     */
    public final void setAppointmentUnid(final String appointmentUnid) {
        fAppointmentUnid = appointmentUnid;
    }

    /**
     * @return  a string representation of the object.
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return fChair + " " + fTitle + " " + fLocation + " Start: " + DateUtil.getDateString(fStartDate) + " " + DateUtil.getTimeString(fStartTime);
    }
}
