package edu.asu.diging.quadriga.legacy.factory.elements.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.elements.Actor;
import edu.asu.diging.quadriga.legacy.factory.elements.IActorFactory;

@Deprecated
@Service
public class ActorFactory implements IActorFactory {
    @Override
    public Actor createActor() {
        return new Actor();
    }

    @Override
    public Actor createActor(String sourceUri) {
        Actor actorObject = new Actor();
        if (sourceUri == null) {
            actorObject.setSourceURI("");
        } else {
            actorObject.setSourceURI(sourceUri);
        }
        return actorObject;
    }

}