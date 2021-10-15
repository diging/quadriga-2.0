package edu.asu.diging.quadriga.core.citesphere;

import java.util.List;

import org.springframework.web.client.HttpClientErrorException;

import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

public interface CitesphereConnector {
    
    /**
     * Fetches the list of Citesphere applications
     * @return the list of apps
     */
    List<CitesphereAppInfo> getCitesphereApps() throws HttpClientErrorException;

}