package edu.asu.diging.quadriga.core.model;

import java.util.List;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

public class Collection {

    private ObjectId _id;
    private String name;
    private String description;
    private List<CitesphereAppInfo> apps;

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
    
    public List<CitesphereAppInfo> getApps() {
        return apps;
    }

    public void setApps(List<CitesphereAppInfo> apps) {
        this.apps = apps;
    }

}
