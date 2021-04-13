package edu.asu.diging.quadriga.domain.events.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.events.factory.IRelationEventFactory;
import edu.asu.diging.quadriga.model.events.RelationEvent;
@Deprecated
@Service
public class RelationEventFactory implements IRelationEventFactory {

    @Override
    public RelationEvent createRelationEvent() {
        return new RelationEvent();
    }
}