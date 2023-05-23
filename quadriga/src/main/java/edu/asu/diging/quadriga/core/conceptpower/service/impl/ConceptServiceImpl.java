package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.conceptpower.service.ConceptService;
import edu.asu.diging.quadriga.core.data.neo4j.ConceptRepository;
import edu.asu.diging.quadriga.core.model.mapped.Concept;


@Service
public class ConceptServiceImpl implements ConceptService{
    
    @Autowired
    private ConceptRepository conceptRepository;

    @Override
    public List<Concept> findByMappedTripleGroupId(String mappedTripleGroupId) {
        return conceptRepository.findByMappedTripleGroupId(mappedTripleGroupId);
    }
    

}
