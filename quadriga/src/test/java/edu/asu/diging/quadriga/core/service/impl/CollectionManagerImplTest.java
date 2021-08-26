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
import edu.asu.diging.quadriga.core.literals.TestLiterals;
import edu.asu.diging.quadriga.core.literals.TestLiterals.CollectionLiterals;
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
    public void test_findCollection_success() {
        Collection collection = new Collection();
        ObjectId objectId = new ObjectId();
        collection.setId(objectId);
        Mockito.when(collectionRepo.findBy_id(objectId)).thenReturn(collection);
        Collection foundCollection = managerToTest.findCollection(objectId.toString());
        Assert.assertEquals(collection.getId().toString(), foundCollection.getId().toString());
    }

    @Test
    public void test_editCollection_success() {

        String name = CollectionLiterals.COLLECTION_NAME;
        String desc = CollectionLiterals.COLLECTION_DESC;
        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(name);
        existingCollection.setDescription(desc);

        Mockito.when(managerToTest.findCollection(id.toString())).thenReturn(existingCollection);

        Optional<String> editedName = Optional.of(CollectionLiterals.EDITED_NAME);
        Optional<String> editedDescription = Optional.of(CollectionLiterals.EDITED_DESC);

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName.get());
        editedCollection.setDescription(editedDescription.get());

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(editedName.get())
                        && arg0.getDescription().equals(editedDescription.get()));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName.get(), updatedCollection.getName());
        Assert.assertEquals(editedDescription.get(), updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_nullName() {

        String name = CollectionLiterals.COLLECTION_NAME;
        String desc = CollectionLiterals.COLLECTION_DESC;
        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(name);
        existingCollection.setDescription(desc);

        Mockito.when(managerToTest.findCollection(id.toString())).thenReturn(existingCollection);

        Optional<String> editedName = Optional.ofNullable(CollectionLiterals.COLLECTION_NAME);
        Optional<String> editedDescription = Optional.ofNullable(null);

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(editedName.get());
        editedCollection.setDescription(desc);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(editedName.get()) && arg0.getDescription().equals(desc));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName.get(), updatedCollection.getName());
        Assert.assertEquals(desc, updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_nullDescription() {

        String name = CollectionLiterals.COLLECTION_NAME;
        String desc = CollectionLiterals.COLLECTION_DESC;
        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(name);
        existingCollection.setDescription(desc);

        Mockito.when(managerToTest.findCollection(id.toString())).thenReturn(existingCollection);

        Optional<String> editedName = Optional.ofNullable(null);
        Optional<String> editedDescription = Optional.ofNullable(CollectionLiterals.EDITED_DESC);

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(name);
        editedCollection.setDescription(editedDescription.get());

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(name) && arg0.getDescription().equals(editedDescription.get()));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(name, updatedCollection.getName());
        Assert.assertEquals(editedDescription.get(), updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_nullNameAndDescription() {

        String name = CollectionLiterals.COLLECTION_NAME;
        String desc = CollectionLiterals.COLLECTION_DESC;
        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(name);
        existingCollection.setDescription(desc);

        Mockito.when(managerToTest.findCollection(id.toString())).thenReturn(existingCollection);

        Optional<String> editedName = Optional.ofNullable(null);
        Optional<String> editedDescription = Optional.ofNullable(null);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(name) && arg0.getDescription().equals(desc));
            }

        }))).thenReturn(existingCollection);

        Collection sameCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), sameCollection.getId().toString());
        Assert.assertEquals(name, sameCollection.getName());
        Assert.assertEquals(desc, sameCollection.getDescription());

    }

    @Test
    public void test_editCollection_blankNameAndDescription() {

        String name = CollectionLiterals.COLLECTION_NAME;
        String desc = CollectionLiterals.COLLECTION_DESC;
        Collection existingCollection = new Collection();
        ObjectId id = new ObjectId();
        existingCollection.setId(id);
        existingCollection.setName(name);
        existingCollection.setDescription(desc);

        Mockito.when(managerToTest.findCollection(id.toString())).thenReturn(existingCollection);

        Optional<String> editedName = Optional.ofNullable(TestLiterals.BLANK);
        Optional<String> editedDescription = Optional.ofNullable(TestLiterals.BLANK);

        Collection editedCollection = new Collection();
        editedCollection.setId(id);
        editedCollection.setName(TestLiterals.BLANK);
        editedCollection.setDescription(TestLiterals.BLANK);

        Mockito.when(collectionRepo.save(Mockito.argThat(new ArgumentMatcher<Collection>() {

            @Override
            public boolean matches(Collection arg0) {
                return (arg0.getName().equals(TestLiterals.BLANK) && arg0.getDescription().equals(TestLiterals.BLANK));
            }

        }))).thenReturn(editedCollection);

        Collection updatedCollection = managerToTest.editCollection(id.toString(), editedName, editedDescription);
        Assert.assertEquals(id.toString(), updatedCollection.getId().toString());
        Assert.assertEquals(editedName.get(), updatedCollection.getName());
        Assert.assertEquals(editedDescription.get(), updatedCollection.getDescription());

    }

    @Test
    public void test_editCollection_noCollectionFoundForId() {
        ObjectId objectId = new ObjectId();
        Mockito.when(managerToTest.findCollection(objectId.toString())).thenReturn(null);

        Optional<String> editedName = Optional.ofNullable(CollectionLiterals.EDITED_NAME);
        Optional<String> editedDescription = Optional.ofNullable(CollectionLiterals.EDITED_DESC);

        Collection updatedCollection = managerToTest.editCollection(objectId.toString(), editedName, editedDescription);
        Assert.assertNull(updatedCollection);

    }

}
