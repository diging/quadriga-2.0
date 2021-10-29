package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.EventGraphRepository;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.EventGraphService;

@Service
public class EventGraphServiceImpl implements EventGraphService {

    @Autowired
    private EventGraphRepository repo;

    @Override
    public void saveEventGraphs(List<EventGraph> events) {
        for (EventGraph event : events) {
            repo.save(event);
        }
    }

    @Override
    public EventGraph findEventGraphById(String eventGraphId) throws InvalidObjectIdException {
        ObjectId eventGraphObjectId;
        try {
            eventGraphObjectId = new ObjectId(eventGraphId);
            return repo.findById(eventGraphObjectId).orElse(null);
        } catch(IllegalArgumentException e) {
            throw new InvalidObjectIdException("EventGraphId " + eventGraphId + " is not a valid ObjectId");
        }
    }

    @Override
    public List<EventGraph> findAllEventGraphsByCollectionId(ObjectId collectionId) {
        return repo.findByCollectionId(collectionId).orElse(null);
    }

    @Override
    public EventGraph findLatestEventGraphByCollectionId(ObjectId collectionId) {
        return repo.findByCollectionIdOrderByCreationTimeDesc(collectionId)
                .map(eventGraphs -> eventGraphs.get(0))
                .orElse(null);
    }
    
    @Override
    public EventGraph findEventGraphBySourceURI(String sourceURI) throws InvalidObjectIdException {
        // Ned to implement this method to get list of EventGraphs as per provided sourceURI
        ObjectId eventGraphObjectId;
        try {
            eventGraphObjectId = new ObjectId(sourceURI);
            return repo.findById(eventGraphObjectId).orElse(null);
        } catch(IllegalArgumentException e) {
            throw new InvalidObjectIdException("EventGraphId " + sourceURI + " is not a valid ObjectId");
        }
    }
    
}
