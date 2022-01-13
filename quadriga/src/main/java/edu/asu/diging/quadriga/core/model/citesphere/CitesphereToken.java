package edu.asu.diging.quadriga.core.model.citesphere;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * The Citesphere Token that represents the authenticated token received by quadriga
 * and the principal user, authorities that were returned as a response from Citesphere
 * 
 * @author poojakulkarni
 *
 */
public class CitesphereToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;
    
    private String token;
    private CitesphereUser principal;
    
    public CitesphereToken(String token) {
        super(null);
        this.token = token;
    }
    
    public CitesphereToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }
    
    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(CitesphereUser principal) {
        this.principal = principal;
    }

}
