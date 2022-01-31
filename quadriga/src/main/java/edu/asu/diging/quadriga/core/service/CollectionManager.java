package edu.asu.diging.quadriga.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;

public interface CollectionManager {

    /**
     * Saves a collection in database with the given details
     * @param name collection name
     * @param description collection description
     * @param apps list of citesphere apps attached to the collection
     * @return the saved collection
     */
    public Collection addCollection(String name, String description, String username, List<String> apps);
    
    /**
     * Finds a collection from the collection table by id
     * @param id used to look up the collection in mongodb
     * @return Collection Instance that is found from the database
     * 
     **/
    public Collection findCollection(String id);
    
    /**
     * 
     * Edits an existing Collection and updates it in the db
     * @param id of the collection that needs to be updated
     * @param name will be the updated name value
     * @param description will be the updated description value
     * @param apps list of citesphere apps attached to the collection
     * @return Collection Instance that is updated in database
     * @throws CollectionNotFoundException in case the collection for the given id is missing
     */
    public Collection editCollection(String id, String name, String description, List<String> apps) throws CollectionNotFoundException;
    
    /**
     * Deletes a collection from collection table by id
     * @param id used to look up the collection in database
     */
    public void deleteCollection(String id) throws CollectionNotFoundException;
    
    /**
     * Finds all collections that have at least one of the app listed in the apps argument
     * 
     * @param apps is a list of apps to be searched
     * @return a list of collections
     */
    public Page<Collection> findByAppsIn(List<String> apps, Pageable pageable);
    
}
