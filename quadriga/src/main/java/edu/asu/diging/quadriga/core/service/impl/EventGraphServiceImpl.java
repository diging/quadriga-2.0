package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;

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
}
