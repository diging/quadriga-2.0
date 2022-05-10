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
    
    /**
     * This method accepts a token from Vogon and sends it to Citesphere's "check_token"
     * end point for token validation using an access token
     * This access token is used by quadriga to talk to citesphere
     * 
     * If Citesphere responds with unauthorized HTTP exception, we try to re-generate
     * the access token, in case it is expired.
     * 
     * @param token is the one we receive from Vogon to check the validity
     * @return the token response received from citesphere
     * @throws BadCredentialsException if token/access token is invalid
     * @throws OAuthException if request is unauthorized because of the token/access token
     */
    TokenInfo getTokenInfo(String token) throws BadCredentialsException, OAuthException;

}