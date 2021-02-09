package edu.asu.diging.quadriga.core.mongo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import edu.asu.diging.quadriga.core.mongo.ElementDao;
import edu.asu.diging.quadriga.domain.elements.Element;

@Service
public class ElementDaoImpl implements ElementDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveElements(List<List<Element>> elements) {
        for (List<Element> list : elements)
            for (Element element : list)
                mongoTemplate.insert(element);
    }

}
