package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.core.exceptions.CitesphereAppNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;

public interface CollectionManager {


    /**
     * Saves a collection in database with the given details
     * @param name collection name
     * @param description collection description
     * @param apps list of citesphere apps attached to the collection
     * @return the saved collection
     */
    public Collection addCollection(String name, String description, List<String> apps) throws CitesphereAppNotFoundException;
    
    /**
     * Finds a collection from the collection table by id
     * @param id used to look up the collection in mongodb
     * @return Collection Instance that is found from the database
     * 
     **/
    public Collection findCollection(String id) throws InvalidObjectIdException;
    
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
    public Collection editCollection(String id, String name, String description, List<String> apps) throws CollectionNotFoundException, CitesphereAppNotFoundException, InvalidObjectIdException;
    
    /**
     * Deletes a collection from collection table by id
     * @param id used to look up the collection in database
     */
    public void deleteCollection(String id) throws CollectionNotFoundException, InvalidObjectIdException;

    
    /**
     * This method checks whether a collection with given collectionId exists and
     * returns the collection if it exists.
     * If it doesn't exist, it simply throws an exception
     * 
     * @param collectionId is the id of the collection to be checked
     * @return the Collection entry found in the database
     * @throws InvalidObjectIdException    if collectionId couldn't be conveted to
     *                                     ObjectId
     * @throws CollectionNotFoundException if collection with given collectionId
     *                                     does't exist
     */
    public Collection getCollection(String collectionId) throws InvalidObjectIdException, CollectionNotFoundException;

}
