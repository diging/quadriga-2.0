package edu.asu.diging.quadriga.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import edu.asu.diging.quadriga.config.model.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;

@PropertySource({"${appConfigFile:classpath:}/app.properties" })
public class TokenValidationProvider {
    
    @Value("citesphere_base_url")
    private String citesphereBaseURL;
    
    @Value("citesphere_check_token_endpoint")
    private String citesphereCheckTokenEndpoint;

    private RestTemplate restTemplate;
    
    public TokenValidationProvider() {
        restTemplate = new RestTemplate();
    }

    public boolean authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        
        try {
            return getUserInfo(token).isActive();
        } catch (TokenExpiredException e) {
            throw new OAuthException();
        }
    }

    public TokenInfo getUserInfo(String token) throws TokenExpiredException {
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        
        String checkTokenUrl = citesphereBaseURL + citesphereCheckTokenEndpoint + "?token=" + token;
        
        try {
            return restTemplate.postForObject(checkTokenUrl, entity, TokenInfo.class, new Object[] {});
        } catch(HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new TokenExpiredException(ex);
            }
            throw new BadCredentialsException("Token is invalid for app.", ex);
        }
    }

}
