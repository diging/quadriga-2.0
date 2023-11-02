package edu.asu.diging.quadriga.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.service.JobManager;

public class JobStatusController {
    
    @Autowired
    private JobManager jobManager;

    
    @GetMapping(value = "/api/v1/job/status/{jobId}")
    public ResponseEntity<Job> getJobStatus(@PathVariable String jobId) {
        Job job = jobManager.get(jobId);
        if(job == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(job, HttpStatus.OK);
    }       
}
