package edu.asu.diging.quadriga.core.service.impl;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.MappedTripleGroupRepository;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.MappedTripleGroupNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;

@Service
public class MappedTripleGroupServiceImpl implements MappedTripleGroupService {
    
    @Autowired
    private MappedTripleGroupRepository mappedTripleGroupRepository;

    @Autowired
    private CollectionManager collectionManager;
    
    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * Converts the given collectionId into an ObjectId and persists a
     * MappedTripleGroup in the database with this collectionId and MappedTripleType.
     * 
     * @param collectionId set to the new MappedTripleGroup
     * @param mappedTripleType is the group type which the current triple belongs to
     * @return the persisted MappedTripleGroup object
     * @throws InvalidObjectIdException    if collectionId couldn't be converted to
     *                                     ObjectId
     */
    @Override
    public MappedTripleGroup add(String collectionId, MappedTripleType mappedTripleType)
            throws InvalidObjectIdException {
        
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        
        try {
            mappedTripleGroup.setCollectionId(new ObjectId(collectionId));
        } catch(IllegalArgumentException e) {
            throw new InvalidObjectIdException(e);
        }
        mappedTripleGroup.setMappedTripleType(mappedTripleType);
        
        return mappedTripleGroupRepository.save(mappedTripleGroup);
    }

    /**
     * This method first checks whether a collection with the given collectionId
     * exists.
     * 
     * If yes, it finds a MappedTripleGroup entry in the database with this
     * collectionId and the MappedTripleType.
     * 
     * @param collectionId used to look for the MappedTripleGroup entry
     * @param mappedTripleType is the group type to be looked for in the database
     * @return the MappedTripleGroup entry that was found, else null
     * @throws InvalidObjectIdException    if collectionId couldn't be converted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if collection with given collectionId
     *                                     doesn't exist
     */
    @Override
    public MappedTripleGroup findByCollectionIdAndMappingType(String collectionId, MappedTripleType mappedTripleType)
            throws InvalidObjectIdException, CollectionNotFoundException {
        
        Collection collection = new Collection();
        collection = collectionManager.findCollection(collectionId);
        
        if(collection == null) {
            throw new CollectionNotFoundException("Couldn't find collection for id: " + collectionId);
        }
        
        return mappedTripleGroupRepository
                .findByCollectionIdAndMappedTripleType(collection.getId(), mappedTripleType)
                .orElse(null);
    }

    /**
     * This method tries to find a MappedTripleGroup with the given
     * mappedTripleGroupId.
     * 
     * @param mappedTripleGroupId is the id used to find a MappedTripleGroup
     * @return the found mappedTripleGroupEntry
     * @throws InvalidObjectIdException if mappedTripleGroupId couldn't be converted
     *                                  to ObjectId
     */
    @Override
    public MappedTripleGroup getById(String mappedTripleGroupId) throws InvalidObjectIdException {
        ObjectId mappedTripleGroupObjectId;
        try {
            mappedTripleGroupObjectId = new ObjectId(mappedTripleGroupId);
        } catch (IllegalArgumentException e) {
            throw new InvalidObjectIdException("MappedTripleGroupId: " + mappedTripleGroupId, e);
        }
        return mappedTripleGroupRepository.findById(mappedTripleGroupObjectId).orElse(null);
    }

    /**
     * This method looks for a MappedTripleGroup using the given mappedTripleGroupId
     * and updates the name of that MappedTripleGroup with the given name.
     * 
     * @param collectionId used to look for the MappedTripleGroup entry
     * @return the updated MappedTripleGroup entry
     * @throws InvalidObjectIdException          if collectionId couldn't be
     *                                           converted to ObjectId
     * @throws MappedTripleGroupNotFoundException if MappedTripleGroup by the given
     *                                           collectionId was not found
     * @throws CollectionNotFoundException       if collection with given
     *                                           collectionId doesn't exist
     */
    @Override
    public MappedTripleGroup updateName(String mappedTripleGroupId, String name)
            throws InvalidObjectIdException, MappedTripleGroupNotFoundException {
        MappedTripleGroup mappedTripleGroup = getById(mappedTripleGroupId);
        if (mappedTripleGroup != null) {
            mappedTripleGroup.setName(name);
            return mappedTripleGroupRepository.save(mappedTripleGroup);
        } else {
            throw new MappedTripleGroupNotFoundException("MappedTripleGroupId: " + mappedTripleGroupId);
        }
    }
    
    /**
     * This method will try to find a MappedTripleGroup entry based on the given
     * collectionId and mappedTripleType. If no entry was found, it will try to create a new one.
     * 
     * @param collectionId used to look for the MappedTripleGroup entry
     * @param mappedTripleType is the group type which is to be looked for in the database
     * @return an existing or a new mappedTripleGroup entry
     * @throws InvalidObjectIdException    if collectionId couldn't be converted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if a collection for given collectionId
     *                                     doesn't exist
     */
    @Override
    public MappedTripleGroup get(String collectionId, MappedTripleType mappedTripleType)
            throws InvalidObjectIdException, CollectionNotFoundException {
        MappedTripleGroup mappedTripleGroup = findByCollectionIdAndMappingType(collectionId, mappedTripleType);

        // In case this is a new collection, or existing collection but new mapping type for that collection
        // we create a new MappedTripleGroup entry
        if (mappedTripleGroup == null) {
            mappedTripleGroup = add(collectionId, mappedTripleType);
            
            // If mappedTripleGroup add fails
            if (mappedTripleGroup == null) {
                logger.error("Couldn't find or persist a new MappedTripleGroup entry for collectionId: " + collectionId);
            }
        }
       
        return mappedTripleGroup;
    }

    @Override
    public MappedTripleGroup findByCollectionIdAndId(String collectionId, String mappedTripleGroupId) throws InvalidObjectIdException {
        ObjectId collectionObjectId;
        ObjectId mappedTripleGroupObjectId;
        try {
            collectionObjectId = new ObjectId(collectionId);
            mappedTripleGroupObjectId = new ObjectId(mappedTripleGroupId);
        } catch (IllegalArgumentException e) {
            throw new InvalidObjectIdException("MappedTripleGroupId: " + mappedTripleGroupId, e);
        }
        return mappedTripleGroupRepository.findBy_idAndCollectionId(mappedTripleGroupObjectId, collectionObjectId).orElse(null);
    }

}
