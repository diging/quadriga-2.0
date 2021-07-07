package edu.asu.diging.quadriga.legacy.factory.events.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.legacy.factory.events.IAppellationEventFactory;
@Deprecated
@Service
public class AppellationEventFactory implements IAppellationEventFactory {

    @Override
    public AppellationEvent createAppellationEvent() {
        return new AppellationEvent();
    }
}