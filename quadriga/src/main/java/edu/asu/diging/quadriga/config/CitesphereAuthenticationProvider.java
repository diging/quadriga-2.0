package edu.asu.diging.quadriga.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereToken;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereUser;

public class CitesphereAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CitesphereConnector citesphereConnector;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        TokenInfo tokenInfo = citesphereConnector.getTokenInfo(token);

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : tokenInfo.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        CitesphereToken citesphereAuth = new CitesphereToken(authorities);
        citesphereAuth
                .setPrincipal(new CitesphereUser(tokenInfo.getUser_name(), tokenInfo.getClient_id(), authorities));
        citesphereAuth.setAuthenticated(true);
        citesphereAuth.setDetails(tokenInfo);

        return citesphereAuth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(CitesphereToken.class);
    }

}
