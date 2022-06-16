package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.RelationEventPattern;

public interface PatternMapper {

    public RelationEventPattern mapPattern(GraphPattern graphPattern);
    
}
