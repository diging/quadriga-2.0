package edu.asu.diging.quadriga.api.v1;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.service.JobManager;
import org.junit.Assert;

public class JobStatusTest {

    
    @Mock
    private JobManager jobManager;
    
    @InjectMocks
    private JobStatusController jobStatusController;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);        
    }
    
    @Test
    public void test_GetJobStatus_Success() {
        Job job = new Job();
        job.setId("job123");
        Mockito.when(jobManager.get("job123")).thenReturn(job);
        ResponseEntity<Job> response = jobStatusController.getJobStatus("job123");
        Assert.assertEquals(response.getStatusCode(),HttpStatus.OK);
    }
    @Test 
    public void test_GetJobStatus_Fail()
    {
        Job job = new Job();
        job.setId("job123");
        Mockito.when(jobManager.get("job123")).thenReturn(null);
        ResponseEntity<Job> response = jobStatusController.getJobStatus("job123");
        Assert.assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        
    }
}
