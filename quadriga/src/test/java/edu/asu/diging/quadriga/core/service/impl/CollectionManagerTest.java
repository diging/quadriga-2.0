package edu.asu.diging.quadriga.core.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.mongo.CollectionRepository;
import edu.asu.diging.quadriga.domain.elements.Collection;
import edu.asu.diging.quadriga.service.impl.CollectionManager;

public class CollectionManagerTest {

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
		Collection collection1 = new Collection();
		collection1.setName("name");
		collection1.setDescription("testCollection");
		Mockito.when(collectionRepo.save(collection1)).thenReturn(collection1);
		Collection collection=managerToTest.addCollection(collection1);
		Mockito.verify(collectionRepo).save(collection);
		Assert.assertNotNull(collection);

	}

}
