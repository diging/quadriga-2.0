package edu.asu.diging.quadriga.core.model;

import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * A mapped collection is used to map Concepts and Predicates to a collection
 * Once a network is submitted by a user, the user can give a name to the mapped collection
 * 
 * @author poojakulkarni
 *
 */
public class MappedTripleGroup {
    
    @Id
    private ObjectId _id;
    
    private String name;
    private ObjectId collectionId;
    private MappedTripleType mappedTripleType;
    
    public ObjectId get_id() {
        return _id;
    }
    
    public void set_id(ObjectId _id) {
        this._id = _id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ObjectId getCollectionId() {
        return collectionId;
    }
    
    public void setCollectionId(ObjectId collectionId) {
        this.collectionId = collectionId;
    }

    public MappedTripleType getMappedTripleType() {
        return mappedTripleType;
    }

    public void setMappedTripleType(MappedTripleType mappedTripleType) {
        this.mappedTripleType = mappedTripleType;
    }

}
