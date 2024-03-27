package edu.asu.diging.quadriga.web.service.model;

import java.util.List;

public class GraphElements {
    
    private List<GraphData> nodes;
    private List<GraphData> edges;
    
    public List<GraphData> getNodes() {
        return nodes;
    }
    public void setNodes(List<GraphData> nodes) {
        this.nodes = nodes;
    }
    public List<GraphData> getEdges() {
        return edges;
    }
    public void setEdges(List<GraphData> edges) {
        this.edges = edges;
    }

}