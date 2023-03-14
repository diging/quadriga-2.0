package edu.asu.diging.quadriga.core.conceptpower.service;

import java.util.List;

import edu.asu.diging.quadriga.core.model.mapped.Concept;

public interface ConceptService {
    
    
    public List<Concept> findByMappedTripleGroupId(String mappedTripleGroupId);

}
