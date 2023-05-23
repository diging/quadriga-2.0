package edu.asu.diging.quadriga.web.service.model;

public enum GraphNodeType {
    
    PREDICATE(0),
    SUBJECT(1),
    OBJECT(1);
    
    /**
     * Corresponds to the group in GraphNodeData. 
     */
    private int groupId;

    private GraphNodeType(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }
    
}
