package edu.asu.diging.quadriga.core.conceptpower.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "conceptpower_concept_type_cache")
public class ConceptType implements Serializable, Comparable<ConceptType> {

    /**
     * 
     */
    private static final long serialVersionUID = 4228339422371096758L;

    @Id
    private String id;
    private String name;
    private String uri;

    @Lob
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(ConceptType type) {
        if (type == null) {
            return -1;
        }
        if (!this.getId().equals(type.getId())) {
            return -1;
        }
        if (!this.getDescription().equals(type.getDescription())) {
            return -1;
        }
        if (!this.getName().equals(type.getName())) {
            return -1;
        }
        if (!this.getUri().equals(type.getUri())) {
            return -1;
        }
        return 0;
    }

}
