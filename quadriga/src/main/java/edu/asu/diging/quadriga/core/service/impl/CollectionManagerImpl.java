package edu.asu.diging.quadriga.core.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.data.CollectionRepository;
import edu.asu.diging.quadriga.core.exceptions.CitesphereAppNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.service.CollectionManager;

@Service
public class CollectionManagerImpl implements CollectionManager {

    @Autowired
    private CitesphereConnector citesphereConnector;
    
    @Autowired
    private CollectionRepository collectionRepo;

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#addCollection(java.lang.String, java.lang.String, java.util.List)
     */
    public Collection addCollection(String name, String description, List<String> apps) throws CitesphereAppNotFoundException {   
        validateApps(apps);      
        Collection collection = new Collection();
        collection.setName(name);
        collection.setDescription(description);
        collection.setApps(apps);
        return collectionRepo.save(collection);
    }

    @Override
    public Collection findCollection(String id) throws InvalidObjectIdException {
        try {
            return collectionRepo.findById(new ObjectId(id)).orElse(null);
        } catch(IllegalArgumentException e) {
            throw new InvalidObjectIdException(e);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.CollectionManager#getCollections(java.lang.String)
     */
    @Override
    public List<Collection> getCollections(String app) {
        return collectionRepo.findByAppsContaining(app);
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#editCollection(java.lang.String, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public Collection editCollection(String id, String name, String description, List<String> apps) throws CollectionNotFoundException, CitesphereAppNotFoundException, InvalidObjectIdException {
        Collection collection = findCollection(id);

        if (Objects.nonNull(collection)) {    
            validateApps(apps);
            collection.setName(name);
            collection.setDescription(description);
            collection.setApps(apps);
            return collectionRepo.save(collection);
        } else {
            throw new CollectionNotFoundException("CollectionId: " + id);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#deleteCollection(java.lang.String)
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

    /**
     * Validates the list of apps by verifying the client ids from citesphere. 
     * 
     * @param apps
     * @throws CitesphereAppNotFoundException
     */
    private void validateApps(List<String> apps) throws CitesphereAppNotFoundException {
        List<CitesphereAppInfo> citesphereApps = citesphereConnector.getCitesphereApps();
        HashSet<String> appSet = new HashSet<String>(
                citesphereApps.stream().map(app -> app.getClientId()).collect(Collectors.toList()));
        if (apps != null) {
            for (String clientId : apps) {
                if (!appSet.contains(clientId)) {
                    throw new CitesphereAppNotFoundException("No Citesphere App found with client id " + clientId);
                }
            }
        }
    }
}
