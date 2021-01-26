package edu.asu.diging.quadriga.domain.events.factory;

import edu.asu.diging.quadriga.domain.events.CreationEvent;

/**
 * This is the interface class for CreationEventFactory class
 * which has the following methods:
 * createCreationEvent()
 * @author Veena Borannagowda
 *
 */
public interface ICreationEventFactory {

    CreationEvent createCreationEvent();

}