package edu.asu.diging.quadriga.api.v1.service.impl;

import java.util.ArrayList;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.service.MapGraphToTriple;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.JobNotFoundException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;
import edu.asu.diging.quadriga.core.service.JobManager;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;

@Service
@PropertySource("classpath:config.properties")
public class MapGraphToTripleImpl implements MapGraphToTriple{

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${quadriga.base_url}")
    private String quadrigaBaseUri;

    @Value("${quadriga.job_status_uri}")
    private String quadrigaJobStatusUri;

    @Value("${quadriga.collection_page_uri}")
    private String quadrigaCollectionPageUri;
   
    @Autowired
    private AsyncPatternProcessor asyncPatternProcessor;
    
    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @Autowired
    private JobManager jobManager;

    @Override
    public List<JobPatternInfo> mapPatterns(String collectionId, List<EventGraph> eventGraphs, List<PatternMapping> patternMappingList) throws InvalidObjectIdException, CollectionNotFoundException {
        
        MappedTripleGroup mappedTripleGroup = mappedTripleGroupService.add(collectionId, null);        
        String jobId = jobManager.createJob(collectionId, mappedTripleGroup.get_id().toString(), eventGraphs.size());

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
            jobInfo.setTrack(quadrigaBaseUri + quadrigaJobStatusUri + "/" + jobId + "/status");
            jobInfo.setExplore(quadrigaBaseUri + quadrigaCollectionPageUri + collectionId);
            jobInfos.add(jobInfo);
        }
        
        return jobInfos;
    }
}
