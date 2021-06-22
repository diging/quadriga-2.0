package edu.asu.diging.quadriga.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.asu.diging.quadriga.network.model.Network;
import edu.asu.diging.quadriga.network.model.Quadruple;

public interface IRepositoryManager {

	Quadruple processJsonAndStoreInDb(Network json) throws JsonMappingException, JsonProcessingException;

}
