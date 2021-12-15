package edu.asu.diging.quadriga.core.conceptpower.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptType;

/**
 * An interface used to retrieve data from conceptpower_concept_type table that
 * stores information for a concept's type extracted from ConceptPower
 * 
 * @author poojakulkarni
 *
 */
@Repository
public interface ConceptTypeRepository extends PagingAndSortingRepository<ConceptType, String> {

}
