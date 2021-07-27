package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;

public interface MappedTripleService {

    Predicate storeMappedGraph(Graph graph) throws NodeNotFoundException;

}