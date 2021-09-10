package edu.asu.diging.quadriga.core.service.impl;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.RestTemplate;

import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;

public class TokenValidatorImplTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private TokenValidatorImpl tokenValidatorImpl;
    
    @Before
    public void setUp() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);
//        System.setProperty("citesphere_base_url", "CITESPHERE_BASE_URL_VALUE");
//        System.setProperty("citesphere_check_token_endpoint", "CITESPHERE_CHECK_TOKEN_ENDPOINT_VALUE");
//        FieldUtils.writeField(TokenValidatorImpl.class, "citesphereBaseURL", "CITESPHERE_BASE_URL_VALUE", true);
//        FieldUtils.writeField(TokenValidatorImpl.class, "citesphereCheckTokenEndpoint", "CITESPHERE_CHECK_TOKEN_ENDPOINT_VALUE",  true);
    }
    
    @Test
    public void test_validateToken_success() throws OAuthException, BadCredentialsException, TokenExpiredException {
        String token = "sample_token";
        String checkTokenUrl = "nullnull?token=" + token;
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setActive(true);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        
        Mockito.when(restTemplate.postForObject(checkTokenUrl, entity, TokenInfo.class, new Object[] {})).thenReturn(tokenInfo);
        
        boolean isTokenActive = tokenValidatorImpl.validateToken(token);
        
        Assert.assertTrue(isTokenActive);
        
        
    }

}
