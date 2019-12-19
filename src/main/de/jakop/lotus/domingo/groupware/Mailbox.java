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

import java.util.Iterator;

/**
 * Interface to the mail functionality of a Notes mail database.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface Mailbox {

    /**
     * Returns the name of the owner of a mailbox.
     *
     * @return name of owner
     */
    String getOwner();

    /**
     * Returns an iterator over all mails in the Inbox.
     *
     * @return iterator over all mails in the Inbox
     */
    Iterator getInbox();

    /**
     * Returns an iterator over all mails in the Inbox.
     *
     * <p>Depending on how the inbox is sorted (ascending or descending by
     * date), choose where to start reading mails.</p>
     *
     * @param reverseOrder whether to start at the beginning or at the end
     * @return iterator over all mails in the Inbox
     */
    Iterator getInbox(final boolean reverseOrder);


    /**
     * Creates a new Memo.
     *
     * @return the new Memo instance
     */
    Email newEmail();

    /**
     * Sends a memo.
     *
     * @param memo the memo to send
     */
    void saveAsDraft(final Email memo);

    /**
     * Sends a memo.
     *
     * @param memo the memo to send
     */
    void send(final Email memo);

    /**
     * Creates a new memo as a forward of an existing memo.
     *
     * @param memo the memo to forward
     * @return forward memo
     */
    Email forward(final Email memo);

    /**
     * Creates a new memo as a forward to an existing memo.
     *
     * @param memo an existing memo
     * @param withAttachments forward with or without attachments
     * @return the reply memo
     */
    Email forward(final Email memo, final boolean withAttachments);

        /**
     * Creates a new memo as a reply to the sender of the original memo.
     *
     * @param memo the memo to forward
     * @param withHistory if the original mail should be included
     * @param withAttachments if attachments should be included
     * @return reply memo
     */
    Email reply(final Email memo, final boolean withHistory, final boolean withAttachments);

    /**
     * Creates a new memo as a reply to all original recipients of the original memo.
     *
     * @param memo the memo to forward
     * @param withHistory if the original mail should be included
     * @param withAttachments if attachments should be included
     * @return reply memo
     */
    Email replyToAll(final Email memo, final boolean withHistory, final boolean withAttachments);

    /**
     * Deletes an existing memo.
     *
     * @param memo a memo to delete
     */
    void remove(final Email memo);

    /**
     * Deletes an existing memo.
     *
     * @param memo a memo digest to delete
     */
    void remove(final EmailDigest memo);

    /**
     * Returns the email represented by the given email-digest.
     *
     * @param emailDigest an email-digest
     * @return the corresponding email
     */
    Email getEmail(EmailDigest emailDigest);
}
