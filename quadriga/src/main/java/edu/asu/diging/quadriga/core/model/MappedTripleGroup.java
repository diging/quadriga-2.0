package edu.asu.diging.quadriga.core.model;

import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * A MappedTripleGroup will group together all mappings of a specific mapping type
 * per collection
 * 
 * E.g. For Collection C1 a MappedTripleGroup M1 will be created for DefaultMapping,
 * another MappedTripleGroup M2 will be created for CustomMapping, etc.
 * 
 * This MappedTripleGroupId will be added to the triple's concepts and predicates
 * 
 * This MappedTripleGroup can also be named by the user later
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
