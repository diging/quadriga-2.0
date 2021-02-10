package edu.asu.diging.quadriga.core.mongo;

import java.util.List;

import edu.asu.diging.quadriga.domain.elements.Element;

public interface ElementDao {

    public void saveElements(List<List<Element>> elements);
}
