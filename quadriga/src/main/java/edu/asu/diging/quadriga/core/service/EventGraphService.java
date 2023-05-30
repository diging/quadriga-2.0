package edu.asu.diging.quadriga.core.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.asu.diging.quadriga.api.v1.model.Graph;
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
    public EventGraph findLatestEventGraphByCollectionId(ObjectId collectionId);
    
    /**
     * Groups the event graphs mapped to a collection by source uri and returns the total count. 
     * @param collectionId
     * @return total count
     */
    public long getNumberOfSubmittedNetworks(ObjectId collectionId);

    /**
     * Returns all event graphs by collection id
     * @param collectionId
     * @param size 
     * @param page 
     * @return
     */
    public Page<EventGraph>  findAllEventGraphsByCollectionId(ObjectId collectionId, Pageable pageable);
    
    
    public List<EventGraph>  findAllEventGraphsByCollectionId(String collectionId);
    
    /**
     * Maps the network to events and saves it in the database
     * @param graph
     * @param collectionId
     */
    public void mapNetworkAndSave(Graph graph, String collectionId);
}
