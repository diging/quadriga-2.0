package edu.asu.diging.quadriga.elements.factory;

import edu.asu.diging.quadriga.model.elements.VocabularyEntry;

/**
 * This is the interface class for VocabularyEntryFactory class which has the
 * following methods: createVocabularyEntry()
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
public interface IVocabularyEntryFactory {

    VocabularyEntry createVocabularyEntry();

    VocabularyEntry createVocabularyEntry(String sourceUri);

}