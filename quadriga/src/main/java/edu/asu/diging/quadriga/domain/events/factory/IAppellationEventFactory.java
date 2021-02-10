package edu.asu.diging.quadriga.domain.events.factory;

import edu.asu.diging.quadriga.domain.events.AppellationEvent;

/**
 * This is the interface class for AppellationEventFactory class which has the
 * following methods: createAppellationEvent()
 * 
 * @author Veena Borannagowda
 *
 */
public interface IAppellationEventFactory {

    AppellationEvent createAppellationEvent();

}