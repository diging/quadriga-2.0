package edu.asu.diging.quadriga.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.asu.diging.quadriga.network.model.Network;

public interface IRepositoryManager {

    Network processJsonAndStoreInDb(String json) throws JsonMappingException, JsonProcessingException;

}
