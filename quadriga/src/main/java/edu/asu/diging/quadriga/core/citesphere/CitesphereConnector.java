package edu.asu.diging.quadriga.core.citesphere;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.HttpClientErrorException;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

public interface CitesphereConnector {
    
    /**
     * Fetches the list of Citesphere applications
     * @return the list of apps
     */
    List<CitesphereAppInfo> getCitesphereApps() throws HttpClientErrorException;
    
    TokenInfo getTokenInfo(String token) throws BadCredentialsException, OAuthException;

}