package edu.asu.diging.quadriga.web.model;

import java.util.List;

public class GraphNodeData extends GraphData {
    
    private int group;
    private String label;
    private String description;
    private String uri;
    private List<String> eventGraphIds;
    private List<String> alternativeUris;
    
    public int getGroup() {
        return group;
    }
    public void setGroup(int group) {
        this.group = group;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public List<String> getEventGraphIds() {
        return eventGraphIds;
    }
    public void setEventGraphIds(List<String> eventGraphIds) {
        this.eventGraphIds = eventGraphIds;
    }
    public List<String> getAlternativeUris() {
        return alternativeUris;
    }
    public void setAlternativeUris(List<String> alternativeUris) {
        this.alternativeUris = alternativeUris;
    }

}
