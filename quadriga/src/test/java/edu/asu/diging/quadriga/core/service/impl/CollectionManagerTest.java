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

    private Collection collection1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        collection1 = new Collection();
        collection1.setName("name");
        collection1.setDescription("testCollection");
        Mockito.when(collectionRepo.save(collection1)).thenReturn(collection1);

    }

    @Test
    public void test_addCollection_success() {
        CollectionForm collectionForm = new CollectionForm();
        String name = "name";
        String description = "testCollection";
        collectionForm.setName(name);
        collectionForm.setDescription(description);
        Collection collection = managerToTest.addCollection(collectionForm);
        Mockito.verify(collectionRepo).save(collection);
        Assert.assertEquals(name, collection.getName());
        Assert.assertEquals(description, collection.getDescription());
    }

}
