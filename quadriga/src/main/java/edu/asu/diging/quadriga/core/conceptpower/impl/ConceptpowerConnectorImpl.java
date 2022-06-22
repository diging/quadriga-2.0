package edu.asu.diging.quadriga.core.conceptpower.impl;

import org.springframework.beans.factory.annotation.Value;

import edu.asu.diging.quadriga.core.conceptpower.ConceptpowerConnector;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerResponse;

public class ConceptpowerConnectorImpl implements ConceptpowerConnector {

    @Value("${conceptpower_base_url}")
    private String conceptpowerBaseUrl;
    
    @Value("${conceptpower_getconcept_url}")
    private String conceptpowerGetConceptUrl;
    
    @Value("${conceptpower_searchconcept_url}")
    private String conceptpowerSearchconceptUrl;
    
    @Override
    public ConceptpowerResponse getConceptEntry(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConceptpowerResponse findConceptEntryEqualTo(String uri) {
        // TODO Auto-generated method stub
        return null;
    }

}
