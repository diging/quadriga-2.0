package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;

public interface MappedTripleService {

    Predicate storeMappedGraph(Graph graph, MappedCollection mappedCollection, String eventGraphId) throws NodeNotFoundException;

    /**
     * Retrieves the mapped triples for the given collection id
     * @param mappedCollectionId Mapped Collection Id for which the triples are to be fetched
     * @return the retrieved Triples
     */
    List<Triple> getMappedTriples(String mappedCollectionId);

    /**
     * Retrieves the mapped triples for concept with the given uri and excluding the ones containing concept from the ignoreList
     * @param mappedCollectionId Mapped Collection Id for which the triples are to be fetched
     * @param uri URI of the concept
     * @param ignoreList List of concept URIs to be ignored which are directly connected to the query concept
     * @return the retrieved Triples
     */
    List<Triple> getTriplesByUri(String mappedCollectionId, String uri, List<String> ignoreList);
}