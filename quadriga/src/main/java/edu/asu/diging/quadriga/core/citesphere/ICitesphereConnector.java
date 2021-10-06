package edu.asu.diging.quadriga.core.citesphere;

import java.util.List;

import org.springframework.web.client.HttpClientErrorException;

import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

public interface ICitesphereConnector {
    
    /**
     * Fetches the list of Citesphere applications
     * @return the list of apps
     */
    List<CitesphereAppInfo> getCitesphereApps() throws HttpClientErrorException;
    
    /**
     * Gets a new access token for connecting with Citesphere
     * @return the access token
     */
    String getAccessToken();
}