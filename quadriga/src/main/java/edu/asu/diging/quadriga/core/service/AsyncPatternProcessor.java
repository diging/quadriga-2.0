package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.jobs.Job;

public interface AsyncPatternProcessor {

    public List<String> processPatterns(String collectionId, List<GraphPattern> graphPattern,
            List<EventGraph> networks);

    public Job getJobInfo(String jobId);
    
    public void removeJob(String jobId);

}
