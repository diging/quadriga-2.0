package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.model.Collection;

public interface CollectionManager {

    public Collection addCollection(String name, String description);
    public Collection findCollection(String id);
    public boolean deleteCollection(String id);
}
