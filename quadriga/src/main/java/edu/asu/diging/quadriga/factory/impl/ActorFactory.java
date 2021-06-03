package edu.asu.diging.quadriga.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.elements.factory.IActorFactory;
import edu.asu.diging.quadriga.model.elements.Actor;


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