package edu.asu.diging.quadriga.core.service.impl;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.MappedCollectionRepository;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.MappedCollectionNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.MappedCollectionService;

@Service
public class MappedCollectionServiceImpl implements MappedCollectionService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MappedCollectionRepository mappedCollectionRepository;

    @Autowired
    private CollectionManager collectionManager;

    /**
     * This method checks whether a collection with given collectionId exists and
     * returns the collection if it exists If one doesn't exist, then the
     * collectionId shouldn't be linked to the mappedCollection
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
            logger.error("Couldn't find collection for collectionId: " + collectionId);
            throw new CollectionNotFoundException();
        }
        return collection;
    }

    /**
     * Converts the given collectionId into an ObjectId and persists a
     * MappedCollection in the database with this collectionId
     * 
     * @param collectionId set to the new MappedCollection
     * @return the persisted MappedCollection object
     * @throws InvalidObjectIdException    if collectionId couldn't be converted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if collection with given collectionId
     *                                     does't exist
     */
    @Override
    public MappedCollection addMappedCollection(String collectionId)
            throws InvalidObjectIdException, CollectionNotFoundException {
        MappedCollection mappedCollection = new MappedCollection();
        mappedCollection.setCollectionId(checkAndGetCollection(collectionId).getId());
        return mappedCollectionRepository.save(mappedCollection);
    }

    /**
     * This method first checks whether a collection with the given collectionId
     * exists
     * 
     * If yes, it finds a MappedCollection entry in the database with this
     * collectionId
     * 
     * @param collectionId used to look for the MappedCollection entry
     * @return the MappedCollection entry that was found, else null
     * @throws InvalidObjectIdException    if collectionId couldn't be converted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if collection with given collectionId
     *                                     doesn't exist
     */
    @Override
    public MappedCollection findMappedCollectionByCollectionId(String collectionId)
            throws InvalidObjectIdException, CollectionNotFoundException {
        return mappedCollectionRepository.findByCollectionId(checkAndGetCollection(collectionId).getId()).orElse(null);
    }

    /**
     * This method tries to find a MappedCollection with the given
     * mappedCollectionId
     * 
     * @param mappedCollectionId is the id used to find a MappedCollection
     * @return the found MappedCollectionEntry
     * @throws InvalidObjectIdException if mappedCollectionId couldn't be converted
     *                                  to ObjectId
     */
    @Override
    public MappedCollection findMappedCollectionById(String mappedCollectionId) throws InvalidObjectIdException {
        ObjectId mappedCollectionObjectId;
        try {
            mappedCollectionObjectId = new ObjectId(mappedCollectionId);
        } catch (IllegalArgumentException e) {
            logger.error("Couldn't find MappedCollection entry, invalid mappedCollectionId: " + mappedCollectionId);
            throw new InvalidObjectIdException();
        }
        return mappedCollectionRepository.findById(mappedCollectionObjectId).orElse(null);
    }

    /**
     * This method looks for a MappedCollection using the given MappedCollectionId
     * and updates the name of that MappedCollection with the given name
     * 
     * @param collectionId used to look for the MappedCollection entry
     * @return the updated MappedCollection entry
     * @throws InvalidObjectIdException          if collectionId couldn't be
     *                                           converted to ObjectId
     * @throws MappedCollectionNotFoundException if MappedCollection by the given
     *                                           collectionId was not found
     * @throws CollectionNotFoundException       if collection with given
     *                                           collectionId doesn't exist
     */
    @Override
    public MappedCollection updateMappedCollectionNameById(String mappedCollectionId, String name)
            throws InvalidObjectIdException, MappedCollectionNotFoundException {
        MappedCollection mappedCollection = findMappedCollectionById(mappedCollectionId);
        if (mappedCollection != null) {
            mappedCollection.setName(name);
            return mappedCollectionRepository.save(mappedCollection);
        } else {
            logger.error("Couldn't MappedCollection for mappedCollectionId: " + mappedCollectionId);
            throw new MappedCollectionNotFoundException();
        }
    }

    /**
     * This method will try to find a MappedCollection entry based on the given
     * collectionId. If no entry was found, it will try to create a new one
     * 
     * @param collectionId used to look for the MappedCollection entry
     * @return an existing or a new MappedCollectionEntry
     * @throws InvalidObjectIdException    if collectionId couldn't be converted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if a collection for given collectionId
     *                                     doesn't exist
     */
    @Override
    public MappedCollection findOrAddMappedCollectionByCollectionId(String collectionId)
            throws InvalidObjectIdException, CollectionNotFoundException {
        MappedCollection mappedCollection = findMappedCollectionByCollectionId(collectionId);

        // In case this is a new collection and a network is being submitted to this
        // collection for the first time, we create a new MappedCollection entry
        if (mappedCollection == null) {
            mappedCollection = addMappedCollection(collectionId);
        }
        if (mappedCollection == null) {
            logger.error("Couldn't find or persist a new MappedCollection entry for collectionId: " + collectionId);
        }
        return mappedCollection;
    }

}
