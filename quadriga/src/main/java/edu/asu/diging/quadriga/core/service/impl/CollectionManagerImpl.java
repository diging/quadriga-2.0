package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.CollectionRepository;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;

@Service
public class CollectionManagerImpl implements CollectionManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CollectionRepository collectionRepo;

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#addCollection(java.lang.String, java.lang.String, java.util.List)
     */
    public Collection addCollection(String name, String description, List<String> apps) {
        Collection collection =new Collection();
        collection.setName(name);
        collection.setDescription(description);
        collection.setApps(apps);
        return collectionRepo.save(collection);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#findCollection(java.lang.String)
     */
    @Override
    public Collection findCollection(String id) throws InvalidObjectIdException {
        try {
            return collectionRepo.findById(new ObjectId(id)).orElse(null);
        } catch(IllegalArgumentException e) {
            logger.error("Couldn't retrieve a collection, invalid collectionId: " + id);
            throw new InvalidObjectIdException();
        }
    }


    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#editCollection(java.lang.String, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public Collection editCollection(String id, String name, String description, List<String> apps) throws CollectionNotFoundException, InvalidObjectIdException {

        Collection collection = findCollection(id);

        if (Objects.nonNull(collection)) {
            collection.setName(name);
            collection.setDescription(description);
            collection.setApps(apps);
            return collectionRepo.save(collection);
        } else {
            logger.error("Couldn't find collection for collectionId: " + id);
            throw new CollectionNotFoundException();
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#deleteCollection(java.lang.String)
     */
    public void deleteCollection(String id) throws CollectionNotFoundException, InvalidObjectIdException {
        Collection collection = findCollection(id);
        
        if(Objects.nonNull(collection)) {
            
            // Once networks are linked with collections, only empty collections will be deleted
            // If it is linked to a network, we will archive the collection.
            collectionRepo.delete(collection);
        } else {
            logger.error("Couldn't find collection for collectionId: " + id);
            throw new CollectionNotFoundException();
        }
    }

}
