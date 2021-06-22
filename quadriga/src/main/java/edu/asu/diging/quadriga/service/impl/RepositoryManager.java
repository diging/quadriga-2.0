package edu.asu.diging.quadriga.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.quadriga.network.model.Network;
import edu.asu.diging.quadriga.network.model.Quadruple;
import edu.asu.diging.quadriga.service.INetworkService;
import edu.asu.diging.quadriga.service.IRepositoryManager;

@Service
public class RepositoryManager implements IRepositoryManager {


	@Autowired
	private INetworkService networkService;

	@Override
	public Quadruple processJsonAndStoreInDb(Network json) {

		Quadruple quadruple=new Quadruple();
		quadruple.setSource(json.getDefaultMapping().getSource());
		quadruple.setObject(json.getDefaultMapping().getObject());
		quadruple.setPredicate(json.getDefaultMapping().getPredicate());
		quadruple.setContext(json.getContext());
		return  networkService.saveElement(quadruple);

	}

}
