package edu.asu.diging.quadriga.core.citesphere.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ClientCredentialsGrant;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

@Service
@PropertySource("classpath:/config.properties")
public class CitesphereConnectorImpl implements CitesphereConnector {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${citesphere_client_id}")
    private String citesphereClientId;

    @Value("${citesphere_client_secret}")
    private String citesphereSecret;

    @Value("${citesphere_base_url}")
    private String citesphereBaseUrl;

    @Value("${citesphere_token_endpoint}")
    private String citesphereTokenEndpoint;

    @Value("${citesphere_check_token_endpoint}")
    private String citesphereCheckTokenEndpoint;

    @Value("${citesphere_scopes}")
    private String citesphereScopes;

    @Value("${citesphere_get_apps_endpoint}")
    private String citesphereAppsEndpoint;

    private RestTemplate restTemplate;

    private String currentAccessToken;

    public CitesphereConnectorImpl() {
        restTemplate = new RestTemplate();
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.citesphere.ICitesphereConnector#getCitesphereApps()
     */
    @Override
    @Cacheable(value = "citesphereApps")
    public List<CitesphereAppInfo> getCitesphereApps() throws HttpClientErrorException {
        String appUrl = citesphereBaseUrl + citesphereAppsEndpoint;

        try {
            return restTemplate.exchange(appUrl, HttpMethod.GET, generateTokenEntity(),
                    new ParameterizedTypeReference<List<CitesphereAppInfo>>() {
                    }).getBody();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                currentAccessToken = getAccessToken();
                return restTemplate.exchange(appUrl, HttpMethod.GET, generateTokenEntity(),
                        new ParameterizedTypeReference<List<CitesphereAppInfo>>() {
                        }).getBody();
            }
            throw ex;
        }
    }
    
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
    @Override
    public TokenInfo getTokenInfo(String token) throws BadCredentialsException, OAuthException {
        

        String checkTokenUrl = citesphereBaseUrl + citesphereCheckTokenEndpoint + "?token=" + token;
        TokenInfo tokenInfo = null;
        
        try {
            tokenInfo = restTemplate.postForObject(checkTokenUrl, generateTokenEntity(), TokenInfo.class, new Object[] {});
        } catch (HttpClientErrorException e1) {
            
            if (e1.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                
                // If we get an unauthorized exception, the accessToken could be expired
                // so we generate a new access token and we again try to call checkToken URL
                currentAccessToken = getAccessToken();
                try {
                    tokenInfo = restTemplate.postForObject(checkTokenUrl, generateTokenEntity(), TokenInfo.class, new Object[] {});
                }  catch(HttpClientErrorException e2) {

                    //  If we again get an unauthorized exception, we will just throw an OAuthException
                    if (e2.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        throw new OAuthException();
                    }
                    throw new BadCredentialsException("Token is invalid for app.", e1);
                }
            } else {
            
                // If it is some other kind of exception, we throw a BadCredentialsException
                throw new BadCredentialsException("Token is invalid for app.", e1);
            }
        }

        return tokenInfo;
    }
    
    
    /**
     * Here we generate an HTTP entity based on the accessToken
     * If accessToken is absent, we ask the getAccessToken method to generate one
     * 
     * @return an HTTP Entity
     */
    private HttpEntity<String> generateTokenEntity() {
        if (currentAccessToken == null) {
            currentAccessToken = getAccessToken();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + currentAccessToken);

        return new HttpEntity<String>(headers);
    }

    private String getAccessToken() {
        AuthorizationGrant clientGrant = new ClientCredentialsGrant();
        ClientID clientID = new ClientID(citesphereClientId);
        Secret clientSecret = new Secret(citesphereSecret);
        ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
        Scope scope = new Scope(citesphereScopes.split(","));

        URI tokenEndpoint;
        try {
            tokenEndpoint = new URI(citesphereBaseUrl + citesphereTokenEndpoint);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }

        TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, clientGrant, scope);
        TokenResponse response;
        try {
            response = TokenResponse.parse(request.toHTTPRequest().send());
        } catch (ParseException | IOException e) {
            throw new IllegalArgumentException(e);
        }

        if (!response.indicatesSuccess()) {
            logger.error("Quadriga could not retrieve access token. Maybe the configuration is wrong.");
            throw new IllegalArgumentException(
                    "Quadriga could not retrieve access token. Maybe the configuration is wrong.");
        }

        AccessToken accessTokenResponse = ((AccessTokenResponse) response).getTokens().getAccessToken();
        return accessTokenResponse.getValue();
    }

}
