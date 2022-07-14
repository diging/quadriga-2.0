package edu.asu.diging.quadriga.api.v1;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.model.PatternMappingList;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.JobManager;

@Controller
public class MapGraphToTripleController {

    @Value("")
    private String quadrigaBaseUri;
    
    @Autowired
    private AsyncPatternProcessor asyncPatternProcessor;

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private JobManager jobManager;

    @PostMapping(value = "/api/v1/collection/{collectionId}/network/map")
    public ResponseEntity<List<JobPatternInfo>> mapPatternToTriples(@PathVariable String collectionId,
            @RequestBody PatternMappingList patternMappingList) {

        List<EventGraph> eventGraphs = eventGraphService.getEventGraphs(new ObjectId(collectionId));
        if (eventGraphs == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<JobPatternInfo> jobInfos = new ArrayList<>();
        for (PatternMapping pattern : patternMappingList.getPatternMappings()) {
            String jobId = jobManager.createJob(collectionId, pattern.getMappedTripleGroupId(), eventGraphs.size());
            asyncPatternProcessor.processPattern(jobId, collectionId, pattern, eventGraphs);
            JobPatternInfo jobInfo = new JobPatternInfo();
            jobInfo.setJobId(jobId);
            jobInfo.setTrack(quadrigaBaseUri + jobId);
            jobInfos.add(jobInfo);
        }

        return new ResponseEntity<>(jobInfos, HttpStatus.OK);
    }

    @GetMapping(value = "/api/v1/job/{jobId}/status")
    public ResponseEntity<Job> getJobStatus(@PathVariable String jobId) {
        return new ResponseEntity<>(jobManager.get(jobId), HttpStatus.OK);
    }
    
    class JobPatternInfo {
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

}
