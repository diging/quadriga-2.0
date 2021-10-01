package edu.asu.diging.quadriga.core.service;

import org.springframework.security.authentication.BadCredentialsException;

import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;

public interface TokenValidator {
       
    public TokenInfo getTokenInfo(String token) throws BadCredentialsException, OAuthException;
    
}
