package edu.asu.diging.quadriga.api.v1;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.service.JobManager;

public class JobStatusTest {

    
    @Mock
    private JobManager jobManager;
    
    @Mock
    private JobStatusController jobStatusController;
    
    @Test
    public void testGetJobStatus() {
        Job job = new Job();
        job.setId("job123");
        when(jobManager.get("job123")).thenReturn(job);

        ResponseEntity<Job> response = jobStatusController.getJobStatus("job123");

        assert (response.getStatusCode() == HttpStatus.OK);
        assert (response.getBody().getId().equals("job123"));
    }
}
