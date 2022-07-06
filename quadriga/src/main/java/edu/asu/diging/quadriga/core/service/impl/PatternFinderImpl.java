package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.Metadata;
import edu.asu.diging.quadriga.api.v1.model.NetworkConstants;
import edu.asu.diging.quadriga.api.v1.model.NodeData;
import edu.asu.diging.quadriga.api.v1.model.NodeMetadata;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerAlternativeId;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.core.model.events.pattern.AppellationEventPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.CreationEventPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.RelationEventPattern;
import edu.asu.diging.quadriga.core.service.ConceptFinder;
import edu.asu.diging.quadriga.core.service.PatternFinder;

@Service
public class PatternFinderImpl implements PatternFinder {

    @Autowired
    private ConceptFinder conceptFinder;

    @Override
    public List<Graph> findGraphsWithPattern(Metadata patternMetaData, CreationEventPattern patternRoot,
            EventGraph eventGraph) {
        List<Graph> result = new ArrayList<>();
        Queue<CreationEvent> graphNodes = new LinkedList<>();
        graphNodes.add(eventGraph.getRootEvent());
        while (!graphNodes.isEmpty()) {
            CreationEvent graphNode = graphNodes.poll();
            if (doesMatchPattern(graphNode, patternRoot)) {
                Graph subGraph = new Graph();
                subGraph.setMetadata(patternMetaData);
                subGraph.setNodes(new HashMap<>());
                extractGraph(subGraph, graphNode, patternRoot);
                result.add(subGraph);
            }
            if (graphNode instanceof RelationEvent) {
                RelationEvent relationNode = (RelationEvent) graphNode;
                graphNodes.add(relationNode.getRelation().getObject());
                graphNodes.add(relationNode.getRelation().getSubject());
                graphNodes.add(relationNode.getRelation().getPredicate());
            }
        }
        return result;
    }

    private boolean doesMatchPattern(CreationEvent graphNode, CreationEventPattern patternNode) {
        if (patternNode == null) {
            return true;
        }

        if (graphNode == null) {
            return false;
        }

        if (graphNode instanceof RelationEvent && patternNode instanceof RelationEventPattern) {
            return matchRelationNode((RelationEvent) graphNode, (RelationEventPattern) patternNode);
        } else if (graphNode instanceof AppellationEvent && patternNode instanceof AppellationEventPattern) {
            return matchAppellationNode((AppellationEvent) graphNode, (AppellationEventPattern) patternNode);
        }

        return false;
    }

    private boolean matchAppellationNode(AppellationEvent graphNode, AppellationEventPattern patternNode) {

        if (patternNode.getConceptType() != null && !patternNode.getConceptType().isEmpty()
                && !matchConceptType(graphNode.getTerm().getInterpretation().getSourceURI(),
                        patternNode.getConceptType())) {
            return false;
        }

        if (patternNode.getInterpretation() != null && !patternNode.getInterpretation().isEmpty()
                && !matchInterpretation(graphNode.getTerm().getInterpretation().getSourceURI(),
                        patternNode.getInterpretation())) {
            return false;
        }

        return true;
    }

    private boolean matchRelationNode(RelationEvent graphNode, RelationEventPattern patternNode) {
        return doesMatchPattern(graphNode.getRelation().getObject(), patternNode.getObject())
                && doesMatchPattern(graphNode.getRelation().getSubject(), patternNode.getSubject())
                && doesMatchPattern(graphNode.getRelation().getPredicate(), patternNode.getPredicate());
    }

    private boolean matchInterpretation(String graphNodeInterpretation, String patternNodeInterpretation) {
        if (graphNodeInterpretation.equals(patternNodeInterpretation)) {
            return true;
        }

        ConceptEntry concept = conceptFinder.getConcept(graphNodeInterpretation);
        if (concept != null) {
            for (ConceptpowerAlternativeId alternativeId : concept.getAlternativeIds()) {
                if (alternativeId.getConceptUri().equals(patternNodeInterpretation)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matchConceptType(String graphNodeInterpretation, String patternConceptType) {
        ConceptEntry concept = conceptFinder.getConcept(graphNodeInterpretation);
        if (concept != null && concept.getType() != null) {
            return concept.getType().getUri().equals(patternConceptType);
        }
        return false;
    }

    private void extractGraph(Graph graph, CreationEvent graphNode, CreationEventPattern patternNode) {
        if (patternNode == null) {
            return;
        }

        NodeData node = null;
        if (graphNode instanceof RelationEvent) {
            RelationEvent relationGraphNode = (RelationEvent) graphNode;
            RelationEventPattern relationPatternNode = (RelationEventPattern) patternNode;
            extractGraph(graph, relationGraphNode.getRelation().getObject(), relationPatternNode.getObject());
            extractGraph(graph, relationGraphNode.getRelation().getSubject(), relationPatternNode.getSubject());
            extractGraph(graph, relationGraphNode.getRelation().getPredicate(), relationPatternNode.getPredicate());
            node = createNodeData(relationGraphNode);
        } else if (graphNode instanceof AppellationEvent) {
            node = createNodeData((AppellationEvent) graphNode);
        }

        if (node != null) {
            graph.getNodes().put(patternNode.getId(), node);
        }

    }

    private NodeData createNodeData(AppellationEvent appellationEvent) {
        NodeData appellationNode = new NodeData();
        NodeMetadata metadata = new NodeMetadata();
        metadata.setType(NetworkConstants.APPELLATION_EVENT_TYPE);
        metadata.setInterpretation(appellationEvent.getTerm().getInterpretation().getSourceURI());
        appellationNode.setMetadata(metadata);
        ConceptEntry concept = conceptFinder.getConcept(appellationEvent.getTerm().getInterpretation().getSourceURI());
        if (concept != null) {
            appellationNode.setLabel(concept.getLemma());

        } else {
            appellationNode.setLabel(appellationEvent.getTerm().getInterpretation().getSourceURI());
        }
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

}
