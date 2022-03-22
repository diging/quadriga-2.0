package edu.asu.diging.quadriga.core.model;

import java.time.OffsetDateTime;
import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * Represents a Collection of networks
 * Creation time in the collection will be stored in UTC
 *
 */
public class Collection {

    @Id
    private ObjectId _id;
    
    private String name;
    private String description;
    private OffsetDateTime creationTime;

    public Collection() {
        this.creationTime = OffsetDateTime.now();
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
    
    public OffsetDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(OffsetDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
