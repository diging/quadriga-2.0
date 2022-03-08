package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.EventGraphRepository;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
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
    MongoTemplate mongoTemplate;
    
    

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
	public long groupEventGraphsBySourceUri(ObjectId collectionId) {
		UnwindOperation unwind = unwind("context");
		GroupOperation group = Aggregation.group("context.sourceUri");
		MatchOperation match= match(Criteria.where("collectionId").is(collectionId));
		CountOperation count  = Aggregation.count().as("total");
    	Aggregation agg = newAggregation(unwind, match, group, count);
    	AggregationResults<Document> result = mongoTemplate.aggregate(
    			agg, mongoTemplate.getCollectionName(EventGraph.class), Document.class);
    	
    	if (result.getMappedResults().isEmpty()) {
            return 0;
        }
    	return result.getMappedResults().get(0).getInteger("total");
		
	}
    
}
