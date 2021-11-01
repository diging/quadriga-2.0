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
import edu.asu.diging.quadriga.core.exceptions.MappedCollectionNotFoundException;
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
    public void test_addMappedCollection_success() throws InvalidObjectIdException, CollectionNotFoundException {
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
    public void test_addMappedCollection_invalidObjectId()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(collectionManager.findCollection("xxx")).thenThrow(InvalidObjectIdException.class);
        Assert.assertThrows(InvalidObjectIdException.class,
                () -> mappedCollectionServiceImpl.addMappedCollection("xxx"));
    }

    @Test
    public void test_addMappedCollection_collectionNotFound()
            throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(null);
        Assert.assertThrows(CollectionNotFoundException.class,
                () -> mappedCollectionServiceImpl.addMappedCollection(objectId.toString()));
    }

    @Test
    public void test_findMappedCollectionByCollectionId_success()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Collection collection = new Collection();
        ObjectId objectId = new ObjectId();
        collection.setId(objectId);

        String name = "mappedCollection1";
        MappedCollection mappedCollection = new MappedCollection();
        mappedCollection.setCollectionId(objectId);
        mappedCollection.setName(name);

        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(collection);
        Mockito.when(mappedCollectionRepository.findByCollectionId(objectId)).thenReturn(Optional.of(mappedCollection));

        MappedCollection foundMappedCollection = mappedCollectionServiceImpl
                .findMappedCollectionByCollectionId(objectId.toString());

        Assert.assertEquals(mappedCollection.getCollectionId(), foundMappedCollection.getCollectionId());
        Assert.assertEquals(name, foundMappedCollection.getName());
    }

    @Test
    public void test_findMappedCollectionByCollectionId_invalidObjectId()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(collectionManager.findCollection("xxx")).thenThrow(InvalidObjectIdException.class);
        Assert.assertThrows(InvalidObjectIdException.class,
                () -> mappedCollectionServiceImpl.findMappedCollectionByCollectionId("xxx"));
    }

    @Test
    public void test_findMappedCollectionByCollectionId_collectionNotFound()
            throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(null);
        Assert.assertThrows(CollectionNotFoundException.class,
                () -> mappedCollectionServiceImpl.findMappedCollectionByCollectionId(objectId.toString()));
    }

    @Test
    public void test_findMappedCollectionById_success() throws InvalidObjectIdException {
        ObjectId objectId = new ObjectId();
        MappedCollection mappedCollection = new MappedCollection();
        mappedCollection.set_id(objectId);
        Mockito.when(mappedCollectionRepository.findById(objectId)).thenReturn(Optional.of(mappedCollection));
        MappedCollection foundCollection = mappedCollectionServiceImpl.findMappedCollectionById(objectId.toString());
        Assert.assertEquals(mappedCollection.get_id(), foundCollection.get_id());
    }

    @Test
    public void test_findMappedCollectionById_invalidObjectId() throws InvalidObjectIdException {
        Assert.assertThrows(InvalidObjectIdException.class,
                () -> mappedCollectionServiceImpl.findMappedCollectionById("xxx"));
    }

    @Test
    public void test_updateMappedCollectionNameById_success() throws InvalidObjectIdException, MappedCollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        MappedCollection mappedCollectionOld = new MappedCollection();
        mappedCollectionOld.set_id(objectId);
        mappedCollectionOld.setName("mappedCollection_old");

        Mockito.when(mappedCollectionRepository.findById(objectId)).thenReturn(Optional.of(mappedCollectionOld));

        MappedCollection mappedCollectionNew = new MappedCollection();
        mappedCollectionNew.set_id(objectId);
        String updatedName = "mappedCollection_new";
        mappedCollectionNew.setName(updatedName);

        Mockito.when(mappedCollectionRepository.save(Mockito.argThat(new ArgumentMatcher<MappedCollection>() {
            @Override
            public boolean matches(MappedCollection mappedCollection) {
                return mappedCollection.get_id().equals(objectId) && mappedCollection.getName().equals(updatedName);
            }
        }))).thenReturn(mappedCollectionNew);
        
        MappedCollection mappedCollectionUpdated = mappedCollectionServiceImpl.updateMappedCollectionNameById(objectId.toString(), updatedName);
        Assert.assertEquals(updatedName, mappedCollectionUpdated.getName());
        Assert.assertEquals(objectId, mappedCollectionUpdated.get_id());
    }
    
    @Test
    public void test_updateMappedCollectionNameById_missingMappedCollection() {
        ObjectId objectId = new ObjectId();
        Mockito.when(mappedCollectionRepository.findById(objectId)).thenReturn(Optional.ofNullable(null));
        Assert.assertThrows(MappedCollectionNotFoundException.class, () -> mappedCollectionServiceImpl.updateMappedCollectionNameById(objectId.toString(), "XYZ"));
    }
    
    @Test
    public void test_findOrAddMappedCollectionByCollectionId_foundMappedCollection() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId collectionObjectId = new ObjectId();
        Collection collection = new Collection();
        collection.setId(collectionObjectId);
        Mockito.when(collectionManager.findCollection(collectionObjectId.toString())).thenReturn(collection);
        
        ObjectId mappedCollectionObjectId = new ObjectId();
        MappedCollection mappedCollection = new MappedCollection();
        mappedCollection.set_id(mappedCollectionObjectId);
        Mockito.when(mappedCollectionRepository.findByCollectionId(collectionObjectId)).thenReturn(Optional.of(mappedCollection));
        
        MappedCollection foundMappedCollection = mappedCollectionServiceImpl.findOrAddMappedCollectionByCollectionId(collectionObjectId.toString());
        Assert.assertEquals(mappedCollectionObjectId, foundMappedCollection.get_id());
        
    }
    
    @Test
    public void test_findOrAddMappedCollectionByCollectionId_newMappedCollection() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId collectionObjectId = new ObjectId();
        Collection collection = new Collection();
        collection.setId(collectionObjectId);
        Mockito.when(collectionManager.findCollection(collectionObjectId.toString())).thenReturn(collection);
        
        Mockito.when(mappedCollectionRepository.findByCollectionId(collectionObjectId)).thenReturn(Optional.ofNullable(null));
        
        MappedCollection mappedCollection = new MappedCollection();
        mappedCollection.setCollectionId(collectionObjectId);
        
        Mockito.when(mappedCollectionRepository.save(Mockito.argThat(new ArgumentMatcher<MappedCollection>() {

            @Override
            public boolean matches(MappedCollection mappedCollection) {
                return mappedCollection.getCollectionId().equals(collectionObjectId);
            }
        }))).thenReturn(mappedCollection);
        
        MappedCollection foundMappedCollection = mappedCollectionServiceImpl.findOrAddMappedCollectionByCollectionId(collectionObjectId.toString());
        Assert.assertEquals(collectionObjectId, foundMappedCollection.getCollectionId());
        
    }
    
    @Test
    public void test_findOrAddMappedCollectionByCollectionId_addNewMappedCollectionFailed() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId collectionObjectId = new ObjectId();
        Collection collection = new Collection();
        collection.setId(collectionObjectId);
        Mockito.when(collectionManager.findCollection(collectionObjectId.toString())).thenReturn(collection);
        
        Mockito.when(mappedCollectionRepository.findByCollectionId(collectionObjectId)).thenReturn(Optional.ofNullable(null));
        
        MappedCollection mappedCollection = new MappedCollection();
        mappedCollection.setCollectionId(collectionObjectId);
        
        Mockito.when(mappedCollectionRepository.save(Mockito.argThat(new ArgumentMatcher<MappedCollection>() {

            @Override
            public boolean matches(MappedCollection mappedCollection) {
                return mappedCollection.getCollectionId().equals(collectionObjectId);
            }
        }))).thenReturn(null);
        
        Assert.assertNull(mappedCollectionServiceImpl.findOrAddMappedCollectionByCollectionId(collectionObjectId.toString()));
        
    }

}
