package edu.asu.diging.quadriga.core.service.impl;

import java.util.Arrays;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
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
    private CollectionManager managerToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_addCollection_success() {
        
        String name = "name";
        String desc = "description";
        String[] apps = {"app1", "app2"};
        Collection savedCollection = new Collection();
        ObjectId id = new ObjectId();
        savedCollection.setId(id);
        savedCollection.setName(name);
        savedCollection.setDescription(desc);
        savedCollection.setApps(Arrays.asList(apps));
        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(name) && arg0.getDescription().equals(desc));
            }
        }))).thenReturn(savedCollection);
        
        Collection collection = managerToTest.addCollection(name, desc, Arrays.asList(apps));
        Assert.assertEquals(id, collection.getId());
        Assert.assertEquals(name, collection.getName());
        Assert.assertEquals(desc, collection.getDescription());
        for(String app : apps) {
            Assert.assertTrue(collection.getApps().contains(app));
        }
    }

}
