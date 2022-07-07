package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
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

    private Map<String, Job> taskTracker;

    @Autowired
    private PatternFinder patternFinder;

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @Autowired
    private PatternMapper patternMapper;

    public AsyncPatternProcessorImpl() {
        taskTracker = new ConcurrentHashMap<>();
    }

    @Override
    public List<String> processPatterns(String collectionId, List<GraphPattern> graphPattern,
            List<EventGraph> networks) {
        List<String> jobIds = new ArrayList<>();
        for (GraphPattern pattern : graphPattern) {
            String jobId = UUID.randomUUID().toString();

            processPattern(jobId, collectionId, pattern, networks);

            Job job = new Job();
            job.setId(jobId);
            taskTracker.put(jobId, job);
            jobIds.add(jobId);
        }
        return jobIds;
    }

    @Async
    private void processPattern(String jobId, String collectionId, GraphPattern graphPattern,
            List<EventGraph> networks) {

        CreationEventPattern patternRoot = patternMapper.mapPattern(graphPattern);
        for (EventGraph network : networks) {
            MappedTripleGroup mappedTripleGroup;
            try {
                if (graphPattern.getMappedTripleGroupId() != null && !graphPattern.getMappedTripleGroupId().isEmpty()) {
                    mappedTripleGroup = mappedTripleGroupService.getById(graphPattern.getMappedTripleGroupId());
                } else {
                    mappedTripleGroup = mappedTripleGroupService.get(collectionId, MappedTripleType.DEFAULT_MAPPING);
                }
            } catch (InvalidObjectIdException | CollectionNotFoundException e) {
                logger.error("No collection found with id {}", collectionId, e);
                return;
            }

            List<Graph> extractedGraphs = patternFinder.findGraphsWithPattern(graphPattern.getMetadata(), patternRoot,
                    network);
            for (Graph extractedGraph : extractedGraphs) {
                try {
                    mappedTripleService.storeMappedGraph(extractedGraph, mappedTripleGroup);
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public Job getJobInfo(String jobId) {
        return taskTracker.get(jobId);
    }

    @Override
    public void removeJob(String jobId) {
        Job job = taskTracker.get(jobId);
        if (job.getStatus() != JobStatus.PROCESSING) {
            taskTracker.remove(jobId);
        }
    }

}
