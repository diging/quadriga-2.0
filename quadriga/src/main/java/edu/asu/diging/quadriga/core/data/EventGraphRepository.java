package edu.asu.diging.quadriga.core.data;



import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.asu.diging.quadriga.core.model.EventGraph;

public interface EventGraphRepository extends MongoRepository<EventGraph, ObjectId> {

    public Optional<EventGraph> findFirstByCollectionIdOrderByCreationTimeDesc(ObjectId collectionId);
    
    public List<EventGraph> findFirstByCollectionIdOrderByCreationTimeDesc(ObjectId collectionId, Pageable pageable);
    
    public Optional<List<EventGraph>> findByCollectionId(ObjectId collectionId);
    
}
