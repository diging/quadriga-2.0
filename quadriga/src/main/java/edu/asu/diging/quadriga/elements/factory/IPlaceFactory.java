package edu.asu.diging.quadriga.elements.factory;

import edu.asu.diging.quadriga.model.elements.Place;

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