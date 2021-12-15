package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import edu.asu.diging.quadriga.core.data.EventGraphRepository;
import edu.asu.diging.quadriga.core.model.EventGraph;

public class EventGraphServiceImplTest {

    @Mock
    private EventGraphRepository eventGraphRepository;

    @InjectMocks
    private EventGraphServiceImpl eventGraphServiceImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_findAllEventGraphsByCollectionId_success() throws InterruptedException {
        ObjectId collectionObjectId = new ObjectId();

        EventGraph eventGraph1 = new EventGraph();
        ObjectId eventGraphObjectId1 = new ObjectId();

        eventGraph1.setId(eventGraphObjectId1);
        eventGraph1.setCollectionId(collectionObjectId);

        EventGraph eventGraph2 = new EventGraph();
        ObjectId eventGraphObjectId2 = new ObjectId();

        eventGraph2.setId(eventGraphObjectId2);
        eventGraph2.setCollectionId(collectionObjectId);

        // As per this list, eventGraph2 was created after eventGraph1
        List<EventGraph> eventGraphs = new ArrayList<EventGraph>();
        eventGraphs.add(eventGraph2);
        eventGraphs.add(eventGraph1);

        Mockito.when(eventGraphRepository.findByCollectionId(collectionObjectId))
                .thenReturn(Optional.of(new ArrayList<EventGraph>(eventGraphs)));

        List<EventGraph> foundEventGraphs = new ArrayList<>();
        eventGraphServiceImpl.findAllEventGraphsByCollectionId(collectionObjectId)
                            .forEach(eventGraph -> {
                                foundEventGraphs.add(eventGraph);
                            });
        
        // The latest, i.e. EventGraph2, will be the 1st one on the list
        Assert.assertEquals(eventGraphObjectId2, foundEventGraphs.get(0).getId());
        Assert.assertEquals(eventGraphObjectId1, foundEventGraphs.get(1).getId());
    }
    
    @Test
    public void test_findLatestEventGraphByCollectionId_sucess() {
        ObjectId collectionObjectId = new ObjectId();

        EventGraph eventGraph = new EventGraph();
        ObjectId eventGraphObjectId1 = new ObjectId();

        eventGraph.setId(eventGraphObjectId1);
        eventGraph.setCollectionId(collectionObjectId);

        Mockito.when(eventGraphRepository.findByCollectionIdOrderByCreationTimeDesc(collectionObjectId))
                .thenReturn(Optional.of(Collections.singletonList(eventGraph)));

        EventGraph latestEventGraph = eventGraphServiceImpl.findLatestEventGraphByCollectionId(collectionObjectId);

        Assert.assertEquals(eventGraphObjectId1, latestEventGraph.getId());
    }
    
    @Test
    public void test_findLatestEventGraphByCollectionId_failure() {
        ObjectId collectionObjectId = new ObjectId();

        Mockito.when(eventGraphRepository.findByCollectionIdOrderByCreationTimeDesc(collectionObjectId))
                .thenReturn(Optional.ofNullable(null));

        Assert.assertNull(eventGraphServiceImpl.findLatestEventGraphByCollectionId(collectionObjectId));
    }
    
    @Test
    public void test_findEventGraphsBySourceURI_success() {
        ObjectId collectionObjectId = new ObjectId();

        EventGraph eventGraph = new EventGraph();
        ObjectId eventGraphObjectId1 = new ObjectId();

        eventGraph.setId(eventGraphObjectId1);
        eventGraph.setCollectionId(collectionObjectId);
        
        String sourceURI = "http://handle.net/234";
        
        List<EventGraph> eventGraphs = new ArrayList<>();
        eventGraphs.add(eventGraph);
        
        Mockito.when(eventGraphRepository.findByContextSourceUri(sourceURI)).thenReturn(Optional.of(eventGraphs));
        
        List<EventGraph> foundEventGraphs = eventGraphServiceImpl.findEventGraphsBySourceURI(sourceURI);
        Assert.assertEquals(eventGraphs.get(0).getId(), foundEventGraphs.get(0).getId());
    }
    

}
