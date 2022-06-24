package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.api.v1.model.NetworkConstants;
import edu.asu.diging.quadriga.api.v1.model.NodeData;
import edu.asu.diging.quadriga.api.v1.model.NodeMetadata;
import edu.asu.diging.quadriga.core.conceptpower.ConceptpowerConnector;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerResponse;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.core.model.events.pattern.AppellationEventPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.CreationEventPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.RelationEventPattern;
import edu.asu.diging.quadriga.core.service.PatternFinder;
import edu.asu.diging.quadriga.core.service.PatternMapper;

public class PatternFinderImpl implements PatternFinder {

    @Autowired
    private PatternMapper patternMapper;

    @Autowired
    private ConceptpowerConnector conceptpowerConnector;

    @Override
    public List<Graph> findGraphsWithPattern(GraphPattern pattern, EventGraph eventGraph) {
        CreationEventPattern patternRoot = patternMapper.mapPattern(pattern);
        List<Graph> result = new ArrayList<>();
        Queue<CreationEvent> graphNodes = new LinkedList<>();
        graphNodes.add(eventGraph.getRootEvent());
        while (!graphNodes.isEmpty()) {
            CreationEvent graphNode = graphNodes.poll();
            if (doesMatchPattern(graphNode, patternRoot)) {
                Graph subGraph = new Graph();
                subGraph.setMetadata(pattern.getMetadata());
                subGraph.setNodes(new HashMap<>());
                extractGraph(subGraph, graphNode, patternRoot);
                result.add(subGraph);
            }
            if (graphNode instanceof RelationEvent) {
                graphNodes.add(((RelationEvent) graphNode).getRelation().getObject());
                graphNodes.add(((RelationEvent) graphNode).getRelation().getSubject());
                graphNodes.add(((RelationEvent) graphNode).getRelation().getPredicate());
            }
        }
        return result;
    }

    private void extractGraph(Graph graph, CreationEvent graphNode, CreationEventPattern patternNode) {
        if (patternNode == null) {
            return;
        }

        NodeData node = null;
        if (graphNode instanceof RelationEvent) {
            extractGraph(graph, ((RelationEvent) graphNode).getRelation().getObject(),
                    ((RelationEventPattern) patternNode).getObject());
            extractGraph(graph, ((RelationEvent) graphNode).getRelation().getSubject(),
                    ((RelationEventPattern) patternNode).getSubject());
            extractGraph(graph, ((RelationEvent) graphNode).getRelation().getPredicate(),
                    ((RelationEventPattern) patternNode).getPredicate());
            node = createNodeData((RelationEvent) graphNode);
        } else if (graphNode instanceof AppellationEvent) {
            node = createNodeData((AppellationEvent) graphNode);
        }

        if (node != null) {
            graph.getNodes().put(patternNode.getId(), node);
        }

    }

    private boolean doesMatchPattern(CreationEvent graphNode, CreationEventPattern patternNode) {
        if (patternNode == null) {
            return true;
        }

        if (graphNode == null) {
            return false;
        }

        if (graphNode instanceof RelationEvent && patternNode instanceof RelationEventPattern) {
            return matchRelationNodes((RelationEvent) graphNode, (RelationEventPattern) patternNode);
        } else if (graphNode instanceof AppellationEvent && patternNode instanceof AppellationEventPattern) {
            return matchAppellationNodes((AppellationEvent) graphNode, (AppellationEventPattern) patternNode);
        }

        return false;
    }

    private NodeData createNodeData(AppellationEvent appellationEvent) {
        NodeData appellationNode = new NodeData();
        NodeMetadata metadata = new NodeMetadata();
        metadata.setType(NetworkConstants.APPELLATION_EVENT_TYPE);
        metadata.setInterpretation(appellationEvent.getTerm().getInterpretation().getSourceURI());
        appellationNode.setMetadata(metadata);
        appellationNode.setLabel(
                appellationEvent.getTerm().getPrintedRepresentation().getTermParts().iterator().next().getExpression());
        return appellationNode;
    }

    private NodeData createNodeData(RelationEvent relationEvent) {
        NodeData relationNode = new NodeData();
        NodeMetadata metadata = new NodeMetadata();
        metadata.setType(NetworkConstants.RELATION_EVENT_TYPE);
        relationNode.setMetadata(metadata);
        relationNode.setLabel("");
        return relationNode;
    }

    private boolean matchAppellationNodes(AppellationEvent graphNode, AppellationEventPattern patternNode) {

        if (patternNode.getConceptType() != null && !patternNode.getConceptType().isEmpty() && matchConceptType(
                graphNode.getTerm().getInterpretation().getSourceURI(), patternNode.getConceptType())) {
            return false;
        }

        if (patternNode.getInterpretation() != null && !patternNode.getInterpretation().isEmpty()
                && !patternNode.getInterpretation().equals(graphNode.getTerm().getInterpretation().getSourceURI())) {
            return false;
        }

        return true;
    }

    private boolean matchRelationNodes(RelationEvent graphNode, RelationEventPattern patternNode) {
        return doesMatchPattern(graphNode.getRelation().getObject(), patternNode.getObject())
                && doesMatchPattern(graphNode.getRelation().getSubject(), patternNode.getSubject())
                && doesMatchPattern(graphNode.getRelation().getPredicate(), patternNode.getPredicate());
    }

    private boolean matchConceptType(String graphNodeInterpretation, String patternConceptType) {
        ConceptEntry conceptEntry = conceptpowerConnector.getConceptEntry(graphNodeInterpretation);
        if (conceptEntry != null) {
            return conceptEntry.getType().getUri().equals(patternConceptType);
        }
        ConceptpowerResponse similarEntries = conceptpowerConnector.findConceptEntryEqualTo(graphNodeInterpretation);
        if (similarEntries != null && similarEntries.getConceptEntries() != null
                && !similarEntries.getConceptEntries().isEmpty()) {
            return similarEntries.getConceptEntries().get(0).getType().getUri().equals(patternConceptType);
        }
        return false;
    }

}
