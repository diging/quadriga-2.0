package edu.asu.diging.quadriga.core.mongo.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.mongo.EventGraphDao;

@Service
public class EventGraphDaoImpl implements EventGraphDao {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    
    /**
     * Groups the event graphs mapped to a collection by source uri and returns the total count. 
     * @param collectionId
     * @return total count
     */
    @Override
    public long countEventGraphsByCollectionId(Object collectionId) {
        MatchOperation match= match(Criteria.where("collectionId").is(collectionId));
        CountOperation count  = Aggregation.count().as("total");
        Aggregation agg = newAggregation(match, count);

        AggregationResults<Document> result = mongoTemplate.aggregate(
                agg, mongoTemplate.getCollectionName(EventGraph.class), Document.class);

        if (result.getMappedResults().isEmpty()) {
            return 0;
        }
        return result.getMappedResults().get(0).getInteger("total");
    }
}
