package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

public interface ICollectionManager {

    /**
     * Saves a collection in database with the given details
     * @param name collection name
     * @param description collection description
     * @param apps list of citesphere apps attached to the collection
     * @return the saved collection
     */
    public Collection addCollection(String name, String description, List<CitesphereAppInfo> apps);
    
}
