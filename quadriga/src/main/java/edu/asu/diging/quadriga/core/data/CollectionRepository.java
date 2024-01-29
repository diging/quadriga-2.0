package edu.asu.diging.quadriga.core.data;

import java.util.List;


import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.Collection;

@Repository
public interface CollectionRepository extends MongoRepository<Collection, ObjectId>{
    
    @Query("{$or: [{'owner': ?0}, {'apps': {$in: ?1}}]}")
    Page<Collection> findByOwnerOrAppsIn(String owner, List<String> apps, Pageable pageable);

    Page<Collection> findByArchived(boolean archived, Pageable paging);

    List<Collection> findByAppsContaining(String app);
    
}