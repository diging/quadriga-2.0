package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;

public interface TokenValidator {
       
    public boolean validateToken(String token) throws TokenExpiredException;
    
}
