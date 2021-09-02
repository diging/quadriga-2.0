package edu.asu.diging.quadriga.core.service.impl;

import java.util.Optional;

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
    private CollectionManagerImpl managerToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test_addCollection_success() {
        
        String name = "name";
        String desc = "description";
        Collection savedCollection = new Collection();
        ObjectId id = new ObjectId();
        savedCollection.setId(id);
        savedCollection.setName(name);
        savedCollection.setDescription(desc);
        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(name) && arg0.getDescription().equals(desc));
            }
        }))).thenReturn(savedCollection);
        
        Collection collection = managerToTest.addCollection(name, desc);
        Assert.assertEquals(id, collection.getId());
        Assert.assertEquals(name, collection.getName());
        Assert.assertEquals(desc, collection.getDescription());
    }
    
    
    @Test
    public void test_deleteCollection_success() {
        
        String name = "name";
        String desc = "description";
        Collection collection = new Collection();
        ObjectId id = new ObjectId();
        collection.setId(id);
        collection.setName(name);
        collection.setDescription(desc);
        
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(collection));
        Mockito.doNothing().when(collectionRepo).delete(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(name) && arg0.getDescription().equals(desc));
            }
        }));
        
        boolean deleted = managerToTest.deleteCollection(id.toString());
        Assert.assertTrue(deleted);
    }
    
    
    @Test
    public void test_deleteCollection_missingCollection() {
        
        String name = "name";
        String desc = "description";
        Collection collection = new Collection();
        ObjectId id = new ObjectId();
        collection.setId(id);
        collection.setName(name);
        collection.setDescription(desc);
        
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.ofNullable(null));
        
        boolean deleted = managerToTest.deleteCollection(id.toString());
        Assert.assertFalse(deleted);
    }

}
