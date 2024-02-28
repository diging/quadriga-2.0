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
import edu.asu.diging.quadriga.core.pattern.PatternAppellationEvent;
import edu.asu.diging.quadriga.core.pattern.PatternCreationEvent;
import edu.asu.diging.quadriga.core.pattern.PatternRelationEvent;
import edu.asu.diging.quadriga.core.service.ConceptFinder;
import edu.asu.diging.quadriga.core.service.PatternFinder;

@Service
public class PatternFinderImpl implements PatternFinder {

    @Autowired
    private ConceptFinder conceptFinder;

    /** (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.PatternFinder#findGraphsWithPattern(edu.asu.diging.quadriga.api.v1.model.Metadata, edu.asu.diging.quadriga.core.model.events.pattern.PatternCreationEvent, edu.asu.diging.quadriga.core.model.EventGraph)
     * Finds and extracts the sub-networks which match the pattern
     * @param patternMetaData triple metadata for the pattern
     * @param patternRoot     root node of pattern
     * @param eventGraph      network to be searched
     * @return the list of extracted sub-networks
     */
    @Override
    public List<Graph> findGraphsWithPattern(Metadata patternMetaData, PatternCreationEvent patternRoot,
            EventGraph eventGraph) {
        List<Graph> matchingSubNetworks = new ArrayList<>();
        Queue<CreationEvent> graphNodes = new LinkedList<>();
        graphNodes.add(eventGraph.getRootEvent());
        while (!graphNodes.isEmpty()) {
            CreationEvent graphNode = graphNodes.poll();
            if (doesMatchPattern(graphNode, patternRoot)) {
                Graph subGraph = new Graph();
                subGraph.setMetadata(patternMetaData);
                subGraph.setNodes(new HashMap<>());
                extractGraph(subGraph, graphNode, patternRoot);
                matchingSubNetworks.add(subGraph);
            }
            if (graphNode instanceof RelationEvent) {
                RelationEvent relationNode = (RelationEvent) graphNode;
                graphNodes.add(relationNode.getRelation().getObject());
                graphNodes.add(relationNode.getRelation().getSubject());
                graphNodes.add(relationNode.getRelation().getPredicate());
            }
        }
        return matchingSubNetworks;
    }

    /**
     * Checks if a given graph node matches the corresponding pattern node based on their types.
     * @param graphNode   The graph node to be matched against the pattern node.
     * @param patternNode The pattern node to be matched against the graph node.
     * @return true if the graph node matches the pattern node, false otherwise.
     */
    private boolean doesMatchPattern(CreationEvent graphNode, PatternCreationEvent patternNode) {
        if (patternNode == null) {
            return true;
        }

        if (graphNode == null) {
            return false;
        }

        if (graphNode instanceof RelationEvent && patternNode instanceof PatternRelationEvent) {
            return matchRelationNode((RelationEvent) graphNode, (PatternRelationEvent) patternNode);
        } else if (graphNode instanceof AppellationEvent && patternNode instanceof PatternAppellationEvent) {
            return matchAppellationNode((AppellationEvent) graphNode, (PatternAppellationEvent) patternNode);
        }

        return false;
    }

    /**
     * 
     * Matches a graph node representing an appellation against a pattern node representing an appellation based on their concept type and interpretation.
     *
     * @param graphNode   The graph node representing an appellation to be matched against the pattern node.
     * @param patternNode The pattern node representing an appellation to be matched against the graph node.
     * @return true if the graph node matches the pattern node based on concept type and interpretation,
     * false otherwise.
     */
    private boolean matchAppellationNode(AppellationEvent graphNode, PatternAppellationEvent patternNode) {

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

    /**
     * Matches a graph node representing a relation against a pattern node representing a pattern relation
     * by recursively checking if the object, subject, and predicate nodes match.
     *
     * @param graphNode   The graph node representing a relation to be matched against the pattern node.
     * @param patternNode The pattern node representing a relation to be matched against the graph node.
     * @return true if all object, subject, and predicate nodes match, false otherwise.
     */
    private boolean matchRelationNode(RelationEvent graphNode, PatternRelationEvent patternNode) {
        return doesMatchPattern(graphNode.getRelation().getObject(), patternNode.getObject())
                && doesMatchPattern(graphNode.getRelation().getSubject(), patternNode.getSubject())
                && doesMatchPattern(graphNode.getRelation().getPredicate(), patternNode.getPredicate());
    }

    
    /**
     * Matches the interpretation of a graph node against the interpretation of a pattern node.
     *
     * @param graphNodeInterpretation   The interpretation of the graph node.
     * @param patternNodeInterpretation The interpretation of the pattern node.
     * @return true if the interpretations match or if a corresponding concept alternative ID is found, false otherwise.
     */
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
    
    /**
     * Matches the concept type of a graph node against the concept type of a pattern node.
     *
     * @param graphNodeInterpretation The interpretation of the graph node.
     * @param patternConceptType      The concept type of the pattern node.
     * @return true if the concept types match, false otherwise.
     */
    private boolean matchConceptType(String graphNodeInterpretation, String patternConceptType) {
        ConceptEntry concept = conceptFinder.getConcept(graphNodeInterpretation);
        if (concept != null && concept.getType() != null) {
            return concept.getType().getTypeUri().equals(patternConceptType);
        }
        return false;
    }

    /**
     * Extracts graph data from a graph node and adds it to the provided graph object.
     *
     * @param graph       The graph object to which the extracted data will be added.
     * @param graphNode   The graph node to be extracted.
     * @param patternNode The pattern node corresponding to the graph node.
     */
    private void extractGraph(Graph graph, CreationEvent graphNode, PatternCreationEvent patternNode) {
        if (patternNode == null) {
            return;
        }

        NodeData node = null;
        if (graphNode instanceof RelationEvent) {
            RelationEvent graphRelationNode = (RelationEvent) graphNode;
            PatternRelationEvent patternRelationNode = (PatternRelationEvent) patternNode;
            extractGraph(graph, graphRelationNode.getRelation().getObject(), patternRelationNode.getObject());
            extractGraph(graph, graphRelationNode.getRelation().getSubject(), patternRelationNode.getSubject());
            extractGraph(graph, graphRelationNode.getRelation().getPredicate(), patternRelationNode.getPredicate());
            node = createNodeData(graphRelationNode);
        } else if (graphNode instanceof AppellationEvent) {
            node = createNodeData((AppellationEvent) graphNode);
        }

        if (node != null) {
            graph.getNodes().put(patternNode.getId(), node);
        }

    }

    /**
     * Creates a NodeData object from an AppellationEvent object.
     *
     * @param appellationEvent The AppellationEvent object from which the NodeData will be created.
     * @return The NodeData object created from the AppellationEvent.
     */
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

    /**
     * Creates a NodeData object from a RelationEvent object.
     *
     * @param relationEvent The RelationEvent object from which the NodeData will be created.
     * @return The NodeData object created from the RelationEvent.
     */
    private NodeData createNodeData(RelationEvent relationEvent) {
        NodeData relationNode = new NodeData();
        NodeMetadata metadata = new NodeMetadata();
        metadata.setType(NetworkConstants.RELATION_EVENT_TYPE);
        relationNode.setMetadata(metadata);
        relationNode.setLabel("");
        return relationNode;
    }

}
