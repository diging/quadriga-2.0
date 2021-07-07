package edu.asu.diging.quadriga.legacy.factory.events.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.legacy.factory.events.IRelationEventFactory;
@Deprecated
@Service
public class RelationEventFactory implements IRelationEventFactory {

    @Override
    public RelationEvent createRelationEvent() {
        return new RelationEvent();
    }
}