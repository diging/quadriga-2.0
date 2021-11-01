package edu.asu.diging.quadriga.core.data;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.MappedCollection;

/**
 * This is a repository that represents a MappedCollection entry in the database
 * 
 * @author poojakulkarni
 *
 */
@Repository
public interface MappedCollectionRepository extends MongoRepository<MappedCollection, ObjectId> {
    
    public Optional<MappedCollection> findByCollectionId(ObjectId collectionId);

}
