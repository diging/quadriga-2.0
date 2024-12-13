package edu.asu.diging.quadriga.api.v1;

import java.util.ArrayList;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.service.GraphTripleMapper;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.JobManager;
import org.junit.Assert;
import org.junit.Before;

public class GraphTripleMapperControllerTest {

    @Mock
    private AsyncPatternProcessor asyncPatternProcessor;

    @Mock
    private EventGraphService eventGraphService;

    @Mock
    private JobManager jobManager;
    
    @Mock
    private CollectionManager collectionManager;
    
    @Mock 
    private GraphTripleMapper graphTripleMapper;

    @InjectMocks
    private GraphTripleMapperController graphTripleMapperController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);        
    }


    @Test
    public void test_mapPatternToTriples_Success() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId collectionId = new ObjectId();
        Collection collection = new Collection();
        List<PatternMapping> patternMappings = new ArrayList<>();
        
        ObjectId objectId = new ObjectId();
        List<EventGraph> eventGraphs = new ArrayList<>();       
        Job job = new Job();
        job.setId("job123");
        List<JobPatternInfo> jobInfos = new ArrayList<>();
        JobPatternInfo jobInfo = new JobPatternInfo();
        jobInfo.setJobId("job123");
        jobInfos.add(jobInfo);
        
        Mockito.when(eventGraphService.getEventGraphs(objectId)).thenReturn(eventGraphs);
        Mockito.when(jobManager.createJob(collectionId.toString(), "mappedTripleGroupId", eventGraphs.size())).thenReturn("job123");
        Mockito.when(jobManager.get("job123")).thenReturn(job);
        Mockito.when(collectionManager.findCollection(collectionId.toString())).thenReturn(collection);
        Mockito.when(graphTripleMapper.mapPatterns(collectionId.toString(), eventGraphs, patternMappings)).thenReturn(jobInfos);

        ResponseEntity<List<JobPatternInfo>> response = graphTripleMapperController.mapPatternToTriples(collectionId.toString(), patternMappings);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    public void test_mapPatternTriples_NotFound() throws InvalidObjectIdException, CollectionNotFoundException
    {
        ObjectId collectionId = new ObjectId();
        List<PatternMapping> patternMappings = new ArrayList<>();
        
        Mockito.when(eventGraphService.getEventGraphs(collectionId)).thenReturn(null);
        ResponseEntity<List<JobPatternInfo>> response = graphTripleMapperController.mapPatternToTriples(collectionId.toString(),patternMappings);
        Assert.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        
    }
    
}
