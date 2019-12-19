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

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jakop.lotus.domingo.DDocument;
import de.jakop.lotus.domingo.DViewEntry;
import de.jakop.lotus.domingo.groupware.Email;
import de.jakop.lotus.domingo.groupware.EmailDigest;
import de.jakop.lotus.domingo.groupware.Email.Importance;
import de.jakop.lotus.domingo.groupware.Email.Priority;
import de.jakop.lotus.domingo.map.*;
import de.jakop.lotus.domingo.map.BaseMapper;
import de.jakop.lotus.domingo.map.DirectMapper;
import de.jakop.lotus.domingo.map.MapMapper;
import de.jakop.lotus.domingo.map.MappingException;


/**
 * Mapper for memos in a Notes mail database.
 *
 * @see Email
 * @see EmailDigest
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class EmailMapper extends BaseMapper {

    // TODO make column numbers configurable for better support of individualized mail templates

    private static final int DEFAULT_SUBJECT_COLUMN = 11;

    private static final int DEFAULT_TIME_COLUMN = 8;

    private static final int DEFAULT_DATE_COLUMN = 7;

    private static final int DEFAULT_WHO_COLUMN = 5;

    private static final Class INSTANCE_CLASS = Email.class;

    private static final Class DIGEST_CLASS = EmailDigest.class;

    /**
     * Constructor.
     *
     * @throws MethodNotFoundException if the getter or setter method was not found for the attribute name
     */
    public EmailMapper() throws MethodNotFoundException {
        super(INSTANCE_CLASS, DIGEST_CLASS);
        add(new ConstantMapper("Form", "Memo"));
        add(new DirectMapper("Subject", String.class));
        add(new DirectMapper("Body", String.class));
        add(new DirectMapper("From", String.class));
        add(new DirectMapper("Principal", String.class));
        add(new DirectMapper("SendTo", "Recipients", List.class));
        add(new DirectMapper("CopyTo", "Cc", List.class));
        add(new DirectMapper("BlindCopyTo", "Bcc", List.class));
        add(new DirectMapper("Categories", "Categories", Set.class));
        add(new DirectMapper("DeliveredDate", Calendar.class));
        add(new PriorityMapper());
        add(new ImportanceMapper());
        add(new HeaderMapper());
        add(new SaveAfterSendMapper());
    }

    /**
     * {@inheritDoc}
     *
     * @see DMapper#newInstance()
     */
    public Object newInstance() {
        return new Email();
    }

    /**
     * {@inheritDoc}
     *
     * @see DMapper#newDigest()
     */
    public Object newDigest() {
        return new EmailDigest();
    }

    /**
     * {@inheritDoc}
     *
     * @see DMapper#map(DViewEntry, java.lang.Object)
     */
    public void map(final DViewEntry viewEntry, final Object object) throws MappingException {
        EmailDigest digest = (EmailDigest) object;
        List columnValues = viewEntry.getColumnValues();
        digest.setUnid(viewEntry.getUniversalID());
        digest.setWho((String) columnValues.get(getWhoColumnIndex()));
        digest.setDate((Calendar) columnValues.get(getDateColumnIndex()));
        digest.setTime((Calendar) columnValues.get(getTimeColumnIndex()));
        // revisit simple hack to suppotr R6.5 and R7
        try {
            digest.setSubject((String) columnValues.get(getSubjectColumnIndex()));
        } catch (RuntimeException e) {
            digest.setSubject((String) columnValues.get(getSubjectColumnIndex() + 1));
        }
    }

    private int getWhoColumnIndex() {
        // TODO make this depending from notes version
        return DEFAULT_WHO_COLUMN;
    }

    private int getDateColumnIndex() {
        // TODO make this depending from notes version
        return DEFAULT_DATE_COLUMN;
    }

    private int getTimeColumnIndex() {
        // TODO make this depending from notes version
        return DEFAULT_TIME_COLUMN;
    }

    private int getSubjectColumnIndex() {
        // TODO make this depending from notes version
        return DEFAULT_SUBJECT_COLUMN;
    }

    /**
     * A mapper for email header values.
     * Disallowed values are the explicit header attributes like
     * <code>From</code>, <code>To</code>, <code>Cc</code>, <code>Bcc</code>,
     * <code>Subject</code> or <code>Body</code> as well as all item-names
     * that are used internally in Notes email documents.
     *
     */
    private static class HeaderMapper extends MapMapper {

        private static final Set DISALLOWED_NAMES = new HashSet();

        static {
            String itemName = "From";
            // disallow explicit headers
            addDisallowedName(itemName);
            addDisallowedName("To");
            addDisallowedName("CopyTo");
            addDisallowedName("BlindCopyTo");
            addDisallowedName("Principal");
            addDisallowedName("Subject");
            addDisallowedName("Body");
            addDisallowedName("DeliveryPriority");
            addDisallowedName("Importance");

            // disallow reserved Notes items
            addDisallowedName("$AltNameLanguageTags");
            addDisallowedName("$EncryptionStatus");
            addDisallowedName("$FILE");
            addDisallowedName("$Fonts");
            addDisallowedName("$KeepPrivate");
            addDisallowedName("$REF");
            addDisallowedName("$RFSaveInfo");
            addDisallowedName("$SignatureStatus");
            addDisallowedName("$StorageBcc");
            addDisallowedName("$StorageCc");
            addDisallowedName("$StorageTo");
            addDisallowedName("$UpdatedBy");
            addDisallowedName("AltCopyTo");
            addDisallowedName("AltFrom");
            addDisallowedName("BGTableColor");
            addDisallowedName("DefaultMailSaveOptions");
            addDisallowedName("Encrypt");
            addDisallowedName("EnterBlindCopyTo");
            addDisallowedName("EnterCopyTo");
            addDisallowedName("EnterSendTo");
            addDisallowedName("Form");
            addDisallowedName("InetBlindCopyTo");
            addDisallowedName("InetCopyTo");
            addDisallowedName("InetSendTo");
            addDisallowedName("In_Reply_To");
            addDisallowedName("Logo");
            addDisallowedName("tmpImp");
            addDisallowedName("Sign");

            // TODO complete list of disallowed names
        }

        /**
         * Ensures that disallowed names are added in all lower-case to
         * allow later case-insensitive checks.
         */
        private static void addDisallowedName(final String itemName) {
            DISALLOWED_NAMES.add(itemName.toLowerCase());
        }

        /**
         * Case-insensitive check if a given name is a disallowed name
         * for an email header.
         *
         * @see MapMapper#isNameAllowed(java.lang.String)
         */
        protected boolean isNameAllowed(final String name) {
            return DISALLOWED_NAMES.contains(name.toLowerCase());
        }

        /**
         * @see MapMapper#getMap()
         */
        protected Map getMap(final Object object) {
            return ((Email) object).getHeaders();
        }
    }

    /**
     * Maps the importance.
     */
    private static final class ImportanceMapper extends BaseDMapper {

        /**
         * {@inheritDoc}
         * @see Mapper#map(DDocument, java.lang.Object)
         */
        public void map(final DDocument document, final Object object) throws MappingException {
            if (!"".equals(document.getItemValueString("Importance"))) {
                ((Email) object).setImportance(get(document.getItemValueString("Importance").charAt(0)));
            } else {
                ((Email) object).setImportance(Importance.NORMAL);
            }
        }

        /**
         * {@inheritDoc}
         * @see Mapper#map(java.lang.Object, DDocument)
         */
        public void map(final Object object, final DDocument document) throws MappingException {
            document.replaceItemValue("Importance", getImportance(((Email) object).getImportance()));
        }

        private static int getImportance(final Importance value) {
            if (value == Importance.LOW) {
                return 1;
            } else if (value == Importance.NORMAL) {
                return 2;
            } else if (value == Importance.HIGH) {
                return 1;
            }
            throw new IllegalArgumentException("Unknown importance: " + value);
        }

        private Importance get(final char value) {
            if (value == '1') {
                return Importance.HIGH;
            } else if (value == '2') {
                return Importance.NORMAL;
            } else if (value == '3') {
                return Importance.LOW;
            }
            throw new IllegalArgumentException("Unknown importance: " + value);
        }
    }

    /**
     * Maps the priority.
     */
    private static final class PriorityMapper extends BaseDMapper {

        /**
         * {@inheritDoc}
         * @see Mapper#map(DDocument, java.lang.Object)
         */
        public void map(final DDocument document, final Object object) throws MappingException {
            if (!"".equals(document.getItemValueString("DeliveryPriority"))) {
                ((Email) object).setPriority(getPriority(document.getItemValueString("DeliveryPriority").charAt(0)));
            } else {
                ((Email) object).setPriority(Priority.NORMAL);
            }
        }

        /**
         * {@inheritDoc}
         * @see Mapper#map(java.lang.Object, DDocument)
         */
        public void map(final Object object, final DDocument document) throws MappingException {
            document.replaceItemValue("DeliveryPriority", getPriority(((Email) object).getPriority()));
        }

        private static String getPriority(final Priority value) {
            if (value == Priority.LOW) {
                return "L";
            } else if (value == Priority.NORMAL) {
                return "N";
            } else if (value == Priority.HIGH) {
                return "H";
            }
            throw new IllegalArgumentException("Unknown priority: " + value);
        }

        private static Priority getPriority(final char value) {
            if (value == 'H') {
                return Priority.HIGH;
            } else if (value == 'N') {
                return Priority.NORMAL;
            } else if (value == 'L') {
                return Priority.LOW;
            }
            throw new IllegalArgumentException("Unknown priority: " + value);
        }
    }

    /**
     * Mapper for the attribute <code>SaveAfterSend</code>.
     */
    private static final class SaveAfterSendMapper extends CustomMapper {

        /**
         * {@inheritDoc}
         * @see Mapper#map(DDocument, java.lang.Object)
         */
        public void map(final DDocument document, final Object object) throws MappingException {
            ((Email) object).setSaveOnSend(true);
        }

        /**
         * {@inheritDoc}
         * @see Mapper#map(java.lang.Object, DDocument)
         */
        public void map(final Object object, final DDocument document) throws MappingException {
            document.setSaveMessageOnSend(((Email) object).getSaveOnSend());
        }
    }
}
