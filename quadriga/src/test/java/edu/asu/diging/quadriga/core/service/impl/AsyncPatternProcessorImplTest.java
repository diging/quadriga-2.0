package edu.asu.diging.quadriga.core.service.impl;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.Metadata;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.core.data.JobRepository;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.JobNotFoundException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.model.jobs.JobStatus;
import edu.asu.diging.quadriga.core.pattern.PatternCreationEvent;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.PatternFinder;
import edu.asu.diging.quadriga.core.service.PatternMapper;

@RunWith(MockitoJUnitRunner.class)
public class AsyncPatternProcessorImplTest {

    @InjectMocks
    private AsyncPatternProcessorImpl asyncPatternProcessor;

    @Mock
    private PatternFinder patternFinder;

    @Mock
    private MappedTripleService mappedTripleService;

    @Mock
    private MappedTripleGroupService mappedTripleGroupService;

    @Mock
    private PatternMapper patternMapper;

    @Mock
    private JobRepository jobRepository;

    private List<EventGraph> networks;
    private PatternMapping patternMapping;
    private Job job;
    private MappedTripleGroup mappedTripleGroup;
    private List<Graph> listOfGraphs = new ArrayList<>() ; 
    ObjectId mappedTripleGroupId;
       
    @Before
    public void setUp() {
        networks = new ArrayList<>();
        patternMapping = new PatternMapping();
        job = new Job();
        job.setId("job123");
        mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroupId = new ObjectId();
        mappedTripleGroup.set_id(mappedTripleGroupId);
        
    }

    @Test
    public void testProcessPatternSuccess() throws InvalidObjectIdException, CollectionNotFoundException, JobNotFoundException {
    
        Mockito.when(jobRepository.findById("job123")).thenReturn(Optional.of(job));
        Mockito.when(patternMapper.mapPattern(patternMapping)).thenReturn(new PatternCreationEvent());

        asyncPatternProcessor.processPattern("job123", "collectionId", patternMapping, networks);
    }
    
}
