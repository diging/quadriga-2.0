package edu.asu.diging.quadriga.api.v1.service;

import java.util.List;



import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.core.model.EventGraph;

public interface MapGraphToTriple {
    
    public List<JobPatternInfo> mapPatterns(String collectionId, String jobId, List<EventGraph> eventGraphs, List<PatternMapping> patternMappingList);
    
}
