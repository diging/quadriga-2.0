package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.core.exceptions.SimpleUserAppNotFoundException;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;

public interface SimpleUserAppService {

    /**
     * Saves a mapping of the Quadriga user to client id of Citesphere apps.
     * @param username username for the mapping
     * @param clientId client id for the mapping
     * @return the saved details
     */
    public SimpleUserApp save(String username, String clientId);
    
    /**
     * Finds list of all apps that the user has access to
     * @param username is the username to be searched
     * @return a list of SimpleUserApps with provided username
     */
    public List<SimpleUserApp> findByUsername(String username);
    
    /**
     * Deletes the app with given username and app client id
     * @param username is the username to be searched
     * @param appClientId is the app Client Id to be searched
     */
    public void delete(String username, String appClientId) throws SimpleUserAppNotFoundException;
    
    /**
     * To find list of all apps that user has access to with pagination
     * @param offset determines the starting point of the result set
     * @param pageSize is the number of items per page
     */
    public List<SimpleUserApp> findByUsername(String username,int offset,int pageSize);
    
    /**
     * 
     * @param username username is the username to be searched
     * @return a List of client Ids associated with the user
     */
    public List<String> findAppClientIdsByUsername(String username);
   
}
