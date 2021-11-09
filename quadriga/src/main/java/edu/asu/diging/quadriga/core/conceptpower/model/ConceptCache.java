package edu.asu.diging.quadriga.core.conceptpower.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name =  "conceptpower_concept_cache")
public class ConceptCache implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6260562492095202271L;
    
    @Id
    private String uri;
    private String id;
    private String word;
    
    @Lob
    private String description;
    
    private String conceptList;
    private String typeId;
    private LocalDateTime lastUpdated;
    
    @ElementCollection
    @CollectionTable(name = "conceptpower_alternative_uris")
    private List<String> alternativeUris;
    
    @ElementCollection
    @CollectionTable(name = "conceptpower_equal_to")
    private List<String> equalTo;
    
    @ElementCollection
    @CollectionTable(name = "conceptpower_wordnetids")
    private List<String> wordNetIds;
    
    private String creatorId;
    
    @Transient
    private ConceptType conceptType;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConceptList() {
        return conceptList;
    }

    public void setConceptList(String conceptList) {
        this.conceptList = conceptList;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<String> getAlternativeUris() {
        return alternativeUris;
    }

    public void setAlternativeUris(List<String> alternativeUris) {
        this.alternativeUris = alternativeUris;
    }

    public List<String> getEqualTo() {
        return equalTo;
    }

    public void setEqualTo(List<String> equalTo) {
        this.equalTo = equalTo;
    }

    public List<String> getWordNetIds() {
        return wordNetIds;
    }

    public void setWordNetIds(List<String> wordNetIds) {
        this.wordNetIds = wordNetIds;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public ConceptType getConceptType() {
        return conceptType;
    }

    public void setConceptType(ConceptType conceptType) {
        this.conceptType = conceptType;
    }

}
