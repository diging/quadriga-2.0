package edu.asu.diging.quadriga.domain.events.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.events.factory.ICreationEventFactory;
import edu.asu.diging.quadriga.model.events.CreationEvent;

@Service
public class CreationEventFactory implements ICreationEventFactory {

    @Override
    public CreationEvent createCreationEvent() {
        return new CreationEvent();
    }
}