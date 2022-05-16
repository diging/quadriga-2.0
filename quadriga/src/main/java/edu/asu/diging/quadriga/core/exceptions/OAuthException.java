package edu.asu.diging.quadriga.core.exceptions;

import org.springframework.ldap.AuthenticationException;

public class OAuthException extends AuthenticationException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public OAuthException() {
        super();
    }

    public OAuthException(javax.naming.AuthenticationException cause) {
        super(cause);
    }

}
