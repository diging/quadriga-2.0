package edu.asu.diging.quadriga.legacy.factory.elements.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.elements.Place;
import edu.asu.diging.quadriga.legacy.factory.elements.IPlaceFactory;

/**
 * This is the factory class for Place element. This is used to instantiate
 * Place class.
 * 
 * @author Veena Borannagowda
 * 
 */
@Deprecated
@Service
public class PlaceFactory implements IPlaceFactory {
    @Override
    public Place createPlace() {
        return new Place();
    }

    @Override
    public Place createPlace(String sourceUri) {
        Place placeObject = new Place();
        if (sourceUri == null) {
            placeObject.setSourceURI("");
        } else {
            placeObject.setSourceURI(sourceUri);
        }
        return placeObject;
    }
}