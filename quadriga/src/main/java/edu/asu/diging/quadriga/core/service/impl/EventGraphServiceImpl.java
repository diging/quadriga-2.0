package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.EventGraphRepository;
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
    public List<EventGraph> findEventGraphsBySourceURI(String sourceURI) {
        return repo.findByContextSourceUri(sourceURI).orElse(null);
    }
    
}
