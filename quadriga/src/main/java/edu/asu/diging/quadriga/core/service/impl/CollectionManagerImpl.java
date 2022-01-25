package edu.asu.diging.quadriga.core.service.impl;

import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.CollectionRepository;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;

@Service
public class CollectionManagerImpl implements CollectionManager {

    @Autowired
    private CollectionRepository collectionRepo;

    /**
     * Creates a new Collection instance and stores it in the db.
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
     * Finds a collection from the collection table by id.
     * 
     * @param id used to look up the collection in mongodb
     * 
     * 
     * @return Collection Instance that is found from the database
     * @throws InvalidObjectIdException if collectionId couldn't be converted to ObjectId
     * 
     **/
    @Override
    public Collection findCollection(String id) throws InvalidObjectIdException {
        try {
            return collectionRepo.findById(new ObjectId(id)).orElse(null);
        } catch(IllegalArgumentException e) {
            throw new InvalidObjectIdException(e);
        }
    }

    /**
     * 
     * Edits an existing Collection and updates it in the db.
     * 
     * @param id of the collection that needs to be updated
     * @param name will be the updated name value
     * @param description will be the updated description value
     * @return Collection Instance that is updated in database
     * @throws CollectionNotFoundException in case the collection for the given id is missing
     * @throws InvalidObjectIdException if collectionId couldn't be converted to ObjectId
     */
    @Override
    public Collection editCollection(String id, String name, String description) throws CollectionNotFoundException, InvalidObjectIdException {
        Collection collection = findCollection(id);

        if (Objects.nonNull(collection)) {
            collection.setName(name);
            collection.setDescription(description);
            return collectionRepo.save(collection);
        } else {
            throw new CollectionNotFoundException("CollectionId: " + id);
        }
    }
    
    /**
     * Deletes a collection from collection table by id.
     * 
     * @param id used to look up the collection in mongodb
     * @throws InvalidObjectIdException if collectionId couldn't be converted to ObjectId
     * 
     */
    @Override
    public void deleteCollection(String id) throws CollectionNotFoundException, InvalidObjectIdException {
        Collection collection = findCollection(id);
        
        if(Objects.nonNull(collection)) {
            
            // Once networks are linked with collections, only empty collections will be deleted
            // If it is linked to a network, we will archive the collection.
            collectionRepo.delete(collection);
        } else {
            throw new CollectionNotFoundException("CollectionId: " + id);
        }
    }

}
