package edu.asu.diging.quadriga.core.conceptpower.service;

import edu.asu.diging.quadriga.core.model.mapped.Concept;

public interface ConceptService {
    
    
    public Concept findByMappedTripleGroupId(String mappedTripleGroupId);

}
