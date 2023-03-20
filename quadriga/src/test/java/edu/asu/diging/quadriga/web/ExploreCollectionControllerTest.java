package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.impl.CollectionManagerImpl;
import junit.framework.Assert;

public class ExploreCollectionControllerTest {
    
    
    public static final String COLLECTION_NAME = "Collection name";
    public static final String COLLECTION_DESC = "Collection description";
    public static final List<String> COLLECTION_APPS = new ArrayList<>();
    public static final String EDIT_COLLECTION = "auth/editCollection";
    public static final String REDIRECT_SHOW_COLLECTION = "redirect:/auth/collections";
    public static final String ERROR_PAGE = "error404Page";

    @Mock
    private CollectionManagerImpl collectionManager;

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

        Assert.assertEquals("auth/exploreCollection", result);
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

        Assert.assertEquals("error404Page", result);
        //verify(collectionManager, times(1)).findCollection(collectionId);
    }
    
}
    
    
    
    


