package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;

public interface ConceptFinder {
    
    /**
     * Retrieves the concept entry for the given uri by deriving various formats and using appropriate conceptpower API
     * @param uri appellation event uri for which the concept entry is to be retrieved
     * @return the retrieved concept
     */
    ConceptEntry getConcept(String uri);

}
