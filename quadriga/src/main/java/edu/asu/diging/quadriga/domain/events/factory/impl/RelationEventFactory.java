package edu.asu.diging.quadriga.domain.events.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.domain.events.RelationEvent;
import edu.asu.diging.quadriga.domain.events.factory.IRelationEventFactory;

@Service
public class RelationEventFactory implements IRelationEventFactory {

    @Override
    public RelationEvent createRelationEvent() {
        return new RelationEvent();
    }
}