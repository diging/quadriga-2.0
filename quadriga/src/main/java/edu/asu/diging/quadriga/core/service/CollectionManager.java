package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;

public interface CollectionManager {

    public Collection addCollection(String name, String description);
    public Collection findCollection(String id) throws InvalidObjectIdException;
    public Collection editCollection(String id, String name, String description) throws CollectionNotFoundException, InvalidObjectIdException;
    public void deleteCollection(String id) throws CollectionNotFoundException, InvalidObjectIdException;
    public Collection getCollection(String collectionId) throws InvalidObjectIdException, CollectionNotFoundException;
}
