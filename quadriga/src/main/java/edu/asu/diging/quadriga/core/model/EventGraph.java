package edu.asu.diging.quadriga.core.model;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.model.events.CreationEvent;

public class EventGraph {

    private ObjectId _id;
    private CreationEvent  rootEvent;
    
    public EventGraph() {}
    
    public EventGraph(CreationEvent root) {
        this.rootEvent = root;
    }
    
    public ObjectId getId() {
        return _id;
    }
    
    public void setId(ObjectId id) {
        this._id = id;
    }

    public CreationEvent getRootEvent() {
        return rootEvent;
    }

    public void setRootEvent(CreationEvent rootEvent) {
        this.rootEvent = rootEvent;
    }
    
    
}
