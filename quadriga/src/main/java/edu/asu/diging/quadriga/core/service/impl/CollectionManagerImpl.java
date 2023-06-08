package edu.asu.diging.quadriga.core.service.impl;

import java.time.OffsetDateTime;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.data.CollectionRepository;
import edu.asu.diging.quadriga.core.exceptions.CitesphereAppNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.PredicateManager;

@Service
public class CollectionManagerImpl implements CollectionManager {

    @Autowired
    private CitesphereConnector citesphereConnector;
    
    @Autowired
    private CollectionRepository collectionRepo;
    
    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;
    
    @Autowired
    private EventGraphService eventGraphService;
    
    @Autowired
    private PredicateManager predicateManager;
    
    private Logger logger = LoggerFactory.getLogger(getClass());


    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.CollectionManager#addCollection(java.lang.String, java.lang.String, java.util.List)
     */
    public Collection addCollection(String name, String description, List<String> apps) throws CitesphereAppNotFoundException {   
        validateApps(apps);      
        Collection collection = new Collection();
        collection.setCreationTime(OffsetDateTime.now());
        collection.setName(name);
        collection.setDescription(description);
        collection.setApps(apps);
        return collectionRepo.save(collection);
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.CollectionManager#findCollection(java.lang.String)
     */
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
     * @see edu.asu.diging.quadriga.core.service.CollectionManager#editCollection(java.lang.String, java.lang.String, java.lang.String, java.util.List)
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
     * @see edu.asu.diging.quadriga.core.service.CollectionManager#deleteCollection(java.lang.String)
     */
    @Override
    public Collection deleteCollection(String id) throws CollectionNotFoundException, InvalidObjectIdException {
        Collection collection = findCollection(id);
        
        if (collection != null) {
            EventGraph eventGraph = eventGraphService.findLatestEventGraphByCollectionId(new ObjectId(id));
            // If networks are linked with the collection, archive it. Delete if it's empty.
            if (eventGraph != null) {
                collection.setArchived(true);
                collectionRepo.save(collection);
                return collection;
            }
            collectionRepo.delete(collection);
            return null;
        } else {
            throw new CollectionNotFoundException("CollectionId: " + id);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ICollectionManager#getCollection(java.lang.String)
     */
    @Override
    public Collection getCollection(String collectionId) throws InvalidObjectIdException, CollectionNotFoundException {
        Collection collection = findCollection(collectionId);
        if (collection == null) {
            throw new CollectionNotFoundException("CollectionId: " + collectionId);
        }
        return collection;
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
    
    /**
     * This method returns the number of default mappings present in the collection
     * One MappedTripleGroup will exist for the "DefaultMappings" for this collection
     * To get this number of default mappings, this method will check how many 'Predicates' have
     * this mappedTripleGroupId linked to them
     * This is because every default mapping has one predicate
     * So, if the MappedTripleGroupId is present on n predicates, this collection
     * must have n defaultMappings 
     * 
     * @param collectionId used to find mappedTripleGroupId
     * @return the number of default mappings
     */
    @Override
    public int getNumberOfDefaultMappings(String collectionId) {
        try {
            MappedTripleGroup mappedTripleGroup = mappedTripleGroupService.findByCollectionIdAndMappingType(collectionId, MappedTripleType.DEFAULT_MAPPING);
            if(mappedTripleGroup != null) {    
                return predicateManager.countPredicatesByMappedTripleGroup(mappedTripleGroup.get_id().toString());
            }

        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("Couldn't find number of default mappings for collection ",e);
        }
        return 0;
    }

    @Override
    public Page<Collection> findByArchived(boolean archived,int pageInt,int sizeInt) {
        
        return collectionRepo.findByArchived(archived, PageRequest.of(pageInt, sizeInt));
    }
}
