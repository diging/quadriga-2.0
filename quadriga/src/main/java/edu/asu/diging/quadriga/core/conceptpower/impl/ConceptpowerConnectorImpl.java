package edu.asu.diging.quadriga.core.conceptpower.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.asu.diging.quadriga.core.conceptpower.ConceptpowerConnector;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerResponse;

@Service
@PropertySource("classpath:config.properties")
public class ConceptpowerConnectorImpl implements ConceptpowerConnector {

    @Value("${conceptpower_base_url}")
    private String conceptpowerBaseUrl;

    @Value("${conceptpower_concept_url}")
    private String conceptpowerConceptUrl;

    @Value("${conceptpower_search_url}")
    private String conceptpowerSearchUrl;

    private RestTemplate restTemplate;

    public ConceptpowerConnectorImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public ConceptEntry getConceptEntry(String id) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        HttpEntity<?> entity = new HttpEntity<Object>(requestHeaders);
        
        ResponseEntity<ConceptpowerResponse> response = restTemplate.exchange(
                String.format("%s%s%s", conceptpowerBaseUrl, conceptpowerConceptUrl, id), HttpMethod.GET, entity, ConceptpowerResponse.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            ConceptpowerResponse concepts = response.getBody();
            if (concepts.getConceptEntries() != null && !concepts.getConceptEntries().isEmpty()) {
                return concepts.getConceptEntries().get(0);
            }
        }
        return null;
    }

    @Override
    public ConceptpowerResponse findConceptEntryEqualTo(String uri) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        HttpEntity<?> entity = new HttpEntity<Object>(requestHeaders);

        ResponseEntity<ConceptpowerResponse> response = restTemplate.exchange(
                String.format("%s%sequal_to=%s", conceptpowerBaseUrl, conceptpowerSearchUrl, uri), HttpMethod.GET,
                entity, ConceptpowerResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }

    }

}
