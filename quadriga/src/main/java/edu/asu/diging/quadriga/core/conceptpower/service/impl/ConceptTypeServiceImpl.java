package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.conceptpower.data.ConceptTypeRepository;
import edu.asu.diging.quadriga.core.conceptpower.model.ConceptType;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptTypeService;

@Service
public class ConceptTypeServiceImpl implements ConceptTypeService {

    @Autowired
    private ConceptTypeRepository conceptTypeRepository;

    @Override
    public void saveConceptType(ConceptType conceptType) {
        conceptTypeRepository.save(conceptType);
    }

}
