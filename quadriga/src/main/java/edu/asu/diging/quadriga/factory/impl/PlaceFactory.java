package edu.asu.diging.quadriga.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.elements.factory.IPlaceFactory;
import edu.asu.diging.quadriga.model.elements.Place;

/**
 * This is the factory class for Place element. This is used to instantiate
 * Place class.
 * 
 * @author Veena Borannagowda
 * 
 */

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