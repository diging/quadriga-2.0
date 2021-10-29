package edu.asu.diging.quadriga.core.model.mapped;

import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Concept {

    @Id 
    @GeneratedValue
    private Long id;
    
    private String uri;
    private String label;
    
    private String mappedCollectionId;
    private String linkedEventGraphId;
    private String mappingType;
    
    @Relationship(type = "PREDICATE")
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

    public String getMappedCollectionId() {
        return mappedCollectionId;
    }

    public void setMappedCollectionId(String mappedCollectionId) {
        this.mappedCollectionId = mappedCollectionId;
    }

    public String getLinkedEventGraphId() {
        return linkedEventGraphId;
    }

    public void setLinkedEventGraphId(String linkedEventGraphId) {
        this.linkedEventGraphId = linkedEventGraphId;
    }

    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }
    
}
