package edu.asu.diging.quadriga.core.conceptpower.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

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
        // If both old and new types are null/blank, nothing has changed
        // If old value is null/blank, new value is not null/blank, difference present
        // If old value is not null/blank, new value is null/blank, difference present
        // If both are not null/blank, we need to check difference
        
        if (type == null ) return -1;
        if(isDifferentString(type.getId(), this.getId())) return -1;
        if(isDifferentString(type.getDescription(), this.getDescription())) return -1;
        if(isDifferentString(type.getName(), this.getName())) return -1;
        if(isDifferentString(type.getUri(), this.getUri())) return -1;
        return 0;
    }
    
    private static boolean isDifferentString(String str1, String str2) {
        if(!(StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)) 
                && (StringUtils.isEmpty(str1) || !str1.equals(str2))) {
            return true;
        }
        return false;
    }

}
