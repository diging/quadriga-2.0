package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphEdgeData;
import edu.asu.diging.quadriga.web.model.GraphElement;
import edu.asu.diging.quadriga.web.model.GraphElements;
import edu.asu.diging.quadriga.web.model.GraphNodeData;
import edu.asu.diging.quadriga.web.model.GraphNodeType;

public class GraphUtil {

    public static GraphElements mapToGraph(List<DefaultMapping> triples) {
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

    private static GraphNodeData createNode(TripleElement tripleElement, GraphNodeType graphNodeType,
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
