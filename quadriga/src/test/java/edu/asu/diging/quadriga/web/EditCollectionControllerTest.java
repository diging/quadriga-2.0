package edu.asu.diging.quadriga.web;

import java.util.HashMap;
import java.util.Optional;

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

import edu.asu.diging.quadriga.core.literals.TestLiterals.CollectionLiterals;
import edu.asu.diging.quadriga.core.literals.TestLiterals.URLs;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

public class EditCollectionControllerTest {

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
        collection.setName(CollectionLiterals.COLLECTION_NAME);
        collection.setDescription(CollectionLiterals.COLLECTION_DESC);

        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(collection);

        String view = editCollectionController.get(objectId.toString(), model);

        CollectionForm collectionForm = (CollectionForm) model.getAttribute("collectionForm");

        Assert.assertEquals(URLs.EDIT_COLLECTION, view);
        Assert.assertEquals(objectId.toString(), collectionForm.getId().toString());
        Assert.assertEquals(CollectionLiterals.COLLECTION_NAME, collectionForm.getName());
        Assert.assertEquals(CollectionLiterals.COLLECTION_DESC, collectionForm.getDescription());
    }

    @Test
    public void test_getCollection_noCollectionFound() {
        ObjectId objectId = new ObjectId();
        Model model = new ConcurrentModel();

        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(null);

        String view = editCollectionController.get(objectId.toString(), model);

        Object collectionForm = model.getAttribute("collectionForm");

        Assert.assertEquals(URLs.REDIRECT_SHOW_COLLECTION, view);
        Assert.assertEquals(null, collectionForm);
    }

    @Test
    public void test_editCollection_success() {
        ObjectId objectId = new ObjectId();
        CollectionForm collectionForm = new CollectionForm();
        collectionForm.setId(objectId.toString());
        collectionForm.setName(CollectionLiterals.COLLECTION_NAME);
        collectionForm.setDescription(CollectionLiterals.COLLECTION_DESC);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        BindingResult bindingResult = new MapBindingResult(new HashMap<String, String>(), null);

        Mockito.when(collectionManager.editCollection(objectId.toString(),
                Optional.ofNullable(CollectionLiterals.COLLECTION_NAME),
                Optional.ofNullable(CollectionLiterals.COLLECTION_DESC))).thenReturn(new Collection());

        String view = editCollectionController.edit(objectId.toString(), collectionForm, bindingResult,
                redirectAttributes);

        Assert.assertEquals(URLs.REDIRECT_SHOW_COLLECTION, view);
        Assert.assertEquals("success", redirectAttributes.getFlashAttributes().get("alert_type"));
        Assert.assertEquals("Collection has been edited.", redirectAttributes.getFlashAttributes().get("alert_msg"));
        Assert.assertTrue((boolean) redirectAttributes.getFlashAttributes().get("show_alert"));

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

        Assert.assertEquals(URLs.EDIT_COLLECTION, view);
        Assert.assertEquals("danger", redirectAttributes.getFlashAttributes().get("alert_type"));
        Assert.assertEquals("Something went wrong while editing collections, please try again!",
                redirectAttributes.getFlashAttributes().get("alert_msg"));
        Assert.assertTrue((boolean) redirectAttributes.getFlashAttributes().get("show_alert"));

    }

    @Test
    public void test_editCollection_nullCollectionForm() {
        ObjectId objectId = new ObjectId();
        CollectionForm collectionForm = null;
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        BindingResult bindingResult = new MapBindingResult(new HashMap<String, String>(), null);

        String view = editCollectionController.edit(objectId.toString(), collectionForm, bindingResult,
                redirectAttributes);

        Assert.assertEquals(URLs.EDIT_COLLECTION, view);
        Assert.assertEquals("danger", redirectAttributes.getFlashAttributes().get("alert_type"));
        Assert.assertEquals("Something went wrong while editing collections, please try again!",
                redirectAttributes.getFlashAttributes().get("alert_msg"));
        Assert.assertTrue((boolean) redirectAttributes.getFlashAttributes().get("show_alert"));

    }

    @Test
    public void test_editCollection_nullCollection() {
        ObjectId objectId = new ObjectId();
        CollectionForm collectionForm = new CollectionForm();
        collectionForm.setId(objectId.toString());
        collectionForm.setName(CollectionLiterals.COLLECTION_NAME);
        collectionForm.setDescription(CollectionLiterals.COLLECTION_DESC);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        BindingResult bindingResult = new MapBindingResult(new HashMap<String, String>(), null);

        Mockito.when(collectionManager.editCollection(objectId.toString(),
                Optional.ofNullable(CollectionLiterals.COLLECTION_NAME),
                Optional.ofNullable(CollectionLiterals.COLLECTION_DESC))).thenReturn(null);

        String view = editCollectionController.edit(objectId.toString(), collectionForm, bindingResult,
                redirectAttributes);

        Assert.assertEquals(URLs.EDIT_COLLECTION, view);
        Assert.assertEquals("danger", redirectAttributes.getFlashAttributes().get("alert_type"));
        Assert.assertEquals("Something went wrong while editing collections, please try again!",
                redirectAttributes.getFlashAttributes().get("alert_msg"));
        Assert.assertTrue((boolean) redirectAttributes.getFlashAttributes().get("show_alert"));

    }

}
