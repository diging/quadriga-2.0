package edu.asu.diging.quadriga.legacy.factory.elements;

import edu.asu.diging.quadriga.core.model.elements.Actor;

@Deprecated
public interface IActorFactory {

    Actor createActor();

    Actor createActor(String sourceUri);

}