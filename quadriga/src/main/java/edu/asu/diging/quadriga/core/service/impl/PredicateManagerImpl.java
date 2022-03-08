package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.neo4j.PredicateRepository;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;
import edu.asu.diging.quadriga.core.service.PredicateManager;

@Service
public class PredicateManagerImpl implements PredicateManager {
    
    @Autowired
    private PredicateRepository predicateRepository;

    @Override
    public List<Predicate> findByMappedTripleGroupId(String mappedTripleGroupId) {
        return predicateRepository.findByMappedTripleGroupId(mappedTripleGroupId).orElse(null);

    }

	@Override
	public int countPredicatesByMappedTripleGroup(String mappedTripleGroupId) {
		return predicateRepository.countPredicatesByMappedTripleGroup(mappedTripleGroupId);
	}

   
    
}
