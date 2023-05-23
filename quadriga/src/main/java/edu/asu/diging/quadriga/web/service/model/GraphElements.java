package edu.asu.diging.quadriga.web.service.model;

import java.util.List;

public class GraphElements {
    
    private List<GraphElement> nodes;
    private List<GraphElement> edges;
    
    public List<GraphElement> getNodes() {
        return nodes;
    }
    public void setNodes(List<GraphElement> nodes) {
        this.nodes = nodes;
    }
    public List<GraphElement> getEdges() {
        return edges;
    }
    public void setEdges(List<GraphElement> edges) {
        this.edges = edges;
    }

}