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

package de.jakop.lotus.domingo.groupware;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import de.jakop.lotus.domingo.monitor.ConsoleMonitor;
import de.jakop.lotus.domingo.groupware.map.MailDatabase;
import junit.framework.TestCase;

/**
 * Tests for class {@link Groupware}.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class GroupwareTest extends TestCase {

    /**
     * Test the calendar interface.
     *
     * {@link MailDatabase#getObjects(java.util.Calendar, java.util.Calendar)}.
     *
     * @throws GroupwareException for any groupware problem
     */
    public void testCalendarInterfaceLocal() throws GroupwareException {

        ConsoleMonitor monitor = new ConsoleMonitor();
        monitor.setLevel(ConsoleMonitor.DEBUG);
        Groupware groupware = new Groupware("notes:///local", monitor);

        CalendarInterface calendar = groupware.getCalendar();
        assertNotNull(calendar);

        Calendar fromDate = new GregorianCalendar();
        Calendar toDate = new GregorianCalendar();

        fromDate.set(2007, Calendar.OCTOBER, 6, 0, 0, 0);
        toDate.set(2099, Calendar.NOVEMBER, 7, 23, 59, 59);
        List l = calendar.getObjects(fromDate, toDate);
        monitor.info("Found " + l.size() + " entries");

        Iterator it = l.iterator();
        int counter = 1;

        while (it.hasNext()) {
            CalendarEntryDigest ced = (CalendarEntryDigest) it.next();
            String st = (ced.getStartDateTime() == null ? "unknown" : ced.getStartDateTime().getTime().toString());
            monitor.info("Entry " + counter + " " + st  +  " " + ced.getSubject() + " " + ced.getUnid());
            counter++;
        }
        groupware.close();
    }

    /**
     * Test sending a mail thru the local Notes client.
     *
     * @throws GroupwareException for any groupware problem
     */
    public void testSendMailLocal() throws GroupwareException {
        ConsoleMonitor monitor = new ConsoleMonitor();
        monitor.setLevel(ConsoleMonitor.DEBUG);
        Groupware groupware = new Groupware("notes:///local", monitor);
        sendMail(groupware);
        groupware.close();
    }

    /**
     * Test sending a mail thru DIIOP on a local installed Domino server.
     *
     * @throws GroupwareException for any groupware problem
     */
    public void xtestSendMailDIIOP() throws GroupwareException {
        ConsoleMonitor monitor = new ConsoleMonitor();
        monitor.setLevel(ConsoleMonitor.DEBUG);
        Groupware groupware = new Groupware("notes:///Kurt Riede:password@localhost", monitor);
        sendMail(groupware);
        groupware.close();
    }

    /**
     * Test sending a mail.
     */
    private void sendMail(final Groupware groupware) throws GroupwareException {
        Mailbox mailbox = groupware.getMailbox();
        assertNotNull(mailbox);

        String owner = mailbox.getOwner();
        System.out.println("Owner = " + owner);
        Email email = mailbox.newEmail();
        email.setFrom(owner);
        email.setSubject("Test");
        email.setRecipient(owner);
        email.setBody("Hello world!");
        mailbox.send(email);
    }

    /**
     * Test reading mails.
     *
     * @throws GroupwareException for any groupware problem
     */
    public void testListMails() throws GroupwareException {
        ConsoleMonitor monitor = new ConsoleMonitor();
        monitor.setLevel(ConsoleMonitor.DEBUG);
        Groupware groupware = new Groupware("notes:///", monitor);
        Mailbox mailbox = groupware.getMailbox();
        Iterator inbox = mailbox.getInbox();
        if (!inbox.hasNext()) {
            System.out.println("no mails found!");
        }
        while (inbox.hasNext()) {
            EmailDigest emailDigest = (EmailDigest) inbox.next();
            Email email = mailbox.getEmail(emailDigest);
            System.out.println("--------------------------------");
            System.out.println("From: " + email.getFrom());
            System.out.println("To: " + email.getRecipients());
            System.out.println("Cc: " + email.getCc());
            System.out.println("Bcc: " + email.getBcc());
            System.out.println("Subject: " + email.getSubject());
            System.out.println("Message: ");
            System.out.println(email.getBody());
        }
    }
}
