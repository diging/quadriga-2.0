package edu.asu.diging.quadriga.core.conceptpower.data;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.conceptpower.model.CachedConcept;

/**
 * 
 * An interface used to retrieve data from conceptpower_concept_entry table that
 * stores data extracted from ConceptPower
 * 
 * @author poojakulkarni
 *
 */
@Repository
public interface ConceptCacheRepository extends PagingAndSortingRepository<CachedConcept, String> {

    @Query("SELECT c from CachedConcept c WHERE ?1 in elements(c.alternativeUris)")
    public List<CachedConcept> findConceptByAlternativeURI(String uri);
    
    public CachedConcept findByUri(String uri);
}