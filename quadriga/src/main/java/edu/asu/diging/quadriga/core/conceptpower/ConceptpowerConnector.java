package edu.asu.diging.quadriga.core.conceptpower;

import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerResponse;

public interface ConceptpowerConnector {

    ConceptEntry getConceptEntry(String id);

    ConceptpowerResponse findConceptEqualTo(String uri);

}
