package edu.asu.diging.quadriga.core.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.data.CollectionRepository;
import edu.asu.diging.quadriga.core.model.Collection;

public class CollectionManagerImplTest {

    @Mock
    private CollectionRepository collectionRepo;

    @InjectMocks
    private CollectionManagerImpl managerToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test_addCollection_success() {
        Collection collection1 = new Collection();
        collection1.setName("name");
        collection1.setDescription("testCollection");
        Mockito.when(collectionRepo.save(collection1)).thenReturn(collection1);
        Collection collection = managerToTest.addCollection(collection1.getName(), collection1.getDescription());
        Mockito.verify(collectionRepo).save(collection);
        Assert.assertNotNull(collection);

    }

}
