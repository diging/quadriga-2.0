package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.core.model.EventGraph;

public interface AsyncPatternProcessor {

    public void processPattern(String jobId, String collectionId, GraphPattern graphPattern, List<EventGraph> networks);

}
