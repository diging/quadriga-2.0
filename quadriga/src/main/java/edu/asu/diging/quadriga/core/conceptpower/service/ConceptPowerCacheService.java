package edu.asu.diging.quadriga.core.conceptpower.service;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;

/**
 * A service for working with the ConceptCache entries in the database
 * 
 * @author poojakulkarni
 *
 */
@Service
public interface ConceptPowerCacheService {
    
    public ConceptCache getConceptByUri(String uri);
    
    public ConceptCache saveConceptCache(String uri);
    
    public ConceptCache updateConceptCache(String uri);

}
