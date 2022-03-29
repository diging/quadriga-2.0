package edu.asu.diging.quadriga.core.data;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import edu.asu.diging.quadriga.core.model.EventGraph;

public interface EventGraphRepository extends MongoRepository<EventGraph, ObjectId> {
    
    public Optional<List<EventGraph>> findByCollectionId(ObjectId collectionId);
    public Optional<List<EventGraph>> findByCollectionIdOrderByCreationTimeDesc(ObjectId collectionId);
    
    @Query(value = "{'context.sourceUri': ?0}")
    public Optional<List<EventGraph>> findByContextSourceUri(String sourceURI);

    public Optional<List<EventGraph>> findFirstByCollectionIdOrderByCreationTimeDesc(ObjectId collectionId);

}
