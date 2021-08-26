package edu.asu.diging.quadriga.core.model.mapped;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;

@RelationshipEntity(type = "PREDICATE")
public class Predicate {
    
    @Id 
    @GeneratedValue
    private Long id;

    private String relationship;
    private String label;
    
    @StartNode
    private Concept source;

    @EndNode
    private Concept target;

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public Concept getSource() {
        return source;
    }

    public void setSource(Concept source) {
        this.source = source;
    }

    public Concept getTarget() {
        return target;
    }

    public void setTarget(Concept target) {
        this.target = target;
    }
}