package edu.asu.diging.quadriga.core.conceptpower.service;

import java.util.List;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;

/**
 * A service for working with the ConceptCache entries in the database
 * 
 * @author poojakulkarni
 *
 */
public interface ConceptCacheService {
    
    public ConceptCache getConceptByUri(String uri);
    
    public List<ConceptCache> getConceptsByAlternativeUri(String uri);
    
    public void saveConceptCache(ConceptCache conceptCache);
    
    public ConceptCache updateConceptCache(String uri);

}
