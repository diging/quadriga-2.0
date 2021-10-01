package edu.asu.diging.quadriga.core.service.impl;

import java.util.Objects;
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
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;

public class CollectionManagerImplTest {
    
    public static final String BLANK = "";
    public static final String COLLECTION_NAME = "Collection name";
    public static final String COLLECTION_DESC = "Collection description";
    public static final String EDITED_NAME = "Edited name";
    public static final String EDITED_DESC = "Edited description";
    

    @Mock
    private CollectionRepository collectionRepo;

    @InjectMocks
    private CollectionManagerImpl managerToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

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
    public void test_deleteCollection_success() throws CollectionNotFoundException, InvalidObjectIdException {
        
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
        
        managerToTest.deleteCollection(id.toString());
    }
    
    
    @Test
    public void test_deleteCollection_missingCollection() throws CollectionNotFoundException {
        
        String name = "name";
        String desc = "description";
        Collection collection = new Collection();
        ObjectId id = new ObjectId();
        collection.setId(id);
        collection.setName(name);
        collection.setDescription(desc);
        
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.ofNullable(null));
        
        Assert.assertThrows(CollectionNotFoundException.class,
                ()  -> managerToTest.deleteCollection(id.toString()));
    }

    @Test
    public void test_findCollection_success() throws InvalidObjectIdException {
        Collection collection = new Collection();
        ObjectId objectId = new ObjectId();
        collection.setId(objectId);
        Mockito.when(collectionRepo.findById(objectId)).thenReturn(Optional.of(collection));
        Collection foundCollection = managerToTest.findCollection(objectId.toString());
        Assert.assertEquals(collection.getId().toString(), foundCollection.getId().toString());
    }
    
    @Test
    public void test_findCollection_missingCollection() throws InvalidObjectIdException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionRepo.findById(objectId)).thenReturn(Optional.ofNullable(null));
        Collection foundCollection = managerToTest.findCollection(objectId.toString());
        Assert.assertNull(foundCollection);
    }

    @Test
    public void test_editCollection_success() throws CollectionNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);

        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = EDITED_NAME;
        String editedDescription = EDITED_DESC;

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName);
        editedCollection.setDescription(editedDescription);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(editedName) && arg0.getDescription().equals(editedDescription));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_nullDescription() throws CollectionNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);

        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = COLLECTION_NAME;
        String editedDescription = null;

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName);
        editedCollection.setDescription(editedDescription);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(editedName) && Objects.isNull(arg0.getDescription()));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_nullName() throws CollectionNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);

        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = null;
        String editedDescription = EDITED_DESC;

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName);
        editedCollection.setDescription(editedDescription);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (Objects.isNull(arg0.getName()) && arg0.getDescription().equals(editedDescription));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_nullNameAndDescription() throws CollectionNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);

        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = null;
        String editedDescription = null;

        Collection updatedCollection = new Collection();
        updatedCollection.setId(id);
        updatedCollection.setName(editedName);
        updatedCollection.setDescription(editedDescription);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (Objects.isNull(arg0.getName()) && Objects.isNull(arg0.getDescription()));
            }

        }))).thenReturn(updatedCollection);

        managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_blankNameAndDescription() throws CollectionNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);

        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = BLANK;
        String editedDescription = BLANK;

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName);
        editedCollection.setDescription(editedDescription);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(BLANK) && arg0.getDescription().equals(BLANK));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_noCollectionFoundForId() throws CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        Mockito.when(collectionRepo.findById(objectId)).thenReturn(Optional.ofNullable(null));

        Assert.assertThrows(CollectionNotFoundException.class,
                ()  -> managerToTest.editCollection(objectId.toString(), EDITED_NAME, EDITED_DESC));

    }

}
