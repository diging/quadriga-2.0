package edu.asu.diging.quadriga.core.data;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.asu.diging.quadriga.core.model.EventGraph;

public interface EventGraphRepository extends MongoRepository<EventGraph, ObjectId> {

}
