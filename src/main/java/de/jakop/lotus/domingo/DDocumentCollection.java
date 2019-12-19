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

import java.util.Iterator;

/**
 * Represents a collection of documents and provides access to documents
 * within it.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DDocumentCollection extends DBase {

    /**
     * Returns an Iterator to loop over all documents in the view.
     *
     * @return Iterator
     */
    Iterator getAllDocuments();

    /**
     * Conducts a full-text search of all the documents in a document
     * collection, and reduces the collection to a sorted collection of those
     * documents that match.
     *
     * <p>See {@link #fullTextSearch(String, int)} for more details.</p>
     *
     * @param query The full-text query
     */
    void fullTextSearch(String query);

    /**
     * Conducts a full-text search of all the documents in a document
     * collection, and reduces the collection to a sorted collection of those
     * documents that match.
     *
     * <p><b>Usage</b></p>
     * <p>This method moves the current pointer to the first document in the
     * collection. The collection of documents that match the full-text query
     * are sorted by relevance, with highest relevance first. You can access the
     * relevance score of each document in the collection using
     * {@link DDocument#getFTSearchScore()} in DDocument.</p>
     *
     * <p>If the database is not full-text indexed, this method works,
     * but less efficiently. To test for an index, use {@link DDatabase#isFTIndexed()}.</p>
     * <p>To create an index on a local database, use {@link DDatabase#updateFTIndex(boolean)}.</p>
     *
     * <p>This method searches all documents in a document collection. To search all documents in a
     * database, use FTSearch in Database. To search only documents found in a
     * particular view, use FTSearch in View or FTSearch in ViewEntryCollection.</p>
     *
     * <p><b>Query syntax</b></p>
     * <p>To search for a word or phrase, enter the word or phrase as is,
     * except that search keywords must be enclosed in quotes. Remember to
     * escape quotes if you are inside a literal. Wildcards, operators, and
     * other syntax are permitted. For the complete syntax rules, see "Finding
     * documents in a database" in Lotus Notes 6 Help.</p>
     *
     * @param query The full-text query
     * @param maxdocs The maximum number of documents you want returned from the
     *            query. Set this parameter to 0 to receive all matching
     *            documents.
     */
    void fullTextSearch(String query, int maxdocs);
}
