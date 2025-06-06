package edu.asu.diging.quadriga.core.citesphere.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;

@RunWith(MockitoJUnitRunner.class)
public class CitesphereConnectorImplTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private CitesphereConnectorImpl citesphereConnectorImpl;
    
    private String accessToken;
    private String token;
    private String checkTokenUrl;
    private String citesphereClientId;
    private String citesphereClientSecret;
    
    @Before
    public void setUp() throws NoSuchFieldException, SecurityException {
        accessToken = "SAMPLE_ACCESS_TOKEN";
        token = "SAMPLE_TOKEN";
        checkTokenUrl = "http://xxx.xx/citesphere/api/oauth/check_token?token=SAMPLE_TOKEN";
        citesphereClientId = "SAMPLE_CLIENT_ID";
        citesphereClientSecret = "SAMPLE_CLIENT_SECRET";

        ReflectionTestUtils.setField(citesphereConnectorImpl, "currentAccessToken", accessToken);
        ReflectionTestUtils.setField(citesphereConnectorImpl, "citesphereBaseUrl", "http://xxx.xx/citesphere");
        ReflectionTestUtils.setField(citesphereConnectorImpl, "citesphereCheckTokenEndpoint", "/api/oauth/check_token");
        ReflectionTestUtils.setField(citesphereConnectorImpl, "citesphereTokenEndpoint", "/api/oauth/token");
        ReflectionTestUtils.setField(citesphereConnectorImpl, "citesphereClientId", citesphereClientId);
        ReflectionTestUtils.setField(citesphereConnectorImpl, "citesphereSecret", citesphereClientSecret);
        ReflectionTestUtils.setField(citesphereConnectorImpl, "citesphereScopes", "read");

        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    public void test_getTokenInfo_success() {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setActive(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Mockito.when(restTemplate.postForObject(checkTokenUrl, entity, TokenInfo.class, new Object[] {}))
                .thenReturn(tokenInfo);

        TokenInfo receivedTokenInfo = citesphereConnectorImpl.getTokenInfo(token);

        Assert.assertTrue(receivedTokenInfo.isActive());
    }

    
    @Test
    public void test_getTokenInfo_bad_credentials()
            throws URISyntaxException, ParseException, IOException {
        HttpHeaders headers = new HttpHeaders();

        // We get an exception while validating token but it is not an unauth exception
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity1 = new HttpEntity<String>(headers);

        Mockito.when(restTemplate.postForObject(checkTokenUrl, entity1, TokenInfo.class, new Object[] {}))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        Assert.assertThrows(BadCredentialsException.class, () -> citesphereConnectorImpl.getTokenInfo(token));
    }

    
   

}