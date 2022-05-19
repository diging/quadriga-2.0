package edu.asu.diging.quadriga.core.service;

import java.util.List;

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
     * Finds list of all apps that the user has access to
     * 
     * @param username is the username to be searched
     * @return a list of SimpleUserApps with provided username
     */
    public List<SimpleUserApp> findByUsername(String username);
    
    /**
     * Deletes the app with given username and app Client Id
     * 
     * @param username is the username to be searched
     * @param appClientId is the app Client Id to be searched
     */
    public void delete(String username, String appClientId);
    
    /**
     * Retrieves citesphere apps that the user has access to
     * @param user user for which the app are to be retrieved
     * @return List of the apps
     */
    public List<CitesphereAppInfo> getCitesphereApps(SimpleUser user) ;

}
