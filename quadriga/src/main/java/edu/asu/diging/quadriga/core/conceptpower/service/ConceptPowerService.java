package edu.asu.diging.quadriga.core.conceptpower.service;

import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;

public interface ConceptPowerService {

	public ConceptPowerReply getConceptByUri(String uri);
	
}
