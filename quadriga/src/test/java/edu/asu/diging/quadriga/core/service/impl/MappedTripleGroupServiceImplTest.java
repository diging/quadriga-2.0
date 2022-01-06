package edu.asu.diging.quadriga.core.service.impl;

import java.util.Arrays;
import java.util.List;
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
        MockitoAnnotations.openMocks(this);
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
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DEFAULT_MAPPING);

        Mockito.when(collectionManager.getCollection(objectId.toString())).thenReturn(collection);
        Mockito.when(mappedTripleGroupRepository.save(Mockito.argThat(new ArgumentMatcher<MappedTripleGroup>() {

            @Override
            public boolean matches(MappedTripleGroup argument) {
                return argument.getCollectionId().equals(objectId);
            }
        }))).thenReturn(mappedTripleGroup);

        MappedTripleGroup savedMappedTripleGroup = mappedTripleGroupServiceImpl.addMappedTripleGroup(objectId.toString(), MappedTripleType.DEFAULT_MAPPING);

        Assert.assertEquals(mappedTripleGroup.getCollectionId(), savedMappedTripleGroup.getCollectionId());
        Assert.assertEquals(MappedTripleType.DEFAULT_MAPPING, savedMappedTripleGroup.getMappedTripleType());
        Assert.assertEquals(name, savedMappedTripleGroup.getName());
    }

    @Test
    public void test_addMappedTripleGroup_invalidObjectId()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(collectionManager.getCollection("xxx")).thenThrow(InvalidObjectIdException.class);
        Assert.assertThrows(InvalidObjectIdException.class,
                () -> mappedTripleGroupServiceImpl.addMappedTripleGroup("xxx", MappedTripleType.DEFAULT_MAPPING));
    }

    @Test
    public void test_addMappedTripleGroup_collectionNotFound()
            throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionManager.getCollection(objectId.toString())).thenThrow(new CollectionNotFoundException());
        Assert.assertThrows(CollectionNotFoundException.class,
                () -> mappedTripleGroupServiceImpl.addMappedTripleGroup(objectId.toString(), MappedTripleType.DEFAULT_MAPPING));
    }

    @Test
    public void test_findDefaultMappedTripleGroupsByCollectionId_success()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Collection collection = new Collection();
        ObjectId objectId = new ObjectId();
        collection.setId(objectId);

        String name1 = "mappedTripleGroup1";
        String name2 = "mappedTripleGroup2";
        
        MappedTripleGroup mappedTripleGroup1 = new MappedTripleGroup();
        mappedTripleGroup1.setCollectionId(objectId);
        mappedTripleGroup1.setName(name1);
        mappedTripleGroup1.setMappedTripleType(MappedTripleType.DefaultMapping);
        
        MappedTripleGroup mappedTripleGroup2 = new MappedTripleGroup();
        mappedTripleGroup2.setCollectionId(objectId);
        mappedTripleGroup2.setName(name2);
        mappedTripleGroup2.setMappedTripleType(MappedTripleType.DefaultMapping);
        
        List<MappedTripleGroup> defaultMappings = Arrays.asList(mappedTripleGroup1, mappedTripleGroup2);

        Mockito.when(collectionManager.findCollection(objectId.toString())).thenReturn(collection);
        Mockito.when(mappedTripleGroupRepository.findByCollectionId(objectId)).thenReturn(Optional.of(defaultMappings));

        List<MappedTripleGroup> founddefaultMappings = mappedTripleGroupServiceImpl
                .findDefaultMappedTripleGroupsByCollectionId(objectId.toString());

        Assert.assertEquals(2, founddefaultMappings.size());
        Assert.assertEquals(mappedTripleGroup1.getCollectionId(), founddefaultMappings.get(0).getCollectionId());
        Assert.assertEquals(MappedTripleType.DefaultMapping, founddefaultMappings.get(0).getMappedTripleType());
        Assert.assertEquals(MappedTripleType.DefaultMapping, founddefaultMappings.get(1).getMappedTripleType());
        Assert.assertEquals(name1, founddefaultMappings.get(0).getName());
        Assert.assertEquals(name2, founddefaultMappings.get(1).getName());
    }

    @Test
    public void test_findMappedTripleGroupByCollectionId_invalidObjectId()
            throws InvalidObjectIdException, CollectionNotFoundException {
        Mockito.when(collectionManager.getCollection("xxx")).thenThrow(InvalidObjectIdException.class);
        Assert.assertThrows(InvalidObjectIdException.class, () -> mappedTripleGroupServiceImpl.findByCollectionIdAndMappingType("xxx", MappedTripleType.DEFAULT_MAPPING));
    }

    @Test
    public void test_findMappedTripleGroupByCollectionId_collectionNotFound()
            throws InvalidObjectIdException, CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionManager.getCollection(objectId.toString())).thenThrow(new CollectionNotFoundException());
        Assert.assertThrows(CollectionNotFoundException.class, () -> mappedTripleGroupServiceImpl.findByCollectionIdAndMappingType(objectId.toString(), MappedTripleType.DEFAULT_MAPPING));
    }

    @Test
    public void test_findMappedTripleGroupById_success() throws InvalidObjectIdException {
        ObjectId objectId = new ObjectId();
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.set_id(objectId);
        Mockito.when(mappedTripleGroupRepository.findById(objectId)).thenReturn(Optional.of(mappedTripleGroup));
        MappedTripleGroup foundCollection = mappedTripleGroupServiceImpl.getById(objectId.toString());
        Assert.assertEquals(mappedTripleGroup.get_id(), foundCollection.get_id());
    }

    @Test
    public void test_findMappedTripleGroupById_invalidObjectId() throws InvalidObjectIdException {
        Assert.assertThrows(InvalidObjectIdException.class,
                () -> mappedTripleGroupServiceImpl.getById("xxx"));
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

}
