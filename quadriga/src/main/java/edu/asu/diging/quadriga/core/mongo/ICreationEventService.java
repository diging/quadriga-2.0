package edu.asu.diging.quadriga.core.mongo;

import java.util.List;

import edu.asu.diging.quadriga.model.elements.Element;

public interface ICreationEventService {

    public void saveElements(List<List<Element>> elements);
}
