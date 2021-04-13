package edu.asu.diging.quadriga.domain.events.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.events.factory.IAppellationEventFactory;
import edu.asu.diging.quadriga.model.events.AppellationEvent;
@Deprecated
@Service
public class AppellationEventFactory implements IAppellationEventFactory {

    @Override
    public AppellationEvent createAppellationEvent() {
        return new AppellationEvent();
    }
}