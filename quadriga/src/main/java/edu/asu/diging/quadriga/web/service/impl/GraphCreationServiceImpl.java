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

import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptEntry;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerConnectorService;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphEdgeData;
import edu.asu.diging.quadriga.web.model.GraphElement;
import edu.asu.diging.quadriga.web.model.GraphElements;
import edu.asu.diging.quadriga.web.model.GraphNodeData;
import edu.asu.diging.quadriga.web.model.GraphNodeType;
import edu.asu.diging.quadriga.web.service.GraphCreationService;

@Service
public class GraphCreationServiceImpl implements GraphCreationService {
    
    @Autowired
    private ConceptPowerConnectorService conceptPowerConnectorService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public GraphElements createGraph(List<EventGraph> eventGraphs) {

        List<GraphData> graphEdges = new ArrayList<>();
        List<GraphData> graphNodes = new ArrayList<>();
        Map<String, GraphNodeData> uniqueNodes = new HashMap<String, GraphNodeData>();

        eventGraphs.stream()
                .filter(eventGraph -> eventGraph.getRootEvent() instanceof RelationEvent)
                .forEach(validEventGraph -> createNodesAndEdges((RelationEvent) (validEventGraph.getRootEvent()), graphNodes, graphEdges,
                        uniqueNodes, validEventGraph.getId().toString()));

        GraphElements graphElements = new GraphElements();
        graphElements.setNodes(wrapInGraphElements(graphNodes));
        graphElements.setEdges(wrapInGraphElements(graphEdges));

        return graphElements;
    }

    @Override
    public String createNodesAndEdges(RelationEvent event, List<GraphData> graphNodes, List<GraphData> graphEdges,
            Map<String, GraphNodeData> uniqueNodes, String eventGraphId) {
        Relation relation = event.getRelation();
        String predicateNodeId = null, subjectNodeId = null, objectNodeId = null;

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

        createEdge(graphEdges, subjectNodeId, predicateNodeId, eventGraphId);
        createEdge(graphEdges, objectNodeId, predicateNodeId, eventGraphId);

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
        return node.getId();
    }

    @Override
    public GraphNodeData createNode(AppellationEvent event, GraphNodeType graphNodeType, String eventGraphId) {
        GraphNodeData node = new GraphNodeData();
        node.setId(new ObjectId().toString());
        node.setGroup(graphNodeType.getGroupId());
        node.setEventGraphId(new ArrayList<String>(Collections.singletonList(eventGraphId)));
        
        String sourceURI = event.getTerm().getInterpretation().getSourceURI();
        
        // TODO: Need to get data from cache instead of always making a REST call
        if(sourceURI != null && sourceURI.contains("www.digitalhps.org")) {
            ConceptPowerReply conceptPowerReply = conceptPowerConnectorService.getConceptPowerReply(sourceURI);
            
            if(conceptPowerReply != null) {
                List<ConceptEntry> conceptEntries = conceptPowerReply.getConceptEntries();
                
                if(!conceptEntries.isEmpty()) {
                    ConceptEntry conceptEntry = conceptPowerReply.getConceptEntries().get(0);
                    node.setLabel(conceptEntry.getLemma());
                    node.setDescription(conceptEntry.getDescription());
                } else {
                    logger.error("No concept entries present in ConceptPower reply for sourceURI: " + sourceURI);
                }
            }
        } else {
            // TODO: Need to get data from viaf if viaf URL is present
            node.setLabel(event.getTerm().getPrintedRepresentation().getTermParts().iterator().next().getExpression());
        }
        
        return node;
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

    private List<GraphElement> wrapInGraphElements(List<GraphData> dataList) {
        List<GraphElement> elements = new ArrayList<>();
        dataList.forEach(data -> {
            GraphElement element = new GraphElement();
            element.setData(data);
            elements.add(element);
        });
        return elements;
    }

}
