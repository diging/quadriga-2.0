package edu.asu.diging.quadriga.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.Edge;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.api.v1.model.NetworkConstants;
import edu.asu.diging.quadriga.api.v1.model.PatternNodeData;
import edu.asu.diging.quadriga.core.model.events.pattern.AppellationEventPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.CreationEventPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.RelationEventPattern;
import edu.asu.diging.quadriga.core.service.PatternMapper;
import edu.asu.diging.quadriga.core.service.TriFunction;

@Service
public class PatternMapperImpl implements PatternMapper {
    
    private Map<String, TriFunction<String, PatternNodeData, GraphPattern, CreationEventPattern>> creationMethods;

    public PatternMapperImpl() {
        creationMethods = new HashMap<>();
        creationMethods.put(NetworkConstants.APPELLATION_EVENT_TYPE, this::createNodeEventPattern);
        creationMethods.put(NetworkConstants.RELATION_EVENT_TYPE, this::createRelationEventPattern);
    }

    @Override
    public CreationEventPattern mapPattern(GraphPattern graphPattern) {
        List<Entry<String, PatternNodeData>> startNodes = graphPattern.getNodes().entrySet().stream()
                .filter(node -> graphPattern.getEdges().stream().noneMatch(edge -> edge.getTarget().equals(node.getKey())))
                .collect(Collectors.toList());
        
        if (startNodes.size() != 1) {
            return null;
        }
        String key = startNodes.get(0).getKey();
        PatternNodeData node = startNodes.get(0).getValue();
        CreationEventPattern root = createGraph(key, node, graphPattern);
        return root;
    }
    
    private CreationEventPattern createGraph(String nodeId, PatternNodeData node, GraphPattern graphPattern) {
        TriFunction<String, PatternNodeData, GraphPattern, CreationEventPattern> method = creationMethods.get(node.getType());
        return method.apply(nodeId, node, graphPattern);
    }
    
    private CreationEventPattern createRelationEventPattern(String nodeId, PatternNodeData node, GraphPattern graphPattern) {
        RelationEventPattern relationPatternNode = new RelationEventPattern();
        
        Optional<Edge> subject = graphPattern.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_SUBJECT))
                .findFirst();
        if (subject.isPresent()) {
            relationPatternNode.setSubject(
                    createGraph(subject.get().getTarget(), graphPattern.getNodes().get(subject.get().getTarget()), graphPattern));
        }

        Optional<Edge> predicate = graphPattern.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_PREDICATE))
                .findFirst();
        if (predicate.isPresent()) {
            relationPatternNode.setPredicate((AppellationEventPattern)
                    createGraph(predicate.get().getTarget(), graphPattern.getNodes().get(predicate.get().getTarget()), graphPattern));
        }

        Optional<Edge> object = graphPattern.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_OBJECT))
                .findFirst();
        if (object.isPresent()) {
            relationPatternNode.setObject(
                    createGraph(object.get().getTarget(), graphPattern.getNodes().get(object.get().getTarget()), graphPattern));
        }
        
        return relationPatternNode;
    }
    
    private CreationEventPattern createNodeEventPattern(String nodeId, PatternNodeData node, GraphPattern graphPattern) {
        AppellationEventPattern patternNode = new AppellationEventPattern();
        patternNode.setConceptType(node.getConceptType());
        patternNode.setInterpretation(node.getInterpretation());
        return patternNode;
    }

}
