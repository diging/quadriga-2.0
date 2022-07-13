package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.Metadata;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.pattern.PatternCreationEvent;

public interface PatternFinder {

    /**
     * Finds and extracts the sub-networks which match the pattern
     * 
     * @param patternMetaData triple metadata for the pattern
     * @param patternRoot     root node of pattern
     * @param eventGraph      network to be searched
     * @return the list of extracted sub-networks
     */
    List<Graph> findGraphsWithPattern(Metadata patternMetaData, PatternCreationEvent patternRoot,
            EventGraph eventGraph);

}
