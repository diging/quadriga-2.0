package edu.asu.diging.quadriga.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.Edge;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.model.NetworkConstants;
import edu.asu.diging.quadriga.api.v1.model.PatternNodeData;
import edu.asu.diging.quadriga.core.pattern.PatternAppellationEvent;
import edu.asu.diging.quadriga.core.pattern.PatternCreationEvent;
import edu.asu.diging.quadriga.core.pattern.PatternRelationEvent;
import edu.asu.diging.quadriga.core.service.PatternMapper;
import edu.asu.diging.quadriga.core.service.TriFunction;

@Service
public class PatternMapperImpl implements PatternMapper {

    private Map<String, TriFunction<String, PatternNodeData, PatternMapping, PatternCreationEvent>> creationMethods;

    public PatternMapperImpl() {
        creationMethods = new HashMap<>();
        creationMethods.put(NetworkConstants.APPELLATION_EVENT_TYPE, this::createNodeEventPattern);
        creationMethods.put(NetworkConstants.RELATION_EVENT_TYPE, this::createRelationEventPattern);
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.PatternMapper#mapPattern(edu.asu.diging.quadriga.api.v1.model.PatternMapping)
     */
    @Override
    public PatternCreationEvent mapPattern(PatternMapping patternMapping) {
        List<Entry<String, PatternNodeData>> startNodes = patternMapping.getNodes().entrySet().stream().filter(
                node -> patternMapping.getEdges().stream().noneMatch(edge -> edge.getTarget().equals(node.getKey())))
                .collect(Collectors.toList());

        if (startNodes.size() != 1) {
            return null;
        }
        String key = startNodes.get(0).getKey();
        PatternNodeData node = startNodes.get(0).getValue();
        PatternCreationEvent root = createGraph(key, node, patternMapping);
        return root;
    }

    private PatternCreationEvent createGraph(String nodeId, PatternNodeData node, PatternMapping patternMapping) {
        TriFunction<String, PatternNodeData, PatternMapping, PatternCreationEvent> method = creationMethods
                .get(node.getType());
        return method.apply(nodeId, node, patternMapping);
    }

    private PatternCreationEvent createRelationEventPattern(String nodeId, PatternNodeData node,
            PatternMapping patternMapping) {
        PatternRelationEvent relationPatternNode = new PatternRelationEvent();
        relationPatternNode.setId(nodeId);

        Optional<Edge> subject = patternMapping.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_SUBJECT))
                .findFirst();
        if (subject.isPresent()) {
            relationPatternNode.setSubject(createGraph(subject.get().getTarget(),
                    patternMapping.getNodes().get(subject.get().getTarget()), patternMapping));
        }

        Optional<Edge> predicate = patternMapping.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_PREDICATE))
                .findFirst();
        if (predicate.isPresent()) {
            relationPatternNode.setPredicate((PatternAppellationEvent) createGraph(predicate.get().getTarget(),
                    patternMapping.getNodes().get(predicate.get().getTarget()), patternMapping));
        }

        Optional<Edge> object = patternMapping.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_OBJECT))
                .findFirst();
        if (object.isPresent()) {
            relationPatternNode.setObject(createGraph(object.get().getTarget(),
                    patternMapping.getNodes().get(object.get().getTarget()), patternMapping));
        }

        return relationPatternNode;
    }

    private PatternCreationEvent createNodeEventPattern(String nodeId, PatternNodeData node,
            PatternMapping patternMapping) {
        PatternAppellationEvent patternNode = new PatternAppellationEvent();
        patternNode.setId(nodeId);
        patternNode.setConceptType(node.getConceptType());
        patternNode.setInterpretation(node.getInterpretation());
        return patternNode;
    }

}
