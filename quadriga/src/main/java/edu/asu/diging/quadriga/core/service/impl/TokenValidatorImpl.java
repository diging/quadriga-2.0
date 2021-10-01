package edu.asu.diging.quadriga.core.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.exceptions.TokenInfoNotFoundException;
import edu.asu.diging.quadriga.core.service.TokenValidator;

@Service
@PropertySource({ "classpath:config.properties" })
public class TokenValidatorImpl implements TokenValidator {

    @Value("${citesphere_base_url}")
    private String citesphereBaseURL;

    @Value("${citesphere_check_token_endpoint}")
    private String citesphereCheckTokenEndpoint;

    @Value("${citesphere_token_endpoint}")
    private String citesphereTokenEndpoint;

    @Value("${citesphere_client_id}")
    private String citesphereClientId;

    @Value("${citesphere_client_secret}")
    private String citesphereClientSecret;

    @Value("${citesphere_scopes}")
    private String citesphereScopes;

    private RestTemplate restTemplate;

    private String accessToken;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public TokenValidatorImpl() {
        restTemplate = new RestTemplate();
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
     * @return whether the token is active or not
     * @throws BadCredentialsException if token/access token is invalid
     * @throws OAuthException if request is unauthorized because of the token/access token
     * @throws TokenInfoNotFoundException in case citesphere sends a null response
     */
    @Override
    public boolean validateToken(String token) throws BadCredentialsException, OAuthException, TokenInfoNotFoundException {
        

        String checkTokenUrl = citesphereBaseURL + citesphereCheckTokenEndpoint + "?token=" + token;
        TokenInfo tokenInfo = null;
        
        try {
            tokenInfo = restTemplate.postForObject(checkTokenUrl, generateCheckTokenEntity(), TokenInfo.class, new Object[] {});
        } catch (HttpClientErrorException e1) {
            
            if (e1.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                
                // If we get an unauthorized exception, the accessToken could be expired
                // so we generate a new access token and we again try to call checkToken URL
                accessToken = getAccessToken();
                try {
                    tokenInfo = restTemplate.postForObject(checkTokenUrl, generateCheckTokenEntity(), TokenInfo.class, new Object[] {});
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

        if (tokenInfo != null) {
            return tokenInfo.isActive();
        } else {
            throw new TokenInfoNotFoundException();
        }
    }
    
    
    /**
     * Here we generate an HTTP entity based on the accessToken
     * If accessToken is absent, we ask the getAccessToken method to generate one
     * 
     * @return an HTTP Entity
     */
    private HttpEntity<String> generateCheckTokenEntity() {
        if (accessToken == null) {
            accessToken = getAccessToken();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        return new HttpEntity<String>(headers);
    }

    
    /**
     * This method is used to generate an access token using the ClientId and ClientSecret
     * that is registered with Citesphere
     * 
     * @return the generated access token
     */
    private String getAccessToken() {

        AuthorizationGrant clientGrant = new ClientCredentialsGrant();
        ClientID clientID = new ClientID(citesphereClientId);
        Secret clientSecret = new Secret(citesphereClientSecret);
        ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
        Scope scope = new Scope(citesphereScopes.split(","));
        URI tokenEndpoint;

        try {
            tokenEndpoint = new URI(citesphereBaseURL + citesphereTokenEndpoint);
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

        return ((AccessTokenResponse) response).getTokens().getAccessToken().getValue();
    }

}
