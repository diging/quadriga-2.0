package edu.asu.diging.quadriga.web;

import java.util.ArrayList;



import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import edu.asu.diging.quadriga.core.conceptpower.model.CachedConcept;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptCacheService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptService;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.impl.CollectionManagerImpl;
import edu.asu.diging.quadriga.web.service.model.GraphElements;


public class ExploreCollectionControllerTest {
    
    
    public static final String COLLECTION_NAME = "Collection name";
    public static final String COLLECTION_DESC = "Collection description";
    public static final List<String> COLLECTION_APPS = new ArrayList<>();
    public static final String EXPLORE_COLLECTION = "auth/exploreCollection";
    public static final String ERROR_PAGE = "error404Page";

    @Mock
    private CollectionManagerImpl collectionManager;
    
    @Mock
    private ConceptCacheService conceptCacheService;
    
    @Mock
    private MappedTripleGroup mappedTrippleGroup;
    
    @Mock
    private MappedTripleGroupService mappedTripleGroupService;
    
    @Mock
    private MappedTripleService mappedTripleService;
    
    @Mock
    private ConceptService conceptService;
    

    @InjectMocks
    public ExploreCollectionController exploreCollectionController;
    
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);        
    }
    
    @Test
    public void test_exploreTriples_success() throws InvalidObjectIdException {
        ObjectId objectId = new ObjectId();
        Collection mockCollection = new Collection();
        mockCollection.setName("Test Collection");
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(mockCollection);
        Model model = new ConcurrentModel();

        String result = exploreCollectionController.exploreTriples(objectId.toString(), model);
        Assert.assertEquals(EXPLORE_COLLECTION, result);
        Assert.assertEquals("Test Collection", model.getAttribute("collectionName"));
        Assert.assertEquals(objectId.toString(), model.getAttribute("collection"));
      
    }
   
    @Test
    public void testInvalidCollectionId() throws InvalidObjectIdException {
       
        String collectionId = "InvalidId";
        Mockito.when(collectionManager.findCollection(collectionId)).thenThrow(InvalidObjectIdException.class);
        Model model = new ConcurrentModel();
        String result = exploreCollectionController.exploreTriples(collectionId, model);
        Assert.assertEquals(ERROR_PAGE, result);
  
    }
    
    @Test
    public void testGetGraphForUriWithValidInput() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        String uri ="https:/uri/";
        CachedConcept conceptCache = new CachedConcept();
        List<String> equalTo = new ArrayList<>();
        equalTo.add(uri);
        conceptCache.setUri(uri);
        conceptCache.setId(objectId.toString());
        conceptCache.setEqualTo(equalTo);
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.set_id(objectId);
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DEFAULT_MAPPING);
        List<DefaultMapping> triples = new ArrayList<>();
        DefaultMapping mapping = new DefaultMapping();
        triples.add(mapping);
        Concept concept = new Concept();
        concept.setUri(uri);
        List<Concept> conceptList = new ArrayList<Concept>();
        conceptList.add(concept);
        
        Mockito.when(conceptCacheService.getConceptByUri(uri)).thenReturn(conceptCache);
        Mockito.when(mappedTripleGroupService.findByCollectionIdAndMappingType(objectId.toString(), MappedTripleType.DEFAULT_MAPPING)).thenReturn(mappedTripleGroup);
        Mockito.when(mappedTripleService.getTriplesByUri(objectId.toString(),uri,null)).thenReturn(triples);
        Mockito.when(conceptService.findByMappedTripleGroupId(objectId.toString())).thenReturn(conceptList);
        ResponseEntity<GraphElements> response = exploreCollectionController.getGraphForUri(objectId.toString(), uri, new ArrayList<>());        
        Assert.assertEquals(org.springframework.http.HttpStatus.OK , response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }
    
    
    @Test()
    public void testGetGraphForUriWithInvalidCollectionId() throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(mappedTripleGroupService.findByCollectionIdAndMappingType("collectionId", MappedTripleType.DEFAULT_MAPPING)).thenThrow(CollectionNotFoundException.class);
        ResponseEntity<GraphElements> response = exploreCollectionController.getGraphForUri("collectionId", "uri1", null);
        Assert.assertEquals(org.springframework.http.HttpStatus.NOT_FOUND,response.getStatusCode());
        
    }
    @Test()
    public void testGetGraphForUriWithInvalidObjectId() throws InvalidObjectIdException, CollectionNotFoundException{
        Mockito.when(mappedTripleGroupService.findByCollectionIdAndMappingType("collectionId",MappedTripleType.DEFAULT_MAPPING )).thenThrow(InvalidObjectIdException.class);
        ResponseEntity<GraphElements> response =  exploreCollectionController.getGraphForUri("collectionId", "InvalidUri",null );
        Assert.assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST,response.getStatusCode());
       
    }    
}
    
    
    
    


