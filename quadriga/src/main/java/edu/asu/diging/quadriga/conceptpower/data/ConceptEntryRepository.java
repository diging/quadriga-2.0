package edu.asu.diging.quadriga.conceptpower.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.conceptpower.model.ConceptEntry;

/**
 * 
 * An interface used to retrieve data from conceptpower_concept_entry table that stores data
 * extracted from ConceptPower
 * 
 * @author poojakulkarni
 *
 */
@Repository
public interface ConceptEntryRepository extends PagingAndSortingRepository<ConceptEntry, String> {

}
