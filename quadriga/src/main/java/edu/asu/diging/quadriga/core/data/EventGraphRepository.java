package edu.asu.diging.quadriga.core.data;

import java.util.List;

//imports as static
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.EventGraph;

public interface EventGraphRepository extends MongoRepository<EventGraph, ObjectId> {

    
    public Optional<List<EventGraph>> findByCollectionIdOrderByCreationTimeDesc(ObjectId collectionId);

//    @Aggregation(pipeline= {"{'$match' :{'collectionId' : #{#collectionId}},'$unwind' : '$context' , '$group' : {_id: '$context.sourceUri'}} "})
//    public  Optional<List<EventGraph>> groupEventGraphsBySourceUri(ObjectId collectionId);
    
//    @Aggregation(pipeline= {"{'$match' :{'collectionId' : #{#collectionId}},'$unwind' : '$context' , '$group' : {_id: '$context.sourceUri'}} "})
//    public default  Optional<List<EventGraph>> groupEventGraphsBySourceUri(ObjectId collectionId){
//		
//    	Aggregation agg = newAggregation(
//    			match(Criteria.where("collectionId").is(collectionId)),
//    			unwind("context"),
//    			group("context.sourceUri"));
//    	
//    	AggregationResults<EventGraph> result = mongoTemplate.aggregate(
//    			agg, "zips", EventGraph.class);
//    	return null;
//    	
//    }

}
