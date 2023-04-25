import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import edu.asu.diging.quadriga.api.v1.MapGraphToTripleController;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.model.PatternMappingList;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.JobManager;

public class MapGraphToTripleControllerTest {

    @Mock
    private AsyncPatternProcessor asyncPatternProcessor;

    @Mock
    private EventGraphService eventGraphService;

    @Mock
    private JobManager jobManager;

    @InjectMocks
    private MapGraphToTripleController mapGraphToTripleController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMapPatternToTriples() {
        String collectionId = "collection123";
        PatternMappingList patternMappingList = new PatternMappingList();
        List<PatternMapping> patternMappings = new ArrayList<>();
        patternMappingList.setPatternMappings(patternMappings);

        ObjectId objectId = new ObjectId();
        List<EventGraph> eventGraphs = new ArrayList<>();
        eventGraphs.add(mock(EventGraph.class));
        when(eventGraphService.getEventGraphs(objectId)).thenReturn(eventGraphs);

        Job job = new Job();
        job.setId("job123");
        when(jobManager.createJob(collectionId, "mappedTripleGroupId", eventGraphs.size())).thenReturn("job123");
        when(jobManager.get("job123")).thenReturn(job);

        ResponseEntity<List<MapGraphToTripleController.JobPatternInfo>> response = mapGraphToTripleController
                .mapPatternToTriples(collectionId, patternMappingList);

        assert (response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void testGetJobStatus() {
        Job job = new Job();
        job.setId("job123");
        when(jobManager.get("job123")).thenReturn(job);

        ResponseEntity<Job> response = mapGraphToTripleController.getJobStatus("job123");

        assert (response.getStatusCode() == HttpStatus.OK);
        assert (response.getBody().getId().equals("job123"));
    }

}
