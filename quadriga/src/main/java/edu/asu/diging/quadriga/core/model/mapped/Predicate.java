package edu.asu.diging.quadriga.core.model.mapped;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Predicate {
    
    @Id 
    @GeneratedValue
    private Long id;

    private String relationship;
    private String label;
    
    private String mappedTripleGroupId;
    
    @Relationship(direction = Direction.OUTGOING)
    private Concept source;

    @TargetNode
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

    public String getMappedTripleGroupId() {
        return mappedTripleGroupId;
    }

    public void setMappedTripleGroupId(String mappedTripleGroupId) {
        this.mappedTripleGroupId = mappedTripleGroupId;
    }
    
}
