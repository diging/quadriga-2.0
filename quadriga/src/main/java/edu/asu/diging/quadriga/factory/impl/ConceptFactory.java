package edu.asu.diging.quadriga.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.elements.factory.IConceptFactory;
import edu.asu.diging.quadriga.model.elements.Concept;

/**
 * This is the factory class for Concept element. This is used to instantiate
 * Concept class.
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
@Service
public class ConceptFactory implements IConceptFactory {
    @Override
    public Concept createConcept() {
        return new Concept();
    }

    @Override
    public Concept createConcept(String sourceUri) {
        Concept conceptObject = new Concept();
        conceptObject.setSourceURI(sourceUri == null ? "" : sourceUri);
        return conceptObject;
    }

}