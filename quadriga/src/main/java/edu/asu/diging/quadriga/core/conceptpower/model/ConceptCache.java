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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * This class represents the 'conceptpower_concept_cache' table present in the
 * database that stores cached concept data
 * 
 * @author Digital Innovation Group
 *
 */
@Entity
@Table(name = "conceptpower_concept_cache")
public class ConceptCache implements Serializable, Comparable<ConceptCache> {

    private static final long serialVersionUID = 1L;

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
    
    /**
     * Compares the old and new cache values to determine if a difference exists.
     * 
     *If both old and new cache values are {@code null}, blank, or empty, no difference is present.
     *If the old value is {@code null}, blank, or empty, and the new value is not {@code null}, blank, or empty, a difference is present.
     *If the old value is not {@code null}, blank, or empty, and the new value is {@code null}, blank, or empty, a difference is present.
     *If both values are not {@code null}, blank, or empty, their content must be compared to determine if a difference exists.
     *
     */
    @Override
    public int compareTo(ConceptCache conceptCache) {
        
        if(isDifferentList(conceptCache.getAlternativeUris(), this.getAlternativeUris())) {
            return -1;
        }
        if(isDifferentList(conceptCache.getEqualTo(), this.getEqualTo())) {
            return -1;
        }
        if(isDifferentList(conceptCache.getWordNetIds(), this.getWordNetIds())) {
            return -1;
        }
        if (isDifferentString(conceptCache.getConceptList(), this.getConceptList())) {
            return -1;
        }
        if (isDifferentString(conceptCache.getCreatorId(), this.getCreatorId())) {
            return -1;
        }
        if (isDifferentString(conceptCache.getDescription(), this.getDescription())) {
            return -1;
        }
        if (isDifferentString(conceptCache.getId(), this.getId())) {
            return -1;
        }
        if (isDifferentString(conceptCache.getPos(), this.getPos())) {
            return -1;
        }
        if (isDifferentString(conceptCache.getTypeId(), this.getTypeId())) {
            return -1;
        }
        if (isDifferentString(conceptCache.getUri(), this.getUri())) {
            return -1;
        }
        if (isDifferentString(conceptCache.getWord(), this.getWord())) {
            return -1;
        }
        return 0;
    }
    
    private static boolean isDifferentString(String str1, String str2) {
        if(!(StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)) 
                && (StringUtils.isEmpty(str1) || !str1.equals(str2))) {
            return true;
        }
        return false;
    }

    private static boolean isDifferentList(List<String> list1, List<String> list2) {
        if (!(isNullOrEmpty(list1) && isNullOrEmpty(list2) 
                && (isNullOrEmpty(list1) || !list1.equals(list2)))) {
            return true;
        }
        return false;
    }
    
    private static boolean isNullOrEmpty(List<String> list) {
        return list == null || list.isEmpty();
    }

}
