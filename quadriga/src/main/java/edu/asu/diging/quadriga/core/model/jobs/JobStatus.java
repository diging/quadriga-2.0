package edu.asu.diging.quadriga.core.model.jobs;

/**
 * Enumeration representing the status of a job.
 */
public enum JobStatus {
    /*
     * Job status indicating that the job has been started.
     */
    STARTED,
    /*
     * Job status indicating that the job is currently being processed.
     */
    PROCESSING,
    /*
     * Job status indicating that the job has been completed successfully.
     */
    DONE,
    /*
     * Job status indicating that the job has failed to complete.
     */
    FAILURE;
}

