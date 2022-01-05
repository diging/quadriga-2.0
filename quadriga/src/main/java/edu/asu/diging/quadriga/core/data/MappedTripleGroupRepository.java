package edu.asu.diging.quadriga.core.data;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.MappedTripleGroup;

/**
 * This is a repository that represents a MappedTripleGroup entry in the database
 * 
 * @author poojakulkarni
 *
 */
@Repository
public interface MappedTripleGroupRepository extends MongoRepository<MappedTripleGroup, ObjectId> {
    
    public Optional<List<MappedTripleGroup>> findByCollectionId(ObjectId collectionId);

}
