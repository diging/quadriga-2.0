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
        Collection collection = new Collection();
        collection.setName(name);
        collection.setDescription(description);
        collection.setApps(apps);
        return collectionRepo.save(collection);
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#findCollection(java.lang.String)
     */
    @Override
    public Collection findCollection(String id) {
        return collectionRepo.findById(new ObjectId(id)).orElse(null);
    }


    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#editCollection(java.lang.String, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public Collection editCollection(String id, String name, String description, List<String> apps) throws CollectionNotFoundException, CitesphereAppNotFoundException {
        Collection collection = findCollection(id);

        if (Objects.nonNull(collection)) {
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
            collection.setName(name);
            collection.setDescription(description);
            collection.setApps(apps);
            return collectionRepo.save(collection);
        } else {
            throw new CollectionNotFoundException();
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#deleteCollection(java.lang.String)
     */
    public void deleteCollection(String id) throws CollectionNotFoundException {
        Collection collection = findCollection(id);
        
        if(Objects.nonNull(collection)) {
            
            // Once networks are linked with collections, only empty collections will be deleted
            // If it is linked to a network, we will archive the collection.
            collectionRepo.delete(collection);
        } else {
            throw new CollectionNotFoundException();
        }
    }

}
