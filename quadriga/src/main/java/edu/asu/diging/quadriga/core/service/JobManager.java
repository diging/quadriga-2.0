package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.model.jobs.Job;

public interface JobManager {

    /**
     * Creates a new job entry for pattern matching and triple extraction
     * @param collectionId networks mapped for the given collection need to be processed for the created job
     * @param mappedTripleGroupId extracted triples will be saved for the given triple group id
     * @param totalNetworks count of the total networks to be processed
     * @return Id for the created job
     */
    public String createJob(String collectionId, String mappedTripleGroupId, int totalNetworks);
    
    /**
     * Retrieves job details for the given job id
     * @param jobId id of the job to be retrieved
     * @return the job details
     */
    public Job get(String jobId);
    
}
