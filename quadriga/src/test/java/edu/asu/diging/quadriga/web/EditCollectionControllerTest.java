package edu.asu.diging.quadriga.web;

import java.util.HashMap;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

public class EditCollectionControllerTest {
    
    public static final String COLLECTION_NAME = "Collection name";
    public static final String COLLECTION_DESC = "Collection description";
    public static final String EDIT_COLLECTION = "auth/editCollection";
    public static final String REDIRECT_SHOW_COLLECTION = "redirect:/auth/collections";
    public static final String ERROR_PAGE = "error404Page";

    @Mock
    private CollectionManager collectionManager;

    @InjectMocks
    private EditCollectionController editCollectionController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test_getCollection_success() {
        ObjectId objectId = new ObjectId();
        Model model = new ConcurrentModel();
        Collection collection = new Collection();
        collection.setId(objectId);
        collection.setName(COLLECTION_NAME);
        collection.setDescription(COLLECTION_DESC);

        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(collection);

        String view = editCollectionController.get(objectId.toString(), model);

        CollectionForm collectionForm = (CollectionForm) model.getAttribute("collectionForm");

        Assert.assertEquals(EDIT_COLLECTION, view);
        Assert.assertEquals(objectId.toString(), collectionForm.getId().toString());
        Assert.assertEquals(COLLECTION_NAME, collectionForm.getName());
        Assert.assertEquals(COLLECTION_DESC, collectionForm.getDescription());
    }

    @Test
    public void test_getCollection_noCollectionFound() {
        ObjectId objectId = new ObjectId();
        Model model = new ConcurrentModel();

        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(null);

        String view = editCollectionController.get(objectId.toString(), model);

        Object collectionForm = model.getAttribute("collectionForm");

        Assert.assertEquals(ERROR_PAGE, view);
        Assert.assertEquals(null, collectionForm);
    }

    @Test
    public void test_editCollection_success() throws CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        CollectionForm collectionForm = new CollectionForm();
        collectionForm.setId(objectId.toString());
        collectionForm.setName(COLLECTION_NAME);
        collectionForm.setDescription(COLLECTION_DESC);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        BindingResult bindingResult = new MapBindingResult(new HashMap<String, String>(), null);

        Mockito.when(collectionManager.editCollection(objectId.toString(), COLLECTION_NAME, COLLECTION_DESC)).thenReturn(new Collection());

        String view = editCollectionController.edit(objectId.toString(), collectionForm, bindingResult,
                redirectAttributes);

        Assert.assertEquals(REDIRECT_SHOW_COLLECTION, view);
        Assert.assertEquals("success", redirectAttributes.getFlashAttributes().get("alert_type"));
        Assert.assertEquals("Collection has been edited.", redirectAttributes.getFlashAttributes().get("alert_msg"));
        Assert.assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("show_alert"));

    }

    @Test
    public void test_editCollection_hasBindingResultErrors() {
        ObjectId objectId = new ObjectId();
        CollectionForm collectionForm = new CollectionForm();
        collectionForm.setId(objectId.toString());
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        BindingResult bindingResult = new MapBindingResult(new HashMap<String, String>(), null);
        bindingResult.addError(new FieldError("name", "input", null));

        String view = editCollectionController.edit(objectId.toString(), collectionForm, bindingResult,
                redirectAttributes);

        Assert.assertEquals(EDIT_COLLECTION, view);

    }

    @Test
    public void test_editCollection_nullCollection() throws CollectionNotFoundException  {
        ObjectId objectId = new ObjectId();
        CollectionForm collectionForm = new CollectionForm();
        collectionForm.setId(objectId.toString());
        collectionForm.setName(COLLECTION_NAME);
        collectionForm.setDescription(COLLECTION_DESC);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        BindingResult bindingResult = new MapBindingResult(new HashMap<String, String>(), null);

        Mockito.when(collectionManager.editCollection(objectId.toString(), COLLECTION_NAME, COLLECTION_DESC))
            .thenThrow(new CollectionNotFoundException("Collection not found!"));

        String view = editCollectionController.edit(objectId.toString(), collectionForm, bindingResult,
                redirectAttributes);

        Assert.assertEquals(ERROR_PAGE, view);

    }

}