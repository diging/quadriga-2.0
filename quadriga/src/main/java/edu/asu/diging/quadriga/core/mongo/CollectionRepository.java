package edu.asu.diging.quadriga.core.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.domain.elements.Collection;

@Repository
public interface CollectionRepository extends MongoRepository<Collection, String>{

}
