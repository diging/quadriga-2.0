package edu.asu.diging.quadriga.core.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;

import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.TokenInfoNotFoundException;

@RunWith(MockitoJUnitRunner.class)
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
        
        ReflectionTestUtils.setField(tokenValidatorImpl, "accessToken", accessToken);
        ReflectionTestUtils.setField(tokenValidatorImpl, "citesphereBaseURL", "http://diging.asu.edu/citesphere");
        ReflectionTestUtils.setField(tokenValidatorImpl, "citesphereCheckTokenEndpoint", "/api/oauth/check_token");
        ReflectionTestUtils.setField(tokenValidatorImpl, "citesphereTokenEndpoint", "/api/oauth/token");
        ReflectionTestUtils.setField(tokenValidatorImpl, "citesphereClientId", citesphereClientId);
        ReflectionTestUtils.setField(tokenValidatorImpl, "citesphereClientSecret", citesphereClientSecret);
        ReflectionTestUtils.setField(tokenValidatorImpl, "citesphereScopes", "read");
        
        MockitoAnnotations.openMocks(this);
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
        String newAccessToken = "NEW_" + accessToken;
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setActive(true);
        HttpHeaders headers = new HttpHeaders();
        
        // First we will throw unauth error representing expired access token
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity1 = new HttpEntity<String>(headers);
        
        Mockito.when(restTemplate.postForObject(checkTokenUrl, entity1, TokenInfo.class, new Object[] {}))
        .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        
        
        // Then we will receive valid token response using newly generated access token

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(new Tokens(new BearerAccessToken(newAccessToken), null));
        Mockito.mockStatic(TokenResponse.class).when(() -> TokenResponse.parse(Mockito.any(HTTPResponse.class))).thenReturn(accessTokenResponse);
        
        headers.set("Authorization", "Bearer " + newAccessToken);
        HttpEntity<String> entity2 = new HttpEntity<String>(headers);
        
        Mockito.when(restTemplate.postForObject(checkTokenUrl, entity2, TokenInfo.class, new Object[] {})).thenReturn(tokenInfo);
        
        Assert.assertTrue(tokenValidatorImpl.validateToken(token));
    }

}
