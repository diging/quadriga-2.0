package edu.asu.diging.quadriga.core.conceptpower;

import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerResponse;

public interface ConceptpowerConnector {
    
    ConceptpowerResponse getConceptEntry(String id);
    
    ConceptpowerResponse findConceptEntryEqualTo(String uri);

}
