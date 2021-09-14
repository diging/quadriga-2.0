package edu.asu.diging.quadriga.core.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;

import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.TokenInfoNotFoundException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TokenResponse.class)
@PowerMockIgnore({"jdk.internal.reflect.*"})
public class TokenValidatorImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TokenValidatorImpl tokenValidatorImpl;

    private String accessToken;
    private String token;
    private String checkTokenUrl;
    private String citesphereClientId;
    private String citesphereClientSecret;

    @Before
    public void setUp() throws NoSuchFieldException, SecurityException {
        accessToken = "SAMPLE_ACCESS_TOKEN";
        token = "SAMPLE_TOKEN";
        checkTokenUrl = "http://diging.asu.edu/citesphere/api/oauth/check_token?token=SAMPLE_TOKEN";
        citesphereClientId = "SAMPLE_CLIENT_ID";
        citesphereClientSecret = "SAMPLE_CLIENT_SECRET";

        FieldSetter.setField(tokenValidatorImpl, tokenValidatorImpl.getClass().getDeclaredField("accessToken"),
                accessToken);
        FieldSetter.setField(tokenValidatorImpl, tokenValidatorImpl.getClass().getDeclaredField("citesphereBaseURL"),
                "http://diging.asu.edu/citesphere");
        FieldSetter.setField(tokenValidatorImpl,
                tokenValidatorImpl.getClass().getDeclaredField("citesphereCheckTokenEndpoint"),
                "/api/oauth/check_token");
        FieldSetter.setField(tokenValidatorImpl,
                tokenValidatorImpl.getClass().getDeclaredField("citesphereTokenEndpoint"), "/api/oauth/token");
        FieldSetter.setField(tokenValidatorImpl, tokenValidatorImpl.getClass().getDeclaredField("citesphereClientId"),
                citesphereClientId);
        FieldSetter.setField(tokenValidatorImpl,
                tokenValidatorImpl.getClass().getDeclaredField("citesphereClientSecret"), citesphereClientSecret);
        FieldSetter.setField(tokenValidatorImpl, tokenValidatorImpl.getClass().getDeclaredField("citesphereScopes"),
                "read");
        
        MockitoAnnotations.initMocks(this);
        
        PowerMockito.mockStatic(TokenResponse.class);
    }

    @Test
    public void test_validateToken_success() throws TokenInfoNotFoundException {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setActive(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Mockito.when(restTemplate.postForObject(checkTokenUrl, entity, TokenInfo.class, new Object[] {}))
                .thenReturn(tokenInfo);

        boolean isTokenActive = tokenValidatorImpl.validateToken(token);

        Assert.assertTrue(isTokenActive);
    }

    @Test
    public void test_validateToken_unauth1_auth2_success() throws TokenInfoNotFoundException, URISyntaxException, ParseException, IOException {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setActive(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        AuthorizationGrant clientGrant = new ClientCredentialsGrant();
        ClientAuthentication clientAuth = new ClientSecretBasic(new ClientID(citesphereClientId), new Secret(citesphereClientSecret));
        Scope scope = new Scope("read");
        URI tokenEndpoint = new URI("http://diging.asu.edu/citesphere/api/oauth/token");
        TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, clientGrant, scope);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(new Tokens(new BearerAccessToken(accessToken), null));
        
        PowerMockito.when(TokenResponse.parse(request.toHTTPRequest().send())).thenReturn(accessTokenResponse);
        
        Mockito.when(restTemplate.postForObject(checkTokenUrl, entity, TokenInfo.class, new Object[] {}))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED))
                .thenReturn(tokenInfo);

        boolean isTokenActive = tokenValidatorImpl.validateToken(token);

        Assert.assertTrue(isTokenActive);
    }

}
