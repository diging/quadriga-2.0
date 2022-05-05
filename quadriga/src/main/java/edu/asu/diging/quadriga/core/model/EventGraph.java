package edu.asu.diging.quadriga.core.model;

import java.time.OffsetDateTime;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.model.events.CreationEvent;

public class EventGraph {

    private ObjectId _id;
    private CreationEvent  rootEvent;
    private DefaultMapping defaultMapping;
    private OffsetDateTime creationTime;
    private ObjectId collectionId;
    private String appName;
    private Context context;
    private String submittingApp;
    
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

    public DefaultMapping getDefaultMapping() {
        return defaultMapping;
    }

    public void setDefaultMapping(DefaultMapping defaultMapping) {
        this.defaultMapping = defaultMapping;
    }
    
    public OffsetDateTime getCreationTime() {
        return creationTime;
    }
    
    public void setCreationTime(OffsetDateTime creationTime) {
        this.creationTime = creationTime;
    }
    
    public ObjectId getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(ObjectId collectionId) {
        this.collectionId = collectionId;
    }
    
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    
}
