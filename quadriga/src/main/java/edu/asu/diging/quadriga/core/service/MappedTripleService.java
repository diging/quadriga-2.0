package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.MappedTriplesPage;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;

public interface MappedTripleService {

    Predicate storeMappedGraph(Graph graph, MappedTripleGroup mappedTripleGroup) throws NodeNotFoundException;
    
    /**
     * Retrieves the mapped triples for the given triple group id
     * @param mappedTripleGroupId Group Id for which the triples are to be fetched
     * @return the retrieved Triples
     */
    MappedTriplesPage getMappedTriples(String mappedTripleGroupId, int page, int pageSize);

}