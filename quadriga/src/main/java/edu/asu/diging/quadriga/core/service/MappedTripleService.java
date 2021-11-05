package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;

public interface MappedTripleService {

    Predicate storeMappedGraph(Graph graph, MappedCollection mappedCollection) throws NodeNotFoundException;
    
    /**
     * Retrieves the mapped triples for the given collection id
     * @param collectionId Collection Id for which the triples are to be fetched
     * @return the retrieved Triples
     */
    List<Triple> getMappedTriples(String collectionId);

}