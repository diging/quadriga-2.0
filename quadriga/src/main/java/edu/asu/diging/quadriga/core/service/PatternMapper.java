package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.CreationEventPattern;

public interface PatternMapper {

    public CreationEventPattern mapPattern(GraphPattern graphPattern);
    
}
