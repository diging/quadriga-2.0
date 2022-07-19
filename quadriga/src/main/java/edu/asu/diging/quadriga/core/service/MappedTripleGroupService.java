package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.MappedTripleGroupNotFoundException;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;

/**
 * 
 * This is a service that uses the MappedTripleGroupRepository to manage a
 * MappedTripleGroup in the database.
 * 
 * @author poojakulkarni
 *
 */
public interface MappedTripleGroupService {

    public MappedTripleGroup add(String collectionId, MappedTripleType mappedTripleType)
            throws InvalidObjectIdException, CollectionNotFoundException;

    public MappedTripleGroup findByCollectionIdAndMappingType(String collectionId, MappedTripleType mappedTripleType)
            throws InvalidObjectIdException, CollectionNotFoundException;
    
    public MappedTripleGroup findByCollectionIdAndId(String collectionId, String mappedTripleGroupId) throws InvalidObjectIdException;

    public MappedTripleGroup getById(String mappedTripleGroupId) throws InvalidObjectIdException;

    public MappedTripleGroup updateName(String mappedTripleGroupId, String name)
            throws InvalidObjectIdException, MappedTripleGroupNotFoundException;
    
    public MappedTripleGroup get(String collectionId, MappedTripleType mappedTripleType)
            throws InvalidObjectIdException, CollectionNotFoundException;

}
