package edu.asu.diging.quadriga.api.v1.service;

import java.util.List;


import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMappingList;
import edu.asu.diging.quadriga.core.model.EventGraph;

public interface MapGraphToTriple {
    
    public List<JobPatternInfo> getJobPatternInfo(String collectionId, String jobId, List<EventGraph> eventGraphs, PatternMappingList patternMappingList);
    
}
