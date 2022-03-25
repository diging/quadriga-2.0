package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.EventGraphRepository;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.mongo.EventGraphDao;
import edu.asu.diging.quadriga.core.mongo.impl.EventGraphDaoImpl;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
@Service
public class EventGraphServiceImpl implements EventGraphService {

    @Autowired
    private EventGraphRepository repo;
    
    
    @Autowired
    private EventGraphDao eventGraphDao;

    
    @Override
    public void saveEventGraphs(List<EventGraph> events) {
        for (EventGraph event : events) {
            repo.save(event);
        }
    }

    @Override
    public List<EventGraph> findAllEventGraphsByCollectionId(ObjectId collectionId) {
        return repo.findByCollectionIdOrderByCreationTimeDesc(collectionId).orElse(null);
    }

    
    @Override
    public long getNumberOfSubmittedNetworks(ObjectId collectionId) {
                
        return eventGraphDao.countEventGraphsByCollectionId(collectionId);
        
    }
    
}
