package edu.asu.diging.quadriga.core.mongo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.mongo.ICreationEventService;
import edu.asu.diging.quadriga.model.elements.Element;
@Deprecated
@Service
public class CreationEventService implements ICreationEventService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveElements(List<List<Element>> elements) {
        for (List<Element> list : elements)
            for (Element element : list) {
                mongoTemplate.insert(element);
            }
    }

}
