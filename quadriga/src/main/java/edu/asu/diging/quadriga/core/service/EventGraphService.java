package edu.asu.diging.quadriga.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;

public interface EventGraphService {

    /**
     * Saves the given list of eventGraphs to the database
     * 
     * @param graphs to be persisted
     */
    public void saveEventGraphs(List<EventGraph> graphs);
    
    /**
     * Finds all eventGraphs mapped to a collection in the descending order of creation time
     * 
     * @param collectionId is the id used to finds all eventGraphs
     * @return a list of eventGraphs in descending order
     * @throws InvalidObjectIdException if the collectionId contains non-hexadecimal characters
     */
    public List<EventGraph> findAllEventGraphsByCollectionId(ObjectId collectionId);

	public long groupEventGraphsBySourceUri(ObjectId collectionId);
}
