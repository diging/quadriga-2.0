package edu.asu.diging.quadriga.core.mongo;

import java.util.List;

import edu.asu.diging.quadriga.model.events.CreationEvent;

public interface ICreationEventService {

    public void saveCreationEvents(List<CreationEvent> elements);
}
