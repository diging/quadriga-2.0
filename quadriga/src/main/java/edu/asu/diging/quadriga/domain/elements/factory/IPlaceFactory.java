package edu.asu.diging.quadriga.domain.elements.factory;

import edu.asu.diging.quadriga.domain.elements.Place;

/**
 * This is the interface class for PlaceFactory class
 * which has the following methods:
 * createPlace()
 * @author Veena Borannagowda
 *
 */
public interface IPlaceFactory {

    Place createPlace();
    Place createPlace(String sourceUri);

}