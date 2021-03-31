package edu.asu.diging.quadriga.service;

import edu.asu.diging.quadriga.domain.elements.Collection;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

public interface ICollectionManager {

    public Collection addCollection(CollectionForm collectionForm);
}
