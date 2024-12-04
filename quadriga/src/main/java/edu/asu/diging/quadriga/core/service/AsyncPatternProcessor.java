package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.core.exceptions.JobNotFoundException;
import edu.asu.diging.quadriga.core.model.EventGraph;

public interface AsyncPatternProcessor {

    /**
     * Processes pattern matching and creates triples for the qualified networks
     * @param jobId Id of the processing job
     * @param collectionId Id of the collection
     * @param patternMapping holds the pattern mapping details
     * @param networks collection networks to be searched for pattern
     * @throws JobNotFoundException 
     */
    public void processPattern(String jobId, String collectionId, PatternMapping patternMapping, List<EventGraph> networks) throws JobNotFoundException;

}
