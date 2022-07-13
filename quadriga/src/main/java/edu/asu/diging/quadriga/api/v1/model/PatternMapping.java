package edu.asu.diging.quadriga.api.v1.model;

import java.util.List;
import java.util.Map;

public class PatternMapping {

    private String mappedTripleGroupId;
    private Metadata metadata;
    private Map<String, PatternNodeData> nodes;
    private List<Edge> edges;
    
    public String getMappedTripleGroupId() {
        return mappedTripleGroupId;
    }
    public void setMappedTripleGroupId(String mappedTripleGroupId) {
        this.mappedTripleGroupId = mappedTripleGroupId;
    }
    public Metadata getMetadata() {
        return metadata;
    }
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
    public Map<String, PatternNodeData> getNodes() {
        return nodes;
    }
    public void setNodes(Map<String, PatternNodeData> nodes) {
        this.nodes = nodes;
    }
    public List<Edge> getEdges() {
        return edges;
    }
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}
