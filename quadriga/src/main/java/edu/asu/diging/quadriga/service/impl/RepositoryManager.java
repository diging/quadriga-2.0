package edu.asu.diging.quadriga.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.quadriga.network.model.Network;
import edu.asu.diging.quadriga.service.IRepositoryManager;
@Deprecated
@Service
public class RepositoryManager implements IRepositoryManager {


    //@Autowired
   // private ICreationEventService elementDao;

    @Override
    public Network processJsonAndStoreInDb(String json) throws JsonMappingException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        
        Network network = mapper.readValue(json, Network.class);
       // elementDao.saveElements(network);

        return network;

    }

}
