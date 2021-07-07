package edu.asu.diging.quadriga.legacy.factory.elements;

import edu.asu.diging.quadriga.core.model.elements.Concept;

/**
 * This is the interface class for ConceptFactory class which has the following
 * methods: createConcept()
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
public interface IConceptFactory {

    Concept createConcept();

    Concept createConcept(String sourceUri);

}