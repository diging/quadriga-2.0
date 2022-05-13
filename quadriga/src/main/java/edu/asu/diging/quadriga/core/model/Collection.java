package edu.asu.diging.quadriga.core.model;

import java.util.List;

import javax.persistence.Id;

import org.bson.types.ObjectId;

public class Collection {

    @Id
    private ObjectId _id;
    
    private String name;
    private String description;
    private String username;
    
    /**
     * List of Client Ids for Citesphere Apps which are associated with the collection
     */
    private List<String> apps;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
