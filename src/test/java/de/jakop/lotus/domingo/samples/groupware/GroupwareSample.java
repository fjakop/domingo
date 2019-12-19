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
package de.jakop.lotus.domingo.samples.groupware;

import java.util.Calendar;
import java.util.Iterator;

import de.jakop.lotus.domingo.groupware.AddressBook;
import de.jakop.lotus.domingo.groupware.CalendarEntry;
import de.jakop.lotus.domingo.groupware.CalendarInterface;
import de.jakop.lotus.domingo.groupware.ContactDigest;
import de.jakop.lotus.domingo.groupware.Email;
import de.jakop.lotus.domingo.groupware.EmailDigest;
import de.jakop.lotus.domingo.groupware.Groupware;
import de.jakop.lotus.domingo.groupware.GroupwareException;
import de.jakop.lotus.domingo.groupware.Mailbox;
import de.jakop.lotus.domingo.monitor.ConsoleMonitor;


/**
 * A simple example about how to use the groupware package of domingo.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public class GroupwareSample {

    private static final int MAX_MAILS = 10;

    private static final int MAX_CONTACTS = 10;

    /**
     * The main method.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        GroupwareSample sample = new GroupwareSample();
        try {
            sample.run();
        } catch (GroupwareException e) {
            e.printStackTrace();
        }
    }

    /**
     * run
     * @throws GroupwareException if the databases cannot be opened
     */
    private void run() throws GroupwareException {

        //initialize the groupware package
        ConsoleMonitor monitor = new ConsoleMonitor();
        monitor.setLevel(ConsoleMonitor.DEBUG);
        Groupware groupware = new Groupware("notes:///local/", monitor);
        Mailbox mailbox = groupware.getMailbox();
        CalendarInterface calendar = groupware.getCalendar();
        Iterator addressbooks = groupware.getAddressBooks();

        while (addressbooks.hasNext()) {
            System.out.println("---");
            AddressBook ab = (AddressBook) addressbooks.next();
            System.out.println("Title: " + ab.getTitle() + " Private? " + ab.isPrivate() + " Public? " + ab.isPublic());

            Iterator contacts = ab.getContacts();
            if (!contacts.hasNext()) {
                System.out.println("no contacts found");
            } else {
                System.out.println("Printing first " + MAX_CONTACTS + " entries");
                int i = 0;
                while (contacts.hasNext() && i < MAX_CONTACTS) {
                    i++;
                    ContactDigest c = (ContactDigest) contacts.next();
                    System.out.println(i + ". " + c.getFullName() + " " + c.getEmail());
                }
            }
        }

        // Read the last ten mails from the inbox
        System.out.println("---");
        Iterator inbox = mailbox.getInbox();
        int i = 0;
        while (inbox.hasNext() && i++ < MAX_MAILS) {
            EmailDigest emailDigest = (EmailDigest) inbox.next();
            System.out.println(emailDigest.toString());
        }

        // create and send an email
        System.out.println("---");
        Email email = new Email();
        email.setSubject("Test");
        email.setRecipient("kriede@users.sourceforge.net");
        email.setBody("Hello world!");
        mailbox.send(email);
        System.out.println("Email sent");

        // create a new calendar entry
        CalendarEntry entry = new CalendarEntry();
        entry.setTitle("New Years Party");
        entry.setStartDate(2006, Calendar.DECEMBER, 31);
        entry.setStartTime(20, 0, 0);
        entry.setEndDate(2007, Calendar.JANUARY, 1);
        entry.setEndTime(5, 0, 0);
        calendar.save(entry);
        System.out.println("Appointment created");
    }
}
