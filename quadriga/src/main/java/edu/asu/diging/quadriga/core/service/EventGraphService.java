package edu.asu.diging.quadriga.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.exceptions.TriplesNotFoundException;
import edu.asu.diging.quadriga.core.model.EventGraph;

public interface EventGraphService {

    public void saveEventGraphs(List<EventGraph> graphs);
    public List<EventGraph> findAllEventGraphsByCollectionId(String collectionId);
    public int findAllTriplesInEventGraph(ObjectId id) throws TriplesNotFoundException;
}
