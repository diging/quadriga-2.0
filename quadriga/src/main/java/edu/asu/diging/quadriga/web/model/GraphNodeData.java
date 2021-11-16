package edu.asu.diging.quadriga.web.model;

import java.util.List;

public class GraphNodeData extends GraphData {
    
    private int group;
    private String label;
    private String description;
    private List<String> eventGraphIds;
    
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
    public List<String> getEventGraphIds() {
        return eventGraphIds;
    }
    public void setEventGraphId(List<String> eventGraphIds) {
        this.eventGraphIds = eventGraphIds;
    }

}