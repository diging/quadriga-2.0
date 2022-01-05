package edu.asu.diging.quadriga.core.conceptpower.service;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerSearchResults;

/**
 * This service is used to get concept data from ConceptPower
 * 
 * @author poojakulkarni
 *
 */
public interface ConceptPowerService {

    /**
     * This method checks if Concept is present in DB If concept is present in DB &
     * updated within last 2 days then it just returns the concept If concept is
     * present in DB & is not updated within last 2 days, it calls conceptpower,
     * gets latest concept data, updates the concept in the database and then
     * returns the concept If concept is not present in DB, it calls conceptpower
     * gets latest concept data, creates an entry for the concept in the database
     * and returns the concept
     * 
     * @param uri used to search entry in database or make a REST call to
     *            conceptpower
     * @return the conceptCache database entry
     */
    public ConceptCache getConceptByUri(String uri);
    
    /**
     * This method maps the ConceptPowerReply object returned from ConceptPower to a
     * ConceptCache object that would be stored in the database
     * 
     * @param conceptPowerReply is the object used to generate a ConceptCache object
     * @return the generated ConceptCache object
     */
    public ConceptCache mapConceptPowerReplyToConceptCache(ConceptPowerReply conceptPowerReply);
    
    /**
     * Searches conceptpower for the given search term
     * @param searchTerm Term to be searched
     * @param page Page number to be retrieved
     * @return the retrieved search response from conceptpower
     */
    public ConceptPowerSearchResults searchConcepts(String searchTerm, int page);

}
