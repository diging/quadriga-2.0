package edu.asu.diging.quadriga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.mongo.NetworkRepository;
import edu.asu.diging.quadriga.network.model.Network;
import edu.asu.diging.quadriga.service.INetworkService;

@Service
public class NetworkService implements INetworkService {

    @Autowired
    private NetworkRepository repo;

    @Override
    public Network saveElement(Network element) {
        return repo.insert(element);
    }
}
