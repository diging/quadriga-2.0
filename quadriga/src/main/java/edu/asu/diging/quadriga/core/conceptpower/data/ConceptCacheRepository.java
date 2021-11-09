package edu.asu.diging.quadriga.core.conceptpower.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;

/**
 * 
 * An interface used to retrieve data from conceptpower_concept_entry table that stores data
 * extracted from ConceptPower
 * 
 * @author poojakulkarni
 *
 */
@Repository
public interface ConceptCacheRepository extends PagingAndSortingRepository<ConceptCache, String> {

}
