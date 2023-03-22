package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
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

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptCacheService;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.impl.CollectionManagerImpl;
import edu.asu.diging.quadriga.web.model.GraphElements;


public class ExploreCollectionControllerTest {
    
    
    public static final String COLLECTION_NAME = "Collection name";
    public static final String COLLECTION_DESC = "Collection description";
    public static final List<String> COLLECTION_APPS = new ArrayList<>();
    public static final String EXPLORE_COLLECTION = "/auth/collections/{collectionId}/explore";
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
    

    @InjectMocks
    public ExploreCollectionController exploreCollectionController;
    
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);        
    }
    
    @Test
    public void test_exploreTriples_success() throws InvalidObjectIdException {
        String collectionId = "validId";
        Collection mockCollection = new Collection();
        mockCollection.setName("Test Collection");
        Mockito.when(collectionManager.findCollection(collectionId)).thenReturn(mockCollection);
        Model model = new ConcurrentModel();

        String result = exploreCollectionController.exploreTriples(collectionId, model);
        Assert.assertEquals(EXPLORE_COLLECTION, result);
        Assert.assertEquals("Test Collection", model.getAttribute("collectionName"));
        Assert.assertEquals(collectionId, model.getAttribute("collection"));
        //verify(collectionManager, times(1)).findCollection(collectionId);
        
        
    }
   
    @Test
    public void testInvalidCollectionId() throws InvalidObjectIdException {

        String collectionId = "invalidId";
        Mockito.when(collectionManager.findCollection(collectionId)).thenThrow(InvalidObjectIdException.class);
        Model model = new ConcurrentModel();

        String result = exploreCollectionController.exploreTriples(collectionId, model);

        Assert.assertEquals(ERROR_PAGE, result);
        //verify(collectionManager, times(1)).findCollection(collectionId);
    }
    
    /*@Test
    public void testGetGraphForUriWithValidInput() throws InvalidObjectIdException, CollectionNotFoundException {
        ConceptCache conceptCache = new ConceptCache("uri1", "id1", "type1");
        Mockito.when(conceptCacheService.getConceptByUri("uri1")).thenReturn(conceptCache);
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup("id1", MappedTripleType.DEFAULT_MAPPING);
        Mockito.when(mappedTripleGroupService.findByCollectionIdAndMappingType("collectionId", MappedTripleType.DEFAULT_MAPPING)).thenReturn(mappedTripleGroup);
        List<DefaultMapping> triples = new ArrayList<>();
        DefaultMapping mapping = new DefaultMapping("s1", "p1", "o1");
        triples.add(mapping);
        Mockito.when(mappedTripleService.getTriplesByUri(anyString(), anyString(), anyList())).thenReturn(triples);
        ResponseEntity<GraphElements> response = exploreCollectionController.getGraphForUri("collectionId", "uri1", null);
        Assert.assertEquals(HttpStatus.OK , response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        GraphElements graphElements = response.getBody();
        Assert.assertEquals(1, graphElements.getNodes().size());
        Assert.assertEquals(1, graphElements.getEdges().size());
        Assert.assertEquals("s1", graphElements.getNodes().get(0).getId());
        Assert.assertEquals("p1", graphElements.getEdges().get(0).getLabel());
        Assert.assertEquals("o1", graphElements.getEdges().get(0).getTo());
    }*/
    
    
    @Test(expected = CollectionNotFoundException.class)
    public void testGetGraphForUriWithInvalidCollectionId() throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(mappedTripleGroupService.findByCollectionIdAndMappingType("collectionId", MappedTripleType.DEFAULT_MAPPING)).thenThrow(CollectionNotFoundException.class);
        exploreCollectionController.getGraphForUri("collectionId", "uri1", null);
    }
    @Test(expected = InvalidObjectIdException.class)
    public void testGetGraphForUriWithInvalidUri() throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(conceptCacheService.getConceptByUri("uri1")).thenReturn(null);
        exploreCollectionController.getGraphForUri("collectionId", "uri1", null);
    }    
}
    
    
    
    


