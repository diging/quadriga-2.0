package edu.asu.diging.quadriga.domain.elements.factory;

import edu.asu.diging.quadriga.domain.elements.VocabularyEntry;

/**
 * This is the interface class for VocabularyEntryFactory class which has the
 * following methods: createVocabularyEntry()
 * 
 * @author Veena Borannagowda
 *
 */
public interface IVocabularyEntryFactory {

    VocabularyEntry createVocabularyEntry();

    VocabularyEntry createVocabularyEntry(String sourceUri);

}