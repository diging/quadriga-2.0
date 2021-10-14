package edu.asu.diging.quadriga.core.data;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.asu.diging.quadriga.core.model.EventGraph;

public interface EventGraphRepository extends MongoRepository<EventGraph, ObjectId> {
    
    public Optional<Page<EventGraph>> findByCollectionIdOrderByCreationTimeDesc(ObjectId collectionId, Pageable pageable);
    public Optional<List<EventGraph>> findByCollectionIdOrderByCreationTimeDesc(ObjectId collectionId);

}
