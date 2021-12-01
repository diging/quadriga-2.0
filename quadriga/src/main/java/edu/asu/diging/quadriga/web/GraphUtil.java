package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphElements;
import edu.asu.diging.quadriga.web.model.GraphNodeData;
import edu.asu.diging.quadriga.web.model.GraphNodeType;

public class GraphUtil {
    
    public GraphElements mapToGraph(List<Triple> triples) {
        List<GraphData> nodes = new ArrayList<>();
        List<GraphData> edges = new ArrayList<>();
        
        triples.forEach(triple -> {
            
        });
    }
    
    private GraphNodeData createNode(TripleElement triple, GraphNodeType graphNodeType) {
        GraphNodeData nodeData = new GraphNodeData();
        nodeData.setId(new ObjectId().toString());
        nodeData.setLabel(triple.getLabel());
        nodeData.setGroup(graphNodeType.getGroupId());
        nodeData.setUri(triple.getUri());
        return nodeData;
    }
}
