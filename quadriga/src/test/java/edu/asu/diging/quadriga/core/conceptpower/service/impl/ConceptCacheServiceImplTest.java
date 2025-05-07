package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.conceptpower.data.ConceptCacheRepository;
import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;

public class ConceptCacheServiceImplTest {

    @Mock
    private ConceptCacheRepository conceptCacheRepository;
    
    @InjectMocks
    private ConceptCacheServiceImpl conceptCacheServiceImpl;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void test_getConceptByAlternativeUri_oneConcept_success() {
        ConceptCache conceptCache1 = new ConceptCache();
        
        String uri = "URI-2";
        String altURI = "URI-1";
        
        conceptCache1.setUri(uri);
        conceptCache1.setAlternativeUris(Collections.singletonList(altURI));
        
        Mockito.when(conceptCacheRepository.findConceptByAlternativeURI(altURI))
                .thenReturn(Collections.singletonList(conceptCache1));
        
        ConceptCache altConceptCache = conceptCacheServiceImpl.getConceptByAlternativeUri(altURI);
        
        Assert.assertEquals(1, altConceptCache.getAlternativeUris().size());
        Assert.assertEquals(altURI, altConceptCache.getAlternativeUris().get(0));
        Assert.assertEquals(uri, altConceptCache.getUri());
        
    }
    
    @Test
    public void test_getConceptByAlternativeUri_twoConcepts_success() {
        ConceptCache conceptCache1 = new ConceptCache();
        ConceptCache conceptCache2 = new ConceptCache();
        
        String uri1 = "URI-2";
        String uri2 = "URI-3";
        String altURI = "URI-1";
        
        conceptCache1.setUri(uri1);
        conceptCache2.setUri(uri2);
        
        conceptCache1.setAlternativeUris(Collections.singletonList(altURI));
        conceptCache2.setAlternativeUris(Collections.singletonList(altURI));
        
        Mockito.when(conceptCacheRepository.findConceptByAlternativeURI(altURI))
                .thenReturn(Arrays.asList(conceptCache1, conceptCache2));
        
        ConceptCache altConceptCache = conceptCacheServiceImpl.getConceptByAlternativeUri(altURI);
        
        Assert.assertEquals(1, altConceptCache.getAlternativeUris().size());
        Assert.assertEquals(altURI, altConceptCache.getAlternativeUris().get(0));
        Assert.assertEquals(uri1, altConceptCache.getUri());
        
    }
    
    @Test
    public void test_getConceptByAlternativeUri_nullConcepts() {
        Mockito.when(conceptCacheRepository.findConceptByAlternativeURI(Mockito.anyString()))
                .thenReturn(null);
        ConceptCache conceptCache = conceptCacheServiceImpl.getConceptByAlternativeUri("uri");
        
        Assert.assertNull(conceptCache);
    }
    
    @Test
    public void test_getConceptByAlternativeUri_emptyConceptList() {
        Mockito.when(conceptCacheRepository.findConceptByAlternativeURI(Mockito.anyString()))
                .thenReturn(new ArrayList<ConceptCache>());
        ConceptCache conceptCache = conceptCacheServiceImpl.getConceptByAlternativeUri("uri");
        
        Assert.assertNull(conceptCache);
    }
    
    @Test
    public void test_getConceptByUri_conceptExists() {
        ConceptCache conceptCache = new ConceptCache();
        String uri = "URI-1";
        conceptCache.setUri(uri);
        
        Mockito.when(conceptCacheRepository.findById(uri)).thenReturn(Optional.of(conceptCache));
        
        ConceptCache foundConceptCache = conceptCacheServiceImpl.getConceptByUri(uri);
        
        Assert.assertEquals(uri, foundConceptCache.getUri());
    }
    
    @Test
    public void test_getConceptByUri_altConceptExists() {
        ConceptCache conceptCache = new ConceptCache();
        String uri = "URI-1";
        conceptCache.setUri(uri);
        
        Mockito.when(conceptCacheRepository.findById(uri)).thenReturn(Optional.ofNullable(null));
        Mockito.when(conceptCacheRepository.findConceptByAlternativeURI(uri)).thenReturn(Collections.singletonList(conceptCache));
        
        ConceptCache foundConceptCache = conceptCacheServiceImpl.getConceptByUri(uri);
        
        Assert.assertEquals(uri, foundConceptCache.getUri());
    }
    
    @Test
    public void test_getConceptByUri_noConceptExists() {
        
        Mockito.when(conceptCacheRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
        Mockito.when(conceptCacheRepository.findConceptByAlternativeURI(Mockito.anyString())).thenReturn(null);
        
        ConceptCache foundConceptCache = conceptCacheServiceImpl.getConceptByUri("URI-1");
        
        Assert.assertNull(foundConceptCache);
    }
    
    @Test
    public void test_getConceptByUri_altConceptEmptyList() {
        
        Mockito.when(conceptCacheRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
        Mockito.when(conceptCacheRepository.findConceptByAlternativeURI(Mockito.anyString())).thenReturn(new ArrayList<>());
        
        ConceptCache foundConceptCache = conceptCacheServiceImpl.getConceptByUri("URI-1");
        
        Assert.assertNull(foundConceptCache);
    }
    
}
