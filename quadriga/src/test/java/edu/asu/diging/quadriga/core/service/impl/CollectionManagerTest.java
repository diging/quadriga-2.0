package edu.asu.diging.quadriga.core.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.mongo.CollectionRepository;
import edu.asu.diging.quadriga.domain.elements.Collection;
import edu.asu.diging.quadriga.service.impl.CollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

public class CollectionManagerTest {

    @Mock
    private CollectionRepository collectionRepo;

    @InjectMocks
    private CollectionManager managerToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
    }

    @Test
    public void test_addCollection_success() {
        CollectionForm collectionForm = new CollectionForm();
        String name = "name";
        String description = "testCollection";
        Collection collection = new Collection();
        collection.setName(name);
        collection.setDescription(description);
        collectionForm.setName(name);
        collectionForm.setDescription(description);
        Collection outputCollection = managerToTest.addCollection(collectionForm);
        Mockito.when(collectionRepo.save(collection)).thenReturn(collection);
        Assert.assertEquals(name, outputCollection.getName());
        Assert.assertEquals(description, outputCollection.getDescription());
    }

}
