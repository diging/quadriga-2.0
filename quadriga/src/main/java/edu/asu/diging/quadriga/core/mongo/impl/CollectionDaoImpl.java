package edu.asu.diging.quadriga.core.mongo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.mongo.CollectionDao;
import edu.asu.diging.quadriga.domain.elements.Collection;

@Service
public class CollectionDaoImpl implements CollectionDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveCollection(Collection collection) {
        mongoTemplate.insert(collection);
    }

}
