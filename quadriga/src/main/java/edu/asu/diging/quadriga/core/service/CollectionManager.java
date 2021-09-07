package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;

public interface CollectionManager {

    public Collection addCollection(String name, String description);
    public Collection findCollection(String id);
    public Collection editCollection(String id, String name, String description) throws CollectionNotFoundException;
}
