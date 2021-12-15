package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.core.model.mapped.Concept;

public interface ConceptManager {
    
    public List<Concept> findConceptsByMappingTypeAndMappedCollectionId(String mappingType, String mappedCollectionId);

}
