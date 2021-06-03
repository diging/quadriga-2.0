package edu.asu.diging.quadriga.core.mongo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.mongo.ICreationEventService;
import edu.asu.diging.quadriga.model.elements.Element;
import edu.asu.diging.quadriga.model.events.CreationEvent;

@Service
public class CreationEventService implements ICreationEventService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveCreationEvents(List<CreationEvent> elements) {
        for (CreationEvent event : elements) {
            mongoTemplate.insert(event);
        }
    }
}
