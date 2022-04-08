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
     * @param pageable used to specify page number and size per page
     * @return a list of eventGraphs in descending order
     */
    public EventGraph findLatestEventGraphByCollectionId(ObjectId collectionId);
    
    /**
     * Groups the event graphs mapped to a collection by source uri and returns the total count. 
     * @param collectionId
     * @return total count
     */
    public long getNumberOfSubmittedNetworks(ObjectId collectionId);

    public List<EventGraph>  findAllEventGraphsByCollectionId(ObjectId collectionId);
}
