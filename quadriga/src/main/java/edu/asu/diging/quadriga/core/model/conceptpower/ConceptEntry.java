package edu.asu.diging.quadriga.core.model.conceptpower;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConceptEntry {

    private String id;
    @JsonProperty("concept_uri")
    private String conceptUri;
    private String lemma;
    private String pos;
    private String description;
    @JsonProperty("creator_id")
    private String creatorId;

    private String conceptList;
    private boolean deleted;
    @JsonProperty("equal_to")
    private String equalTo;
    @JsonProperty("similar_to")
    private String similarTo;
    @JsonProperty("synonym_ids")
    private String synonymIds;

    private ConceptpowerConceptType type;
    private List<ConceptpowerAlternativeId> alternativeIds;

    public String getId() {
        return id;
    }

    public String getConceptId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConceptUri() {
        return conceptUri;
    }

    public void setConceptUri(String conceptUri) {
        this.conceptUri = conceptUri;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<String> getEqualTo() {
        if (equalTo == null) {
            return null;
        }
        return Arrays.asList(equalTo.split(","));
    }

    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
    }

    public List<String> getSimilarTo() {
        if (similarTo == null) {
            return null;
        }
        return Arrays.asList(similarTo.split(","));
    }

    public void setSimilarTo(String similarTo) {
        this.similarTo = similarTo;
    }

    public String getSynonymIds() {
        return synonymIds;
    }

    public void setSynonymIds(String synonymIds) {
        this.synonymIds = synonymIds;
    }

    public ConceptpowerConceptType getType() {
        return type;
    }

    public void setType(ConceptpowerConceptType type) {
        this.type = type;
    }

    public List<ConceptpowerAlternativeId> getAlternativeIds() {
        return alternativeIds;
    }

    public void setAlternativeIds(List<ConceptpowerAlternativeId> alternativeIds) {
        this.alternativeIds = alternativeIds;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

}
