package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.core.data.JobRepository;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.events.pattern.CreationEventPattern;
import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.model.jobs.JobStatus;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.PatternFinder;
import edu.asu.diging.quadriga.core.service.PatternMapper;

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

    @Async
    @Override
    public void processPattern(String jobId, String collectionId, GraphPattern graphPattern,
            List<EventGraph> networks) {

        Job job = jobRepository.findById(jobId).orElse(null);
        job.setStatus(JobStatus.PROCESSING);
        jobRepository.save(job);

        CreationEventPattern patternRoot = patternMapper.mapPattern(graphPattern);
        if (patternRoot == null) {
            job.setStatus(JobStatus.FAILURE);
            jobRepository.save(job);
            return;
        }
        
        MappedTripleGroup mappedTripleGroup;
        try {
            if (graphPattern.getMappedTripleGroupId() != null && !graphPattern.getMappedTripleGroupId().isEmpty()) {
                mappedTripleGroup = mappedTripleGroupService.getByCollectionIdAndId(collectionId,
                        graphPattern.getMappedTripleGroupId());
                if (mappedTripleGroup == null) {
                    job.setStatus(JobStatus.FAILURE);
                    jobRepository.save(job);
                    return;
                }
            } else {
                mappedTripleGroup = mappedTripleGroupService.get(collectionId, MappedTripleType.DEFAULT_MAPPING);
            }
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("Invalid triple group id {} or collection id {} for job {}",
                    graphPattern.getMappedTripleGroupId(), collectionId, job.getId(), e);
            job.setStatus(JobStatus.FAILURE);
            jobRepository.save(job);
            return;
        }

        for (EventGraph network : networks) {
            List<Graph> extractedGraphs = patternFinder.findGraphsWithPattern(graphPattern.getMetadata(), patternRoot,
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
