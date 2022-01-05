package edu.asu.diging.quadriga.core.service.impl;

import org.bson.types.ObjectId;
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

    /**
     * This method checks whether a collection with given collectionId exists and
     * returns the collection if it exists If one doesn't exist, then the
     * collectionId shouldn't be linked to the mappedTripleGroup
     * 
     * @param collectionId is the id of the collection to be checked
     * @return the Collection entry found in the database
     * @throws InvalidObjectIdException    if collectionId couldn't be conveted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if collection with given collectionId
     *                                     does't exist
     */
    private Collection checkAndGetCollection(String collectionId)
            throws InvalidObjectIdException, CollectionNotFoundException {
        Collection collection = collectionManager.findCollection(collectionId);
        if (collection == null) {
            throw new CollectionNotFoundException("CollectionId: " + collectionId);
        }
        return collection;
    }

    /**
     * Converts the given collectionId into an ObjectId and persists a
     * MappedTripleGroup in the database with this collectionId
     * 
     * @param collectionId set to the new MappedTripleGroup
     * @return the persisted MappedTripleGroup object
     * @throws InvalidObjectIdException    if collectionId couldn't be converted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if collection with given collectionId
     *                                     does't exist
     */
    @Override
    public MappedTripleGroup addMappedTripleGroup(String collectionId)
            throws InvalidObjectIdException, CollectionNotFoundException {
        
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        
        mappedTripleGroup.setCollectionId(checkAndGetCollection(collectionId).getId());
        
        // This will be updated when custom mappings are added
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DefaultMapping);
        
        return mappedTripleGroupRepository.save(mappedTripleGroup);
    }

    /**
     * This method first checks whether a collection with the given collectionId
     * exists
     * 
     * If yes, it finds a MappedTripleGroup entry in the database with this
     * collectionId
     * 
     * @param collectionId used to look for the MappedTripleGroup entry
     * @return the MappedTripleGroup entry that was found, else null
     * @throws InvalidObjectIdException    if collectionId couldn't be converted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if collection with given collectionId
     *                                     doesn't exist
     */
    @Override
    public MappedTripleGroup findMappedTripleGroupByCollectionId(String collectionId)
            throws InvalidObjectIdException, CollectionNotFoundException {
        return mappedTripleGroupRepository.findByCollectionId(checkAndGetCollection(collectionId).getId()).orElse(null);
    }

    /**
     * This method tries to find a MappedTripleGroup with the given
     * mappedTripleGroupId
     * 
     * @param mappedTripleGroupId is the id used to find a MappedTripleGroup
     * @return the found mappedTripleGroupEntry
     * @throws InvalidObjectIdException if mappedTripleGroupId couldn't be converted
     *                                  to ObjectId
     */
    @Override
    public MappedTripleGroup findMappedTripleGroupById(String mappedTripleGroupId) throws InvalidObjectIdException {
        ObjectId mappedTripleGroupObjectId;
        try {
            mappedTripleGroupObjectId = new ObjectId(mappedTripleGroupId);
        } catch (IllegalArgumentException e) {
            throw new InvalidObjectIdException("MappedTripleGroupId: " + mappedTripleGroupId);
        }
        return mappedTripleGroupRepository.findById(mappedTripleGroupObjectId).orElse(null);
    }

    /**
     * This method looks for a MappedTripleGroup using the given mappedTripleGroupId
     * and updates the name of that MappedTripleGroup with the given name
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
    public MappedTripleGroup updateMappedTripleGroupNameById(String mappedTripleGroupId, String name)
            throws InvalidObjectIdException, MappedTripleGroupNotFoundException {
        MappedTripleGroup mappedTripleGroup = findMappedTripleGroupById(mappedTripleGroupId);
        if (mappedTripleGroup != null) {
            mappedTripleGroup.setName(name);
            return mappedTripleGroupRepository.save(mappedTripleGroup);
        } else {
            throw new MappedTripleGroupNotFoundException("MappedTripleGroupId: " + mappedTripleGroupId);
        }
    }

}
