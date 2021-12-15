package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.neo4j.ConceptRepository;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.service.ConceptManager;

@Service
public class ConceptManagerImpl implements ConceptManager {

    @Autowired
    private ConceptRepository conceptRepository;

    @Override
    public List<Concept> findConceptsByMappingTypeAndMappedCollectionId(String mappingType, String mappedCollectionId) {
        return conceptRepository.findByMappingTypeAndMappedCollectionId(mappingType, mappedCollectionId).orElse(null);
    }

}
