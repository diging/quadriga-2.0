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

import edu.asu.diging.quadriga.core.data.MappedTripleGroupRepository;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.MappedTripleGroupNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.service.CollectionManager;

public class MappedTripleGroupServiceImplTest {

    @Mock
    private MappedTripleGroupRepository mappedTripleGroupRepository;

    @Mock
    private CollectionManager collectionManager;

    @InjectMocks
    private MappedTripleGroupServiceImpl mappedTripleGroupServiceImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_addMappedTripleGroup_success() throws InvalidObjectIdException, CollectionNotFoundException {
        Collection collection = new Collection();
        ObjectId objectId = new ObjectId();
        collection.setId(objectId);

        String name = "mappedTripleGroup1";
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.setCollectionId(objectId);
        mappedTripleGroup.setName(name);
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DefaultMapping);

        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(collection);
        Mockito.when(mappedTripleGroupRepository.save(Mockito.argThat(new ArgumentMatcher<MappedTripleGroup>() {

            @Override
            public boolean matches(MappedTripleGroup argument) {
                return argument.getCollectionId().equals(objectId);
            }
        }))).thenReturn(mappedTripleGroup);

        MappedTripleGroup savedMappedTripleGroup = mappedTripleGroupServiceImpl.addMappedTripleGroup(objectId.toString());

        Assert.assertEquals(mappedTripleGroup.getCollectionId(), savedMappedTripleGroup.getCollectionId());
        Assert.assertEquals(MappedTripleType.DefaultMapping, savedMappedTripleGroup.getMappedTripleType());
        Assert.assertEquals(name, savedMappedTripleGroup.getName());
    }

    @Test
    public void test_addMappedTripleGroup_invalidObjectId()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(collectionManager.findCollection("xxx")).thenThrow(InvalidObjectIdException.class);
        Assert.assertThrows(InvalidObjectIdException.class,
                () -> mappedTripleGroupServiceImpl.addMappedTripleGroup("xxx"));
    }

    @Test
    public void test_addMappedTripleGroup_collectionNotFound()
            throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(null);
        Assert.assertThrows(CollectionNotFoundException.class,
                () -> mappedTripleGroupServiceImpl.addMappedTripleGroup(objectId.toString()));
    }

    @Test
    public void test_findMappedTripleGroupByCollectionId_success()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Collection collection = new Collection();
        ObjectId objectId = new ObjectId();
        collection.setId(objectId);

        String name = "mappedTripleGroup1";
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.setCollectionId(objectId);
        mappedTripleGroup.setName(name);
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DefaultMapping);

        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(collection);
        Mockito.when(mappedTripleGroupRepository.findByCollectionId(objectId)).thenReturn(Optional.of(mappedTripleGroup));

        MappedTripleGroup foundMappedTripleGroup = mappedTripleGroupServiceImpl
                .findMappedTripleGroupByCollectionId(objectId.toString());

        Assert.assertEquals(mappedTripleGroup.getCollectionId(), foundMappedTripleGroup.getCollectionId());
        Assert.assertEquals(MappedTripleType.DefaultMapping, foundMappedTripleGroup.getMappedTripleType());
        Assert.assertEquals(name, foundMappedTripleGroup.getName());
    }

    @Test
    public void test_findMappedTripleGroupByCollectionId_invalidObjectId()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(collectionManager.findCollection("xxx")).thenThrow(InvalidObjectIdException.class);
        Assert.assertThrows(InvalidObjectIdException.class,
                () -> mappedTripleGroupServiceImpl.findMappedTripleGroupByCollectionId("xxx"));
    }

    @Test
    public void test_findMappedTripleGroupByCollectionId_collectionNotFound()
            throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(null);
        Assert.assertThrows(CollectionNotFoundException.class,
                () -> mappedTripleGroupServiceImpl.findMappedTripleGroupByCollectionId(objectId.toString()));
    }

    @Test
    public void test_findMappedTripleGroupById_success() throws InvalidObjectIdException {
        ObjectId objectId = new ObjectId();
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.set_id(objectId);
        Mockito.when(mappedTripleGroupRepository.findById(objectId)).thenReturn(Optional.of(mappedTripleGroup));
        MappedTripleGroup foundCollection = mappedTripleGroupServiceImpl.findMappedTripleGroupById(objectId.toString());
        Assert.assertEquals(mappedTripleGroup.get_id(), foundCollection.get_id());
    }

    @Test
    public void test_findMappedTripleGroupById_invalidObjectId() throws InvalidObjectIdException {
        Assert.assertThrows(InvalidObjectIdException.class,
                () -> mappedTripleGroupServiceImpl.findMappedTripleGroupById("xxx"));
    }

    @Test
    public void test_updateMappedTripleGroupNameById_success() throws InvalidObjectIdException, MappedTripleGroupNotFoundException {
        ObjectId objectId = new ObjectId();
        MappedTripleGroup mappedTripleGroupOld = new MappedTripleGroup();
        mappedTripleGroupOld.set_id(objectId);
        mappedTripleGroupOld.setName("mappedTripleGroup_old");

        Mockito.when(mappedTripleGroupRepository.findById(objectId)).thenReturn(Optional.of(mappedTripleGroupOld));

        MappedTripleGroup mappedTripleGroupNew = new MappedTripleGroup();
        mappedTripleGroupNew.set_id(objectId);
        String updatedName = "mappedTripleGroup_new";
        mappedTripleGroupNew.setName(updatedName);

        Mockito.when(mappedTripleGroupRepository.save(Mockito.argThat(new ArgumentMatcher<MappedTripleGroup>() {
            @Override
            public boolean matches(MappedTripleGroup mappedTripleGroup) {
                return mappedTripleGroup.get_id().equals(objectId) && mappedTripleGroup.getName().equals(updatedName);
            }
        }))).thenReturn(mappedTripleGroupNew);
        
        MappedTripleGroup mappedTripleGroupUpdated = mappedTripleGroupServiceImpl.updateMappedTripleGroupNameById(objectId.toString(), updatedName);
        Assert.assertEquals(updatedName, mappedTripleGroupUpdated.getName());
        Assert.assertEquals(objectId, mappedTripleGroupUpdated.get_id());
    }
    
    @Test
    public void test_updateMappedTripleGroupNameById_missingMappedTripleGroup() {
        ObjectId objectId = new ObjectId();
        Mockito.when(mappedTripleGroupRepository.findById(objectId)).thenReturn(Optional.ofNullable(null));
        Assert.assertThrows(MappedTripleGroupNotFoundException.class, () -> mappedTripleGroupServiceImpl.updateMappedTripleGroupNameById(objectId.toString(), "XYZ"));
    }
    
    @Test
    public void test_findOrAddMappedTripleGroupByCollectionId_foundMappedTripleGroup() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId collectionObjectId = new ObjectId();
        Collection collection = new Collection();
        collection.setId(collectionObjectId);
        Mockito.when(collectionManager.findCollection(collectionObjectId.toString())).thenReturn(collection);
        
        ObjectId mappedTripleGroupObjectId = new ObjectId();
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.set_id(mappedTripleGroupObjectId);
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DefaultMapping);
        
        Mockito.when(mappedTripleGroupRepository.findByCollectionId(collectionObjectId)).thenReturn(Optional.of(mappedTripleGroup));
        
        MappedTripleGroup foundMappedTripleGroup = mappedTripleGroupServiceImpl.findOrAddMappedTripleGroupByCollectionId(collectionObjectId.toString());
        Assert.assertEquals(mappedTripleGroupObjectId, foundMappedTripleGroup.get_id());
        Assert.assertEquals(MappedTripleType.DefaultMapping, foundMappedTripleGroup.getMappedTripleType());   
    }
    
    @Test
    public void test_findOrAddMappedTripleGroupByCollectionId_newMappedTripleGroup() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId collectionObjectId = new ObjectId();
        Collection collection = new Collection();
        collection.setId(collectionObjectId);
        Mockito.when(collectionManager.findCollection(collectionObjectId.toString())).thenReturn(collection);
        
        Mockito.when(mappedTripleGroupRepository.findByCollectionId(collectionObjectId)).thenReturn(Optional.ofNullable(null));
        
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.setCollectionId(collectionObjectId);
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DefaultMapping);
        
        Mockito.when(mappedTripleGroupRepository.save(Mockito.argThat(new ArgumentMatcher<MappedTripleGroup>() {

            @Override
            public boolean matches(MappedTripleGroup mappedTripleGroup) {
                return mappedTripleGroup.getCollectionId().equals(collectionObjectId);
            }
        }))).thenReturn(mappedTripleGroup);
        
        MappedTripleGroup foundMappedTripleGroup = mappedTripleGroupServiceImpl.findOrAddMappedTripleGroupByCollectionId(collectionObjectId.toString());
        Assert.assertEquals(collectionObjectId, foundMappedTripleGroup.getCollectionId());
        Assert.assertEquals(MappedTripleType.DefaultMapping, foundMappedTripleGroup.getMappedTripleType());
        
    }
    
    @Test
    public void test_findOrAddMappedTripleGroupByCollectionId_addNewMappedTripleGroupFailed() throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId collectionObjectId = new ObjectId();
        Collection collection = new Collection();
        collection.setId(collectionObjectId);
        Mockito.when(collectionManager.findCollection(collectionObjectId.toString())).thenReturn(collection);
        
        Mockito.when(mappedTripleGroupRepository.findByCollectionId(collectionObjectId)).thenReturn(Optional.ofNullable(null));
        
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.setCollectionId(collectionObjectId);
        
        Mockito.when(mappedTripleGroupRepository.save(Mockito.argThat(new ArgumentMatcher<MappedTripleGroup>() {

            @Override
            public boolean matches(MappedTripleGroup mappedTripleGroup) {
                return mappedTripleGroup.getCollectionId().equals(collectionObjectId);
            }
        }))).thenReturn(null);
        
        Assert.assertNull(mappedTripleGroupServiceImpl.findOrAddMappedTripleGroupByCollectionId(collectionObjectId.toString()));
        
    }

}
