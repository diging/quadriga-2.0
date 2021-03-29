package edu.asu.diging.quadriga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.mongo.CollectionDao;
import edu.asu.diging.quadriga.domain.elements.Collection;
import edu.asu.diging.quadriga.service.ICollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

@Service
public class CollectionManager implements ICollectionManager {

    @Autowired
    private CollectionDao collectionDao;

    public void addCollection(CollectionForm collectionForm) {
        Collection collection = new Collection();
        collection.setName(collectionForm.getName());
        collection.setDescription(collectionForm.getDescription());
        collectionDao.saveCollection(collection);
    }

}
