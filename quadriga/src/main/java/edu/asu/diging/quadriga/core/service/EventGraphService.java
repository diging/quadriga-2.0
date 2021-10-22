package edu.asu.diging.quadriga.core.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Finds event graph by provided eventGraphId
     * 
     * @param eventGraphId used for searrching eventGraph
     */
    public EventGraph findEventGraphById(String eventGraphId) throws InvalidObjectIdException;
    
    /**
     * Finds all eventGraphs mapped to a collection in the descending order of creation time as per the page number and size
     * 
     * @param collectionId is the id used to finds all eventGraphs
     * @param pageable used to specify page number and size per page
     * @return a list of eventGraphs in descending order
     */
    public Page<EventGraph> findAllEventGraphsByCollectionId(ObjectId collectionId, Pageable pageable);
    
    /**
     * Finds the latest eventGraph mapped to a collection in the descending order of creation time
     * 
     * @param collectionId is the id used to finds the latest eventGraph
     * @return the latest eventGraph
     * @throws InvalidObjectIdException if the collectionId contains non-hexadecimal characters
     */
    public EventGraph findLatestEventGraphByCollectionId(ObjectId collectionId);
}
