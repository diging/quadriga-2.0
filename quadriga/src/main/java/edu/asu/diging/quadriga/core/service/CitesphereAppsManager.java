package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

public interface CitesphereAppsManager {
	
	/**
     * Retrieves citesphere apps that the user has access to
     * @param user user for which the app are to be retrieved
     * @return List of the apps
     */
	public List<CitesphereAppInfo> getCitesphereApps(SimpleUser user) ;

}
