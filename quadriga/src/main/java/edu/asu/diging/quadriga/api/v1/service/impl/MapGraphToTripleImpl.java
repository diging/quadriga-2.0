package edu.asu.diging.quadriga.api.v1.service.impl;

import java.util.ArrayList;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.service.MapGraphToTriple;
import edu.asu.diging.quadriga.core.exceptions.JobNotFoundException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;

@Service
public class MapGraphToTripleImpl implements MapGraphToTriple{
    
//    private final String quadrigaBaseUri = "http://localhost:8081/quadriga/" ;
//    private final String quadrigaJobStatusUri = "api/v1/job/";
//    private final String quadrigaCollectionPageUri = "auth/collections/";
    
    @Value("${quadriga.base-uri}")
    private String quadrigaBaseUri;

    @Value("${quadriga.job-status-uri}")
    private String quadrigaJobStatusUri;

    @Value("${quadriga.collection-page-uri}")
    private String quadrigaCollectionPageUri;

    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private AsyncPatternProcessor asyncPatternProcessor;

    @Override
    public List<JobPatternInfo> mapPatterns(String collectionId, String jobId,List<EventGraph> eventGraphs, List<PatternMapping> patternMappingList) {
        
        List<JobPatternInfo> jobInfos = new ArrayList<>();
        
        for (PatternMapping pattern : patternMappingList) {
            
            try {
                asyncPatternProcessor.processPattern(jobId, collectionId, pattern, eventGraphs);
            } catch (JobNotFoundException e) {
                logger.error("Job not found for Job id "+jobId+" and collection Id "+collectionId,e);
                return jobInfos;
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
