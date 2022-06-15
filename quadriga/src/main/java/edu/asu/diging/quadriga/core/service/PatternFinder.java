package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.core.model.EventGraph;

public interface PatternFinder {
    
    List<Graph> findGraphsWithPattern(GraphPattern pattern, EventGraph eventGraph);

}
