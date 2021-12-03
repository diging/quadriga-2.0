package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;
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
    
}
