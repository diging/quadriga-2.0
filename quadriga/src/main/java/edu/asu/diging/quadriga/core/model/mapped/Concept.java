package edu.asu.diging.quadriga.core.model.mapped;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@Node
public class Concept {

    @Id 
    @GeneratedValue
    private Long id;
    
    private String uri;
    private String label;
    
    private String mappedTripleGroupId;
    
    @Relationship(type = "PREDICATE", direction=Direction.OUTGOING)
    private List<Concept> relatedConcepts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Concept> getRelatedConcepts() {
        return relatedConcepts;
    }

    public void setRelatedConcepts(List<Concept> relatedConcepts) {
        this.relatedConcepts = relatedConcepts;
    }

    public String getMappedTripleGroupId() {
        return mappedTripleGroupId;
    }

    public void setMappedTripleGroupId(String mappedTripleGroupId) {
        this.mappedTripleGroupId = mappedTripleGroupId;
    }
    
}
