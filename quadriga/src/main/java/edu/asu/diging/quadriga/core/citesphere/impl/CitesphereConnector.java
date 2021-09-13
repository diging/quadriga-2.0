package edu.asu.diging.quadriga.core.citesphere.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

import edu.asu.diging.quadriga.core.citesphere.ICitesphereConnector;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

@Service
@PropertySource("classpath:/config.properties")
public class CitesphereConnector implements ICitesphereConnector {

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

    public CitesphereConnector() {
        restTemplate = new RestTemplate();
    }

    @Override
    public List<CitesphereAppInfo> getCitesphereApps() throws HttpClientErrorException {
        if (currentAccessToken == null) {
            currentAccessToken = getAccessToken();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + currentAccessToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String appUrl = citesphereBaseUrl + citesphereAppsEndpoint;

        try {
            return restTemplate.exchange(appUrl, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<CitesphereAppInfo>>() {}).getBody();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                currentAccessToken = getAccessToken();
                headers.set("Authorization", "Bearer " + currentAccessToken);
                return restTemplate.exchange(appUrl, HttpMethod.GET, entity,
                        new ParameterizedTypeReference<List<CitesphereAppInfo>>() {}).getBody();
            }
            throw ex;
        }
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
