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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * This class represents the 'conceptpower_concept_cache' table present in the
 * database that stores cached concept data
 * 
 * @author poojakulkarni
 *
 */
@Entity
@Table(name = "conceptpower_concept_cache")
public class ConceptCache implements Serializable, Comparable<ConceptCache> {

    /**
     * 
     */
    private static final long serialVersionUID = 6260562492095202271L;

    @Id
    private String uri;
    private String id;
    private String word;
    private String pos;

    @Lob
    private String description;

    private String conceptList;
    private String typeId;
    private boolean deleted;
    private LocalDateTime lastUpdated;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "conceptpower_alternative_uris")
    private List<String> alternativeUris;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "conceptpower_equal_to")
    private List<String> equalTo;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "conceptpower_wordnetids")
    private List<String> wordNetIds;

    private String creatorId;

    @Transient
    private ConceptType conceptType;

    public ConceptCache() {
        this.lastUpdated = LocalDateTime.now();
    }

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

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public int compareTo(ConceptCache conceptCache) {
        if (this.getAlternativeUris() == null) {
            return -1;
        } else {
            if(this.getAlternativeUris().size() != conceptCache.getAlternativeUris().size()) return -1;
            for(String alternativeUri: this.getAlternativeUris()) {
                if(!conceptCache.getAlternativeUris().contains(alternativeUri)) {
                    return -1;
                }
            }
        }
        // Figure this out
        if (this.getConceptList() == null && conceptCache.getConceptList() != null) {
            return -1;
        }
        if (this.getCreatorId() == null || !this.getCreatorId().equals(conceptCache.getCreatorId())) {
            return -1;
        }
        if (this.getDescription() == null || !this.getDescription().equals(conceptCache.getDescription())) {
            return -1;
        }
        if (this.getEqualTo() == null || !this.getEqualTo().equals(conceptCache.getEqualTo())) {
            return -1;
        }
        if (this.getId() == null || !this.getId().equals(conceptCache.getId())) {
            return -1;
        }
        if (this.getPos() == null || !this.getPos().equals(conceptCache.getPos())) {
            return -1;
        }
        if (this.getTypeId() == null || !this.getTypeId().equals(conceptCache.getTypeId())) {
            return -1;
        }
        if (this.getUri() == null || !this.getUri().equals(conceptCache.getUri())) {
            return -1;
        }
        if (this.getWord() == null || !this.getWord().equals(conceptCache.getWord())) {
            return -1;
        }
        if (this.getWordNetIds() == null || !this.getWordNetIds().equals(conceptCache.getWordNetIds())) {
            return -1;
        }
        return 0;
    }

}
