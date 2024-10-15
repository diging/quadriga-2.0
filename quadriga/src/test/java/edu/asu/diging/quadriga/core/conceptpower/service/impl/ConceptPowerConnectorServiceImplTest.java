package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptEntry;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;

@RunWith(MockitoJUnitRunner.class)
public class ConceptPowerConnectorServiceImplTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private ConceptPowerConnectorServiceImpl conceptPowerConnectorServiceImpl;
    
    private String conceptPowerBaseUrl;
    
    private String conceptPowerIdUrl;
    
    private String conceptPowerURL;
    
    @Before
    public void setUp() {
        conceptPowerBaseUrl = "https://chps.asu.edu/conceptpower";
        conceptPowerIdUrl = "/rest/Concept?id={concept_uri}";
        conceptPowerURL = conceptPowerBaseUrl + conceptPowerIdUrl;
        
        ReflectionTestUtils.setField(conceptPowerConnectorServiceImpl, "conceptPowerBaseURL", conceptPowerBaseUrl);
        ReflectionTestUtils.setField(conceptPowerConnectorServiceImpl, "conceptPowerIdURL", conceptPowerIdUrl);
        
        MockitoAnnotations.openMocks(this);
    }
    
    
    @Test
    public void test_getConceptPowerReply_success() {
        String conceptUri = "http://www.digitalhps.org/concepts/WID-09972010-N-01-cousin";
        String lemma = "cousin";
        
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("concept_uri", conceptUri);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(conceptUri);
        conceptEntry.setLemma(lemma);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ResponseEntity<ConceptPowerReply> responseEntity = new ResponseEntity<>(conceptPowerReply, HttpStatus.ACCEPTED);
        
        Mockito.when(restTemplate.exchange(conceptPowerURL, HttpMethod.GET, httpEntity, ConceptPowerReply.class, pathVariables))
                .thenReturn(responseEntity);
        
        ConceptPowerReply replyFromResponse = conceptPowerConnectorServiceImpl.getConceptPowerReply(conceptUri);
        
        Assert.assertNotNull(replyFromResponse.getConceptEntries());
        Assert.assertEquals(conceptUri, replyFromResponse.getConceptEntries().get(0).getConceptUri());
        Assert.assertEquals(lemma, replyFromResponse.getConceptEntries().get(0).getLemma());
    }
    
    @Test
    public void test_getConceptPowerReply_nullResponse() {
        String conceptUri = "http://www.digitalhps.org/concepts/WID-09972010-N-01-cousin";
        String lemma = "cousin";
        
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("concept_uri", conceptUri);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(conceptUri);
        conceptEntry.setLemma(lemma);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ResponseEntity<ConceptPowerReply> responseEntity = new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        
        Mockito.when(restTemplate.exchange(conceptPowerURL, HttpMethod.GET, httpEntity, ConceptPowerReply.class, pathVariables))
                .thenReturn(responseEntity);
        
        Assert.assertNull(conceptPowerConnectorServiceImpl.getConceptPowerReply(conceptUri));
    }
    
    @Test
    public void test_getConceptPowerReply_restClientException() {
        String conceptUri = "http://www.digitalhps.org/concepts/WID-09972010-N-01-cousin";
        String lemma = "cousin";
        
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("concept_uri", conceptUri);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(conceptUri);
        conceptEntry.setLemma(lemma);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        Mockito.when(restTemplate.exchange(conceptPowerURL, HttpMethod.GET, httpEntity, ConceptPowerReply.class, pathVariables))
                .thenThrow(RestClientException.class);
        
        Assert.assertNull(conceptPowerConnectorServiceImpl.getConceptPowerReply(conceptUri));
    }

}
