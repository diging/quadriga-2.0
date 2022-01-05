package edu.asu.diging.quadriga.core.model;

import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * A MappedTripleGroup is used to map Concepts and Predicates to a collection
 * One CollectionId can have multiple MappedTripleGroups
 * A new MappedTripleGroup is created per network
 * Every MappedTripleGroup will have MappedTripleType which specifies mapping type
 * Once a network is submitted by a user, the user can give a name to the mapped collection
 * 
 * MappedTriple Group examples
 * 
 * Collection C1 -
 * M1 => C1 + DefaultMapping
 * M2 => C1 + DefaultMapping
 * M3 => C1 + CustomMapping1
 * M4 => C1 + CustomMapping2
 * 
 * Collection C2 -
 * M5 => C2 + DefaultMapping
 * M6 => C2 + CustomMapping3
 * 
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
