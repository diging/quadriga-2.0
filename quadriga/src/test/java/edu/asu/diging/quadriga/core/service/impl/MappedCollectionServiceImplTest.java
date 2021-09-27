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

import edu.asu.diging.quadriga.core.data.MappedCollectionRepository;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.service.CollectionManager;

public class MappedCollectionServiceImplTest {
    
    @Mock
    private MappedCollectionRepository mappedCollectionRepository;
    
    @Mock
    private CollectionManager collectionManager;
    
    @InjectMocks
    private MappedCollectionServiceImpl mappedCollectionServiceImpl;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void addMappedCollection_success() throws InvalidObjectIdException, CollectionNotFoundException {
        Collection collection = new Collection();
        ObjectId objectId = new ObjectId();
        collection.setId(objectId);
        
        String name = "mappedCollection1";
        MappedCollection mappedCollection = new MappedCollection();
        mappedCollection.setCollectionId(objectId);
        mappedCollection.setName(name);
        
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(collection);
        Mockito.when(mappedCollectionRepository.save(Mockito.argThat(new ArgumentMatcher<MappedCollection>() {

            @Override
            public boolean matches(MappedCollection argument) {
                return argument.getCollectionId().equals(objectId);
            }
        }))).thenReturn(mappedCollection);
        
        MappedCollection savedMappedCollection = mappedCollectionServiceImpl.addMappedCollection(objectId.toString());
        
        Assert.assertEquals(mappedCollection.getCollectionId(), savedMappedCollection.getCollectionId());
        Assert.assertEquals(name, savedMappedCollection.getName());
    }
    
    @Test
    public void addMappedCollection_invalidObjectId() throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(collectionManager.findCollection("xxx")).thenThrow(InvalidObjectIdException.class);
        Assert.assertThrows(InvalidObjectIdException.class, () -> mappedCollectionServiceImpl.addMappedCollection("xxx"));
    }
    
    @Test
    public void addMappedCollection_collectionNotFound() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(null);
        Assert.assertThrows(CollectionNotFoundException.class, () -> mappedCollectionServiceImpl.addMappedCollection(objectId.toString()));
    }
    
    @Test
    public void findMappedCollectionByCollectionId_success() throws InvalidObjectIdException, CollectionNotFoundException {
        Collection collection = new Collection();
        ObjectId objectId = new ObjectId();
        collection.setId(objectId);
        
        String name = "mappedCollection1";
        MappedCollection mappedCollection = new MappedCollection();
        mappedCollection.setCollectionId(objectId);
        mappedCollection.setName(name);
        
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(collection);
        Mockito.when(mappedCollectionRepository.findByCollectionId(objectId)).thenReturn(Optional.of(mappedCollection));
        
        MappedCollection foundMappedCollection = mappedCollectionServiceImpl.findMappedCollectionByCollectionId(objectId.toString());
        
        Assert.assertEquals(mappedCollection.getCollectionId(), foundMappedCollection.getCollectionId());
        Assert.assertEquals(name, foundMappedCollection.getName());
    }
    
    @Test
    public void findMappedCollectionByCollectionId_invalidObjectId() throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(collectionManager.findCollection("xxx")).thenThrow(InvalidObjectIdException.class);
        Assert.assertThrows(InvalidObjectIdException.class, () -> mappedCollectionServiceImpl.findMappedCollectionByCollectionId("xxx"));
    }
    
    @Test
    public void findMappedCollectionByCollectionId_collectionNotFound() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(null);
        Assert.assertThrows(CollectionNotFoundException.class, () -> mappedCollectionServiceImpl.findMappedCollectionByCollectionId(objectId.toString()));
    }

}
