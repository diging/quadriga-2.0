package edu.asu.diging.quadriga.core.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.CollectionRepository;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;

@Service
public class CollectionManagerImpl implements CollectionManager {

    @Autowired
    private CollectionRepository collectionRepo;

    
    /**
     * Creates a new Collection instance and stores it in mongodb
     * 
     * @param collection   collection data from the Collection form needs to be added to database
     * 
     * 
     * @return Collection Instance that is saved in database
     * 
     **/
    public Collection addCollection(String name, String description) {
        Collection collection=new Collection();
        collection.setName(name);
        collection.setDescription(description);
        return collectionRepo.save(collection);
    }
    
    
    /**
     * Finds a collection from mongodb collection table by _id
     * 
     * @param id used to look up the collection in mongodb
     * 
     * 
     * @return Collection Instance that is found from the database
     * 
     **/
    @Override
    public Collection findCollection(String id) {
        return collectionRepo.findById(new ObjectId(id)).orElse(null);
    }
    
    
    /**
     * Deletes a collection from mongodb collection table by _id
     * 
     * @param id used to look up the collection in mongodb
     * 
     */
    public boolean deleteCollection(String id) {
        Collection collection = findCollection(id);
        
        if(Objects.nonNull(collection)) {
            collectionRepo.delete(collection);
            return true;
        }
        
        return false;
    }

}
