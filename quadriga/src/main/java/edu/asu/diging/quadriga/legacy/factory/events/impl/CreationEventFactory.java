package edu.asu.diging.quadriga.legacy.factory.events.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.legacy.factory.events.ICreationEventFactory;

@Deprecated
@Service
public class CreationEventFactory implements ICreationEventFactory {

    @Override
    public CreationEvent createCreationEvent() {
        return new CreationEvent();
    }
}