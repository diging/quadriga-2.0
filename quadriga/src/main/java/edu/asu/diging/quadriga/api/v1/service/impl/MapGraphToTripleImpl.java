package edu.asu.diging.quadriga.api.v1.service.impl;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.model.PatternMappingList;
import edu.asu.diging.quadriga.api.v1.service.MapGraphToTriple;
import edu.asu.diging.quadriga.core.exceptions.JobNotFoundException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;

@Service
public class MapGraphToTripleImpl implements MapGraphToTriple{
    
    private final String quadrigaBaseUri = "http://localhost:8081/quadriga/" ;
    private final String quadrigaJobStatusUri = "api/v1/job/";
    private final String quadrigaCollectionPageUri = "auth/collections/";
    
    @Autowired
    private AsyncPatternProcessor asyncPatternProcessor;

    @Override
    public List<JobPatternInfo> getJobPatternInfo(String collectionId, String jobId,List<EventGraph> eventGraphs, PatternMappingList patternMappingList) {
        
        List<JobPatternInfo> jobInfos = new ArrayList<>();
        
        for (PatternMapping pattern : patternMappingList.getPatternMappings()) {
            
            try {
                asyncPatternProcessor.processPattern(jobId, collectionId, pattern, eventGraphs);
            } catch (JobNotFoundException e) {
                return null;
            }
            JobPatternInfo jobInfo = new JobPatternInfo();
            jobInfo.setJobId(jobId);
            jobInfo.setTrack(quadrigaBaseUri + quadrigaJobStatusUri + jobId + "/status");
            jobInfo.setExplore(quadrigaBaseUri + quadrigaCollectionPageUri + collectionId);
            jobInfos.add(jobInfo);
        }
        
        return jobInfos;
    }
}
