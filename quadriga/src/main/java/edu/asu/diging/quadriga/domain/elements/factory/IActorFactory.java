package edu.asu.diging.quadriga.domain.elements.factory;

import edu.asu.diging.quadriga.domain.elements.Actor;

public interface IActorFactory {

    Actor createActor();

    Actor createActor(String sourceUri);
    
}