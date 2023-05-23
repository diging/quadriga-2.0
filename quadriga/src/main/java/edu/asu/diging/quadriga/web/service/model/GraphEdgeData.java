package edu.asu.diging.quadriga.web.service.model;

public class GraphEdgeData extends GraphData {
    
    private String source;
    private String target;
    private String eventGraphId;
    
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String getEventGraphId() {
        return eventGraphId;
    }
    public void setEventGraphId(String eventGraphId) {
        this.eventGraphId = eventGraphId;
    }

}
