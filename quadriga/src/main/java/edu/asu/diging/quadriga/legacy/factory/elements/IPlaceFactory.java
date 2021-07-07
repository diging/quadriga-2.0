package edu.asu.diging.quadriga.legacy.factory.elements;

import edu.asu.diging.quadriga.core.model.elements.Place;

/**
 * This is the interface class for PlaceFactory class which has the following
 * methods: createPlace()
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
public interface IPlaceFactory {

    Place createPlace();

    Place createPlace(String sourceUri);

}