package edu.asu.diging.quadriga.config.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

public class ApiAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, UsernamePasswordAuthenticationToken> {

    private BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
    
    @Override
    public UsernamePasswordAuthenticationToken buildDetails(HttpServletRequest arg0) {
        return authenticationConverter.convert(arg0);
    }

}