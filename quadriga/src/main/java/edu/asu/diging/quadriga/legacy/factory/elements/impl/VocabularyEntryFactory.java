package edu.asu.diging.quadriga.legacy.factory.elements.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.elements.VocabularyEntry;
import edu.asu.diging.quadriga.legacy.factory.elements.IVocabularyEntryFactory;

/**
 * This is the factory class for VocabularyEntry element. This is used to
 * instantiate VocabularyEntry class.
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
@Service
public class VocabularyEntryFactory implements IVocabularyEntryFactory {

    @Override
    public VocabularyEntry createVocabularyEntry() {
        return new VocabularyEntry();
    }

    @Override
    public VocabularyEntry createVocabularyEntry(String sourceUri) {
        VocabularyEntry vocabularyEntryObject = new VocabularyEntry();
        if (sourceUri == null) {
            vocabularyEntryObject.setSourceURI("");
        } else {
            vocabularyEntryObject.setSourceURI(sourceUri);
        }
        return vocabularyEntryObject;
    }
}