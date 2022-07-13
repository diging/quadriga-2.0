package edu.asu.diging.quadriga.core.conceptpower;

import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerResponse;

public interface ConceptpowerConnector {

    /**
     * Retrieves the concept for the given id from Conceptpower
     * @param id is the concept id
     * @return the retrieved concept
     */
    ConceptEntry getConceptEntry(String id);

    /**
     * Retrieves the concept entries which are equal to the given interpretation
     * @param uri URI of interpretation
     * @return the retrieved results
     */
    ConceptpowerResponse findConceptEqualTo(String uri);

}
