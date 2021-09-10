package edu.asu.diging.quadriga.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/config.properties")
public class CitesphereConfig {

    @Value("${_citesphere_client_key}")
    private String citesphereKey;
    
    @Value("${_citesphere_client_secret}")
    private String citesphereSecret;
    
    
}
