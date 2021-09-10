package edu.asu.diging.quadriga.core.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.service.TokenValidator;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;

@Service
@PropertySource({ "${appConfigFile:classpath:}/app.properties" })
public class TokenValidatorImpl implements TokenValidator {

    @Value("${citesphere_base_url}")
    private String citesphereBaseURL;

    @Value("${citesphere_check_token_endpoint}")
    private String citesphereCheckTokenEndpoint;

    private RestTemplate restTemplate;

    public TokenValidatorImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public boolean validateToken(String token) throws TokenExpiredException , BadCredentialsException, OAuthException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        TokenInfo tokenInfo = null;

        String checkTokenUrl = citesphereBaseURL + citesphereCheckTokenEndpoint + "?token=" + token;
        
        System.out.println(checkTokenUrl);

        try {
            tokenInfo = restTemplate.postForObject(checkTokenUrl, entity, TokenInfo.class, new Object[] {});
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new TokenExpiredException(ex);
            }
            throw new BadCredentialsException("Token is invalid for app.", ex);
        }
        
        if(Objects.nonNull(tokenInfo)) {
            return tokenInfo.isActive();
        } else {
            throw new OAuthException();
        }
    }

}
