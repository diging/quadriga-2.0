package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerCacheService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerConnectorService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerService;

@Service
public class ConceptPowerServiceImpl implements ConceptPowerService {
	
	@Autowired
	private ConceptPowerCacheService conceptPowerCacheService;
	
	@Autowired
	private ConceptPowerConnectorService conceptPowerConnectorService;

	@Override
	public ConceptPowerReply getConceptByUri(String uri) {
		// Step 1. Check if Concept is present in DB
		// Step 2: If concept is present in DB & updated within last 2 days return concept
		// Step 3: If concept is present in DB & not updated within last 2 days:
		//			1. Call ConceptPower
		//			2. Update DB
		//			3. Return concept
		// Step 3: If concept is not present in DB
		//			1. Call ConceptPower
		//			2. Create entry in DB
		//			3. Return concept
		return null;
	}

}
