package edu.asu.diging.quadriga.core.data;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.Collection;

@Repository
public interface CollectionRepository extends MongoRepository<Collection, ObjectId>{

    Collection findBy_id(ObjectId _id);

}