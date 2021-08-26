package edu.asu.diging.quadriga.core.service;

import java.util.Optional;

import edu.asu.diging.quadriga.core.model.Collection;

public interface CollectionManager {

    public Collection addCollection(String name, String description);
    public Collection findCollection(String id);
    public Collection editCollection(String id, Optional<String> name, Optional<String> description);
}
