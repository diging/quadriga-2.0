package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.core.model.events.pattern.PatternCreationEvent;

public interface PatternMapper {

    /**
     * Transforms the pattern mapping into a traversable network
     * @param patternMapping pattern to be transformed
     * @return the root node of the transformed network
     */
    public PatternCreationEvent mapPattern(PatternMapping patternMapping);
    
}
