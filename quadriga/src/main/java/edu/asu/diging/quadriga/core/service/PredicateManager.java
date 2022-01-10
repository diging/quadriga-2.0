package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.core.model.mapped.Predicate;

public interface PredicateManager {
    
    /**
     * This method is used to get a list of all Predicates with given MappedTripleGroupId
     * 
     * @param mappedTripleGroupId for searching predicates
     * @return a list of Predicates
     */
    public List<Predicate> findByMappedTripleGroupId(String mappedTripleGroupId);
    
}
