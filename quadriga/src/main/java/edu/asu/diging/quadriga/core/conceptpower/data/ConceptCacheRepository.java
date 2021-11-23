package edu.asu.diging.quadriga.core.conceptpower.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;

/**
 * 
 * An interface used to retrieve data from conceptpower_concept_entry table that
 * stores data extracted from ConceptPower
 * 
 * @author poojakulkarni
 *
 */
@Repository
public interface ConceptCacheRepository extends PagingAndSortingRepository<ConceptCache, String> {

    @Query("select c from ConceptCache c where ?1 in elements(c.alternativeUris)")
    public List<ConceptCache> findConceptByAlternativeURI(String uri);

}