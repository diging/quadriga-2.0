package edu.asu.diging.quadriga.elements.factory;

import edu.asu.diging.quadriga.model.elements.Actor;

@Deprecated
public interface IActorFactory {

    Actor createActor();

    Actor createActor(String sourceUri);

}