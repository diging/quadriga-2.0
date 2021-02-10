package edu.asu.diging.quadriga.domain.elements.factory;

import edu.asu.diging.quadriga.domain.elements.Concept;

/**
 * This is the interface class for ConceptFactory class which has the following
 * methods: createConcept()
 * 
 * @author Veena Borannagowda
 *
 */
public interface IConceptFactory {

    Concept createConcept();

    Concept createConcept(String sourceUri);

}