package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphEdgeData;
import edu.asu.diging.quadriga.web.model.GraphElement;
import edu.asu.diging.quadriga.web.model.GraphElements;
import edu.asu.diging.quadriga.web.model.GraphNodeData;
import edu.asu.diging.quadriga.web.model.GraphNodeType;

public class GraphUtil {
    
    public static GraphElements mapToGraph(List<Triple> triples) {
        Map<Long, GraphNodeData> conceptNodeMap = new HashMap<>();
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
    
    private static GraphNodeData createNode(TripleElement triple, GraphNodeType graphNodeType, Map<Long, GraphNodeData> conceptNodeMap, List<GraphData> nodes) {
        if (graphNodeType != GraphNodeType.PREDICATE && conceptNodeMap.containsKey(triple.getId())) {
            return conceptNodeMap.get(triple.getId());
        }
        GraphNodeData nodeData = new GraphNodeData();
        nodeData.setId(new ObjectId().toString());
        nodeData.setLabel(triple.getLabel());
        nodeData.setGroup(graphNodeType.getGroupId());
        nodeData.setUri(triple.getUri());
        if (graphNodeType != GraphNodeType.PREDICATE) {
            conceptNodeMap.put(triple.getId(), nodeData);
        }
        nodes.add(nodeData);
        return nodeData;
    }
    
    private static void createEdge(GraphData source, GraphData target, List<GraphData> edges) {
        GraphEdgeData edgeData = new GraphEdgeData();
        edgeData.setId(new ObjectId().toString());
        edgeData.setSource(source.getId());
        edgeData.setTarget(target.getId());
        edges.add(edgeData);
    }
    
    private static List<GraphElement> wrapInGraphElements(List<GraphData> dataList) {
        List<GraphElement> elements = new ArrayList<>();
        dataList.forEach(data -> {
            GraphElement element = new GraphElement();
            element.setData(data);
            elements.add(element);
        });
        return elements;
    }
}
