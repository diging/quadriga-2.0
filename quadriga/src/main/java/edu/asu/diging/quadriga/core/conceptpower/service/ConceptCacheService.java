package edu.asu.diging.quadriga.core.conceptpower.service;

import edu.asu.diging.quadriga.core.conceptpower.model.CachedConcept;

/**
 * A service for working with the ConceptCache entries in the database
 * 
 * @author poojakulkarni
 *
 */
public interface ConceptCacheService {

    /**
     * This method gets a ConceptCache entry from the database If it is not present,
     * it checks the alternative URIs to get a conceptCache entry
     * 
     * @param uri used to check in alternativeUris
     * @return a conceptCache entry
     */
    public CachedConcept getConceptByUri(String uri);

    /**
     * This method checks the AlternativeURIs ElementCollection of ConceptCache The
     * Concept which has listed the provided URI as an alternativeURI would be
     * returned
     * 
     * @param uri to be checked as an alternativeURI
     * @return a ConceptCache object from ConceptCache table
     */
    public CachedConcept getConceptByAlternativeUri(String uri);

    /**
     * This method saves the provided ConceptCache entity in the database
     * 
     * @param conceptCache is the entity to be saved
     */
    public void saveConceptCache(CachedConcept conceptCache);

    /**
     * This method deleted the ConceptCache entry that matches the provided URI
     * 
     * @param uri used to delete the ConceptCache entry
     */
    public void deleteConceptCacheByUri(String uri);

}
