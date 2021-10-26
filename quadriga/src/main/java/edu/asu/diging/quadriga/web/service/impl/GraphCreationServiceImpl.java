package edu.asu.diging.quadriga.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.elements.TermPart;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
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
    public GraphElements createGraph(EventGraph eventGraph) {
        CreationEvent rootEvent = eventGraph.getRootEvent();
        // This means that there are zero subject-object-predicate triples in the EventGraph
        if(!(rootEvent instanceof RelationEvent)) {
            return null;
        }
        
        List<GraphData> graphEdges = new ArrayList<>();
        List<GraphData> graphNodes = new ArrayList<>();
        
        addNodesAndEdges((RelationEvent)rootEvent, graphNodes, graphEdges, eventGraph.getId().toString());

        GraphElements graphElements = new GraphElements();
        graphElements.setNodes(wrapInGraphElement(graphNodes));
        graphElements.setEdges(wrapInGraphElement(graphEdges));
        return graphElements;
    }
    
    private List<GraphElement> wrapInGraphElement(List<GraphData> dataList) {
        List<GraphElement> elements = new ArrayList<>();
        dataList.forEach(data -> {
            GraphElement element = new GraphElement();
            element.setData(data);
            elements.add(element);
        });
        return elements;
    }

    public ObjectId addNodesAndEdges(RelationEvent event, List<GraphData> graphNodes, List<GraphData> graphEdges, String eventGraphId) {
        Relation relation = event.getRelation();
        ObjectId predicateNodeId = null, subjectNodeId = null, objectNodeId = null;
        
        if(relation.getPredicate() != null) {
            predicateNodeId = createNode(graphNodes, relation.getPredicate(), GraphNodeType.PREDICATE);
        } else {
            logger.error("A predicate is missing in one of the relations for EventGraph: " + eventGraphId);
        }
        
        if(relation.getSubject() != null) {
            if(relation.getSubject() instanceof RelationEvent) {
                subjectNodeId = addNodesAndEdges((RelationEvent) relation.getSubject(), graphNodes, graphEdges, eventGraphId);
            } else {
                subjectNodeId = createNode(graphNodes, (AppellationEvent)relation.getSubject(), GraphNodeType.SUBJECT);
            }
        } else {
            logger.error("A subject is missing in one of the relations for EventGraph: " + eventGraphId);
        }
        
        if(relation.getObject() != null) {
            if(relation.getObject() instanceof RelationEvent) {
                objectNodeId = addNodesAndEdges((RelationEvent) relation.getObject(), graphNodes, graphEdges, eventGraphId);
            } else {
                objectNodeId = createNode(graphNodes, (AppellationEvent)relation.getObject(), GraphNodeType.OBJECT);
            }
        } else {
            logger.error("An object is missing in one of the relations for EventGraph: " + eventGraphId);
        }
        
        createEdge(graphEdges, subjectNodeId, predicateNodeId);
        createEdge(graphEdges, objectNodeId, predicateNodeId);
        
        return predicateNodeId;
    }
    
    @Override
    public ObjectId createNode(List<GraphData> graphNodes, AppellationEvent event, GraphNodeType graphNodeType) {
        ObjectId objectId = new ObjectId();
        GraphNodeData node = new GraphNodeData();
        node.setId(objectId.toString());
        node.setGroup(graphNodeType.getGroupId());
        // TODO: Update below to get label from ConceptPower using sourceURI
        node.setLabel(event.getTerm().getPrintedRepresentation().getTermParts().toArray(new TermPart[0])[0].getExpression());
        graphNodes.add(node);
        
        return objectId;
    }
    
    @Override
    public void createEdge(List<GraphData> graphEdges, ObjectId sourceId, ObjectId targetId) {
        GraphEdgeData edgeSubject = new GraphEdgeData();
        edgeSubject.setId(new ObjectId().toString());
        edgeSubject.setSource(sourceId.toString());
        edgeSubject.setTarget(targetId.toString());
        graphEdges.add(edgeSubject);
    }

}
