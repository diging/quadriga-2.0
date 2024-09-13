package edu.asu.diging.quadriga.api.v1.model;

public class JobPatternInfo {
    
    /*
     * jobId is the jobId of the job created for processing the pattern.
     */
    private String jobId;
    
    /*
     * track is the URI of the status of the job.
     */
    private String track;
    
    /*
     * explore is the URI of the collection whose graphs are being mapped with the input patterns.
     */
    private String explore;
    
    public String getJobId() {
        return jobId;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public String getTrack() {
        return track;
    }
    public void setTrack(String track) {
        this.track = track;
    }
    public String getExplore() {
        return explore;
    }
    public void setExplore(String explore) {
        this.explore = explore;
    }
}