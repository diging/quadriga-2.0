package edu.asu.diging.quadriga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.mongo.CollectionRepository;
import edu.asu.diging.quadriga.domain.elements.Collection;
import edu.asu.diging.quadriga.service.ICollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

@Service
public class CollectionManager implements ICollectionManager {

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
    public Collection addCollection(Collection collection) {
        return collectionRepo.save(collection);
    }

}
