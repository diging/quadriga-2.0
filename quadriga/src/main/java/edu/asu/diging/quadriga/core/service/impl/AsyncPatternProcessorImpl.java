package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;


import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.core.data.JobRepository;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.model.jobs.JobStatus;
import edu.asu.diging.quadriga.core.pattern.PatternCreationEvent;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.PatternFinder;
import edu.asu.diging.quadriga.core.service.PatternMapper;
import edu.asu.diging.quadriga.core.exceptions.JobNotFoundException;

@Component
public class AsyncPatternProcessorImpl implements AsyncPatternProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PatternFinder patternFinder;

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @Autowired
    private PatternMapper patternMapper;

    @Autowired
    private JobRepository jobRepository;

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.AsyncPatternProcessor#processPattern(java.lang.String, java.lang.String, edu.asu.diging.quadriga.api.v1.model.PatternMapping, java.util.List)
     */
    @Override
    @Async
    public void processPattern(String jobId, String collectionId, PatternMapping patternMapping,
            List<EventGraph> networks) throws JobNotFoundException {

        Job job = jobRepository.findById(jobId).orElse(null);
        PatternCreationEvent patternRoot = patternMapper.mapPattern(patternMapping);
        
        if (job == null) {
            throw new JobNotFoundException();  
        } 
        
        job.setStatus(JobStatus.PROCESSING);
        jobRepository.save(job);
        
        MappedTripleGroup mappedTripleGroup;
        try {
            if (patternMapping.getMappedTripleGroupId() != null && !patternMapping.getMappedTripleGroupId().isEmpty()) {
                mappedTripleGroup = mappedTripleGroupService.findByCollectionIdAndId(collectionId,
                        patternMapping.getMappedTripleGroupId());
                if (mappedTripleGroup == null) {
                    mappedTripleGroup = new MappedTripleGroup();
                    mappedTripleGroup.set_id(new ObjectId(patternMapping.getMappedTripleGroupId()));
                    jobRepository.save(job);
                }
            } else {
                mappedTripleGroup = mappedTripleGroupService.getMappedTripleGroupIfExistsOrAdd(collectionId, MappedTripleType.CUSTOM_MAPPING);
            }
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("Invalid triple group id {} or collection id {} for job {}",
                    patternMapping.getMappedTripleGroupId(), collectionId, job.getId(), e);

            job.setStatus(JobStatus.FAILURE);
            jobRepository.save(job);
            return;
        }

        for (EventGraph network : networks) {
            List<Graph> extractedGraphs = patternFinder.findAndMapGraphsWithPattern(patternMapping.getMetadata(), patternRoot,
                    network);
            for (Graph extractedGraph : extractedGraphs) {
                try {
                    mappedTripleService.storeMappedGraph(extractedGraph, mappedTripleGroup);
                } catch (NodeNotFoundException e) {
                    logger.error("Error mapping a triple for job {}", job.getId(), e);
                }
            }
            job.setProcessedNetworks(job.getProcessedNetworks() + 1);
            jobRepository.save(job);
        }
        job.setStatus(JobStatus.DONE);
        jobRepository.save(job);
    }
}
