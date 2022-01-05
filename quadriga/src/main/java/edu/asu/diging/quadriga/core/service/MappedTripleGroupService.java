package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.MappedTripleGroupNotFoundException;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;

/**
 * 
 * This is a service that uses the MappedTripleGroupRepository to manage a
 * MappedTripleGroup in the database
 * 
 * @author poojakulkarni
 *
 */
public interface MappedTripleGroupService {

    public MappedTripleGroup addMappedTripleGroup(String collectionId)
            throws InvalidObjectIdException, CollectionNotFoundException;

    public MappedTripleGroup findMappedTripleGroupByCollectionId(String collectionId)
            throws InvalidObjectIdException, CollectionNotFoundException;

    public MappedTripleGroup findMappedTripleGroupById(String mappedTripleGroupId) throws InvalidObjectIdException;

    public MappedTripleGroup updateMappedTripleGroupNameById(String mappedTripleGroupId, String name)
            throws InvalidObjectIdException, MappedTripleGroupNotFoundException;

}
