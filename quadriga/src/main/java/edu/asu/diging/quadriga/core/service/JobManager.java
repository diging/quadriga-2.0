package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.model.jobs.Job;

public interface JobManager {

    public String createJob(String collectionId, String mappedTripleGroupId, int totalNetworks);
    
    public Job get(String jobId);
    
}
