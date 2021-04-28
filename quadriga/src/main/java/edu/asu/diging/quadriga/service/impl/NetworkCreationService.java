package edu.asu.diging.quadriga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.network.model.Network;
import edu.asu.diging.quadriga.service.INetworkCreationService;

@Service
public class NetworkCreationService implements INetworkCreationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Network saveElement(Network element) {
        return mongoTemplate.insert(element);
    }
}
