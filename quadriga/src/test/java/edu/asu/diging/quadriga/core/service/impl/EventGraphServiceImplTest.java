package edu.asu.diging.quadriga.core.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import edu.asu.diging.quadriga.core.data.EventGraphRepository;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.mongo.EventGraphDao;
import edu.asu.diging.quadriga.core.mongo.impl.EventGraphDaoImpl;



public class EventGraphServiceImplTest {

    @Mock
    private EventGraphRepository eventGraphRepository;

    @InjectMocks
    private EventGraphServiceImpl eventGraphServiceImpl;
    
    @Mock
    private MongoTemplate mongoTemplate;
    
    @InjectMocks
    private EventGraphDaoImpl eventGraphDaoImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_findLatestEventGraphByCollectionId_success() throws InterruptedException {
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

        Mockito.when(eventGraphRepository.findFirstByCollectionIdOrderByCreationTimeDesc(collectionObjectId))
                .thenReturn(Optional.of(eventGraphs));
        
        List<EventGraph> foundEventGraphs = eventGraphServiceImpl.findLatestEventGraphByCollectionId(collectionObjectId);
        
        // The latest, i.e. EventGraph2, will be the 1st one on the list
        Assert.assertEquals(eventGraphObjectId2, foundEventGraphs.get(0).getId());
    }
    
    
    @Test
    public void test_countEventGraphsBy_success() throws InterruptedException {
        ObjectId collectionObjectId = new ObjectId();

        EventGraph eventGraph1 = new EventGraph();
        ObjectId eventGraphObjectId1 = new ObjectId();
 

        eventGraph1.setId(eventGraphObjectId1);
        eventGraph1.setCollectionId(collectionObjectId);

        EventGraph eventGraph2 = new EventGraph();
        ObjectId eventGraphObjectId2 = new ObjectId();

        eventGraph2.setId(eventGraphObjectId2);
        eventGraph2.setCollectionId(collectionObjectId);

        List<EventGraph> eventGraphs = new ArrayList<EventGraph>();
        eventGraphs.add(eventGraph2);
        eventGraphs.add(eventGraph1);

        Document doc1= new Document();
        doc1.put("total", 2);
        List<Document> mappedResult= new ArrayList<Document>();
        mappedResult.add(doc1);

        AggregationResults<Document> aggregationResultsMock = mock(AggregationResults.class);
                  
        Mockito.when(aggregationResultsMock.getMappedResults()).thenReturn(mappedResult);
        Mockito.when(mongoTemplate.getCollectionName(eq(EventGraph.class))).thenReturn("EVENT_GRAPH");
        Mockito.when(mongoTemplate.aggregate(any(Aggregation.class), any(String.class), eq(Document.class)))
            .thenReturn(aggregationResultsMock);
        
        long totalCount = eventGraphDaoImpl.countEventGraphsByCollectionId(collectionObjectId);

        //Both the event graphs belong to the group since the sourceUri is same. 
        Assert.assertEquals(totalCount, 2);
    }

}
