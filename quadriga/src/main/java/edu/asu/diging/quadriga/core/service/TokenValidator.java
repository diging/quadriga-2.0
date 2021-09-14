package edu.asu.diging.quadriga.core.service;

import org.springframework.security.authentication.BadCredentialsException;

import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.exceptions.TokenInfoNotFoundException;

public interface TokenValidator {
       
    public boolean validateToken(String token) throws BadCredentialsException, OAuthException, TokenInfoNotFoundException;
    
}
