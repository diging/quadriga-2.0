package edu.asu.diging.quadriga.api.v1.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.model.PatternMappingList;
import edu.asu.diging.quadriga.api.v1.service.MapGraphToTriple;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;

@Service
public class MapGraphToTripleImpl implements MapGraphToTriple{
    
    @Value("${quadriga_base_url}")
    private String quadrigaBaseUri;
    
    @Value("${quadriga_job_status_api}")
    private String quadrigaJobStatusUri;
    
    @Value("${quadriga_collection_page}")
    private String quadrigaCollectionPageUri;
    
    @Autowired
    private AsyncPatternProcessor asyncPatternProcessor;

    @Override
    public List<JobPatternInfo> getJobPatternInfo(String collectionId, String jobId,List<EventGraph> eventGraphs, PatternMappingList patternMappingList) {
        
        List<JobPatternInfo> jobInfos = new ArrayList<>();
        
        for (PatternMapping pattern : patternMappingList.getPatternMappings()) {
            
            asyncPatternProcessor.processPattern(jobId, collectionId, pattern, eventGraphs);
            JobPatternInfo jobInfo = new JobPatternInfo();
            jobInfo.setJobId(jobId);
            jobInfo.setTrack(quadrigaBaseUri + quadrigaJobStatusUri + jobId);
            jobInfo.setExplore(quadrigaBaseUri + quadrigaCollectionPageUri + collectionId);
            jobInfos.add(jobInfo);
        }
        
        return jobInfos;
    }
}
