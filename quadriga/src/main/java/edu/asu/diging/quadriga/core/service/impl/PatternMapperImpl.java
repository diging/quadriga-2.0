package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.api.v1.model.NodeData;
import edu.asu.diging.quadriga.api.v1.model.PatternNodeData;
import edu.asu.diging.quadriga.core.model.events.pattern.RelationEventPattern;
import edu.asu.diging.quadriga.core.service.PatternMapper;

@Service
public class PatternMapperImpl implements PatternMapper {

    @Override
    public RelationEventPattern map(GraphPattern graphPattern) {
        List<Entry<String, PatternNodeData>> startNodes = graphPattern.getNodes().entrySet().stream()
                .filter(node -> graphPattern.getEdges().stream().noneMatch(edge -> edge.getTarget().equals(node.getKey())))
                .collect(Collectors.toList());
        
        
    }

}
