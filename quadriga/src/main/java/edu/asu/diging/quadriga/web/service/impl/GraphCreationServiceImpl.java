package edu.asu.diging.quadriga.web.service.impl;

import java.util.ArrayList;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.conceptpower.model.CachedConcept;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerService;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.web.service.model.GraphData;
import edu.asu.diging.quadriga.web.service.model.GraphEdgeData;
import edu.asu.diging.quadriga.web.service.model.GraphElement;
import edu.asu.diging.quadriga.web.service.model.GraphElements;
import edu.asu.diging.quadriga.web.service.model.GraphNodeData;
import edu.asu.diging.quadriga.web.service.model.GraphNodeType;
import edu.asu.diging.quadriga.web.service.GraphCreationService;

@Service
public class GraphCreationServiceImpl implements GraphCreationService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConceptPowerService conceptPowerService;

    @Override
    public GraphElements createGraph(List<EventGraph> eventGraphs) {

        List<GraphData> graphEdges = new ArrayList<>();
        List<GraphData> graphNodes = new ArrayList<>();
        Map<String, GraphNodeData> uniqueNodes = new HashMap<String, GraphNodeData>();

        eventGraphs.stream()
            .filter(eventGraph -> eventGraph.getRootEvent() instanceof RelationEvent)
            .forEach(validEventGraph -> createNodesAndEdges((RelationEvent) (validEventGraph.getRootEvent()),
                        graphNodes, graphEdges, uniqueNodes, validEventGraph.getId().toString()));

        GraphElements graphElements = new GraphElements();
        graphElements.setNodes(wrapInGraphElements(graphNodes));
        graphElements.setEdges(wrapInGraphElements(graphEdges));

        return graphElements;
    }

    @Override
    public String createNodesAndEdges(RelationEvent event, List<GraphData> graphNodes, List<GraphData> graphEdges,
            Map<String, GraphNodeData> uniqueNodes, String eventGraphId) {
        Relation relation = event.getRelation();
        String predicateNodeId = null;
        String subjectNodeId = null;
        String objectNodeId = null;

        if (relation.getPredicate() != null) {
            predicateNodeId = createPredicateNode(graphNodes, relation.getPredicate(), eventGraphId);
        } else {
            logger.error("A predicate is missing in one of the relations for EventGraph: " + eventGraphId);
        }

        if (relation.getSubject() != null) {
            if (relation.getSubject() instanceof RelationEvent) {
                subjectNodeId = createNodesAndEdges((RelationEvent) relation.getSubject(), graphNodes, graphEdges,
                        uniqueNodes, eventGraphId);
            } else {
                subjectNodeId = createSubjectOrObjectNode(graphNodes, (AppellationEvent) relation.getSubject(),
                        uniqueNodes, GraphNodeType.SUBJECT, eventGraphId);
            }
        } else {
            logger.error("A subject is missing in one of the relations for EventGraph: " + eventGraphId);
        }

        if (relation.getObject() != null) {
            if (relation.getObject() instanceof RelationEvent) {
                objectNodeId = createNodesAndEdges((RelationEvent) relation.getObject(), graphNodes, graphEdges,
                        uniqueNodes, eventGraphId);
            } else {
                objectNodeId = createSubjectOrObjectNode(graphNodes, (AppellationEvent) relation.getObject(),
                        uniqueNodes, GraphNodeType.OBJECT, eventGraphId);
            }
        } else {
            logger.error("An object is missing in one of the relations for EventGraph: " + eventGraphId);
        }

        if(subjectNodeId != null && predicateNodeId != null) {
            createEdge(graphEdges, subjectNodeId, predicateNodeId, eventGraphId);
        }
        
        if(objectNodeId != null && predicateNodeId != null) {
            createEdge(graphEdges, objectNodeId, predicateNodeId, eventGraphId);
        }

        return predicateNodeId;
    }

    @Override
    public String createPredicateNode(List<GraphData> graphNodes, AppellationEvent event, String eventGraphId) {
        GraphNodeData predicateNode = createNode(event, GraphNodeType.PREDICATE, eventGraphId);
        graphNodes.add(predicateNode);
        return predicateNode.getId();
    }

    @Override
    public String createSubjectOrObjectNode(List<GraphData> graphNodes, AppellationEvent event,
            Map<String, GraphNodeData> uniqueNodes, GraphNodeType graphNodeType, String eventGraphId) {
        String sourceUri = event.getTerm().getInterpretation().getSourceURI();
        GraphNodeData node;
        
        if (sourceUri != null && uniqueNodes.containsKey(sourceUri)) {
            node = uniqueNodes.get(sourceUri);
            node.getEventGraphIds().add(eventGraphId);
            return node.getId();
        }

        node = createNode(event, graphNodeType, eventGraphId);
        graphNodes.add(node);
        uniqueNodes.put(sourceUri, node);
        
        if(sourceUri != null && !sourceUri.equals("") && node.getAlternativeUris() != null) {
            node.getAlternativeUris()
            .stream()
            .filter(nullableAltUri -> nullableAltUri != null)
            .filter(alternativeUri -> !alternativeUri.equals("") && !alternativeUri.equals(sourceUri))
                .forEach(alternativeUriValue -> uniqueNodes.put(alternativeUriValue, node));
        }
        
        return node.getId();
    }

    @Override
    public GraphNodeData createNode(AppellationEvent event, GraphNodeType graphNodeType, String eventGraphId) {
        
        GraphNodeData node = new GraphNodeData();
        node.setId(new ObjectId().toString());
        node.setGroup(graphNodeType.getGroupId());
        node.setEventGraphIds(new ArrayList<String>(Collections.singletonList(eventGraphId)));

        String sourceURI = event.getTerm().getInterpretation().getSourceURI();

        if (sourceURI != null && sourceURI.contains("www.digitalhps.org")) {

            node.setUri(sourceURI);
            CachedConcept conceptCache = conceptPowerService.getConceptByUri(sourceURI);

            if (conceptCache != null) {
                node.setLabel(conceptCache.getWord());
                node.setDescription(conceptCache.getDescription());
                node.setAlternativeUris(conceptCache.getAlternativeUris());
            }
        } else {
            // Need to get data from viaf if viaf URL is present, but doesn't seem to be part of story Q20-19
            node.setLabel(event.getTerm().getPrintedRepresentation().getTermParts().iterator().next().getExpression());
        }

        return node;
    }
    
    private static  GraphNodeData createNode(TripleElement tripleElement, GraphNodeType graphNodeType,
            Map<String, GraphNodeData> conceptNodeMap, List<GraphData> nodes) {
        String elementUri = tripleElement.getUri();
        //Avoid node duplication if the element is not a predicate element and if it already exists
        if (graphNodeType != GraphNodeType.PREDICATE && elementUri != null && !elementUri.isEmpty()
                && conceptNodeMap.containsKey(elementUri)) {
            return conceptNodeMap.get(elementUri);
        }
        GraphNodeData nodeData = new GraphNodeData();
        nodeData.setId(new ObjectId().toString());
        nodeData.setLabel(tripleElement.getLabel());
        nodeData.setGroup(graphNodeType.getGroupId());
        nodeData.setUri(tripleElement.getUri());
        if (graphNodeType != GraphNodeType.PREDICATE && elementUri != null && !elementUri.isEmpty()) {
            conceptNodeMap.put(elementUri, nodeData);
        }
        nodes.add(nodeData);
        return nodeData;
    }
    
    @Override
    public void createEdge(List<GraphData> graphEdges, String sourceId, String targetId, String eventGraphId) {
        GraphEdgeData edge = new GraphEdgeData();
        edge.setId(new ObjectId().toString());
        edge.setSource(sourceId);
        edge.setTarget(targetId);
        edge.setEventGraphId(eventGraphId);
        graphEdges.add(edge);
    }
    private static  void createEdge(GraphData source, GraphData target, List<GraphData> edges) {
        GraphEdgeData edgeData = new GraphEdgeData();
        edgeData.setId(new ObjectId().toString());
        edgeData.setSource(source.getId());
        edgeData.setTarget(target.getId());
        edges.add(edgeData);
    }
    
    private static  List<GraphElement> wrapInGraphElements(List<GraphData> dataList) {
        List<GraphElement> elements = new ArrayList<>();
        dataList.forEach(data -> {
            GraphElement element = new GraphElement();
            element.setData(data);
            elements.add(element);
        });
        return elements;
    }
    
    @Override
    public GraphElements mapToGraph(List<DefaultMapping> triples) {
        Map<String, GraphNodeData> conceptNodeMap = new HashMap<>();
        List<GraphData> nodes = new ArrayList<>();
        List<GraphData> edges = new ArrayList<>();

        triples.forEach(triple -> {
            GraphNodeData subject = createNode(triple.getSubject(), GraphNodeType.SUBJECT, conceptNodeMap, nodes);
            GraphNodeData object = createNode(triple.getObject(), GraphNodeType.OBJECT, conceptNodeMap, nodes);
            GraphNodeData predicate = createNode(triple.getPredicate(), GraphNodeType.PREDICATE, conceptNodeMap, nodes);

            createEdge(subject, predicate, edges);
            createEdge(predicate, object, edges);
        });

        GraphElements graphElements = new GraphElements();
        graphElements.setNodes(wrapInGraphElements(nodes));
        graphElements.setEdges(wrapInGraphElements(edges));
        return graphElements;
    }


}
