package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerConnectorService;

@Service
@PropertySource({ "classpath:config.properties" })
public class ConceptPowerConnectorServiceImpl implements ConceptPowerConnectorService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${conceptpower_base_url}")
    private String conceptPowerBaseURL;

    @Value("${conceptpower_id_endpoint}")
    private String conceptPowerIdEndpoint;

    private RestTemplate restTemplate;

    public ConceptPowerConnectorServiceImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public ConceptPowerReply getConceptPowerReply(String conceptURI) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("concept_uri", conceptURI);

        String conceptPowerURL = conceptPowerBaseURL + conceptPowerIdEndpoint;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<ConceptPowerReply> response = restTemplate.exchange(conceptPowerURL, HttpMethod.GET,
                    httpEntity, ConceptPowerReply.class, pathVariables);
            if (response != null) {
                return response.getBody();
            } else {
                logger.error("No response returned from ConceptPower for URI: " + conceptURI);
                if (conceptPowerURL == null || conceptPowerURL.equals("")) {
                    logger.error("ConceptPowerURL was found to be blank or null");
                }
            }
        } catch (RestClientException e) {
            logger.error("Could not get concept for URI: " + conceptURI + " at URL: " + conceptPowerURL, e);
        }

        return null;
    }

}
