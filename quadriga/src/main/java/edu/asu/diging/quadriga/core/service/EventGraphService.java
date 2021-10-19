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
    
    /**
     * Finds the number of triples in a particular eventGraph using the ObjectId of an EventGraph
     * 
     * @param id used to find all triples
     * @return the number of triples
     * @throws TriplesNotFoundException if an EventGraph has zero triples
     */
    public int findAllTriplesInEventGraph(ObjectId id) throws TriplesNotFoundException;
}
