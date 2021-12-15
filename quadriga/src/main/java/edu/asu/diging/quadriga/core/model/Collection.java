package edu.asu.diging.quadriga.core.model;

import java.util.List;
import java.time.LocalDateTime;

import javax.persistence.Id;

import org.bson.types.ObjectId;

public class Collection {

    @Id
    private ObjectId _id;
    
    private String name;
    private String description;
    
    private List<String> apps;

    // Time will be stored in UTC
    private LocalDateTime creationTime;
    
    public Collection() {
        this.creationTime = LocalDateTime.now();
    }
    
    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;

    }
    
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

}
