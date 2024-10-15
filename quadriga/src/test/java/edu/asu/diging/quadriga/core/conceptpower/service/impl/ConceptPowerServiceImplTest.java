package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;
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
    public void test_mapConceptPowerReplyToConceptCache_nullConceptEntries() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply(); 
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertNull(conceptCache);
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_twoConceptEntries() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry1 = new ConceptEntry();
        ConceptEntry conceptEntry2 = new ConceptEntry();
        
        String uri1 = "URI-1";
        String uri2 = "URI-2";
        
        conceptEntry1.setConceptUri(uri1);
        conceptEntry2.setConceptUri(uri2);
        
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry1, conceptEntry2));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        
        Assert.assertEquals(uri1, conceptCache.getUri());
        
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_basePropertiesSuccess() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        String uri = "http://www.digitalhps.org/concepts/WID-09972010-N-01-cousin";
        String description = "the child of your aunt or uncle";
        String pos = "NOUN";
        String conceptList = "Sample conceptList";
        String creatorId = "CreatorId";
        String word = "cousin";
        
        boolean deleted = false;
        conceptEntry.setConceptUri(uri);
        conceptEntry.setDescription(description);
        conceptEntry.setPos(pos);
        conceptEntry.setConceptList(conceptList);
        conceptEntry.setDeleted(deleted);
        conceptEntry.setCreatorId(creatorId);
        conceptEntry.setLemma(word);
        
        String expectedId = "WID-09972010-N-01-cousin";
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        
        Assert.assertEquals(uri, conceptCache.getUri());
        Assert.assertEquals(description, conceptCache.getDescription());
        Assert.assertEquals(expectedId, conceptCache.getId());
        Assert.assertEquals(conceptList, conceptCache.getConceptList());
        Assert.assertFalse(conceptCache.isDeleted());
        Assert.assertEquals(creatorId, conceptCache.getCreatorId());
        Assert.assertEquals(word, conceptCache.getWord());
        
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_nullUri() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertNull(conceptCache.getUri());
        Assert.assertNull(conceptCache.getId());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_blankUri() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        String uri = "";
        conceptEntry.setConceptUri(uri);
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertEquals(uri, conceptCache.getUri());
        Assert.assertNull(conceptCache.getId());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_uriWithoutForwrdaSlash() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        String uri = "URI-1";
        conceptEntry.setConceptUri(uri);
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertEquals(uri, conceptCache.getUri());
        Assert.assertNull(conceptCache.getId());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_nullWordNetIds() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertTrue(conceptCache.getWordNetIds().isEmpty());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_emptyWordNetIds() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setWordnetId("");
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertTrue(conceptCache.getWordNetIds().isEmpty());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_onlyWhitespacesInWordNetIds() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setWordnetId("   ");
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertTrue(conceptCache.getWordNetIds().isEmpty());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_oneWordNetId() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        String wordNet = "wordnet1";
        conceptEntry.setWordnetId(wordNet);
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertArrayEquals(new String[] {wordNet}, conceptCache.getWordNetIds().toArray());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_moreThanOneWordNetId() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        String wordNet1 = "wordnet1";
        String wordNet2 = "wordnet2";
        String wordNet3 = "wordnet3";
        
        conceptEntry.setWordnetId(String.join(",", wordNet1, wordNet2, wordNet3));
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertArrayEquals(new String[] {wordNet1, wordNet2, wordNet3}, conceptCache.getWordNetIds().toArray());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_nullEqualTos() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertTrue(conceptCache.getEqualTo().isEmpty());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_emptyEqualTos() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setEqualTo("");
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertTrue(conceptCache.getEqualTo().isEmpty());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_onlyWhitespacesInEqualTos() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setEqualTo("   ");
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertTrue(conceptCache.getEqualTo().isEmpty());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_oneEqualTo() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        String equalTo = "equalTo";
        conceptEntry.setEqualTo(equalTo);
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertArrayEquals(new String[] {equalTo}, conceptCache.getEqualTo().toArray());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_moreThanOneEqualTos() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        String equalTo1 = "equalTo1";
        String equalTo2 = "equalTo2";
        String equalTo3 = "equalTo3";
        
        conceptEntry.setEqualTo(String.join(",", equalTo1, equalTo2, equalTo3));
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertArrayEquals(new String[] {equalTo1, equalTo2, equalTo3}, conceptCache.getEqualTo().toArray());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_nullAltUris() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertNull(conceptCache.getAlternativeUris());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_emptyAltUris() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setAlternativeIds(new ArrayList<>());
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertNull(conceptCache.getAlternativeUris());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_nullAltConceptUri() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        AlternativeId alternativeId = new AlternativeId();
        conceptEntry.setAlternativeIds(Collections.singletonList(alternativeId));
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertTrue(conceptCache.getAlternativeUris().isEmpty());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_blankAltConceptUri() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        AlternativeId alternativeId = new AlternativeId();
        alternativeId.setConceptUri("");
        conceptEntry.setAlternativeIds(Collections.singletonList(alternativeId));
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertTrue(conceptCache.getAlternativeUris().isEmpty());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_oneAltId() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        AlternativeId alternativeId = new AlternativeId();
        String conceptUri = "URI-1"; 
        alternativeId.setConceptUri(conceptUri);
        
        conceptEntry.setAlternativeIds(Collections.singletonList(alternativeId));
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertArrayEquals(new String[]{conceptUri}, conceptCache.getAlternativeUris().toArray());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_moreThanOneAltId() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        AlternativeId alternativeId1 = new AlternativeId();
        AlternativeId alternativeId2 = new AlternativeId();
        AlternativeId alternativeId3 = new AlternativeId();
        String conceptUri1 = "URI-1";
        String conceptUri2 = "URI-2";
        String conceptUri3 = "URI-3";
        
        alternativeId1.setConceptUri(conceptUri1);
        alternativeId2.setConceptUri(conceptUri2);
        alternativeId3.setConceptUri(conceptUri3);
        
        conceptEntry.setAlternativeIds(Arrays.asList(alternativeId1, alternativeId2, alternativeId3));
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertArrayEquals(new String[]{conceptUri1, conceptUri2, conceptUri3}, conceptCache.getAlternativeUris().toArray());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_conceptTypeNull() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        Assert.assertNull(conceptCache.getConceptType());
        Assert.assertNull(conceptCache.getTypeId());
    }
    
    @Test
    public void test_mapConceptPowerReplyToConceptCache_conceptTypeNotNull() {
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        
        String uri = "URI-1";
        String id = "ID1";
        String name = "Sample Name";
        String expectedDescription = "";
        
        Type type = new Type();
        type.setTypeId(id);
        type.setTypeName(name);
        type.setTypeUri(uri);
        
        conceptEntry.setType(type);
        conceptPowerReply.setConceptEntries(Collections.singletonList(conceptEntry));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.mapConceptPowerReplyToConceptCache(conceptPowerReply);
        
        Assert.assertEquals(uri, conceptCache.getConceptType().getUri());
        Assert.assertEquals(uri, conceptCache.getTypeId());
        Assert.assertEquals(id, conceptCache.getConceptType().getId());
        Assert.assertEquals(name, conceptCache.getConceptType().getName());
        Assert.assertEquals(expectedDescription, conceptCache.getConceptType().getDescription());
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
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(ConceptCache.class));
        
        ConceptCache conceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, conceptCache.getUri());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB() {
        String sourceURI = "URI-1";
        ConceptCache conceptCache = new ConceptCache();
        conceptCache.setUri(sourceURI);
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdatedToday_noDiff() {
        String sourceURI = "URI-1";
        ConceptCache conceptCache = new ConceptCache();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now());
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdatedToday_posDiff() {
        String sourceURI = "URI-1";
        String posOld = "NOUN";
        String posNew = "VERB";
        ConceptCache conceptCache = new ConceptCache();
        conceptCache.setUri(sourceURI);
        conceptCache.setLastUpdated(LocalDateTime.now());
        conceptCache.setPos(posOld);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptEntry.setPos(posNew);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(posOld, foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_noDiff() {
        String sourceURI = "URI-1";
        ConceptCache conceptCache = new ConceptCache();
        conceptCache.setUri(sourceURI);
        
        LocalDateTime threeDaysBack = LocalDateTime.now().minusDays(3);
        conceptCache.setLastUpdated(threeDaysBack);
        
        ConceptPowerReply conceptPowerReply = new ConceptPowerReply();
        ConceptEntry conceptEntry = new ConceptEntry();
        conceptEntry.setConceptUri(sourceURI);
        conceptPowerReply.setConceptEntries(Arrays.asList(conceptEntry));
        
        Mockito.when(conceptCacheService.getConceptByUri(sourceURI)).thenReturn(conceptCache);
        Mockito.when(conceptPowerConnectorService.getConceptPowerReply(sourceURI)).thenReturn(conceptPowerReply);
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(threeDaysBack, foundConceptCache.getLastUpdated());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_posDiff() {
        String sourceURI = "URI-1";
        String posOld = "NOUN";
        String posNew = "VERB";
        ConceptCache conceptCache = new ConceptCache();
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
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(ConceptCache.class));
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(posNew, foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_oldPosNullNewPosNotNull() {
        String sourceURI = "URI-1";
        String posOld = null;
        String posNew = "VERB";
        ConceptCache conceptCache = new ConceptCache();
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
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(ConceptCache.class));
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(posNew, foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_oldPosNotNullNewPosNull() {
        String sourceURI = "URI-1";
        String posOld = "NOUN";
        String posNew = null;
        ConceptCache conceptCache = new ConceptCache();
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
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(ConceptCache.class));
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertNull(foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_oldPosAndNewPosNull() {
        String sourceURI = "URI-1";
        String posOld = null;
        String posNew = null;
        ConceptCache conceptCache = new ConceptCache();
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
        Mockito.doNothing().when(conceptCacheService).saveConceptCache(Mockito.any(ConceptCache.class));
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertNull(foundConceptCache.getPos());
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_altUrisNoDiff() {
        String sourceURI = "URI-1";
        String altURI = "ALT-URI-1";
        
        ConceptCache conceptCache = new ConceptCache();
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
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(sourceURI, foundConceptCache.getAlternativeUris().get(0));
        Assert.assertEquals(altURI, foundConceptCache.getAlternativeUris().get(1));
    }
    
    @Test
    public void test_getConceptByUri_conceptPresentInDB_lastUpdated3DaysBack_altUrisDiffPresent() {
        String sourceURI = "URI-1";
        String altURI = "ALT-URI-1";
        String altURIDiff = "ALT-URI-2";
        
        ConceptCache conceptCache = new ConceptCache();
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
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
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
        
        ConceptCache conceptCache = new ConceptCache();
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
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
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
        
        ConceptCache conceptCache = new ConceptCache();
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
        
        ConceptCache foundConceptCache = conceptPowerServiceImpl.getConceptByUri(sourceURI);
        Assert.assertEquals(sourceURI, foundConceptCache.getUri());
        Assert.assertEquals(nameNew, foundConceptCache.getConceptType().getName());
    }
}
