package edu.asu.diging.quadriga.api.v1.service;

import java.util.List;



import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;

public interface GraphTripleMapper {
    
    public List<JobPatternInfo> mapPatterns(String collectionId, List<EventGraph> eventGraphs, List<PatternMapping> patternMappingList) throws InvalidObjectIdException, CollectionNotFoundException;
    
}
