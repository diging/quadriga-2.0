package edu.asu.diging.quadriga.domain.events.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.domain.events.AppellationEvent;
import edu.asu.diging.quadriga.domain.events.factory.IAppellationEventFactory;

@Service
public class AppellationEventFactory implements IAppellationEventFactory {
@Override
    public AppellationEvent createAppellationEvent()
    {
        return new AppellationEvent();
    }
}