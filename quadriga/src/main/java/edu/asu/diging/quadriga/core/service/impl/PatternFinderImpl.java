package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.pattern.CreationEventPattern;
import edu.asu.diging.quadriga.core.service.PatternFinder;
import edu.asu.diging.quadriga.core.service.PatternMapper;

public class PatternFinderImpl implements PatternFinder {

    @Autowired
    private PatternMapper patternMapper;
    
    @Override
    public List<Graph> findGraphsWithPattern(GraphPattern pattern, EventGraph eventGraph) {
        CreationEventPattern patternRoot = patternMapper.mapPattern(pattern);
        List<Graph> result = new ArrayList<>();
        CreationEvent graphRoot = eventGraph.getRootEvent();
        
        return result;
    }
    
    private Graph extractGraph(CreationEvent graphNode, CreationEventPattern patternNode) {
        
        return null;
    }
    

}
