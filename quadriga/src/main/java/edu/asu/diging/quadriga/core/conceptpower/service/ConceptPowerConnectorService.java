package edu.asu.diging.quadriga.core.conceptpower.service;

import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;

/**
 * A service that extracts XML data from ConceptPower using REST calls and
 * parses the result into Java objects using JAXB model classes
 * 
 * @author poojakulkarni
 *
 */
public interface ConceptPowerConnectorService {

    public ConceptPowerReply getConceptPowerReply(String conceptURI);
    
    public ConceptPowerReply searchConcepts(String searchTerm, int page);
    
}
