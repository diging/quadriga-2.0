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
        graphElements.setNodes(graphNodes);
        graphElements.setEdges(graphEdges);

        return graphElements;
    }

    /**
     * This method creates nodes and edges for subject, object and predicate nodes
     * For a predicate node, the event type is always appellation event and the predicate node is created
     * 
     * For subject and object, if the event type is appellation then the nodes that are created are leaf
     * nodes
     * But if event type is relation event, this method is called recursively to because every relation event
     * itself has subject, object and predicate
     * 
     * After the nodes are created, both subject and object are linked to the predicate node by creating edges
     * One edge goes from subject to predicate, another one from object to predicate
     * 
     * The predicate node's id is returned to the parent of the subtree, so that it could
     * be linked to its parent's predicate
     * 
     * @param event is the relation event for which the method creates subject, object and predicate nodes
     * @param graphNodes maintain the list of nodes created
     * @param graphEdges maintain the list of edges created
     * @param uniqueNodes maintains a map that links every unique sourceURI to its corresponding node
     * @param eventGraphId is the id of the current EventGraph
     * @return
     */
    private String createNodesAndEdges(RelationEvent event, List<GraphData> graphNodes, List<GraphData> graphEdges,
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
        } 


        if (relation.getObject() != null) {
            if (relation.getObject() instanceof RelationEvent) {
                objectNodeId = createNodesAndEdges((RelationEvent) relation.getObject(), graphNodes, graphEdges,
                        uniqueNodes, eventGraphId);
            } else {
                objectNodeId = createSubjectOrObjectNode(graphNodes, (AppellationEvent) relation.getObject(),
                        uniqueNodes, GraphNodeType.OBJECT, eventGraphId);
            }
        } 


        if(subjectNodeId != null && predicateNodeId != null) {
            createEdge(graphEdges, subjectNodeId, predicateNodeId, eventGraphId);
        }
        
        if(objectNodeId != null && predicateNodeId != null) {
            createEdge(graphEdges, objectNodeId, predicateNodeId, eventGraphId);
        }
        
        if (subjectNodeId == null) {
            logger.error("A subject is missing in one of the relations for EventGraph: " + eventGraphId);
            
        }
        if (objectNodeId == null) {
            logger.error("An object is missing in one of the relations for EventGraph: " + eventGraphId); 
        }

        return predicateNodeId;
    }

    /**
     * Creates a predicate node using the event data and adds it to list of graph nodes
     * 
     * @param graphNodes list to maintain all current graph nodes
     * @param event is an appellation event that contains data to be set to the node
     * @return the id of the created predicate node
     */
    private String createPredicateNode(List<GraphData> graphNodes, AppellationEvent event, String eventGraphId) {
        GraphNodeData predicateNode = createNode(event, GraphNodeType.PREDICATE, eventGraphId);
        graphNodes.add(predicateNode);
        return predicateNode.getId();
    }
    
    /**
     * Checks if the unique nodes map contains the same sourceURI as the node to be created
     * If yes, returns this node
     * Else it creates a subject or object node using the event data and adds it to the list of graph nodes
     * and the map of unique nodes with key as sourceURI and value as the node object itself 
     * 
     * @param graphNodes list to maintain all current graph nodes
     * @param event is an appellation event that contains data to be set to the node
     * @param uniqueNodes map to maintain nodes with unique sourceURI that can be reused
     * @param graphNodeType indicates whether node to be created is subject or object node to accordingly set group id
     * @return the id of an existing node from unique nodes or the newly created node
     */
    private String createSubjectOrObjectNode(List<GraphData> graphNodes, AppellationEvent event,
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
    
    /**
     * Creates a GraphNodeData object and sets details such as id, label, group
     * 
     * @param event is the appellation event for which node is being created
     * @param graphNodeType used to set group id
     * @return the created GraphNodeData object
     */
    private GraphNodeData createNode(AppellationEvent event, GraphNodeType graphNodeType, String eventGraphId) {
        
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
    
    /**
     * Creates a graph node based on the provided TripleElement.
     *
     * @param tripleElement the TripleElement from which to create the node
     * @param graphNodeType the type of the graph node (subject, predicate, or object)
     * @param conceptNodeMap a map containing already created graph nodes, used to avoid duplication
     * @param nodes the list of graph nodes to which the newly created node will be added
     * @return the created GraphNodeData representing the graph node
     */
    private GraphNodeData createNode(TripleElement tripleElement, GraphNodeType graphNodeType,
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
    
    /**
     * Creates that edge that links the provided source and target using their IDs and it to
     * the list of graphEdges
     * 
     * @param graphEdges is the list that maintains all edges created for the graph
     * @param sourceId is the source node's id to be linked to the target
     * @param targetId is the target node's id to be linked to the source
     * @param eventGraphId is the id of the event graph.
     */
    private void createEdge(List<GraphData> graphEdges, String sourceId, String targetId, String eventGraphId) {
        
        GraphEdgeData edge = new GraphEdgeData();
        edge.setId(new ObjectId().toString());
        edge.setSource(sourceId);
        edge.setTarget(targetId);
        edge.setEventGraphId(eventGraphId);
        graphEdges.add(edge);
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

            createEdge(edges, subject.getId(), predicate.getId(), null);
            createEdge(edges, predicate.getId(), object.getId(), null);
        });

        GraphElements graphElements = new GraphElements();
        graphElements.setNodes(nodes);
        graphElements.setEdges(edges);
        return graphElements;
    }


}
