package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;

public interface ConceptFinder {
    
    ConceptEntry getConcept(String uri);

}
