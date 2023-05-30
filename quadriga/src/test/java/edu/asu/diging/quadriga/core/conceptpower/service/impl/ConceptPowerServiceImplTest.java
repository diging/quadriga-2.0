package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.time.LocalDateTime;


import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.conceptpower.model.CachedConcept;
import edu.asu.diging.quadriga.core.conceptpower.model.ConceptType;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.AlternativeId;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptEntry;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.Type;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptCacheService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerConnectorService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptTypeService;

public class ConceptPowerServiceImplTest {

    @Mock
    private ConceptCacheService conceptCacheService;
    
    @Mock
    private ConceptTypeService conceptTypeService;
    
    @Mock
    private ConceptPowerConnectorService conceptPowerConnectorService;
    
    @InjectMocks
    private ConceptPowerServiceImpl conceptPowerServiceImpl;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    
    @Test
    public void test_getConceptByUri_noConceptInDB() {
        String sourceURI = "URI-1";
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(null);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(CachedConcept.class));
        
        CachedConcept conceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, conceptCache.getUri());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB() {
        String sourceURI = "URI-1";
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdatedToday_noDiff() {
        String sourceURI = "URI-1";
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now());
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdatedToday_posDiff() {
        String sourceURI = "URI-1";
        String posOld = "NOUN";
        String posNew = "VERB";
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now());
        conceptCache.setPos(posOld);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setPos(posNew);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(posOld, foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_noDiff() {
        String sourceURI = "URI-1";
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        
        LocalDateTime threeDaysBack = LocalDateTime.now().minusDays(3);
        conceptCache.setLastUpdated(threeDaysBack);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(threeDaysBack, foundConceptCache.getLastUpdated());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_posDiff() {
        String sourceURI = "URI-1";
        String posOld = "NOUN";
        String posNew = "VERB";
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now().minusDays(3));
        conceptCache.setPos(posOld);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setPos(posNew);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(CachedConcept.class));
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(posNew, foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_oldPosNullNewPosNotNull() {
        String sourceURI = "URI-1";
        String posOld = null;
        String posNew = "VERB";
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now().minusDays(3));
        conceptCache.setPos(posOld);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setPos(posNew);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(CachedConcept.class));
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(posNew, foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_oldPosNotNullNewPosNull() {
        String sourceURI = "URI-1";
        String posOld = "NOUN";
        String posNew = null;
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now().minusDays(3));
        conceptCache.setPos(posOld);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setPos(posNew);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(CachedConcept.class));
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertNull(foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_oldPosAndNewPosNull() {
        String sourceURI = "URI-1";
        String posOld = null;
        String posNew = null;
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now().minusDays(3));
        conceptCache.setPos(posOld);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setPos(posNew);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(CachedConcept.class));
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertNull(foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_altUrisNoDiff() {
        String sourceURI = "URI-1";
        String altURI = "ALT-URI-1";
        
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now().minusDays(3));
        conceptCache.setAlternativeUris(Arrays.asList(sourceURI, altURI));
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        AlternativeId altURI1 = new AlternativeId();
        AlternativeId altURI2 = new AlternativeId();
        
        altURI1.setConceptUri(sourceURI);
        altURI2.setConceptUri(altURI);
        
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setAlternativeIds(Arrays.asList(altURI1, altURI2));
        
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(sourceURI, foundConceptCache.getAlternativeUris().get(0));
        Assert.assertEquals(altURI, foundConceptCache.getAlternativeUris().get(1));
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_altUrisDiffPresent() {
        String sourceURI = "URI-1";
        String altURI = "ALT-URI-1";
        String altURIDiff = "ALT-URI-2";
        
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now().minusDays(3));
        conceptCache.setAlternativeUris(Arrays.asList(sourceURI, altURI));
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        AlternativeId altURI1 = new AlternativeId();
        AlternativeId altURI2 = new AlternativeId();
        AlternativeId altURI3 = new AlternativeId();
        
        altURI1.setConceptUri(sourceURI);
        altURI2.setConceptUri(altURI);
        altURI3.setConceptUri(altURIDiff);
        
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setAlternativeIds(Arrays.asList(altURI1, altURI2, altURI3));
        
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(sourceURI, foundConceptCache.getAlternativeUris().get(0));
        Assert.assertEquals(altURI, foundConceptCache.getAlternativeUris().get(1));
        Assert.assertEquals(altURIDiff, foundConceptCache.getAlternativeUris().get(2));
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_typeNoDiff() {
        String sourceURI = "URI-1";
        String nameOld = "NAME-1";
        String nameNew = "NAME-1";
        LocalDateTime threeDaysBack = LocalDateTime.now().minusDays(3);
        
        ConceptType conceptType = new ConceptType();
        conceptType.setName(nameOld);
        
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setConceptType(conceptType);
        conceptCache.setLastUpdated(threeDaysBack);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        Type type = new Type();
        type.setTypeName(nameNew);
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setType(type);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(nameNew, foundConceptCache.getConceptType().getName());
        Assert.assertEquals(threeDaysBack, foundConceptCache.getLastUpdated());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_typeNameDiff() {
        String sourceURI = "URI-1";
        String nameOld = "NAME-1";
        String nameNew = "NAME-2";
        
        ConceptType conceptType = new ConceptType();
        conceptType.setName(nameOld);
        
        CachedConcept conceptCache = new CachedConcept();
        conceptCache.setUri(sourceURI);
        conceptCache.setConceptType(conceptType);
        conceptCache.setLastUpdated(LocalDateTime.now().minusDays(3));
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        Type type = new Type();
        type.setTypeName(nameNew);
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setType(type);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        
        CachedConcept foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(nameNew, foundConceptCache.getConceptType().getName());
    }
}
