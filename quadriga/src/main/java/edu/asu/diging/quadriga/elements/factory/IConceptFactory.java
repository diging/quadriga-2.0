package edu.asu.diging.quadriga.elements.factory;

import edu.asu.diging.quadriga.model.elements.Concept;

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