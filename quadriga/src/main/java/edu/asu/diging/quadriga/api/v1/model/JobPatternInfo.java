package edu.asu.diging.quadriga.api.v1.model;

/*
 * @JobPatternInfo The object of this class is returned when @MapGraphToTripleController is called to map patterns in a collection.
 * @param jobId is the jobId of the job created for processing the pattern.
 * @param track is the URI of the status of the job.
 * @param explore is the URI of the collection whose graphs are being mapped with the input patterns.
 */

public class JobPatternInfo {
    
    
    private String jobId;
    private String track;
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
