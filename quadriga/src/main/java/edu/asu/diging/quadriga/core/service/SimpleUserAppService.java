package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.core.exceptions.UserAppNotFoundException;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

public interface SimpleUserAppService {

    /**
     * Saves the simpleUser object in the database, that maps the provided user to the clientId
     * 
     * @param simpleUserApp is the entity to be persisted
     * @return the persisted entity
     */
    public SimpleUserApp save(SimpleUserApp simpleUserApp);
    
    /**
     * Finds list of all SimpleUserApp objects with the provided username
     * 
     * @param username is the username to be searched
     * @return a list of SimpleUserApps with provided username
     */
    public List<SimpleUserApp> findByUsername(String username);
    
    /**
     * Finds a SimpleUser object from the database by querying on the provided username and appClientId
     * 
     * @param username is the username to be searched
     * @param appClientId is the appClientId to be searched
     * @return a SimpleUserApp object with the provided username and appClientId
     */
    public SimpleUserApp findByUsernameAndAppClientId(String username, String appClientId);
    
    /**
     * Deletes a SimpleUserApp entity from the database by querying on the username and appClientId
     * 
     * @param username is the username to be searched
     * @param appClientId is the appClientId to be searched
     * @throws UserAppNotFoundException is no user + app combination was found to be removed
     */
    public void delete(String username, String appClientId) throws UserAppNotFoundException;
    
    /**
     * Retrieves citesphere apps that the user has access to
     * @param user user for which the app are to be retrieved
     * @return List of the apps
     */
    public List<CitesphereAppInfo> getAccessibleCitesphereApps(SimpleUser user) ;

}
