package edu.asu.diging.quadriga.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.elements.TermPart;
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
            predicateNodeId = createPredicateNode(graphNodes, relation.getPredicate());
        } else {
            logger.error("A predicate is missing in one of the relations for EventGraph: " + eventGraphId);
        }

        if (relation.getSubject() != null) {
            if (relation.getSubject() instanceof RelationEvent) {
                subjectNodeId = createNodesAndEdges((RelationEvent) relation.getSubject(), graphNodes, graphEdges,
                        uniqueNodes, eventGraphId);
            } else {
                subjectNodeId = createSubjectOrObjectNode(graphNodes, (AppellationEvent) relation.getSubject(),
                        uniqueNodes, GraphNodeType.SUBJECT);
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
                        uniqueNodes, GraphNodeType.OBJECT);
            }
        } else {
            logger.error("An object is missing in one of the relations for EventGraph: " + eventGraphId);
        }

        createEdge(graphEdges, subjectNodeId, predicateNodeId);
        createEdge(graphEdges, objectNodeId, predicateNodeId);

        return predicateNodeId;
    }

    @Override
    public String createPredicateNode(List<GraphData> graphNodes, AppellationEvent event) {
        GraphNodeData predicateNode = createNode(event, GraphNodeType.PREDICATE);
        graphNodes.add(predicateNode);
        return predicateNode.getId();
    }

    @Override
    public String createSubjectOrObjectNode(List<GraphData> graphNodes, AppellationEvent event,
            Map<String, GraphNodeData> uniqueNodes, GraphNodeType graphNodeType) {
        String sourceUri = event.getTerm().getInterpretation().getSourceURI();

        if (sourceUri != null && uniqueNodes.containsKey(sourceUri)) {
            return uniqueNodes.get(sourceUri).getId();
        }

        GraphNodeData node = createNode(event, graphNodeType);
        graphNodes.add(node);
        uniqueNodes.put(sourceUri, node);
        return node.getId();
    }

    @Override
    public GraphNodeData createNode(AppellationEvent event, GraphNodeType graphNodeType) {
        GraphNodeData node = new GraphNodeData();
        node.setId(new ObjectId().toString());
        node.setGroup(graphNodeType.getGroupId());
        
        // TODO: Update below to get label from ConceptPower using sourceURI
        node.setLabel(event.getTerm().getPrintedRepresentation().getTermParts().toArray(new TermPart[0])[0].getExpression());
        return node;
    }

    @Override
    public void createEdge(List<GraphData> graphEdges, String sourceId, String targetId) {
        GraphEdgeData edgeSubject = new GraphEdgeData();
        edgeSubject.setId(new ObjectId().toString());
        edgeSubject.setSource(sourceId);
        edgeSubject.setTarget(targetId);
        graphEdges.add(edgeSubject);
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
