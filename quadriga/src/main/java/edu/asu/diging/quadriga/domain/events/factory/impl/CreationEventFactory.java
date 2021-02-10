package edu.asu.diging.quadriga.domain.events.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.domain.events.CreationEvent;
import edu.asu.diging.quadriga.domain.events.factory.ICreationEventFactory;

@Service
public class CreationEventFactory implements ICreationEventFactory {

    @Override
    public CreationEvent createCreationEvent() {
        return new CreationEvent();
    }
}