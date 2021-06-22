package edu.asu.diging.quadriga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.mongo.QuadrupleRepository;
import edu.asu.diging.quadriga.network.model.Quadruple;
import edu.asu.diging.quadriga.service.INetworkService;

@Service
public class NetworkService implements INetworkService {

	@Autowired
	private QuadrupleRepository repo;

	@Override
	public Quadruple saveElement(Quadruple quadruple) {
		return repo.save(quadruple);
	}
}
