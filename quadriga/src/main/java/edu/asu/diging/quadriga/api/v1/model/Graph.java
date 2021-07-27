package edu.asu.diging.quadriga.api.v1.model;

import java.util.List;
import java.util.Map;

public class Graph {

	private Metadata metadata;
	private Map<String, NodeData> nodes;
	private List<Edge> edges;
	
	public Metadata getMetadata() {
        return metadata;
    }
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
    public Map<String, NodeData> getNodes() {
        return nodes;
    }
    public void setNodes(Map<String, NodeData> nodes) {
        this.nodes = nodes;
    }
    public List<Edge> getEdges() {
        return edges;
    }
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
    
}
