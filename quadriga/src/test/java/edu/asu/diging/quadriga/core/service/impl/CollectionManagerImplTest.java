package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;


import java.util.List;
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
import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.data.CollectionRepository;
import edu.asu.diging.quadriga.core.exceptions.CitesphereAppNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.UserNotAuthorizedException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

public class CollectionManagerImplTest {
    
    public static final String BLANK = "";
    public static final String COLLECTION_NAME = "Collection name";
    public static final String COLLECTION_DESC = "Collection description";
    public static final List<String> COLLECTION_APPS_1 = new ArrayList<>();
    public static final List<String> COLLECTION_APPS_2 = new ArrayList<>();
    public static final String EDITED_NAME = "Edited name";
    public static final String EDITED_DESC = "Edited description";
    public static final List<Collection> collections = new ArrayList<>();
    public static final Collection collection1 = new Collection();
    public static final Collection collection2 = new Collection();
    public static final List<CitesphereAppInfo> citesphereApps = new ArrayList<>();

    @Mock
    private CollectionRepository collectionRepo;

    @InjectMocks
    private CollectionManagerImpl managerToTest;
    
    @Mock
    private CitesphereConnector citesphereConnector;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        COLLECTION_APPS_1.add("app1");
        COLLECTION_APPS_1.add("app2");
        
        COLLECTION_APPS_2.add("app1");
        COLLECTION_APPS_2.add("app3");

        CitesphereAppInfo app1 = new CitesphereAppInfo();
        app1.setClientId("app1");
        citesphereApps.add(app1);
        
        CitesphereAppInfo app2 = new CitesphereAppInfo();
        app2.setClientId("app2");
        citesphereApps.add(app2);
        
        CitesphereAppInfo app3 = new CitesphereAppInfo();
        app3.setClientId("app3");
        citesphereApps.add(app3);
        
        ObjectId id1 = new ObjectId();
        collection1.setId(id1);
        collection1.setApps(COLLECTION_APPS_1);
        collections.add(collection1);
        
        ObjectId id2 = new ObjectId();
        collection2.setId(id2);
        collection2.setApps(COLLECTION_APPS_2);
        collections.add(collection2);
    }

    @Test
    public void test_addCollection_success() throws CitesphereAppNotFoundException {

        String name = "name";
        String desc = "description";
        String username = "username";
        Collection savedCollection = new Collection();
        ObjectId id = new ObjectId();
        savedCollection.setId(id);
        savedCollection.setName(name);
        savedCollection.setDescription(desc);
        savedCollection.setApps(COLLECTION_APPS_1);
        
        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);
        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(name) && arg0.getDescription().equals(desc));
            }
        }))).thenReturn(savedCollection);
        Collection collection = managerToTest.addCollection(name, desc, username, COLLECTION_APPS_1);
        Assert.assertEquals(id, collection.getId());
        Assert.assertEquals(name, collection.getName());
        Assert.assertEquals(desc, collection.getDescription());
        for(String app : COLLECTION_APPS_1) {
            Assert.assertTrue(collection.getApps().contains(app));
        }
    }
    
    
    @Test
    public void test_deleteCollection_success() throws CollectionNotFoundException, InvalidObjectIdException,UserNotAuthorizedException {
        
        String name = "name";
        String desc = "description";
        Collection collection = new Collection();
        ObjectId id = new ObjectId();
        collection.setId(id);
        collection.setName(name);
        collection.setDescription(desc);
        collection.setOwner(name);
        SimpleUser simpleUser = new SimpleUser();
        simpleUser.setUsername(name);
  
        
        
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(collection));
        Mockito.doNothing().when(collectionRepo).delete(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(name) && arg0.getDescription().equals(desc));
            }
        }));
        
        managerToTest.deleteCollection(id.toString(),simpleUser);
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
        collection.setOwner(name);
        SimpleUser simpleUser = new SimpleUser();
        simpleUser.setUsername(name);
        
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.ofNullable(null));
        
        Assert.assertThrows(CollectionNotFoundException.class,
                ()  -> managerToTest.deleteCollection(id.toString(),simpleUser));
    }
    @Test
    public void test_deleteCollection_UserNotAuthorised() throws UserNotAuthorizedException {
        
        String name = "name";
        String otherName = "OtherName";
        String desc = "description";
        Collection collection = new Collection();
        ObjectId id = new ObjectId();
        collection.setId(id);
        collection.setName(name);
        collection.setDescription(desc);
        collection.setOwner(name);
        SimpleUser simpleUser = new SimpleUser();
        simpleUser.setUsername(name);
        SimpleUser otherUser = new SimpleUser();
        otherUser.setUsername(otherName);
        
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(collection));
        
        Assert.assertThrows(UserNotAuthorizedException.class,
                ()  -> managerToTest.deleteCollection(id.toString(),otherUser));
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
    public void test_getCollections_success() {
        String clientId = "app1";
        
        Mockito.when(collectionRepo.findByAppsContaining(clientId)).thenReturn(collections);
        
        List<Collection> response = managerToTest.getCollections(clientId);
        
        for(Collection collection : response) {
            Assert.assertTrue(collection.getApps().contains(clientId));
        }
    }
    
    @Test
    public void test_getCollections_empty() {
        String clientId = "app5";
        
        Mockito.when(collectionRepo.findByAppsContaining(clientId)).thenReturn(new ArrayList<>());
        
        List<Collection> response = managerToTest.getCollections(clientId);
        
        Assert.assertEquals(0, response.size());
    }

    @Test
    public void test_editCollection_success() throws CollectionNotFoundException, CitesphereAppNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);
        existingCollection.setApps(COLLECTION_APPS_1);

        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = EDITED_NAME;
        String editedDescription = EDITED_DESC;
        List<String> editedApps = new ArrayList<>(COLLECTION_APPS_2);

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName);
        editedCollection.setDescription(editedDescription);
        editedCollection.setApps(editedApps);

        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);
        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(editedName) && arg0.getDescription().equals(editedDescription));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription, editedApps);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());

        for(String app : updatedCollection.getApps()) {
            Assert.assertTrue(COLLECTION_APPS_2.contains(app));
        }
    }

    @Test
    public void test_editCollection_nullDescription() throws CollectionNotFoundException, CitesphereAppNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);
        existingCollection.setApps(COLLECTION_APPS_1);

        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = COLLECTION_NAME;
        String editedDescription = null;
        List<String> editedApps = new ArrayList<>(COLLECTION_APPS_2);

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName);
        editedCollection.setDescription(editedDescription);
        editedCollection.setApps(editedApps);

        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);
        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(editedName) && Objects.isNull(arg0.getDescription()));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription, editedApps);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());
        for(String app : updatedCollection.getApps()) {
            Assert.assertTrue(COLLECTION_APPS_2.contains(app));
        }
    }

    @Test
    public void test_editCollection_nullName() throws CollectionNotFoundException, CitesphereAppNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);
        existingCollection.setApps(COLLECTION_APPS_1);

        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = null;
        String editedDescription = EDITED_DESC;
        List<String> editedApps = new ArrayList<>(COLLECTION_APPS_2);

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName);
        editedCollection.setDescription(editedDescription);
        editedCollection.setApps(editedApps);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (Objects.isNull(arg0.getName()) && arg0.getDescription().equals(editedDescription));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription, editedApps);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());
        for(String app : updatedCollection.getApps()) {
            Assert.assertTrue(COLLECTION_APPS_2.contains(app));
        }
    }

    @Test
    public void test_editCollection_nullNameAndDescription() throws CollectionNotFoundException, CitesphereAppNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);
        existingCollection.setApps(COLLECTION_APPS_1);

        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = null;
        String editedDescription = null;
        List<String> editedApps = new ArrayList<>(COLLECTION_APPS_2);

        Collection updatedCollection = new Collection();
        updatedCollection.setId(id);
        updatedCollection.setName(editedName);
        updatedCollection.setDescription(editedDescription);
        updatedCollection.setApps(editedApps);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (Objects.isNull(arg0.getName()) && Objects.isNull(arg0.getDescription()));
            }

        }))).thenReturn(updatedCollection);


        updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription, editedApps);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());
        for(String app : updatedCollection.getApps()) {
            Assert.assertTrue(COLLECTION_APPS_2.contains(app));
        }
    }

    @Test
    public void test_editCollection_blankNameAndDescription() throws CollectionNotFoundException, CitesphereAppNotFoundException, InvalidObjectIdException {

        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);
        existingCollection.setApps(COLLECTION_APPS_1);

        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);
        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        String editedName = BLANK;
        String editedDescription = BLANK;
        List<String> editedApps = new ArrayList<>(COLLECTION_APPS_2);

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName);
        editedCollection.setDescription(editedDescription);
        editedCollection.setApps(editedApps);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(BLANK) && arg0.getDescription().equals(BLANK));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription, editedApps);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName, updatedCollection.getName());
        Assert.assertEquals(editedDescription, updatedCollection.getDescription());
        for(String app : updatedCollection.getApps()) {
            Assert.assertTrue(COLLECTION_APPS_2.contains(app));
        }
    }

    @Test
    public void test_editCollection_noCollectionFoundForId() throws CollectionNotFoundException {
        ObjectId objectId = new ObjectId();
        
        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);
        Mockito.when(collectionRepo.findById(objectId)).thenReturn(Optional.ofNullable(null));

        Assert.assertThrows(CollectionNotFoundException.class,
                ()  -> managerToTest.editCollection(objectId.toString(), EDITED_NAME, EDITED_DESC, COLLECTION_APPS_2));

    }
    
    @Test(expected = CitesphereAppNotFoundException.class)
    public void test_editCollection_noAppFoundForClientId() throws CollectionNotFoundException, InvalidObjectIdException, CitesphereAppNotFoundException {
        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(COLLECTION_NAME);
        existingCollection.setDescription(COLLECTION_DESC);
        existingCollection.setApps(COLLECTION_APPS_1);

        Mockito.when(collectionRepo.findById(id)).thenReturn(Optional.of(existingCollection));

        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(new ArrayList<>());

        managerToTest.editCollection(id.toString(), EDITED_NAME, EDITED_DESC, new ArrayList<>(COLLECTION_APPS_2));
    }

}
