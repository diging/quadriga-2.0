package edu.asu.diging.quadriga.web.model;

public enum GraphNodeType {
    
    PREDICATE(0),
    SUBJECT(1),
    OBJECT(1);
    
    private int groupId;

    private GraphNodeType(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }
    
}
